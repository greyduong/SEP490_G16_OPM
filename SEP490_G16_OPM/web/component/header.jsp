<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="header"
     class="p-2 border-b border-slate-300 mb-5 flex items-center gap-6 sticky top-0 bg-white z-100 right-0 left-0">
    <div id="toggleSidebar" class="ring-1 ring-slate-300 hover:cursor-pointer hover:bg-slate-100 rounded-sm px-2 py-1">
        <span class="mdi mdi-menu"></span></div>
    <a href="${pageContext.request.contextPath}/home"><img class="w-10 h-10" src="img/logo.svg"></a>
    <div class="flex items-center gap-6">
        <a href="${pageContext.request.contextPath}/home"
           class="!no-underline !font-medium !text-slate-600 hover:!text-green-600">
            <span class="mdi mdi-home"></span>
            Trang chủ
        </a>
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
            <a href="${pageContext.request.contextPath}/profile"
               class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-account"></span>
                    ${user.fullName}
            </a>
            <button id="logout"
                    class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-logout"></span>
                Logout
            </button>
        </c:if>
    </div>
</div>
<div id="sidebar"
     class="z-99 w-55 duration-500 transition-all fixed top-0 pt-13 left-0 border-r border-slate-300 h-full bg-white overflow-y-auto">
    <div class="mt-2 flex flex-col gap-2 p-2 *:overflow-x-hidden *:whitespace-nowrap *:transition-all *:font-medium *:text-md *:hover:!bg-slate-100 *:p-2 *:rounded-lg *:!text-slate-600 *:hover:!text-slate-700 *:!no-underline *:flex *:gap-2">
        <a href="${pageContext.request.contextPath}/my-farms">
            <span class="mdi mdi-barn"></span>
            Trang trại
        </a>
        <a href="${pageContext.request.contextPath}/my-offers">
            <span class="mdi mdi-offer"></span>
            Chào bán
        </a>
        <a href="${pageContext.request.contextPath}/CustomerOrderPageController">
            <span class="mdi mdi-invoice-list"></span>
            Đơn hàng của khách
        </a>
        <a href="${pageContext.request.contextPath}/OrdersRequestController">
            <span class="mdi mdi-invoice-list"></span>
            Đơn hàng chờ duyệt
        </a>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/cart">
                <span class="mdi mdi-cart"></span>
                Giỏ hàng
            </a>
            <a href="${pageContext.request.contextPath}/orders"
            >
                <span class="mdi mdi-invoice-list"></span>
                Đơn hàng của tôi
            </a>
            <a href="${pageContext.request.contextPath}/application">
                <span class="mdi mdi-invoice-list-outline"></span>
                Your Application
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
            }, 500);
        } else {
            $("body > *:not(#header):not(#sidebar)").animate({
                marginLeft: $("#sidebar").width() + "px"
            }, 500);
        }
    });
    $("#logout").on("click", function (e) {
        e.preventDefault();
        if (!confirm("Do you want to logout?")) {
            return;
        }
        window.location.href = "${pageContext.request.contextPath}/logout";
    });
    $(window).on("load", function () {
        $("body > *:not(#header):not(#sidebar)").css("margin-left", $("#sidebar").width() + "px")
    })
</script>
