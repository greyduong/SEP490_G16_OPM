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

    public List<Order> getOrdersByBuyerId(int dealerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice, o.Status, o.CreatedAt, "
                + "       p.Name AS OfferName, p.ImageURL, p.RetailPrice, u.FullName AS SellerName "
                + "FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN UserAccount u ON o.SellerID = u.UserID "
                + "WHERE o.DealerID = ? "
                + "ORDER BY o.CreatedAt DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dealerId);
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

                // Set pigs offer info
                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                order.setPigsOffer(offer);

                // Set seller info
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

    public List<Order> getPendingOrdersBySellerId(int sellerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice, o.Status, o.CreatedAt, "
                + "       p.Name AS OfferName, p.ImageURL, p.RetailPrice, u.FullName AS DealerName "
                + "FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN UserAccount u ON o.DealerID = u.UserID "
                + "WHERE o.SellerID = ? AND o.Status = 'Pending' "
                + "ORDER BY o.CreatedAt DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sellerId);
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

                // Set pigs offer info
                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                order.setPigsOffer(offer);

                // Set dealer info
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

    public List<Order> getOrdersExcludingPending(int sellerID) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice, o.Status, o.CreatedAt, "
                + "       p.Name AS OfferName, p.ImageURL, p.RetailPrice, u.FullName AS DealerName "
                + "FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN UserAccount u ON o.DealerID = u.UserID "
                + "WHERE o.SellerID = ? AND o.Status != 'Pending' "
                + "ORDER BY o.CreatedAt DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sellerID);
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

                // Set offer details
                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                order.setPigsOffer(offer);

                // Set dealer info
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

    public boolean confirmOrder(int orderID) {
        String sql = "UPDATE Orders SET Status = 'Confirmed' WHERE OrderID = ? AND Status = 'Pending'"; // Only update if the order is 'Pending'
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;  // Return true if one or more rows were updated
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;  // Return false if the update fails
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

    public static void main(String[] args) {
        OrderDAO dao = new OrderDAO();

        // Thử nghiệm với từng loại thống kê
        int sellerId = 4; // ⚠️ Thay bằng ID người bán hợp lệ trong database

        System.out.println("📊 Thống kê theo năm:");
        List<OrderStat> statsByYear = dao.getOrderStatsFlexible("year", null, null, null, sellerId);
        statsByYear.forEach(stat -> System.out.println(stat.getLabel() + " - " + stat.getTotal()));

        System.out.println("\n📊 Thống kê theo tháng trong năm 2024:");
        List<OrderStat> statsByMonth = dao.getOrderStatsFlexible("month", 2024, null, null, sellerId);
        statsByMonth.forEach(stat -> System.out.println(stat.getLabel() + " - " + stat.getTotal()));

        System.out.println("\n📊 Thống kê theo ngày trong tháng 4/2025:");
        List<OrderStat> statsByDay = dao.getOrderStatsFlexible("day", 2025, 4, null, sellerId);
        statsByDay.forEach(stat -> System.out.println(stat.getLabel() + " - " + stat.getTotal()));

        System.out.println("\n📊 Thống kê theo giờ trong ngày 24/4/2025:");
        List<OrderStat> statsByHour = dao.getOrderStatsFlexible("hour", 2025, 4, 24, sellerId);
        statsByHour.forEach(stat -> System.out.println(stat.getLabel() + " - " + stat.getTotal()));
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
        String selectSql = "SELECT OfferID, Quantity FROM Orders "
                + "WHERE status = 'Pending' AND DATEDIFF(HOUR, CreatedAt, GETDATE()) >= 24";

        String updateOfferSql = "UPDATE PigsOffer SET Quantity = Quantity + ? WHERE OfferID = ?";

        String cancelSql = "UPDATE Orders "
                + "SET status = 'Cancelled' "
                + "WHERE status = 'Pending' AND DATEDIFF(HOUR, CreatedAt, GETDATE()) >= 24";

        try (
                PreparedStatement selectStm = connection.prepareStatement(selectSql); PreparedStatement updateOfferStm = connection.prepareStatement(updateOfferSql); PreparedStatement cancelStm = connection.prepareStatement(cancelSql);) {
            ResultSet rs = selectStm.executeQuery();

            int counter = 0;

            while (rs.next()) {
                int offerId = rs.getInt("OfferID");
                int quantity = rs.getInt("Quantity");

                updateOfferStm.setInt(1, quantity);
                updateOfferStm.setInt(2, offerId);
                updateOfferStm.addBatch();
                counter++;
            }

            if (counter > 0) {
                updateOfferStm.executeBatch(); // Cộng lại số lượng
                int affected = cancelStm.executeUpdate(); // Huỷ đơn hàng
                System.out.println("🔁 Đã huỷ " + affected + " đơn và hoàn số lượng về offer.");
            } else {
                System.out.println("⏳ Không có đơn hàng nào cần huỷ.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
