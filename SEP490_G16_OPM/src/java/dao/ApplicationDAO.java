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
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Application> getApplicationsByFilter(int userId, String keyword, String status, String sortByDate) {
        List<Application> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM Application WHERE UserID = ?");
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND Content LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }

        if ("newest".equals(sortByDate)) {
            sql.append(" ORDER BY SentAt DESC");
        } else if ("oldest".equals(sortByDate)) {
            sql.append(" ORDER BY SentAt ASC");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application app = new Application();
                    app.setApplicationID(rs.getInt("ApplicationID"));
                    app.setUserID(rs.getInt("UserID"));
                    app.setContent(rs.getString("Content"));
                    app.setReply(rs.getString("Reply"));
                    app.setStatus(rs.getString("Status"));
                    app.setSentAt(rs.getTimestamp("SentAt"));
                    app.setProcessingDate(rs.getTimestamp("ProcessingDate"));
                    app.setFile(rs.getString("FilePath"));

                    list.add(app);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Application> getApplicationsByFilterPaged(
            int userId, String keyword, String status, String sortByDate,
            int pageIndex, int pageSize
    ) {
        List<Application> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Application WHERE UserID = ?");
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND Content LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }

        if ("newest".equals(sortByDate)) {
            sql.append(" ORDER BY SentAt DESC");
        } else if ("oldest".equals(sortByDate)) {
            sql.append(" ORDER BY SentAt ASC");
        } else {
            sql.append(" ORDER BY SentAt DESC"); // default
        }

        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add((pageIndex - 1) * pageSize);
        params.add(pageSize);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setApplicationID(rs.getInt("ApplicationID"));
                app.setUserID(rs.getInt("UserID"));
                app.setContent(rs.getString("Content"));
                app.setReply(rs.getString("Reply"));
                app.setStatus(rs.getString("Status"));
                app.setSentAt(rs.getTimestamp("SentAt"));
                app.setProcessingDate(rs.getTimestamp("ProcessingDate"));
                app.setFile(rs.getString("FilePath"));

                list.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countApplicationsByFilter(int userId, String keyword, String status) {
        int total = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Application WHERE UserID = ?");
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND Content LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public int countApplicationsForManager(String keyword, String status) {
        int total = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Application WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND Content LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status.trim());
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public List<Application> getApplicationsForManager(String keyword, String status, String sort, int page, int pageSize) {
        List<Application> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Application WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND Content LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status.trim());
        }

        if ("asc".equalsIgnoreCase(sort)) {
            sql.append(" ORDER BY SentAt ASC");
        } else {
            sql.append(" ORDER BY SentAt DESC");
        }

        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add((page - 1) * pageSize);
        params.add(pageSize);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setApplicationID(rs.getInt("ApplicationID"));
                app.setUserID(rs.getInt("UserID"));
                app.setContent(rs.getString("Content"));
                app.setReply(rs.getString("Reply"));
                app.setStatus(rs.getString("Status"));
                app.setSentAt(rs.getTimestamp("SentAt"));
                app.setProcessingDate(rs.getTimestamp("ProcessingDate"));
                app.setFile(rs.getString("FilePath"));
                list.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
