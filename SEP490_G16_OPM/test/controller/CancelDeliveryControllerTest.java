package controller;

import dao.DeliveryDAO;
import dao.OrderDAO;
import dao.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import model.Email;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CancelDeliveryControllerTest {
    @InjectMocks
    private CancelDeliveryController controller;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private DeliveryDAO deliveryDAO;
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private UserDAO userDAO;

    private MockedStatic<Email> mockEmail;

    private User dealer;
    private User seller;
    private User user;
    private Order order;
    
    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setUserID(2);
        user.setRoleID(5);

        dealer = new User();
        dealer.setUserID(2);
        dealer.setRoleID(5);
        dealer.setFullName("Dealer A");
        dealer.setEmail("dealer@example.com");

        seller = new User();
        seller.setUserID(3);
        seller.setFullName("Seller B");
        seller.setEmail("seller@example.com");

        order = new Order();
        order.setOrderID(101);
        order.setDealerID(2);
        order.setSellerID(3);

        mockEmail = Mockito.mockStatic(Email.class);
        mockEmail.when(() -> Email.sendEmail(anyString(), anyString(), anyString())).thenAnswer(inv -> null);
        doNothing().when(response).sendRedirect(anyString());

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("deliveryID")).thenReturn("5");
        when(request.getParameter("cancelReason")).thenReturn("Reason valid");

        when(session.getAttribute("user")).thenReturn(user);

        when(deliveryDAO.getOrderIdByDeliveryId(5)).thenReturn(101);
        when(deliveryDAO.getDealerIdByDeliveryId(5)).thenReturn(2);
        when(deliveryDAO.getDeliveryStatusById(5)).thenReturn("Pending");
        when(deliveryDAO.getOrderStatusByDeliveryId(5)).thenReturn("Processing");
        when(deliveryDAO.updateDeliveryStatus(5, "Canceled")).thenReturn(true);
        when(deliveryDAO.appendToDeliveryComments(eq(5), contains("HỦY"))).thenReturn(true);

        when(orderDAO.getOrderById(101)).thenReturn(order);

        when(userDAO.getUserById(3)).thenReturn(seller);
        when(userDAO.getUserById(2)).thenReturn(dealer);

        when(deliveryDAO.getDeliveryTotalPrice(5)).thenReturn(1000000.0);
        when(deliveryDAO.getDeliveryQuantity(5)).thenReturn(10);
    }
    
    @After
    public void tearDown() {
        mockEmail.close();
    }

    /**
     * Sucessfull
     * 
     * deliveryId = 5
     * cancelReason = "Reason valid"
     * orderId = 101
     * userId = 2
     * userRole = 5
     * dealerId = 2
     * deliveryStatus = "Pending"
     * orderStatus = "Processing"
     * @throws Exception 
     */
    @Test
    public void testSuccessfulCancellation() throws Exception {
        controller.doPost(request, response);
        verify(response).sendRedirect(contains(URLEncoder.encode("Giao hàng đã được hủy", StandardCharsets.UTF_8)));
        mockEmail.verify(() -> Email.sendEmail(anyString(), anyString(), anyString()), times(2));
    }

    /**
     * Unauthenticated
     * 
     * deliveryId = 5
     * cancelReason = "Reason valid"
     * orderId = 101
     * userId = null
     * userRole = null
     * dealerId = 2
     * deliveryStatus = "Pending"
     * orderStatus = "Processing"
     * @throws Exception 
     */
    @Test
    public void testUnauthenticated_RedirectsWithError() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        when(request.getParameter("deliveryID")).thenReturn("7");
        when(deliveryDAO.getOrderIdByDeliveryId(7)).thenReturn(200);
        when(deliveryDAO.getDealerIdByDeliveryId(7)).thenReturn(2); // DealerID mismatch

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("Bạn không có quyền hủy giao hàng này.", "UTF-8")));
    }

    /**
     * Unauthorized (Wrong role)
     * 
     * deliveryId = 5
     * cancelReason = "Reason valid"
     * orderId = 101
     * userId = 4 // invalid role
     * userRole = 5
     * dealerId = 2
     * deliveryStatus = "Pending"
     * orderStatus = "Processing"
     * @throws Exception 
     */
    @Test
    public void testUnauthorized_RedirectsWithError() throws Exception {
        user.setRoleID(4);

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("Bạn không có quyền hủy giao hàng này.", "UTF-8")));
    }

    /**
     * Unauthorized (Current user and dealer not match)
     * 
     * deliveryId = 5
     * cancelReason = "Reason valid"
     * orderId = 101
     * userId = 4 // invalid role
     * userRole = 5
     * dealerId = 2
     * deliveryStatus = "Pending"
     * orderStatus = "Processing"
     * @throws Exception 
     */
    @Test
    public void testUnauthorized2_RedirectsWithError() throws Exception {
        user.setUserID(99);

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("Bạn không có quyền hủy giao hàng này.", "UTF-8")));
    }

    /**
     * Delivery Not Pending
     * 
     * deliveryId = 5
     * cancelReason = "Reason valid"
     * orderId = 101
     * userId = 4 // invalid role
     * userRole = 5
     * dealerId = 2
     * deliveryStatus = "Confirmed"
     * orderStatus = "Processing"
     * @throws Exception 
     */
    @Test
    public void testDeliveryNotPending_RedirectsWithError() throws Exception {
        when(deliveryDAO.getDeliveryStatusById(5)).thenReturn("Confirmed");

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("Chỉ được hủy khi đơn giao hàng đang chờ và đơn hàng đang xử lý", "UTF-8")));
    }

    /**
     * Order Not Processing
     * 
     * deliveryId = 5
     * cancelReason = "Reason valid"
     * orderId = 101
     * userId = 4 // invalid role
     * userRole = 5
     * dealerId = 2
     * deliveryStatus = "Pending"
     * orderStatus = "Confirmed"
     * @throws Exception 
     */
    @Test
    public void testOrderNotProcessing_RedirectsWithError() throws Exception {
        when(deliveryDAO.getOrderStatusByDeliveryId(5)).thenReturn("Confirmed");

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("Chỉ được hủy khi đơn giao hàng đang chờ và đơn hàng đang xử lý", "UTF-8")));
    }

    /**
     * Update Delivery Fail
     * 
     * deliveryId = 5
     * cancelReason = "Reason valid"
     * orderId = 101
     * userId = 4 // invalid role
     * userRole = 5
     * dealerId = 2
     * deliveryStatus = "Pending"
     * orderStatus = "Confirmed"
     * @throws Exception 
     */
    @Test
    public void testUpdateDeliveryFail_RedirectsWithError() throws Exception {
        when(deliveryDAO.updateDeliveryStatus(5, "Canceled")).thenReturn(false);

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("Hủy giao hàng thất bại", "UTF-8")));
    }
}
