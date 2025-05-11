<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manager User | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <main class="px-5 w-210">
            <div class="font-bold text-2xl mb-3">Thêm người dùng</div>
            <div id="error" class="text-red-600 font-bold text-2xl">${error}</div>
            <form method="POST" action="?action=add">
                <div class="form-group">
                    <label for="fullname" class="font-bold">Tên đầy đủ</label>
                    <input name="fullname" value="${fullname}" type="text" class="form-control" id="fullname" placeholder="Điền tên đầy đủ">
                </div>
                <div class="form-group">
                    <label for="username" class="font-bold">Tên đăng nhập</label>
                    <input name="username" value="${username}" type="text" class="form-control" id="username" placeholder="Điền tên đăng nhập">
                </div>
                <div class="form-group">
                    <label for="email" class="font-bold">Email</label>
                    <input name="email" value="${email}" type="email" class="form-control" id="email" placeholder="Điền email">
                </div>
                <div class="form-group">
                    <label for="password" class="font-bold">Mật khẩu</label>
                    <input name="password" value="${password}" type="password" class="form-control" id="password" placeholder="Điền mật khẩu">
                </div>
                <div class="form-group">
                    <label for="role" class="font-bold">Loại tài khoản</label>
                    <select name="role" class="form-control" id="role">
                        <option value="5" ${role == 5 ? 'selected' : ''}>Pig Dealer</option>
                        <option value="4" ${role == 4 ? 'selected' : ''}>Pig Seller</option>
                        <option value="3" ${role == 3 ? 'selected' : ''}>Staff</option>
                        <option value="2" ${role == 2 ? 'selected' : ''}>Manager</option>
                        <option value="1" ${role == 1 ? 'selected' : ''}>Admin</option>
                    </select>
                </div>
                <div class="mt-1">
                    <button type="submit" class="btn_ bg-lime-600 hover:bg-lime-700 text-white"><span class="mdi mdi-plus"></span>Tạo</button>
                    <a href="?" class="btn_ btn-secondary" id="cancel">Hủy</a>
                </div>
            </form>
        </main>
        <div id="success" class="d-none">${success}</div>
        <style type="text/tailwindcss">
            .btn_ {
                @apply inline-flex items-center gap-1 justify-center transition-all !no-underline px-2 py-1 rounded-sm hover:cursor-pointer;
            }
        </style>
        <script>
            if ($("#success").text().length > 0) {
                alert($("#success").text());
                window.location.href = "?";
            }
        </script>
        <jsp:include page="component/footer.jsp" />
    </body>
</html>