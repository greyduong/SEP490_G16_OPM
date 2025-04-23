<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@mdi/font@7.4.47/css/materialdesignicons.min.css"/>
<div class="p-2 border-b border-slate-300 flex items-center gap-6 mb-5 sticky top-0 bg-white z-100">
    <a href="${pageContext.request.contextPath}/home"><img class="w-10 h-10" src="img/logo.svg"></a>
    <div class="flex items-center gap-6">
        <a href="${pageContext.request.contextPath}/home" class="!font-bold !text-slate-600 hover:!text-green-600">
            <span class="mdi mdi-home"></span>
            Trang chủ
        </a>
        <a href="${pageContext.request.contextPath}/my-farms" class="!font-bold !text-slate-600 hover:!text-green-600">
            <span class="mdi mdi-barn"></span>
            Trang trại
        </a>
        <a href="${pageContext.request.contextPath}/offers" class="!font-bold !text-slate-600 hover:!text-green-600">
            <span class="mdi mdi-offer"></span>
            Chào bán
        </a>
        <a href="${pageContext.request.contextPath}/CustomerOrderPageController" class="!font-bold !text-slate-600 hover:!text-green-600">
            <span class="mdi mdi-invoice-list"></span>
            Đơn hàng của khách
        </a>
        <a href="${pageContext.request.contextPath}/OrdersRequestController" class="!font-bold !text-slate-600 hover:!text-green-600">
            <span class="mdi mdi-invoice-list"></span>
            Đơn hàng chờ duyệt
        </a>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/cart" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-cart"></span>
                Giỏ hàng
            </a>
            <a href="${pageContext.request.contextPath}/orders" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-invoice-list"></span>
                Đơn hàng của tôi
            </a>
            <a href="${pageContext.request.contextPath}/application" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-invoice-list-outline"></span>
                Your Application
            </a>
        </c:if>
    </div>
    <div class="ml-auto flex items-center gap-2">
        <c:if test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login" class="border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-login"></span>
                Login
            </a>
            <a href="${pageContext.request.contextPath}/register" class="border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-account-plus"></span>
                Register
            </a>
        </c:if>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/profile" class="border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-account"></span>
                ${user.fullName}
            </a>
            <button id="logout" class="border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-logout"></span>
                Logout
            </button>
        </c:if>
    </div>
</div>
<script>
    document.getElementById("logout").addEventListener("click", function (e) {
        e.preventDefault();
        if (!confirm("Do you want to logout?")) {
            return;
        }
        window.location.href = "${pageContext.request.contextPath}/logout";
    });
</script>
