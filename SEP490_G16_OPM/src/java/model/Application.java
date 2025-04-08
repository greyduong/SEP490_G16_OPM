package model;

import java.util.Date;

public class Application {

    private int applicationID;
    private int userID;
    private String content;
    private String reply;
    private String status; // The status of the application (e.g., Pending, Approved, Rejected)
    private Date sentAt;   // The date when the application was sent
    private Date processingDate; // The date when the application was processed (if applicable)
    private String file; // The file associated with the application (e.g., file name or URL)

    // Constructors
    public Application() {
    }

    // Constructor with auto-generated applicationID
    public Application(int userID, String content, String reply, String status, Date sentAt, Date processingDate, String file) {
        this.userID = userID;
        this.content = content;
        this.reply = reply;
        this.status = status;
        this.sentAt = sentAt;
        this.processingDate = processingDate;
        this.file = file;
    }

    // Getters and Setters
    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public Date getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    // toString method for debugging or logging purposes
    @Override
    public String toString() {
        return "Application{"
                + "applicationID=" + applicationID
                + ", userID=" + userID
                + ", content='" + content + '\''
                + ", reply='" + reply + '\''
                + ", status='" + status + '\''
                + ", sentAt=" + sentAt
                + ", processingDate=" + processingDate
                + ", file='" + file + '\''
                + '}';
    }
}
