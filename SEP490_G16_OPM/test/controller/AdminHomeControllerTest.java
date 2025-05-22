package controller;

import com.google.gson.Gson;
import dal.DBContext;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AdminHomeControllerTest {

    @Spy
    private AdminHomeController controller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private DBContext dbContext;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Before
    public void setUp() throws Exception {
        doReturn(dbContext).when(controller).getDBContext();
        when(request.getRequestDispatcher("admin.jsp")).thenReturn(requestDispatcher);
    }

    /**
     * Show Admin Dashboard
     * 
     * <br>totalUsers = 100
     *
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoGet() throws ServletException, IOException {
        int totalUsers = 100;
        when(dbContext.fetchOne(any(), anyString())).thenReturn(totalUsers);
        when(controller.getTotalUser(dbContext)).thenReturn(totalUsers);

        controller.doGet(request, response);

        verify(controller).getTotalUser(dbContext);
        verify(request).setAttribute("totalUser", totalUsers);
        verify(requestDispatcher).forward(request, response);
    }

    /**
     * Get User Chart Data
     * 
     * @throws ServletException
     * @throws IOException
     * @throws SQLException 
     */
    @Test
    public void testDoPost() throws ServletException, IOException, SQLException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        Map<String, Object> userChartData = Map.of(
                "name", "userChart",
                "labels", List.of("Active", "Inactive"),
                "datasets", List.of(Map.of("label", "Người dùng", "data", List.of(100, 50))),
                "type", "pie"
        );
        when(controller.getUserChart(dbContext)).thenReturn(userChartData);

        controller.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(controller).getUserChart(dbContext);

        List<Map<String, Object>> expectedCharts = List.of(userChartData);
        Gson gson = new Gson();
        assertEquals(gson.toJson(expectedCharts), stringWriter.toString().trim());
        writer.flush();
    }

    /**
     * Get Total User
     * 
     * <br>totalUsers = 100
     * 
     * @throws SQLException 
     */
    @Test
    public void testGetTotalUser() throws SQLException {
        int totalUsers = 100;
        when(dbContext.fetchOne(any(), anyString())).thenReturn(totalUsers);

        int actualTotal = controller.getTotalUser(dbContext);

        assertEquals(totalUsers, actualTotal);
        verify(dbContext).fetchOne(any(), eq("SELECT COUNT(*) FROM UserAccount"));
    }
}
