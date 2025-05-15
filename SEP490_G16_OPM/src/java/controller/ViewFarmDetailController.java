package controller;

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

@WebServlet(name = "ViewFarmDetailController", urlPatterns = {"/farm-detail"})
public class ViewFarmDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");

            if (idParam == null || !idParam.matches("\\d+")) {
                response.sendRedirect("home?error=invalid-id");
                return;
            }
            int farmId = Integer.parseInt(idParam);

            FarmDAO farmDAO = new FarmDAO();
            PigsOfferDAO offerDAO = new PigsOfferDAO();

            Farm farm = farmDAO.getFarmById(farmId);
            if (farm == null) {
                response.sendRedirect("home?error=farm-not-found");
                return;
            }
            // copy nguyên home  getDealerHomePage bỏ sang
            List<PigsOffer> offers = offerDAO.getOffersByFarmId(farmId);

            request.setAttribute("farm", farm);
            request.setAttribute("offers", offers);
            request.getRequestDispatcher("farmdetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("home?error=invalid-farm-id");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home?error=internal-error");
        }
    }
}
