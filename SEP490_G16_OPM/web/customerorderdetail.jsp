<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết đơn hàng</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <c:if test="${not empty msg}">
            <div class="alert alert-info" role="alert">${msg}</div>
        </c:if>

        <div class="container mt-5">
            <c:if test="${not empty order}">
                <div class="row">
                    <!-- Thông tin đơn hàng -->
                    <div class="col-md-4">
                        <h3>Thông tin đơn hàng</h3>
                        <table class="table table-bordered">
                            <tr>
                                <th>Mã đơn hàng</th>
                                <td>${order.orderID}</td>
                            </tr>
                            <tr>
                                <th>Trạng thái</th>
                                <td>${order.status}</td>
                            </tr>
                            <tr>
                                <th>Người mua</th>
                                <td>${order.dealer.fullName}</td>
                            </tr>
                            <tr>
                                <th>Tên chào bán</th>
                                <td>${order.pigsOffer.name}</td>
                            </tr>
                            <tr>
                                <th>Tổng giá (VND)</th>
                                <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true" /></td>
                            </tr>
                            <tr>
                                <th>Số lượng</th>
                                <td>${order.quantity}</td>
                            </tr>
                            <tr>
                                <th>Ngày tạo</th>
                                <td><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" /></td>
                            </tr>
                            <tr>
                                <th>Ngày xử lý</th>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty order.processedDate}">
                                            <fmt:formatDate value="${order.processedDate}" pattern="dd/MM/yyyy HH:mm" />
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </table>

                        <button type="button" class="btn btn-outline-primary btn-sm" data-toggle="modal" data-target="#createDeliveryModal">
                            ➕ Tạo giao hàng
                        </button>
                    </div>

                    <!-- Thông tin giao hàng -->
                    <div class="col-md-7">
                        <h3>Danh sách giao hàng</h3>
                        <c:if test="${empty deliveryList}">
                            <p class="text-center">Chưa có giao hàng nào cho đơn hàng này.</p>
                        </c:if>

                        <c:if test="${not empty deliveryList}">
                            <table class="table table-bordered text-center">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Mã</th>
                                        <th>Trạng thái</th>
                                        <th>Người nhận</th>
                                        <th>Số lượng</th>
                                        <th>Tổng giá (VND)</th>
                                        <th>Ngày tạo</th>
                                        <th>Ghi chú</th>
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
                                            <td><fmt:formatDate value="${d.createdAt}" pattern="dd/MM/yyyy HH:mm" /></td>
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

        <!-- Modal tạo giao hàng -->
        <div class="modal fade" id="createDeliveryModal" tabindex="-1" role="dialog" aria-labelledby="createDeliveryModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                <div class="modal-content">
                    <form action="CreateDeliveryController" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title" id="createDeliveryModalLabel">Tạo giao hàng mới</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Đóng">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>

                        <div class="modal-body">
                            <input type="hidden" name="orderID" value="${order.orderID}" />
                            <input type="hidden" name="dealerID" value="${order.dealer.userID}" />
                            <input type="hidden" name="sellerID" value="${order.seller.userID}" />

                            <div class="form-group">
                                <label>Tên người nhận:</label>
                                <input type="text" name="recipientName" class="form-control" required />
                            </div>

                            <div class="form-group">
                                <label>Số lượng:</label>
                                <input type="number" name="quantity" class="form-control" min="0" max="${remainingQuantity}" required />
                            </div>

                            <div class="form-group">
                                <label>Tổng giá (VND):</label>
                                <input type="number" name="totalPrice" class="form-control" min="0" max="${remainingPrice}" step="1000" required />
                            </div>

                            <div class="form-group">
                                <label>Ghi chú:</label>
                                <textarea name="comments" class="form-control" rows="3"></textarea>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                            <button type="submit" class="btn btn-success">Xác nhận giao hàng</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="component/footer.jsp" />

        <script>
            // Mở modal tự động nếu có ?openCreateDelivery=true
            window.addEventListener("DOMContentLoaded", () => {
                const params = new URLSearchParams(window.location.search);
                if (params.get("openCreateDelivery") === "true") {
                    $('#createDeliveryModal').modal('show');
                }
            });
        </script>

    </body>
</html>
