package controller;

import dao.FarmDAO;
import exeception.AppException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import model.Farm;
import model.Page;

@WebServlet("/farms")
public class FarmController extends HttpServlet {

    public Optional<Integer> getIntParameter(HttpServletRequest req, String name) {
        try {
            return Optional.ofNullable(req.getParameter(name)).map(Integer::parseInt);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<String> getStringParameter(HttpServletRequest req, String name) {
        return Optional.ofNullable(req.getParameter(name)).map(str -> str.isBlank() ? null : str);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getIntParameter(req, "id").isPresent()) {
            doGetFarmDetails(req, resp);
            return;
        }
        doGetListFarm(req, resp);
    }

    private void doGetFarmDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<Integer> idOpt = getIntParameter(req, "id");
            if (idOpt.isEmpty()) throw new AppException("Invalid id");
            final int id = idOpt.get();
            final FarmDAO db = new FarmDAO();
            final Farm farm = db.getById(id);
            req.setAttribute("farm", farm);
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("farm.jsp").forward(req, resp);
    }

    private void doGetListFarm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FarmDAO db = new FarmDAO();
        int pageNumber = getIntParameter(req, "pageNumber").orElse(1);
        String search = getStringParameter(req, "search").orElse("");
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        Page<Farm> page = db.search(search, pageNumber, 6);
        req.setAttribute("page", page);
        int nextPage = pageNumber < page.getTotalPage() ? pageNumber + 1 : page.getTotalPage();
        int prevPage = pageNumber > 1 ? pageNumber - 1 : 1;
        req.setAttribute("nextPage", nextPage);
        req.setAttribute("prevPage", prevPage);
        long offset = (long) (page.getPageNumber() - 1) * page.getPageSize();
        req.setAttribute("offset", offset);
        req.getRequestDispatcher("farms.jsp").forward(req, resp);
    }
}
