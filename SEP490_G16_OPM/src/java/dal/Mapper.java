package dal;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {
    T fromResultSet(ResultSet rs) throws SQLException;
}
