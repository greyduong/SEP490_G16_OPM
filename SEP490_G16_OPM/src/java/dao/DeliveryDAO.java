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

}
