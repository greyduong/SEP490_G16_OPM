package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WalletUseHistory {
    private long transactionID;
    private int userID;
    private long amount;
	private String note;
    private Timestamp createdAt;

    public long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

	public String getCreatedAtAsString(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(createdAt.getTime()));
    }

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
