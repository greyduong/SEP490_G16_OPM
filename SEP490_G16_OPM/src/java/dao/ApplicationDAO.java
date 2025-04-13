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
                applications.add(application);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return applications;
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
            stm.setString(6, application.getFile()); // Set file path here
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    // Update an application's status or reply
    public boolean updateApplication(Application application) {
        String query = "UPDATE Application SET Status = ?, Reply = ?, ProcessingDate = ?, FilePath = ? WHERE ApplicationID = ?";
        try {
            stm = connection.prepareStatement(query);
            stm.setString(1, application.getStatus());
            stm.setString(2, application.getReply());
            stm.setTimestamp(3, application.getProcessingDate() != null ? new java.sql.Timestamp(application.getProcessingDate().getTime()) : null);
            stm.setString(4, application.getFile()); // Update file path here
            stm.setInt(5, application.getApplicationID());
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    // Delete an application
    public boolean deleteApplication(int applicationID) {
        String query = "DELETE FROM Application WHERE ApplicationID = ?";
        try {
            stm = connection.prepareStatement(query);
            stm.setInt(1, applicationID);
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

//    public static void main(String[] args) {
//        ApplicationDAO da = new ApplicationDAO();
//
//        List<Application> applications = da.getAllApplications();
//        if (applications.isEmpty()) {
//            System.out.println("No applications found.");
//        } else {
//            System.out.println("Applications retrieved: ");
//            for (Application application : applications) {
//                System.out.println(application); // This will use the toString() method of the Application class
//            }
//        }
//    }
}
