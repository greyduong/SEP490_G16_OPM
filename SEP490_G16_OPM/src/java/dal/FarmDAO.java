package dal;

import java.util.List;
import model.Farm;
import model.Page;

public class FarmDAO extends DBContext {

    /**
     * Fetch farms data
     * @param pageNumber page number (default 1)
     * @param pageSize page size (default 10)
     * @return {@link Page}
     */
    public Page<Farm> getAllFarm(int pageNumber, int pageSize) {
        String query = "SELECT * FROM Farm ORDER BY FarmID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        Page<Farm> page = new Page<>();
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        int offset = (pageNumber - 1) * pageSize;
        List<Farm> data = fetchAll(Farm.class, query, offset, pageSize);
        if (data == null) return null;
        page.setData(data);
        return page;
    }
    
    public Farm getFarm(int farmId) {
        String query = "SELECT * FROM Farm WHERE FarmID = ?";
        return fetchOne(Farm.class, query, farmId);
    }
}
