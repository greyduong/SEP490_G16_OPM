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

    public void cancelExpiredOrders() {
        String sql = "UPDATE Orders "
                + "SET status = 'Cancelled' "
                + "WHERE status = 'Pending' AND DATEDIFF(HOUR, create_time, GETDATE()) >= 24";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int affectedRows = ps.executeUpdate();
            System.out.println("🔁 Số đơn hàng bị hủy: " + affectedRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
