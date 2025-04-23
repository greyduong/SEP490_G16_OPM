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
        <main class="w-200 mx-auto mb-5">
            <div class="text-3xl mb-3"><span class="mdi mdi-account-star me-2"></span>Manage User</div>
            <div class="mb-3 d-flex">
                <form action="" class="inline-flex items-center rounded-sm gap-2 border border-slate-300 px-2 has-[:focus]:border-slate-400">
                    <span class="mdi mdi-magnify"></span>
                    <input value="${search}" name="search" type="text" placeholder="Search user" class="outline-none py-1 focus:!outline-none">
                </form>
                <a href="?action=add" class="ml-auto !bg-blue-600 !text-white px-2 py-1 rounded-sm hover:!bg-blue-700 hover:cursor-pointer">
                    <span class="mdi mdi-plus-box"></span>
                    Add user
                </a>
            </div>
            <div class="mb-3 overflow-x-auto">
                <table class="border border-slate-300 overflow-x-auto w-full">
                    <thead>
                        <tr class="text-sm bg-slate-50 text-slate-500 text-left *:py-2 *:px-3 border-b border-slate-300">
                            <th>ID</th>
                            <th>Email</th>
                            <th>Full name</th>
                            <th>Username</th>
                            <th>Status</th>
                            <th>Role</th>
                            <th>Action</th>
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
                                    <div class="flex gap-2">
                                        <a href="?action=edit&id=${usr.getUserID()}" class="text-sm px-2 py-1 rounded-sm !text-slate-500 !bg-slate-100 hover:cursor-pointer hover:!bg-slate-200">Edit</a>
                                        <c:choose>
                                            <c:when test="${usr.getStatus() == 'Active'}">
                                                <form class="delete-user-form" method="POST" action="?action=delete&id=${usr.getUserID()}"><button class="delete-user text-sm px-2 py-1 rounded-sm text-slate-500 bg-slate-100 hover:cursor-pointer hover:bg-slate-200">Delete</button></form>
                                            </c:when>
                                            <c:when test="${usr.getStatus() == 'Inactive'}">
                                                <form class="recover-user-form" method="POST" action="?action=recover&id=${usr.getUserID()}"><button class="recover-user text-sm px-2 py-1 rounded-sm text-slate-500 bg-slate-100 hover:cursor-pointer hover:bg-slate-200">Recover</button></form>
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
                <span class="mr-2">Showing ${offset+1}-${offset + users.size()} of ${total} items</span>
                <a href="?page=${page-1}&search=${search}" class="!text-slate-600 !bg-slate-100 px-2 py-1 hover:!bg-slate-200 rounded-sm hover:cursor-pointer">Previous</a>
                <a href="?page=${page+1}&search=${search}" class="!text-slate-600 !bg-slate-100 px-2 py-1 hover:!bg-slate-200 rounded-sm hover:cursor-pointer">Next</a>
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />
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