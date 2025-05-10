<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Chi tiết đơn hàng</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <!-- Order Detail Section Begin -->
        <div class="container mt-5 mb-5">
            <c:if test="${not empty msg}">
                <div class="alert alert-info text-center">${msg}</div>
            </c:if>

            <div class="row">
                <div class="col-lg-5">
                    <h4>Thông tin đơn hàng</h4>
                    <table class="table table-bordered">
                        <tr>
                            <th>Mã đơn hàng</th>
                            <td>${order.orderID}</td>
                        </tr>
                        <tr>
                            <th>Trạng thái</th>
                            <td>
                                <c:choose>
                                    <c:when test="${order.status == 'Pending'}">Chờ xác nhận</c:when>
                                    <c:when test="${order.status == 'Confirmed'}">Đã xác nhận</c:when>
                                    <c:when test="${order.status == 'Rejected'}">Đã từ chối</c:when>
                                    <c:when test="${order.status == 'Canceled'}">Đã hủy</c:when>
                                    <c:when test="${order.status == 'Processing'}">Đang xử lý</c:when>
                                    <c:when test="${order.status == 'Deposited'}">Đã đặt cọc</c:when>
                                    <c:when test="${order.status == 'Completed'}">Hoàn thành</c:when>
                                    <c:otherwise>${order.status}</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th>Tên chào bán</th>
                            <td>${order.pigsOffer.name}</td>
                        </tr>
                        <tr>
                            <th>Người bán</th>
                            <td>${seller.fullName}</td>
                        </tr>
                        <tr>
                            <th>Số lượng</th>
                            <td>${order.quantity}</td>
                        </tr>
                        <tr>
                            <th>Tổng giá (VND)</th>
                            <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/></td>
                        </tr>
                        <tr>
                            <th>Ngày tạo</th>
                            <td><fmt:formatDate value="${order.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                        <tr>
                            <th>Ngày xử lý</th>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty order.processedDate}">
                                        <fmt:formatDate value="${order.processedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>

                    </table>
                </div>

                <div class="col-lg-7">
                    <h4>Thông tin giao hàng</h4>
                    <c:choose>
                        <c:when test="${not empty deliveryList}">
                            <table class="table table-bordered text-center">
                                <thead class="thead-dark">
                                    <tr>
                                        <th>Mã giao hàng</th>
                                        <th>Trạng thái</th>
                                        <th>Người nhận</th>
                                        <th>Số lượng</th>
                                        <th>Tổng giá</th>
                                        <th>Ngày tạo</th>
                                        <th>Thao tác</th>
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
                                                        <button type="submit" class="btn btn-sm btn-success">Xác nhận</button>
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
                            <p class="text-center mt-3">Chưa có thông tin giao hàng nào.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <!-- Order Detail Section End -->
        <jsp:include page="component/footer.jsp" />
    </body>
</html>
