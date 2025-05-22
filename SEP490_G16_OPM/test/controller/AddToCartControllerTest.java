package controller;

import dao.CartDAO;
import dao.PigsOfferDAO;
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

    @Spy
    private AddToCartController controller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private PigsOfferDAO pigsOfferDAO;

    private User sessionUser;
    private PigsOffer offer;

    @Before
    public void setup() throws Exception {
        sessionUser = new User();
        sessionUser.setUserID(4);

        offer = new PigsOffer();
        offer.setOfferID(10);
        offer.setStatus("Available");
        offer.setMinQuantity(5);
        offer.setQuantity(100);
        offer.setName("Offer Name");

        // mock dao
        doReturn(cartDAO).when(controller).getCartDAO();
        doReturn(pigsOfferDAO).when(controller).getPigsOfferDAO();

        // mock session
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(request.getSession()).thenReturn(session);

        // mock dao method
        when(pigsOfferDAO.getOfferById(anyInt())).thenReturn(offer);
        doNothing().when(cartDAO).addToCart(anyInt(), anyInt(), anyInt());
        doNothing().when(response).sendRedirect(anyString());
    }

    /**
     * Test Case 1: Success - Success Add To Cart
     *
     * <br>ParameterOfferID = 10
     * <br>ParameterQuantity = 10
     * <br>SessionUserID = 5
     * <br>OfferStatus = "Available"
     * <br>OfferMinQuantity = 5
     * <br>OfferQuantity = 100
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_Success_AddToCart() throws Exception {
        int quantity = 10;
        int sessionUserId = 5;
        int offerId = 10;
        String offerStatus = "Available";
        int offerMinQuantity = 5;
        int offerQuantity = 100;

        sessionUser.setUserID(sessionUserId);
        offer.setOfferID(offerId);
        offer.setStatus(offerStatus);
        offer.setMinQuantity(offerMinQuantity);
        offer.setQuantity(offerQuantity);

        when(request.getParameter("offerId")).thenReturn(String.valueOf(offerId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        controller.doPost(request, response);

        verify(pigsOfferDAO).getOfferById(offerId);
        verify(cartDAO).addToCart(sessionUserId, offerId, quantity);
        verify(response).sendRedirect(contains("cart?search=Offer+Name"));
    }

    /**
     * Test Case 2: Quantity Less Than Min Quantity - Redirect Home With Message
     *
     * <br>ParameterOfferID = 10
     * <br>ParameterQuantity = 10
     * <br>SessionUserID = 5
     * <br>OfferStatus = "Available"
     * <br>OfferMinQuantity = 11
     * <br>OfferQuantity = 100
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_QuantityLessThanMinQuantity_RedirectHomeWithMessage() throws Exception {
        int quantity = 10;
        int sessionUserId = 5;
        int offerId = 10;
        String offerStatus = "Available";
        int offerMinQuantity = 11;
        int offerQuantity = 100;

        sessionUser.setUserID(sessionUserId);
        offer.setOfferID(offerId);
        offer.setStatus(offerStatus);
        offer.setMinQuantity(offerMinQuantity);
        offer.setQuantity(offerQuantity);

        when(request.getParameter("offerId")).thenReturn(String.valueOf(offerId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        controller.doPost(request, response);

        verify(pigsOfferDAO).getOfferById(offerId);
        verify(session).setAttribute(eq("msg"), contains("Số lượng không phù hợp"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case 3: Offer Unavailable - Redirect Home With Message
     *
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = 10
     * <br>SessionUserID = 5
     * <br>OfferStatus = "Banned"
     * <br>OfferMinQuantity = 10
     * <br>OfferQuantity = 100
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OfferUnavailable_RedirectHomeWithMessage() throws Exception {
        int quantity = 10;
        int sessionUserId = 5;
        int offerId = 10;
        String offerStatus = "Banned";
        int offerMinQuantity = 11;
        int offerQuantity = 100;

        sessionUser.setUserID(sessionUserId);
        offer.setOfferID(offerId);
        offer.setStatus(offerStatus);
        offer.setMinQuantity(offerMinQuantity);
        offer.setQuantity(offerQuantity);

        when(request.getParameter("offerId")).thenReturn(String.valueOf(offerId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        controller.doPost(request, response);

        verify(pigsOfferDAO).getOfferById(offerId);
        verify(session).setAttribute(eq("msg"), contains("Chào bán này hiện không thể đặt hàng"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case 4: Offer Unavailable - Redirect Home With Message
     *
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = 11
     * <br>SessionUserID = 5
     * <br>OfferStatus = null
     * <br>OfferMinQuantity = null
     * <br>OfferQuantity = null
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OfferNotFound_RedirectHomeWithMessage() throws Exception {
        int quantity = 10;
        int offerId = 11;
        int sessionUserId = 5;

        sessionUser.setUserID(sessionUserId);

        when(request.getParameter("offerId")).thenReturn(String.valueOf(offerId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        when(pigsOfferDAO.getOfferById(11)).thenReturn(null);

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("ngưng bán hoặc không tồn tại"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case 5: Invalid Input - Redirect Home With Message
     *
     * <br>SessionUserID = 5
     * <br>ParameterQuantity = "invalid"
     * <br>ParameterOfferID = 10
     * <br>OfferStatus = "Available"
     * <br>OfferMinQuantity = 5
     * <br>OfferQuantity = 100
     * <br>OfferName = "OfferName"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_InvalidInput_RedirectHomeWithMessage() throws Exception {
        int sessionUserId = 5;

        sessionUser.setUserID(sessionUserId);

        when(request.getParameter("quantity")).thenReturn("invalid");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("Dữ liệu nhập không hợp lệ"));
        verify(response).sendRedirect("home");
    }
}
