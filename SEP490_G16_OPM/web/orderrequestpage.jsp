<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Orders Request Page</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <div class="container mt-4">
            <h3 class="mb-4">Danh sách đơn hàng đang chờ</h3>

            <c:if test="${not empty param.msg}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    ${param.msg}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Đóng">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>

            <!-- Bộ lọc -->
            <form class="form-inline mb-3" method="get" action="orders-request">
                <select name="farmId" class="form-control form-control-sm mr-2">
                    <option value="">Tất cả trại</option>
                    <c:forEach var="farm" items="${farmList}">
                        <option value="${farm.farmID}" ${param.farmId == farm.farmID ? 'selected' : ''}>
                            ${farm.farmName}
                        </option>
                    </c:forEach>
                </select>

                <input type="text" name="search" class="form-control form-control-sm mr-2"
                       placeholder="Tìm mã đơn hoặc tên chào bán" value="${param.search}" />

                <input type="hidden" name="sort" value="${param.sort}"/>

                <button type="submit" class="btn btn-sm btn-success mr-2">Lọc</button>
                <a href="orders-request" class="btn btn-sm btn-outline-secondary">Bỏ lọc</a>
            </form>

            <!-- Cấu hình sort toggle -->
            <c:set var="currentSort" value="${param.sort}" />
            <c:set var="nextOrderIdSort" value="${currentSort == 'orderid_desc' ? 'orderid_asc' : 'orderid_desc'}" />
            <c:set var="nextQuantitySort" value="${currentSort == 'quantity_desc' ? 'quantity_asc' : 'quantity_desc'}" />
            <c:set var="nextPriceSort" value="${currentSort == 'price_desc' ? 'price_asc' : 'price_desc'}" />
            <c:set var="nextCreatedAtSort" value="${currentSort == 'createdAt_desc' ? 'createdAt_asc' : 'createdAt_desc'}" />

            <!-- Bảng -->
            <c:if test="${empty page or empty page.data}">
                <div class="alert alert-warning text-center">Không có đơn hàng nào đang chờ.</div>
            </c:if>

            <c:if test="${not empty page and not empty page.data}">
                <form action="confirm-order" method="post">
                    <table class="table table-bordered text-center">
                        <thead class="thead-dark">
                            <tr>
                                <th>
                                    Mã đơn
                                    <a href="orders-request?sort=${nextOrderIdSort}&search=${param.search}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'orderid_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'orderid_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Người mua</th>
                                <th>Chào bán</th>
                                <th>
                                    Số lượng
                                    <a href="orders-request?sort=${nextQuantitySort}&search=${param.search}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'quantity_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'quantity_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>
                                    Tổng giá
                                    <a href="orders-request?sort=${nextPriceSort}&search=${param.search}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'price_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'price_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Trạng thái</th>
                                <th>
                                    Ngày tạo
                                    <a href="orders-request?sort=${nextCreatedAtSort}&search=${param.search}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'createdAt_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'createdAt_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${page.data}">
                                <tr>
                                    <td>
                                        <a href="#" data-toggle="modal" data-target="#orderModal${o.orderID}">
                                            ${o.orderID}
                                        </a>
                                    </td>
                                    <td>${o.dealer.fullName}</td>
                                    <td>${o.pigsOffer.name}</td>
                                    <td>${o.quantity}</td>
                                    <td><fmt:formatNumber value="${o.totalPrice}" type="number" groupingUsed="true"/></td>
                                    <td>${o.status}</td>
                                    <td><fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td>
                                        <form action="confirm-order" method="post" class="d-inline">
                                            <input type="hidden" name="orderID" value="${o.orderID}" />
                                            <button type="submit" class="btn btn-success btn-sm">Xác nhận</button>
                                        </form>
                                        <form action="reject-order" method="post" class="d-inline ml-1">
                                            <input type="hidden" name="orderID" value="${o.orderID}" />
                                            <button type="submit" class="btn btn-outline-danger btn-sm"
                                                    onclick="return confirm('Bạn có chắc chắn muốn từ chối đơn hàng này không?')">
                                                Từ chối
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </form>
            </c:if>
        </div>

        <jsp:include page="component/footer.jsp" />
    </body>
</html>
