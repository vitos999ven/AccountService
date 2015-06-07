package service;

import hibernate.Exceptions.AmountException;
import hibernate.logic.Amount;
import hibernate.util.Factory;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountServiceImpl extends UnicastRemoteObject implements AccountService {

    private final Map<Integer, Long> cash;
    private final MutexProvider mutexProvider;
    private final Mutex getAmountInc;
    private final Mutex addAmountInc;
    private final List<Long> ReqsForGetAmount;
    private final List<Long> ReqsForAddAmount;

    public AccountServiceImpl() throws RemoteException {
        this.cash = new HashMap<>();
        this.mutexProvider = new MutexProvider();
        this.addAmountInc = new Mutex(0);
        this.getAmountInc = new Mutex(0);
        this.ReqsForAddAmount = new LinkedList<>();
        this.ReqsForGetAmount = new LinkedList<>();
    }

    @Override
    public Long getAmount(Integer id) throws RemoteException, AmountException {
        synchronized (getAmountInc) {
            getAmountInc.increment();
            addNewReqInList(ReqsForGetAmount);
        }
        try {
            Mutex mutex = mutexProvider.getMutex(id);
            synchronized (mutex) {
                Long value = cash.get(id);
                if (value != null) {
                    return value;
                }
                Amount amount = Factory.getInstance().getAmountDAO().getAmount(id);
                if (amount != null) {
                    cash.put(amount.getId(), amount.getValue());
                } else {
                    throw new AmountException("Operation: GET, Amount doesn't exist!", id);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new AmountException("SQLException has been throwed!", id);
        }
        return (long) 0;
    }

    @Override
    public void addAmount(Integer id, Long value) throws RemoteException, AmountException {
        synchronized (addAmountInc) {
            addAmountInc.increment();
            addNewReqInList(ReqsForAddAmount);
        }
        try {
            Mutex mutex = mutexProvider.getMutex(id);
            synchronized (mutex) {
                cash.put(id, value);
                try {
                    Factory.getInstance().getAmountDAO().addAmount(new Amount(id, value));
                } catch (AmountException ex) {
                    Factory.getInstance().getAmountDAO().updateAmount(id, value);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new AmountException("SQLException has been throwed!", id);
        }
    }

    public int getLastReqNumForAddAmount() {
        synchronized (addAmountInc) {
        return ReqsForAddAmount.size();
        }
    }

    public int getLastReqNumForGetAmount() {
        synchronized (getAmountInc) {
        return ReqsForGetAmount.size();
        }
    }
    
    public int getIncrementForAddAmount() {
        synchronized (addAmountInc) {
            return addAmountInc.getValue();
        }
    }

    public int getIncrementForGetAmount() {
        synchronized (getAmountInc) {
        return getAmountInc.getValue();
        }
    }

    public void resetInc() {
        synchronized (addAmountInc) {
            addAmountInc.setValue(0);
        }
        synchronized (getAmountInc) {
            getAmountInc.setValue(0);
        }
    }

    private void addNewReqInList(List<Long> list) {
        long currentTime = System.currentTimeMillis();
        Iterator<Long> iter = list.iterator();
        while (iter.hasNext()) {
            if ((currentTime - iter.next()) > 1000) {
                iter.remove();
            } else {
                break;
            }
        }
        list.add(currentTime);
    }

}
