package hibernate.util;

import hibernate.DAO.AmountDAO;
import hibernate.DAO.Impl.AmountDAOImpl;

public class Factory {

    private static class FactoryHolder {
        private final static Factory instance = new Factory();
    }

    private static class AmountDAOHolder {
        private final static AmountDAO amountDAO = new AmountDAOImpl();
    }

    public static Factory getInstance() {
        return FactoryHolder.instance;
    }

    public AmountDAO getAmountDAO() {
        return AmountDAOHolder.amountDAO;
    }

}
