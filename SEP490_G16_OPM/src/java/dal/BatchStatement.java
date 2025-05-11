package dal;

import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hieu
 */
public class BatchStatement {
    private final Connection connection;
    private final String query;
    private final List<Object[]> params = new LinkedList<>();
    public BatchStatement(Connection connection, String query) {
        this.query = query;
        this.connection = connection;
    }
    public BatchStatement params(Object... params) {
        this.params.add(params);
        return this;
    }
    public int[] execute() {
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            for(var p : params) {
                int index = 0;
                for (var param : p) {
                    index++;
                    pstm.setObject(index, param);
                }
                pstm.addBatch();
            }
            return pstm.executeBatch();
        } catch (SQLException ex) {
            Logger.getLogger(BatchStatement.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
