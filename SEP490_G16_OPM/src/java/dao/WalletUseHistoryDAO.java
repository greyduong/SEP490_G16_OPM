package dao;

import dal.DBContext;
import model.Page;
import model.User;
import model.WalletUseHistory;

public class WalletUseHistoryDAO extends DBContext {
    public void create(WalletUseHistory history) {
        executeUpdate("INSERT INTO WalletUseHistory(UserID, Amount) VALUES (?, ?)", history.getUserID(), history.getAmount());
    }
    public Page<WalletUseHistory> getAll(int userID, int pageNumber) {
        Page<WalletUseHistory> page = new Page<>();
        int itemsPerPage = 5;
        long offset = (pageNumber - 1) * itemsPerPage;
        page.setPageNumber(pageNumber);
        var data = fetchAll(rs -> {
            WalletUseHistory h = new WalletUseHistory();
            h.setTransactionID(rs.getLong("ID"));
            return h;
        }, "SELECT * FROM WalletTopupHistory WHERE UserID = ? ORDER BY CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", userID, offset, itemsPerPage);
        int totalItems = count("SELECT COUNT(*) FROM WalletTopupHistory WHERE UserID = ?", userID);
        int totalPages = totalItems / itemsPerPage + 1;
        page.setData(data);
        page.setPageSize(itemsPerPage);
        page.setTotalData(totalItems);
        page.setTotalPage(totalPages);
        return page;
    }

	public boolean use(int userID, long amount) {
		User user = fetchOne(rs -> {
			User u = new User();
			u.setWallet(rs.getDouble("Wallet"));
			return u;
		}, "SELECT * FROM UserAccount WHERE UserID = ?", userID);
		// không tồn tại user
		if (user == null) return false;
		double wallet = user.getWallet();
		// không đủ tiền
		if (wallet < amount) {
			return false;
		}
		// cập nhật tiền
		executeUpdate("UPDATE UserAccount SET Wallet = Wallet - ? WHERE UserID = ?", amount, userID);
		// tạo lịch sử giao dịch
		WalletUseHistory h = new WalletUseHistory();
		h.setUserID(userID);
		h.setAmount(amount);
		create(h);
		return true;
	}
}
