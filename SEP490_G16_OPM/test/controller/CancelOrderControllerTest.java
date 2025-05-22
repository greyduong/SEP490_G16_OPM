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

    private User sessionUser;
    private Order order;

    @Before
    public void setUp() {
        doReturn(pigsOfferDAO).when(controller).getPigsOfferDAO();
        doReturn(orderDAO).when(controller).getOrderDAO();

        sessionUser = new User();
        sessionUser.setUserID(1);
        sessionUser.setEmail("test@example.com");
        sessionUser.setFullName("Test User");

        User seller = new User();
        seller.setUserID(2);
        seller.setEmail("seller@example.com");
        seller.setFullName("Seller User");

        order = new Order();
        order.setOrderID(100);
        order.setDealer(sessionUser);
        order.setSeller(seller);
        order.setStatus("Pending");
        order.setOfferID(20);
        order.setQuantity(10);
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).minusHours(12)));

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(controller.getOrderDAO()).thenReturn(orderDAO);

        // mock email
        mockEmail = Mockito.mockStatic(Email.class);
        mockEmail.when(() -> Email.sendEmail(anyString(), anyString(), anyString())).thenAnswer(inv -> null);
    }

    @After
    public void tearDown() {
        mockEmail.close();
    }

    @Test
    public void testDoPost_MissingOrderId() throws Exception {
        controller.doPost(request, response);
        verify(session).setAttribute(eq("msg"), eq("Thiếu mã đơn hàng."));
        verify(response).sendRedirect("myorders");
        verifyNoMoreInteractions(orderDAO, pigsOfferDAO);
    }

    @Test
    public void testDoPost_InvalidCancelReason() throws Exception {
        when(request.getParameter("orderId")).thenReturn("100");
        when(request.getParameter("cancelReason")).thenReturn("");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), eq("Lý do hủy không được để trống."));
        verify(response).sendRedirect("myorders");
        verifyNoMoreInteractions(orderDAO, pigsOfferDAO);
    }

    @Test
    public void testDoPost_OrderNotFound() throws Exception {
        when(request.getParameter("orderId")).thenReturn("100");
        when(request.getParameter("cancelReason")).thenReturn("Muốn đổi ý");
        when(orderDAO.getOrderById(100)).thenReturn(null);

        controller.doPost(request, response);

        verify(orderDAO).getOrderById(100);
        verify(session).setAttribute(eq("msg"), eq("Đơn hàng không tồn tại."));
        verify(response).sendRedirect("myorders");
        verifyNoMoreInteractions(pigsOfferDAO);
    }

    @Test
    public void testDoPost_OrderNotPending() throws Exception {
        order.setStatus("Processing");
        when(request.getParameter("orderId")).thenReturn("100");
        when(request.getParameter("cancelReason")).thenReturn("Không còn nhu cầu");
        when(orderDAO.getOrderById(100)).thenReturn(order);

        controller.doPost(request, response);

        verify(orderDAO).getOrderById(100);
        verify(session).setAttribute(eq("msg"), eq("Chỉ có đơn trạng thái chờ xác nhận mới được hủy."));
        verify(response).sendRedirect("myorders");
        verifyNoMoreInteractions(pigsOfferDAO);
    }

    @Test
    public void testDoPost_NotOrderOwner() throws Exception {
        User anotherUser = new User();
        anotherUser.setUserID(3);
        when(request.getParameter("orderId")).thenReturn("100");
        when(request.getParameter("cancelReason")).thenReturn("Tìm được chỗ khác");
        when(orderDAO.getOrderById(100)).thenReturn(order);
        when(session.getAttribute("user")).thenReturn(anotherUser);

        controller.doPost(request, response);

        verify(orderDAO).getOrderById(100);
        verify(session).setAttribute(eq("msg"), eq("Bạn không có quyền hủy đơn này."));
        verify(response).sendRedirect("myorders");
        verifyNoMoreInteractions(pigsOfferDAO);
    }

    @Test
    public void testDoPost_OrderOlderThan24Hours() throws Exception {
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).minusHours(25)));
        when(request.getParameter("orderId")).thenReturn("100");
        when(request.getParameter("cancelReason")).thenReturn("Hủy vì lâu quá");
        when(orderDAO.getOrderById(100)).thenReturn(order);

        controller.doPost(request, response);

        verify(orderDAO).getOrderById(100);
        verify(session).setAttribute(eq("msg"), eq("Đơn hàng đã quá 24 giờ, không thể hủy."));
        verify(response).sendRedirect("myorders");
        verifyNoMoreInteractions(pigsOfferDAO);
    }

    @Test
    public void testDoPost_CancelOrderSuccess_NoteSuccess_OfferQuantityUpdated_StatusUpdated_EmailsSent() throws Exception {
        when(request.getParameter("orderId")).thenReturn("100");
        when(request.getParameter("cancelReason")).thenReturn("Không còn nhu cầu nữa");
        when(orderDAO.getOrderById(100)).thenReturn(order);
        when(orderDAO.cancelOrder(100)).thenReturn(true);
        when(orderDAO.updateOrderNote(100, "Không còn nhu cầu nữa")).thenReturn(true);
        when(pigsOfferDAO.getOfferQuantity(20)).thenReturn(10 + 10); // Initial quantity + cancelled quantity

        controller.doPost(request, response);

        verify(orderDAO).getOrderById(100);
        verify(orderDAO).cancelOrder(100);
        verify(orderDAO).updateOrderNote(100, "Không còn nhu cầu nữa");
        verify(pigsOfferDAO).updateOfferQuantity(20, 10);
        verify(pigsOfferDAO).getOfferQuantity(20);
        verify(pigsOfferDAO).setOfferStatus(20, "Available");
        verify(session).setAttribute(eq("msg"), eq("Hủy đơn thành công."));
        verify(response).sendRedirect("myorders");
        // Further verification for email sending might be needed if Email class interaction is mocked.
    }

    @Test
    public void testDoPost_CancelOrderSuccess_NoteFails_OfferQuantityUpdated_StatusUpdated_EmailsSent() throws Exception {
        when(request.getParameter("orderId")).thenReturn("100");
        when(request.getParameter("cancelReason")).thenReturn("Thay đổi quyết định");
        when(orderDAO.getOrderById(100)).thenReturn(order);
        when(orderDAO.cancelOrder(100)).thenReturn(true);
        when(orderDAO.updateOrderNote(100, "Thay đổi quyết định")).thenReturn(false);
        when(pigsOfferDAO.getOfferQuantity(20)).thenReturn(10 + 10);

        controller.doPost(request, response);

        verify(orderDAO).getOrderById(100);
        verify(orderDAO).cancelOrder(100);
        verify(orderDAO).updateOrderNote(100, "Thay đổi quyết định");
        verify(pigsOfferDAO).updateOfferQuantity(20, 10);
        verify(pigsOfferDAO).getOfferQuantity(20);
        verify(pigsOfferDAO).setOfferStatus(20, "Available");
        verify(session).setAttribute(eq("msg"), eq("Hủy đơn thành công, nhưng không thể lưu ghi chú."));
        verify(response).sendRedirect("myorders");
    }

    @Test
    public void testDoPost_CancelOrderFails() throws Exception {
        when(request.getParameter("orderId")).thenReturn("100");
        when(request.getParameter("cancelReason")).thenReturn("Lỗi hệ thống");
        when(orderDAO.getOrderById(100)).thenReturn(order);
        when(orderDAO.cancelOrder(100)).thenReturn(false);

        controller.doPost(request, response);

        verify(orderDAO).getOrderById(100);
        verify(orderDAO).cancelOrder(100);
        verify(session).setAttribute(eq("msg"), eq("Hủy đơn thất bại, vui lòng thử lại."));
        verify(response).sendRedirect("myorders");
        verifyNoMoreInteractions(pigsOfferDAO);
    }

    @Test
    public void testDoPost_InvalidOrderIdFormat() throws Exception {
        when(request.getParameter("orderId")).thenReturn("abc");
        when(request.getParameter("cancelReason")).thenReturn("Không rõ lý do");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), eq("Mã đơn hàng không hợp lệ."));
        verify(response).sendRedirect("myorders");
        verifyNoMoreInteractions(orderDAO, pigsOfferDAO);
    }
}
