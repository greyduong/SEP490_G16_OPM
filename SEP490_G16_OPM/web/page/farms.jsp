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
        <title>Online Pig Market</title>
        <jsp:include page="../component/library.jsp"></jsp:include>
        </head>
        <body>
        <jsp:include page="../component/header.jsp"></jsp:include>
            <section class="container">
                <div>
                    <form method="GET" action="farm">
                        <div class="d-flex">
                            <div class="d-inline-flex" style="gap: .5rem;">
                                <input name="search" class="form-control" value="${param.search}" />
                            <button type="submit" class="btn btn-secondary"><i class="fa fa-search"></i></button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="row row-cols-3 py-2">
                <c:forEach items="${page.getData()}" var="farm">
                    <div class="col">
                        <div class="flex flex-col gap-2">
                            <div>
                                <img src="img/blog/blog-1.jpg" alt="">
                            </div>
                            <div class="flex flex-col gap-2">
                                <div>
                                    <h5><a href="?id=${farm.getFarmID()}">${farm.getFarmName()}</a></h5>
                                    <div>${farm.getLocation()}</div>
                                </div>
                                <div>${farm.getDescription()}</div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div>Total: ${page.getTotalPage()} Pages</div>
            <div>Current Page: Page ${page.getPageNumber()}</div>
            <div class="d-flex justify-content-center">
                <div class="d-flex" style="gap: .3rem;">
                    <a class="btn btn-secondary" href="?pageNumber=${prevPage}&search=${param.search}">Previous</a>
                    <a class="btn btn-secondary" href="?pageNumber=${nextPage}&search=${param.search}">Next</a>
                </div>
            </div>
        </section>
        <jsp:include page="../component/footer.jsp"></jsp:include>
    </body>
</html>