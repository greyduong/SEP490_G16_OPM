package dao;

import dal.DBContext;
import java.time.LocalDate;
import java.util.List;
import java.sql.Date;
import model.Page;
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

    public Page<ServerLog> getAllLogs(LocalDate from, LocalDate to, int page, int size) {
        System.out.println("page = " + page);
        var result = new Page<ServerLog>();
        if (page < 1) page = 1;
        int total = fetchOne(rs -> rs.getInt(1), "SELECT COUNT(*) FROM ServerLog WHERE CAST(CreatedAt AS DATE) >= ? AND CAST(CreatedAt AS DATE) <= ?", Date.valueOf(from), Date.valueOf(to));
        int totalPages = Math.ceilDiv(total, size);
        System.out.println("totalPages = " + totalPages);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = 1;
        result.setPageNumber(page);
        result.setPageSize(size);
        result.setTotalElements(total);
        result.setTotalPage(totalPages);
        long offset = (page - 1) * size;
        var data = fetchAll((rs) -> {
            ServerLog log = new ServerLog();
            log.setLogID(rs.getInt("LogID"));
            log.setContent(rs.getString("Content"));
            log.setCreatedAt(rs.getTimestamp("CreatedAt"));
            return log;
        }, "SELECT * FROM ServerLog WHERE CAST(CreatedAt AS DATE) >= ? AND CAST(CreatedAt AS DATE) <= ? ORDER BY CreatedAt OFFSET ? ROWS FETCH NEXT ? ROW ONLY", Date.valueOf(from), Date.valueOf(to), offset, size);
        result.setData(data);
        return result;
    }
}
