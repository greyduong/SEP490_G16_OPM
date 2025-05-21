package controller;

import dao.CartDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ShoppingCartControllerTest {

    @InjectMocks
    private ShoppingCartController controller;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    private User mockUser;

    @Before
    public void setUp() {
        mockUser = new User();
        mockUser.setUserID(10); // any sample user ID

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getRequestDispatcher("shoppingcart.jsp")).thenReturn(dispatcher);
    }

    /**
     * Default Parameters
     * 
     * page = null
     * search = null
     * sort = null
     * @throws Exception 
     */
    @Test
    public void testDoGet_withDefaultParams() throws Exception {
        // Given
        when(request.getParameter("page")).thenReturn(null); // no page param
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameterMap()).thenReturn(new HashMap<>());

        List<Cart> mockCartList = Arrays.asList(new Cart(), new Cart());
        when(cartDAO.getCartByUserIdWithFilter(10, 1, 3, null, null)).thenReturn(mockCartList);
        when(cartDAO.countCartItemsByUserWithFilter(10, null)).thenReturn(2);

        // When
        controller.doGet(request, response);

        // Then
        verify(request).setAttribute("cartList", mockCartList);
        verify(request).setAttribute("totalPages", 1);
        verify(request).setAttribute("currentPage", 1);
        verify(request).setAttribute("totalItems", 2);
        verify(request).setAttribute("param", request.getParameterMap());
        verify(dispatcher).forward(request, response);
    }
}
