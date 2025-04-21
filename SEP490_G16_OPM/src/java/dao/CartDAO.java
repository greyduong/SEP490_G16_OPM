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
import model.Cart;
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
public class CartDAO extends DBContext {

    public List<Cart> getCartByUserIdWithPaging(int userId, int pageIndex) {
        List<Cart> cartList = new ArrayList<>();
        String sql = "SELECT c.CartID, c.UserID, c.OfferID, c.Quantity, "
                + "       u.FullName, u.Email, u.Phone, u.Address, u.Wallet, "
                + "       p.OfferID AS P_OfferID, p.Name, p.PigBreed, p.RetailPrice, "
                + "       p.ImageURL, p.MinQuantity, p.Quantity AS OfferQuantity "
                + "FROM Cart c "
                + "JOIN UserAccount u ON c.UserID = u.UserID "
                + "JOIN PigsOffer p ON c.OfferID = p.OfferID "
                + "WHERE c.UserID = ? "
                + "ORDER BY c.CartID "
                + "OFFSET ? ROWS FETCH NEXT 3 ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, (pageIndex - 1) * 3);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Cart info
                Cart cart = new Cart();
                cart.setCartID(rs.getInt("CartID"));
                cart.setUserID(rs.getInt("UserID"));
                cart.setOfferID(rs.getInt("OfferID"));
                cart.setQuantity(rs.getInt("Quantity"));

                // User info
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setWallet(rs.getDouble("Wallet"));
                cart.setUser(user);

                // Offer info
                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("P_OfferID"));
                offer.setName(rs.getString("Name"));
                offer.setPigBreed(rs.getString("PigBreed"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setQuantity(rs.getInt("OfferQuantity"));
                cart.setPigsOffer(offer);

                cartList.add(cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartList;
    }

    public int countCartItemsByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM Cart WHERE UserID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addToCart(int userId, int offerId, int quantity) {
        String sqlInfo = "SELECT o.Quantity, o.MinQuantity, ISNULL(c.Quantity, 0) AS CartQuantity "
                + "FROM PigsOffer o LEFT JOIN Cart c ON o.OfferID = c.OfferID AND c.UserID = ? "
                + "WHERE o.OfferID = ?";

        String sqlUpdate = "UPDATE Cart SET Quantity = ? WHERE UserID = ? AND OfferID = ?";
        String sqlInsert = "INSERT INTO Cart (UserID, OfferID, Quantity) VALUES (?, ?, ?)";

        try (PreparedStatement psInfo = connection.prepareStatement(sqlInfo)) {
            psInfo.setInt(1, userId);
            psInfo.setInt(2, offerId);

            try (ResultSet rs = psInfo.executeQuery()) {
                if (rs.next()) {
                    int offerQuantity = rs.getInt("Quantity");
                    int minQuantity = rs.getInt("MinQuantity");
                    int cartQuantity = rs.getInt("CartQuantity");

                    if (quantity < minQuantity || cartQuantity > offerQuantity) {
                        return;
                    }

                    if (cartQuantity > 0) {
                        try (PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate)) {
                            psUpdate.setInt(1, quantity);
                            psUpdate.setInt(2, userId);
                            psUpdate.setInt(3, offerId);
                            psUpdate.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement psInsert = connection.prepareStatement(sqlInsert)) {
                            psInsert.setInt(1, userId);
                            psInsert.setInt(2, offerId);
                            psInsert.setInt(3, quantity);
                            psInsert.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteCartById(int cartId) {
        String sql = "DELETE FROM Cart WHERE CartID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateCartQuantity(int cartId, int quantity) {
        String sql = "UPDATE Cart SET Quantity = ? WHERE CartID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, quantity);
            stm.setInt(2, cartId);
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeCartById(int cartId) {
        String sql = "DELETE FROM Cart WHERE CartID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, cartId);
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
