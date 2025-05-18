package dao;

import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Farm;
import model.PigsOffer;
import model.Page;
import model.User;

/**
 *
 * @author dangtuong
 */
public class PigsOfferDAO extends DBContext {

    public static Mapper<PigsOffer> mapper() {
        return (rs) -> {
            PigsOffer pigsOffer = new PigsOffer();

            pigsOffer.setOfferID(rs.getInt("OfferID"));
            pigsOffer.setSellerID(rs.getInt("SellerID"));
            pigsOffer.setFarmID(rs.getInt("FarmID"));
            pigsOffer.setCategoryID(rs.getInt("CategoryID"));
            pigsOffer.setName(rs.getString("Name"));
            pigsOffer.setPigBreed(rs.getString("PigBreed"));
            pigsOffer.setQuantity(rs.getInt("Quantity"));
            pigsOffer.setMinQuantity(rs.getInt("MinQuantity"));
            pigsOffer.setMinDeposit(rs.getDouble("MinDeposit"));
            pigsOffer.setRetailPrice(rs.getDouble("RetailPrice"));
            pigsOffer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
            pigsOffer.setDescription(rs.getString("Description"));
            pigsOffer.setImageURL(rs.getString("ImageURL"));
            pigsOffer.setStartDate(rs.getDate("StartDate"));
            pigsOffer.setEndDate(rs.getDate("EndDate"));
            pigsOffer.setStatus(rs.getString("Status"));
            pigsOffer.setNote(rs.getString("Note"));
            pigsOffer.setCreatedAt(rs.getTimestamp("CreatedAt"));

            return pigsOffer;
        };
    }

    PreparedStatement stm;
    ResultSet rs;

    //get all offer
    public ArrayList<PigsOffer> getAllPigsOffers() {
        ArrayList<PigsOffer> list = new ArrayList<>();
        try {
            String strSQL = "SELECT * FROM [OPM].[dbo].[PigsOffer]";
            stm = connection.prepareStatement(strSQL);
            rs = stm.executeQuery();
            while (rs.next()) {
                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setSellerID(rs.getInt("SellerID"));
                offer.setFarmID(rs.getInt("FarmID"));
                offer.setCategoryID(rs.getInt("CategoryID"));
                offer.setName(rs.getString("Name"));
                offer.setPigBreed(rs.getString("PigBreed"));
                offer.setQuantity(rs.getInt("Quantity"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setStartDate(rs.getDate("StartDate"));
                offer.setEndDate(rs.getDate("EndDate"));
                offer.setStatus(rs.getString("Status"));
                offer.setNote(rs.getString("Note"));
                offer.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(offer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return list;

    }

    public Page<PigsOffer> getAllOffersWithFilter(String farmId, String search, String status,
            String sort, int pageNumber, int pageSize) throws Exception {
        Page<PigsOffer> page = new Page<>();
        List<PigsOffer> list = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;

        StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");
        if (farmId != null && !farmId.isEmpty()) {
            whereClause.append(" AND p.FarmID = ?");
            params.add(Integer.parseInt(farmId));
        }
        if (search != null && !search.isEmpty()) {
            whereClause.append(" AND p.Name LIKE ?");
            params.add("%" + search + "%");
        }
        if (status != null && !status.isEmpty()) {
            whereClause.append(" AND p.Status = ?");
            params.add(status);
        }

        String orderClause;
        switch (sort != null ? sort : "") {
            case "quantity_asc" ->
                orderClause = " p.Quantity ASC ";
            case "quantity_desc" ->
                orderClause = " p.Quantity DESC ";
            case "totalprice_asc" ->
                orderClause = " p.TotalOfferPrice ASC ";
            case "totalprice_desc" ->
                orderClause = " p.TotalOfferPrice DESC ";
            case "order_asc" ->
                orderClause = " ISNULL(o.OrderCount, 0) ASC ";
            case "order_desc" ->
                orderClause = " ISNULL(o.OrderCount, 0) DESC ";
            case "enddate_asc" ->
                orderClause = " p.EndDate ASC ";
            case "enddate_desc" ->
                orderClause = " p.EndDate DESC ";
            default ->
                orderClause = " p.CreatedAt DESC ";
        }

        String sql = """
        SELECT p.*, 
               u.UserID AS SellerUserID, u.FullName AS SellerName,
               f.FarmID AS FarmID, f.FarmName, f.Location, f.Status AS FarmStatus,
               c.CategoryID AS CatID, c.Name AS CategoryName,
               ISNULL(o.OrderCount, 0) AS OrderCount
        FROM PigsOffer p
        JOIN UserAccount u ON p.SellerID = u.UserID
        JOIN Farm f ON p.FarmID = f.FarmID
        JOIN Category c ON p.CategoryID = c.CategoryID
        LEFT JOIN (
            SELECT OfferID, COUNT(*) AS OrderCount
            FROM Orders
            GROUP BY OfferID
        ) o ON p.OfferID = o.OfferID
        """ + whereClause + " ORDER BY " + orderClause + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        String countSql = "SELECT COUNT(*) FROM PigsOffer p " + whereClause;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int i = 1;
            for (Object param : params) {
                ps.setObject(i++, param);
            }
            ps.setInt(i++, offset);
            ps.setInt(i, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PigsOffer offer = new PigsOffer();

                    offer.setOfferID(rs.getInt("OfferID"));
                    offer.setSellerID(rs.getInt("SellerID"));
                    offer.setFarmID(rs.getInt("FarmID"));
                    offer.setCategoryID(rs.getInt("CategoryID"));
                    offer.setName(rs.getString("Name"));
                    offer.setPigBreed(rs.getString("PigBreed"));
                    offer.setQuantity(rs.getInt("Quantity"));
                    offer.setMinQuantity(rs.getInt("MinQuantity"));
                    offer.setMinDeposit(rs.getDouble("MinDeposit"));
                    offer.setRetailPrice(rs.getDouble("RetailPrice"));
                    offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                    offer.setDescription(rs.getString("Description"));
                    offer.setImageURL(rs.getString("ImageURL"));
                    offer.setStartDate(rs.getDate("StartDate"));
                    offer.setEndDate(rs.getDate("EndDate"));
                    offer.setStatus(rs.getString("Status"));
                    offer.setNote(rs.getString("Note"));
                    offer.setCreatedAt(rs.getTimestamp("CreatedAt"));

                    offer.setOrderCount(rs.getInt("OrderCount"));

                    User seller = new User();
                    seller.setUserID(rs.getInt("SellerUserID"));
                    seller.setFullName(rs.getString("SellerName"));
                    offer.setSeller(seller);

                    Farm farm = new Farm();
                    farm.setFarmID(rs.getInt("FarmID"));
                    farm.setFarmName(rs.getString("FarmName"));
                    farm.setLocation(rs.getString("Location"));
                    farm.setStatus(rs.getString("FarmStatus"));
                    offer.setFarm(farm);

                    Category category = new Category();
                    category.setCategoryID(rs.getInt("CatID"));
                    category.setName(rs.getString("CategoryName"));
                    offer.setCategory(category);

                    list.add(offer);
                }
            }

            try (PreparedStatement countPs = connection.prepareStatement(countSql)) {
                for (i = 0; i < params.size(); i++) {
                    countPs.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = countPs.executeQuery()) {
                    if (rs.next()) {
                        int total = rs.getInt(1);
                        page.setTotalData(total);
                        page.setTotalPage((int) Math.ceil((double) total / pageSize));
                    }
                }
            }
        }

        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.setData(list);
        return page;
    }

    public ArrayList<PigsOffer> getPagedPigsOffers(int page, int pageSize) {
        ArrayList<PigsOffer> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        try {
            String strSQL = "SELECT * FROM [OPM].[dbo].[PigsOffer] "
                    + "ORDER BY CreatedAt DESC "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            stm = connection.prepareStatement(strSQL);
            stm.setInt(1, offset);
            stm.setInt(2, pageSize);
            rs = stm.executeQuery();

            while (rs.next()) {
                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setSellerID(rs.getInt("SellerID"));
                offer.setFarmID(rs.getInt("FarmID"));
                offer.setCategoryID(rs.getInt("CategoryID"));
                offer.setName(rs.getString("Name"));
                offer.setPigBreed(rs.getString("PigBreed"));
                offer.setQuantity(rs.getInt("Quantity"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setStartDate(rs.getDate("StartDate"));
                offer.setEndDate(rs.getDate("EndDate"));
                offer.setStatus(rs.getString("Status"));
                offer.setNote(rs.getString("Note"));
                offer.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(offer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return list;
    }

    public Page<PigsOffer> getOffersBySeller(int sellerId, int pageNumber, int pageSize, String sort, String search, String status, String farmId) {
        Page<PigsOffer> page = new Page<>();
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        List<PigsOffer> list = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;

        StringBuilder whereClause = new StringBuilder(" WHERE p.SellerID = ? ");
        List<Object> params = new ArrayList<>();
        params.add(sellerId);

        if (search != null && !search.trim().isEmpty()) {
            whereClause.append(" AND p.Name LIKE ? ");
            params.add("%" + search.trim() + "%");
        }

        if (status != null && !status.isEmpty()) {
            whereClause.append(" AND p.Status = ? ");
            params.add(status);
        }

        if (farmId != null && !farmId.isEmpty()) {
            whereClause.append(" AND p.FarmID = ? ");
            params.add(Integer.parseInt(farmId));
        }

        String orderClause;
        switch (sort != null ? sort : "") {
            case "quantity_asc" ->
                orderClause = " p.Quantity ASC, p.EndDate ASC, ISNULL(o.OrderCount, 0) ASC, p.OfferID ASC ";
            case "quantity_desc" ->
                orderClause = " p.Quantity DESC, p.EndDate DESC, ISNULL(o.OrderCount, 0) DESC, p.OfferID DESC ";
            case "totalprice_asc" ->
                orderClause = " p.TotalOfferPrice ASC, p.EndDate ASC, p.Quantity ASC, ISNULL(o.OrderCount, 0) ASC, p.OfferID ASC ";
            case "totalprice_desc" ->
                orderClause = " p.TotalOfferPrice DESC, p.EndDate DESC, p.Quantity DESC, ISNULL(o.OrderCount, 0) DESC, p.OfferID DESC ";
            case "order_asc" ->
                orderClause = " ISNULL(o.OrderCount, 0) ASC, p.EndDate ASC, p.Quantity ASC, p.OfferID ASC ";
            case "order_desc" ->
                orderClause = " ISNULL(o.OrderCount, 0) DESC, p.EndDate DESC, p.Quantity DESC, p.OfferID DESC ";
            case "enddate_asc" ->
                orderClause = " p.EndDate ASC, p.Quantity ASC, ISNULL(o.OrderCount, 0) ASC, p.OfferID ASC ";
            case "enddate_desc" ->
                orderClause = " p.EndDate DESC, p.Quantity DESC, ISNULL(o.OrderCount, 0) DESC, p.OfferID DESC ";
            default ->
                orderClause = " p.CreatedAt ASC, p.OfferID ASC ";
        }

        String sql = """
            SELECT p.*, 
                   u.UserID AS SellerUserID, u.FullName AS SellerName,
                   f.FarmID AS FarmID, f.FarmName, f.Location, f.Status AS FarmStatus,
                   c.CategoryID AS CatID, c.Name AS CategoryName,
                   ISNULL(o.OrderCount, 0) AS OrderCount
            FROM PigsOffer p
            JOIN UserAccount u ON p.SellerID = u.UserID
            JOIN Farm f ON p.FarmID = f.FarmID
            JOIN Category c ON p.CategoryID = c.CategoryID
            LEFT JOIN (
                SELECT OfferID, COUNT(*) AS OrderCount
                FROM Orders
                GROUP BY OfferID
            ) o ON p.OfferID = o.OfferID
            """ + whereClause + " ORDER BY " + orderClause + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        String countSql = "SELECT COUNT(*) FROM PigsOffer p " + whereClause;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int i = 1;
            for (Object param : params) {
                ps.setObject(i++, param);
            }
            ps.setInt(i++, offset);
            ps.setInt(i, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PigsOffer offer = new PigsOffer();

                    offer.setOfferID(rs.getInt("OfferID"));
                    offer.setSellerID(rs.getInt("SellerID"));
                    offer.setFarmID(rs.getInt("FarmID"));
                    offer.setCategoryID(rs.getInt("CategoryID"));
                    offer.setName(rs.getString("Name"));
                    offer.setPigBreed(rs.getString("PigBreed"));
                    offer.setQuantity(rs.getInt("Quantity"));
                    offer.setMinQuantity(rs.getInt("MinQuantity"));
                    offer.setMinDeposit(rs.getDouble("MinDeposit"));
                    offer.setRetailPrice(rs.getDouble("RetailPrice"));
                    offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                    offer.setDescription(rs.getString("Description"));
                    offer.setImageURL(rs.getString("ImageURL"));
                    offer.setStartDate(rs.getDate("StartDate"));
                    offer.setEndDate(rs.getDate("EndDate"));
                    offer.setStatus(rs.getString("Status"));
                    offer.setNote(rs.getString("Note"));
                    offer.setCreatedAt(rs.getTimestamp("CreatedAt"));

                    offer.setOrderCount(rs.getInt("OrderCount"));

                    User seller = new User();
                    seller.setUserID(rs.getInt("SellerUserID"));
                    seller.setFullName(rs.getString("SellerName"));
                    offer.setSeller(seller);

                    Farm farm = new Farm();
                    farm.setFarmID(rs.getInt("FarmID"));
                    farm.setFarmName(rs.getString("FarmName"));
                    farm.setLocation(rs.getString("Location"));
                    farm.setStatus(rs.getString("FarmStatus"));
                    offer.setFarm(farm);

                    Category category = new Category();
                    category.setCategoryID(rs.getInt("CatID"));
                    category.setName(rs.getString("CategoryName"));
                    offer.setCategory(category);

                    list.add(offer);
                }
            }

            try (PreparedStatement countPs = connection.prepareStatement(countSql)) {
                for (i = 1; i <= params.size(); i++) {
                    countPs.setObject(i, params.get(i - 1));
                }

                try (ResultSet countRs = countPs.executeQuery()) {
                    if (countRs.next()) {
                        int totalRecords = countRs.getInt(1);
                        int totalPages = totalRecords / pageSize + (totalRecords % pageSize > 0 ? 1 : 0);
                        page.setTotalData(totalRecords);
                        page.setTotalPage(totalPages);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.setData(list);
        return page;
    }

    public int countOffersBySeller(int sellerId) {
        String sql = "SELECT COUNT(*) FROM PigsOffer WHERE SellerID = ?";
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

    public int countAvailableOffersBySeller(int sellerId) {
        String sql = """
            SELECT COUNT(*)
            FROM PigsOffer
            WHERE SellerID = ?
              AND Status = 'Available'
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

    public int countAllOffers() {
        int count = 0;
        try {
            String strSQL = "SELECT COUNT(*) FROM [OPM].[dbo].[PigsOffer]";
            stm = connection.prepareStatement(strSQL);
            rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return count;
    }

    public PigsOffer getOfferById(int id) {
        PigsOffer offer = null;
        String sql = "SELECT o.*, "
                + "f.FarmName, f.Location AS FarmLocation, f.Description AS FarmDescription, f.Status AS FarmStatus, "
                + "u.FullName AS SellerName, u.Username, u.Email, u.Phone, u.Address, "
                + "c.Name AS CategoryName "
                + "FROM PigsOffer o "
                + "JOIN Farm f ON o.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.SellerID = u.UserID "
                + "JOIN Category c ON o.CategoryID = c.CategoryID "
                + "WHERE o.OfferID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setSellerID(rs.getInt("SellerID"));
                offer.setFarmID(rs.getInt("FarmID"));
                offer.setCategoryID(rs.getInt("CategoryID"));
                offer.setName(rs.getString("Name"));
                offer.setPigBreed(rs.getString("PigBreed"));
                offer.setQuantity(rs.getInt("Quantity"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setStartDate(rs.getDate("StartDate"));
                offer.setEndDate(rs.getDate("EndDate"));
                offer.setStatus(rs.getString("Status"));
                offer.setNote(rs.getString("Note"));
                offer.setCreatedAt(rs.getTimestamp("CreatedAt"));

                // Set Farm
                Farm farm = new Farm();
                farm.setFarmID(rs.getInt("FarmID"));
                farm.setFarmName(rs.getString("FarmName"));
                farm.setLocation(rs.getString("FarmLocation"));
                farm.setDescription(rs.getString("FarmDescription"));
                farm.setStatus(rs.getString("FarmStatus"));
                offer.setFarm(farm);

                // Set Seller
                User seller = new User();
                seller.setUserID(rs.getInt("SellerID"));
                seller.setFullName(rs.getString("SellerName"));
                seller.setUsername(rs.getString("Username"));
                seller.setEmail(rs.getString("Email"));
                seller.setPhone(rs.getString("Phone"));
                seller.setAddress(rs.getString("Address"));
                offer.setSeller(seller);

                // Set Category
                Category category = new Category();
                category.setCategoryID(rs.getInt("CategoryID"));
                category.setName(rs.getString("CategoryName"));
                offer.setCategory(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return offer;
    }

    public ArrayList<PigsOffer> getTop5OtherOffersByCategory(int categoryId, int excludeOfferId) {
        ArrayList<PigsOffer> list = new ArrayList<>();
        String sql = "SELECT TOP 5 o.*, "
                + "f.FarmName, f.Location AS FarmLocation, f.Description AS FarmDescription, f.Status AS FarmStatus, "
                + "u.FullName AS SellerName, u.Username, u.Email, u.Phone, u.Address, "
                + "c.Name AS CategoryName "
                + "FROM PigsOffer o "
                + "JOIN Farm f ON o.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.SellerID = u.UserID "
                + "JOIN Category c ON o.CategoryID = c.CategoryID "
                + "WHERE o.CategoryID = ? AND o.OfferID <> ? AND o.Status = 'Available' "
                + "ORDER BY o.StartDate DESC";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, categoryId);
            stm.setInt(2, excludeOfferId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PigsOffer offer = extractOfferFromResultSet(rs);
                list.add(offer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<PigsOffer> getTop5OtherOffersByFarm(int farmId, int excludeOfferId) {
        ArrayList<PigsOffer> list = new ArrayList<>();
        String sql = "SELECT TOP 5 o.*, "
                + "f.FarmName, f.Location AS FarmLocation, f.Description AS FarmDescription, f.Status AS FarmStatus, "
                + "u.FullName AS SellerName, u.Username, u.Email, u.Phone, u.Address, "
                + "c.Name AS CategoryName "
                + "FROM PigsOffer o "
                + "JOIN Farm f ON o.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.SellerID = u.UserID "
                + "JOIN Category c ON o.CategoryID = c.CategoryID "
                + "WHERE o.FarmID = ? AND o.OfferID <> ? AND o.Status = 'Available' "
                + "ORDER BY o.StartDate DESC";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, farmId);
            stm.setInt(2, excludeOfferId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PigsOffer offer = extractOfferFromResultSet(rs);
                list.add(offer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<PigsOffer> getTop5LatestOffers(int excludeOfferId) {
        ArrayList<PigsOffer> list = new ArrayList<>();
        String sql = "SELECT TOP 5 o.*, "
                + "f.FarmName, f.Location AS FarmLocation, f.Description AS FarmDescription, f.Status AS FarmStatus, "
                + "u.FullName AS SellerName, u.Username, u.Email, u.Phone, u.Address, "
                + "c.Name AS CategoryName "
                + "FROM PigsOffer o "
                + "JOIN Farm f ON o.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.SellerID = u.UserID "
                + "JOIN Category c ON o.CategoryID = c.CategoryID "
                + "WHERE o.OfferID <> ? AND o.Status = 'Available' "
                + "ORDER BY o.CreatedAt DESC";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, excludeOfferId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PigsOffer offer = extractOfferFromResultSet(rs);
                list.add(offer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private PigsOffer extractOfferFromResultSet(ResultSet rs) throws Exception {
        PigsOffer offer = new PigsOffer();
        offer.setOfferID(rs.getInt("OfferID"));
        offer.setSellerID(rs.getInt("SellerID"));
        offer.setFarmID(rs.getInt("FarmID"));
        offer.setCategoryID(rs.getInt("CategoryID"));
        offer.setName(rs.getString("Name"));
        offer.setPigBreed(rs.getString("PigBreed"));
        offer.setQuantity(rs.getInt("Quantity"));
        offer.setMinQuantity(rs.getInt("MinQuantity"));
        offer.setMinDeposit(rs.getDouble("MinDeposit"));
        offer.setRetailPrice(rs.getDouble("RetailPrice"));
        offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
        offer.setDescription(rs.getString("Description"));
        offer.setImageURL(rs.getString("ImageURL"));
        offer.setStartDate(rs.getDate("StartDate"));
        offer.setEndDate(rs.getDate("EndDate"));
        offer.setStatus(rs.getString("Status"));
        offer.setNote(rs.getString("Note"));
        offer.setCreatedAt(rs.getTimestamp("CreatedAt"));

        // Farm
        Farm farm = new Farm();
        farm.setFarmID(rs.getInt("FarmID"));
        farm.setFarmName(rs.getString("FarmName"));
        farm.setLocation(rs.getString("FarmLocation"));
        farm.setDescription(rs.getString("FarmDescription"));
        farm.setStatus(rs.getString("FarmStatus"));
        offer.setFarm(farm);

        // Seller
        User seller = new User();
        seller.setUserID(rs.getInt("SellerID"));
        seller.setFullName(rs.getString("SellerName"));
        seller.setUsername(rs.getString("Username"));
        seller.setEmail(rs.getString("Email"));
        seller.setPhone(rs.getString("Phone"));
        seller.setAddress(rs.getString("Address"));
        offer.setSeller(seller);

        // Category
        Category category = new Category();
        category.setCategoryID(rs.getInt("CategoryID"));
        category.setName(rs.getString("CategoryName"));
        offer.setCategory(category);

        return offer;
    }

    public boolean createPigsOffer(PigsOffer offer) {
        String sql = "INSERT INTO PigsOffer (SellerID, FarmID, CategoryID, Name, PigBreed, Quantity, MinQuantity, "
                + "MinDeposit, RetailPrice, TotalOfferPrice, Description, ImageURL, StartDate, EndDate, Status, Note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, offer.getSellerID());
            stm.setInt(2, offer.getFarmID());
            stm.setInt(3, offer.getCategoryID());
            stm.setString(4, offer.getName());
            stm.setString(5, offer.getPigBreed());
            stm.setInt(6, offer.getQuantity());
            stm.setInt(7, offer.getMinQuantity());
            stm.setDouble(8, offer.getMinDeposit());
            stm.setDouble(9, offer.getRetailPrice());
            stm.setDouble(10, offer.getTotalOfferPrice());
            stm.setString(11, offer.getDescription());
            stm.setString(12, offer.getImageURL());
            stm.setDate(13, offer.getStartDate());
            stm.setDate(14, offer.getEndDate());
            stm.setString(15, offer.getStatus());
            stm.setString(16, offer.getNote());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePigsOffer(PigsOffer offer) {
        String sql = "UPDATE PigsOffer SET SellerID = ?, FarmID = ?, CategoryID = ?, Name = ?, PigBreed = ?, "
                + "Quantity = ?, MinQuantity = ?, MinDeposit = ?, RetailPrice = ?, TotalOfferPrice = ?, "
                + "Description = ?, ImageURL = ?, StartDate = ?, EndDate = ?, Status = ?, Note = ? "
                + "WHERE OfferID = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, offer.getSellerID());
            stm.setInt(2, offer.getFarmID());
            stm.setInt(3, offer.getCategoryID());
            stm.setString(4, offer.getName());
            stm.setString(5, offer.getPigBreed());
            stm.setInt(6, offer.getQuantity());
            stm.setInt(7, offer.getMinQuantity());
            stm.setDouble(8, offer.getMinDeposit());
            stm.setDouble(9, offer.getRetailPrice());
            stm.setDouble(10, offer.getTotalOfferPrice());
            stm.setString(11, offer.getDescription());
            stm.setString(12, offer.getImageURL());
            stm.setDate(13, offer.getStartDate());
            stm.setDate(14, offer.getEndDate());
            stm.setString(15, offer.getStatus());
            stm.setString(16, offer.getNote());
            stm.setInt(17, offer.getOfferID()); // WHERE condition

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatusAndNote(PigsOffer offer) {
        String sql = "UPDATE PigsOffer SET Status = ?, Note = ? WHERE OfferID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, offer.getStatus());
            ps.setString(2, offer.getNote());
            ps.setInt(3, offer.getOfferID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void cancelPendingOrdersByOfferId(int offerId, String reason) throws Exception {
        // 1. Cancel deliveries thuộc các đơn hàng Pending của offer
        String cancelDeliveriesSql = """
        UPDATE d
        SET d.Status = 'Canceled'
        FROM Delivery d
        JOIN Orders o ON d.OrderID = o.OrderID
        WHERE o.OfferID = ? AND o.Status = 'Pending'
    """;

        try (PreparedStatement ps = connection.prepareStatement(cancelDeliveriesSql)) {
            ps.setInt(1, offerId);
            ps.executeUpdate();
        }

        // 2. Cancel các đơn hàng Pending của offer
        String cancelOrdersSql = """
        UPDATE Orders
        SET Status = 'Canceled', Note = ?
        WHERE OfferID = ? AND Status = 'Pending'
    """;

        try (PreparedStatement ps = connection.prepareStatement(cancelOrdersSql)) {
            ps.setString(1, reason);
            ps.setInt(2, offerId);
            ps.executeUpdate();
        }
    }

    public void updateOfferQuantityAfterCheckout(int offerId, int purchasedQuantity) {
        String sql = "UPDATE PigsOffer SET Quantity = Quantity - ? WHERE OfferID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, purchasedQuantity);
            stm.setInt(2, offerId);
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOfferStatus(int offerId, String status) {
        String sql = "UPDATE PigsOffer SET Status = ? WHERE OfferID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, offerId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOfferStatus(int offerId, String status) throws Exception {
        String sql = "UPDATE PigsOffer SET Status = ? WHERE OfferID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, offerId);
            ps.executeUpdate();
        }
    }

    public void updateOfferQuantity(int offerId, int quantityToAdd) {
        String sql = "UPDATE PigsOffer SET Quantity = Quantity + ? WHERE OfferID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantityToAdd);
            ps.setInt(2, offerId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getOfferQuantity(int offerId) {
        String sql = "SELECT Quantity FROM PigsOffer WHERE OfferID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, offerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<PigsOffer> getPagedPigsOffersWithSort(String sortOption, int page, int pageSize) {
        ArrayList<PigsOffer> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        String orderClause = "ORDER BY CreatedAt DESC";
        if (sortOption != null) {
            switch (sortOption) {
                case "quantity_asc" ->
                    orderClause = "ORDER BY Quantity ASC";
                case "quantity_desc" ->
                    orderClause = "ORDER BY Quantity DESC";
                case "price_asc" ->
                    orderClause = "ORDER BY RetailPrice ASC";
                case "price_desc" ->
                    orderClause = "ORDER BY RetailPrice DESC";
                case "oldest" ->
                    orderClause = "ORDER BY CreatedAt ASC";
                case "newest" ->
                    orderClause = "ORDER BY CreatedAt DESC";
            }
        }

        String sql = "SELECT * FROM PigsOffer WHERE Status = 'Available' " + orderClause
                + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, offset);
            stm.setInt(2, pageSize);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setSellerID(rs.getInt("SellerID"));
                offer.setFarmID(rs.getInt("FarmID"));
                offer.setCategoryID(rs.getInt("CategoryID"));
                offer.setName(rs.getString("Name"));
                offer.setPigBreed(rs.getString("PigBreed"));
                offer.setQuantity(rs.getInt("Quantity"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setStartDate(rs.getDate("StartDate"));
                offer.setEndDate(rs.getDate("EndDate"));
                offer.setStatus(rs.getString("Status"));
                offer.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(offer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<PigsOffer> searchOffersFlexible(String keyword, String categoryName, String sortOption, int page, int pageSize) {
        ArrayList<PigsOffer> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        StringBuilder sql = new StringBuilder("SELECT po.* FROM PigsOffer po JOIN Category c ON po.CategoryID = c.CategoryID WHERE po.Status = 'Available' ");
        ArrayList<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND po.Name LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql.append("AND c.Name = ? ");
            params.add(categoryName.trim());
        }

        String orderClause;
        if (sortOption == null || sortOption.isEmpty()) {
            orderClause = "ORDER BY po.CreatedAt DESC ";
        } else {
            switch (sortOption) {
                case "quantity_asc" ->
                    orderClause = "ORDER BY po.Quantity ASC ";
                case "quantity_desc" ->
                    orderClause = "ORDER BY po.Quantity DESC ";
                case "price_asc" ->
                    orderClause = "ORDER BY po.RetailPrice ASC ";
                case "price_desc" ->
                    orderClause = "ORDER BY po.RetailPrice DESC ";
                case "oldest" ->
                    orderClause = "ORDER BY po.CreatedAt ASC ";
                case "newest" ->
                    orderClause = "ORDER BY po.CreatedAt DESC ";
                default ->
                    orderClause = "ORDER BY po.CreatedAt DESC ";
            }
        }
        sql.append(orderClause);
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        params.add(offset);
        params.add(pageSize);

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setSellerID(rs.getInt("SellerID"));
                offer.setFarmID(rs.getInt("FarmID"));
                offer.setCategoryID(rs.getInt("CategoryID"));
                offer.setName(rs.getString("Name"));
                offer.setPigBreed(rs.getString("PigBreed"));
                offer.setQuantity(rs.getInt("Quantity"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setStartDate(rs.getDate("StartDate"));
                offer.setEndDate(rs.getDate("EndDate"));
                offer.setStatus(rs.getString("Status"));
                offer.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(offer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countOffersFlexible(String keyword, String categoryName) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM PigsOffer po JOIN Category c ON po.CategoryID = c.CategoryID WHERE po.Status = 'Available' ");
        ArrayList<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND po.Name LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql.append("AND c.Name = ? ");
            params.add(categoryName.trim());
        }

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int countAvailableOffers() {
        String sql = "SELECT COUNT(*) FROM PigsOffer WHERE Status = 'Available'";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countOffersByFarmId(int farmId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM PigsOffer WHERE FarmID = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, farmId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("countOffersByFarmId: " + e.getMessage());
        }

        return count;
    }

    public List<PigsOffer> getOffersByFarmId(int farmId) {
        List<PigsOffer> offers = new ArrayList<>();
        String sql = "SELECT * FROM PigsOffer WHERE FarmID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, farmId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PigsOffer offer = new PigsOffer();
                    offer.setOfferID(rs.getInt("OfferID"));
                    offer.setSellerID(rs.getInt("SellerID"));
                    offer.setFarmID(rs.getInt("FarmID"));
                    offer.setCategoryID(rs.getInt("CategoryID"));
                    offer.setName(rs.getString("Name"));
                    offer.setPigBreed(rs.getString("PigBreed"));
                    offer.setQuantity(rs.getInt("Quantity"));
                    offer.setMinQuantity(rs.getInt("MinQuantity"));
                    offer.setMinDeposit(rs.getBigDecimal("MinDeposit").doubleValue());
                    offer.setRetailPrice(rs.getBigDecimal("RetailPrice").doubleValue());
                    offer.setTotalOfferPrice(rs.getBigDecimal("TotalOfferPrice").doubleValue());
                    offer.setDescription(rs.getString("Description"));
                    offer.setImageURL(rs.getString("ImageURL"));
                    offer.setStartDate(rs.getDate("StartDate"));
                    offer.setEndDate(rs.getDate("EndDate"));
                    offer.setStatus(rs.getString("Status"));
                    offer.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    offers.add(offer);
                }
            }

        } catch (Exception e) {
            System.out.println("getOffersByFarmId: " + e.getMessage());
        }

        return offers;
    }

    public boolean updateStatus(PigsOffer offer) {
        String sql = "UPDATE PigsOffer SET Status = ? WHERE OfferID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, offer.getStatus());
            ps.setInt(2, offer.getOfferID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateOffersStatus(List<PigsOffer> offers, String status) {
        var statement = batch("UPDATE PigsOffer SET Status = ? WHERE OfferID = ?");
        offers.forEach(offer -> {
            statement.params(status, offer.getOfferID());
        });
        statement.execute();
    }

    public List<PigsOffer> getAvailableUpcomingOffers() {
        return fetchAll(rs -> {
            PigsOffer offer = new PigsOffer();
            offer.setOfferID(rs.getInt("OfferID"));
            offer.setSellerID(rs.getInt("SellerID"));
            offer.setFarmID(rs.getInt("FarmID"));
            offer.setCategoryID(rs.getInt("CategoryID"));
            offer.setName(rs.getString("Name"));
            offer.setPigBreed(rs.getString("PigBreed"));
            offer.setQuantity(rs.getInt("Quantity"));
            offer.setMinQuantity(rs.getInt("MinQuantity"));
            offer.setMinDeposit(rs.getBigDecimal("MinDeposit").doubleValue());
            offer.setRetailPrice(rs.getBigDecimal("RetailPrice").doubleValue());
            offer.setTotalOfferPrice(rs.getBigDecimal("TotalOfferPrice").doubleValue());
            offer.setDescription(rs.getString("Description"));
            offer.setImageURL(rs.getString("ImageURL"));
            offer.setStartDate(rs.getDate("StartDate"));
            offer.setEndDate(rs.getDate("EndDate"));
            offer.setStatus(rs.getString("Status"));
            offer.setCreatedAt(rs.getTimestamp("CreatedAt"));
            return offer;
        }, """
           SELECT *
           FROM PigsOffer
           WHERE status = 'Upcoming' AND GETDATE() > StartDate
           """);
    }

    public List<PigsOffer> getExpiredOffers() {
        return fetchAll(rs -> {
            PigsOffer offer = new PigsOffer();
            offer.setOfferID(rs.getInt("OfferID"));
            offer.setSellerID(rs.getInt("SellerID"));
            offer.setFarmID(rs.getInt("FarmID"));
            offer.setCategoryID(rs.getInt("CategoryID"));
            offer.setName(rs.getString("Name"));
            offer.setPigBreed(rs.getString("PigBreed"));
            offer.setQuantity(rs.getInt("Quantity"));
            offer.setMinQuantity(rs.getInt("MinQuantity"));
            offer.setMinDeposit(rs.getBigDecimal("MinDeposit").doubleValue());
            offer.setRetailPrice(rs.getBigDecimal("RetailPrice").doubleValue());
            offer.setTotalOfferPrice(rs.getBigDecimal("TotalOfferPrice").doubleValue());
            offer.setDescription(rs.getString("Description"));
            offer.setImageURL(rs.getString("ImageURL"));
            offer.setStartDate(rs.getDate("StartDate"));
            offer.setEndDate(rs.getDate("EndDate"));
            offer.setStatus(rs.getString("Status"));
            offer.setCreatedAt(rs.getTimestamp("CreatedAt"));
            return offer;
        }, """
           SELECT *
           FROM PigsOffer
           WHERE status = 'Available' AND GETDATE() > EndDate
           """);
    }

    public List<PigsOffer> getOffersByFarmWithFilter(int farmId, String keyword, String categoryName, String sort) throws Exception {
        List<PigsOffer> offers = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM PigsOffer WHERE FarmID = ? AND Status = ?");
        List<Object> params = new ArrayList<>();
        params.add(farmId);
        params.add("Available"); // ✅ chỉ lấy các offer đang mở bán

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND Name LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }

        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql.append(" AND CategoryID IN (SELECT CategoryID FROM Category WHERE Name = ?)");
            params.add(categoryName.trim());
        }

        // Xử lý sắp xếp
        if (sort != null) {
            switch (sort) {
                case "quantity_asc":
                    sql.append(" ORDER BY Quantity ASC");
                    break;
                case "quantity_desc":
                    sql.append(" ORDER BY Quantity DESC");
                    break;
                case "price_asc":
                    sql.append(" ORDER BY (Quantity * RetailPrice) ASC");
                    break;
                case "price_desc":
                    sql.append(" ORDER BY (Quantity * RetailPrice) DESC");
                    break;
            }
        }

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    PigsOffer offer = new PigsOffer();
                    offer.setOfferID(rs.getInt("OfferID"));
                    offer.setSellerID(rs.getInt("SellerID"));
                    offer.setFarmID(rs.getInt("FarmID"));
                    offer.setCategoryID(rs.getInt("CategoryID"));
                    offer.setName(rs.getString("Name"));
                    offer.setPigBreed(rs.getString("PigBreed"));
                    offer.setQuantity(rs.getInt("Quantity"));
                    offer.setMinQuantity(rs.getInt("MinQuantity"));
                    offer.setMinDeposit(rs.getDouble("MinDeposit"));
                    offer.setRetailPrice(rs.getDouble("RetailPrice"));
                    offer.setDescription(rs.getString("Description"));
                    offer.setImageURL(rs.getString("ImageURL"));
                    offer.setStartDate(rs.getDate("StartDate"));
                    offer.setEndDate(rs.getDate("EndDate"));
                    offer.setStatus(rs.getString("Status"));
                    offer.setCreatedAt(rs.getTimestamp("CreatedAt"));

                    // Tính tổng giá = số lượng * giá lẻ
                    offer.setTotalOfferPrice(offer.getQuantity() * offer.getRetailPrice());

                    offers.add(offer);
                }
            }
        }

        return offers;
    }
}
