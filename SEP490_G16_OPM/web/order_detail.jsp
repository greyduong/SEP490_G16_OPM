<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết đơn hàng</title>
        <jsp:include page="component/library.jsp" />
        <style>
            .table td, .table th {
                vertical-align: middle;
            }
            .wrap-text {
                white-space: pre-line;
            }
        </style>
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <div class="container mt-5 mb-5">
            <c:if test="${not empty msg}">
                <div class="alert alert-info text-center">${msg}</div>
            </c:if>

            <h4 class="mb-3">Thông tin đơn hàng</h4>
            <div class="row">
                <div class="col-md-6">
                    <table class="table table-bordered">
                        <tr><th>Mã</th><td>${order.orderID}</td></tr>
                        <tr><th>Tên chào bán</th><td>${order.pigsOffer.name}</td></tr>
                        <tr><th>Số lượng</th><td>${order.quantity}</td></tr>
                        <tr><th>Tổng giá (VND)</th>
                            <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true" /></td></tr>
                    </table>
                </div>
                <div class="col-md-6">
                    <table class="table table-bordered">
                        <tr><th>Tên trang trại</th><td>${order.farm.farmName}</td></tr>
                        <tr><th>Người bán</th><td>${seller.fullName}</td></tr>
                        <tr><th>Trạng thái</th>
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
                        <tr><th>Ngày tạo</th><td><fmt:formatDate value="${order.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>
                        <tr><th>Ngày xử lý</th>
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
            </div>

            <h4 class="mt-5 mb-3">Thông tin giao hàng</h4>
            <c:choose>
                <c:when test="${not empty deliveryList}">
                    <table class="table table-bordered text-center">
                        <thead class="thead-dark">
                            <tr>
                                <th>Mã</th>
                                <th>Trạng thái</th>
                                <th>Người nhận</th>
                                <th>Số lượng</th>
                                <th>Tổng giá</th>
                                <th>Ngày tạo</th>
                                <th>Ghi chú</th>
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
                                    <td class="wrap-text">${d.comments}</td>
                                    <td>
                                        <c:if test="${d.deliveryStatus == 'Pending' && order.status == 'Processing'}">
                                            <form action="confirm-delivery" method="post" style="display:inline;">
                                                <input type="hidden" name="deliveryID" value="${d.deliveryID}" />
                                                <button type="submit" class="btn btn-sm btn-success">Xác nhận</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr class="table-info font-weight-bold">
                                <td colspan="3">✅ Tổng đã giao</td>
                                <td>${totalDeliveredQuantity}</td>
                                <td><fmt:formatNumber value="${totalDeliveredPrice}" type="number" groupingUsed="true"/></td>
                                <td colspan="3"></td>
                            </tr>
                            <tr class="table-warning font-weight-bold">
                                <td colspan="3">⏳ Đang chờ xác nhận</td>
                                <td>${totalPendingQuantity}</td>
                                <td><fmt:formatNumber value="${totalPendingPrice}" type="number" groupingUsed="true"/></td>
                                <td colspan="3"></td>
                            </tr>
                            <tr class="table-primary font-weight-bold">
                                <td colspan="3">📦 Tổng đã tạo</td>
                                <td>${totalCreatedQuantity}</td>
                                <td><fmt:formatNumber value="${totalCreatedPrice}" type="number" groupingUsed="true"/></td>
                                <td colspan="3"></td>
                            </tr>
                            <tr class="table-danger font-weight-bold">
                                <td colspan="3">🧮 Còn lại</td>
                                <td>${realRemainingQuantity}</td>
                                <td><fmt:formatNumber value="${realRemainingPrice}" type="number" groupingUsed="true"/></td>
                                <td colspan="3"></td>
                            </tr>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p class="text-center mt-3">Chưa có thông tin giao hàng nào.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <jsp:include page="component/footer.jsp" />
    </body>
</html>
