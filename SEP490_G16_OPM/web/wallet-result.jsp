<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="author" content="">
        <title>Nạp ví | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <main class="text-center mb-5">
            <div class="font-bold text-2xl mb-3">Kết quả nạp</div>
            <c:if test="${not empty error}">
                <div class="text-red-600 font-bold mb-3">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="text-lime-600 font-bold mb-3">Thành công nạp ${amount} VND vào ví!</div>
            </c:if>
            <c:if test="${not empty fail}">
                <div class="text-red-600 font-bold mb-3">Bạn đã hủy nạp</div>
            </c:if>
            <a href="${pageContext.request.contextPath}/wallet-topup">Tiến hành giao dịch mới</a>
            <div>
                <a class="!no-underline text-xs !text-slate-500" href="${pageContext.request.contextPath}/wallet"><span class="mdi mdi-chevron-left"></span>Quay về lịch sử giao dịch</a>
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />     
    </body>
</html>
