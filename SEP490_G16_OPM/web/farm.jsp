<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zxx">
    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Ogani Template">
        <meta name="keywords" content="Ogani, unica, creative, html">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Farm Details | Online Pig Market</title>
        <jsp:include page="component/library.jsp"></jsp:include>
        </head>
        <body>
        <jsp:include page="component/header.jsp"></jsp:include>
            <section class="container">
                <div>
                    <div>ID = ${farm.getFarmID()}</div>
                    <div>Name = ${farm.getFarmName()}</div>
                    <div>Location = ${farm.getLocation()}</div>
                    <div>Description = ${farm.getDescription()}</div>
                </div>
            </section>
        <jsp:include page="component/footer.jsp"></jsp:include>
    </body>
</html>