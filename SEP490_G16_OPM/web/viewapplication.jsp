<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    model.User user = (model.User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login-register.jsp");
        return;
    }
    if (user.getRoleID() != 4 && user.getRoleID() != 5) {
        response.sendRedirect("login-register.jsp?error=access-denied");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Xem đơn - Chợ Heo Trực Tuyến</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <!-- Bảng hiển thị đơn đăng ký -->
        <section class="product-details spad">
            <div class="container">
                <h4 class="mb-3">Đơn của tôi</h4>

                <!-- Thanh tìm kiếm -->
                <form class="form-inline mb-3" method="GET" action="application">
                    <input type="text" name="keyword" class="form-control mr-sm-2" placeholder="Tìm theo mục đích hoặc trạng thái..." value="${param.keyword}">
                    <button type="submit" class="btn btn-outline-primary">Tìm kiếm</button>
                </form>

                <table class="table table-bordered table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>Mục đích</th>
                            <th>Ngày gửi</th>
                            <th>Tệp đính kèm</th>
                            <th>Trạng thái</th>
                            <th>Ngày xử lý</th>
                            <th>Phản hồi</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="application" items="${applicationList}">
                            <tr>
                                <td>${application.content}</td>
                                <td><fmt:formatDate value="${application.sentAt}" pattern="dd/MM/yyyy" /></td>
                                <td>
                                    <c:if test="${not empty application.file}">
                                        <a href="<c:url value='/application/${application.file}' />" target="_blank">${application.file}</a>
                                    </c:if>
                                    <c:if test="${empty application.file}">
                                        Không có tệp đính kèm
                                    </c:if>
                                </td>
                                <td>${application.status}</td>
                                <td><fmt:formatDate value="${application.processingDate}" pattern="dd/MM/yyyy" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty application.reply}">
                                            ${application.reply}
                                        </c:when>
                                        <c:otherwise>
                                            Chưa có phản hồi
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                        <!-- Hiển thị nếu không có đơn đăng ký -->
                        <c:if test="${empty applicationList}">
                            <tr>
                                <td colspan="6" class="text-center text-muted">Không có đơn đăng ký nào.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>

        <jsp:include page="component/footer.jsp" />
    </body>
</html>
