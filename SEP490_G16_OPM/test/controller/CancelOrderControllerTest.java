package controller;

import dao.OrderDAO;
import dao.PigsOfferDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import model.Email;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CancelOrderControllerTest {

    @Spy
    private CancelOrderController controller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private PigsOfferDAO pigsOfferDAO;

    private MockedStatic<Email> mockEmail;

    private int orderId = 101;
    private String cancelReason = "Valid Cancel Reason";
    private int sessionUserId = 5;
    private int orderDealerId = 5;
    private int orderSellerId = 4;
    private int orderOfferId = 202;
    private int orderQuantity = 10;
    private String orderStatus = "Pending";
    private Timestamp orderCreatedAt = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).minusHours(12));

    private User sessionUser;
    private User orderDealer;
    private User orderSeller;
    private Order order;

    @Before
    public void dependencies() {
        doReturn(pigsOfferDAO).when(controller).getPigsOfferDAO();
        doReturn(orderDAO).when(controller).getOrderDAO();
        when(request.getSession()).thenReturn(session);

        // mock email
        mockEmail = mockStatic(Email.class);
        mockEmail.when(() -> Email.sendEmail(anyString(), anyString(), anyString())).thenAnswer(inv -> null);
    }

    public void setup() {
        sessionUser = new User();
        sessionUser.setUserID(sessionUserId);
        sessionUser.setEmail("user@example.com");
        sessionUser.setFullName("Example User");
        orderSeller = new User();
        orderSeller.setUserID(orderSellerId);
        orderSeller.setEmail("seller@example.com");
        orderSeller.setFullName("Example Seller");
        orderDealer = new User();
        orderDealer.setUserID(orderDealerId);
        orderDealer.setEmail("dealer@example.com");
        orderDealer.setFullName("Example Dealer");
        order = new Order();
        order.setOrderID(orderId);
        order.setDealer(sessionUser);
        order.setSeller(orderSeller);
        order.setDealer(orderDealer);
        order.setOfferID(orderOfferId);
        order.setStatus(orderStatus);
        order.setOfferID(orderOfferId);
        order.setQuantity(orderQuantity);
        order.setCreatedAt(orderCreatedAt);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(request.getParameter("orderId")).thenReturn(String.valueOf(orderId));
        when(request.getParameter("cancelReason")).thenReturn(cancelReason);
        when(orderDAO.getOrderById(orderId)).thenReturn(order);
        when(orderDAO.updateOrderNote(orderId, cancelReason)).thenReturn(true);
        when(orderDAO.cancelOrder(orderId)).thenReturn(true);
        when(pigsOfferDAO.getOfferQuantity(orderId)).thenReturn(orderQuantity);
    }

    @After
    public void tearDown() {
        mockEmail.close();
    }

    /**
     * Test Case 1: Cancel Order Success
     *
     * <br>OrderId = 101
     * <br>CancelReason = "Example Cancel Reason"
     * <br>OrderStatus = "Pending"
     * <br>OrderCreatedAt = now().minusHours(12)
     * <br>OrderQuantity = 10
     * <br>SessionUserId = 4
     * <br>OrderDealerId = 4
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_Success() throws Exception {
        setup();
        
        controller.doPost(request, response);

        // cộng quantity lại vào offer
        verify(pigsOfferDAO).updateOfferQuantity(orderOfferId, orderQuantity);

        verify(session).setAttribute(eq("msg"), eq("Hủy đơn thành công."));
        verify(response).sendRedirect("myorders");
    }

    /**
     * Test Case 2: Missing Order Id - Redirect With Message
     *
     * <br>OrderId = "" // empty order id
     * <br>CancelReason = "Example Cancel Reason"
     * <br>OrderStatus = null
     * <br>OrderCreatedAt = null
     * <br>OrderQuantity = null
     * <br>SessionUserId = null
     * <br>OrderDealerId = null
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_MissingOrderId() throws Exception {
        setup();

        when(request.getParameter("orderId")).thenReturn(null);

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), eq("Thiếu mã đơn hàng."));
        verify(response).sendRedirect("myorders");
    }

    /**
     * Test Case 3: Invalid Cancel Reason - Redirect With Message
     *
     * <br>OrderId = 101
     * <br>CancelReason = "" // empty cancel reason
     * <br>OrderStatus = "Pending"
     * <br>OrderCreatedAt = now().minusHours(12)
     * <br>OrderQuantity = 10
     * <br>SessionUserId = 4
     * <br>OrderDealerId = 4
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_InvalidCancelReason() throws Exception {
        setup();

        when(request.getParameter("cancelReason")).thenReturn("");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), eq("Lý do hủy không được để trống."));
        verify(response).sendRedirect("myorders");
    }

    /**
     * Test Case 4: Order Not Found - Redirect With Message
     *
     * <br>OrderId = 102 // invalid order id
     * <br>CancelReason = "Valid Cancel Reason"
     * <br>OrderStatus = null
     * <br>OrderCreatedAt = null
     * <br>OrderQuantity = null
     * <br>SessionUserId = null
     * <br>OrderDealerId = null
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OrderNotFound() throws Exception {
        orderId = 102;

        setup();

        when(orderDAO.getOrderById(102)).thenReturn(null);

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), eq("Đơn hàng không tồn tại."));
        verify(response).sendRedirect("myorders");
    }

    /**
     * Test Case 5: Order Not Pending
     *
     * <br>OrderId = 101
     * <br>CancelReason = "Example Cancel Reason"
     * <br>OrderStatus = "Processing" // not pending
     * <br>OrderCreatedAt = now().minusHours(12)
     * <br>OrderQuantity = 10
     * <br>SessionUserId = 4
     * <br>OrderDealerId = 4
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OrderNotPending() throws Exception {
        orderStatus = "Processing";

        setup();

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), eq("Chỉ có đơn trạng thái chờ xác nhận mới được hủy."));
        verify(response).sendRedirect("myorders");
    }

    /**
     * Test Case 6: Unauthorized
     *
     * <br>OrderId = 101
     * <br>CancelReason = "Example Cancel Reason"
     * <br>OrderStatus = "Pending"
     * <br>OrderCreatedAt = now().minusHours(12)
     * <br>OrderQuantity = 10
     * <br>SessionUserId = 99 // not order owner (need equal dealerId)
     * <br>OrderDealerId = 4
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_Unauthorized() throws Exception {
        sessionUserId = 99;

        setup();

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), eq("Bạn không có quyền hủy đơn này."));
        verify(response).sendRedirect("myorders");
    }

    /**
     * Test Case 7: Order Older Than 24 Hours
     *
     * <br>OrderId = 101
     * <br>CancelReason = "Example Cancel Reason"
     * <br>OrderStatus = "Pending"
     * <br>OrderCreatedAt = now().minusHours(25) // over 24h
     * <br>OrderQuantity = 10
     * <br>SessionUserId = 4
     * <br>OrderDealerId = 4
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OrderOlderThan24Hours() throws Exception {
        orderCreatedAt = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).minusHours(25));

        setup();

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), eq("Đơn hàng đã quá 24 giờ, không thể hủy."));
        verify(response).sendRedirect("myorders");
    }


    /**
     * Test Case 8: Invalid OrderId Format
     *
     * <br>OrderId = "abc"
     * <br>CancelReason = "Example Cancel Reason"
     * <br>OrderStatus = "Pending"
     * <br>OrderCreatedAt = now().minusHours(12)
     * <br>OrderQuantity = 10
     * <br>SessionUserId = 4
     * <br>OrderDealerId = 4
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_InvalidOrderIdFormat() throws Exception {
        setup();

        when(request.getParameter("orderId")).thenReturn("abc");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), eq("Mã đơn hàng không hợp lệ."));
        verify(response).sendRedirect("myorders");
    }
}
