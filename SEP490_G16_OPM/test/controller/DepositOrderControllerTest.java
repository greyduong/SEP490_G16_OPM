package controller;

import dao.OrderDAO;
import dao.WalletUseHistoryDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import model.Email;
import model.Order;
import model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DepositOrderControllerTest {

    @InjectMocks
    private DepositOrderController controller;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private WalletUseHistoryDAO walletUseHistoryDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private User dealerUser;
    private Order order;

    private MockedStatic<Email> mockEmail;

    @Before
    public void setUp() {
        mockEmail = Mockito.mockStatic(Email.class);
        mockEmail.when(() -> Email.sendEmail(anyString(), anyString(), anyString())).thenAnswer(inv -> null);
        dealerUser = new User();
        dealerUser.setUserID(2);

        order = new Order();
        order.setOrderID(100);
        order.setDealerID(2);
        order.setStatus("Confirmed");
        order.setTotalPrice(1000000);
        order.setProcessedDate(new Timestamp(System.currentTimeMillis() - 2 * 60 * 60 * 1000)); // 2 hours ago
        order.setDealer(dealerUser);
        User seller = new User();
        seller.setEmail("seller@example.com");
        order.setSeller(seller);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(dealerUser);
        when(request.getParameter("orderId")).thenReturn("100");
        when(request.getParameter("search")).thenReturn("");
        when(request.getParameter("status")).thenReturn("");
        when(request.getParameter("sort")).thenReturn("");
        when(request.getParameter("page")).thenReturn("1");
    }

    @After
    public void tearDown() {
        mockEmail.close();
    }

    /**
     *
     * userID = 2 orderId = 100 search = null status = null sort = null page = 1
     * dealerId = 2
     *
     * @throws Exception
     */
    @Test
    public void testSuccessfulDeposit() throws Exception {
        when(orderDAO.getOrderById(100)).thenReturn(order);
        when(walletUseHistoryDAO.use(2, 10000L, "Đặt cọc cho đơn hàng #100")).thenReturn(true);
        when(orderDAO.updateOrderStatus(100, "Deposited")).thenReturn(true);

        when(orderDAO.updateOrderNote(eq(100), anyString())).thenReturn(true);
        doNothing().when(response).sendRedirect(anyString());

        controller.doPost(request, response);

        verify(orderDAO).updateOrderStatus(100, "Deposited");
        mockEmail.verify(() -> Email.sendEmail(anyString(), anyString(), anyString()));
    }

    @Test
    public void testOrderExpiredOver24Hours() throws Exception {
        order.setProcessedDate(new Timestamp(System.currentTimeMillis() - 25 * 60 * 60 * 1000)); // 25 hours ago
        when(orderDAO.getOrderById(100)).thenReturn(order);
        doNothing().when(response).sendRedirect(anyString());

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("quá hạn 24 giờ", StandardCharsets.UTF_8)));
    }

    @Test
    public void testInsufficientWalletBalance() throws Exception {
        when(orderDAO.getOrderById(100)).thenReturn(order);
        when(walletUseHistoryDAO.use(2, 10000L, "Đặt cọc cho đơn hàng #100")).thenReturn(false);

        doNothing().when(response).sendRedirect(anyString());

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("Không đủ tiền", StandardCharsets.UTF_8)));
    }

    @Test
    public void testInvalidOrderId() throws Exception {
        when(request.getParameter("orderId")).thenReturn("notanumber");
        doNothing().when(response).sendRedirect(anyString());

        controller.doPost(request, response);

        verify(response).sendRedirect(contains("myorders"));
    }

    @Test
    public void testUnauthorizedDealer() throws Exception {
        dealerUser.setUserID(5); // mismatch
        when(orderDAO.getOrderById(100)).thenReturn(order);

        doNothing().when(response).sendRedirect(anyString());

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("không có quyền", StandardCharsets.UTF_8)));
    }

    @Test
    public void testUpdateOrderFails() throws Exception {
        when(orderDAO.getOrderById(100)).thenReturn(order);
        when(walletUseHistoryDAO.use(anyInt(), anyLong(), anyString())).thenReturn(true);
        when(orderDAO.updateOrderStatus(100, "Deposited")).thenReturn(false);

        doNothing().when(response).sendRedirect(anyString());

        controller.doPost(request, response);

        verify(response).sendRedirect(contains(URLEncoder.encode("Không thể cập nhật", StandardCharsets.UTF_8)));
    }
}
