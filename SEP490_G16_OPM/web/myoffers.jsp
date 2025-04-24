<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chào bán | Online Pig Market</title>
        <jsp:include page="component/library.jsp"/>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>

        <!-- Offer List Section Begin -->
        <section class="product spad" style="padding-top: 30px;">
            <div class="container">
                <div class="d-flex justify-content-between mb-3">
                    <h4>Danh sách chào bán của bạn</h4>
                    <a href="createOffer" class="btn btn-success">+ Tạo chào bán mới</a>
                </div>

                <c:if test="${not empty msg}">
                    <div class="alert alert-success">${msg}</div>
                </c:if>

                <form class="form-inline mb-3" method="get" action="my-offers">

                    <!-- Dropdown chọn trang trại -->
                    <select name="farmId" class="form-control form-control-sm mr-2">
                        <option value="">Tất cả trại</option>
                        <c:forEach var="farm" items="${myFarms}">
                            <option value="${farm.farmID}" ${param.farmId == farm.farmID ? 'selected' : ''}>
                                ${farm.farmName}
                            </option>
                        </c:forEach>
                    </select>

                    <!-- Ô tìm kiếm theo tên -->
                    <input type="text" name="search" class="form-control form-control-sm mr-2"
                           placeholder="Tìm tên chào bán"
                           value="${param.search != null ? param.search : ''}" />

                    <!-- Dropdown chọn trạng thái -->
                    <select name="status" class="form-control form-control-sm mr-2">
                        <option value="">Tất cả trạng thái</option>
                        <option value="Available" ${param.status == 'Available' ? 'selected' : ''}>Còn hàng</option>
                        <option value="Unavailable" ${param.status == 'Unavailable' ? 'selected' : ''}>Ngưng bán</option>
                    </select>

                    <!-- Giữ sort hiện tại -->
                    <input type="hidden" name="sort" value="${param.sort}"/>

                    <!-- Nút tìm kiếm -->
                    <button type="submit" class="btn btn-sm btn-success mr-2">Tìm</button>

                    <!-- Nút bỏ lọc -->
                    <a href="my-offers" class="btn btn-sm btn-outline-secondary">Bỏ lọc</a>
                </form>

                <c:choose>
                    <c:when test="${not empty page and not empty page.data}">

                        <div class="table-responsive" style="overflow-x: auto; max-width: 100%;">

                            <c:set var="currentSort" value="${param.sort}" />
                            <c:set var="nextQuantitySort" value="${currentSort == 'quantity_desc' ? 'quantity_asc' : 'quantity_desc'}" />
                            <c:set var="nextPriceSort" value="${currentSort == 'totalprice_desc' ? 'totalprice_asc' : 'totalprice_desc'}" />
                            <c:set var="nextOrderSort" value="${currentSort == 'order_desc' ? 'order_asc' : 'order_desc'}" />
                            <c:set var="nextEndDateSort" value="${currentSort == 'enddate_desc' ? 'enddate_asc' : 'enddate_desc'}" />                          

                            <table class="table table-bordered w-100" style="min-width: 1000px;">
                                <thead class="thead-dark text-center">
                                    <tr>
                                        <th style="width: 5%;">ID</th>
                                        <th style="width: 15%;">Tên</th>
                                        <th style="width: 15%;">Trại</th>
                                        <th style="width: 9%;">SL 
                                            <a href="my-offers?sort=${nextQuantitySort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                                <c:choose>
                                                    <c:when test="${currentSort == 'quantity_asc'}">▲</c:when>
                                                    <c:when test="${currentSort == 'quantity_desc'}">▼</c:when>
                                                    <c:otherwise>⇅</c:otherwise>
                                                </c:choose>
                                            </a>
                                        </th>
                                        <th style="width: 15%;">Tổng giá<br/>(x1000 VNĐ)
                                            <a href="my-offers?sort=${nextPriceSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                                <c:choose>
                                                    <c:when test="${currentSort == 'totalprice_asc'}">▲</c:when>
                                                    <c:when test="${currentSort == 'totalprice_desc'}">▼</c:when>
                                                    <c:otherwise>⇅</c:otherwise>
                                                </c:choose>
                                            </a>
                                        </th>
                                        <th style="width: 9%;">Đơn
                                            <a href="my-offers?sort=${nextOrderSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                                <c:choose>
                                                    <c:when test="${currentSort == 'order_asc'}">▲</c:when>
                                                    <c:when test="${currentSort == 'order_desc'}">▼</c:when>
                                                    <c:otherwise>⇅</c:otherwise>
                                                </c:choose>
                                            </a>
                                        </th>
                                        <th style="width: 12%;">Kết thúc
                                            <a href="my-offers?sort=${nextEndDateSort}&search=${param.search}&status=${param.status}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                                <c:choose>
                                                    <c:when test="${currentSort == 'enddate_asc'}">▲</c:when>
                                                    <c:when test="${currentSort == 'enddate_desc'}">▼</c:when>
                                                    <c:otherwise>⇅</c:otherwise>
                                                </c:choose>
                                            </a>
                                        </th>
                                        <th style="width: 10%;">Trạng thái</th>
                                        <th style="width: 10%;">Hành động</th>
                                    </tr>
                                </thead>


                                <tbody>
                                    <c:forEach var="offer" items="${page.data}">
                                        <tr class="text-center align-middle">
                                            <td>${offer.offerID}</td>
                                            <td class="text-left" style="max-width: 150px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" title="${offer.name}">
                                                <a href="#" title="${offer.name}" data-toggle="modal" data-target="#offerModal${offer.offerID}">
                                                    ${offer.name}
                                                </a>
                                            </td>
                                            <td class="text-left" style="max-width: 150px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">                                        
                                                ${offer.farm.farmName}
                                            </td>
                                            <td>${offer.quantity}</td>
                                            <td>
                                                <fmt:formatNumber value="${offer.totalOfferPrice / 1000}" type="number" pattern="#,##0"/>
                                            </td>
                                            <td>${offer.orderCount}</td>
                                            <td><fmt:formatDate value="${offer.endDate}" pattern="dd/MM/yyyy"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${offer.status == 'Available'}">
                                                        <span class="text-success small" style="font-size: 0.9rem;">🟢 Hoạt động</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-danger small" style="font-size: 0.9rem;">🔴 Không hoạt động</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="edit-offer?id=${offer.offerID}" class="btn btn-sm btn-primary mb-1">Sửa</a>
                                                <a href="delete-offer?id=${offer.offerID}" class="btn btn-sm btn-danger"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa chào bán này?');">Xóa</a>
                                            </td>
                                        </tr>                              
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="mt-2 text-right">
                                <small>
                                    Từ <strong>${(page.pageNumber - 1) * page.pageSize + 1}</strong>
                                    đến 
                                    <strong>
                                        <c:choose>
                                            <c:when test="${page.pageNumber * page.pageSize < page.totalData}">
                                                ${page.pageNumber * page.pageSize}
                                            </c:when>
                                            <c:otherwise>
                                                ${page.totalData}
                                            </c:otherwise>
                                        </c:choose>
                                    </strong>
                                    trong <strong>${page.totalData}</strong> Chào bán
                                </small>
                            </div>

                        </div>

                        <!-- Pagination -->
                        <nav>
                            <ul class="pagination justify-content-center">
                                <c:forEach var="i" begin="1" end="${page.totalPage}">
                                    <li class="page-item ${i == page.pageNumber ? 'active' : ''}">
                                        <form action="my-offers" method="get" class="d-inline">
                                            <input type="hidden" name="page" value="${i}">
                                            <input type="hidden" name="sort" value="${param.sort}">
                                            <input type="hidden" name="search" value="${param.search}">
                                            <input type="hidden" name="status" value="${param.status}">
                                            <input type="hidden" name="farmId" value="${param.farmId}">
                                            <button type="submit" class="page-link">${i}</button>
                                        </form>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning text-center">Bạn chưa có chào bán nào.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
        <!-- Offer List Section End -->

        <!-- Modal hiển thị chi tiết cho từng offer -->
        <c:forEach var="offer" items="${page.data}">
            <div class="modal fade" id="offerModal${offer.offerID}" tabindex="-1" role="dialog" aria-labelledby="offerModalLabel${offer.offerID}" aria-hidden="true">
                <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Chi tiết chào bán #${offer.offerID}</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Đóng">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <table class="table table-bordered table-striped">
                                <div class="modal-body">
                                    <div class="container-fluid">
                                        <div class="row">
                                            <!-- Cột bên trái -->
                                            <div class="col-md-6">
                                                <h6 class="mb-3"><strong>🐷 Thông tin cơ bản</strong></h6>
                                                <p>
                                                    <img src="${offer.imageURL}" alt="Hình ảnh heo" class="img-thumbnail mt-2" style="max-width: 100%;">
                                                </p>
                                                <p><strong>ID:</strong> ${offer.offerID}</p>
                                                <p><strong>Tên:</strong> ${offer.name}</p>
                                                <p><strong>Giống:</strong> ${offer.pigBreed}</p>
                                                <p><strong>Danh mục:</strong> ${offer.category.name}</p>
                                                <p><strong>Trại:</strong> ${offer.farm.farmName} - ${offer.farm.location}</p>
                                                <p><strong>Mô tả:</strong><br/>${offer.description}</p>
                                            </div>
                                            <!-- Cột bên phải -->
                                            <div class="col-md-6">
                                                <h6 class="mb-3"><strong>💰 Giá & Số lượng</strong></h6>
                                                <p><strong>Số lượng:</strong> ${offer.quantity}</p>
                                                <p><strong>Tối thiểu:</strong> ${offer.minQuantity}</p>
                                                <p><strong>Tiền cọc:</strong>
                                                    <fmt:formatNumber value="${offer.minDeposit}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                                                </p>
                                                <p><strong>Giá lẻ:</strong>
                                                    <fmt:formatNumber value="${offer.retailPrice}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                                                </p>
                                                <p><strong class="text-danger">Tổng giá:</strong>
                                                    <span class="text-danger font-weight-bold">
                                                        <fmt:formatNumber value="${offer.totalOfferPrice}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                                                    </span>
                                                </p>
                                                <h6 class="mt-4 mb-3"><strong>📅 Trạng thái & Thời gian</strong></h6>
                                                <p><strong>Trạng thái:</strong>
                                                    <c:choose>
                                                        <c:when test="${offer.status == 'Available'}">
                                                            <span class="text-success small" style="font-size: 0.9rem;">🟢 Hoạt động</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-danger small" style="font-size: 0.9rem;">🔴 Không hoạt động</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>

                                                <p><strong>Đơn hàng:</strong> ${offer.orderCount}</p>
                                                <p><strong>Ngày bắt đầu:</strong> <fmt:formatDate value="${offer.startDate}" pattern="dd/MM/yyyy"/></p>
                                                <p><strong>Ngày kết thúc:</strong> <fmt:formatDate value="${offer.endDate}" pattern="dd/MM/yyyy"/></p>
                                                <p><strong>Ngày tạo:</strong> <fmt:formatDate value="${offer.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>


        <jsp:include page="component/footer.jsp"/>
    </body>
</html>
