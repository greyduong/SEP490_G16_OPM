<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="canCreateDelivery" value="${sessionScope.user.userID == order.seller.userID 
                                        and (order.status == 'Deposited' or order.status == 'Processing')}" />
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết đơn hàng</title>
        <jsp:include page="component/library.jsp" />
        <style>
            .table th {
                white-space: normal;
                word-wrap: break-word;
            }
        </style>
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
                    <div class="col-md-5">
                        <h3>Thông tin đơn hàng</h3>
                        <table class="table table-bordered">
                            <tr><th>Mã đơn hàng</th><td>${order.orderID}</td></tr>
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
                            <tr><th>Tên trang trại</th><td>${order.farm.farmName}</td></tr>
                            <tr><th>Người mua</th><td>${order.dealer.fullName}</td></tr>
                            <tr><th>Tên chào bán</th><td>${order.pigsOffer.name}</td></tr>
                            <tr><th>Tổng giá (VND)</th><td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true" /></td></tr>
                            <tr><th>Số lượng</th><td>${order.quantity}</td></tr>
                            <tr><th>Ngày tạo</th><td><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" /></td></tr>
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

                        <c:if test="${canCreateDelivery}">
                            <button type="button" class="btn btn-outline-primary btn-sm" data-toggle="modal" data-target="#createDeliveryModal">
                                ➕ Tạo giao hàng
                            </button>
                        </c:if>
                    </div>

                    <!-- Danh sách giao hàng -->
                    <div class="col-md-7">
                        <h3>Danh sách giao hàng</h3>
                        <c:if test="${empty deliveryList}">
                            <p class="text-center">Chưa có giao hàng nào cho đơn hàng này.</p>
                        </c:if>

                        <c:if test="${not empty deliveryList}">
                            <table class="table table-bordered text-center">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Mã</th><th>Trạng thái</th><th>Người nhận</th>
                                        <th>Số lượng</th><th>Tổng giá (VND)</th><th>Ngày tạo</th><th>Ghi chú</th>
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
                                        <td colspan="3">✅ Tổng đã giao</td>
                                        <td>${totalDeliveredQuantity}</td>
                                        <td colspan="3"><fmt:formatNumber value="${totalDeliveredPrice}" type="number" groupingUsed="true"/></td>
                                    </tr>
                                    <tr class="table-warning font-weight-bold">
                                        <td colspan="3">⏳ Đang chờ xác nhận</td>
                                        <td>${totalPendingQuantity}</td>
                                        <td colspan="3"><fmt:formatNumber value="${totalPendingPrice}" type="number" groupingUsed="true"/></td>
                                    </tr>
                                    <tr class="table-primary font-weight-bold">
                                        <td colspan="3">📦 Tổng đã tạo</td>
                                        <td>${totalCreatedQuantity}</td>
                                        <td colspan="3"><fmt:formatNumber value="${totalCreatedPrice}" type="number" groupingUsed="true"/></td>
                                    </tr>
                                    <tr class="table-danger font-weight-bold">
                                        <td colspan="3">🧮 Còn lại</td>
                                        <td>${realRemainingQuantity}</td>
                                        <td colspan="3"><fmt:formatNumber value="${realRemainingPrice}" type="number" groupingUsed="true"/></td>
                                    </tr>
                                </tbody>
                            </table>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Cảnh báo khi trái quyền mở modal -->
        <c:if test="${param.openCreateDelivery == 'true' and not canCreateDelivery}">
            <div class="container mt-3">
                <div class="alert alert-warning text-center">
                    Bạn không có quyền tạo giao hàng cho đơn hàng này hoặc đơn hàng đã hoàn tất.
                </div>
            </div>
        </c:if>

        <!-- Modal tạo giao hàng -->
        <div class="modal fade" id="createDeliveryModal" tabindex="-1" role="dialog" aria-labelledby="createDeliveryModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                <div class="modal-content">
                    <form action="create-delivery" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title" id="createDeliveryModalLabel">Tạo giao hàng mới</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Đóng">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>

                        <div class="modal-body">
                            <c:if test="${canCreateDelivery}">
                                <input type="hidden" name="orderID" value="${order.orderID}" />
                                <input type="hidden" name="dealerID" value="${order.dealer.userID}" />
                                <input type="hidden" name="sellerID" value="${order.seller.userID}" />

                                <c:if test="${realRemainingQuantity <= 0 && realRemainingPrice <= 0}">
                                    <div class="alert alert-danger">
                                        Bạn không thể tạo thêm giao hàng vì đã có đủ số lượng trong các đơn 
                                        <strong>đang chờ xác nhận hoặc đã xác nhận</strong>. Vui lòng chờ người mua xác nhận trước.
                                    </div>
                                </c:if>

                                <c:if test="${realRemainingQuantity > 0 || realRemainingPrice > 0}">
                                    <div class="form-group">
                                        <label>Tên người nhận:</label>
                                        <input type="text" name="recipientName" class="form-control" value="${sessionScope.prevRecipient}" required />
                                        <c:if test="${not empty sessionScope.recipientError}">
                                            <div class="text-danger">${recipientError}</div>
                                            <c:remove var="recipientError" scope="session"/>
                                        </c:if>
                                    </div>

                                    <div class="form-group">
                                        <label>Số lượng:</label>
                                        <input type="number" name="quantity" class="form-control" min="0" max="${remainingQuantity}" value="${sessionScope.prevQuantity}" required />
                                        <c:if test="${not empty sessionScope.quantityError}">
                                            <div class="text-danger">${quantityError}</div>
                                            <c:remove var="quantityError" scope="session"/>
                                        </c:if>
                                    </div>

                                    <div class="form-group">
                                        <label>Tổng giá (VND):</label>
                                        <input type="number" name="totalPrice" class="form-control" min="0" max="${remainingPrice}" step="1000" value="${sessionScope.prevTotalPrice}" required />
                                        <c:if test="${not empty sessionScope.priceError}">
                                            <div class="text-danger">${priceError}</div>
                                            <c:remove var="priceError" scope="session"/>
                                        </c:if>
                                    </div>

                                    <div class="form-group">
                                        <label>Ghi chú:</label>
                                        <textarea name="comments" class="form-control" rows="3">${sessionScope.prevComment}</textarea>
                                        <c:if test="${not empty sessionScope.commentError}">
                                            <div class="text-danger">${commentError}</div>
                                            <c:remove var="commentError" scope="session"/>
                                        </c:if>
                                    </div>
                                </c:if>
                            </c:if>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                            <c:if test="${canCreateDelivery && (realRemainingQuantity > 0 || realRemainingPrice > 0)}">
                                <button type="submit" class="btn btn-success">Xác nhận giao hàng</button>
                            </c:if>
                        </div>

                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="component/footer.jsp" />

        <script>
            window.addEventListener("DOMContentLoaded", () => {
                const params = new URLSearchParams(window.location.search);
                const canCreate = ${canCreateDelivery ? 'true' : 'false'};
                if (params.get("openCreateDelivery") === "true" && canCreate) {
                    $('#createDeliveryModal').modal('show');
                }
            });
        </script>

    </body>
</html>
