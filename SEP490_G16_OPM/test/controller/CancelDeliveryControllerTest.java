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

    @Spy
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

    @Before
    public void setUp() throws Exception {
        // mock dao
        doReturn(deliveryDAO).when(controller).getDeliveryDAO();
        doReturn(orderDAO).when(controller).getOrderDAO();
        doReturn(userDAO).when(controller).getUserDAO();

        // mock email
        mockEmail = Mockito.mockStatic(Email.class);
        mockEmail.when(() -> Email.sendEmail(anyString(), anyString(), anyString())).thenAnswer(inv -> null);
    }

    @After
    public void tearDown() {
        mockEmail.close();
    }

    /**
     * Test Case 1: Sucessfull
     *
     * <br>ParameterDeliveryId = 5
     * <br>ParameterCancelReason = "Reason valid"
     * <br>SessionUserId = 2
     * <br>SessionUserRole = 5
     * <br>DeliveryOrderId = 101
     * <br>DeliveryDealerId = 5
     * <br>DeliverySellerId = 4
     * <br>DeliveryStatus = "Pending"
     * <br>DeliveryOrderStatus = "Processing"
     * <br>DeliveryTotalPrice = 1000000.0
     *
     * @throws Exception
     */
    @Test
    public void testSuccessfulCancellation() throws Exception {
        String parameterDeliveryId = "5";
        String parameterCancelReason = "Reason valid";
        int sessionUserId = 2;
        int sessionUserRole = 5;
        int deliveryId = 5;
        int deliveryDealerId = 2;
        String deliveryStatus = "Pending";
        double deliveryTotalPrice = 1000000.0;
        int deliveryQuantity = 10;
        int deliveryOrderId = 101;
        int deliveryOrderDealerId = 5;
        int deliveryOrderSellerId = 4;
        String deliveryOrderStatus = "Processing";

        User sessionUser = new User();
        sessionUser.setUserID(sessionUserId);
        sessionUser.setRoleID(sessionUserRole);

        User dealer = new User();
        dealer.setUserID(deliveryDealerId);
        dealer.setFullName("Dealer A");
        dealer.setEmail("dealer@example.com");

        User seller = new User();
        seller.setFullName("Seller B");
        seller.setEmail("seller@example.com");

        Order order = new Order();
        order.setDealerID(deliveryOrderDealerId);
        order.setDealer(dealer);
        order.setSellerID(deliveryOrderSellerId);
        order.setSeller(seller);
        order.setStatus(deliveryOrderStatus);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(request.getParameter("deliveryID")).thenReturn(parameterDeliveryId);
        when(request.getParameter("cancelReason")).thenReturn(parameterCancelReason);
        doNothing().when(response).sendRedirect(anyString());
        when(deliveryDAO.getOrderIdByDeliveryId(deliveryId)).thenReturn(deliveryOrderId);
        when(deliveryDAO.getDealerIdByDeliveryId(deliveryId)).thenReturn(deliveryDealerId);
        when(deliveryDAO.getDeliveryStatusById(deliveryId)).thenReturn(deliveryStatus);
        when(deliveryDAO.getOrderStatusByDeliveryId(deliveryId)).thenReturn(deliveryOrderStatus);
        when(deliveryDAO.updateDeliveryStatus(deliveryId, "Canceled")).thenReturn(true);
        when(deliveryDAO.appendToDeliveryComments(eq(deliveryId), contains("HỦY"))).thenReturn(true);
        when(orderDAO.getOrderById(deliveryOrderId)).thenReturn(order);

        when(userDAO.getUserById(deliveryOrderSellerId)).thenReturn(seller);
        when(userDAO.getUserById(deliveryOrderDealerId)).thenReturn(dealer);

        when(deliveryDAO.getDeliveryTotalPrice(deliveryId)).thenReturn(deliveryTotalPrice);
        when(deliveryDAO.getDeliveryQuantity(deliveryId)).thenReturn(deliveryQuantity);

        // Actual
        controller.doPost(request, response);

        // Expected
        verify(response).sendRedirect(contains(URLEncoder.encode("Giao hàng đã được hủy", StandardCharsets.UTF_8)));
        mockEmail.verify(() -> Email.sendEmail(anyString(), anyString(), anyString()), times(2));
    }

    /**
     * Test Case 2: Unauthenticated
     *
     * <br>ParameterDeliveryId = 5
     * <br>ParameterCancelReason = "Reason valid"
     * <br>SessionUserId = null
     * <br>SessionUserRole = null
     * <br>DeliveryOrderId = 101
     * <br>DeliveryDealerId = 5
     * <br>DeliverySellerId = 4
     * <br>DeliveryStatus = "Pending"
     * <br>DeliveryOrderStatus = "Processing"
     * <br>DeliveryTotalPrice = 1000000.0
     *
     * @throws Exception
     */
    @Test
    public void testUnauthenticated_RedirectsWithError() throws Exception {
        String parameterDeliveryId = "5";
        String parameterCancelReason = "Reason valid";
        int sessionUserId = 2;
        int sessionUserRole = 5;
        int deliveryId = 5;
        int deliveryDealerId = 2;
        int deliveryOrderId = 101;
        int deliveryOrderDealerId = 5;
        int deliveryOrderSellerId = 4;
        String deliveryOrderStatus = "Processing";

        User sessionUser = new User();
        sessionUser.setUserID(sessionUserId);
        sessionUser.setRoleID(sessionUserRole);

        User dealer = new User();
        dealer.setUserID(deliveryDealerId);
        dealer.setFullName("Dealer A");
        dealer.setEmail("dealer@example.com");

        User seller = new User();
        seller.setFullName("Seller B");
        seller.setEmail("seller@example.com");

        Order order = new Order();
        order.setDealerID(deliveryOrderDealerId);
        order.setDealer(dealer);
        order.setSellerID(deliveryOrderSellerId);
        order.setSeller(seller);
        order.setStatus(deliveryOrderStatus);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        when(request.getParameter("deliveryID")).thenReturn(parameterDeliveryId);
        when(request.getParameter("cancelReason")).thenReturn(parameterCancelReason);

        when(deliveryDAO.getOrderIdByDeliveryId(deliveryId)).thenReturn(deliveryOrderId);
        when(deliveryDAO.getDealerIdByDeliveryId(deliveryId)).thenReturn(deliveryDealerId);

        // Actual
        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("Bạn không có quyền hủy giao hàng này.", "UTF-8")));
    }
}
