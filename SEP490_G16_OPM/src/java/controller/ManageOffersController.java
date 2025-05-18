package controller;

import dao.FarmDAO;
import dao.PigsOfferDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Farm;
import model.Page;
import model.PigsOffer;
import java.util.List;

@WebServlet(name = "ManageOffersController", urlPatterns = {"/manage-offers"})
public class ManageOffersController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            PigsOfferDAO offerDAO = new PigsOfferDAO();
            FarmDAO farmDAO = new FarmDAO();

            // Lấy danh sách tất cả farms để filter
            List<Farm> allFarms = farmDAO.getAllFarms();
            request.setAttribute("allFarms", allFarms);

            // Lấy các tham số filter từ request
            String farmId = request.getParameter("farmId");
            String search = request.getParameter("search");
            String status = request.getParameter("status");
            String sort = request.getParameter("sort");
            String pageParam = request.getParameter("page");
            int pageNumber = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
            int pageSize = 10;

            // Gọi DAO để lấy danh sách offers với filter
            Page<PigsOffer> page = offerDAO.getAllOffersWithFilter(
                    farmId, search, status, sort, pageNumber, pageSize);

            request.setAttribute("page", page);

            request.getRequestDispatcher("manage-offers.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách chào bán.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
