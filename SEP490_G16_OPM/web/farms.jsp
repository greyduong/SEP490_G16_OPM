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
        <title>List Farms | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <main class="w-200 mx-auto">
            <div class="mb-3">
                <form method="GET" action="">
                    <div class="rounded-lg inline-flex items-center justify-center border border-slate-300 has-[:focus]:!border-slate-400">
                        <span class="px-2 mdi mdi-magnify"></span>
                        <input type="text" value="${param.search}" name="search" class="py-1 rounded-lg" placeholder="Search for farms">
                    </div>
                </form>
            </div>
            <div class="grid grid-cols-3 gap-5 mb-3">
                <c:forEach items="${page.data}" var="farm">
                    <div>
                        <div class="flex flex-col gap-2 ring-1 ring-slate-500/10">
                            <div>
                                <img src="img/blog/blog-1.jpg" alt="">
                            </div>
                            <div class="flex flex-col gap-2 p-2">
                                <div class="flex flex-col">
                                    <a href="?id=${farm.farmID}" class="!text-slate-500 !font-bold hover:!text-slate-700">${farm.farmName}</a>
                                    <div class="text-sm text-slate-600">${farm.getLocation()}</div>
                                </div>
                                <div>${farm.getDescription()}</div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="flex gap-3 items-center mb-5">
                <div>Showing ${offset+1}-${offset+page.data.size()} of ${page.totalElements} items</div>
                <a href="?pageNumber=${prevPage}&search=${param.search}" class="py-1 px-2 rounded-lg border-slate-300 border !text-slate-600">Previous</a>
                <a href="?pageNumber=${nextPage}&search=${param.search}" class="py-1 px-2 rounded-lg border-slate-300 border !text-slate-600">Next</a>
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />
    </body>
</html>