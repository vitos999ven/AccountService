package service;

import hibernate.Exceptions.AmountException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AccountService extends Remote {

    Long getAmount(Integer id) throws RemoteException, AmountException;
    
    void addAmount(Integer id, Long value) throws RemoteException, AmountException;
}
