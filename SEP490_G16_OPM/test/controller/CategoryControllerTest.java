package controller;

import dao.CategoryDAO;
import dao.PigsOfferDAO;
import jakarta.servlet.RequestDispatcher;
import model.Category;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CategoryControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private CategoryDAO categoryDAO;

    @Mock
    private PigsOfferDAO pigsOfferDAO;

    @Mock
    private RequestDispatcher dispatcher;

    @Spy
    private CategoryController controller;

    private String action;
    private int sessionUserId = 2;
    private int sessionUserRole = 2;

    private Integer categoryId = 101;
    private Category category;

    private String name;
    private String description;

    private ArrayList<Category> categories;
    private User sessionUser;

    @Before
    public void dependencies() throws Exception {
        doReturn(categoryDAO).when(controller).getCategoryDAO();
        doReturn(pigsOfferDAO).when(controller).getPigsOfferDAO();
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getSession(false)).thenReturn(session);
        doNothing().when(dispatcher).forward(any(), any());
    }

    public void setup() throws Exception {
        sessionUser = new User();
        sessionUser.setRoleID(sessionUserRole);
        sessionUser.setUserID(sessionUserId);
        when(request.getParameter("action")).thenReturn(action);
        when(request.getParameter("categoryID")).thenReturn(String.valueOf(categoryId));
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("description")).thenReturn(description);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        categories = new ArrayList<>();
        when(categoryDAO.getAllCategories()).thenReturn(categories);

        category = new Category();
        category.setCategoryID(categoryId);
        when(categoryDAO.getCategoryById(categoryId)).thenReturn(category);
    }

    /**
     * Test Case 1: Staff - Show List Category
     * 
     * <br> SessionUserId = 2
     * <br> SessionUserRole = 2
     * <br> Action = null
     * <br> CategoryId = null
     * 
     * @throws Exception 
     */
    @Test
    public void testDoGet_StaffNoAction_ShowListCategory() throws Exception {
        setup();
        controller.doGet(request, response);
        verify(categoryDAO).getAllCategories();
        verify(request).setAttribute("categoryList", categories);
        verify(request).getRequestDispatcher("categorylist.jsp");
    }

    /**
     * Test Case 2: Manager - Show List Category
     * 
     * <br> SessionUserId = 3
     * <br> SessionUserRole = 3
     * <br> Action = null
     * <br> CategoryId = null
     * 
     * @throws Exception 
     */
    @Test
    public void testDoGet_ManagerNoAction_ShowListCategory() throws Exception {
        sessionUserId = 3;
        sessionUserRole = 3;
        setup();
        controller.doGet(request, response);
        verify(categoryDAO).getAllCategories();
        verify(request).setAttribute("categoryList", categories);
        verify(request).getRequestDispatcher("categorylist.jsp");
    }

    /**
     * Test Case 3: Unauthorized
     * 
     * <br> SessionUserId = 4
     * <br> SessionUserRole = 4
     * <br> Action = null
     * <br> CategoryId = null
     * 
     * @throws Exception 
     */
    @Test
    public void testDoGet_Unauthorized() throws Exception {
        sessionUserId = 4;
        sessionUserRole = 4;
        setup();
        controller.doGet(request, response);
        verify(response).sendRedirect("login?error=access-denied");
    }

    /**
     * Test Case 4: Edit Action
     * 
     * <br> SessionUserId = 4
     * <br> SessionUserRole = 4
     * <br> Action = "edit"
     * <br> CategoryId = 101
     * 
     * @throws Exception 
     */
    @Test
    public void testDoGet_EditAction() throws Exception {
        action = "edit";
        categoryId = 101;

        setup();

        controller.doGet(request, response);

        verify(categoryDAO).getCategoryById(categoryId);
        verify(request).setAttribute("category", category);
        verify(categoryDAO).getAllCategories();
        verify(request).setAttribute("categoryList", categories);
        verify(request).getRequestDispatcher("categorylist.jsp");
    }

    @Test
    public void testDoPost_Unauthorized() throws ServletException, IOException {
        sessionUserRole = 4;
        controller.doPost(request, response);
        verify(response).sendRedirect("login?error=access-denied");
    }


    @Test
    public void testDoPost_authorizedUser_addAction() throws ServletException, IOException {
        action = "add";
        name = "Example Category";
        description = "Example Description";
        
        controller.doPost(request, response);

        verify(categoryDAO).addCategory(any(Category.class));
        verify(response).sendRedirect("category");
    }
    /*

    @Test
    public void testDoPost_authorizedUser_updateAction() throws ServletException, IOException {
        setupAuthorizedSession();
        int categoryIdToUpdate = 1;
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("categoryID")).thenReturn(String.valueOf(categoryIdToUpdate));
        when(request.getParameter("name")).thenReturn("Updated Category");
        when(request.getParameter("description")).thenReturn("Updated Description");

        controller.doPost(request, response);

        verify(categoryDAO).updateCategory(any(Category.class));
        verify(response).sendRedirect("category");
        verifyNoMoreInteractions(categoryDAO, pigsOfferDAO, request, response, session);
    }

    @Test
    public void testDoPost_authorizedUser_deleteAction_notUsed() throws ServletException, IOException {
        setupAuthorizedSession();
        int categoryIdToDelete = 1;
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("categoryID")).thenReturn(String.valueOf(categoryIdToDelete));
        when(pigsOfferDAO.isCategoryUsed(categoryIdToDelete)).thenReturn(false);

        controller.doPost(request, response);

        verify(pigsOfferDAO).isCategoryUsed(categoryIdToDelete);
        verify(categoryDAO).deleteCategory(categoryIdToDelete);
        verify(response).sendRedirect("category");
        verifyNoMoreInteractions(categoryDAO, pigsOfferDAO, request, response, session);
    }

    @Test
    public void testDoPost_authorizedUser_deleteAction_isUsed() throws ServletException, IOException {
        setupAuthorizedSession();
        int categoryIdToDelete = 1;
        List<Category> categories = Arrays.asList(new Category(1, "Cat1", "Desc1"), new Category(2, "Cat2", "Desc2"));
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("categoryID")).thenReturn(String.valueOf(categoryIdToDelete));
        when(pigsOfferDAO.isCategoryUsed(categoryIdToDelete)).thenReturn(true);
        when(categoryDAO.getAllCategories()).thenReturn(categories);
        when(request.getRequestDispatcher("categorylist.jsp")).thenReturn(any()); // Mocking RequestDispatcher

        controller.doPost(request, response);

        verify(pigsOfferDAO).isCategoryUsed(categoryIdToDelete);
        verify(categoryDAO).getAllCategories();
        verify(request).setAttribute("categoryList", categories);
        verify(request).setAttribute("deleteError", "Không thể xóa danh mục vì đang được sử dụng trong các bài đăng.");
        verify(request).getRequestDispatcher("categorylist.jsp");
        verify(categoryDAO, never()).deleteCategory(anyInt());
        verifyNoMoreInteractions(categoryDAO, pigsOfferDAO, request, response, session);
    }
     */
}
