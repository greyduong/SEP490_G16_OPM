<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Trang trại | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>

        <!-- Farm Management Table Begin -->
        <section class="spad" style="padding-top: 30px;">
            <div class="container">
                <div class="d-flex justify-content-between mb-3">
                    <h4>Danh sách trang trại của bạn</h4>
                    <a href="createFarm" class="btn btn-success">+ Tạo trang trại</a>
                </div>
                <!-- Display error or success message -->
                <c:if test="${not empty msg}">
                    <div class="alert alert-info" role="alert">
                        ${msg}  <!-- This will display the message set in the controller -->
                    </div>
                </c:if>

                <form class="form-inline mb-3" method="get" action="my-farms">
                    <!-- Ô tìm kiếm -->
                    <input type="text" name="search" class="form-control form-control-sm mr-2"
                           placeholder="Tìm tên trang trại"
                           value="${param.search != null ? param.search : ''}" />

                    <!-- Dropdown chọn trạng thái -->
                    <select name="status" class="form-control form-control-sm mr-2">
                        <option value="">Tất cả trạng thái</option>
                        <option value="Active" ${param.status == 'Active' ? 'selected' : ''}>Hoạt động</option>
                        <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Đang chờ</option>
                        <option value="Inactive" ${param.status == 'Inactive' ? 'selected' : ''}>Không hoạt động</option>
                        <option value="Cancel" ${param.status == 'Cancel' ? 'selected' : ''}>Bị từ chối</option>
                        <option value="Banned" ${param.status == 'Banned' ? 'selected' : ''}>Bị cấm</option>
                    </select>

                    <!-- Hidden giữ sort -->
                    <input type="hidden" name="sort" value="${param.sort}" />

                    <button type="submit" class="btn btn-sm btn-success mr-2">Tìm</button>
                    <a href="my-farms" class="btn btn-sm btn-outline-secondary">Bỏ lọc</a>
                </form>

                <c:choose>
                    <c:when test="${not empty pagedFarms and not empty pagedFarms.data}">

                        <div class="table-responsive">

                            <c:set var="currentSort" value="${param.sort}" />
                            <c:set var="nextOfferSort" value="${currentSort == 'offer_desc' ? 'offer_asc' : 'offer_desc'}" />
                            <c:set var="nextOrderSort" value="${currentSort == 'order_desc' ? 'order_asc' : 'order_desc'}" />
                            <c:set var="nextDateSort" value="${currentSort == 'date_desc' ? 'date_asc' : 'date_desc'}" />

                            <table class="table table-bordered table-striped">
                                <thead class="thead-light">
                                    <tr>
                                        <th>#</th>
                                        <th>
                                            Tên trang trại
                                        </th>
                                        <th>
                                            Vị trí
                                        </th>
                                        <th>
                                            Chào bán
                                            <a href="my-farms?sort=${nextOfferSort}&search=${param.search}&status=${param.status}"
                                               class="btn btn-sm btn-outline-secondary ml-1">
                                                <c:choose>
                                                    <c:when test="${currentSort == 'offer_asc'}">▲</c:when>
                                                    <c:when test="${currentSort == 'offer_desc'}">▼</c:when>
                                                    <c:otherwise>⇅</c:otherwise>
                                                </c:choose>
                                            </a>
                                        </th>
                                        <th>
                                            Đặt hàng
                                            <a href="my-farms?sort=${nextOrderSort}&search=${param.search}&status=${param.status}"
                                               class="btn btn-sm btn-outline-secondary ml-1">
                                                <c:choose>
                                                    <c:when test="${currentSort == 'order_asc'}">▲</c:when>
                                                    <c:when test="${currentSort == 'order_desc'}">▼</c:when>
                                                    <c:otherwise>⇅</c:otherwise>
                                                </c:choose>
                                            </a>
                                        </th>
                                        <th>Trạng thái</th>
                                        <th>
                                            Ngày tạo
                                            <a href="my-farms?sort=${nextDateSort}&search=${param.search}&status=${param.status}"
                                               class="btn btn-sm btn-outline-secondary ml-1">
                                                <c:choose>
                                                    <c:when test="${currentSort == 'date_asc'}">▲</c:when>
                                                    <c:when test="${currentSort == 'date_desc'}">▼</c:when>
                                                    <c:otherwise>⇅</c:otherwise>
                                                </c:choose>
                                            </a>
                                        </th>
                                        <th>Hành động</th>
                                    </tr>
                                </thead>


                                <tbody>
                                    <c:forEach var="farm" items="${pagedFarms.data}" varStatus="loop">
                                        <tr>
                                            <td>${(pagedFarms.pageNumber - 1) * pagedFarms.pageSize + loop.index + 1}</td>
                                            <td>
                                                <a href="#" class="text-primary" data-toggle="modal" data-target="#farmModal${farm.farmID}">
                                                    ${farm.farmName}
                                                </a>
                                            </td>
                                            <td>${farm.location}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${farm.offerCount > 0}">
                                                        <a href="my-offers?farmId=${farm.farmID}" class="text-primary">
                                                            ${farm.offerCount}
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${farm.offerCount}
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${farm.orderCount}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${farm.status == 'Active'}">Hoạt động</c:when>
                                                    <c:when test="${farm.status == 'Pending'}">Đang chờ</c:when>
                                                    <c:when test="${farm.status == 'Inactive'}">Không hoạt động</c:when>
                                                    <c:when test="${farm.status == 'Cancel'}">Bị từ chối</c:when>
                                                    <c:when test="${farm.status == 'Banned'}">Bị cấm</c:when>
                                                    <c:otherwise>Không xác định</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatDate value="${farm.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td>
                                                <c:if test="${farm.status != 'Pending' && farm.status != 'Banned' && farm.status != 'Cancel'}">
                                                    <a href="updateFarm?id=${farm.farmID}&page=${pagedFarms.pageNumber}&sort=${param.sort}&search=${param.search}&status=${param.status}" class="btn btn-sm btn-primary">Sửa</a>
                                                </c:if>
                                                <c:if test="${farm.status == 'Active' and canDeactive[farm.farmID] }">
                                                    <a href="deactivateFarm?id=${farm.farmID}&page=${pagedFarms.pageNumber}&sort=${param.sort}&search=${param.search}&status=${param.status}" class="btn btn-sm btn-warning" onclick="return confirm('Bạn có chắc chắn muốn dừng hoạt động trang trại này?');">Dừng hoạt động</a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <!-- Pagination -->
                        <c:set var="from" value="${(pagedFarms.pageNumber - 1) * pagedFarms.pageSize + 1}" />
                        <c:set var="to" value="${from + pagedFarms.data.size() - 1}" />
                        <div class="text-left mb-2">
                            Từ ${from} đến ${to} trong ${pagedFarms.totalData} Trang trại.
                        </div>

                        <nav aria-label="Page navigation" class="mt-3">
                            <ul class="pagination justify-content-center">
                                <c:forEach begin="1" end="${pagedFarms.totalPage}" var="i">
                                    <li class="page-item ${i == pagedFarms.pageNumber ? 'active' : ''}">
                                        <a class="page-link"
                                           href="my-farms?page=${i}&sort=${param.sort}&search=${param.search}&status=${param.status}">
                                            ${i}
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning text-center">Bạn chưa có trang trại nào.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
        <!-- Farm Management Table End -->

        <!-- Modal hiển thị chi tiết cho từng farm -->
        <c:forEach var="farm" items="${pagedFarms.data}">
            <div class="modal fade" id="farmModal${farm.farmID}" tabindex="-1" role="dialog" aria-labelledby="farmModalLabel${farm.farmID}" aria-hidden="true">
                <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="farmModalLabel${farm.farmID}">Thông tin chi tiết trang trại</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Đóng">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <table class="table table-bordered">
                                <tr>
                                    <th>Ảnh</th>
                                    <td>
                                        <img src="${farm.imageURL}" alt="Ảnh trang trại" style="max-width: 100%; height: auto;" />
                                    </td>
                                </tr>

                                <tr><th>Tên trang trại</th><td>${farm.farmName}</td></tr>
                                <tr><th>Vị trí</th><td>${farm.location}</td></tr>
                                <tr><th>Mô tả</th><td>${farm.description}</td></tr>
                                <tr><th>Số lượng chào bán</th><td>${farm.offerCount}</td></tr>
                                <tr><th>Số lượng đơn hàng</th><td>${farm.orderCount}</td></tr>
                                <tr>
                                    <th>Trạng thái</th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${farm.status == 'Active'}">Hoạt động</c:when>
                                            <c:when test="${farm.status == 'Pending'}">Đang chờ</c:when>
                                            <c:when test="${farm.status == 'Inactive'}">Không hoạt động</c:when>
                                            <c:when test="${farm.status == 'Cancel'}">Bị từ chối</c:when>
                                            <c:when test="${farm.status == 'Banned'}">Bị cấm</c:when>
                                            <c:otherwise>Không xác định</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr><th>Ghi chú</th><td>${farm.note}</td></tr>
                                <tr><th>Ngày tạo</th><td><fmt:formatDate value="${farm.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td></tr>
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
