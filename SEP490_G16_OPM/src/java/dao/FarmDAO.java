package dao;

import dal.DBContext;
import java.util.List;
import model.Farm;
import model.Page;

public class FarmDAO extends DBContext {

    public Page<Farm> getAllFarm(int pageNumber, int pageSize) {
        String selectQuery = "SELECT * FROM Farm WHERE Status = 'Active' ORDER BY FarmID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String countQuery = "SELECT COUNT(*) FROM Farm WHERE Status = 'Active'";
        Page<Farm> page = new Page<>();
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        int offset = (pageNumber - 1) * pageSize;
        List<Farm> data = fetchAll(Farm.class, selectQuery, offset, pageSize);
        int totalRows = count(countQuery);
        int totalPages = totalRows / pageSize + (totalRows % pageSize > 0 ? 1 : 0);
        page.setTotalPage(totalPages);
        if (data == null) {
            return null;
        }
        page.setData(data);
        return page;
    }

    public Farm getFarm(int farmId) {
        String query = "SELECT * FROM Farm WHERE FarmID = ?";
        return fetchOne(Farm.class, query, farmId);
    }

    public int createFarm(Farm farm) {
        return insert(Farm.class, farm);
    }

    public void updateFarm(Farm farm) {
        update(Farm.class, farm);
    }

    public void deleteFarm(int id) {
        Farm farm = new Farm();
        farm.setFarmID(id);
        farm.setStatus("Inactive");
        updateFarm(farm);
    }

    public Page<Farm> searchFarm(String name, int pageNumber, int pageSize) {
        String selectQuery = "SELECT * FROM Farm WHERE Status = 'Active' AND FarmName LIKE ? ORDER BY FarmID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String countQuery = "SELECT COUNT(*) FROM Farm WHERE Status = 'Active' AND FarmName LIKE ?";
        Page<Farm> page = new Page<>();
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        int offset = (pageNumber - 1) * pageSize;
        List<Farm> data = fetchAll(Farm.class, selectQuery, "%" + name + "%", offset, pageSize);
        int totalRows = count(countQuery, "%" + name + "%");
        int totalPages = totalRows / pageSize + (totalRows % pageSize > 0 ? 1 : 0);
        page.setTotalPage(totalPages);
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        if (data == null) {
            return null;
        }
        page.setData(data);
        return page;
    }
}
