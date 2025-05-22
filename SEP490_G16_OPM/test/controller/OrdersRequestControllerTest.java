package controller;

import dao.FarmDAO;
import dao.OrderDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Farm;
import model.Order;
import model.Page;
import model.User;
import org.junit.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OrdersRequestControllerTest {

    @Spy
    private OrdersRequestController controller;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private FarmDAO farmDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    private int userId = 4;
    private int farmId = 101;
    private String search = "keyword";
    private String sort = "orderid_asc";
    private int page = 2;

    private User user;
    private List<Order> orders;
    private List<Farm> farms;
    private int ordersCount = 13;
    private int pageSize = 10;

    public void setup() {
        doReturn(orderDAO).when(controller).getOrderDAO();
        doReturn(farmDAO).when(controller).getFarmDAO();
        user = new User();
        user.setUserID(userId);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("farmId")).thenReturn(String.valueOf(farmId));
        when(request.getParameter("search")).thenReturn(search);
        when(request.getParameter("sort")).thenReturn(sort);
        when(request.getParameter("page")).thenReturn(String.valueOf(page));

        orders = Collections.singletonList(new Order());
        when(orderDAO.countPendingOrdersBySellerWithFilter(eq(userId), eq(farmId), eq(search))).thenReturn(ordersCount);
        when(orderDAO.getPendingOrdersBySellerWithFilter(eq(userId), eq(farmId), eq(search), eq(sort), eq(page), eq(pageSize))).thenReturn(orders);
        farms = Arrays.asList(new Farm(), new Farm());
        when(farmDAO.getActiveFarmsBySellerId(userId)).thenReturn(farms);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    /**
     * With Valid Parameters
     *
     * <br>userID = 10
     * <br>farmID = 1
     * <br>search = "keyword"
     * <br>sort = "orderid_asc"
     * <br>page = "2"
     *
     * @throws Exception
     */
    @Test
    public void testDoGet_WithValidParameters() throws Exception {
        setup();
        controller.doGet(request, response);

        verify(request).setAttribute(eq("page"), any(Page.class));
        verify(request).setAttribute(eq("farmList"), eq(farms));
        verify(dispatcher).forward(request, response);
    }

    /**
     * Without Parameters
     *
     * userID = 10 farmID = null search = null sort = null page = null
     *
     * @throws Exception
     */
    @Test
    public void testDoGet_WithoutOptionalParams_UsesDefaults() throws Exception {
        setup();
        when(request.getParameter("farmId")).thenReturn(null);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);

        controller.doGet(request, response);

        verify(request).setAttribute(eq("page"), any(Page.class));
        verify(request).setAttribute(eq("farmList"), eq(farms));
        verify(dispatcher).forward(request, response);
    }
}
