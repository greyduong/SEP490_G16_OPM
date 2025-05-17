package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.PigsOfferDAO;
import model.PigsOffer;

@WebServlet(name = "PigsOfferDetailsController", urlPatterns = {"/PigsOfferDetails"})
public class PigsOfferDetailsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String offerIdParam = request.getParameter("offerId");

        if (offerIdParam == null || !offerIdParam.matches("\\d+")) {
            response.sendRedirect("home?msg=" + URLEncoder.encode("Mã chào bán không hợp lệ", "UTF-8"));
            return;
        }

        int offerId = Integer.parseInt(offerIdParam);
        PigsOfferDAO offerDAO = new PigsOfferDAO();
        PigsOffer offer = offerDAO.getOfferById(offerId);

        if (offer == null || !"Available".equalsIgnoreCase(offer.getStatus())) {
            response.sendRedirect("home?msg=" + URLEncoder.encode("Chào bán không tồn tại hoặc đã ngừng bán", "UTF-8"));
            return;
        }

        ArrayList<PigsOffer> suggestedOffers = new ArrayList<>();
        String suggestedSource = "";

        // Ưu tiên: cùng loại
        suggestedOffers = offerDAO.getTop5OtherOffersByCategory(
                offer.getCategory().getCategoryID(), offer.getOfferID());
        if (!suggestedOffers.isEmpty()) {
            suggestedSource = "category";
        }

        // Nếu không có → cùng trang trại
        if (suggestedOffers.isEmpty()) {
            suggestedOffers = offerDAO.getTop5OtherOffersByFarm(
                    offer.getFarm().getFarmID(), offer.getOfferID());
            if (!suggestedOffers.isEmpty()) {
                suggestedSource = "farm";
            }
        }

        // Nếu vẫn không có → mới nhất
        if (suggestedOffers.isEmpty()) {
            suggestedOffers = offerDAO.getTop5LatestOffers(offer.getOfferID());
            if (!suggestedOffers.isEmpty()) {
                suggestedSource = "latest";
            }
        }

        request.setAttribute("offer", offer);
        request.setAttribute("suggestedOffers", suggestedOffers);
        request.setAttribute("suggestedSource", suggestedSource);
        request.getRequestDispatcher("pigsofferdetails.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
