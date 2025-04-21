package dao;

import dal.DBContext;
import java.util.Optional;
import model.WalletTopupHistory;

public class WalletTopupHistoryDAO extends DBContext {

    public Optional<WalletTopupHistory> getByTxnRef(String txnRef) {
        return Optional.ofNullable(fetchOne(mapper(), "SELECT * FROM WalletTopupHistory WHERE TxnRef = ?", txnRef));
    }
    
    public void updateStatusByTxnRef(String txnRef, String status) {
        update("UPDATE WalletTopupHistory SET Status = ? WHERE TxnRef = ?", status, txnRef);
    }
    
    public void create(WalletTopupHistory history) {
        insert("INSERT INTO WalletTopupHistory(UserID, Amount, TxnRef, Status) VALUES (?, ?, ?, ?)",
                history.getUserID(),
                history.getAmount(),
                history.getTxnRef(),
                history.getStatus());
    }

    public static Mapper<WalletTopupHistory> mapper() {
        return (rs) -> {
            WalletTopupHistory his = new WalletTopupHistory();
            his.setAmount(rs.getLong("Amount"));
            his.setStatus(rs.getString("Status"));
            his.setTxnRef(rs.getString("TxnRef"));
            his.setUserID(rs.getInt("UserID"));
            return his;
        };
    }
}
