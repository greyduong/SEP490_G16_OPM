<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Customer Order Page</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <c:if test="${not empty msg}">
            <div class="alert alert-info" role="alert">
                ${msg}  <!-- This will display the message set in the controller -->
            </div>
        </c:if>  
        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <div class="breadcrumb__text">
                            <h2>Customer Order Page</h2>
                            <div class="breadcrumb__option">
                                <a href="./index.html">Home</a>
                                <span>Customer Order Page</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- Order List Section Begin -->
        <div class="container mt-5">
            <c:if test="${empty orderList}">
                <p class="text-center">You have no orders yet.</p>
            </c:if>
            <c:if test="${not empty orderList}">
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
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orderList}">
                            <tr>
                                <td><a href="CustomerOrderDetailController?id=${o.orderID}">${o.orderID}</a></td>
                                <td>${o.dealer.fullName}</td>
                                <td>${o.pigsOffer.name}</td>
                                <td>${o.quantity}</td>
                                <td><fmt:formatNumber value="${o.totalPrice}" type="number" groupingUsed="true"/></td>
                                <td>${o.status}</td>
                                <td><fmt:formatDate value="${o.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td>
                                    <!-- Show Create Delivery Button if the order is either 'Deposited' or 'In Delivery' -->
                                    <c:if test="${o.status == 'Deposited' || o.status == 'In Delivery'}">
                                        <form action="CustomerOrderDetailController" method="get">
                                            <input type="hidden" name="id" value="${o.orderID}" />
                                            <input type="hidden" name="openCreateDelivery" value="true" />
                                            <button type="submit" class="btn btn-primary">Create Delivery</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
        <!-- Order List Section End -->

        <jsp:include page="component/footer.jsp" />
    </body>
</html>
