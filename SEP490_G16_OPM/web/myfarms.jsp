<%-- 
    Document   : myfarms.jsp
    Created on : Apr 23, 2025, 2:28:53 PM
    Author     : duong
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- Check if the user is logged in and has the Seller role --%>
<c:if test="${empty sessionScope.user}">
    <script>
        window.location.href = "login-register.jsp"; // Redirect to login if not logged in
    </script>
</c:if>

<c:if test="${sessionScope.user.roleID != 4}">
    <script>
        window.location.href = "home"; // Redirect if the user is not a Seller
    </script>
</c:if>    

<!DOCTYPE html>
<html lang="zxx">

    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Ogani Template">
        <meta name="keywords" content="Ogani, unica, creative, html">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Trang trại</title>

        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css2?family=Cairo:wght@200;300;400;600;900&display=swap" rel="stylesheet">

        <!-- Css Styles -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="css/elegant-icons.css" type="text/css">
        <link rel="stylesheet" href="css/nice-select.css" type="text/css">
        <link rel="stylesheet" href="css/jquery-ui.min.css" type="text/css">
        <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
        <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">

    </head>

    <body>

        <jsp:include page="component/header.jsp"></jsp:include>

            <!-- Breadcrumb Section Begin -->
            <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-12 text-center">
                            <div class="breadcrumb__text">
                                <h2>Trang trại</h2>
                                <div class="breadcrumb__option">
                                    <a href="home">Trang chủ</a>
                                    <span>Trang trại</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <!-- Breadcrumb Section End -->

            <!-- Farm Management Table Begin -->
            <section class="spad" style="padding-top: 30px;">
                <div class="container">
                    <h4 class="mb-4">Danh sách trang trại của bạn</h4>

                    <!-- Display error or success message -->
                <c:if test="${not empty msg}">
                    <div class="alert alert-info" role="alert">
                        ${msg}  <!-- This will display the message set in the controller -->
                    </div>
                </c:if>

                <c:choose>
                    <c:when test="${not empty pagedFarms and not empty pagedFarms.data}">
                        <div class="table-responsive">

                            <c:set var="currentSort" value="${param.sort}" />
                            <c:set var="nextOfferSort" value="${currentSort == 'offer_asc' ? 'offer_desc' : 'offer_asc'}" />
                            <c:set var="nextOrderSort" value="${currentSort == 'order_asc' ? 'order_desc' : 'order_asc'}" />
                            <c:set var="nextDateSort" value="${currentSort == 'date_asc' ? 'date_desc' : 'date_asc'}" />

                            <div class="text-left mb-3">
                                <a href="createFarm" class="btn btn-sm btn-success">
                                    <i class="fa fa-plus mr-1"></i> Tạo trang trại
                                </a>
                            </div>

                            <form class="form-inline mb-3" method="get" action="ViewMyFarmsController">
                                <!-- Ô tìm kiếm -->
                                <input type="text" name="search" class="form-control form-control-sm mr-2"
                                       placeholder="Tìm tên trang trại"
                                       value="${param.search != null ? param.search : ''}" />

                                <!-- Dropdown chọn trạng thái -->
                                <select name="status" class="form-control form-control-sm mr-2">
                                    <option value="">Tất cả trạng thái</option>
                                    <option value="Active" ${param.status == 'Active' ? 'selected' : ''}>Hoạt động</option>
                                    <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Đang chờ</option>
                                </select>

                                <!-- Hidden giữ sort -->
                                <input type="hidden" name="sort" value="${param.sort}" />

                                <button type="submit" class="btn btn-sm btn-success mr-2">Tìm</button>
                                <a href="ViewMyFarmsController" class="btn btn-sm btn-outline-secondary">Bỏ lọc</a>
                            </form>



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
                                            <a href="ViewMyFarmsController?sort=${currentSort == 'offer_asc' ? 'offer_desc' : 'offer_asc'}&search=${param.search}&status=${param.status}"
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
                                            <a href="ViewMyFarmsController?sort=${currentSort == 'order_asc' ? 'order_desc' : 'order_asc'}&search=${param.search}&status=${param.status}"
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
                                            <a href="ViewMyFarmsController?sort=${currentSort == 'date_asc' ? 'date_desc' : 'date_asc'}&search=${param.search}&status=${param.status}"
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
                                            <td>${loop.index + 1 + (pagedFarms.pageNumber - 1) * pagedFarms.pageSize}</td>
                                            <td>
                                                <a href="#" class="text-primary" data-toggle="modal" data-target="#farmModal${farm.farmID}">
                                                    ${farm.farmName}
                                                </a>
                                            </td>
                                            <td>${farm.location}</td>
                                            <td>${farm.offerCount}</td>
                                            <td>${farm.orderCount}</td>
                                            <td>${farm.status == 'Active' ? 'Hoạt động' : farm.status == 'Pending' ? 'Đang chờ' : 'Không xác định'}</td>
                                            <td><fmt:formatDate value="${farm.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td>
                                                <a href="editFarm?id=${farm.farmID}&page=${pagedFarms.pageNumber}&sort=${param.sort}&search=${param.search}&status=${param.status}" class="btn btn-sm btn-primary">Sửa</a>
                                                <a href="deleteFarm?id=${farm.farmID}&page=${pagedFarms.pageNumber}&sort=${param.sort}&search=${param.search}&status=${param.status}" class="btn btn-sm btn-danger"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa trang trại này?');">Xóa</a>
                                            </td>
                                        </tr>

                                        <c:forEach var="farm" items="${pagedFarms.data}" varStatus="loop">
                                            <!-- Modal hiển thị thông tin chi tiết -->
                                        <div class="modal fade" id="farmModal${farm.farmID}" tabindex="-1" role="dialog" aria-labelledby="farmModalLabel${farm.farmID}" aria-hidden="true">
                                            <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="farmModalLabel${farm.farmID}">Thông tin chi tiết trang trại</h5>
                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                            <span aria-hidden="true">&times;</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <p><strong>Tên trang trại:</strong> ${farm.farmName}</p>
                                                        <p><strong>Vị trí:</strong> ${farm.location}</p>
                                                        <p><strong>Mô tả:</strong> ${farm.description}</p>
                                                        <p><strong>Số lượng chào bán:</strong> ${farm.offerCount}</p>
                                                        <p><strong>Số lượng đơn hàng:</strong> ${farm.orderCount}</p>
                                                        <p><strong>Trạng thái:</strong> ${farm.status == 'Active' ? 'Hoạt động' : farm.status == 'Pending' ? 'Đang chờ' : 'Không xác định'}</p>
                                                        <p><strong>Ngày tạo:</strong> <fmt:formatDate value="${farm.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>

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
                                           href="ViewMyFarmsController?page=${i}&sort=${param.sort}&search=${param.search}&status=${param.status}">
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


        <jsp:include page="component/footer.jsp"></jsp:include>

        <!-- Js Plugins -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.nice-select.min.js"></script>
        <script src="js/jquery-ui.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>
        <script src="js/bootstrap.min.js"></script> 

    </body>

</html>