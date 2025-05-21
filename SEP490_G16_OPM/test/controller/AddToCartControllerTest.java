package controller;
import dao.CartDAO;
import dao.PigsOfferDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.PigsOffer;
import model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddToCartControllerTest {

    @InjectMocks
    private AddToCartController controller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private PigsOfferDAO pigsOfferDAO;

    private User user;
    private PigsOffer offer;

    @Before
    public void setup() {
        Mockito.lenient();
        user = new User();
        user.setUserID(1);

        offer = new PigsOffer();
        offer.setOfferID(10);
        offer.setStatus("Available");
        offer.setMinQuantity(5);
        offer.setQuantity(100);
        offer.setName("Offer Name");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("offerId")).thenReturn("10");
        when(request.getParameter("quantity")).thenReturn("10");
        when(pigsOfferDAO.getOfferById(10)).thenReturn(offer);
    }

    /**
     * Success
     * 
     * UserID = 1
     * OfferID = 10
     * OfferStatus = "Available"
     * OfferMinQuantity = 5
     * OfferQuantity = 100
     * OfferName = "OfferName"
     * Quantity = 10
     * 
     * @throws Exception 
     */
    @Test
    public void testDoPost_Success_AddToCart() throws Exception {
        doNothing().when(cartDAO).addToCart(anyInt(), anyInt(), anyInt());
        doNothing().when(response).sendRedirect(anyString());

        var doPost = AddToCartController.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(controller, request, response);

        verify(cartDAO).addToCart(1, 10, 10);
        verify(response).sendRedirect(contains("cart?search=Offer+Name"));
    }

    /**
     * Invalid Quantity
     * 
     * UserID = 1
     * OfferID = 10
     * OfferStatus = "Available"
     * OfferMinQuantity = 10
     * OfferQuantity = 20
     * OfferName = "OfferName"
     * Quantity = 5
     * 
     * @throws Exception 
     */
    @Test
    public void testDoPost_InvalidQuantity() throws Exception {
        offer.setMinQuantity(10);
        when(request.getParameter("quantity")).thenReturn("5");

        doNothing().when(response).sendRedirect(anyString());

        var doPost = AddToCartController.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(controller, request, response);

        verify(session).setAttribute(eq("msg"), contains("Số lượng không phù hợp"));
        verify(response).sendRedirect("home");
    }

    /**
     * Offer Unavailable
     * 
     * UserID = 1
     * OfferID = 10
     * OfferStatus = "Banned"
     * OfferMinQuantity = 10
     * OfferQuantity = 20
     * OfferName = "OfferName"
     * Quantity = 5
     * 
     * @throws Exception 
     */
    @Test
    public void testDoPost_OfferUnavailable() throws Exception {
        offer.setStatus("Banned");
        when(request.getRequestDispatcher("shoppingcart.jsp")).thenReturn(dispatcher);

        var doPost = AddToCartController.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(controller, request, response);

        verify(session).setAttribute(eq("msg"), contains("Chào bán này hiện không thể đặt hàng"));
        verify(response).sendRedirect("home");
    }

    /**
     * Offer Not Found
     * 
     * UserID = 1
     * OfferID = 10
     * OfferStatus = null
     * OfferMinQuantity = null
     * OfferQuantity = null
     * OfferName = null
     * Quantity = 5
     * 
     * @throws Exception 
     */
    @Test
    public void testDoPost_OfferNotFound() throws Exception {
        when(pigsOfferDAO.getOfferById(10)).thenReturn(null);
        doNothing().when(response).sendRedirect("home");

        var doPost = AddToCartController.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(controller, request, response);

        verify(session).setAttribute(eq("msg"), contains("ngưng bán hoặc không tồn tại"));
        verify(response).sendRedirect("home");
    }

    /**
     * Invalid Input
     * 
     * UserID = 1
     * OfferID = 10
     * OfferStatus = "Available"
     * OfferMinQuantity = 5
     * OfferQuantity = 100
     * OfferName = "OfferName"
     * Quantity = "invalid"
     * 
     * @throws Exception 
     */
    @Test
    public void testDoPost_InvalidInput() throws Exception {
        when(request.getParameter("quantity")).thenReturn("invalid");

        doNothing().when(response).sendRedirect("home");

        var doPost = AddToCartController.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(controller, request, response);

        verify(session).setAttribute(eq("msg"), contains("Dữ liệu nhập không hợp lệ"));
        verify(response).sendRedirect("home");
    }
}

