package controller;

import dao.CategoryDAO;
import dao.FarmDAO;
import dao.PigsOfferDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Farm;
import model.PigsOffer;

import java.io.IOException;
import java.util.List;
import model.Category;

@WebServlet(name = "ViewFarmDetailController", urlPatterns = {"/farm-detail"})
public class ViewFarmDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String farmIdParam = request.getParameter("farmId"); // Đổi từ "id" → "farmId" cho đúng với JSP

            if (farmIdParam == null || !farmIdParam.matches("\\d+")) {
                response.sendRedirect("home?error=invalid-farm-id");
                return;
            }

            int farmId = Integer.parseInt(farmIdParam);

            // Lấy các tham số lọc và sắp xếp
            String keyword = request.getParameter("keyword");
            String categoryName = request.getParameter("categoryName");
            String sort = request.getParameter("sort");

            // DAO
            FarmDAO farmDAO = new FarmDAO();
            PigsOfferDAO offerDAO = new PigsOfferDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            // Lấy thông tin farm
            Farm farm = farmDAO.getFarmById(farmId);
            if (farm == null) {
                response.sendRedirect("home?error=farm-not-found");
                return;
            }

            // Lấy danh sách offer có lọc và sắp xếp
            List<PigsOffer> offers = offerDAO.getOffersByFarmWithFilter(farmId, keyword, categoryName, sort);

            // Lấy danh sách loại heo (category)
            List<Category> categoryList = categoryDAO.getAllCategories();

            // Gửi dữ liệu về view
            request.setAttribute("farm", farm);
            request.setAttribute("offers", offers);
            request.setAttribute("categoryList", categoryList);
            request.getRequestDispatcher("farmdetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("home?error=invalid-farm-id");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home?error=internal-error");
        }
    }
}
