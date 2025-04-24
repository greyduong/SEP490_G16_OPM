<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>View Application - Online Pig Market</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container text-center">
                <div class="breadcrumb__text">
                    <h2>Application List</h2>
                    <div class="breadcrumb__option">
                        <a href="home">Home</a>
                        <span>Applications</span>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- Application Display Table -->
        <section class="product-details spad">
            <div class="container">
                <h4 class="mb-3">Applications</h4>
                <table class="table table-bordered table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>Purpose</th>
                            <th>Create Date</th>
                            <th>File</th>
                            <th>Status</th>
                            <th>Processing Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- Loop through applications and display each in the table -->
                        <c:forEach var="application" items="${applicationList}">
                            <tr>
                                <td>${application.content}</td>
                                <td><fmt:formatDate value="${application.sentAt}" pattern="dd/MM/yyyy" /></td>
                                <!-- Display the file associated with the application -->
                                <td>
                                    <c:if test="${not empty application.file}">
                                        <a href="<c:url value='/application/${application.file}' />" target="_blank">${application.file}</a>
                                    </c:if>
                                    <c:if test="${empty application.file}">
                                        No file available
                                    </c:if>
                                </td>
                                <td>${application.status}</td>
                                <td><fmt:formatDate value="${application.processingDate}" pattern="dd/MM/yyyy" /></td>
                            </tr>
                        </c:forEach>
                        <!-- Display message when there are no applications -->
                        <c:if test="${empty applicationList}">
                            <tr>
                                <td colspan="5" class="text-center text-muted">No applications available.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>
        <jsp:include page="component/footer.jsp" />
    </body>
</html>
