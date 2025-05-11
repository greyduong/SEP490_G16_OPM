package dao;

import dal.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Farm;
import model.Page;
import model.User;

public class FarmDAO extends DBContext {

    public Page<Farm> getFarmsByFilterbySellerId(int userId, String search, String status, int pageNumber, int pageSize, String sort) {
        Page<Farm> page = new Page<>();
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        int offset = (pageNumber - 1) * pageSize;

        StringBuilder whereClause = new StringBuilder(" WHERE f.SellerID = ? ");
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (search != null && !search.trim().isEmpty()) {
            whereClause.append(" AND f.FarmName LIKE ? ");
            params.add("%" + search.trim() + "%");
        }
        if (status != null && !status.isEmpty()) {
            whereClause.append(" AND f.Status = ? ");
            params.add(status);
        }

        String orderClause;
        switch (sort != null ? sort : "") {
            case "id_desc" ->
                orderClause = " f.FarmID DESC ";
            case "offer_asc" ->
                orderClause = " OfferCount ASC ";
            case "offer_desc" ->
                orderClause = " OfferCount DESC ";
            case "order_asc" ->
                orderClause = " OrderCount ASC ";
            case "order_desc" ->
                orderClause = " OrderCount DESC ";
            case "date_asc" ->
                orderClause = " f.CreatedAt ASC ";
            case "date_desc" ->
                orderClause = " f.CreatedAt DESC ";
            default ->
                orderClause = " f.FarmID ASC ";
        }

        String selectQuery = """
        SELECT f.FarmID, f.SellerID, f.FarmName, f.Location, f.Description, f.Note, f.Status, f.CreatedAt, f.ImageURL,
               u.UserID, u.FullName, u.Username, u.Email, u.Phone, u.Address, u.RoleID,
               COUNT(DISTINCT po.OfferID) AS OfferCount,
               COUNT(DISTINCT o.OrderID) AS OrderCount
        FROM Farm f
        JOIN UserAccount u ON f.SellerID = u.UserID
        LEFT JOIN PigsOffer po ON f.FarmID = po.FarmID
        LEFT JOIN Orders o ON f.FarmID = o.FarmID
        """ + whereClause + """
        GROUP BY f.FarmID, f.SellerID, f.FarmName, f.Location, f.Description, f.Note, f.Status, f.CreatedAt, f.ImageURL,
                 u.UserID, u.FullName, u.Username, u.Email, u.Phone, u.Address, u.RoleID
        ORDER BY """ + orderClause + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        String countQuery = "SELECT COUNT(*) FROM Farm f " + whereClause;

        List<Farm> farms = new ArrayList<>();
        try (
                PreparedStatement stm = connection.prepareStatement(selectQuery); PreparedStatement countStm = connection.prepareStatement(countQuery)) {

            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            stm.setInt(params.size() + 1, offset);
            stm.setInt(params.size() + 2, pageSize);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Farm farm = new Farm();
                    farm.setFarmID(rs.getInt("FarmID"));
                    farm.setSellerID(rs.getInt("SellerID"));
                    farm.setFarmName(rs.getString("FarmName"));
                    farm.setLocation(rs.getString("Location"));
                    farm.setDescription(rs.getString("Description"));
                    farm.setNote(rs.getString("Note"));
                    farm.setStatus(rs.getString("Status"));
                    farm.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    farm.setImageURL(rs.getString("ImageURL"));
                    farm.setOfferCount(rs.getInt("OfferCount"));
                    farm.setOrderCount(rs.getInt("OrderCount"));

                    User seller = new User();
                    seller.setUserID(rs.getInt("UserID"));
                    seller.setFullName(rs.getString("FullName"));
                    seller.setUsername(rs.getString("Username"));
                    seller.setEmail(rs.getString("Email"));
                    seller.setPhone(rs.getString("Phone"));
                    seller.setAddress(rs.getString("Address"));
                    seller.setRoleID(rs.getInt("RoleID"));
                    farm.setSeller(seller);

                    farms.add(farm);
                }
            }

            for (int i = 0; i < params.size(); i++) {
                countStm.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = countStm.executeQuery()) {
                if (rs.next()) {
                    int totalRows = rs.getInt(1);
                    int totalPages = totalRows / pageSize + (totalRows % pageSize > 0 ? 1 : 0);
                    page.setTotalPage(totalPages);
                    page.setTotalData(totalRows);
                }
            }

        } catch (Exception e) {
            System.out.println("getFarmsByFilter: " + e.getMessage());
            return null;
        }

        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.setData(farms);
        return page;
    }

    public boolean createNewFarm(Farm farm) {
        String sql = """
        INSERT INTO Farm (SellerID, FarmName, Location, Description, Note, ImageURL, Status, CreatedAt)
        VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, farm.getSellerID());
            stm.setString(2, farm.getFarmName());
            stm.setString(3, farm.getLocation());
            stm.setString(4, farm.getDescription());
            stm.setString(5, "Waiting");
            stm.setString(6, farm.getImageURL());
            stm.setString(7, "Pending");
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("createFarm: " + e.getMessage());
            return false;
        }
    }

    public Farm getFarmById(int farmId) {
        String query = "SELECT * FROM Farm WHERE FarmID = ?";
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setInt(1, farmId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    Farm farm = new Farm();
                    farm.setFarmID(rs.getInt("FarmID"));
                    farm.setSellerID(rs.getInt("SellerID"));
                    farm.setFarmName(rs.getString("FarmName"));
                    farm.setLocation(rs.getString("Location"));
                    farm.setDescription(rs.getString("Description"));
                    farm.setNote(rs.getString("Note"));
                    farm.setStatus(rs.getString("Status"));
                    farm.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    farm.setImageURL(rs.getString("ImageURL"));
                    return farm;
                }
            }
        } catch (Exception e) {
            System.out.println("getFarmById: " + e.getMessage());
        }
        return null;
    }

    public Farm getFarmWithSellerById(int farmId) {
        String sql = """
        SELECT f.FarmID, f.FarmName, f.Location, f.Description, f.Status, f.Note, f.ImageURL, f.CreatedAt,
               u.UserID, u.FullName, u.Email, u.Phone, u.Address,
               (SELECT COUNT(*) FROM Farm WHERE SellerID = f.SellerID) AS TotalFarms,
               (SELECT COUNT(*) FROM PigsOffer WHERE SellerID = f.SellerID) AS TotalOffers
        FROM Farm f
        JOIN UserAccount u ON f.SellerID = u.UserID
        WHERE f.FarmID = ?
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, farmId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    Farm farm = new Farm();
                    farm.setFarmID(rs.getInt("FarmID"));
                    farm.setFarmName(rs.getString("FarmName"));
                    farm.setLocation(rs.getString("Location"));
                    farm.setDescription(rs.getString("Description"));
                    farm.setStatus(rs.getString("Status"));
                    farm.setNote(rs.getString("Note"));
                    farm.setImageURL(rs.getString("ImageURL"));
                    farm.setCreatedAt(rs.getTimestamp("CreatedAt"));

                    User seller = new User();
                    seller.setUserID(rs.getInt("UserID"));
                    seller.setFullName(rs.getString("FullName"));
                    seller.setEmail(rs.getString("Email"));
                    seller.setPhone(rs.getString("Phone"));
                    seller.setAddress(rs.getString("Address"));
                    seller.setTotalFarms(rs.getInt("TotalFarms"));
                    seller.setTotalOffers(rs.getInt("TotalOffers"));

                    farm.setSeller(seller);
                    return farm;
                }
            }
        } catch (Exception e) {
            System.out.println("getFarmWithSellerById: " + e.getMessage());
        }
        return null;
    }

    public boolean updateOldFarm(Farm farm) {
        String sql = """
        UPDATE Farm 
        SET FarmName = ?, Location = ?, Description = ?, ImageURL = ?, Status = ?
        WHERE FarmID = ?
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, farm.getFarmName());
            stm.setString(2, farm.getLocation());
            stm.setString(3, farm.getDescription());
            stm.setString(4, farm.getImageURL());
            stm.setString(5, farm.getStatus());
            stm.setInt(6, farm.getFarmID());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("updateOldFarm: " + e.getMessage());
            return false;
        }
    }

    public boolean canDeactivateFarm(int farmId) {
        String offerQuery = "SELECT COUNT(*) FROM PigsOffer WHERE FarmID = ? AND Status = 'Available'";
        String orderQuery = "SELECT COUNT(*) FROM Orders WHERE FarmID = ? AND Status != 'Done'";

        try (
                PreparedStatement offerStm = connection.prepareStatement(offerQuery); PreparedStatement orderStm = connection.prepareStatement(orderQuery)) {
            offerStm.setInt(1, farmId);
            orderStm.setInt(1, farmId);

            try (ResultSet offerRs = offerStm.executeQuery(); ResultSet orderRs = orderStm.executeQuery()) {
                boolean hasAvailableOffer = offerRs.next() && offerRs.getInt(1) > 0;
                boolean hasUnfinishedOrder = orderRs.next() && orderRs.getInt(1) > 0;
                return !hasAvailableOffer && !hasUnfinishedOrder;
            }
        } catch (Exception e) {
            System.out.println("canDeactivateFarm: " + e.getMessage());
        }
        return false;
    }

    public boolean setFarmStatus(int farmId, String status) {
        String sql = "UPDATE Farm SET Status = ? WHERE FarmID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, status);
            stm.setInt(2, farmId);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOldFarm(int farmId, int userId) {
        String sql = "DELETE FROM Farm WHERE FarmID = ? AND SellerID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, farmId);
            stm.setInt(2, userId);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("deleteOldFarm: " + e.getMessage());
        }
        return false;
    }

    public List<Farm> getFarmsBySellerId(int sellerId) {
        List<Farm> farms = new ArrayList<>();
        String sql = """
        SELECT f.FarmID, f.FarmName, f.Location, f.Description, f.Note, f.Status, f.CreatedAt, f.ImageURL
        FROM Farm f
        WHERE f.SellerID = ?
        ORDER BY f.FarmName
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Farm farm = new Farm();
                    farm.setFarmID(rs.getInt("FarmID"));
                    farm.setFarmName(rs.getString("FarmName"));
                    farm.setLocation(rs.getString("Location"));
                    farm.setDescription(rs.getString("Description"));
                    farm.setNote(rs.getString("Note"));
                    farm.setStatus(rs.getString("Status"));
                    farm.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    farm.setImageURL(rs.getString("ImageURL"));
                    farms.add(farm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return farms;
    }

    public List<Farm> getAllFarmsWithPagination(int offset, int limit) {
        List<Farm> farms = new ArrayList<>();
        String sql = """
        SELECT FarmID, FarmName, Location, Description, Note, Status, CreatedAt, ImageURL
        FROM Farm
        ORDER BY CreatedAt DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Farm f = new Farm();
                    f.setFarmID(rs.getInt("FarmID"));
                    f.setFarmName(rs.getString("FarmName"));
                    f.setLocation(rs.getString("Location"));
                    f.setDescription(rs.getString("Description"));
                    f.setNote(rs.getString("Note"));
                    f.setStatus(rs.getString("Status"));
                    f.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    f.setImageURL(rs.getString("ImageURL"));
                    farms.add(f);
                }
            }
        } catch (Exception e) {
            System.out.println("getAllFarmsWithPagination: " + e.getMessage());
        }
        return farms;
    }

    public int countAllFarms() {
        String sql = "SELECT COUNT(*) FROM Farm";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("countAllFarms: " + e.getMessage());
        }
        return 0;
    }

    public int countFarmsBySeller(int sellerId) {
        String sql = """
            SELECT COUNT(*)
            FROM Farm
            WHERE SellerID = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Farm> searchAllFarmsWithPagination(String keyword, int offset, int limit) {
        List<Farm> farms = new ArrayList<>();
        String sql = """
        SELECT FarmID, FarmName, Location, Description, Note, Status, CreatedAt, ImageURL
        FROM Farm
        WHERE FarmName LIKE ?
        ORDER BY CreatedAt DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Farm f = new Farm();
                    f.setFarmID(rs.getInt("FarmID"));
                    f.setFarmName(rs.getString("FarmName"));
                    f.setLocation(rs.getString("Location"));
                    f.setDescription(rs.getString("Description"));
                    f.setNote(rs.getString("Note"));
                    f.setStatus(rs.getString("Status"));
                    f.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    f.setImageURL(rs.getString("ImageURL"));
                    farms.add(f);
                }
            }
        } catch (Exception e) {
            System.out.println("searchAllFarmsWithPagination: " + e.getMessage());
        }

        return farms;
    }

    public int countSearchAllFarms(String keyword) {
        String sql = "SELECT COUNT(*) FROM Farm WHERE FarmName LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("countSearchAllFarms: " + e.getMessage());
        }
        return 0;
    }

    public List<Farm> getActiveFarmsBySellerId(int sellerId) {
        List<Farm> farms = new ArrayList<>();
        String sql = """
        SELECT f.FarmID, f.FarmName, f.Location, f.Description, f.Note, f.Status, f.CreatedAt, f.ImageURL
        FROM Farm f
        WHERE f.SellerID = ? AND f.Status = 'Active'
        ORDER BY f.FarmName
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Farm farm = new Farm();
                    farm.setFarmID(rs.getInt("FarmID"));
                    farm.setFarmName(rs.getString("FarmName"));
                    farm.setLocation(rs.getString("Location"));
                    farm.setDescription(rs.getString("Description"));
                    farm.setNote(rs.getString("Note"));
                    farm.setStatus(rs.getString("Status"));
                    farm.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    farm.setImageURL(rs.getString("ImageURL"));
                    farms.add(farm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return farms;
    }

    public int countActiveFarmsBySeller(int sellerId) {
        String sql = """
            SELECT COUNT(*)
            FROM Farm
            WHERE SellerID = ?
              AND Status = 'Active'
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isActiveFarm(int farmId) {
        String sql = "SELECT 1 FROM Farm WHERE FarmID = ? AND Status = 'Active'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, farmId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Page<Farm> getFarmsForManager(String search, String status, int pageNumber, int pageSize, String sort) {
        Page<Farm> page = new Page<>();
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        int offset = (pageNumber - 1) * pageSize;

        StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            whereClause.append(" AND f.FarmName LIKE ? ");
            params.add("%" + search.trim() + "%");
        }
        if (status != null && !status.isEmpty()) {
            whereClause.append(" AND f.Status = ? ");
            params.add(status);
        }

        String orderClause;
        switch (sort != null ? sort : "") {
            case "date_asc" ->
                orderClause = " f.CreatedAt ASC ";
            case "date_desc" ->
                orderClause = " f.CreatedAt DESC ";
            default ->
                orderClause = " f.FarmID DESC ";
        }

        String selectQuery = """
            SELECT f.FarmID, f.SellerID, f.FarmName, f.Location, f.Description, f.Note, f.Status, f.ImageURL, f.CreatedAt,
                   u.FullName, u.Email, u.Phone, u.Address,
                   (SELECT COUNT(*) FROM Farm WHERE SellerID = f.SellerID) AS TotalFarms,
                   (SELECT COUNT(*) FROM PigsOffer WHERE SellerID = f.SellerID) AS TotalOffers
            FROM Farm f
            JOIN UserAccount u ON f.SellerID = u.UserID
        """ + whereClause + " ORDER BY " + orderClause + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        String countQuery = "SELECT COUNT(*) FROM Farm f " + whereClause;

        List<Farm> farms = new ArrayList<>();
        try (
                PreparedStatement stm = connection.prepareStatement(selectQuery); PreparedStatement countStm = connection.prepareStatement(countQuery)) {
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
                countStm.setObject(i + 1, params.get(i));
            }
            stm.setInt(params.size() + 1, offset);
            stm.setInt(params.size() + 2, pageSize);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Farm farm = new Farm();
                    farm.setFarmID(rs.getInt("FarmID"));
                    farm.setSellerID(rs.getInt("SellerID"));
                    farm.setFarmName(rs.getString("FarmName"));
                    farm.setLocation(rs.getString("Location"));
                    farm.setDescription(rs.getString("Description"));
                    farm.setNote(rs.getString("Note"));
                    farm.setStatus(rs.getString("Status"));
                    farm.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    farm.setImageURL(rs.getString("ImageURL"));

                    User seller = new User();
                    seller.setUserID(rs.getInt("SellerID"));
                    seller.setFullName(rs.getString("FullName"));
                    seller.setEmail(rs.getString("Email"));
                    seller.setPhone(rs.getString("Phone"));
                    seller.setAddress(rs.getString("Address"));
                    seller.setTotalFarms(rs.getInt("TotalFarms"));
                    seller.setTotalOffers(rs.getInt("TotalOffers"));

                    farm.setSeller(seller);
                    farms.add(farm);
                }
            }

            try (ResultSet rs = countStm.executeQuery()) {
                if (rs.next()) {
                    int totalRows = rs.getInt(1);
                    int totalPages = totalRows / pageSize + (totalRows % pageSize > 0 ? 1 : 0);
                    page.setTotalPage(totalPages);
                    page.setTotalData(totalRows);
                }
            }
        } catch (Exception e) {
            System.out.println("getFarmsForManager: " + e.getMessage());
            return null;
        }

        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.setData(farms);
        return page;
    }

    public boolean updateStatusAndNote(Farm farm) {
        String sql = "UPDATE Farm SET Status = ?, Note = ? WHERE FarmID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, farm.getStatus());
            stm.setString(2, farm.getNote());
            stm.setInt(3, farm.getFarmID());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("updateStatusAndNote: " + e.getMessage());
            return false;
        }
    }

}
