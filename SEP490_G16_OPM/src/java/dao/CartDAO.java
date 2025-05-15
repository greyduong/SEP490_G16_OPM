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
import model.Category;
import model.Farm;
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
public class CartDAO extends DBContext {

    public List<Cart> getCartByUserIdWithFilter(int userId, int pageIndex, int pageSize, String search, String sort) {
        List<Cart> cartList = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT c.CartID, c.UserID, c.OfferID, c.Quantity, "
                + "u.FullName, u.Email, u.Phone, u.Address, u.Wallet, "
                + "p.OfferID AS P_OfferID, p.Name AS OfferName, p.PigBreed, p.RetailPrice, "
                + "p.ImageURL, p.MinQuantity, p.Quantity AS OfferQuantity, "
                + "p.MinDeposit, p.TotalOfferPrice, p.Description, p.StartDate, p.EndDate, "
                + "f.FarmID, f.FarmName, cat.CategoryID, cat.Name AS CategoryName "
                + "FROM Cart c "
                + "JOIN UserAccount u ON c.UserID = u.UserID "
                + "JOIN PigsOffer p ON c.OfferID = p.OfferID "
                + "JOIN Farm f ON p.FarmID = f.FarmID "
                + "JOIN Category cat ON p.CategoryID = cat.CategoryID "
                + "WHERE c.UserID = ? "
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND p.Name LIKE ? ");
        }

        switch (sort != null ? sort.trim() : "") {
            case "quantity_asc" ->
                sql.append("ORDER BY c.Quantity ASC ");
            case "quantity_desc" ->
                sql.append("ORDER BY c.Quantity DESC ");
            case "price_asc" ->
                sql.append("ORDER BY p.RetailPrice ASC ");
            case "price_desc" ->
                sql.append("ORDER BY p.RetailPrice DESC ");
            case "total_asc" ->
                sql.append("ORDER BY (c.Quantity * p.RetailPrice) ASC ");
            case "total_desc" ->
                sql.append("ORDER BY (c.Quantity * p.RetailPrice) DESC ");
            default ->
                sql.append("ORDER BY c.CartID ");
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + search.trim() + "%");
            }
            ps.setInt(paramIndex++, (pageIndex - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cart cart = new Cart();
                cart.setCartID(rs.getInt("CartID"));
                cart.setUserID(rs.getInt("UserID"));
                cart.setOfferID(rs.getInt("OfferID"));
                cart.setQuantity(rs.getInt("Quantity"));

                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setWallet(rs.getDouble("Wallet"));
                cart.setUser(user);

                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("P_OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setPigBreed(rs.getString("PigBreed"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setQuantity(rs.getInt("OfferQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                offer.setStartDate(rs.getDate("StartDate"));
                offer.setEndDate(rs.getDate("EndDate"));

                Farm farm = new Farm();
                farm.setFarmID(rs.getInt("FarmID"));
                farm.setFarmName(rs.getString("FarmName"));
                offer.setFarm(farm);

                Category cat = new Category();
                cat.setCategoryID(rs.getInt("CategoryID"));
                cat.setName(rs.getString("CategoryName"));
                offer.setCategory(cat);

                cart.setPigsOffer(offer);
                cartList.add(cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartList;
    }

    public int countCartItemsByUserWithFilter(int userId, String search) {
        String sql = "SELECT COUNT(*) FROM Cart c JOIN PigsOffer p ON c.OfferID = p.OfferID WHERE c.UserID = ? ";
        if (search != null && !search.trim().isEmpty()) {
            sql += "AND p.Name LIKE ? ";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(2, "%" + search.trim() + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Cart getCartById(int cartId) {
        String sql = "SELECT c.CartID, c.UserID, c.OfferID, c.Quantity, "
                + "u.FullName, u.Email, u.Phone, u.Address, u.Wallet, "
                + "p.Name AS OfferName, p.Quantity AS OfferQuantity, p.MinQuantity, p.RetailPrice "
                + "FROM Cart c "
                + "JOIN UserAccount u ON c.UserID = u.UserID "
                + "JOIN PigsOffer p ON c.OfferID = p.OfferID "
                + "WHERE c.CartID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cart cart = new Cart();
                    cart.setCartID(rs.getInt("CartID"));
                    cart.setQuantity(rs.getInt("Quantity"));

                    // Set User
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhone(rs.getString("Phone"));
                    user.setAddress(rs.getString("Address"));
                    user.setWallet(rs.getDouble("Wallet"));
                    cart.setUser(user);

                    // Set PigsOffer
                    PigsOffer offer = new PigsOffer();
                    offer.setOfferID(rs.getInt("OfferID"));
                    offer.setName(rs.getString("OfferName"));
                    offer.setQuantity(rs.getInt("OfferQuantity"));
                    offer.setMinQuantity(rs.getInt("MinQuantity"));
                    offer.setRetailPrice(rs.getDouble("RetailPrice"));
                    cart.setPigsOffer(offer);

                    return cart;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void addToCart(int userId, int offerId, int quantity) {
        String sqlOffer = "SELECT Quantity, MinQuantity FROM PigsOffer WHERE OfferID = ?";
        String sqlCheck = "SELECT Quantity FROM Cart WHERE UserID = ? AND OfferID = ?";
        String sqlUpdate = "UPDATE Cart SET Quantity = ? WHERE UserID = ? AND OfferID = ?";
        String sqlInsert = "INSERT INTO Cart (UserID, OfferID, Quantity) VALUES (?, ?, ?)";

        try (PreparedStatement psOffer = connection.prepareStatement(sqlOffer)) {
            psOffer.setInt(1, offerId);
            try (ResultSet rsOffer = psOffer.executeQuery()) {
                if (rsOffer.next()) {
                    int maxQuantity = rsOffer.getInt("Quantity");
                    int minQuantity = rsOffer.getInt("MinQuantity");

                    // Kiểm tra số lượng nhập vào có hợp lệ không
                    if (quantity < minQuantity || quantity > maxQuantity) {
                        // Không hợp lệ thì không thêm, có thể ném Exception hoặc log
                        System.out.println("Quantity không hợp lệ: " + quantity);
                        return;
                    }

                    // Tiếp tục kiểm tra xem có trong cart chưa
                    try (PreparedStatement psCheck = connection.prepareStatement(sqlCheck)) {
                        psCheck.setInt(1, userId);
                        psCheck.setInt(2, offerId);

                        try (ResultSet rsCheck = psCheck.executeQuery()) {
                            if (rsCheck.next()) {
                                // Update cart với số lượng mới
                                try (PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate)) {
                                    psUpdate.setInt(1, quantity);
                                    psUpdate.setInt(2, userId);
                                    psUpdate.setInt(3, offerId);
                                    psUpdate.executeUpdate();
                                }
                            } else {
                                // Insert mới vào cart
                                try (PreparedStatement psInsert = connection.prepareStatement(sqlInsert)) {
                                    psInsert.setInt(1, userId);
                                    psInsert.setInt(2, offerId);
                                    psInsert.setInt(3, quantity);
                                    psInsert.executeUpdate();
                                }
                            }
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

    public PigsOffer getPigsOfferByCartId(int cartId) {
        String sql = "SELECT p.* FROM Cart c "
                + "JOIN PigsOffer p ON c.OfferID = p.OfferID "
                + "WHERE c.CartID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PigsOffer offer = new PigsOffer();
                    offer.setOfferID(rs.getInt("OfferID"));
                    offer.setName(rs.getString("Name"));
                    offer.setQuantity(rs.getInt("Quantity"));
                    offer.setMinQuantity(rs.getInt("MinQuantity"));
                    offer.setRetailPrice(rs.getDouble("RetailPrice"));
                    offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                    offer.setMinDeposit(rs.getDouble("MinDeposit"));
                    offer.setDescription(rs.getString("Description"));
                    offer.setStartDate(rs.getDate("StartDate"));
                    offer.setEndDate(rs.getDate("EndDate"));
                    // (Tuỳ bạn có cần thêm Farm, Category, Seller không)

                    return offer;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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
