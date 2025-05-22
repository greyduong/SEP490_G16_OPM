package controller;

import dao.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ChangePasswordControllerTest {

    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private UserDAO userDAO;
    @Spy
    private ChangePasswordController controller;

    private int sessionUserId = 5;
    private String sessionUserUsername = "exampleusername";
    private String sessionUserPassword = "currpasswd";
    private String currentPassword = "currpasswd";
    private String newPassword = "Passwd1@!";
    private String confirmNewPassword = "Passwd1@!";

    private User sessionUser;

    @Before
    public void dependencies() {
        doReturn(userDAO).when(controller).getUserDAO();
        when(request.getSession(false)).thenReturn(session);
    }

    public void setup() {
        sessionUser = new User();
        sessionUser.setUserID(sessionUserId);
        sessionUser.setUsername(sessionUserUsername);
        sessionUser.setPassword(sessionUserPassword);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(userDAO.updatePassword(sessionUserUsername, newPassword)).thenReturn(true);
        when(request.getParameter("currentPassword")).thenReturn(currentPassword);
        when(request.getParameter("newPassword")).thenReturn(newPassword);
        when(request.getParameter("confirmNewPassword")).thenReturn(confirmNewPassword);
    }

    /**
     * Test Case 1: Success
     *
     * <br>sessionUserId = 5
     * <br>sessionUserUsername = "exampleusername"
     * <br>sessionUserPassword = "currpasswd"
     * <br>currentPassword = "currpasswd"
     * <br>newPassword = "Passwd1@!"
     * <br>confirmNewPassword = "Passwd1@!"
     *
     * @throws Exception
     */
    @Test
    public void testSuccess() throws Exception {
        setup();

        controller.doPost(request, response);

        verify(session).setAttribute("success", "Đổi mật khẩu thành công.");
    }

    /**
     * Test Case 2: Empty Field
     *
     * <br>sessionUserId = 5
     * <br>sessionUserUsername = "exampleusername"
     * <br>sessionUserPassword = "currpasswd"
     * <br>currentPassword = "" // empty field
     * <br>newPassword = "Passwd1@!"
     * <br>confirmNewPassword = "Passwd1@!"
     *
     * @throws Exception
     */
    @Test
    public void testEmptyFields() throws Exception {
        currentPassword = "";

        setup();

        controller.doPost(request, response);

        verify(session).setAttribute("error", "Vui lòng nhập đầy đủ các trường.");
    }

    /**
     * Test Case 3: Wrong Current Password
     *
     * <br>sessionUserId = 5
     * <br>sessionUserUsername = "exampleusername"
     * <br>sessionUserPassword = "currpasswd"
     * <br>currentPassword = "notcurrentpassword" // wrong current password
     * <br>newPassword = "Passwd1@!"
     * <br>confirmNewPassword = "Passwd1@!"
     *
     * @throws Exception
     */
    @Test
    public void testWrongCurrentPassword() throws Exception {
        currentPassword = "notcurrentpassword";

        setup();

        controller.doPost(request, response);

        verify(session).setAttribute("error", "Mật khẩu hiện tại không đúng.");
    }

    /**
     * Test Case 4: Confirm Password Not Match
     *
     * <br>sessionUserId = 5
     * <br>sessionUserUsername = "exampleusername"
     * <br>sessionUserPassword = "currpasswd"
     * <br>currentPassword = "currpasswd"
     * <br>newPassword = "Passwd1@!"
     * <br>confirmNewPassword = "Passwd1@!@@@"
     *
     * @throws Exception
     */
    @Test
    public void testConfirmPasswordNotMatch() throws Exception {
        confirmNewPassword = "Passwd1@!@@@";

        setup();

        controller.doPost(request, response);

        verify(session).setAttribute("error", "Mật khẩu mới không khớp.");
    }

    /**
     * Test Case 4: Invalid Password
     *
     * <br>sessionUserId = 5
     * <br>sessionUserUsername = "exampleusername"
     * <br>sessionUserPassword = "currpasswd"
     * <br>currentPassword = "notcurrentpassword" // wrong current password
     * <br>newPassword = "invalid"
     * <br>confirmNewPassword = "invalid"
     *
     * @throws Exception
     */
    @Test
    public void testInvalidPassword() throws Exception {
        newPassword = "invalid";
        confirmNewPassword = "invalid";

        setup();

        controller.doPost(request, response);

        verify(session).setAttribute(eq("error"), contains("Mật khẩu mới phải"));
    }
}
