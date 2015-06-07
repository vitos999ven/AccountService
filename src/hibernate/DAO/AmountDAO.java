package hibernate.DAO;

import hibernate.Exceptions.AmountException;
import java.sql.SQLException;
import java.util.List;
import hibernate.logic.Amount;

public interface AmountDAO {

    public void addAmount(Amount amount) throws SQLException, AmountException;
    public void updateAmount(int id, long value) throws SQLException, AmountException;
    public Amount getAmount(int id) throws SQLException;
    public List<Amount> getAllAmounts() throws SQLException;
    public void deleteAmount(int id) throws SQLException, AmountException;
    public void deleteAllAmounts() throws SQLException;
    
}
