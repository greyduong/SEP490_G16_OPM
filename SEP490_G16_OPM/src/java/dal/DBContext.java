package dal;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import util.DatabaseMapper;

public class DBContext {

    protected Connection connection;
    private final String user = "sa";
    private final String password = "123";
    private final String connectUrl = "jdbc:sqlserver://DESKTOP-G5K8Q7S\\MSSQLSERVER01:1433;databaseName=OPM";
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

    public <T> List<T> fetchAll(Class<T> clazz, String query, Object... params) {
        List<T> result = new ArrayList<>();
        DatabaseMapper<T> convertor = new DatabaseMapper<>(clazz);
        try (PreparedStatement pstm = prepareStatement(query, params); ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                T item = convertor.fromResultSet(rs);
                result.add(item);
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public <T> T fetchOne(Class<T> clazz, String query, Object... params) {
        DatabaseMapper<T> convertor = new DatabaseMapper<>(clazz);
        try (PreparedStatement pstm = prepareStatement(query, params); ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                T item = convertor.fromResultSet(rs);
                return item;
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public int count(String query, Object... params) {
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

    public <T> int insert(Class<T> clazz, T data) {
        DatabaseMapper<T> mapper = new DatabaseMapper<>(clazz);
        Map<String, Object> fields = mapper.getFields(data);
        List<String> columns = fields.keySet().stream().toList();
        List<Object> values = columns.stream().map(col -> fields.get(col)).toList();
        String query = "INSERT INTO %s ".formatted(mapper.getTableName());
        query += "(" + String.join(", ", columns) + ")";
        query += " VALUES (" + String.join(", ", "?".repeat(columns.size()).split("")) + ")";
        try (PreparedStatement pstm = prepareStatementReturnKeys(query, values.toArray())) {
            int rows = pstm.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Insert failed");
            }
            ResultSet generatedKey = pstm.getGeneratedKeys();
            if (generatedKey.next()) {
                return generatedKey.getInt(1);
            } else {
                throw new SQLException("Insert failed");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public <T> void update(Class<T> clazz, T data) {
        DatabaseMapper<T> mapper = new DatabaseMapper<>(clazz);
        Map<String, Object> fields = mapper.getFields(data);
        String primaryKey = mapper.getPrimaryKey();
        int id = mapper.getPrimaryKey(data);
        List<String> columns = fields.keySet().stream().filter(col -> !col.equals(primaryKey)).toList();
        List<Object> values = columns.stream().map(col -> fields.get(col)).toList();
        String query = "UPDATE %s".formatted(mapper.getTableName());
        query += " SET " + String.join(", ", columns.stream().map(col -> "%s = ?".formatted(col)).toList());
        query += " WHERE %s = ? ".formatted(mapper.getPrimaryKey());
        List<Object> params = new ArrayList<>();
        params.addAll(values);
        params.add(id);
        Logger.getLogger(DBContext.class.getName()).info(query);
        try (PreparedStatement pstm = prepareStatement(query, params.toArray())) {
            int rows = pstm.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Update failed");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
}
