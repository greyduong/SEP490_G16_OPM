/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
                delivery.setPhone(rs.getString("Phone"));
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

    public int getTotalQuantityByStatuses(int orderId) {
        String sql = "SELECT SUM(Quantity) FROM Delivery "
                + "WHERE OrderID = ? AND DeliveryStatus IN ('Confirmed', 'Pending')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalPriceByStatuses(int orderId) {
        String sql = "SELECT SUM(TotalPrice) FROM Delivery "
                + "WHERE OrderID = ? AND DeliveryStatus IN ('Confirmed', 'Pending')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getTotalDeliveredQuantity(int orderId) {
        String sql = "SELECT SUM(Quantity) FROM Delivery WHERE OrderID = ? AND DeliveryStatus = 'Confirmed'";
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
        String sql = "SELECT SUM(TotalPrice) FROM Delivery WHERE OrderID = ? AND DeliveryStatus = 'Confirmed'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getDeliveryQuantity(int deliveryID) {
        String sql = "SELECT Quantity FROM Delivery WHERE DeliveryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, deliveryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getDeliveryTotalPrice(int deliveryID) {
        String sql = "SELECT TotalPrice FROM Delivery WHERE DeliveryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, deliveryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("TotalPrice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int createDelivery(int orderId, int sellerId, int dealerId, String recipientName, String phone, int quantity, double totalPrice, String comments) {
        String sql = "INSERT INTO Delivery (OrderID, SellerID, DealerID, RecipientName, Phone, Quantity, TotalPrice, Comments, DeliveryStatus, CreatedAt) "
                + "OUTPUT INSERTED.DeliveryID "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Pending', GETDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, sellerId);
            ps.setInt(3, dealerId);
            ps.setString(4, recipientName);
            ps.setString(5, phone);
            ps.setInt(6, quantity);
            ps.setDouble(7, totalPrice);
            ps.setString(8, comments);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("DeliveryID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
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

    public boolean updateDeliveryStatus(int deliveryID, String status) {
        String sql = "UPDATE Delivery SET DeliveryStatus = ? WHERE DeliveryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, deliveryID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean appendToDeliveryComments(int deliveryId, String note) {
        String sql = "UPDATE Delivery SET comments = ISNULL(comments, '') + CHAR(13) + ? WHERE deliveryID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, note);
            stm.setInt(2, deliveryId);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean confirmDelivery(int deliveryID) {
        String sql = "UPDATE Delivery SET DeliveryStatus = 'Confirmed' WHERE DeliveryID = ? AND DeliveryStatus = 'Pending'";
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

    public String getOrderStatusByDeliveryId(int deliveryId) {
        String status = null;
        String sql = "SELECT o.Status FROM Delivery d "
                + "JOIN Orders o ON d.OrderID = o.OrderID "
                + "WHERE d.DeliveryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, deliveryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("Status");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public String getDeliveryStatusById(int deliveryID) {
        String sql = "SELECT DeliveryStatus FROM Delivery WHERE DeliveryID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, deliveryID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("DeliveryStatus");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public void updateCompletedOrders() {
        executeUpdate("""
                      UPDATE Orders
                      SET Status = 'Completed', Note = N'Đơn hàng đã hoàn tất'
                      WHERE OrderID IN (
                      	SELECT o1.OrderID FROM (
                      		SELECT OrderID, SUM(Quantity) AS 'SumQuantity', SUM(TotalPrice) AS 'SumTotalPrice'
                      		FROM Delivery WHERE DeliveryStatus = 'Confirmed'
                      		GROUP BY OrderID
                      	) o2 JOIN Orders o1 ON o1.OrderID = o2.OrderID
                      WHERE o2.SumQuantity >= o1.Quantity AND o2.SumTotalPrice >= o1.TotalPrice)
                      """);
    }

    public void confirmDeliveries(List<Delivery> deliveries) {
        var statement = batch("UPDATE Delivery SET DeliveryStatus = 'Confirmed' WHERE DeliveryID = ?");
        List<Integer> orderIds = deliveries.stream().map(e -> e.getOrderID()).distinct().toList();
        var statement2 = batch("UPDATE Orders SET Status = 'Processing', Note = N'Đơn đang được xử lý' WHERE OrderID = ?");
        deliveries.forEach(delivery -> {
            statement.params(delivery.getDeliveryID());
        });
        orderIds.forEach(orderId -> {
            statement2.params("Processing", orderId);
        });
        statement.execute();
        statement2.execute();
        updateCompletedOrders();
    }

    public void updateDeliveriesStatus(List<Delivery> deliveries, String status) {
        var statement = batch("UPDATE Delivery SET DeliveryStatus = ? WHERE DeliveryID = ?");
        deliveries.forEach(delivery -> {
            statement.params(status, delivery.getDeliveryID());
        });
        statement.execute();
    }

    public List<Delivery> getReadyDeliveries() {
        return fetchAll(rs -> {
            Delivery delivery = new Delivery();
            delivery.setDeliveryID(rs.getInt("DeliveryID"));
            delivery.setOrderID(rs.getInt("OrderID"));
            delivery.setSellerID(rs.getInt("SellerID"));
            delivery.setDealerID(rs.getInt("DealerID"));
            delivery.setDeliveryStatus(rs.getString("DeliveryStatus"));
            delivery.setRecipientName(rs.getString("RecipientName"));
            delivery.setPhone(rs.getString("Phone"));
            delivery.setQuantity(rs.getInt("Quantity"));
            delivery.setTotalPrice(rs.getDouble("TotalPrice"));
            delivery.setCreatedAt(rs.getTimestamp("CreatedAt"));
            delivery.setComments(rs.getString("Comments"));
            return delivery;
        }, "SELECT * FROM Delivery WHERE DeliveryStatus = 'Pending' AND DATEDIFF(HOUR, GETDATE(), CreatedAt) >= 24");
    }
}
