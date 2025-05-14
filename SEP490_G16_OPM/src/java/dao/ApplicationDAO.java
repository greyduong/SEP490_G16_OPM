package dao;

import dal.DBContext;
import model.Application;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    // Get all applications
    public List<Application> getAllApplications() {
        List<Application> applications = new ArrayList<>();
        String query = "SELECT * FROM Application";
        try {
            stm = connection.prepareStatement(query);
            rs = stm.executeQuery();
            while (rs.next()) {
                Application application = new Application();
                application.setApplicationID(rs.getInt("ApplicationID"));
                application.setUserID(rs.getInt("UserID"));
                application.setContent(rs.getString("Content"));
                application.setReply(rs.getString("Reply"));
                application.setStatus(rs.getString("Status"));
                application.setSentAt(rs.getTimestamp("SentAt"));
                application.setProcessingDate(rs.getTimestamp("ProcessingDate"));
                application.setFile(rs.getString("FilePath"));
                applications.add(application);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return applications;
    }

    // Get applications by user ID
    public List<Application> getByUserId(int userId) {
        List<Application> list = new ArrayList<>();
        String query = "SELECT * FROM Application WHERE UserID = ? ORDER BY SentAt DESC";
        try {
            stm = connection.prepareStatement(query);
            stm.setInt(1, userId);
            rs = stm.executeQuery();
            while (rs.next()) {
                Application application = new Application();
                application.setApplicationID(rs.getInt("ApplicationID"));
                application.setUserID(rs.getInt("UserID"));
                application.setContent(rs.getString("Content"));
                application.setReply(rs.getString("Reply"));
                application.setStatus(rs.getString("Status"));
                application.setSentAt(rs.getTimestamp("SentAt"));
                application.setProcessingDate(rs.getTimestamp("ProcessingDate"));
                application.setFile(rs.getString("FilePath"));
                list.add(application);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    // Search application by keyword (content or status) for a specific user
    public List<Application> searchByUserAndKeyword(int userId, String keyword) {
        List<Application> list = new ArrayList<>();
        String query = "SELECT * FROM Application WHERE UserID = ? AND (Content LIKE ? OR Status LIKE ?) ORDER BY SentAt DESC";
        try {
            stm = connection.prepareStatement(query);
            stm.setInt(1, userId);
            String kw = "%" + keyword + "%";
            stm.setString(2, kw);
            stm.setString(3, kw);
            rs = stm.executeQuery();
            while (rs.next()) {
                Application application = new Application();
                application.setApplicationID(rs.getInt("ApplicationID"));
                application.setUserID(rs.getInt("UserID"));
                application.setContent(rs.getString("Content"));
                application.setReply(rs.getString("Reply"));
                application.setStatus(rs.getString("Status"));
                application.setSentAt(rs.getTimestamp("SentAt"));
                application.setProcessingDate(rs.getTimestamp("ProcessingDate"));
                application.setFile(rs.getString("FilePath"));
                list.add(application);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    // Get application by ID
    public Application getApplicationById(int applicationID) {
        Application application = null;
        String query = "SELECT * FROM Application WHERE ApplicationID = ?";
        try {
            stm = connection.prepareStatement(query);
            stm.setInt(1, applicationID);
            rs = stm.executeQuery();
            if (rs.next()) {
                application = new Application();
                application.setApplicationID(rs.getInt("ApplicationID"));
                application.setUserID(rs.getInt("UserID"));
                application.setContent(rs.getString("Content"));
                application.setReply(rs.getString("Reply"));
                application.setStatus(rs.getString("Status"));
                application.setSentAt(rs.getTimestamp("SentAt"));
                application.setProcessingDate(rs.getTimestamp("ProcessingDate"));
                application.setFile(rs.getString("FilePath"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return application;
    }

    // Create a new application
    public boolean createApplication(Application application) {
        String query = "INSERT INTO Application (UserID, Content, Status, SentAt, ProcessingDate, FilePath) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            stm = connection.prepareStatement(query);
            stm.setInt(1, application.getUserID());
            stm.setString(2, application.getContent());
            stm.setString(3, application.getStatus());
            stm.setTimestamp(4, new java.sql.Timestamp(application.getSentAt().getTime()));
            stm.setTimestamp(5, application.getProcessingDate() != null ? new java.sql.Timestamp(application.getProcessingDate().getTime()) : null);
            stm.setString(6, application.getFile());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    // Update an application
    public boolean updateApplication(Application application) {
        String query = "UPDATE Application SET Status = ?, Reply = ?, ProcessingDate = ?, FilePath = ? WHERE ApplicationID = ?";
        try {
            stm = connection.prepareStatement(query);
            stm.setString(1, application.getStatus());
            stm.setString(2, application.getReply());
            stm.setTimestamp(3, application.getProcessingDate() != null ? new java.sql.Timestamp(application.getProcessingDate().getTime()) : null);
            stm.setString(4, application.getFile());
            stm.setInt(5, application.getApplicationID());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    // Delete application
    public boolean deleteApplication(int applicationID) {
        String query = "DELETE FROM Application WHERE ApplicationID = ?";
        try {
            stm = connection.prepareStatement(query);
            stm.setInt(1, applicationID);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    // Helper: close resources
    private void close() {
        try {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
