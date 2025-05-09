<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Orders Request Page</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <c:if test="${not empty msg}">
            <div class="alert alert-info" role="alert">
                ${msg}  <!-- This will display the message set in the controller -->
            </div>
        </c:if> 

        <!-- Order List Section Begin -->
        <div class="container mt-5">
            <c:if test="${empty orderList}">
                <p class="text-center">You have no pending orders.</p>
            </c:if>
            <c:if test="${not empty orderList}">
                <form action="ConfirmOrderController" method="post">
                    <table class="table table-bordered text-center">
                        <thead class="table-dark">
                            <tr>
                                <th>Order ID</th>
                                <th>Dealer</th>
                                <th>Offer</th>
                                <th>Quantity</th>
                                <th>Total Price (VND)</th>
                                <th>Status</th>
                                <th>Created At</th>
                                <th>Action</th> <!-- Action column for the Confirm button -->
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${orderList}">
                                <tr>
                                    <td><a href="view-order-detail?id=${o.orderID}">${o.orderID}</a></td>
                                    <td>${o.dealer.fullName}</td>
                                    <td>${o.pigsOffer.name}</td>
                                    <td>${o.quantity}</td>
                                    <td><fmt:formatNumber value="${o.totalPrice}" type="number" groupingUsed="true"/></td>
                                    <td>${o.status}</td>
                                    <td><fmt:formatDate value="${o.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>
                                        <c:if test="${o.status == 'Pending'}">
                                            <!-- Add a confirm button for Pending orders -->
                                            <button type="submit" name="orderID" value="${o.orderID}" class="btn btn-success">Confirm</button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </form>
            </c:if>
        </div>
        <jsp:include page="component/footer.jsp"></jsp:include>
    </body>
</html>
