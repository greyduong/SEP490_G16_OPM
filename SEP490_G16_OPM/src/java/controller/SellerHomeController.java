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

        req.setAttribute("totalFarms", totalFarms);
        req.setAttribute("activeFarms", activeFarms);
        req.setAttribute("totalOffers", totalOffers);
        req.setAttribute("availableOffers", availableOffers);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalDebt", totalDebt);

        req.getRequestDispatcher("seller.jsp").forward(req, resp);

    }
}
