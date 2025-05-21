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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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

    @InjectMocks
    private ApplicationController controller;

    /**
     * Test case: Success
     * 
     * roleID = 4
     * keyword = ""
     * status = "Pending"
     * page = 1
     * 
     * Expected: Should retrieve filtered application list and forward to JSP
     */
    @Test
    public void testDoGet_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserID(4);
        mockUser.setRoleID(4);

        List<Application> mockApplications = new ArrayList<>();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("keyword")).thenReturn("");
        when(request.getParameter("status")).thenReturn("Pending");
        when(request.getParameter("sortByDate")).thenReturn("desc");
        when(request.getParameter("page")).thenReturn("1");
        when(dao.getApplicationsByFilterPaged(4, "", "Đang chờ xử lý", "desc", 1, 5)).thenReturn(mockApplications);
        when(dao.countApplicationsByFilter(4, "", "Đang chờ xử lý")).thenReturn(0);
        when(request.getRequestDispatcher("viewapplication.jsp")).thenReturn(dispatcher);

        controller.doGet(request, response);

        verify(request).setAttribute("applicationList", mockApplications);
        verify(request).setAttribute("currentPage", 1);
        verify(request).setAttribute("totalPages", 0);
        verify(request).setAttribute("keyword", "");
        verify(request).setAttribute("status", "Pending");
        verify(request).setAttribute("sortByDate", "desc");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Test case: Unauthorized access (session is null)
     * 
     * Expected: Redirect to home with 403 error
     */
    @Test
    public void testDoGet_Unauthorized_NoSession() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        controller.doGet(request, response);
        verify(response).sendRedirect("home?error=403");
    }

    /**
     * Test case: Unauthorized user (no user in session)
     * 
     * Expected: Redirect to home with 403 error
     */
    @Test
    public void testDoGet_Unauthorized_NoUser() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        controller.doGet(request, response);
        verify(response).sendRedirect("home?error=403");
    }

    /**
     * Test case: Authorized user but invalid page number (non-integer)
     * Expected: Page defaults to 1
     */
    @Test
    public void testDoGet_InvalidPageNumber() throws Exception {
        User mockUser = new User();
        mockUser.setUserID(4);
        mockUser.setRoleID(4);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("keyword")).thenReturn("");
        when(request.getParameter("status")).thenReturn("Confirmed");
        when(request.getParameter("sortByDate")).thenReturn("asc");
        when(request.getParameter("page")).thenReturn("abc");
        when(dao.getApplicationsByFilterPaged(4, "", "Đã phê duyệt", "asc", 1, 5)).thenReturn(new ArrayList<>());
        when(dao.countApplicationsByFilter(4, "", "Đã phê duyệt")).thenReturn(0);
        when(request.getRequestDispatcher("viewapplication.jsp")).thenReturn(dispatcher);

        controller.doGet(request, response);

        verify(request).setAttribute("currentPage", 1);
        verify(dispatcher).forward(request, response);
    }
}
