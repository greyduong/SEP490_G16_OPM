package dao;

import model.User;
import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO extends DBContext {

    public static Mapper<User> mapper() {
        return (rs) -> {
            int userID = rs.getInt("UserID");
            int roleID = rs.getInt("RoleID");
            String fullName = rs.getString("FullName");
            String password = rs.getString("Password");
            String email = rs.getString("Email");
            String phone = rs.getString("Phone");
            String address = rs.getString("Address");
            double wallet = rs.getDouble("Wallet");
            String status = rs.getString("Status");
            User user = new User(userID, roleID, fullName, fullName, password, email, phone, address, wallet, status);
            return user;
        };
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(fetchOne(mapper(), "SELECT * FROM UserAccount WHERE UserID = ?", id));
    }

    public int countSearch(String pattern) {
        return count("SELECT COUNT(*) FROM UserAccount WHERE Username LIKE ?", "%" + pattern + "%");
    }

    public List<User> search(String pattern, int page, int size) {
        long offset = (page - 1) * size;
        return fetchAll(
                mapper(),
                "SELECT * FROM UserAccount WHERE Username LIKE ? ORDER BY UserID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
                "%" + pattern + "%",
                offset,
                size);
    }

    PreparedStatement stm;
    ResultSet rs;

    // Check if username exists
    public boolean checkExistsUsername(String username) {
        try {
            String sql = "SELECT * FROM UserAccount WHERE Username = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            rs = stm.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("checkExistsUsername: " + e.getMessage());
        }
        return false;
    }
    // Check if email exists

    public boolean checkExistsEmail(String email) {
        try {
            String sql = "SELECT * FROM UserAccount WHERE Email = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, email);
            rs = stm.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("checkExistsEmail: " + e.getMessage());
        }
        return false;
    }

    // Login check
    public boolean checkUser(String username, String password) {
        try {
            String sql = "SELECT * FROM UserAccount WHERE Username = ? AND Password = ? AND Status = 'Active'";
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            rs = stm.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("checkUser: " + e.getMessage());
        }
        return false;
    }

    // Register new user
    public boolean addNewUser(User user) {
        try {
            String sql = "INSERT INTO UserAccount (RoleID, FullName, Username, Password, Email, Phone, Address, Wallet, Status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, user.getRoleID());
            stm.setString(2, user.getFullName());
            stm.setString(3, user.getUsername());
            stm.setString(4, user.getPassword());
            stm.setString(5, user.getEmail());
            stm.setString(6, user.getPhone());
            stm.setString(7, user.getAddress());
            stm.setDouble(8, user.getWallet());
            stm.setString(9, user.getStatus());

            int rows = stm.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("addNewUser: " + e.getMessage());
        }
        return false;
    }

    // Get user by username
    public User getUserByUsername(String username) {
        User user = null;
        try {
            String sql = "SELECT * FROM UserAccount WHERE Username = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            rs = stm.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt("UserID");
                int roleID = rs.getInt("RoleID");
                String fullName = rs.getString("FullName");
                String password = rs.getString("Password");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                double wallet = rs.getDouble("Wallet");
                String status = rs.getString("Status");

                user = new User(userID, roleID, fullName, username, password, email, phone, address, wallet, status);
            }
        } catch (Exception e) {
            System.out.println("getUserByUsername: " + e.getMessage());
        }
        return user;
    }

    public User getUserById(int id) {
        User user = null;
        try {
            String sql = "SELECT * FROM UserAccount WHERE UserID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM UserAccount";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setFullName(rs.getString("FullName"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password")); // optional to hide
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setWallet(rs.getDouble("Wallet"));
                user.setStatus(rs.getString("Status"));
                list.add(user);
            }
        } catch (Exception e) {
            System.out.println("getAllUsers: " + e.getMessage());
        }
        return list;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE UserAccount SET FullName = ?, Email = ?, Phone = ?, Address = ? WHERE UserID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());
            ps.setInt(5, user.getUserID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("updateUser: " + e.getMessage());
        }
        return false;
    }

    public User getUserByEmail(String email) {
        User user = null;
        try {
            String sql = "SELECT * FROM UserAccount WHERE Email = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setFullName(rs.getString("FullName"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setWallet(rs.getDouble("Wallet"));
                user.setStatus(rs.getString("Status"));
            }
        } catch (Exception e) {
            System.out.println("getUserByEmail: " + e.getMessage());
        }
        return user;
    }

    public boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE UserAccount SET Password = ? WHERE Username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("updatePassword: " + e.getMessage());
        }
        return false;
    }

}
