package controller;

import dao.DeliveryDAO;
import dao.FarmDAO;
import dao.OrderDAO;
import dao.PigsOfferDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import model.OrderStat;
import model.User;

@WebServlet("/seller")
public class SellerHomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        FarmDAO farmDAO = new FarmDAO();
        PigsOfferDAO offerDAO = new PigsOfferDAO();
        OrderDAO orderDAO = new OrderDAO();
        DeliveryDAO deliveryDAO = new DeliveryDAO();

        int sellerId = user.getUserID();

        int totalFarms = farmDAO.countFarmsBySeller(sellerId);
        int activeFarms = farmDAO.countActiveFarmsBySeller(sellerId);
        int totalOffers = offerDAO.countOffersBySeller(sellerId);
        int availableOffers = offerDAO.countAvailableOffersBySeller(sellerId);
        int totalOrders = orderDAO.countOrdersBySeller(sellerId);

        double totalRevenue = deliveryDAO.calculateTotalRevenueFromDeliveryBySeller(sellerId);
        double totalConfirmedOrDepositedOrders = orderDAO.calculateTotalConfirmedOrDepositedOrdersBySeller(sellerId);
        double totalDebt = totalConfirmedOrDepositedOrders - totalRevenue;

        // Get filter parameters
        String type = req.getParameter("type");
        String yearStr = req.getParameter("year");
        String monthStr = req.getParameter("month");
        String dayStr = req.getParameter("day");

        if (type == null || type.trim().isEmpty()) {
            type = "month";
        }

        Integer year = null, month = null, day = null;
        try {
            if (yearStr != null && yearStr.matches("\\d+")) {
                year = Integer.parseInt(yearStr);
            }
            if (monthStr != null && monthStr.matches("\\d+")) {
                month = Integer.parseInt(monthStr);
            }
            if (dayStr != null && dayStr.matches("\\d+")) {
                day = Integer.parseInt(dayStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        List<OrderStat> stats = orderDAO.getOrderStatsFlexible(type, year, month, day, sellerId);

        req.setAttribute("totalFarms", totalFarms);
        req.setAttribute("activeFarms", activeFarms);
        req.setAttribute("totalOffers", totalOffers);
        req.setAttribute("availableOffers", availableOffers);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalDebt", totalDebt);
        req.setAttribute("orderStats", stats);
        req.setAttribute("statType", type);

        req.getRequestDispatcher("seller.jsp").forward(req, resp);

    }
}
