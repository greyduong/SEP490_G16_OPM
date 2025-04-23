<jsp:useBean id="farm" scope="request" type="model.Farm"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zxx">
<head>
    <meta charset="UTF-8">
    <meta name="description" content="Ogani Template">
    <meta name="keywords" content="Ogani, unica, creative, html">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Farm Details | Online Pig Market</title>
    <jsp:include page="component/library.jsp"/>
</head>
<body>
<jsp:include page="component/header.jsp"/>
<main class="w-120 mx-auto">
    <div>
        <div>ID = ${farm.farmID}</div>
        <div>Name = ${farm.farmName}</div>
        <div>Location = ${farm.location}</div>
        <div>Description = ${farm.description}</div>
    </div>
</main>
<jsp:include page="component/footer.jsp"/>
</body>
</html>