package hibernate.DAO.Impl;

import hibernate.DAO.AmountDAO;
import hibernate.Exceptions.AmountException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import hibernate.logic.Amount;
import org.hibernate.Query;
import org.hibernate.Session;
import hibernate.util.HibernateUtil;
import org.hibernate.HibernateException;

public class AmountDAOImpl implements AmountDAO {

    @Override
    public void addAmount(Amount amount) throws SQLException, AmountException {
        Session session = null;
        if (getAmount(amount.getId()) != null){
            throw new AmountException("Operation: ADD, Amount already exists!", amount.getId());
        }
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(amount);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println(e.toString());
            StackTraceElement[] el = e.getStackTrace();
            for (StackTraceElement el1 : el) {
                System.out.println(el1);
            }
        } finally {
            if ((session != null) && (session.isOpen())) {
                session.close();
            }
        }
    }

    @Override
    public void updateAmount(int id, long value) throws SQLException, AmountException {
        Session session = null;
        if (getAmount(id) == null){
            throw new AmountException("Operation: UPDATE, Amount doesn't exist!", id);
        }
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(new Amount(id, value));
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println(e.toString());
            StackTraceElement[] el = e.getStackTrace();
            for (StackTraceElement el1 : el) {
                System.out.println(el1);
            }
        } finally {
            if ((session != null) && (session.isOpen())) {
                session.close();
            }
        }
    }

    @Override
    public Amount getAmount(int id) throws SQLException{
        Session session = null;
        Amount amount = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createSQLQuery(
                    "SELECT * FROM ammounts a WHERE a.id = :id"
            ).addEntity(Amount.class).setInteger("id", id);
            if (!query.list().isEmpty()) {
                amount = (Amount) query.list().get(0);
            } 
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println(e.toString());
            StackTraceElement[] el = e.getStackTrace();
            for (StackTraceElement el1 : el) {
                System.out.println(el1);
            }
        } finally {
            if ((session != null) && (session.isOpen())) {
                session.close();
            }
        }
        return amount;
    }

    @Override
    public List<Amount> getAllAmounts() throws SQLException {
        Session session = null;
        List<Amount> amounts = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            amounts = session.createCriteria(Amount.class).list();
        } catch (HibernateException e) {
            System.out.println(e.toString());
            StackTraceElement[] el = e.getStackTrace();
            for (StackTraceElement el1 : el) {
                System.out.println(el1);
            }
        } finally {
            if ((session != null) && (session.isOpen())) {
                session.close();
            }
        }
        return amounts;
    }

    @Override
    public void deleteAmount(int id) throws SQLException, AmountException {
        Session session = null;
        Amount amount = getAmount(id);
        if (amount == null) {
            throw new AmountException("Operation: DELETE, Amount doesn't exist!", id);
        }
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(amount);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println(e.toString());
            StackTraceElement[] el = e.getStackTrace();
            for (StackTraceElement el1 : el) {
                System.out.println(el1);
            }
        } finally {
            if ((session != null) && (session.isOpen())) {
                session.close();
            }
        }
    }

    @Override
    public void deleteAllAmounts() throws SQLException {
        Session session = null;
        List<Amount> amounts = getAllAmounts();
        if (amounts.isEmpty()) {
            return;
        }
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            for (Amount amount : amounts) {
                session.delete(amount);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println(e.toString());
            StackTraceElement[] el = e.getStackTrace();
            for (StackTraceElement el1 : el) {
                System.out.println(el1);
            }
        } finally {
            if ((session != null) && (session.isOpen())) {
                session.close();
            }
        }
    }

}
