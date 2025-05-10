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
import model.Farm;
import model.Order;
import model.OrderStat;
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
public class OrderDAO extends DBContext {

    public void insertOrder(int dealerId, int sellerId, int offerId, int quantity, double totalPrice) {
        String sql = "INSERT INTO Orders (DealerID, SellerID, OfferID, Quantity, TotalPrice) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dealerId);
            ps.setInt(2, sellerId);
            ps.setInt(3, offerId);
            ps.setInt(4, quantity);
            ps.setDouble(5, totalPrice);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByBuyerWithFilter(int buyerId, String search, String status, String sort, int pageIndex, int pageSize) {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice, o.Status, o.CreatedAt, o.ProcessedDate, "
                + "p.Name AS OfferName, p.ImageURL, p.RetailPrice, p.MinQuantity, p.MinDeposit, p.TotalOfferPrice, p.Description, "
                + "f.FarmID, f.FarmName, f.Location, u.FullName AS SellerName "
                + "FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN Farm f ON p.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.SellerID = u.UserID "
                + "WHERE o.DealerID = ? "
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND o.Status = ? ");
        }

        switch (sort != null ? sort : "") {
            case "orderid_asc":
                sql.append("ORDER BY o.OrderID ASC ");
                break;
            case "orderid_desc":
                sql.append("ORDER BY o.OrderID DESC ");
                break;
            case "quantity_asc":
                sql.append("ORDER BY o.Quantity ASC ");
                break;
            case "quantity_desc":
                sql.append("ORDER BY o.Quantity DESC ");
                break;
            case "totalprice_asc":
                sql.append("ORDER BY o.TotalPrice ASC ");
                break;
            case "totalprice_desc":
                sql.append("ORDER BY o.TotalPrice DESC ");
                break;
            case "createdat_asc":
                sql.append("ORDER BY o.CreatedAt ASC ");
                break;
            case "createdat_desc":
                sql.append("ORDER BY o.CreatedAt DESC ");
                break;
            case "processeddate_asc":
                sql.append("ORDER BY o.ProcessedDate ASC ");
                break;
            case "processeddate_desc":
                sql.append("ORDER BY o.ProcessedDate DESC ");
                break;
            default:
                sql.append("ORDER BY o.CreatedAt DESC ");
                break;
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, buyerId);

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
                ps.setString(index++, "%" + search.trim() + "%");
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            int offset = (pageIndex - 1) * pageSize;
            ps.setInt(index++, offset);
            ps.setInt(index, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDealerID(rs.getInt("DealerID"));
                order.setSellerID(rs.getInt("SellerID"));
                order.setOfferID(rs.getInt("OfferID"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setTotalPrice(rs.getDouble("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                order.setProcessedDate(rs.getTimestamp("ProcessedDate"));

                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                order.setPigsOffer(offer);

                Farm farm = new Farm();
                farm.setFarmID(rs.getInt("FarmID"));
                farm.setFarmName(rs.getString("FarmName"));
                farm.setLocation(rs.getString("Location"));
                order.setFarm(farm);

                User seller = new User();
                seller.setUserID(rs.getInt("SellerID"));
                seller.setFullName(rs.getString("SellerName"));
                order.setSeller(seller);

                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public int countOrdersByBuyerWithFilter(int buyerId, String search, String status) {
        int count = 0;
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "WHERE o.DealerID = ? "
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND o.Status = ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, buyerId);

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
                ps.setString(index++, "%" + search.trim() + "%");
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<Order> getPendingOrdersBySellerWithFilter(int sellerId, Integer farmId, String search, String sort, int pageIndex, int pageSize) {
        List<Order> orders = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice, o.Status, o.CreatedAt, o.ProcessedDate, "
                + "p.Name AS OfferName, p.ImageURL, p.RetailPrice, p.MinQuantity, p.MinDeposit, p.TotalOfferPrice, p.Description, "
                + "f.FarmID, f.FarmName, f.Location, "
                + "u.FullName AS DealerName "
                + "FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN Farm f ON p.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.DealerID = u.UserID "
                + "WHERE o.SellerID = ? AND o.Status = 'Pending' "
        );

        if (farmId != null) {
            sql.append("AND p.FarmID = ? ");
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }

        switch (sort != null ? sort : "") {
            case "orderid_asc":
                sql.append("ORDER BY o.OrderID ASC ");
                break;
            case "orderid_desc":
                sql.append("ORDER BY o.OrderID DESC ");
                break;
            case "quantity_asc":
                sql.append("ORDER BY o.Quantity ASC ");
                break;
            case "quantity_desc":
                sql.append("ORDER BY o.Quantity DESC ");
                break;
            case "price_asc":
                sql.append("ORDER BY o.TotalPrice ASC ");
                break;
            case "price_desc":
                sql.append("ORDER BY o.TotalPrice DESC ");
                break;
            case "createdAt_asc":
                sql.append("ORDER BY o.CreatedAt ASC ");
                break;
            case "createdAt_desc":
                sql.append("ORDER BY o.CreatedAt DESC ");
                break;
            case "processeddate_asc":
                sql.append("ORDER BY o.ProcessedDate ASC ");
                break;
            case "processeddate_desc":
                sql.append("ORDER BY o.ProcessedDate DESC ");
                break;
            default:
                sql.append("ORDER BY o.CreatedAt DESC ");
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, sellerId);

            if (farmId != null) {
                ps.setInt(paramIndex++, farmId);
            }

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + search.trim() + "%");
                ps.setString(paramIndex++, "%" + search.trim() + "%");
            }

            int offset = (pageIndex - 1) * pageSize;
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDealerID(rs.getInt("DealerID"));
                order.setSellerID(rs.getInt("SellerID"));
                order.setOfferID(rs.getInt("OfferID"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setTotalPrice(rs.getDouble("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                order.setProcessedDate(rs.getTimestamp("ProcessedDate"));

                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                order.setPigsOffer(offer);

                Farm farm = new Farm();
                farm.setFarmID(rs.getInt("FarmID"));
                farm.setFarmName(rs.getString("FarmName"));
                farm.setLocation(rs.getString("Location"));
                order.setFarm(farm);

                User dealer = new User();
                dealer.setUserID(rs.getInt("DealerID"));
                dealer.setFullName(rs.getString("DealerName"));
                order.setDealer(dealer);

                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public int countPendingOrdersBySellerWithFilter(int sellerId, Integer farmId, String search) {
        int count = 0;
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "WHERE o.SellerID = ? AND o.Status = 'Pending' "
        );

        if (farmId != null) {
            sql.append("AND p.FarmID = ? ");
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, sellerId);

            if (farmId != null) {
                ps.setInt(index++, farmId);
            }

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
                ps.setString(index++, "%" + search.trim() + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<Order> getOrdersExcludingPendingWithFilter(int sellerId, Integer farmId, String search, String status, String sort, int pageIndex, int pageSize) {
        List<Order> orders = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice, o.Status, o.CreatedAt, o.ProcessedDate, "
                + "p.Name AS OfferName, p.ImageURL, p.RetailPrice, "
                + "f.FarmID, f.FarmName, u.FullName AS DealerName "
                + "FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN Farm f ON p.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.DealerID = u.UserID "
                + "WHERE o.SellerID = ? AND o.Status != 'Pending' "
        );

        if (farmId != null) {
            sql.append("AND f.FarmID = ? ");
        }
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND o.Status = ? ");
        }

        switch (sort != null ? sort : "") {
            case "orderid_asc":
                sql.append("ORDER BY o.OrderID ASC ");
                break;
            case "orderid_desc":
                sql.append("ORDER BY o.OrderID DESC ");
                break;
            case "quantity_asc":
                sql.append("ORDER BY o.Quantity ASC ");
                break;
            case "quantity_desc":
                sql.append("ORDER BY o.Quantity DESC ");
                break;
            case "totalprice_asc":
                sql.append("ORDER BY o.TotalPrice ASC ");
                break;
            case "totalprice_desc":
                sql.append("ORDER BY o.TotalPrice DESC ");
                break;
            case "createdat_asc":
                sql.append("ORDER BY o.CreatedAt ASC ");
                break;
            case "createdat_desc":
                sql.append("ORDER BY o.CreatedAt DESC ");
                break;
            case "processeddate_asc":
                sql.append("ORDER BY o.ProcessedDate ASC ");
                break;
            case "processeddate_desc":
                sql.append("ORDER BY o.ProcessedDate DESC ");
                break;
            default:
                sql.append("ORDER BY o.CreatedAt DESC ");
                break;
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int i = 1;
            ps.setInt(i++, sellerId);

            if (farmId != null) {
                ps.setInt(i++, farmId);
            }
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(i++, "%" + search.trim() + "%");
                ps.setString(i++, "%" + search.trim() + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(i++, status);
            }

            int offset = (pageIndex - 1) * pageSize;
            ps.setInt(i++, offset);
            ps.setInt(i, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDealerID(rs.getInt("DealerID"));
                order.setSellerID(rs.getInt("SellerID"));
                order.setOfferID(rs.getInt("OfferID"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setTotalPrice(rs.getDouble("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                order.setProcessedDate(rs.getTimestamp("ProcessedDate"));

                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                order.setPigsOffer(offer);

                Farm farm = new Farm();
                farm.setFarmID(rs.getInt("FarmID"));
                farm.setFarmName(rs.getString("FarmName"));
                order.setFarm(farm);

                User dealer = new User();
                dealer.setUserID(rs.getInt("DealerID"));
                dealer.setFullName(rs.getString("DealerName"));
                order.setDealer(dealer);

                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public int countOrdersExcludingPendingWithFilter(int sellerID, Integer farmId, String search, String status) {
        int count = 0;
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN Farm f ON p.FarmID = f.FarmID "
                + "WHERE o.SellerID = ? AND o.Status != 'Pending' "
        );

        if (farmId != null) {
            sql.append("AND f.FarmID = ? ");
        }
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND o.Status = ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, sellerID);
            if (farmId != null) {
                ps.setInt(index++, farmId);
            }
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
                ps.setString(index++, "%" + search.trim() + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public Order getOrderById(int orderId) {
        Order order = null;
        try {
            String sql = "SELECT * FROM Orders WHERE OrderID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDealerID(rs.getInt("DealerID"));
                order.setSellerID(rs.getInt("SellerID"));
                order.setOfferID(rs.getInt("OfferID"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setTotalPrice(rs.getDouble("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                order.setProcessedDate(rs.getTimestamp("ProcessedDate"));

                // Lấy thông tin Dealer
                UserDAO userDAO = new UserDAO();
                User dealer = userDAO.getUserById(order.getDealerID());
                order.setDealer(dealer);

                // Lấy thông tin Seller
                User seller = userDAO.getUserById(order.getSellerID());
                order.setSeller(seller);

                // Lấy thông tin Offer
                PigsOfferDAO pigsOfferDAO = new PigsOfferDAO();
                PigsOffer offer = pigsOfferDAO.getOfferById(order.getOfferID());
                order.setPigsOffer(offer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql;
        // Các trạng thái được xem là đã xử lý, cần cập nhật ProcessedDate
        if (newStatus.equals("Confirmed") || newStatus.equals("Rejected")
                || newStatus.equals("Canceled") || newStatus.equals("Completed")
                || newStatus.equals("Deposited")) {
            sql = "UPDATE Orders SET Status = ?, ProcessedDate = GETDATE() WHERE OrderID = ?";
        } else {
            sql = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean confirmOrder(int orderID) {
        String sql = "UPDATE Orders SET Status = 'Confirmed', ProcessedDate = GETDATE() "
                + "WHERE OrderID = ? AND Status = 'Pending'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean rejectOrder(int orderID) {
        String sql = "UPDATE Orders SET Status = 'Rejected', ProcessedDate = GETDATE() "
                + "WHERE OrderID = ? AND Status = 'Pending'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isOrderOwnedBySeller(int orderId, int sellerId) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE OrderID = ? AND SellerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, sellerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countOrdersBySeller(int sellerId) {
        String sql = """
                SELECT COUNT(*) 
                FROM Orders o
                JOIN PigsOffer po ON o.OfferID = po.OfferID
                WHERE po.SellerID = ?
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

    public double calculateTotalConfirmedOrDepositedOrdersBySeller(int sellerId) {
        String sql = """
            SELECT COALESCE(SUM(o.TotalPrice), 0)
            FROM Orders o
            JOIN PigsOffer po ON o.OfferID = po.OfferID
            WHERE po.SellerID = ?
              AND (o.Status = 'Confirmed' OR o.Status = 'Deposited')
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public List<OrderStat> getOrderStatsFlexible(String type, Integer year, Integer month, Integer day, int sellerId) {
        List<OrderStat> list = new ArrayList<>();
        String selectField, groupBy, orderBy;

        switch (type) {
            case "year":
                selectField = "YEAR(o.CreatedAt) AS Label";
                groupBy = "YEAR(o.CreatedAt)";
                orderBy = "YEAR(o.CreatedAt)";
                break;
            case "month":
                selectField = "MONTH(o.CreatedAt) AS Label";
                groupBy = "MONTH(o.CreatedAt)";
                orderBy = "MONTH(o.CreatedAt)";
                break;
            case "day":
                selectField = "DAY(o.CreatedAt) AS Label";
                groupBy = "DAY(o.CreatedAt)";
                orderBy = "DAY(o.CreatedAt)";
                break;
            case "hour":
                // Sửa dùng DATEPART cho SQL Server
                selectField = "DATEPART(HOUR, o.CreatedAt) AS Label";
                groupBy = "DATEPART(HOUR, o.CreatedAt)";
                orderBy = "DATEPART(HOUR, o.CreatedAt)";
                break;
            default:
                // fallback: thống kê theo tháng
                selectField = "MONTH(o.CreatedAt) AS Label";
                groupBy = "MONTH(o.CreatedAt)";
                orderBy = "MONTH(o.CreatedAt)";
                break;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(selectField).append(", COUNT(*) AS Total ")
                .append("FROM Orders o ")
                .append("JOIN PigsOffer po ON o.OfferID = po.OfferID ")
                .append("WHERE po.SellerID = ? ");

        List<Object> params = new ArrayList<>();
        params.add(sellerId);

        // Gắn thêm điều kiện theo mức độ filter
        if (type.equals("month") || type.equals("day") || type.equals("hour")) {
            if (year != null) {
                sql.append("AND YEAR(o.CreatedAt) = ? ");
                params.add(year);
            }
        }

        if (type.equals("day") || type.equals("hour")) {
            if (month != null) {
                sql.append("AND MONTH(o.CreatedAt) = ? ");
                params.add(month);
            }
        }

        if (type.equals("hour") && day != null) {
            sql.append("AND DAY(o.CreatedAt) = ? ");
            params.add(day);
        }

        sql.append("GROUP BY ").append(groupBy).append(" ORDER BY ").append(orderBy);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new OrderStat(rs.getString("Label"), rs.getInt("Total")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getOrderQuantity(int orderId) {
        String sql = "SELECT Quantity FROM Orders WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getOrderTotalPrice(int orderId) {
        String sql = "SELECT TotalPrice FROM Orders WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("TotalPrice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void cancelExpiredOrders() {
        String updateQuery = """
                       UPDATE Orders
                       SET status = 'Cancelled'
                       OUTPUT inserted.OrderID, inserted.Quantity, inserted.OfferID
                       WHERE status = 'Pending' AND DATEDIFF(HOUR, CreatedAt, GETDATE()) >= 24
                       """;
        String addQuantityQuery = """
                                  UPDATE PigsOffer
                                  SET Quantity = Quantity + ? WHERE OfferID = ?
                                  """;
        // Get all updated orders
        List<Order> orders = fetchAll((rs) -> {
            Order order = new Order();
            order.setOrderID(rs.getInt("OrderID"));
            order.setOfferID(rs.getInt("OfferID"));
            order.setQuantity(rs.getInt("Quantity"));
            return order;
        }, updateQuery);
        // no order updated
        if (orders.isEmpty()) {
            java.util.logging.Logger.getLogger(OrderDAO.class.getName()).info("Không có đơn nào cần hủy");
            return;
        }
        String addLogQuery = """
                             INSERT INTO ServerLog(content)
                             VALUES (?)
                             """;

        try (PreparedStatement addLogPstm = getConnection().prepareStatement(addLogQuery); PreparedStatement addQuantityPstm = getConnection().prepareStatement(addQuantityQuery);) {
            for (Order order : orders) {
                String message = "Đã hủy đơn hàng id %s".formatted(order.getOrderID());

                // add quantity back to offer
                addQuantityPstm.setInt(1, order.getQuantity());
                addQuantityPstm.setInt(2, order.getOfferID());
                addQuantityPstm.addBatch();

                // add to server log

                java.util.logging.Logger.getLogger(OrderDAO.class.getName()).info(message);
                addLogPstm.setString(1, message);
                addLogPstm.addBatch();
            }

            addLogPstm.executeBatch();
            addQuantityPstm.executeBatch();

        } catch (Exception e) {
            java.util.logging.Logger.getLogger(OrderDAO.class.getName()).severe(e.getMessage());
        }
    }
}
