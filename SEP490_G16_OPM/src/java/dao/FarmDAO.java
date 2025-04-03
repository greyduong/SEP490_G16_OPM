package dao;

import dal.DBContext;
import dal.FarmMapper;
import java.util.List;
import model.Farm;
import model.Page;

public class FarmDAO extends DBContext {

    public Page<Farm> getAllFarm(int pageNumber, int pageSize) {
        Page<Farm> page = new Page<>();
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        int offset = (pageNumber - 1) * pageSize;
        List<Farm> data = fetchAll(
                FarmMapper.toFarm(),
                "SELECT * FROM Farm WHERE Status = 'Active' ORDER BY FarmID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
                offset,
                pageSize);
        int totalRows = count("SELECT COUNT(*) FROM Farm WHERE Status = 'Active'");
        int totalPages = totalRows / pageSize + (totalRows % pageSize > 0 ? 1 : 0);
        page.setTotalPage(totalPages);
        if (data == null) {
            return null;
        }
        page.setData(data);
        return page;
    }

    public Farm getFarm(int farmId) {
        return fetchOne(
                FarmMapper.toFarm(),
                "SELECT * FROM Farm WHERE FarmID = ?",
                farmId);
    }

    public int createFarm(Farm farm) {
        return insert(
                "INSERT INTO Farm(FarmID, SellerID, FarmName, Location, Description, Status) VALUES (?, ?, ?, ?, ?, ?)",
                farm.getFarmID(),
                farm.getFarmName(),
                farm.getLocation(),
                farm.getDescription(),
                farm.getStatus());
    }

    public void updateFarm(Farm farm) {
        update(
                "UPDATE Farm SET FarmName = ?, Location = ?, Description = ?, Status = ? WHERE FarmID = ?",
                farm.getFarmName(),
                farm.getLocation(),
                farm.getDescription(),
                farm.getStatus(),
                farm.getFarmID());
    }

    public void deleteFarm(int id) {
        update(
                "UPDATE Farm SET Status = ? WHERE FarmID = ?",
                "Inactive",
                id);
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
        List<Farm> data = fetchAll(FarmMapper.toFarm(), selectQuery, "%" + name + "%", offset, pageSize);
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
