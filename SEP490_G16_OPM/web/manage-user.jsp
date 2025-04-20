<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manager User | Online Pig Market</title>
        <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <main class="w-200 mx-auto mb-5">
            <div class="text-3xl mb-3">Manage User</div>
            <div class="mb-2">
                <button id="new-user" class="bg-blue-600 text-white px-5 py-1 rounded-sm hover:bg-blue-700 hover:cursor-pointer" data-toggle="modal" data-target="#addUserForm"><i class="bi bi-plus"></i>Add user</button>
            </div>
            <div class="mb-3">
                <form action="" class="inline-flex items-center gap-2 border border-slate-300 px-3">
                    <i class="bi bi-search"></i>
                    <input value="${search}" name="search" type="text" placeholder="Search user" class="outline-none px-3 py-1 rounded-sm focus:outline-none focus:!border-slate-600 transition-all">
                </form>
            </div>
            <div class="mb-3 overflow-x-auto">
                <table class="border border-slate-300 overflow-x-auto">
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
                                <td><span class="border border-slate-300 rounded-full px-2 inline-flex items-center justify-center text-sm uppercase">${usr.getStatus()}</span></td>
                                <td>${usr.getRoleID()}</td>
                                <td>
                                    <div class="flex gap-2">
                                        <a href="?action=edit&id=${usr.getUserID()}" class="text-sm px-2 py-1 rounded-sm !text-slate-500 !bg-slate-100 hover:cursor-pointer hover:!bg-slate-200">Edit</a>
                                        <form class="delete-user-form" method="POST" action="?action=delete&id=${usr.getUserID()}"><button class="delete-user text-sm px-2 py-1 rounded-sm text-slate-500 bg-slate-100 hover:cursor-pointer hover:bg-slate-200">Delete</button></form>
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
        <div id="addUserForm" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add new user</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p>Modal body text goes here.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary">Save changes</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="component/footer.jsp" />
        <script>
            $(".delete-user-form").on("submit", function (e) {
                e.preventDefault();
                const choice = confirm("Do you want to delete this user?");
                if (!choice)
                    return;
                e.target.submit();
            });
        </script>
    </body>
</html>