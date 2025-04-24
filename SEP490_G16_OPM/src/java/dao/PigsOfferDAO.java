/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
                // ⚠️ KHÔNG ĐÓNG connection ở đây nếu còn dùng tiếp
                // if (connection != null) connection.close();
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
                orderClause = " p.CreatedAt DESC, p.OfferID DESC ";
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

    //get offer by Id
    public PigsOffer getOfferById(int id) {
        PigsOffer offer = null;
        String sql = "SELECT o.*, f.FarmName, u.FullName AS SellerName "
                + "FROM PigsOffer o "
                + "JOIN Farm f ON o.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.SellerID = u.UserID "
                + "WHERE o.OfferID = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
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
                offer.setCreatedAt(rs.getTimestamp("CreatedAt"));
            }
            rs.close();
            stm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return offer;
    }

    //create offer
    public boolean createPigsOffer(PigsOffer offer) {
        String sql = "INSERT INTO PigsOffer (SellerID, FarmID, CategoryID, Name, PigBreed, Quantity, MinQuantity, "
                + "MinDeposit, RetailPrice, TotalOfferPrice, Description, ImageURL, StartDate, EndDate, Status, CreatedAt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
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
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePigsOffer(PigsOffer offer) {
        String sql = "UPDATE PigsOffer SET SellerID = ?, FarmID = ?, CategoryID = ?, Name = ?, PigBreed = ?, "
                + "Quantity = ?, MinQuantity = ?, MinDeposit = ?, RetailPrice = ?, TotalOfferPrice = ?, "
                + "Description = ?, ImageURL = ?, StartDate = ?, EndDate = ?, Status = ? WHERE OfferID = ?";

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
            stm.setInt(16, offer.getOfferID()); // WHERE condition

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    public ArrayList<PigsOffer> getPagedPigsOffersWithSort(String sortOption, int page, int pageSize) {
        ArrayList<PigsOffer> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        String orderClause = "ORDER BY CreatedAt DESC";
        if (sortOption != null) {
            switch (sortOption) {
                case "quantity_asc":
                    orderClause = "ORDER BY Quantity ASC";
                    break;
                case "quantity_desc":
                    orderClause = "ORDER BY Quantity DESC";
                    break;
                case "price_asc":
                    orderClause = "ORDER BY RetailPrice ASC";
                    break;
                case "price_desc":
                    orderClause = "ORDER BY RetailPrice DESC";
                    break;
            }
        }

        String sql = "SELECT * FROM PigsOffer " + orderClause + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

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

        StringBuilder sql = new StringBuilder("SELECT po.* FROM PigsOffer po JOIN Category c ON po.CategoryID = c.CategoryID WHERE 1=1 ");
        ArrayList<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND po.Name LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql.append("AND c.Name = ? ");
            params.add(categoryName.trim());
        }

        if (sortOption != null) {
            switch (sortOption) {
                case "quantity_asc":
                    sql.append("ORDER BY po.Quantity ASC ");
                    break;
                case "quantity_desc":
                    sql.append("ORDER BY po.Quantity DESC ");
                    break;
                case "price_asc":
                    sql.append("ORDER BY po.RetailPrice ASC ");
                    break;
                case "price_desc":
                    sql.append("ORDER BY po.RetailPrice DESC ");
                    break;
                default:
                    sql.append("ORDER BY po.CreatedAt DESC ");
            }
        } else {
            sql.append("ORDER BY po.CreatedAt DESC ");
        }

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
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM PigsOffer po JOIN Category c ON po.CategoryID = c.CategoryID WHERE 1=1 ");
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

}
