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

    private User mockUser;
    private List<Application> mockApplications;

    @Before
    public void setup() {
        mockUser = new User();
        mockUser.setUserID(4);
        mockUser.setRoleID(4);
        mockApplications = new ArrayList<>();

        when(controller.getApplicationDAO()).thenReturn(dao);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("keyword")).thenReturn("");
        when(request.getParameter("status")).thenReturn("Pending");
        when(request.getParameter("sortByDate")).thenReturn("desc");
        when(request.getParameter("page")).thenReturn("1");
        when(dao.getApplicationsByFilterPaged(4, "", "Đang chờ xử lý", "desc", 1, 5)).thenReturn(mockApplications);
        when(dao.countApplicationsByFilter(4, "", "Đang chờ xử lý")).thenReturn(0);
        when(request.getRequestDispatcher("viewapplication.jsp")).thenReturn(dispatcher);
    }

    /**
     * Test case: Success
     * 
     * roleID = 4
     * keyword = ""
     * status = "Pending"
     * page = 1
     * 
     * Expected: Should retrieve filtered application list and forward to JSP
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testDoGet_Success() throws Exception {
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
     * Test case: Unauthorized user (no user in session)
     * 
     * Expected: Redirect to home with 403 error
     */
    @Test
    public void testDoGet_Unauthorized_NoUser() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);
        controller.doGet(request, response);
        verify(response).sendRedirect("home?error=403");
    }

    /**
     * Test case: Authorized user but invalid page number (non-integer)
     * 
     * page = "invalid"
     * 
     * Expected: Page defaults to 1
     */
    @Test
    public void testDoGet_InvalidPageNumber() throws Exception {
        when(request.getParameter("page")).thenReturn("invalid");

        controller.doGet(request, response);

        verify(request).setAttribute("currentPage", 1);
        verify(dispatcher).forward(request, response);
    }
}
