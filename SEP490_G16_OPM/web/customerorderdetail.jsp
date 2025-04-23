<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Customer Order Detail</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <div class="breadcrumb__text">
                            <h2>Customer Order Detail</h2>
                            <div class="breadcrumb__option">
                                <a href="home">Home</a>
                                <span>Customer Order Detail</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Display error or success message -->
        <c:if test="${not empty msg}">
            <div class="alert alert-info" role="alert">
                ${msg}  <!-- This will display the message set in the controller -->
            </div>
        </c:if>

        <!-- Order and Dealer Information Section -->
        <div class="container mt-5">
            <c:if test="${not empty order}">
                <div class="row">
                    <!-- Order Information Table (takes up 6 columns) -->
                    <div class="col-md-4">
                        <h3>Order Information</h3>
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
                                <th>Dealer's Name</th>
                                <td>${order.dealer.fullName}</td>
                            </tr>
                            <tr>
                                <th>Offer</th>
                                <td>${order.pigsOffer.name}</td>
                            </tr>
                            <tr>
                                <th>Total Price (VND)</th>
                                <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true" /></td>
                            </tr>
                            <tr>
                                <th>Quantity</th>
                                <td>${order.quantity}</td>
                            </tr>
                            <tr>
                                <th>Created At</th>
                                <td><fmt:formatDate value="${order.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                            </tr>
                        </table>
                        <c:if test="${not showCreateDeliveryForm}">
                            <form action="CustomerOrderDetailController" method="get" class="mt-2">
                                <input type="hidden" name="id" value="${order.orderID}" />
                                <input type="hidden" name="openCreateDelivery" value="true" />
                                <button type="submit" class="btn btn-sm btn-outline-primary">Create Delivery</button>
                            </form>
                        </c:if>
                    </div>

                    <!-- Delivery Information Table (takes up 6 columns) -->
                    <div class="col-md-7">
                        <h3>Delivery Information</h3>
                        <!-- Check if deliveryList is empty -->
                        <c:if test="${empty deliveryList}">
                            <p class="text-center">No deliveries found for this order.</p>
                        </c:if>

                        <c:if test="${not empty deliveryList}">
                            <table class="table table-bordered text-center">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Delivery ID</th>
                                        <th>Delivery Status</th>
                                        <th>Recipient Name</th>
                                        <th>Quantity</th>
                                        <th>Total Price</th>
                                        <th>Created At</th>
                                        <th>Comments</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="d" items="${deliveryList}">
                                        <tr>
                                            <td>${d.deliveryID}</td>
                                            <td>${d.deliveryStatus}</td>
                                            <td>${d.recipientName}</td>
                                            <td>${d.quantity}</td>
                                            <td><fmt:formatNumber value="${d.totalPrice}" type="number" groupingUsed="true" /></td>
                                            <td><fmt:formatDate value="${d.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                            <td>${d.comments}</td>
                                        </tr>
                                    </c:forEach>
                                    <tr class="table-info font-weight-bold">
                                        <td colspan="3">Tổng đã giao</td>
                                        <td>${totalDeliveredQuantity}</td>
                                        <td colspan="3"><fmt:formatNumber value="${totalDeliveredPrice}" type="number" groupingUsed="true"/></td>
                                    </tr>
                                    <tr class="table-warning font-weight-bold">
                                        <td colspan="3">Còn lại</td>
                                        <td>${remainingQuantity}</td>
                                        <td colspan="3"><fmt:formatNumber value="${remainingPrice}" type="number" groupingUsed="true"/></td>
                                    </tr>
                                </tbody>
                            </table>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>

        <c:if test="${showCreateDeliveryForm}">
            <div class="container mt-4">
                <h3>Create New Delivery</h3>
                <form action="CreateDeliveryController" method="post">
                    <input type="hidden" name="orderID" value="${order.orderID}" />
                    <input type="hidden" name="dealerID" value="${order.dealer.userID}" />
                    <input type="hidden" name="sellerID" value="${order.seller.userID}" />
                    <div class="form-group">
                        <label>Recipient Name:</label>
                        <input type="text" name="recipientName" class="form-control" required />
                    </div>

                    <div class="form-group">
                        <label>Quantity:</label>
                        <input type="number" name="quantity" class="form-control" min="0" max="${remainingQuantity}" required />
                    </div>
                    <div class="form-group">
                        <label>Total Price (VND):</label>
                        <input type="number" name="totalPrice" class="form-control" min="0" max="${remainingPrice}" step="1000" required />
                    </div>


                    <div class="form-group">
                        <label>Comments:</label>
                        <textarea name="comments" class="form-control" rows="3"></textarea>
                    </div>

                    <button type="submit" class="btn btn-success">Confirm Delivery</button>
                </form>
            </div>
        </c:if>
        <jsp:include page="component/footer.jsp"></jsp:include>
    </body>
</html>
