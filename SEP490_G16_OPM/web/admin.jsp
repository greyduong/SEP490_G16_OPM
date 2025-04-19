<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRoleID() != 1) {
        response.sendRedirect("login-register.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Admin - Manage Users</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <body class="bg-light">

        <div class="container mt-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>All User Accounts</h2>
                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#createUserModal">+ Create New Account</button>
            </div>

            <c:if test="${not empty msg}">
                <div class="alert ${msgType == 'success' ? 'alert-success' : 'alert-danger'}">${msg}</div>
            </c:if>

            <table class="table table-bordered table-hover bg-white shadow-sm">
                <thead class="table-light">
                    <tr>
                        <th>UserID</th>
                        <th>Full Name</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Role</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${userList}">
                        <tr>
                            <td>${u.userID}</td>
                            <td>${u.fullName}</td>
                            <td>${u.username}</td>
                            <td>${u.email}</td>
                            <td>${u.phone}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.roleID == 1}">Admin</c:when>
                                    <c:when test="${u.roleID == 2}">Manager</c:when>
                                    <c:when test="${u.roleID == 3}">Staff</c:when>
                                    <c:when test="${u.roleID == 4}">Seller</c:when>
                                    <c:when test="${u.roleID == 5}">Dealer</c:when>
                                </c:choose>
                            </td>
                            <td>${u.status}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Modal for Creating User -->
        <div class="modal fade" id="createUserModal" tabindex="-1" aria-labelledby="createUserModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <form class="modal-content" action="create-user" method="post">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createUserModalLabel">Create New User</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <input type="text" name="fullname" placeholder="Full Name" class="form-control mb-2" required>
                        <input type="text" name="username" placeholder="Username" class="form-control mb-2" required>
                        <input type="email" name="email" placeholder="Email" class="form-control mb-2" required>
                        <input type="text" name="phone" placeholder="Phone Number" class="form-control mb-2" required>
                        <input type="text" name="address" placeholder="Address" class="form-control mb-2" required>
                        <input type="password" name="password" placeholder="Password" class="form-control mb-2" required>
                        <select name="roleID" class="form-control mb-2" required>
                            <option value="">-- Select Role --</option>
                            <option value="2">Manager</option>
                            <option value="3">Staff</option>
                            <option value="4">Seller</option>
                            <option value="5">Dealer</option>
                        </select>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary w-100">Create User</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Bootstrap Bundle JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
