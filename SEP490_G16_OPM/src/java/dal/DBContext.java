package dal;

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

    public PreparedStatement prepareStatementReturnId(String query, String id, Object... params) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement(query, new String[]{id});
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

    /**
     * Thêm data và trả về ID của row vừa tạo
     * @param <T> 
     * @param clazz
     * @param table Tên bảng
     * @param id tên cột của id (ví dụ: FarmID)
     * @param data Farm object chứa data cần tạo
     * @param columns Tên các cột
     * @return 
     */
    public <T> int insert(Class<T> clazz, String table, String id, T data, String... columns) {
        ClassConvertor<T> convertor = new ClassConvertor<>(clazz);
        Map<String, Object> nonNullFields = convertor.getNotNullFields(data);
        List<String> nonNullColumns = Arrays.asList(columns).stream().filter(col -> nonNullFields.containsKey(col.toLowerCase())).toList();
        List<Object> nonNullValues = nonNullColumns.stream().map(col -> nonNullFields.get(col.toLowerCase())).toList();
        String query = "INSERT INTO %s ".formatted(table);
        query += "(" + String.join(", ", nonNullColumns) + ")";
        query += " VALUES (" + String.join(", ", "?".repeat(nonNullColumns.size()).split("")) + ")";
        try (PreparedStatement pstm = prepareStatementReturnId(query, id, nonNullValues.toArray())) {
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
}
