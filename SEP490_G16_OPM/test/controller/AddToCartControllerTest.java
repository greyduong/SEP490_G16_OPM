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

    private int sessionUserId = 5;
    private int offerId = 10;
    private String offerStatus = "Available";
    private int offerQuantity = 10;
    private int offerMinQuantity = 5;
    private String offerName = "Offer Name";

    @Before
    public void dependencies() {
        // mock dao
        doReturn(cartDAO).when(controller).getCartDAO();
        doReturn(pigsOfferDAO).when(controller).getPigsOfferDAO();
        when(request.getSession()).thenReturn(session);
    }

    public void setup() throws Exception {
        sessionUser = new User();
        sessionUser.setUserID(sessionUserId);

        offer = new PigsOffer();
        offer.setOfferID(offerId);
        offer.setStatus(offerStatus);
        offer.setMinQuantity(offerMinQuantity);
        offer.setQuantity(offerQuantity);
        offer.setName(offerName);

        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(pigsOfferDAO.getOfferById(offerId)).thenReturn(offer);
        doNothing().when(cartDAO).addToCart(sessionUserId, offerId, offerQuantity);
        doNothing().when(response).sendRedirect(anyString());
        when(request.getParameter("offerId")).thenReturn(String.valueOf(offerId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(offerQuantity));
    }

    /**
     * Test Case: Success - Success Add To Cart
     *
     * <br><b>Parameters:</b>
     * <br>ParameterOfferID = 10
     * <br>ParameterQuantity = 10
     *
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>Usser logged
     * <br>OfferId = 10 // has offer id 10 in database
     * <br>OfferStatus = "Available" // offer id 10 has status "Available"
     * <br>OfferMinQuantity = 5 // offer id 10 has min quantity 5
     * <br>OfferQuantity = 100 // offer id 10 has quantity 100
     *
     * <br><b>Expected:</b>
     * <br>Add to cart successfully
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_Success_AddToCart() throws Exception {
        setup();

        controller.doPost(request, response);

        verify(pigsOfferDAO).getOfferById(offerId);
        verify(cartDAO).addToCart(sessionUserId, offerId, offerQuantity);
        verify(response).sendRedirect(contains("cart?search=Offer+Name"));
    }

    /**
     * Test Case: Quantity Less Than Min Quantity - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterOfferID = 10
     * <br>ParameterQuantity = 4 // less then min quantity
     *
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>User logged
     * <br>OfferId = 10
     * <br>OfferStatus = "Available"
     * <br>OfferMinQuantity = 5
     * <br>OfferQuantity = 100
     *
     * <br><b>Expected:</b>
     * <br>Show message "Số lượng không phù hợp"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_QuantityLessThanMinQuantity_RedirectHomeWithMessage() throws Exception {
        offerQuantity = 4;

        setup();

        controller.doPost(request, response);

        verify(pigsOfferDAO).getOfferById(offerId);
        verify(session).setAttribute(eq("msg"), contains("Số lượng không phù hợp"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Offer Unavailable - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterOfferID = 10
     * <br>ParameterQuantity = 5
     *
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>User logged
     * <br>OfferId = 10
     * <br>OfferStatus = "Unavailable" // offer id 10 in database has status
     * "Unavailable"
     * <br>OfferMinQuantity = 10
     * <br>OfferQuantity = 100
     *
     * <br><b>Expected:</b>
     * <br>Show message "Chào bán này hiện không thể đặt hàng do đang ngưng bán
     * hoặc bị cấm."
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OfferUnavailable_RedirectHomeWithMessage() throws Exception {
        offerStatus = "Unavailable";

        setup();

        controller.doPost(request, response);

        verify(pigsOfferDAO).getOfferById(offerId);
        verify(session).setAttribute(eq("msg"), contains("Chào bán này hiện không thể đặt hàng"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Offer Banned - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = 10
     *
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>User logged
     * <br>OfferId = 10
     * <br>OfferStatus = "Banned" // offer id 10 in database has status "Banned"
     * <br>OfferMinQuantity = 10
     * <br>OfferQuantity = 100
     *
     * <br><b>Expected:</b>
     * <br>Show message "Chào bán này hiện không thể đặt hàng do đang ngưng bán
     * hoặc bị cấm."
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OfferBanned_RedirectHomeWithMessage() throws Exception {
        offerStatus = "Banned";

        setup();

        controller.doPost(request, response);

        verify(pigsOfferDAO).getOfferById(offerId);
        verify(session).setAttribute(eq("msg"), contains("Chào bán này hiện không thể đặt hàng"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Offer Upcoming - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = 10
     *
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>User logged
     * <br>OfferStatus = "Upcoming" // offer id 10 in database has status
     * "Upcoming"
     * <br>OfferMinQuantity = 10
     * <br>OfferQuantity = 100
     *
     * <br><b>Expected:</b>
     * <br>Show message "Chào bán này hiện không thể đặt hàng do đang ngưng bán
     * hoặc bị cấm."
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OfferUpcoming_RedirectHomeWithMessage() throws Exception {
        offerStatus = "Upcoming";

        setup();

        controller.doPost(request, response);

        verify(pigsOfferDAO).getOfferById(offerId);
        verify(session).setAttribute(eq("msg"), contains("Chào bán này hiện không thể đặt hàng"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Offer Not Found - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = 10
     *
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>User logged
     * <br>Offer id 10 not exist in database
     *
     * <br><b>Expected:</b>
     * <br>Show message "Chào bán đã ngưng bán hoặc không tồn tại!"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OfferNotFound_RedirectHomeWithMessage() throws Exception {
        setup();

        when(pigsOfferDAO.getOfferById(offerId)).thenReturn(null);

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("ngưng bán hoặc không tồn tại"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Null Quantity - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = null
     * <br>ParameterOfferID = 10
     *
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>SessionUserID = 5
     *
     * <br><b>Expected:</b>
     * <br>Show message "Dữ liệu nhập không hợp lệ."
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_NullQuantity_RedirectHomeWithMessage() throws Exception {
        setup();

        when(request.getParameter("quantity")).thenReturn(null);

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("Dữ liệu nhập không hợp lệ"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: String Quantity - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = "abc"
     * <br>ParameterOfferID = 10
     *
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>SessionUserID = 5
     *
     * <br><b>Expected:</b>
     * <br>Show message "Dữ liệu nhập không hợp lệ."
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_StringQuantity_RedirectHomeWithMessage() throws Exception {
        setup();

        when(request.getParameter("quantity")).thenReturn("abc");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("Dữ liệu nhập không hợp lệ"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Overflow Quantity - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity ="9999999999999999"
     * <br>ParameterOfferID = 10
     *
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>SessionUserID = 5
     *
     * <br><b>Expected:</b>
     * <br>Show message "Dữ liệu nhập không hợp lệ."
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OverflowQuantity_RedirectHomeWithMessage() throws Exception {
        setup();

        when(request.getParameter("quantity")).thenReturn("9999999999999999");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("Dữ liệu nhập không hợp lệ"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Null OfferID - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = null
     * 
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>User logged
     *
     * <br><b>Expected:</b>
     * <br>Show message "Dữ liệu nhập không hợp lệ."
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_NullOfferID_RedirectHomeWithMessage() throws Exception {
        setup();

        when(request.getParameter("offerId")).thenReturn(null);

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("Dữ liệu nhập không hợp lệ"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: String OfferID - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = "abc"
     * 
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>User logged
     *
     * <br><b>Expected:</b>
     * <br>Show message "Dữ liệu nhập không hợp lệ."
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_StringOfferID_RedirectHomeWithMessage() throws Exception {
        setup();

        when(request.getParameter("offerId")).thenReturn("abc");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("Dữ liệu nhập không hợp lệ"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Negative OfferID - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = "-1"
     * 
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>User logged
     *
     * <br><b>Expected:</b>
     * <br>Show message "Chào bán đã ngưng bán hoặc không tồn tại!"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_NegativeOfferID_RedirectHomeWithMessage() throws Exception {
        setup();

        when(request.getParameter("offerId")).thenReturn("-101");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("ngưng bán hoặc không tồn tại"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Overflow OfferID - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = "9999999999"
     * 
     * <br><b>Precondition:</b>
     * <br>Database connected
     * <br>User logged
     *
     * <br><b>Expected:</b>
     * <br>Show message "Dữ liệu nhập không hợp lệ."
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_OverflowOfferID_RedirectHomeWithMessage() throws Exception {
        setup();

        when(request.getParameter("offerId")).thenReturn("9999999999");

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("Dữ liệu nhập không hợp lệ"));
        verify(response).sendRedirect("home");
    }

    /**
     * Test Case: Not Connect To Database - Redirect Home With Message
     *
     * <br><b>Parameters:</b>
     * <br>ParameterQuantity = 5
     * <br>ParameterOfferID = 10
     * 
     * <br><b>Precondition:</b>
     * <br>Database not connected
     * <br>User logged
     *
     * <br><b>Expected:</b>
     * <br>Show message "Có lỗi xảy ra trong quá trình xử lý"
     *
     * @throws Exception
     */
    @Test
    public void testDoPost_NotConnectDatabase_RedirectHomeWithMessage() throws Exception {
        setup();

        when(pigsOfferDAO.getOfferById(offerId)).thenThrow(new NullPointerException("Connection is null"));

        controller.doPost(request, response);

        verify(session).setAttribute(eq("msg"), contains("Có lỗi xảy ra trong quá trình xử lý"));
        verify(response).sendRedirect("home");
    }
}
