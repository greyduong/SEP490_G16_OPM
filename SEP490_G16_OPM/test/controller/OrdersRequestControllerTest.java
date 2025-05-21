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
import org.junit.Before;
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

    @InjectMocks
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

    private User user;

    @Before
    public void setup() throws Exception {
    }

    /**
     * With Valid Parameters
     * 
     * userID = 10
     * farmID = 1
     * search = "keyword"
     * sort = "orderid_asc"
     * page = "2"
     * @throws Exception 
     */
    @Test
    public void testDoGet_WithValidParameters() throws Exception {
        // current logged user
        user = new User();
        user.setUserID(10);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        // parameters
        when(request.getParameter("farmId")).thenReturn("1");
        when(request.getParameter("search")).thenReturn("keyword");
        when(request.getParameter("sort")).thenReturn("orderid_asc");
        when(request.getParameter("page")).thenReturn("2");

        List<Order> orders = Collections.singletonList(new Order());
        when(orderDAO.countPendingOrdersBySellerWithFilter(eq(10), eq(1), eq("keyword"))).thenReturn(11);
        when(orderDAO.getPendingOrdersBySellerWithFilter(eq(10), eq(1), eq("keyword"), eq("orderid_asc"), eq(2), eq(10))).thenReturn(orders);
        List<Farm> farms = Arrays.asList(new Farm(), new Farm());
        when(farmDAO.getActiveFarmsBySellerId(10)).thenReturn(farms);

        when(request.getRequestDispatcher("orderrequestpage.jsp")).thenReturn(dispatcher);

        controller.doGet(request, response);
        verify(request).setAttribute(eq("page"), any(Page.class));
        verify(request).setAttribute(eq("farmList"), eq(farms));
        verify(dispatcher).forward(request, response);
    }

    /**
     * Without Parameters
     * 
     * userID = 10
     * farmID = null
     * search = null
     * sort = null
     * page = null
     * @throws Exception 
     */
    @Test
    public void testDoGet_WithoutOptionalParams_UsesDefaults() throws Exception {
        // current logged user
        user = new User();
        user.setUserID(10);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("farmId")).thenReturn(null);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);

        when(orderDAO.countPendingOrdersBySellerWithFilter(eq(10), isNull(), isNull())).thenReturn(5);
        when(orderDAO.getPendingOrdersBySellerWithFilter(eq(10), isNull(), isNull(), isNull(), eq(1), eq(10)))
                .thenReturn(Collections.emptyList());

        when(farmDAO.getActiveFarmsBySellerId(10)).thenReturn(Collections.emptyList());

        when(request.getRequestDispatcher("orderrequestpage.jsp")).thenReturn(dispatcher);

        controller.doGet(request, response);

        verify(request).setAttribute(eq("page"), any(Page.class));
        verify(request).setAttribute(eq("farmList"), eq(Collections.emptyList()));
        verify(dispatcher).forward(request, response);
    }
}
