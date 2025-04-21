<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">
        <title>Wallet Topup | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <main class="container mb-5">
            <div class="w-25 mx-auto">
                <h4 class="font-weight-bold">Wallet Result</h4>
                <p class="text-danger">${error}</p>
                <c:choose>
                    <c:when test="${success}">
                        <p class="text-primary">Success topup ${vnp_Amount} to wallet</p>
                    </c:when>
                        <c:when test="${!success}">
                        <p class="text-primary">Transaction failed</p>
                    </c:when>
                </c:choose>
                
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />     
    </body>
</html>
