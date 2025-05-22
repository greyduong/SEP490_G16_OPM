package controller;

import dao.CartDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PigsOffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UpdateCartControllerTest {

    @Spy
    private UpdateCartController controller;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private int cartId = 101;
    private int page = 1;
    private int quantity = 11;
    private int cartOfferId = 202;
    private int cartOfferMinQuantity = 10;
    private int cartOfferQuantity = 100;

    private PigsOffer offer;

    public void setup() {
        doReturn(cartDAO).when(controller).getCartDAO();
        offer = new PigsOffer();
        offer.setMinQuantity(cartOfferMinQuantity);
        offer.setQuantity(cartOfferQuantity);
        when(cartDAO.getPigsOfferByCartId(cartId)).thenReturn(offer);
        when(request.getParameter("cartId")).thenReturn(String.valueOf(cartId));
        when(request.getParameter("page")).thenReturn(String.valueOf(page));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
    }

    @Test
    public void testDoPost_InvalidQuantity_LessThanMin() throws Exception {
        quantity = 9;
        setup();

        controller.doPost(request, response);

        verify(response).sendRedirect("cart?page=%s&error=Số lượng không hợp lệ".formatted(page));
        verify(cartDAO, never()).updateCartQuantity(anyInt(), anyInt());
    }

    @Test
    public void testDoPost_InvalidQuantity_GreaterThanMax() throws Exception {
        quantity = 999;

        setup();

        controller.doPost(request, response);

        // Then
        verify(response).sendRedirect("cart?page=%s&error=Số lượng không hợp lệ".formatted(page));
        verify(cartDAO, never()).updateCartQuantity(anyInt(), anyInt());
    }

    @Test
    public void testDoPost_ValidQuantity() throws Exception {
        quantity = 10;

        setup();

        controller.doPost(request, response);

        verify(cartDAO).updateCartQuantity(cartId, quantity);
        verify(response).sendRedirect("cart?page=%s".formatted(page));
    }
}
