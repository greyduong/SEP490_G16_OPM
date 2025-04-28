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
        <main class="container mb-5">
            <div class="w-25 mx-auto">
                <h4 class="font-weight-bold">Wallet Result</h4>
                <c:if test="${not empty requestScope.error}"><p class="text-success">${requestScope.error}</p></c:if>
                <c:if test="${not empty requestScope.success}"><p class="text-success">Thành công nạp ${requestScope.vnp_Amount} VND vào ví!</p></c:if>
                <c:if test="${not empty requestScope.fail}"><p class="text-success">Giao dịch thất bại</p></c:if>
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />     
    </body>
</html>
