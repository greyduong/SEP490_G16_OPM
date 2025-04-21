package model;

public class WalletTopupHistory {
    private long transactionID;
    private int userID;
    private long amount;
    private String txnRef;
    private String status;

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

    public String getTxnRef() {
        return txnRef;
    }

    public void setTxnRef(String txnRef) {
        this.txnRef = txnRef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}