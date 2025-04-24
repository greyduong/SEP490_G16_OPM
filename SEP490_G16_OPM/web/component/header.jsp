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
        <a href="${pageContext.request.contextPath}/home" class="!font-bold !text-slate-600 hover:!text-green-600">
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
            <a href="${pageContext.request.contextPath}/login" class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-login"></span>
                <fmt:message key="login" bundle="${headerBundle}" />
            </a>
            <a href="${pageContext.request.contextPath}/register" class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-account-plus"></span>
                <fmt:message key="register" bundle="${headerBundle}" />
            </a>
        </c:if>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/profile" class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-account"></span>
                ${user.fullName}
            </a>
            <button id="logout" class="!no-underline border border-slate-300 !text-slate-600 hover:border-slate-400 hover:!bg-slate-50 px-2 py-1 rounded-lg">
                <span class="mdi mdi-logout"></span>
                <fmt:message key="logout" bundle="${headerBundle}" />
            </button>
        </c:if>
    </div>
</div>
<script>
    $("#logout").on("click", function (e) {
        e.preventDefault();
        if (!confirm("<fmt:message key="logout.confirm" bundle="${headerBundle}" />")) {
            return;
        }
        window.location.href = "${pageContext.request.contextPath}/logout";
    });
</script>
