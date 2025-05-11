<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="p-2 border border-slate-300 flex items-center gap-6 mb-5 sticky top-0 bg-white z-100">
    <c:if test="${not empty sessionScope.user}">
        <div id="toggleSidebar" class="border border-slate-300 text-slate-600 rounded-sm px-2 py-1 hover:!border-slate-400 hover:text-slate-700 hover:cursor-pointer"><span class="mdi mdi-menu"></span></div>
        </c:if>
    <a href="${pageContext.request.contextPath}/home"><img class="w-10 h-10" src="img/logo.svg"></a>
    <div class="flex items-center gap-6">
        <a href="${pageContext.request.contextPath}/home"
           class="!no-underline !font-medium !text-slate-600 hover:!text-lime-600">
            <span class="mdi mdi-home"></span>
            Trang chủ
        </a>
        <c:if test="${sessionScope.user != null && sessionScope.user.roleID == 4}">
            <!-- Seller -->
            <a href="${pageContext.request.contextPath}/my-farms" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-barn"></span>
                Trang trại của tôi
            </a>
        </c:if>

        <c:if test="${sessionScope.user == null || sessionScope.user.roleID != 4}">
            <!-- Guest, Dealer, Admin... -->
            <a href="${pageContext.request.contextPath}/farms" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-barn"></span>
                Trang trại
            </a>
        </c:if>

    </div>
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
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/wallet" class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-cash"></span>
                <fmt:formatNumber currencyCode="VND" value="${sessionScope.user.wallet}" />đ
            </a>
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
    <div id="sidebar" class="z-99 w-60 duration-500 transition-all fixed top-0 pt-13 left-0 border-r border-slate-300 h-full bg-white overflow-y-auto [&::-webkit-scrollbar]:w-2 [&::-webkit-scrollbar-track]:bg-gray-100 [&::-webkit-scrollbar-thumb]:bg-gray-300">
        <div class="mt-2 flex flex-col gap-2 p-2 *:overflow-x-hidden *:whitespace-nowrap *:transition-all *:font-medium *:text-md *:hover:!bg-slate-100 *:p-2 *:rounded-lg *:!text-slate-600 *:hover:!text-slate-700 *:!no-underline *:flex *:gap-2">
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
                    Hóa đơn của khách
                </a>
                <a href="${pageContext.request.contextPath}/orders-request">
                    <span class="mdi mdi-invoice-list"></span>
                    Hóa đơn chờ duyệt
                </a>
                <a href="${pageContext.request.contextPath}/application">
                    <span class="mdi mdi-invoice-list-outline"></span>
                    Đơn đề nghị
                </a>
            </c:if>
            <c:if test="${sessionScope.user.roleID == 5}">
                <a href="${pageContext.request.contextPath}/cart">
                    <span class="mdi mdi-cart"></span>
                    Giỏ hàng
                </a>
                <a href="${pageContext.request.contextPath}/myorders"
                   >
                    <span class="mdi mdi-invoice-list"></span>
                    Hóa của tôi
                </a>
                <a href="${pageContext.request.contextPath}/application">
                    <span class="mdi mdi-invoice-list-outline"></span>
                    Đơn đề nghị
                </a>
            </c:if>
            <c:if test="${sessionScope.user.roleID == 2}">
                <a href="${pageContext.request.contextPath}/pending-farms">
                    <span class="mdi mdi-barn"></span>
                    Trang trại chờ duyệt
                </a>              
            </c:if>
        </div>
    </div>
    <script>
        $("#toggleSidebar").on("click", function () {
            $("#sidebar").toggleClass("-translate-x-full");
            if ($("#sidebar").hasClass("-translate-x-full")) {
                $("body > *:not(#header):not(#sidebar)").animate({
                    marginLeft: "0px"
                }, 300);
            } else {
                $("body > *:not(#header):not(#sidebar):not(.modal)").animate({
                    marginLeft: $("#sidebar").width() + "px"
                }, 300);
            }
        });
        $(window).on("load", function () {
            $("body > *:not(#header):not(#sidebar):not(.modal)").css("margin-left", $("#sidebar").width() + "px")
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
