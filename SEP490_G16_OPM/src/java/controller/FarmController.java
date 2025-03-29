package controller;

import dao.FarmDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import model.Farm;
import model.Page;

@WebServlet("/farm")
public class FarmController  extends HttpServlet {
    
    public Integer getIntParameter(HttpServletRequest req, String name) {
        try {
            return Optional.ofNullable(req.getParameter(name)).map(Integer::parseInt).get();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = getIntParameter(req, "id");
        if (id != null) {
            doGetFarmDetails(req, resp);
            return;
        }
        doGetListFarm(req, resp);
    }
    
    private void doGetFarmDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = getIntParameter(req, "id");
        FarmDAO db = new FarmDAO();
        Farm farm = db.getFarm(id);
        req.setAttribute("farm", farm);
        req.getRequestDispatcher("page/farm.jsp").forward(req, resp);
    }

    private void doGetListFarm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FarmDAO db = new FarmDAO();
        Integer pageNumber = getIntParameter(req, "pageNumber");
        if (pageNumber < 1) pageNumber = 1;
        Page<Farm> farms = db.getAllFarm(pageNumber, 3);
        req.setAttribute("farms", farms);
        req.getRequestDispatcher("page/farms.jsp").forward(req, resp);
    }
}
