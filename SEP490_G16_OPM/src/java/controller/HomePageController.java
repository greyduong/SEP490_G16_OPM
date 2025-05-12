package controller;

import dao.CategoryDAO;
import dao.PigsOfferDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.PigsOffer;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class HomePageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<User> logged = Optional.ofNullable(request.getAttribute("user")).map(obj -> (User) obj);
        if (logged.isEmpty()) {
            getDealerHomePage(request, response);
            return;
        }
        switch (logged.get().getRoleID()) {
            case 5 ->
                getDealerHomePage(request, response);
            case 4 ->
                response.sendRedirect(request.getContextPath() + "/seller");
            case 3 ->
                response.sendRedirect(request.getContextPath() + "/staff");
            case 2 ->
                response.sendRedirect(request.getContextPath() + "/manager-home");
            case 1 ->
                response.sendRedirect(request.getContextPath() + "/admin");
        }
    }

    private void getDealerHomePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryName = request.getParameter("categoryName");
        String keyword = request.getParameter("keyword");
        String sort = request.getParameter("sort"); // e.g., "quantity_asc", "price_desc"

        int page = 1;
        final int pageSize = 8;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Load categories
        CategoryDAO categoryDAO = new CategoryDAO();
        ArrayList<Category> categoryList = categoryDAO.getAllCategories();

        // Load offers
        PigsOfferDAO pigsOfferDAO = new PigsOfferDAO();
        ArrayList<PigsOffer> offerList;
        int totalOffers;

        if ((keyword != null && !keyword.trim().isEmpty()) || (categoryName != null && !categoryName.trim().isEmpty())) {
            offerList = pigsOfferDAO.searchOffersFlexible(keyword, categoryName, sort, page, pageSize);
            totalOffers = pigsOfferDAO.countOffersFlexible(keyword, categoryName);
        } else {
            offerList = pigsOfferDAO.getPagedPigsOffersWithSort(sort, page, pageSize);
            totalOffers = pigsOfferDAO.countAvailableOffers();
        }

        int totalPages = (int) Math.ceil((double) totalOffers / pageSize);

        // Set attributes for JSP
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("offerList", offerList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("sort", sort);
        request.setAttribute("keyword", keyword);
        request.setAttribute("categoryName", categoryName);

        request.getRequestDispatcher("homepage.jsp").forward(request, response);
    }
}
