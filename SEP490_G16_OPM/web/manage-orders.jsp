
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý đơn hàng | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
        <style>
            .status-Pending {
                color: #ffc107;
                font-weight: bold;
            }
            .status-Confirmed {
                color: #007bff;
                font-weight: bold;
            }
            .status-Rejected {
                color: #dc3545;
                font-weight: bold;
            }
            .status-Canceled {
                color: #6c757d;
                font-weight: bold;
            }
            .status-Processing {
                color: #17a2b8;
                font-weight: bold;
            }
            .status-Completed {
                color: #28a745;
                font-weight: bold;
            }
            .status-Deposited {
                color: #8e44ad;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <div class="container mt-5">
            <h3 class="mb-4">Tất cả đơn hàng hệ thống</h3>

            <c:if test="${not empty msg}">
                <div class="alert alert-info text-center">${msg}</div>
            </c:if>

            <form method="get" action="manage-orders" class="form-inline mb-3">
                <select name="farmId" class="form-control mr-2">
                    <option value="">Tất cả trang trại</option>
                    <c:forEach var="farm" items="${farmList}">
                        <option value="${farm.farmID}" ${param.farmId == farm.farmID ? 'selected' : ''}>${farm.farmName}</option>
                    </c:forEach>
                </select>

                <input type="text" name="search" class="form-control mr-2" placeholder="Tìm mã đơn hoặc tên chào bán" value="${param.search}" />

                <select name="status" class="form-control mr-2">
                    <option value="">Tất cả trạng thái</option>
                    <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Chờ xác nhận</option>
                    <option value="Confirmed" ${param.status == 'Confirmed' ? 'selected' : ''}>Đã xác nhận</option>
                    <option value="Rejected" ${param.status == 'Rejected' ? 'selected' : ''}>Đã từ chối</option>
                    <option value="Canceled" ${param.status == 'Canceled' ? 'selected' : ''}>Đã hủy</option>
                    <option value="Processing" ${param.status == 'Processing' ? 'selected' : ''}>Đang xử lý</option>
                    <option value="Deposited" ${param.status == 'Deposited' ? 'selected' : ''}>Đã đặt cọc</option>
                    <option value="Completed" ${param.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>
                </select>

                <input type="hidden" name="sort" value="${param.sort}" />
                <button type="submit" class="btn btn-success mr-2">Lọc</button>
                <a href="manage-orders" class="btn btn-outline-secondary">Bỏ lọc</a>
            </form>

            <c:set var="currentSort" value="${param.sort}" />
            <c:set var="nextOrderIdSort" value="${currentSort == 'orderid_desc' ? 'orderid_asc' : 'orderid_desc'}" />
            <c:set var="nextQuantitySort" value="${currentSort == 'quantity_desc' ? 'quantity_asc' : 'quantity_desc'}" />
            <c:set var="nextPriceSort" value="${currentSort == 'totalprice_desc' ? 'totalprice_asc' : 'totalprice_desc'}" />
            <c:set var="nextCreatedAtSort" value="${currentSort == 'createdat_desc' ? 'createdat_asc' : 'createdat_desc'}" />
            <c:set var="nextProcessedSort" value="${currentSort == 'processeddate_desc' ? 'processeddate_asc' : 'processeddate_desc'}" />

            <c:if test="${empty orders}">
                <p class="text-center">Không có đơn hàng nào phù hợp.</p>
            </c:if>

            <c:if test="${not empty orders}">
                <div class="table-responsive">
                    <table class="table table-bordered text-center">
                        <thead class="thead-dark">
                            <tr>
                                <th>STT</th>
                                <th>Mã đơn
                                    <a href="manage-orders?sort=${nextOrderIdSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'orderid_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'orderid_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Chào bán</th>
                                <th>Số lượng
                                    <a href="manage-orders?sort=${nextQuantitySort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'quantity_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'quantity_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Tổng giá
                                    <a href="manage-orders?sort=${nextPriceSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'totalprice_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'totalprice_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th style="min-width: 140px;">Trạng thái</th>
                                <th>Ghi chú</th>
                                <th>Ngày tạo
                                    <a href="manage-orders?sort=${nextCreatedAtSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'createdat_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'createdat_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Ngày xử lý
                                    <a href="manage-orders?sort=${nextProcessedSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'processeddate_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'processeddate_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${orders}" varStatus="loop">
                                <tr>
                                    <td>${(page - 1) * 10 + loop.index + 1}</td>
                                    <td>
                                        <a href="#" class="text-primary" data-toggle="modal" data-target="#orderModal${o.orderID}">
                                            ${o.orderID}
                                        </a>
                                    </td>                            
                                    <td>${o.pigsOffer.name}</td>
                                    <td>${o.quantity}</td>
                                    <td><fmt:formatNumber value="${o.totalPrice}" type="number" groupingUsed="true" /></td>
                                    <td class="status-${o.status}">
                                        <c:choose>
                                            <c:when test="${o.status == 'Pending'}">Chờ xác nhận</c:when>
                                            <c:when test="${o.status == 'Confirmed'}">Đã xác nhận</c:when>
                                            <c:when test="${o.status == 'Rejected'}">Đã từ chối</c:when>
                                            <c:when test="${o.status == 'Canceled'}">Đã hủy</c:when>
                                            <c:when test="${o.status == 'Processing'}">Đang xử lý</c:when>
                                            <c:when test="${o.status == 'Deposited'}">Đã đặt cọc</c:when>
                                            <c:when test="${o.status == 'Completed'}">Hoàn thành</c:when>
                                            <c:otherwise>${o.status}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty o.note}">
                                                ${o.note}
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty o.processedDate}">
                                                <fmt:formatDate value="${o.processedDate}" pattern="dd/MM/yyyy" />
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${o.status == 'Pending'}">
                                            <form action="process-order" method="post" class="d-flex align-items-center flex-wrap" style="gap: 8px;">
                                                <input type="hidden" name="orderId" value="${o.orderID}" />
                                                <input type="text" name="reason" class="form-control form-control-sm"
                                                       placeholder="Lý do huỷ..." required style="max-width: 180px;" />
                                                <button type="submit" class="btn btn-sm btn-danger"
                                                        onclick="return confirm('Bạn có chắc chắn muốn huỷ đơn hàng này không?');">
                                                    Huỷ đơn
                                                </button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>

                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <c:if test="${not empty orders}">
                <!-- Modal Chi Tiết Đơn Hàng -->
                <c:forEach var="o" items="${orders}">
                    <div class="modal fade" id="orderModal${o.orderID}" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-dialog modal-xl modal-dialog-scrollable" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title text-primary">Chi tiết đơn hàng #${o.orderID}</h5>
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                </div>

                                <div class="modal-body">
                                    <div class="row">
                                        <!-- Cột TRÁI -->
                                        <div class="col-md-6 pr-md-4 border-right">
                                            <h6 class="text-uppercase mb-3">Chào bán</h6>
                                            <ul class="list-unstyled ml-2">
                                                <li><strong>Tên:</strong> ${o.pigsOffer.name}</li>
                                                <li><strong>Số lượng:</strong> ${o.pigsOffer.quantity} con</li>
                                                <li><strong>Giá bán lẻ:</strong> 
                                                    <fmt:formatNumber value="${o.pigsOffer.retailPrice}" type="number" groupingUsed="true" /> đ
                                                </li>
                                                <li><strong>Thời gian:</strong>
                                                    <fmt:formatDate value="${o.pigsOffer.startDate}" pattern="dd/MM/yyyy" /> – 
                                                    <fmt:formatDate value="${o.pigsOffer.endDate}" pattern="dd/MM/yyyy" />
                                                </li>
                                            </ul>

                                            <h6 class="text-uppercase mt-4 mb-3">Trang trại</h6>
                                            <ul class="list-unstyled ml-2">
                                                <li><strong>Tên:</strong> ${o.pigsOffer.farm.farmName}</li>
                                                <li><strong>Địa điểm:</strong> ${o.pigsOffer.farm.location}</li>
                                            </ul>

                                            <h6 class="text-uppercase mt-4 mb-3">Người mua</h6>
                                            <ul class="list-unstyled ml-2">
                                                <li><strong>Họ tên:</strong> ${o.dealer.fullName}</li>
                                                <li><strong>SĐT:</strong> ${o.dealer.phone}</li>
                                                <li><strong>Địa chỉ:</strong> ${o.dealer.address}</li>
                                            </ul>

                                            <h6 class="text-uppercase mt-4 mb-3">Người bán</h6>
                                            <ul class="list-unstyled ml-2">
                                                <li><strong>Họ tên:</strong> ${o.seller.fullName}</li>
                                                <li><strong>SĐT:</strong> ${o.seller.phone}</li>
                                                <li><strong>Địa chỉ:</strong> ${o.seller.address}</li>
                                            </ul>
                                        </div>

                                        <!-- Cột PHẢI -->
                                        <div class="col-md-6 pl-md-4">
                                            <h6 class="text-uppercase mb-3">Thông tin đơn hàng</h6>
                                            <ul class="list-unstyled ml-2">
                                                <li><strong>Trạng thái:</strong> <span class="status-${o.status}">${o.status}</span></li>
                                                <li><strong>Ngày tạo:</strong> 
                                                    <fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                                </li>
                                                <li><strong>Ngày xử lý:</strong>
                                                    <c:choose>
                                                        <c:when test="${not empty o.processedDate}">
                                                            <fmt:formatDate value="${o.processedDate}" pattern="dd/MM/yyyy HH:mm" />
                                                        </c:when>
                                                        <c:otherwise>-</c:otherwise>
                                                    </c:choose>
                                                </li>
                                                <li><strong>Số lượng đặt:</strong> ${o.quantity} con</li>
                                                <li><strong>Tổng giá:</strong> 
                                                    <fmt:formatNumber value="${o.totalPrice}" type="number" groupingUsed="true" /> đ
                                                </li>
                                                <li><strong>Ghi chú:</strong> <c:out value="${o.note}" default="-" /></li>
                                            </ul>

                                            <h6 class="text-uppercase mt-4 mb-3">Lịch sử giao hàng</h6>
                                            <c:choose>
                                                <c:when test="${not empty o.deliveries}">
                                                    <div style="max-height: 250px; overflow-y: auto;">
                                                        <table class="table table-sm table-bordered">
                                                            <thead class="thead-light">
                                                                <tr>
                                                                    <th>#</th>
                                                                    <th>Ngày giao</th>
                                                                    <th>Số lượng</th>
                                                                    <th>Giá trị</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach var="d" items="${o.deliveries}" varStatus="i">
                                                                    <tr>
                                                                        <td>${i.index + 1}</td>
                                                                        <td><fmt:formatDate value="${d.createdAt}" pattern="dd/MM/yyyy" /></td>
                                                                        <td>${d.quantity}</td>
                                                                        <td><fmt:formatNumber value="${d.totalPrice}" type="number" groupingUsed="true" /></td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </c:when>
                                                <c:otherwise><p class="ml-2">Chưa có giao hàng nào.</p></c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <div class="text-right mb-2">
                <small>
                    Hiển thị từ <strong>${(page - 1) * 10 + 1}</strong> đến
                    <strong>
                        <c:choose>
                            <c:when test="${page * 10 < totalData}">
                                ${page * 10}
                            </c:when>
                            <c:otherwise>${totalData}</c:otherwise>
                        </c:choose>
                    </strong>
                    trong tổng <strong>${totalData}</strong> đơn hàng.
                </small>
            </div>

            <nav>
                <ul class="pagination justify-content-center">
                    <c:forEach begin="1" end="${totalPage}" var="i">
                        <li class="page-item ${i == page ? 'active' : ''}">
                            <form method="get" action="manage-orders" class="d-inline">
                                <input type="hidden" name="page" value="${i}" />
                                <input type="hidden" name="search" value="${param.search}" />
                                <input type="hidden" name="status" value="${param.status}" />
                                <input type="hidden" name="sort" value="${param.sort}" />
                                <input type="hidden" name="farmId" value="${param.farmId}" />
                                <button class="page-link">${i}</button>
                            </form>
                        </li>
                    </c:forEach>
                </ul>
            </nav>
        </div>

        <jsp:include page="component/footer.jsp" />
    </body>
</html>
