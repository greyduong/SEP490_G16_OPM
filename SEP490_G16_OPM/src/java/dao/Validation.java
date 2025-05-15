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

    // Validate tên chào bán
    public static String validateOfferName(String name) {
        if (isEmpty(name)) {
            return "Tên chào bán không được để trống.";
        }
        if (name.length() > 100) {
            return "Tên chào bán không vượt quá 100 ký tự.";
        }
        if (!isValidTextFormat(name)) {
            return "Tên chào bán không được chứa ký tự đặc biệt hoặc 2 dấu cách liên tiếp.";
        }
        return null;
    }

    // Validate giống heo
    public static String validatePigBreed(String breed) {
        if (isEmpty(breed)) {
            return "Giống heo không được để trống.";
        }
        if (breed.length() > 100) {
            return "Giống heo không vượt quá 100 ký tự.";
        }
        if (!isValidTextFormat(breed)) {
            return "Giống heo không hợp lệ.";
        }
        return null;
    }

    // Validate số lượng và số lượng tối thiểu
    public static String validateQuantity(int quantity, int minQuantity) {
        if (quantity <= 0) {
            return "Số lượng phải lớn hơn 0.";
        }
        if (minQuantity <= 0 || minQuantity > quantity) {
            return "Số lượng tối thiểu phải lớn hơn 0 và nhỏ hơn hoặc bằng số lượng.";
        }
        return null;
    }

    // Validate giá
    public static String validatePrices(double retailPrice, double totalPrice, double deposit) {
        if (retailPrice < 0 || totalPrice < 0 || deposit < 0) {
            return "Giá trị không được âm.";
        }
        if (deposit > totalPrice) {
            return "Tiền đặt cọc không được vượt quá tổng giá chào bán.";
        }
        return null;
    }

    // Validate ngày bắt đầu và kết thúc
    public static String validateDates(java.sql.Date start, java.sql.Date end) {
        if (start == null || end == null) {
            return "Ngày bắt đầu và kết thúc không được để trống.";
        }
        if (end.before(start)) {
            return "Ngày kết thúc phải sau ngày bắt đầu.";
        }
        return null;
    }

    // Validate mô tả
    public static String validateOfferDescription(String description) {
        if (description != null && description.length() > 1000) {
            return "Mô tả không vượt quá 1000 ký tự.";
        }
        return null;
    }

    public static String validateRecipientName(String name) {
        if (isEmpty(name)) {
            return "Tên người nhận không được để trống.";
        }
        if (name.length() > 100) {
            return "Tên người nhận không vượt quá 100 ký tự.";
        }
        if (!isValidTextFormat(name)) {
            return "Tên người nhận không được chứa ký tự đặc biệt hoặc 2 dấu cách liên tiếp.";
        }
        return null;
    }

    public static String validatePhone(String phone) {
        if (isEmpty(phone)) {
            return "Số điện thoại không được để trống.";
        }
        if (phone.length() > 20) {
            return "Số điện thoại không được vượt quá 20 ký tự.";
        }
        if (!phone.matches("^\\+?\\d{5,19}$")) {
            return "Số điện thoại chỉ được chứa số và có thể bắt đầu bằng dấu '+'.";
        }
        return null;
    }

    public static String validateDeliveryQuantity(int quantity, int remainingQuantity) {
        if (quantity <= 0) {
            return "Số lượng phải lớn hơn 0.";
        }
        if (quantity > remainingQuantity) {
            return "Số lượng vượt quá phần còn lại của đơn hàng.";
        }
        return null;
    }

    public static String validateDeliveryPrice(double price, double remainingPrice) {
        if (price <= 0) {
            return "Tổng giá phải lớn hơn 0.";
        }
        if (price > remainingPrice) {
            return "Tổng giá vượt quá phần còn lại của đơn hàng.";
        }
        return null;
    }

    public static String validateDeliveryComment(String comment) {
        if (comment != null && comment.length() > 500) {
            return "Ghi chú không được vượt quá 500 ký tự.";
        }
        return null;
    }

    public static String validateCancelReason(String reason) {
        if (isEmpty(reason)) {
            return "Lý do hủy không được để trống.";
        }
        if (reason.length() > 255) {
            return "Lý do hủy không được vượt quá 255 ký tự.";
        }
        if (!reason.matches("^(?!.* {2})[\\p{L}\\d ,.?!-]{1,}$")) {
            return "Lý do hủy không hợp lệ (không được chứa ký tự đặc biệt lạ hoặc 2 dấu cách liên tiếp).";
        }
        return null;
    }

    public static String validateRejectReason(String reason) {
        if (isEmpty(reason)) {
            return "Lý do từ chối không được để trống.";
        }
        if (reason.length() > 255) {
            return "Lý do từ chối không được vượt quá 255 ký tự.";
        }
        // Không chứa ký tự đặc biệt lạ và không có 2 khoảng trắng liên tiếp
        if (!reason.matches("^(?!.* {2})[\\p{L}\\d ,.?!()-]{1,}$")) {
            return "Lý do từ chối không hợp lệ (không được chứa ký tự đặc biệt lạ hoặc 2 dấu cách liên tiếp).";
        }
        return null;
    }

}
