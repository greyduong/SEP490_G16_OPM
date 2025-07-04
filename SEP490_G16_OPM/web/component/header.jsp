<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style type="text/tailwindcss">
    .header_link {
        @apply !no-underline !font-medium !text-slate-600 hover:!text-lime-600;
    }
</style>
<div class="p-2 border border-slate-300 flex items-center gap-6 mb-5 sticky top-0 bg-white z-100">
    <c:if test="${not empty sessionScope.user}">
        <div id="toggleSidebar" class="border border-slate-300 text-slate-600 rounded-sm px-2 py-1 hover:!border-slate-400 hover:text-slate-700 hover:cursor-pointer"><span class="mdi mdi-menu"></span></div>
        </c:if>
        <c:if test="${sessionScope.user == null || sessionScope.user.roleID != 4}">
        <a href="${pageContext.request.contextPath}/home"><img class="w-10 h-10" src="img/logo.svg"></a>
        <div class="flex items-center gap-6">

            <!-- Guest, Dealer, Admin... -->
            <a href="${pageContext.request.contextPath}/home" class="header_link">
                <span class="mdi mdi-home"></span>
                Trang chủ
            </a>
            <c:if test="${sessionScope.user == null || sessionScope.user.roleID == 5}">
                <a href="${pageContext.request.contextPath}/farms" class="header_link">
                    <span class="mdi mdi-barn"></span>
                    Trang trại
                </a>
            </c:if>
        </div>
    </c:if>
    <c:if test="${sessionScope.user != null && sessionScope.user.roleID == 4}">
        <a href="${pageContext.request.contextPath}/seller"><img class="w-10 h-10" src="img/logo.svg"></a>
        <div class="flex items-center gap-6">
            <!-- Seller -->
            <a href="${pageContext.request.contextPath}/seller" class="header_link">
                <span class="mdi mdi-home"></span>
                Trang chủ
            </a>
            <a href="${pageContext.request.contextPath}/my-farms" class="header_link">
                <span class="mdi mdi-barn"></span>
                Trang trại của tôi
            </a>
        </div>
    </c:if>
    <div class="ml-auto flex items-center gap-2">
        <c:if test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login"
               class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-login"></span>
                Đăng nhập
            </a>
            <a href="${pageContext.request.contextPath}/register"
               class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-account-plus"></span>
                Đăng ký
            </a>
        </c:if>
        <c:if test="${not empty sessionScope.user and (sessionScope.user.roleID == 4 or sessionScope.user.roleID == 5)}">
            <a href="${pageContext.request.contextPath}/wallet" class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-cash"></span>
                <fmt:formatNumber currencyCode="VND" value="${sessionScope.user.wallet}" />đ
            </a>
        </c:if>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/profile"
               class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-account"></span>
                ${sessionScope.user.fullName}
            </a>
            <button id="logout"
                    class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-logout"></span>
                Đăng xuất
            </button>
        </c:if>
    </div>
</div>
<c:if test="${not empty sessionScope.user}">
    <div id="sidebar" class="hidden z-99 w-60 duration-500 transition-all fixed top-0 pt-13 left-0 border-r border-slate-300 h-full bg-white overflow-y-auto [&::-webkit-scrollbar]:w-2 [&::-webkit-scrollbar-track]:bg-gray-100 [&::-webkit-scrollbar-thumb]:bg-gray-300">
        <div class="mt-2 flex flex-col gap-2 p-2 *:overflow-x-hidden *:whitespace-nowrap *:transition-all *:font-medium *:text-md *:hover:!bg-slate-100 *:p-2 *:rounded-lg *:!text-slate-600 *:hover:!text-slate-700 *:!no-underline *:flex *:gap-2">
            <!-- SELLER -->
            <c:if test="${sessionScope.user.roleID == 4}">
                <a href="${pageContext.request.contextPath}/my-farms">
                    <span class="mdi mdi-barn"></span>
                    Trang trại
                </a>
                <a href="${pageContext.request.contextPath}/my-offers">
                    <span class="mdi mdi-offer"></span>
                    Chào bán
                </a>
                <a href="${pageContext.request.contextPath}/customer-orders">
                    <span class="mdi mdi-invoice-list"></span>
                    Đơn hàng của khách
                </a>
                <a href="${pageContext.request.contextPath}/orders-request">
                    <span class="mdi mdi-invoice-list"></span>
                    Đơn hàng chờ duyệt
                </a>
                <a href="${pageContext.request.contextPath}/application">
                    <span class="mdi mdi-invoice-list-outline"></span>
                    Đơn đề nghị
                </a>

            </c:if>
            <!-- DEALER -->
            <c:if test="${sessionScope.user.roleID == 5}">
                <a href="${pageContext.request.contextPath}/cart">
                    <span class="mdi mdi-cart"></span>
                    Giỏ hàng
                </a>
                <a href="${pageContext.request.contextPath}/myorders"
                   >
                    <span class="mdi mdi-invoice-list"></span>
                    Đơn hàng của tôi
                </a>
                <a href="${pageContext.request.contextPath}/application">
                    <span class="mdi mdi-invoice-list-outline"></span>
                    Đơn đề nghị
                </a>
                <a href="${pageContext.request.contextPath}/CreateApplication">
                    <span class="mdi mdi-invoice-list-outline"></span>
                    Tạo đơn
                </a>
                <a href="${pageContext.request.contextPath}/dealer-dashboard">
                    <span class="mdi mdi-chart-bar"></span>
                    Thống kê
                </a>
            </c:if>
            <c:if test="${sessionScope.user.roleID == 1}">
                <a href="${pageContext.request.contextPath}/manage-user">
                    <span class="mdi mdi-account"></span>
                    Quản lý người dùng
                </a>
            </c:if>
            <!-- MANAGER -->
            <c:if test="${sessionScope.user.roleID == 2}">
                <a href="${pageContext.request.contextPath}/manage-farms">
                    <span class="mdi mdi-barn"></span>
                    Tất cả trang trại
                </a>
                <a href="${pageContext.request.contextPath}/pending-farms">
                    <span class="mdi mdi-barn"></span>
                    Trang trại chờ duyệt
                </a>
                <a href="${pageContext.request.contextPath}/manage-offers">
                    <span class="mdi mdi-offer"></span>
                    Tất cả chào bán
                </a>
                <a href="${pageContext.request.contextPath}/manage-orders">
                    <span class="mdi mdi-invoice-list"></span>
                    Tất cả đơn hàng
                </a>
                <a href="${pageContext.request.contextPath}/manage-application">
                    <span class="mdi mdi-invoice-list-outline"></span>
                    Quản lý đơn
                </a>
                <a href="${pageContext.request.contextPath}/category">
                    <span class="mdi mdi-invoice-list-outline"></span>
                    Quản lý loại lợn
                </a>
                <a href="${pageContext.request.contextPath}/server-log">
                    <span class="mdi mdi-math-log"></span>
                    Nhật ký hệ thống
                </a>
            </c:if>
            <!-- Staff -->
            <c:if test="${sessionScope.user.roleID == 3}">                
                <a href="${pageContext.request.contextPath}/pending-farms">
                    <span class="mdi mdi-barn"></span>
                    Trang trại chờ duyệt
                </a>              
                <a href="${pageContext.request.contextPath}/manage-application">
                    <span class="mdi mdi-invoice-list-outline"></span>
                    Quản lý đơn
                </a> 
                <a href="${pageContext.request.contextPath}/category">
                    <span class="mdi mdi-barn"></span>
                    Quản lý loại lợn
                </a> 
            </c:if>
        </div>
    </div>
    <script>
        function getSidebarState() {
            if (localStorage.getItem("sidebar") === null)
                return true;
            return JSON.parse(localStorage.getItem("sidebar"));
        }
        function setSidebarState(state) {
            if (state)
                $("#sidebar").removeClass("hidden");
            localStorage.setItem("sidebar", JSON.stringify(state));
            console.log(state);
            if (state) {
                $("#sidebar").removeClass("-translate-x-full");
                $("body > *:not(#header):not(#sidebar)").animate({
                    marginLeft: $("#sidebar").width() + "px"
                }, 300);
            } else {
                $("#sidebar").removeClass("-translate-x-full");
                $("#sidebar").addClass("-translate-x-full");
                $("body > *:not(#header):not(#sidebar)").animate({
                    marginLeft: "0px"
                }, 300);
            }
        }
        function toggleSidebar() {
            const state = getSidebarState();
            setSidebarState(!state);
        }
        $("#toggleSidebar").on("click", function () {
            toggleSidebar();
        });
        $(window).on("load", function () {
            const state = getSidebarState();
            if (state) {
                $("#sidebar").removeClass("hidden");
                $("body > *:not(#header):not(#sidebar)").css({
                    marginLeft: $("#sidebar").width() + "px"
                });
            }
        });
    </script>
</c:if>
<script>
    $("#logout").on("click", function (e) {
        e.preventDefault();
        if (!confirm("Xác nhận đăng xuất")) {
            return;
        }
        window.location.href = "${pageContext.request.contextPath}/logout";
    });
</script>
<c:if test="${param.error == '403'}">
    <script>
        alert("Bạn không có quyền làm điều này!");
    </script>
</c:if>