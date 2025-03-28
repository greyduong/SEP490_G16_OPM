package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ClassConvertor;

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

    public <T> List<T> fetchAll(Class<T> clazz, String query, Object... params) {
        List<T> result = new ArrayList<>();
        ClassConvertor<T> convertor = new ClassConvertor<>(clazz);
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
        ClassConvertor<T> convertor = new ClassConvertor<>(clazz);
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
}
