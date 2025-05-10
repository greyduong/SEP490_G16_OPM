package dao;

import dal.DBContext;
import java.util.Optional;
import model.Page;
import model.WalletTopupHistory;

public class WalletTopupHistoryDAO extends DBContext {

    public Optional<WalletTopupHistory> getByTxnRef(String txnRef) {
        return Optional.ofNullable(fetchOne(mapper(), "SELECT * FROM WalletTopupHistory WHERE TxnRef = ?", txnRef));
    }
    
    public Page<WalletTopupHistory> getAll(int userID, int pageNumber) {
        Page<WalletTopupHistory> page = new Page<>();
        int itemsPerPage = 5;
        long offset = (pageNumber - 1) * itemsPerPage;
        page.setPageNumber(pageNumber);
        var data = fetchAll(mapper(), "SELECT * FROM WalletTopupHistory WHERE UserID = ? ORDER BY CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", userID, offset, itemsPerPage);
        int totalItems = count("SELECT COUNT(*) FROM WalletTopupHistory WHERE UserID = ?", userID);
        int totalPages = totalItems / itemsPerPage + 1;
        page.setData(data);
        page.setPageSize(itemsPerPage);
        page.setTotalData(totalItems);
        page.setTotalPage(totalPages);
        return page;
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
            his.setCreatedAt(rs.getTimestamp("CreatedAt"));
            return his;
        };
    }
}
