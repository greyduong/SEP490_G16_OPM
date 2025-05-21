package controller;

import dao.CartDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PigsOffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UpdateCartControllerTest {

    @InjectMocks
    private UpdateCartController controller;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private final int cartId = 1;
    private final int page = 2;

    @Before
    public void setUp() {
        when(request.getParameter("cartId")).thenReturn(String.valueOf(cartId));
        when(request.getParameter("page")).thenReturn(String.valueOf(page));
    }

    @Test
    public void testDoPost_InvalidQuantity_LessThanMin() throws Exception {
        // Given
        int invalidQuantity = 1;
        when(request.getParameter("quantity")).thenReturn(String.valueOf(invalidQuantity));

        PigsOffer offer = new PigsOffer();
        offer.setMinQuantity(5);
        offer.setQuantity(20);
        when(cartDAO.getPigsOfferByCartId(cartId)).thenReturn(offer);

        // When
        controller.doPost(request, response);

        // Then
        verify(response).sendRedirect("cart?page=2&error=Số lượng không hợp lệ");
        verify(cartDAO, never()).updateCartQuantity(anyInt(), anyInt());
    }

    @Test
    public void testDoPost_InvalidQuantity_GreaterThanMax() throws Exception {
        // Given
        int invalidQuantity = 25;
        when(request.getParameter("quantity")).thenReturn(String.valueOf(invalidQuantity));

        PigsOffer offer = new PigsOffer();
        offer.setMinQuantity(5);
        offer.setQuantity(20);
        when(cartDAO.getPigsOfferByCartId(cartId)).thenReturn(offer);

        // When
        controller.doPost(request, response);

        // Then
        verify(response).sendRedirect("cart?page=2&error=Số lượng không hợp lệ");
        verify(cartDAO, never()).updateCartQuantity(anyInt(), anyInt());
    }

    @Test
    public void testDoPost_ValidQuantity() throws Exception {
        // Given
        int validQuantity = 10;
        when(request.getParameter("quantity")).thenReturn(String.valueOf(validQuantity));

        PigsOffer offer = new PigsOffer();
        offer.setMinQuantity(5);
        offer.setQuantity(20);
        when(cartDAO.getPigsOfferByCartId(cartId)).thenReturn(offer);

        // When
        controller.doPost(request, response);

        // Then
        verify(cartDAO).updateCartQuantity(cartId, validQuantity);
        verify(response).sendRedirect("cart?page=2");
    }
}
