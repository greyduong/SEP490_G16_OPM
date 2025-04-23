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

    // Kiểm tra không chứa ký tự đặc biệt và không có 2 dấu cách liên tiếp
    private static boolean isValidTextFormat(String input) {
        return input.matches("^(?!.* {2})[\\p{L}\\d ]+$");
    }

    // Viết hoa chữ cái đầu của mỗi từ, còn lại viết thường
    private static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (String word : input.trim().toLowerCase().split("\\s+")) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1)).append(" ");
            }
        }
        return result.toString().trim();
    }

    public static String validateFarmName(String name) {
        if (isEmpty(name)) {
            return "Tên trang trại không được để trống.";
        }
        if (name.length() > 100) {
            return "Tên trang trại không vượt quá 100 ký tự.";
        }
        if (!isValidTextFormat(name)) {
            return "Tên không chứa ký tự đặc biệt hoặc 2 dấu cách liên tiếp.";
        }
        return null;
    }

    public static String validateFarmLocation(String location) {
        if (isEmpty(location)) {
            return "Vị trí không được để trống.";
        }
        if (location.length() > 255) {
            return "Vị trí không vượt quá 255 ký tự.";
        }
        if (!isValidTextFormat(location)) {
            return "Vị trí không chứa ký tự đặc biệt hoặc 2 dấu cách liên tiếp.";
        }
        return null;
    }

    public static String validateFarmDescription(String description) {
        if (description != null && description.length() > 1000) {
            return "Mô tả không vượt quá 1000 ký tự.";
        }
        return null;
    }

    public static String formatToTitleCase(String input) {
        return toTitleCase(input);
    }
}
