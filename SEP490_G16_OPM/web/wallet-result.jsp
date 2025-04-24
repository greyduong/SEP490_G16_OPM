<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
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
                <c:if test="${success != null}"><p class="text-success">Success topup ${vnp_Amount} VND</p></c:if>
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />     
    </body>
</html>
