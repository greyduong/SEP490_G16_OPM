<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manager User | Online Pig Market</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@mdi/font@7.4.47/css/materialdesignicons.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <main class="w-200 mx-auto mb-5">
            <h3 class="mb-3">Edit User</h3>
            <div id="error" class="text-red-600 font-bold text-2xl">${error}</div>
            <form method="POST" action="?action=edit">
                <div class="d-none">
                    <input name="id" value="${id}" type="text" class="form-control">
                </div>
                <div class="form-group">
                    <label for="fullname" class="font-bold">Full Name</label>
                    <input name="fullname" value="${fullname}" type="text" class="form-control" id="fullname" placeholder="Enter full name">
                </div>
                <div class="form-group">
                    <label for="username" class="font-bold">Username</label>
                    <input name="username" value="${username}" type="text" class="form-control" id="username" placeholder="Enter username">
                </div>
                <div class="form-group">
                    <label for="email" class="font-bold">Email</label>
                    <input name="email" value="${email}" type="email" class="form-control" id="email" placeholder="Enter email">
                </div>
                <div class="form-group">
                    <label for="address" class="font-bold">Address</label>
                    <input name="address" value="${address}" type="text" class="form-control" id="address" placeholder="Enter address">
                </div>
                <div class="form-group">
                    <label for="phone" class="font-bold">Phone</label>
                    <input name="phone" value="${phone}" type="text" class="form-control" id="phone" placeholder="Enter phone">
                </div>
                <div class="form-group">
                    <label for="password" class="font-bold">Password</label>
                    <input name="password" value="${password}" type="password" class="form-control" id="password" placeholder="Enter password">
                </div>
                <div class="form-group">
                    <label for="status" class="font-bold">Status</label>
                    <select name="status" class="form-control" id="status">
                        <option value="Active" ${status == 'Active' ? 'selected' : ''}>Active</option>
                        <option value="Inactive" ${status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="role" class="font-bold">Role</label>
                    <select name="role" class="form-control" id="role">
                        <option value="5" ${role == 5 ? 'selected' : ''}>Pig Dealer</option>
                        <option value="4" ${role == 4 ? 'selected' : ''}>Pig Seller</option>
                        <option value="3" ${role == 3 ? 'selected' : ''}>Staff</option>
                        <option value="2" ${role == 2 ? 'selected' : ''}>Manager</option>
                        <option value="1" ${role == 1 ? 'selected' : ''}>Admin</option>
                    </select>
                </div>
                <div>
                    <button type="submit" class="btn btn-primary">Update</button>
                    <a href="?" class="btn btn-secondary" id="cancel">Cancel</a>
                </div>
            </form>
        </main>
        <div id="success" class="d-none">${success}</div>
        
        <script>
            if ($("#success").text().length > 0) {
                alert($("#success").text());
                window.location.href = "?";
            }
        </script>
        <jsp:include page="component/footer.jsp" />
    </body>
</html>