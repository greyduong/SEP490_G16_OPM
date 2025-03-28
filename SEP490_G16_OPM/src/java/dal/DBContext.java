package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
}
