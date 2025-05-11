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
        <main class="px-5">
            <div class="text-3xl mb-3"><span class="mdi mdi-account-star me-2"></span>Quản lý người dùng</div>
            <div class="mb-3 d-flex">
                <form action="" class="inline-flex items-center rounded-sm gap-2 border border-slate-300 px-2 has-[:focus]:!border-lime-400">
                    <span class="mdi mdi-magnify"></span>
                    <input value="${search}" name="search" type="text" placeholder="Tìm kiếm" class="outline-none py-1 focus:!outline-none">
                </form>
                <a href="?action=add" class="ml-auto !no-underline transition-all !bg-lime-600 !text-white px-2 py-1 rounded-sm hover:!bg-lime-700 hover:cursor-pointer">
                    <span class="mdi mdi-plus-box"></span>
                    Tạo
                </a>
            </div>
            <div class="mb-3 overflow-x-auto">
                <table class="border border-slate-300 overflow-x-auto w-full">
                    <thead>
                        <tr class="text-sm bg-slate-50 text-slate-500 text-left *:py-2 *:px-3 border-b border-slate-300">
                            <th>ID</th>
                            <th>Email</th>
                            <th>Tên đầy đủ</th>
                            <th>Tên đăng nhập</th>
                            <th>Trạng thái</th>
                            <th>Role</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${users}" var="usr">
                            <tr class="*:py-2 *:px-3 border-b border-slate-300">
                                <td>${usr.getUserID()}</td>
                                <td>${usr.getEmail()}</td>
                                <td>${usr.getFullName()}</td>
                                <td>${usr.getUsername()}</td>
                                <td><span class="border ${usr.getStatus() == 'Active' ? 'text-green-600' : 'text-red-600'} rounded-full px-2 inline-flex items-center justify-center text-sm uppercase">${usr.getStatus()}</span></td>
                                <td>
                                    <span class="whitespace-nowrap">
                                        <c:choose>
                                            <c:when test="${usr.roleID == 1}">Admin</c:when>
                                            <c:when test="${usr.roleID == 2}">Manager</c:when>
                                            <c:when test="${usr.roleID == 3}">Staff</c:when>
                                            <c:when test="${usr.roleID == 4}">Pig Seller</c:when>
                                            <c:when test="${usr.roleID == 5}">Pig Dealer</c:when>
                                        </c:choose>
                                    </span>
                                </td>
                                <td>
                                    <div class="flex items-center gap-2">
                                        <a href="?action=edit&id=${usr.getUserID()}" class="btn_action hover:!bg-blue-700 !bg-blue-600 !text-white"><span class="mdi mdi-pencil"></span>Sửa</a>
                                        <c:choose>
                                            <c:when test="${usr.getStatus() == 'Active'}">
                                                <form class="delete-user-form" method="POST" action="?action=delete&id=${usr.getUserID()}">
                                                    <button class="delete-user btn_action bg-red-600 text-white hover:bg-red-700">
                                                        <span class="mdi mdi-trash-can"></span><span>Xóa</span>
                                                    </button>
                                                </form>
                                            </c:when>
                                            <c:when test="${usr.getStatus() == 'Inactive'}">
                                                <form class="recover-user-form" method="POST" action="?action=recover&id=${usr.getUserID()}">
                                                    <button class="recover-user btn_action border border-slate-300 hover:border-slate-400">
                                                        <span class="mdi mdi-restore"></span>
                                                        <span>Phục hồi</span>
                                                    </button>
                                                </form>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div>
                <span class="mr-2">Hiện từ hàng ${offset+1}-${offset + users.size()} của tổng ${total} hàng</span>
                <a href="?page=${page-1}&search=${search}" class="btn_"><span class="mdi mdi-chevron-left"></span></a>
                <a href="?page=${page+1}&search=${search}" class="btn_"><span class="mdi mdi-chevron-right"></span></a>
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />
        <style type="text/tailwindcss">
            .btn_ {
                @apply inline-flex items-center gap-2 justify-center transition-all !no-underline !text-white !bg-lime-600 px-2 py-1 hover:!bg-lime-700 rounded-sm hover:cursor-pointer;
            }
            .btn_action {
                @apply !no-underline px-2 py-1 text-sm inline-flex gap-1 items-center justify-center rounded-sm transition-all;
            }
        </style>
        <script>
            $(".delete-user-form").on("submit", function (e) {
                e.preventDefault();
                const choice = confirm("Do you want to delete this user?");
                if (!choice)
                    return;
                e.target.submit();
            });
            $(".recover-user-form").on("submit", function (e) {
                e.preventDefault();
                const choice = confirm("Do you want to recover this user?");
                if (!choice)
                    return;
                e.target.submit();
            });
        </script>
    </body>
</html>
