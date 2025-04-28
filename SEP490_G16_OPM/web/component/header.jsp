<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-<!-- Chọn tiếng việt nếu không có thuộc tính locale trong Session -->
<c:choose>
    <c:when test="${not empty param.lang}">
        <fmt:setLocale value="${param.lang}" />
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="vi_VN" />
    </c:otherwise>
</c:choose>
<!-<!-- Tạo biến headerBundle -->
<fmt:setBundle basename="messages.header" var="headerBundle" />
<div class="p-2 border-b border-slate-300 flex items-center gap-6 mb-5 sticky top-0 bg-white z-100">
    <a href="${pageContext.request.contextPath}/home"><img class="w-10 h-10" src="img/logo.svg"></a>
    <div class="flex items-center gap-6">
        <a href="${pageContext.request.contextPath}/home"
           class="!no-underline !font-medium !text-slate-600 hover:!text-lime-600">
            <span class="mdi mdi-home"></span>
            <fmt:message key="homepage" bundle="${headerBundle}" />
        </a>
        <a href="${pageContext.request.contextPath}/my-farms" class="!font-bold !text-slate-600 hover:!text-green-600">
            <span class="mdi mdi-barn"></span>
            <fmt:message key="farm" bundle="${headerBundle}" />
        </a>
        <a href="${pageContext.request.contextPath}/my-offers" class="!font-bold !text-slate-600 hover:!text-green-600">
            <span class="mdi mdi-offer"></span>
            <fmt:message key="offer" bundle="${headerBundle}" />
        </a>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/CustomerOrderPageController" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-invoice-list"></span>
                <fmt:message key="order.customer" bundle="${headerBundle}" />
            </a>
            <a href="${pageContext.request.contextPath}/OrdersRequestController" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-invoice-list"></span>
                <fmt:message key="order.needconfirm" bundle="${headerBundle}" />
            </a>
            <a href="${pageContext.request.contextPath}/cart" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-cart"></span>
                <fmt:message key="cart" bundle="${headerBundle}" />
            </a>
            <a href="${pageContext.request.contextPath}/orders" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-invoice-list"></span>
                <fmt:message key="order.self" bundle="${headerBundle}" />
            </a>
            <a href="${pageContext.request.contextPath}/application" class="!font-bold !text-slate-600 hover:!text-green-600">
                <span class="mdi mdi-invoice-list-outline"></span>
                <fmt:message key="application.self" bundle="${headerBundle}" />
            </a>
        </c:if>
    </div>
    <div class="ml-auto flex items-center gap-2">
        <c:if test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login"
               class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-login"></span>
                <fmt:message key="login" bundle="${headerBundle}" />
            </a>
            <a href="${pageContext.request.contextPath}/register"
               class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-account-plus"></span>
                <fmt:message key="register" bundle="${headerBundle}" />
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
                <fmt:message key="logout" bundle="${headerBundle}" />
            </button>
        </c:if>
    </div>
</div>
<div id="sidebar" class="z-99 w-60 duration-500 transition-all fixed top-0 pt-13 left-0 border-r border-slate-300 h-full bg-white overflow-y-scroll [&::-webkit-scrollbar]:w-2 [&::-webkit-scrollbar-track]:bg-gray-100 [&::-webkit-scrollbar-thumb]:bg-gray-300">
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
            }, 300);
        } else {
            $("body > *:not(#header):not(#sidebar):not(.modal)").animate({
                marginLeft: $("#sidebar").width() + "px"
            }, 300);
        }
    });
    $("#logout").on("click", function (e) {
        e.preventDefault();
        if (!confirm("<fmt:message key="logout.confirm" bundle="${headerBundle}" />")) {
            return;
        }
        window.location.href = "${pageContext.request.contextPath}/logout";
    });
    $(window).on("load", function () {
        $("body > *:not(#header):not(#sidebar):not(.modal)").css("margin-left", $("#sidebar").width() + "px")
    })
</script>
