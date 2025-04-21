package dal;

import model.User;

public class UserMapper {

    public static Mapper<User> toUser() {
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
            String username = rs.getString("Username");
            return new User(userID, roleID, fullName, username, password, email, phone, address, wallet, status);
        };
    }
}
