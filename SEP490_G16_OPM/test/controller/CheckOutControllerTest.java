package controller;

import dao.CartDAO;
import dao.OrderDAO;
import dao.PigsOfferDAO;
import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.Email;
import model.PigsOffer;
import model.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CheckOutControllerTest {

    @Spy
    private CheckOutController controller;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private PigsOfferDAO offerDAO;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    private MockedStatic<Email> mockEmail;

    // default values
    private int cartId = 101;
    private int offerId = 202;
    private int quantity = 10;
    private int sessionUserId = 5;
    private int cartUserId = 5;
    private int offerSellerId = 2;
    private int offerQuantity = 10;
    private int offerMinQuantity = 1;
    private double offerRetailPrice = 1000;
    private double offerTotalPrice = 10000;
    private String offerStatus = "Available";

    private User sessionUser;
    private Cart cart;
    private PigsOffer offer;
    private PigsOffer updatedOffer;
    private User offerSeller;

    public void setUp() throws Exception {
        doReturn(cartDAO).when(controller).getCartDAO();
        doReturn(offerDAO).when(controller).getOfferDAO();
        doReturn(orderDAO).when(controller).getOrderDAO();
        doReturn(userDAO).when(controller).getUserDAO();

        // mock email
        mockEmail = Mockito.mockStatic(Email.class);
        mockEmail.when(() -> Email.sendEmail(anyString(), anyString(), anyString())).thenAnswer(inv -> null);

        // create object
        sessionUser = new User();
        sessionUser.setUserID(sessionUserId);
        sessionUser.setFullName("Example User");
        sessionUser.setEmail("user@example.com");
        offerSeller = new User();
        offerSeller.setFullName("Example Seller");
        offerSeller.setEmail("seller@example.com");
        cart = new Cart();
        cart.setCartID(cartId);
        cart.setOfferID(offerId);
        cart.setUser(new User());
        cart.getUser().setUserID(cartUserId);
        cart.setUserID(cartUserId);
        cart.setPigsOffer(offer);
        offer = new PigsOffer();
        offer.setName("Example Offer");
        updatedOffer = new PigsOffer();
        offer.setSellerID(offerSellerId);
        offer.setSeller(new User());
        offer.getSeller().setUserID(offerSellerId);
        offer.setQuantity(offerQuantity);
        offer.setMinQuantity(offerMinQuantity);
        offer.setRetailPrice(offerRetailPrice);
        offer.setTotalOfferPrice(offerTotalPrice);
        offer.setStatus(offerStatus);

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(session.getAttribute("user")).thenReturn(sessionUser);

        when(request.getParameter("cartId")).thenReturn(String.valueOf(cartId));
        when(request.getParameter("offerId")).thenReturn(String.valueOf(offerId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        when(cartDAO.getCartById(cartId)).thenReturn(cart);
        when(offerDAO.getOfferById(offerId)).thenReturn(offer).thenReturn(updatedOffer);
        when(userDAO.getUserById(offerSellerId)).thenReturn(offerSeller);

        doNothing().when(orderDAO).insertOrder(anyInt(), anyInt(), anyInt(), anyInt(), anyDouble());
        doNothing().when(offerDAO).updateOfferQuantityAfterCheckout(anyInt(), anyInt());
        doNothing().when(offerDAO).updateOfferStatus(anyInt(), eq("Unavailable"));
        when(cartDAO.deleteCartById(anyInt())).thenReturn(true);
        doNothing().when(response).sendRedirect(anyString());
        doNothing().when(dispatcher).forward(any(), any());

        updatedOffer.setQuantity(offerQuantity - quantity);
    }

    @After
    public void tearDown() {
        mockEmail.close();
    }

    /**
     * Test Case 1: Success - Quantity Equals Offer Quantity
     *
     * <br>ParameterCartId = "101"
     * <br>ParameterOfferId = "202"
     * <br>ParameterQuantity = "10"
     * <br>SessionUserId = 5
     * <br>CartUserId = 5
     * <br>OfferSellerId = 2
     * <br>OfferQuantity = 10
     * <br>OfferMinQuantity = 1
     * <br>OfferRetailPrice = 1000
     * <br>OfferTotalOfferPrice = 10000
     * <br>OfferStatus = "Available"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_SuccessfulCheckout_QuantityEqualsOfferQuantity() throws Exception {
        setUp();

        controller.doPost(request, response);

        verify(orderDAO).insertOrder(sessionUserId, offerSellerId, offerId, quantity, offerTotalPrice);
        verify(offerDAO).updateOfferQuantityAfterCheckout(offerId, quantity);
        verify(offerDAO).updateOfferStatus(offerId, "Unavailable");
        verify(cartDAO).deleteCartById(cartId);
        verify(response).sendRedirect("myorders");
    }

    /**
     * Test Case 2: Success - Quantity Less Than Offer Quantity
     *
     * <br>ParameterCartId = "101"
     * <br>ParameterOfferId = "202"
     * <br>ParameterQuantity = "9" // less then offer quantity
     * <br>SessionUserId = 5
     * <br>CartUserId = 5
     * <br>OfferSellerId = 2
     * <br>OfferQuantity = 10
     * <br>OfferMinQuantity = 1
     * <br>OfferRetailPrice = 1000
     * <br>OfferTotalOfferPrice = 10000
     * <br>OfferStatus = "Available"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_SuccessfulCheckout_QuantityLessThanOfferQuantity() throws Exception {
        quantity = 9;

        setUp();

        double expectedTotalPrice = offer.getRetailPrice() * quantity;

        controller.doPost(request, response);

        verify(orderDAO).insertOrder(sessionUserId, offerSellerId, offerId, quantity, expectedTotalPrice);
        verify(offerDAO).updateOfferQuantityAfterCheckout(offerId, quantity);
        verify(offerDAO, never()).updateOfferStatus(anyInt(), anyString());
        verify(cartDAO).deleteCartById(cartId);
        verify(response).sendRedirect("myorders");
    }

    /**
     * Test Case 3: Invalid Cart - Forward JSP With Error
     *
     * <br>ParameterCartId = "102" // invalid cart id
     * <br>ParameterOfferId = "202"
     * <br>ParameterQuantity = "9"
     * <br>SessionUserId = 5
     * <br>CartUserId = 5
     * <br>OfferSellerId = 2
     * <br>OfferQuantity = 10
     * <br>OfferMinQuantity = 1
     * <br>OfferRetailPrice = 1000
     * <br>OfferTotalOfferPrice = 10000
     * <br>OfferStatus = "Available"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_InvalidCart_ForwardJSPWithError() throws Exception {
        cartId = 102;

        setUp();

        // Cart not found (null)
        when(cartDAO.getCartById(cartId)).thenReturn(null);

        controller.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Giỏ hàng không hợp lệ."));
    }

    /**
     * Test Case 4: Cart User Mismatch - Forward JSP With Error
     *
     * <br>ParameterCartId = "101"
     * <br>ParameterOfferId = "202"
     * <br>ParameterQuantity = "9"
     * <br>SessionUserId = 5
     * <br>CartUserId = 4 // cart user id mismatch
     * <br>OfferSellerId = 2
     * <br>OfferQuantity = 10
     * <br>OfferMinQuantity = 1
     * <br>OfferRetailPrice = 1000
     * <br>OfferTotalOfferPrice = 10000
     * <br>OfferStatus = "Available"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_CartUserMismatch() throws Exception {
        cartUserId = 4;

        setUp();

        controller.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Giỏ hàng không hợp lệ."));
    }

    /**
     * Test Case 5: Offer Not Found - Forward JSP With Error
     *
     * <br>ParameterCartId = "101"
     * <br>ParameterOfferId = "203" // offer id not found
     * <br>ParameterQuantity = "9"
     * <br>SessionUserId = 5
     * <br>CartUserId = 5
     * <br>OfferSellerId = null
     * <br>OfferQuantity = null
     * <br>OfferMinQuantity = null
     * <br>OfferRetailPrice = null
     * <br>OfferTotalOfferPrice = null
     * <br>OfferStatus = null
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OfferNotFound() throws Exception {
        offerId = 203;

        setUp();

        // offer not found
        when(offerDAO.getOfferById(offerId)).thenReturn(null);

        controller.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Không tìm thấy offer."));
    }

    /**
     * Test Case 6: Offer Banned
     *
     * <br>ParameterCartId = "101"
     * <br>ParameterOfferId = "202"
     * <br>ParameterQuantity = "10"
     * <br>SessionUserId = 5
     * <br>CartUserId = 5
     * <br>OfferSellerId = 2
     * <br>OfferQuantity = 10
     * <br>OfferMinQuantity = 1
     * <br>OfferRetailPrice = 1000
     * <br>OfferTotalOfferPrice = 10000
     * <br>OfferStatus = "Banned" // offer banned
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OfferBanned() throws Exception {
        offerStatus = "Banned";

        setUp();

        controller.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Chào bán này hiện không thể đặt hàng do đang ngưng bán hoặc bị cấm."));
        verify(dispatcher).forward(request, response);
    }

    /**
     * Test Case 7: Quantity Less Then Min Quantity
     *
     * <br>ParameterCartId = "101"
     * <br>ParameterOfferId = "202"
     * <br>ParameterQuantity = "10"
     * <br>SessionUserId = 5
     * <br>CartUserId = 5
     * <br>OfferSellerId = 2
     * <br>OfferQuantity = 10
     * <br>OfferMinQuantity = 11 // min quantity > quantity
     * <br>OfferRetailPrice = 1000
     * <br>OfferTotalOfferPrice = 10000
     * <br>OfferStatus = "Available"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_QuantityInvalid() throws Exception {
        // less then min
        quantity = 10;
        offerMinQuantity = 11;

        setUp();

        controller.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Số lượng không hợp lệ."));
        verify(dispatcher).forward(request, response);
    }
}
