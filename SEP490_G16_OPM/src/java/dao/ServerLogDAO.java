package dao;

import dal.DBContext;
import java.util.List;
import model.ServerLog;

public class ServerLogDAO extends DBContext {
    public void createLog(String content) {
        insert("INSERT INTO ServerLog(content) VALUES (?)", content);
    }
    public List<ServerLog> getAllLogs() {
        return fetchAll((rs) -> {
            ServerLog log = new ServerLog();
            log.setLogID(rs.getInt("LogID"));
            log.setContent(rs.getString("Content"));
            log.setCreatedAt(rs.getTimestamp("CreatedAt"));
            return log;
        }, "SELECT * FROM ServerLog");
    }
}