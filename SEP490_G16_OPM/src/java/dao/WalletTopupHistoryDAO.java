package dao;

import dal.DBContext;
import dal.Mapper;
import java.util.Optional;
import model.WalletTopupHistory;

public class WalletTopupHistoryDAO extends DBContext {

    public Optional<WalletTopupHistory> getByTxnRef(String txnRef) {
        return Optional.ofNullable(fetchOne(mapper(), "", txnRef));
    }
    
    public void updateStatusByTxnRef(String txnRef, String status) {
        update("UPDATE WalletTopupHistory SET Status = ? WHERE TxnRef = ?", status, txnRef);
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
