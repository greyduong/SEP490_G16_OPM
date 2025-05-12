<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý phê duyệt trang trại | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <section class="spad" style="padding-top: 30px;">
            <div class="container">
                <h4 class="mb-4">Danh sách trang trại chờ xác nhận</h4>

                <c:if test="${not empty param.msg}">
                    <div class="alert alert-info">${param.msg}</div>
                </c:if>


                <form class="form-inline mb-3" method="get" action="pending-farms">
                    <input type="text" name="search" class="form-control form-control-sm mr-2"
                           placeholder="Tìm tên trang trại"
                           value="${param.search != null ? param.search : ''}" />
                    <input type="hidden" name="sort" value="${param.sort}" />
                    <button type="submit" class="btn btn-sm btn-success mr-2">Tìm</button>
                    <a href="pending-farms" class="btn btn-sm btn-outline-secondary">Bỏ lọc</a>
                </form>

                <c:choose>
                    <c:when test="${not empty pagedFarms and not empty pagedFarms.data}">
                        <div class="table-responsive">
                            <c:set var="currentSort" value="${param.sort}" />
                            <c:set var="nextDateSort" value="${currentSort == 'date_desc' ? 'date_asc' : 'date_desc'}" />

                            <table class="table table-bordered table-striped">
                                <thead class="thead-light">
                                    <tr>
                                        <th>#</th>
                                        <th>Tên trang trại</th>
                                        <th>Vị trí</th>
                                        <th>Người bán</th>
                                        <th>Ngày tạo
                                            <a href="pending-farms?sort=${nextDateSort}&search=${param.search}"
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
                                                <a href="#" class="text-primary" data-toggle="modal"
                                                   data-target="#farmModal${farm.farmID}">${farm.farmName}</a>
                                            </td>
                                            <td>${farm.location}</td>
                                            <td>
                                                <a href="#" class="text-info" data-toggle="modal"
                                                   data-target="#farmModal${farm.farmID}">${farm.seller.fullName}</a>
                                            </td>
                                            <td><fmt:formatDate value="${farm.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td>
                                                <form action="process-farm" method="post" style="display:inline;">
                                                    <input type="hidden" name="id" value="${farm.farmID}" />
                                                    <input type="hidden" name="action" value="approve" />
                                                    <input type="hidden" name="page" value="${pagedFarms.pageNumber}" />
                                                    <input type="hidden" name="sort" value="${param.sort}" />
                                                    <input type="hidden" name="search" value="${param.search}" />
                                                    <button type="submit" class="btn btn-sm btn-success">Xác nhận</button>
                                                </form>

                                                <button type="button" class="btn btn-sm btn-danger" data-toggle="modal" data-target="#rejectModal${farm.farmID}">Từ chối</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

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
                                           href="pending-farms?page=${i}&sort=${param.sort}&search=${param.search}">${i}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>

                        <c:forEach var="farm" items="${pagedFarms.data}">
                            <!-- Modal xem chi tiết -->
                            <div class="modal fade" id="farmModal${farm.farmID}" tabindex="-1" role="dialog" aria-hidden="true">
                                <div class="modal-dialog modal-lg modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Chi tiết trang trại & Người bán</h5>
                                            <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row">
                                                <!-- Farm Info -->
                                                <div class="col-md-6">
                                                    <h6>Thông tin trang trại</h6>
                                                    <table class="table table-bordered">
                                                        <tr><th>Tên</th><td>${farm.farmName}</td></tr>
                                                        <tr><th>Vị trí</th><td>${farm.location}</td></tr>
                                                        <tr><th>Mô tả</th><td>${farm.description}</td></tr>
                                                        <tr><th>Trạng thái</th><td>${farm.status}</td></tr>
                                                        <tr><th>Ngày tạo</th><td><fmt:formatDate value="${farm.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td></tr>
                                                        <tr><th>Ảnh</th><td><img src="${farm.imageURL}" style="max-width:100%"/></td></tr>
                                                    </table>
                                                </div>
                                                <!-- Seller Info -->
                                                <div class="col-md-6">
                                                    <h6>Thông tin người bán</h6>
                                                    <table class="table table-bordered">
                                                        <tr><th>Họ tên</th><td>${farm.seller.fullName}</td></tr>
                                                        <tr><th>Email</th><td>${farm.seller.email}</td></tr>
                                                        <tr><th>Điện thoại</th><td>${farm.seller.phone}</td></tr>
                                                        <tr><th>Địa chỉ</th><td>${farm.seller.address}</td></tr>
                                                        <tr><th>Tổng số trang trại</th><td>${farm.seller.totalFarms}</td></tr>
                                                        <tr><th>Tổng số chào bán</th><td>${farm.seller.totalOffers}</td></tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Modal từ chối -->
                            <div class="modal fade" id="rejectModal${farm.farmID}" tabindex="-1" role="dialog" aria-hidden="true">
                                <div class="modal-dialog modal-lg modal-dialog-centered">
                                    <form action="process-farm" method="post" class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Từ chối trang trại</h5>
                                            <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row">
                                                <!-- Farm Info -->
                                                <div class="col-md-6">
                                                    <h6>Thông tin trang trại</h6>
                                                    <table class="table table-bordered">
                                                        <tr><th>Tên</th><td>${farm.farmName}</td></tr>
                                                        <tr><th>Vị trí</th><td>${farm.location}</td></tr>
                                                        <tr><th>Mô tả</th><td>${farm.description}</td></tr>
                                                        <tr><th>Ngày tạo</th><td><fmt:formatDate value="${farm.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td></tr>
                                                        <tr><th>Ảnh</th><td><img src="${farm.imageURL}" style="max-width:100%"/></td></tr>
                                                    </table>
                                                </div>
                                                <!-- Seller Info -->
                                                <div class="col-md-6">
                                                    <h6>Thông tin người bán</h6>
                                                    <table class="table table-bordered">
                                                        <tr><th>Họ tên</th><td>${farm.seller.fullName}</td></tr>
                                                        <tr><th>Email</th><td>${farm.seller.email}</td></tr>
                                                        <tr><th>Điện thoại</th><td>${farm.seller.phone}</td></tr>
                                                        <tr><th>Địa chỉ</th><td>${farm.seller.address}</td></tr>
                                                    </table>
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="form-group mt-3">
                                                <label><strong>Lý do từ chối:</strong></label>
                                                <textarea name="note" class="form-control" rows="4" required></textarea>
                                            </div>
                                            <input type="hidden" name="id" value="${farm.farmID}" />
                                            <input type="hidden" name="action" value="reject" />
                                            <input type="hidden" name="page" value="${pagedFarms.pageNumber}" />
                                            <input type="hidden" name="sort" value="${param.sort}" />
                                            <input type="hidden" name="search" value="${param.search}" />
                                        </div>
                                        <div class="modal-footer">
                                            <button type="submit" class="btn btn-danger">Xác nhận từ chối</button>
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>

                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning text-center">Không có trang trại nào cần phê duyệt.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
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
