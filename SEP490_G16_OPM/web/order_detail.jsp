<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Order Details</title>
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
                            <h2>Order Details</h2>
                            <div class="breadcrumb__option">
                                <a href="home">Home</a>
                                <span>OrderDetails</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- Order Detail Section Begin -->
        <div class="container mt-5 mb-5">
            <c:if test="${not empty msg}">
                <div class="alert alert-info text-center">${msg}</div>
            </c:if>

            <div class="row">
                <div class="col-lg-5">
                    <h4>Order Information</h4>
                    <table class="table table-bordered">
                        <tr>
                            <th>Order ID</th>
                            <td>${order.orderID}</td>
                        </tr>
                        <tr>
                            <th>Status</th>
                            <td>${order.status}</td>
                        </tr>
                        <tr>
                            <th>Offer</th>
                            <td>${order.pigsOffer.name}</td>
                        </tr>
                        <tr>
                            <th>Seller</th>
                            <td>${seller.fullName}</td>
                        </tr>
                        <tr>
                            <th>Quantity</th>
                            <td>${order.quantity}</td>
                        </tr>
                        <tr>
                            <th>Total Price (VND)</th>
                            <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/></td>
                        </tr>
                        <tr>
                            <th>Created At</th>
                            <td><fmt:formatDate value="${order.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                    </table>
                </div>

                <div class="col-lg-7">
                    <h4>Delivery Information</h4>
                    <c:choose>
                        <c:when test="${not empty deliveryList}">
                            <table class="table table-bordered text-center">
                                <thead class="thead-dark">
                                    <tr>
                                        <th>Delivery ID</th>
                                        <th>Status</th>
                                        <th>Recipient</th>
                                        <th>Quantity</th>
                                        <th>Total Price</th>
                                        <th>Created At</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="d" items="${deliveryList}">
                                        <tr>
                                            <td>${d.deliveryID}</td>
                                            <td>${d.deliveryStatus}</td>
                                            <td>${d.recipientName}</td>
                                            <td>${d.quantity}</td>
                                            <td><fmt:formatNumber value="${d.totalPrice}" type="number" groupingUsed="true"/></td>
                                            <td><fmt:formatDate value="${d.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td>
                                                <c:if test="${d.deliveryStatus == 'Pending'}">
                                                    <form action="ConfirmDeliveryController" method="post" style="display:inline;">
                                                        <input type="hidden" name="deliveryID" value="${d.deliveryID}" />
                                                        <button type="submit" class="btn btn-sm btn-success">Confirm</button>
                                                    </form>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <tr class="table-info font-weight-bold">
                                        <td colspan="3">Tổng đã giao</td>
                                        <td>${totalDeliveredQuantity}</td>
                                        <td><fmt:formatNumber value="${totalDeliveredPrice}" type="number" groupingUsed="true"/></td>
                                        <td colspan="2"></td>
                                    </tr>
                                    <tr class="table-warning font-weight-bold">
                                        <td colspan="3">Còn lại</td>
                                        <td>${remainingQuantity}</td>
                                        <td><fmt:formatNumber value="${remainingPrice}" type="number" groupingUsed="true"/></td>
                                        <td colspan="2"></td>
                                    </tr>

                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <p class="text-center mt-3">No delivery information available.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <!-- Order Detail Section End -->
        <jsp:include page="component/footer.jsp" />
    </body>

</html>



