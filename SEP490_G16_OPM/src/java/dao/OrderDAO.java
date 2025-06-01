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
import model.Farm;
import model.Order;
import model.OrderStat;
import model.Page;
import model.PigsOffer;
import model.User;

/**
 *
 * @author duong
 */
public class OrderDAO extends DBContext {

    public List<Order> getAllOrdersWithFilterAndPaging(String search, String status, int farmId, String sort, int pageIndex, int pageSize) {
        List<Order> orderList = new ArrayList<>();
        int offset = (pageIndex - 1) * pageSize;

        StringBuilder sql = new StringBuilder("""
        SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice,
               o.Status, o.Note, o.CreatedAt, o.ProcessedDate,
               p.Name AS OfferName, p.RetailPrice, p.Quantity AS OfferQuantity,
               p.StartDate, p.EndDate, p.ImageURL,
               f.FarmID, f.FarmName, f.Location AS FarmLocation,
               u.FullName AS DealerName, u.Phone AS DealerPhone, u.Address AS DealerAddress,
               us.FullName AS SellerName, us.Phone AS SellerPhone, us.Address AS SellerAddress
        FROM Orders o
        JOIN PigsOffer p ON o.OfferID = p.OfferID
        JOIN Farm f ON p.FarmID = f.FarmID
        JOIN UserAccount u ON o.DealerID = u.UserID
        JOIN UserAccount us ON o.SellerID = us.UserID
        WHERE 1 = 1
    """);

        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND o.Status = ? ");
            params.add(status.trim());
        }

        if (farmId > 0) {
            sql.append(" AND f.FarmID = ? ");
            params.add(farmId);
        }

        // Order By
        String orderBy = switch (sort != null ? sort : "") {
            case "orderid_asc" ->
                " ORDER BY o.OrderID ASC ";
            case "orderid_desc" ->
                " ORDER BY o.OrderID DESC ";
            case "quantity_asc" ->
                " ORDER BY o.Quantity ASC ";
            case "quantity_desc" ->
                " ORDER BY o.Quantity DESC ";
            case "totalprice_asc" ->
                " ORDER BY o.TotalPrice ASC ";
            case "totalprice_desc" ->
                " ORDER BY o.TotalPrice DESC ";
            case "createdat_asc" ->
                " ORDER BY o.CreatedAt ASC ";
            case "createdat_desc" ->
                " ORDER BY o.CreatedAt DESC ";
            case "processeddate_asc" ->
                " ORDER BY o.ProcessedDate ASC ";
            case "processeddate_desc" ->
                " ORDER BY o.ProcessedDate DESC ";
            default ->
                " ORDER BY o.CreatedAt DESC ";
        };

        sql.append(orderBy);
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
        params.add(offset);
        params.add(pageSize);

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stm.executeQuery();
            DeliveryDAO deliveryDAO = new DeliveryDAO(); // cần truyền connection nếu dùng connection field

            while (rs.next()) {
                Order o = new Order();
                o.setOrderID(rs.getInt("OrderID"));
                o.setDealerID(rs.getInt("DealerID"));
                o.setSellerID(rs.getInt("SellerID"));
                o.setOfferID(rs.getInt("OfferID"));
                o.setQuantity(rs.getInt("Quantity"));
                o.setTotalPrice(rs.getDouble("TotalPrice"));
                o.setStatus(rs.getString("Status"));
                o.setNote(rs.getString("Note"));
                o.setCreatedAt(rs.getTimestamp("CreatedAt"));
                o.setProcessedDate(rs.getTimestamp("ProcessedDate"));

                // Buyer
                User dealer = new User();
                dealer.setUserID(rs.getInt("DealerID"));
                dealer.setFullName(rs.getString("DealerName"));
                dealer.setPhone(rs.getString("DealerPhone"));
                dealer.setAddress(rs.getString("DealerAddress"));
                o.setDealer(dealer);

                // Seller
                User seller = new User();
                seller.setUserID(rs.getInt("SellerID"));
                seller.setFullName(rs.getString("SellerName"));
                seller.setPhone(rs.getString("SellerPhone"));
                seller.setAddress(rs.getString("SellerAddress"));
                o.setSeller(seller);

                // Offer
                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setQuantity(rs.getInt("OfferQuantity"));
                offer.setStartDate(rs.getDate("StartDate"));
                offer.setEndDate(rs.getDate("EndDate"));
                offer.setImageURL(rs.getString("ImageURL"));

                // Farm
                Farm farm = new Farm();
                farm.setFarmID(rs.getInt("FarmID"));
                farm.setFarmName(rs.getString("FarmName"));
                farm.setLocation(rs.getString("FarmLocation"));
                offer.setFarm(farm);
                o.setFarm(farm);

                o.setPigsOffer(offer);

                // Delivery
                List<Delivery> deliveries = deliveryDAO.getDeliveriesByOrderId(o.getOrderID());
                o.setDeliveries(deliveries);

                orderList.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public int countAllOrdersWithFilter(String search, String status, int farmId) {
        int total = 0;
        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) FROM Orders o
        JOIN PigsOffer p ON o.OfferID = p.OfferID
        JOIN Farm f ON p.FarmID = f.FarmID
        WHERE 1 = 1
    """);

        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND o.Status = ? ");
            params.add(status);
        }

        if (farmId > 0) {
            sql.append(" AND f.FarmID = ? ");
            params.add(farmId);
        }

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public void insertOrder(int dealerId, int sellerId, int offerId, int quantity, double totalPrice) {
        String sql = "INSERT INTO Orders (DealerID, SellerID, OfferID, Quantity, TotalPrice) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dealerId);
            ps.setInt(2, sellerId);
            ps.setInt(3, offerId);
            ps.setInt(4, quantity);
            ps.setDouble(5, totalPrice);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertOrder(int dealerId, int sellerId, int offerId, int quantity, double totalPrice, int farmId) {
        String sql = "INSERT INTO Orders (DealerID, SellerID, OfferID, Quantity, TotalPrice, FarmID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dealerId);
            ps.setInt(2, sellerId);
            ps.setInt(3, offerId);
            ps.setInt(4, quantity);
            ps.setDouble(5, totalPrice);
            ps.setInt(6, farmId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByBuyerWithFilter(int buyerId, String search, String status, String sort, int pageIndex, int pageSize) {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice, o.Status, o.CreatedAt, o.ProcessedDate, o.Note, "
                + "p.Name AS OfferName, p.ImageURL, p.RetailPrice, p.MinQuantity, p.MinDeposit, p.TotalOfferPrice, p.Description, "
                + "f.FarmID, f.FarmName, f.Location, u.FullName AS SellerName "
                + "FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN Farm f ON p.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.SellerID = u.UserID "
                + "WHERE o.DealerID = ? "
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND o.Status = ? ");
        }

        switch (sort != null ? sort : "") {
            case "orderid_asc":
                sql.append("ORDER BY o.OrderID ASC ");
                break;
            case "orderid_desc":
                sql.append("ORDER BY o.OrderID DESC ");
                break;
            case "quantity_asc":
                sql.append("ORDER BY o.Quantity ASC ");
                break;
            case "quantity_desc":
                sql.append("ORDER BY o.Quantity DESC ");
                break;
            case "totalprice_asc":
                sql.append("ORDER BY o.TotalPrice ASC ");
                break;
            case "totalprice_desc":
                sql.append("ORDER BY o.TotalPrice DESC ");
                break;
            case "createdat_asc":
                sql.append("ORDER BY o.CreatedAt ASC ");
                break;
            case "createdat_desc":
                sql.append("ORDER BY o.CreatedAt DESC ");
                break;
            case "processeddate_asc":
                sql.append("ORDER BY o.ProcessedDate ASC ");
                break;
            case "processeddate_desc":
                sql.append("ORDER BY o.ProcessedDate DESC ");
                break;
            default:
                sql.append("ORDER BY o.CreatedAt DESC ");
                break;
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, buyerId);

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
                ps.setString(index++, "%" + search.trim() + "%");
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            int offset = (pageIndex - 1) * pageSize;
            ps.setInt(index++, offset);
            ps.setInt(index, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDealerID(rs.getInt("DealerID"));
                order.setSellerID(rs.getInt("SellerID"));
                order.setOfferID(rs.getInt("OfferID"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setTotalPrice(rs.getDouble("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                order.setProcessedDate(rs.getTimestamp("ProcessedDate"));
                order.setNote(rs.getString("Note"));

                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                order.setPigsOffer(offer);

                Farm farm = new Farm();
                farm.setFarmID(rs.getInt("FarmID"));
                farm.setFarmName(rs.getString("FarmName"));
                farm.setLocation(rs.getString("Location"));
                order.setFarm(farm);

                User seller = new User();
                seller.setUserID(rs.getInt("SellerID"));
                seller.setFullName(rs.getString("SellerName"));
                order.setSeller(seller);

                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public int countOrdersByBuyerWithFilter(int buyerId, String search, String status) {
        int count = 0;
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "WHERE o.DealerID = ? "
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND o.Status = ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, buyerId);

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
                ps.setString(index++, "%" + search.trim() + "%");
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<Order> getPendingOrdersBySellerWithFilter(int sellerId, Integer farmId, String search, String sort, int pageIndex, int pageSize) {
        List<Order> orders = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice, o.Status, o.CreatedAt, o.ProcessedDate, o.Note, "
                + "p.Name AS OfferName, p.ImageURL, p.RetailPrice, p.MinQuantity, p.MinDeposit, p.TotalOfferPrice, p.Description, "
                + "f.FarmID, f.FarmName, f.Location, "
                + "u.FullName AS DealerName "
                + "FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN Farm f ON p.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.DealerID = u.UserID "
                + "WHERE o.SellerID = ? AND o.Status = 'Pending' "
        );

        if (farmId != null) {
            sql.append("AND p.FarmID = ? ");
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }

        switch (sort != null ? sort : "") {
            case "orderid_asc":
                sql.append("ORDER BY o.OrderID ASC ");
                break;
            case "orderid_desc":
                sql.append("ORDER BY o.OrderID DESC ");
                break;
            case "quantity_asc":
                sql.append("ORDER BY o.Quantity ASC ");
                break;
            case "quantity_desc":
                sql.append("ORDER BY o.Quantity DESC ");
                break;
            case "price_asc":
                sql.append("ORDER BY o.TotalPrice ASC ");
                break;
            case "price_desc":
                sql.append("ORDER BY o.TotalPrice DESC ");
                break;
            case "createdAt_asc":
                sql.append("ORDER BY o.CreatedAt ASC ");
                break;
            case "createdAt_desc":
                sql.append("ORDER BY o.CreatedAt DESC ");
                break;
            case "processeddate_asc":
                sql.append("ORDER BY o.ProcessedDate ASC ");
                break;
            case "processeddate_desc":
                sql.append("ORDER BY o.ProcessedDate DESC ");
                break;
            default:
                sql.append("ORDER BY o.CreatedAt DESC ");
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, sellerId);

            if (farmId != null) {
                ps.setInt(paramIndex++, farmId);
            }

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + search.trim() + "%");
                ps.setString(paramIndex++, "%" + search.trim() + "%");
            }

            int offset = (pageIndex - 1) * pageSize;
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDealerID(rs.getInt("DealerID"));
                order.setSellerID(rs.getInt("SellerID"));
                order.setOfferID(rs.getInt("OfferID"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setTotalPrice(rs.getDouble("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                order.setProcessedDate(rs.getTimestamp("ProcessedDate"));
                order.setNote(rs.getString("Note"));

                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                offer.setMinQuantity(rs.getInt("MinQuantity"));
                offer.setMinDeposit(rs.getDouble("MinDeposit"));
                offer.setTotalOfferPrice(rs.getDouble("TotalOfferPrice"));
                offer.setDescription(rs.getString("Description"));
                order.setPigsOffer(offer);

                Farm farm = new Farm();
                farm.setFarmID(rs.getInt("FarmID"));
                farm.setFarmName(rs.getString("FarmName"));
                farm.setLocation(rs.getString("Location"));
                order.setFarm(farm);

                User dealer = new User();
                dealer.setUserID(rs.getInt("DealerID"));
                dealer.setFullName(rs.getString("DealerName"));
                order.setDealer(dealer);

                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public int countPendingOrdersBySellerWithFilter(int sellerId, Integer farmId, String search) {
        int count = 0;
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "WHERE o.SellerID = ? AND o.Status = 'Pending' "
        );

        if (farmId != null) {
            sql.append("AND p.FarmID = ? ");
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, sellerId);

            if (farmId != null) {
                ps.setInt(index++, farmId);
            }

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
                ps.setString(index++, "%" + search.trim() + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<Order> getOrdersExcludingPendingWithFilter(int sellerId, Integer farmId, String search, String status, String sort, int pageIndex, int pageSize) {
        List<Order> orders = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT o.OrderID, o.DealerID, o.SellerID, o.OfferID, o.Quantity, o.TotalPrice, o.Status, o.CreatedAt, o.ProcessedDate, o.Note, "
                + "p.Name AS OfferName, p.ImageURL, p.RetailPrice, "
                + "f.FarmID, f.FarmName, u.FullName AS DealerName "
                + "FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN Farm f ON p.FarmID = f.FarmID "
                + "JOIN UserAccount u ON o.DealerID = u.UserID "
                + "WHERE o.SellerID = ? AND o.Status != 'Pending' "
        );

        if (farmId != null) {
            sql.append("AND f.FarmID = ? ");
        }
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND o.Status = ? ");
        }

        switch (sort != null ? sort : "") {
            case "orderid_asc":
                sql.append("ORDER BY o.OrderID ASC ");
                break;
            case "orderid_desc":
                sql.append("ORDER BY o.OrderID DESC ");
                break;
            case "quantity_asc":
                sql.append("ORDER BY o.Quantity ASC ");
                break;
            case "quantity_desc":
                sql.append("ORDER BY o.Quantity DESC ");
                break;
            case "totalprice_asc":
                sql.append("ORDER BY o.TotalPrice ASC ");
                break;
            case "totalprice_desc":
                sql.append("ORDER BY o.TotalPrice DESC ");
                break;
            case "createdat_asc":
                sql.append("ORDER BY o.CreatedAt ASC ");
                break;
            case "createdat_desc":
                sql.append("ORDER BY o.CreatedAt DESC ");
                break;
            case "processeddate_asc":
                sql.append("ORDER BY o.ProcessedDate ASC ");
                break;
            case "processeddate_desc":
                sql.append("ORDER BY o.ProcessedDate DESC ");
                break;
            default:
                sql.append("ORDER BY o.CreatedAt DESC ");
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int i = 1;
            ps.setInt(i++, sellerId);

            if (farmId != null) {
                ps.setInt(i++, farmId);
            }
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(i++, "%" + search.trim() + "%");
                ps.setString(i++, "%" + search.trim() + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(i++, status);
            }

            int offset = (pageIndex - 1) * pageSize;
            ps.setInt(i++, offset);
            ps.setInt(i, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDealerID(rs.getInt("DealerID"));
                order.setSellerID(rs.getInt("SellerID"));
                order.setOfferID(rs.getInt("OfferID"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setTotalPrice(rs.getDouble("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                order.setProcessedDate(rs.getTimestamp("ProcessedDate"));
                order.setNote(rs.getString("Note"));

                PigsOffer offer = new PigsOffer();
                offer.setOfferID(rs.getInt("OfferID"));
                offer.setName(rs.getString("OfferName"));
                offer.setImageURL(rs.getString("ImageURL"));
                offer.setRetailPrice(rs.getDouble("RetailPrice"));
                order.setPigsOffer(offer);

                Farm farm = new Farm();
                farm.setFarmID(rs.getInt("FarmID"));
                farm.setFarmName(rs.getString("FarmName"));
                order.setFarm(farm);

                User dealer = new User();
                dealer.setUserID(rs.getInt("DealerID"));
                dealer.setFullName(rs.getString("DealerName"));
                order.setDealer(dealer);

                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public int countOrdersExcludingPendingWithFilter(int sellerID, Integer farmId, String search, String status) {
        int count = 0;
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Orders o "
                + "JOIN PigsOffer p ON o.OfferID = p.OfferID "
                + "JOIN Farm f ON p.FarmID = f.FarmID "
                + "WHERE o.SellerID = ? AND o.Status != 'Pending' "
        );

        if (farmId != null) {
            sql.append("AND f.FarmID = ? ");
        }
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (CAST(o.OrderID AS VARCHAR) LIKE ? OR p.Name LIKE ?) ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND o.Status = ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, sellerID);
            if (farmId != null) {
                ps.setInt(index++, farmId);
            }
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
                ps.setString(index++, "%" + search.trim() + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public Order getOrderById(int orderId) {
        Order order = null;
        try {
            String sql = "SELECT * FROM Orders WHERE OrderID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDealerID(rs.getInt("DealerID"));
                order.setSellerID(rs.getInt("SellerID"));
                order.setOfferID(rs.getInt("OfferID"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setTotalPrice(rs.getDouble("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                order.setProcessedDate(rs.getTimestamp("ProcessedDate"));
                order.setNote(rs.getString("Note"));

                UserDAO userDAO = new UserDAO();
                order.setDealer(userDAO.getUserById(order.getDealerID()));
                order.setSeller(userDAO.getUserById(order.getSellerID()));

                PigsOfferDAO pigsOfferDAO = new PigsOfferDAO();
                PigsOffer offer = pigsOfferDAO.getOfferById(order.getOfferID());

                if (offer != null) {
                    FarmDAO farmDAO = new FarmDAO();
                    Farm farm = farmDAO.getFarmById(offer.getFarmID());
                    offer.setFarm(farm);
                    order.setFarm(farm);
                }

                order.setPigsOffer(offer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql;
        // Các trạng thái được xem là đã xử lý, cần cập nhật ProcessedDate
        if (newStatus.equals("Confirmed") || newStatus.equals("Rejected")
                || newStatus.equals("Canceled") || newStatus.equals("Completed")
                || newStatus.equals("Deposited") || newStatus.equals("Processing")) {
            sql = "UPDATE Orders SET Status = ?, ProcessedDate = GETDATE() WHERE OrderID = ?";
        } else {
            sql = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancelOrder(int orderID) {
        String sql = "UPDATE Orders SET Status = 'Canceled', ProcessedDate = GETDATE() "
                + "WHERE OrderID = ? AND Status = 'Pending'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean confirmOrder(int orderID) {
        String sql = "UPDATE Orders SET Status = 'Confirmed', ProcessedDate = GETDATE() "
                + "WHERE OrderID = ? AND Status = 'Pending'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean rejectOrder(int orderID) {
        String sql = "UPDATE Orders SET Status = 'Rejected', ProcessedDate = GETDATE() "
                + "WHERE OrderID = ? AND Status = 'Pending'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateOrderNote(int orderId, String note) {
        String sql = "UPDATE Orders SET Note = ? WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, note);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isOrderOwnedBySeller(int orderId, int sellerId) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE OrderID = ? AND SellerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, sellerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countOrdersBySeller(int sellerId) {
        String sql = """
                SELECT COUNT(*) 
                FROM Orders o
                JOIN PigsOffer po ON o.OfferID = po.OfferID
                WHERE po.SellerID = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double calculateTotalConfirmedOrDepositedOrdersBySeller(int sellerId) {
        String sql = """
            SELECT COALESCE(SUM(o.TotalPrice), 0)
            FROM Orders o
            JOIN PigsOffer po ON o.OfferID = po.OfferID
            WHERE po.SellerID = ?
              AND (o.Status = 'Confirmed' OR o.Status = 'Deposited')
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

    public List<OrderStat> getOrderStatsFlexible(String type, Integer year, Integer month, Integer day, int sellerId) {
        List<OrderStat> list = new ArrayList<>();
        String selectField, groupBy, orderBy;

        switch (type) {
            case "year":
                selectField = "YEAR(o.CreatedAt) AS Label";
                groupBy = "YEAR(o.CreatedAt)";
                orderBy = "YEAR(o.CreatedAt)";
                break;
            case "month":
                selectField = "MONTH(o.CreatedAt) AS Label";
                groupBy = "MONTH(o.CreatedAt)";
                orderBy = "MONTH(o.CreatedAt)";
                break;
            case "day":
                selectField = "DAY(o.CreatedAt) AS Label";
                groupBy = "DAY(o.CreatedAt)";
                orderBy = "DAY(o.CreatedAt)";
                break;
            case "hour":
                // Sửa dùng DATEPART cho SQL Server
                selectField = "DATEPART(HOUR, o.CreatedAt) AS Label";
                groupBy = "DATEPART(HOUR, o.CreatedAt)";
                orderBy = "DATEPART(HOUR, o.CreatedAt)";
                break;
            default:
                // fallback: thống kê theo tháng
                selectField = "MONTH(o.CreatedAt) AS Label";
                groupBy = "MONTH(o.CreatedAt)";
                orderBy = "MONTH(o.CreatedAt)";
                break;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(selectField).append(", COUNT(*) AS Total ")
                .append("FROM Orders o ")
                .append("JOIN PigsOffer po ON o.OfferID = po.OfferID ")
                .append("WHERE po.SellerID = ? ");

        List<Object> params = new ArrayList<>();
        params.add(sellerId);

        // Gắn thêm điều kiện theo mức độ filter
        if (type.equals("month") || type.equals("day") || type.equals("hour")) {
            if (year != null) {
                sql.append("AND YEAR(o.CreatedAt) = ? ");
                params.add(year);
            }
        }

        if (type.equals("day") || type.equals("hour")) {
            if (month != null) {
                sql.append("AND MONTH(o.CreatedAt) = ? ");
                params.add(month);
            }
        }

        if (type.equals("hour") && day != null) {
            sql.append("AND DAY(o.CreatedAt) = ? ");
            params.add(day);
        }

        sql.append("GROUP BY ").append(groupBy).append(" ORDER BY ").append(orderBy);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new OrderStat(rs.getString("Label"), rs.getInt("Total")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean doesOrderExist(int orderId) throws Exception {
        String sql = "SELECT 1 FROM Orders WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public String getOrderStatus(int orderId) throws Exception {
        String sql = "SELECT Status FROM Orders WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Status");
                }
            }
        }
        return null;
    }

    public void cancelOrderAndDeliveries(int orderId, String reason) throws Exception {
        // 1. Cancel deliveries của đơn hàng
        String cancelDeliveriesSql = "UPDATE Delivery SET Status = 'Canceled' WHERE OrderID = ?";

        try (PreparedStatement ps = connection.prepareStatement(cancelDeliveriesSql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        }

        // 2. Cancel đơn hàng và ghi chú
        String cancelOrderSql = "UPDATE Orders SET Status = 'Canceled', Note = ? WHERE OrderID = ?";

        try (PreparedStatement ps = connection.prepareStatement(cancelOrderSql)) {
            ps.setString(1, reason);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public int getOrderQuantity(int orderId) {
        String sql = "SELECT Quantity FROM Orders WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getOrderTotalPrice(int orderId) {
        String sql = "SELECT TotalPrice FROM Orders WHERE OrderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("TotalPrice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Order> getExpiredOrders() {
        return fetchAll((rs) -> {
            Order order = new Order();
            order.setOrderID(rs.getInt("OrderID"));
            order.setQuantity(rs.getInt("Quantity"));
            User seller = new User();
            seller.setUserID(rs.getInt("SellerID"));
            seller.setEmail(rs.getString("SellerEmail"));
            User dealer = new User();
            dealer.setUserID(rs.getInt("DealerID"));
            dealer.setEmail(rs.getString("DealerEmail"));
            order.setSeller(seller);
            order.setDealer(dealer);
            return order;
        }, """
           SELECT o.*, s.Email as SellerEmail, d.Email AS DealerEmail
           FROM Orders o
           JOIN UserAccount s ON o.SellerID = s.UserID
           JOIn UserAccount d ON o.DealerID = d.UserID 
           WHERE o.Status = 'Pending' AND DATEDIFF(HOUR, CreatedAt, GETDATE()) >= 24
           """);
    }

    public List<Order> getOverProcessedDateOrders() {
        return fetchAll((rs) -> {
            Order order = new Order();
            order.setOrderID(rs.getInt("OrderID"));
            order.setOfferID(rs.getInt("OfferID"));
            order.setQuantity(rs.getInt("Quantity"));
            User seller = new User();
            seller.setUserID(rs.getInt("SellerID"));
            seller.setEmail("SellerEmail");
            User dealer = new User();
            dealer.setUserID(rs.getInt("DealerID"));
            dealer.setEmail("DealerEmail");
            order.setSeller(seller);
            order.setDealer(dealer);
            return order;
        }, """
           SELECT
           o.*,
           d.Email AS DealerEmail,
           s.Email AS SellerEmail
           FROM Orders o
           JOIN UserAccount s ON o.SellerID = s.UserID
           JOIN UserAccount d ON o.DealerID = d.UserID
           WHERE o.Status = 'Confirmed' AND DATEDIFF(HOUR, ProcessedDate, GETDATE()) >= 24
           """);
    }

    public boolean cancelOrders(List<Order> orders) {
        var statement = batch("UPDATE Orders SET status = 'Canceled' WHERE OrderID = ?");
        var statement2 = batch("UPDATE PigsOffer SET Quantity = Quantity + ? WHERE OfferID = ?");
        orders.forEach(order -> {
            statement.params(order.getOrderID());
            statement2.params(order.getQuantity(), order.getOfferID());
        });
        return statement.execute() != null && statement2.execute() != null;
    }

    public boolean cancelOrders(List<Order> orders, String note) {
        var statement = batch("UPDATE Orders SET Status = 'Canceled', Note = ? WHERE OrderID = ?");
        var statement2 = batch("UPDATE PigsOffer SET Quantity = Quantity + ?, TotalOfferPrice = (Quantity + ?) * RetailPrice WHERE OfferID = ?");
        orders.forEach(order -> {
            statement.params(note, order.getOrderID());
            statement2.params(order.getQuantity(), order.getQuantity(), order.getOfferID());
        });
        return statement.execute() != null && statement2.execute() != null;
    }
}
