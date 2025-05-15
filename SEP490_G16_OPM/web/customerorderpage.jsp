<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh sách đơn hàng</title>
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
            <h3 class="mb-4">Danh sách đơn hàng của khách hàng</h3>

            <c:if test="${not empty msg}">
                <div class="alert alert-info text-center">${msg}</div>
            </c:if>

            <!-- Filter form -->
            <form method="get" action="customer-orders" class="form-inline mb-3">
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
                <a href="customer-orders" class="btn btn-outline-secondary">Bỏ lọc</a>
            </form>

            <c:set var="currentSort" value="${param.sort}" />
            <c:set var="nextOrderIdSort" value="${currentSort == 'orderid_desc' ? 'orderid_asc' : 'orderid_desc'}" />
            <c:set var="nextQuantitySort" value="${currentSort == 'quantity_desc' ? 'quantity_asc' : 'quantity_desc'}" />
            <c:set var="nextPriceSort" value="${currentSort == 'totalprice_desc' ? 'totalprice_asc' : 'totalprice_desc'}" />
            <c:set var="nextCreatedAtSort" value="${currentSort == 'createdat_desc' ? 'createdat_asc' : 'createdat_desc'}" />
            <c:set var="nextProcessedSort" value="${currentSort == 'processeddate_desc' ? 'processeddate_asc' : 'processeddate_desc'}" />

            <c:if test="${empty page.data}">
                <p class="text-center">Không có đơn hàng nào phù hợp.</p>
            </c:if>

            <c:if test="${not empty page.data}">
                <div class="table-responsive">
                    <table class="table table-bordered text-center">
                        <thead class="thead-dark">
                            <tr>
                                <th>STT</th>
                                <th>Mã đơn
                                    <a href="customer-orders?sort=${nextOrderIdSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'orderid_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'orderid_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Chào bán</th>
                                <th>Số lượng
                                    <a href="customer-orders?sort=${nextQuantitySort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'quantity_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'quantity_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Tổng giá
                                    <a href="customer-orders?sort=${nextPriceSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
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
                                    <a href="customer-orders?sort=${nextCreatedAtSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'createdat_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'createdat_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Ngày xử lý
                                    <a href="customer-orders?sort=${nextProcessedSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
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
                            <c:forEach var="o" items="${page.data}" varStatus="loop">
                                <tr>
                                    <td>${(page.pageNumber - 1) * page.pageSize + loop.index + 1}</td>
                                    <td><a href="customer-order-details?id=${o.orderID}">${o.orderID}</a></td>
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
                                        <c:if test="${o.status == 'Deposited' || o.status == 'In Delivery'}">
                                            <form action="customer-order-details" method="get">
                                                <input type="hidden" name="id" value="${o.orderID}" />
                                                <input type="hidden" name="openCreateDelivery" value="true" />
                                                <button type="submit" class="btn btn-sm btn-primary">Giao hàng</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <div class="text-right mb-2">
                <small>
                    Hiển thị từ <strong>${(page.pageNumber - 1) * page.pageSize + 1}</strong> đến
                    <strong>
                        <c:choose>
                            <c:when test="${page.pageNumber * page.pageSize < page.totalData}">
                                ${page.pageNumber * page.pageSize}
                            </c:when>
                            <c:otherwise>${page.totalData}</c:otherwise>
                        </c:choose>
                    </strong>
                    trong tổng <strong>${page.totalData}</strong> đơn hàng.
                </small>
            </div>

            <nav>
                <ul class="pagination justify-content-center">
                    <c:forEach begin="1" end="${page.totalPage}" var="i">
                        <li class="page-item ${i == page.pageNumber ? 'active' : ''}">
                            <form method="get" action="customer-orders" class="d-inline">
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
