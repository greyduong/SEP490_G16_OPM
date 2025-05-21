package controller;

import dao.CartDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RemoveCartControllerTest {

    @InjectMocks
    private RemoveCartController controller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CartDAO cartDAO;

    /**
     * Delete success
     * 
     * id = "1"
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
     * Invalid ID
     * 
     * id = "invalid"
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
