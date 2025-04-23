<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>My Order</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <div class="breadcrumb__text">
                            <h2>My Order</h2>
                            <div class="breadcrumb__option">
                                <a href="./index.html">Home</a>
                                <span>My Order</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <c:if test="${not empty msg}">
            <div class="alert alert-warning">${msg}</div>
        </c:if>


        <!-- Featured Section Begin -->
        <div class="container mt-5">
            <c:if test="${empty orderList}">
                <p class="text-center">You have no orders yet.</p>
            </c:if>
            <c:if test="${not empty orderList}">
                <table class="table table-bordered text-center">
                    <thead class="table-dark">
                        <tr>
                            <th>Order ID</th>
                            <th>Seller</th>
                            <th>Offer</th>
                            <th>Quantity</th>
                            <th>Total Price (VND)</th>
                            <th>Status</th>
                            <th>Created At</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orderList}">
                            <tr>
                                <td> <a href="view-order-detail?id=${o.orderID}">${o.orderID}</a></td>
                                <td>${o.seller.fullName}</td>
                                <td>${o.pigsOffer.name}</td>
                                <td>${o.quantity}</td>
                                <td><fmt:formatNumber value="${o.totalPrice}" type="number" groupingUsed="true"/></td>
                                <td>${o.status}</td>
                                <td><fmt:formatDate value="${o.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
        <!-- Featured Section End -->
        <jsp:include page="component/footer.jsp" />
    </body>

</html>

