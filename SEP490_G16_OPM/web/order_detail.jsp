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
            .status-Pending {
                color: #ffc107;
                font-weight: bold;
            }
            .status-Confirmed {
                color: #28a745;
                font-weight: bold;
            }
            .status-Canceled {
                color: #dc3545;
                font-weight: bold;
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
                                    <c:when test="${order.status == 'Pending'}">
                                        <span style="color: #ffc107; font-weight: bold;">Chờ xác nhận</span>
                                    </c:when>
                                    <c:when test="${order.status == 'Confirmed'}">
                                        <span style="color: #007bff; font-weight: bold;">Đã xác nhận</span>
                                    </c:when>
                                    <c:when test="${order.status == 'Rejected'}">
                                        <span style="color: #dc3545; font-weight: bold;">Đã từ chối</span>
                                    </c:when>
                                    <c:when test="${order.status == 'Canceled'}">
                                        <span style="color: #6c757d; font-weight: bold;">Đã hủy</span>
                                    </c:when>
                                    <c:when test="${order.status == 'Processing'}">
                                        <span style="color: #17a2b8; font-weight: bold;">Đang xử lý</span>
                                    </c:when>
                                    <c:when test="${order.status == 'Deposited'}">
                                        <span style="color: #8e44ad; font-weight: bold;">Đã đặt cọc</span>
                                    </c:when>
                                    <c:when test="${order.status == 'Completed'}">
                                        <span style="color: #28a745; font-weight: bold;">Hoàn thành</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>${order.status}</span>
                                    </c:otherwise>
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
                                    <td class="status-${d.deliveryStatus}">
                                        <c:choose>
                                            <c:when test="${d.deliveryStatus == 'Pending'}">Chờ xác nhận</c:when>
                                            <c:when test="${d.deliveryStatus == 'Confirmed'}">Đã xác nhận</c:when>
                                            <c:when test="${d.deliveryStatus == 'Canceled'}">Đã hủy</c:when>
                                            <c:otherwise>${d.deliveryStatus}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${d.recipientName}</td>
                                    <td>${d.quantity}</td>
                                    <td><fmt:formatNumber value="${d.totalPrice}" type="number" groupingUsed="true"/></td>
                                    <td><fmt:formatDate value="${d.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td class="wrap-text">${d.comments}</td>
                                    <td>
                                        <c:if test="${d.deliveryStatus == 'Pending' && order.status == 'Processing'}">
                                            <form action="confirm-delivery" method="post" style="display:inline-block; margin-right: 5px;">
                                                <input type="hidden" name="deliveryID" value="${d.deliveryID}" />
                                                <button type="submit" class="btn btn-sm btn-success">Xác nhận</button>
                                            </form>
                                            <form action="cancel-delivery" method="post" style="display:inline-block;">
                                                <input type="hidden" name="deliveryID" value="${d.deliveryID}" />
                                                <button type="submit" class="btn btn-sm btn-danger">Hủy</button>
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
        <script>
            document.querySelectorAll("form").forEach(form => {
                form.addEventListener("submit", function () {
                    // Nếu đã có overlay, không tạo thêm
                    if (document.getElementById("loading-overlay"))
                        return;

                    // Tạo overlay
                    const overlay = document.createElement("div");
                    overlay.id = "loading-overlay"; // Gán ID để xử lý sau
                    overlay.style.position = "fixed";
                    overlay.style.top = 0;
                    overlay.style.left = 0;
                    overlay.style.width = "100%";
                    overlay.style.height = "100%";
                    overlay.style.backgroundColor = "rgba(0,0,0,0.5)";
                    overlay.style.display = "flex";
                    overlay.style.justifyContent = "center";
                    overlay.style.alignItems = "center";
                    overlay.style.zIndex = 9999;

                    // Tạo nội dung loading
                    const spinner = document.createElement("div");
                    spinner.innerHTML = `
                <div class="spinner-border text-light" role="status" style="width: 3rem; height: 3rem;">
                    <span class="sr-only">Loading...</span>
                </div>
                <div class="text-white mt-3">Đang xử lý, vui lòng chờ...</div>
            `;
                    spinner.style.textAlign = "center";

                    overlay.appendChild(spinner);
                    document.body.appendChild(overlay);
                });
            });

            // Khi back lại, xóa overlay nếu còn tồn tại
            window.addEventListener("pageshow", function () {
                const overlay = document.getElementById("loading-overlay");
                if (overlay)
                    overlay.remove();
            });
        </script>
    </body>
</html>
