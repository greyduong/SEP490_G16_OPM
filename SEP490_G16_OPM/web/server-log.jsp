<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Server Log | Online Pig Market</title>
        <jsp:include page="component/library.jsp"/>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>
        <div class="px-5 flex flex-col gap-3">
            <table class="text-slate-600 rounded-sm">
                <thead class="bg-slate-50 text-sm font-semibold">
                    <tr class="*:border *:border-slate-300 *:py-1 *:px-2">
                        <th>Thời gian</th>
                        <th>Nội dung</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${logs.totalElements == 0}">
                        <tr class="*:border *:border-slate-300 *:py-1 *:px-2">
                            <td colspan="2">Không có dữ liệu</td>
                        </tr>
                    </c:if>
                    <c:forEach var="log" items="${logs.data}">
                        <tr class="*:border *:border-slate-300 *:py-1 *:px-2">
                            <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${log.createdAt}" /></td>
                            <td>${log.content}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="flex items-center gap-2">
                <div>Hiển thị trang <b>${logs.pageNumber}</b> trên tổng <b>${logs.totalPage}</b> trang</div>
                <form class="flex *:hover:bg-lime-700 *:bg-lime-600 text-white *:py-1 *:px-2 *:rounded-sm gap-2 *:hover:cursor-pointer">
                    <button name="page" value="${logs.pageNumber - 1}"><span class="mdi mdi-chevron-left"></span></button>
                    <button name="page" value="${logs.pageNumber + 1}"><span class="mdi mdi-chevron-right"></span></button>
                </form>
            </div>
        </div>
        <jsp:include page="component/footer.jsp"/>
    </body>
</html>

