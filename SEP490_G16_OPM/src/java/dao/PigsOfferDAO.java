/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.PigsOffer;

/**
 *
 * @author dangtuong
 */
public class PigsOfferDAO extends DBContext {

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

    public static void main(String[] args) {
        PigsOfferDAO dao = new PigsOfferDAO();
        ArrayList<PigsOffer> list = dao.getAllPigsOffers();

        if (list.isEmpty()) {
            System.out.println("❌ Không có dữ liệu Offer nào!");
        } else {
            System.out.println("✅ Danh sách Offer lấy từ Database:");
            for (PigsOffer offer : list) {
                System.out.println("Offer ID: " + offer.getOfferID());
                System.out.println("Tên Offer: " + offer.getName());
                System.out.println("Giống Heo: " + offer.getPigBreed());
                System.out.println("Số lượng: " + offer.getQuantity());
                System.out.println("Giá bán lẻ: " + offer.getRetailPrice());
                System.out.println("Ảnh: " + offer.getImageURL());
                System.out.println("Ngày bắt đầu: " + offer.getStartDate());
                System.out.println("------------------------------------------");
            }
        }
    }
}
