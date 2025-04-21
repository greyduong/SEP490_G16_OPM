package dao;

import dal.DBContext;
import java.util.List;
import model.Farm;
import model.Page;

public class FarmDAO extends DBContext {

    public static Mapper<Farm> mapper() {
        return (rs) -> {
            Farm farm = new Farm();
            farm.setCreatedAt(rs.getTimestamp("CreatedAt"));
            farm.setDescription(rs.getString("Description"));
            farm.setFarmID(rs.getInt("FarmID"));
            farm.setFarmName(rs.getString("FarmName"));
            farm.setLocation(rs.getString("Location"));
            farm.setSellerID(rs.getInt("SellerID"));
            farm.setStatus(rs.getString("Status"));
            return farm;
        };
    }

    public Farm getById(int id) {
        return fetchOne(
                mapper(),
                "SELECT * FROM Farm WHERE FarmID = ?",
                id);
    }

    public int create(Farm farm) {
        return insert(
                "INSERT INTO Farm(FarmID, SellerID, FarmName, Location, Description, Status) VALUES (?, ?, ?, ?, ?, ?)",
                farm.getFarmID(),
                farm.getFarmName(),
                farm.getLocation(),
                farm.getDescription(),
                farm.getStatus());
    }

    public void update(Farm farm) {
        update(
                "UPDATE Farm SET FarmName = ?, Location = ?, Description = ?, Status = ? WHERE FarmID = ?",
                farm.getFarmName(),
                farm.getLocation(),
                farm.getDescription(),
                farm.getStatus(),
                farm.getFarmID());
    }

    public void delete(int id) {
        update(
                "UPDATE Farm SET Status = ? WHERE FarmID = ?",
                "Inactive",
                id);
    }

    public Page<Farm> search(String name, int pageNumber, int pageSize) {
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
        List<Farm> data = fetchAll(mapper(), selectQuery, "%" + name + "%", offset, pageSize);
        int totalRows = count(countQuery, "%" + name + "%");
        int totalPages = totalRows / pageSize + (totalRows % pageSize > 0 ? 1 : 0);
        page.setTotalPage(totalPages);
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.setTotalElements(totalRows);
        if (data == null) {
            return null;
        }
        page.setData(data);
        return page;
    }
}
