package controller;

import dao.CartDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RemoveCartControllerTest {

    @Spy
    private RemoveCartController controller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CartDAO cartDAO;

    @Before
    public void setup() {
        doReturn(cartDAO).when(controller).getCartDAO();
    }

    /**
     * Test Case 1: Delete success
     *
     * <br>id = "1"
     *
     * @throws Exception
     */
    @Test
    public void testValidID() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        doNothing().when(cartDAO).removeCartById(1);
        doNothing().when(response).sendRedirect("cart");

        controller.doGet(request, response);

        verify(response).sendRedirect("cart");
        verify(cartDAO).removeCartById(1);
    }

    /**
     * Test Case 2: Invalid ID
     *
     * <br>id = "invalid"
     *
     * @throws Exception
     */
    @Test
    public void testInvalidID() throws Exception {
        when(request.getParameter("id")).thenReturn("invalid");

        controller.doGet(request, response);
        doNothing().when(response).sendRedirect("cart");

        verify(response).sendRedirect("cart");
    }

}
