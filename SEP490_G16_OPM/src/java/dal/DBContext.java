package dal;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {

    protected Connection connection;
    private final String user = "sa";
    private final String password = "123";
    private final String connectUrl = "jdbc:sqlserver://localhost:1433;databaseName=OPM";
    private final String className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public DBContext() {
        try {
            Class.forName(className);
            connection = DriverManager.getConnection(connectUrl, user, password);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement(query);
        int index = 1;
        for (Object param : params) {
            pstm.setObject(index++, param);
        }
        return pstm;
    }

    public PreparedStatement prepareStatementReturnKeys(String query, Object... params) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int index = 1;
        for (Object param : params) {
            pstm.setObject(index++, param);
        }
        return pstm;
    }

    public <T> List<T> fetchAll(Mapper<T> mapper, String query, Object... params) {
        System.out.println(query);
        List<T> result = new ArrayList<>();
        try (PreparedStatement pstm = prepareStatement(query, params); ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                T item = mapper.fromResultSet(rs);
                result.add(item);
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public <T> T fetchOne(Mapper<T> mapper, String query, Object... params) {
        System.out.println(query);
        try (PreparedStatement pstm = prepareStatement(query, params); ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                T item = mapper.fromResultSet(rs);
                return item;
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public int count(String query, Object... params) {
        System.out.println(query);
        try (PreparedStatement pstm = prepareStatement(query, params); ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("No result");
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public int insert(String query, Object... params) {
        System.out.println(query);
        try (PreparedStatement pstm = prepareStatementReturnKeys(query, params)) {
            int inserted = pstm.executeUpdate();
            if (inserted == 0) {
                throw new SQLException("No row inserted");
            }
            ResultSet generatedKey = pstm.getGeneratedKeys();
            if (generatedKey.next()) {
                return generatedKey.getInt(1);
            } else {
                throw new SQLException("No key generated");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public void update(String query, Object... params) {
        System.out.println(query);
        try (PreparedStatement pstm = prepareStatement(query, params)) {
            int updated = pstm.executeUpdate();
            if (updated == 0) throw new SQLException("No row updated");
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public void delete(String query, Object... params) {
        System.out.println(query);
        try (PreparedStatement pstm = prepareStatement(query, params)) {
            int deleted = pstm.executeUpdate();
            if (deleted == 0) throw new SQLException("No row deleted");
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
}
