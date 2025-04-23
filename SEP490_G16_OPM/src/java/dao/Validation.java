/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author tuan
 */
public class Validation {

    // Check if a string is null or empty
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    // Check if email is valid using a basic regex
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Check if password meets criteria
    public static boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");
    }

    public static boolean passwordsMatch(String p1, String p2) {
        return p1 != null && p1.equals(p2);
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[A-Za-z0-9_]{3,20}$");
    }

    // Validate Login Input
    public static String validateLogin(String username, String password) {
        if (isEmpty(username) || isEmpty(password)) {
            return "Please fill in all fields.";
        }
        if (!isValidUsername(username)) {
            return "Invalid username format.";
        }
        return null;
    }

    // Validate Register Input
    public static String validateRegister(String name, String username, String password, String confirmPassword) {
        if (isEmpty(name) || isEmpty(username) || isEmpty(password) || isEmpty(confirmPassword)) {
            return "Please fill in all fields.";
        }
        if (!isValidUsername(username)) {
            return "Username can only contain letters, numbers, and underscores.";
        }
        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters long and include letters and numbers.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        return null;
    }

    // Kiểm tra họ tên hợp lệ: chỉ chữ cái (unicode) và khoảng trắng, tối thiểu 2 ký tự
    public static boolean isValidFullName(String fullname) {
        return fullname != null && fullname.matches("^[\\p{L} ]{2,50}$");
    }
}
