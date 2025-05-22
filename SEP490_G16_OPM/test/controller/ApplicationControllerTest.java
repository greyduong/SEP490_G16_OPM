package controller;

import dao.ApplicationDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Application;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import org.mockito.Spy;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApplicationControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ApplicationDAO dao;

    @Mock
    private RequestDispatcher dispatcher;

    @Spy
    private ApplicationController controller;

    private User sessionUser;
    private List<Application> applications;

    @Before
    public void setup() {
        sessionUser = new User();
        sessionUser.setUserID(4);
        sessionUser.setRoleID(4);
        applications = new ArrayList<>();

        // mock dao
        doReturn(dao).when(controller).getApplicationDAO();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(dao.getApplicationsByFilterPaged(anyInt(), anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(applications);
        when(dao.countApplicationsByFilter(anyInt(), anyString(), anyString())).thenReturn(applications.size());
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    /**
     * Test case 1: Success - Show List Applications
     * 
     * <br>SessionUserRole = 4
     * <br>ParameterKeyword = "keyword"
     * <br>ParameterStatus = "Pending"
     * <br>ParameterPage = 1
     * <br>ParameterSortByDate = "newest"
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testDoGet_Success_ShowListApplications() throws Exception {
        int sessionUserRole = 4;

        sessionUser.setRoleID(sessionUserRole);
        String keyword = "keyword";
        String status = "Pending";
        String sortByDate = "newest";
        int page = 1;

        when(request.getParameter("keyword")).thenReturn(keyword);
        when(request.getParameter("status")).thenReturn(status);
        when(request.getParameter("sortByDate")).thenReturn(sortByDate);
        when(request.getParameter("page")).thenReturn(String.valueOf(page));

        controller.doGet(request, response);

        verify(request).setAttribute("applicationList", applications);
        verify(request).setAttribute("currentPage", page);
        verify(request).setAttribute("totalPages", applications.size());
        verify(request).setAttribute("keyword", keyword);
        verify(request).setAttribute("status", status);
        verify(request).setAttribute("sortByDate", sortByDate);

        verify(dispatcher).forward(request, response);
    }

    /**
     * Test case 2: Unauthorized - Redirect Home With Error
     * 
     * <br>SessionUserRole = 2
     * <br>ParameterKeyword = "keyword"
     * <br>ParameterStatus = "Pending"
     * <br>ParameterPage = "invalid"
     * <br>ParameterSortByDate = "newest"
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testDoGet_Unauthorized_NoUser() throws Exception {
        sessionUser.setRoleID(2);
        controller.doGet(request, response);
        verify(response).sendRedirect("home?error=403");
    }

    /**
     * Test case 3: Invalid Page Number - Use Default Page Number
     * 
     * <br>SessionUserRole = 4
     * <br>ParameterKeyword = "keyword"
     * <br>ParameterStatus = "Pending"
     * <br>ParameterPage = "invalid"
     * <br>ParameterSortByDate = "newest"
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testDoGet_InvalidPageNumber() throws Exception {
        int sessionUserRole = 4;

        sessionUser.setRoleID(sessionUserRole);
        String keyword = "keyword";
        String status = "Pending";
        String sortByDate = "newest";
        String page = "invalid";

        when(request.getParameter("keyword")).thenReturn(keyword);
        when(request.getParameter("status")).thenReturn(status);
        when(request.getParameter("sortByDate")).thenReturn(sortByDate);
        when(request.getParameter("page")).thenReturn(page);

        controller.doGet(request, response);

        verify(request).setAttribute("currentPage", 1);
        verify(dispatcher).forward(request, response);
    }
}
