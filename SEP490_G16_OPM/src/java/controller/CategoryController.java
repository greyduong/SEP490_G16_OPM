package controller;

import dao.CategoryDAO;
import model.Category;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryController", urlPatterns = {"/category"})
public class CategoryController extends HttpServlet {

    // Check if the user is logged in and has Manager (3) or Staff (2) role
    private boolean isAuthorized(HttpSession session) {
        if (session == null) return false;
        User user = (User) session.getAttribute("user");
        return user != null && (user.getRoleID() == 2 || user.getRoleID() == 3);
    }

    // Handle GET requests to view categories
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!isAuthorized(session)) {
            response.sendRedirect("login-register.jsp?error=access-denied");
            return;
        }

        String action = request.getParameter("action");
        CategoryDAO dao = new CategoryDAO();

        if ("edit".equals(action)) {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            Category category = dao.getCategoryById(categoryID);
            request.setAttribute("category", category);
        }

        List<Category> categoryList = dao.getAllCategories();
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("categorylist.jsp").forward(request, response);
    }

    // Handle POST requests for adding, updating, and deleting categories
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!isAuthorized(session)) {
            response.sendRedirect("login-register.jsp?error=access-denied");
            return;
        }

        String action = request.getParameter("action");
        CategoryDAO dao = new CategoryDAO();

        try {
            if ("add".equals(action)) {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                Category newCategory = new Category();
                newCategory.setName(name);
                newCategory.setDescription(description);
                dao.addCategory(newCategory);
            } else if ("update".equals(action)) {
                int categoryID = Integer.parseInt(request.getParameter("categoryID"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                Category updatedCategory = new Category(categoryID, name, description);
                dao.updateCategory(updatedCategory);
            } else if ("delete".equals(action)) {
                int categoryID = Integer.parseInt(request.getParameter("categoryID"));
                dao.deleteCategory(categoryID);
            }

            response.sendRedirect("category");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Category operation failed", e);
        }
    }
}
