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
import model.Delivery;

/**
 *
 * @author duong
 */
public class DeliveryDAO extends DBContext {

    public List<Delivery> getDeliveriesByOrderId(int orderId) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM Delivery WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Delivery delivery = new Delivery();
                delivery.setDeliveryID(rs.getInt("DeliveryID"));
                delivery.setOrderID(rs.getInt("OrderID"));
                delivery.setSellerID(rs.getInt("SellerID"));
                delivery.setDealerID(rs.getInt("DealerID"));
                delivery.setDeliveryStatus(rs.getString("DeliveryStatus"));
                delivery.setRecipientName(rs.getString("RecipientName"));
                delivery.setQuantity(rs.getInt("Quantity"));
                delivery.setTotalPrice(rs.getDouble("TotalPrice"));
                delivery.setCreatedAt(rs.getTimestamp("CreatedAt"));
                delivery.setComments(rs.getString("Comments"));
                deliveries.add(delivery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    public int getTotalDeliveredQuantity(int orderId) {
        String sql = "SELECT SUM(Quantity) FROM Delivery WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalDeliveredPrice(int orderId) {
        String sql = "SELECT SUM(TotalPrice) FROM Delivery WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean createDelivery(int orderId, int sellerId, int dealerId, String recipientName, int quantity, double totalPrice, String comments) {
        String sql = "INSERT INTO Delivery (OrderID, SellerID, DealerID, RecipientName, Quantity, TotalPrice, Comments, DeliveryStatus, CreatedAt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending', GETDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, sellerId);
            ps.setInt(3, dealerId);
            ps.setString(4, recipientName);
            ps.setInt(5, quantity);
            ps.setDouble(6, totalPrice);
            ps.setString(7, comments);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getDealerIdByDeliveryId(int deliveryID) {
        String sql = "SELECT DealerID FROM Delivery WHERE DeliveryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, deliveryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("DealerID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean confirmDelivery(int deliveryID) {
        String sql = "UPDATE Delivery SET DeliveryStatus = 'Confirmed' WHERE DeliveryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, deliveryID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getOrderIdByDeliveryId(int deliveryID) {
        String sql = "SELECT OrderID FROM Delivery WHERE DeliveryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, deliveryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("OrderID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double calculateTotalRevenueFromDeliveryBySeller(int sellerId) {
    String sql = """
            SELECT COALESCE(SUM(d.TotalPrice), 0)
            FROM Delivery d
            JOIN Orders o ON d.OrderID = o.OrderID
            JOIN PigsOffer po ON o.OfferID = po.OfferID
            WHERE po.SellerID = ?
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

}
