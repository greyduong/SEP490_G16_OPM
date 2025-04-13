<%-- 
    Document   : myorders.jsp
    Created on : Apr 10, 2025, 7:40:30 PM
    Author     : duong
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>My Orders</title>
        <link rel="stylesheet" href="css/bootstrap.min.css"/>
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="text-center mb-4">My Orders</h2>
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

        <script src="js/bootstrap.min.js"></script>
    </body>
</html>

