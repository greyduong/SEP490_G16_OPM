<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý chào bán | Online Pig Market</title>
        <jsp:include page="component/library.jsp"/>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>
        <section class="product spad" style="padding-top: 30px;">
            <div class="container">
                <h4 class="mb-3">Danh sách tất cả chào bán</h4>
                <c:if test="${not empty param.msg}">
                    <div class="alert alert-info text-center font-weight-bold">${param.msg}</div>
                </c:if>
                <form class="form-inline mb-3" method="get" action="manage-offers">
                    <select name="farmId" class="form-control form-control-sm mr-2">
                        <option value="">Tất cả trại</option>
                        <c:forEach var="farm" items="${allFarms}">
                            <option value="${farm.farmID}" ${param.farmId == farm.farmID ? 'selected' : ''}>
                                ${farm.farmName}
                            </option>
                        </c:forEach>
                    </select>
                    <input type="text" name="search" class="form-control form-control-sm mr-2" placeholder="Tìm tên chào bán" value="${param.search}" />
                    <select name="status" class="form-control form-control-sm mr-2">
                        <option value="">Tất cả trạng thái</option>
                        <option value="Available" ${param.status == 'Available' ? 'selected' : ''}>Còn hàng</option>
                        <option value="Unavailable" ${param.status == 'Unavailable' ? 'selected' : ''}>Ngưng bán</option>
                        <option value="Upcoming" ${param.status == 'Upcoming' ? 'selected' : ''}>Sắp mở bán</option>
                        <option value="Banned" ${param.status == 'Banned' ? 'selected' : ''}>Bị cấm</option>
                    </select>
                    <select name="sort" class="form-control form-control-sm mr-2">
                        <option value="">Sắp xếp theo</option>
                        <option value="quantity_asc" ${param.sort == 'quantity_asc' ? 'selected' : ''}>SL tăng</option>
                        <option value="quantity_desc" ${param.sort == 'quantity_desc' ? 'selected' : ''}>SL giảm</option>
                        <option value="totalprice_asc" ${param.sort == 'totalprice_asc' ? 'selected' : ''}>Giá tăng</option>
                        <option value="totalprice_desc" ${param.sort == 'totalprice_desc' ? 'selected' : ''}>Giá giảm</option>
                        <option value="order_asc" ${param.sort == 'order_asc' ? 'selected' : ''}>Đơn tăng</option>
                        <option value="order_desc" ${param.sort == 'order_desc' ? 'selected' : ''}>Đơn giảm</option>
                        <option value="enddate_asc" ${param.sort == 'enddate_asc' ? 'selected' : ''}>Ngày kết thúc ↑</option>
                        <option value="enddate_desc" ${param.sort == 'enddate_desc' ? 'selected' : ''}>Ngày kết thúc ↓</option>
                    </select>
                    <button type="submit" class="btn btn-sm btn-success mr-2">Tìm</button>
                    <a href="manage-offers" class="btn btn-sm btn-outline-secondary">Bỏ lọc</a>
                </form>

                <c:choose>
                    <c:when test="${not empty page and not empty page.data}">
                        <table class="table table-bordered text-center">
                            <thead class="thead-dark">
                                <tr>
                                    <th>#</th>
                                    <th>Tên</th>
                                    <th>Trại</th>
                                    <th>SL</th>
                                    <th>Tổng giá (x1000 ₫)</th>
                                    <th>Đơn</th>
                                    <th>Hạn bán</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="offer" items="${page.data}" varStatus="loop">
                                    <tr>
                                        <td>${(page.pageNumber - 1) * page.pageSize + loop.index + 1}</td>
                                        <td><a href="#" data-toggle="modal" data-target="#offerModal${offer.offerID}">${offer.name}</a></td>
                                        <td>${offer.farm.farmName}</td>
                                        <td>${offer.quantity} con</td>
                                        <td><fmt:formatNumber value="${offer.totalOfferPrice / 1000}" type="number" pattern="#,#00"/></td>
                                        <td>${offer.orderCount}</td>
                                        <td><fmt:formatDate value="${offer.endDate}" pattern="dd/MM/yyyy"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${offer.status == 'Available'}">
                                                    <span class="text-success">🟢 Hoạt động</span>
                                                </c:when>
                                                <c:when test="${offer.status == 'Upcoming'}">
                                                    <span class="text-warning">🕓 Sắp mở bán</span>
                                                </c:when>
                                                <c:when test="${offer.status == 'Unavailable'}">
                                                    <span class="text-danger">🔴 Ngưng bán</span>
                                                </c:when>
                                                <c:when test="${offer.status == 'Banned'}">
                                                    <span class="text-dark">🚫 Bị cấm</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">-</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${offer.status == 'Banned'}">
                                                    <form method="post" action="process-offer">
                                                        <input type="hidden" name="id" value="${offer.offerID}" />
                                                        <input type="hidden" name="action" value="unban" />
                                                        <input type="hidden" name="page" value="${page.pageNumber}" />
                                                        <input type="hidden" name="farmId" value="${param.farmId}" />
                                                        <input type="hidden" name="search" value="${param.search}" />
                                                        <input type="hidden" name="status" value="${param.status}" />
                                                        <input type="hidden" name="sort" value="${param.sort}" />
                                                        <button type="submit" class="btn btn-sm btn-outline-primary" onclick="return confirm('Cho phép bán lại chào bán này?');">✅ Mở cấm</button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <form method="post" action="process-offer" class="form-inline justify-content-center">
                                                        <input type="hidden" name="id" value="${offer.offerID}" />
                                                        <input type="hidden" name="action" value="ban" />
                                                        <input type="text" name="note" class="form-control form-control-sm mr-2" placeholder="Lý do cấm.." required style="max-width: 140px;" />
                                                        <input type="hidden" name="page" value="${page.pageNumber}" />
                                                        <input type="hidden" name="farmId" value="${param.farmId}" />
                                                        <input type="hidden" name="search" value="${param.search}" />
                                                        <input type="hidden" name="status" value="${param.status}" />
                                                        <input type="hidden" name="sort" value="${param.sort}" />
                                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Xác nhận BAN chào bán này?');">🚫 Cấm</button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="mt-2 text-right">
                            <small>
                                Từ <strong>${(page.pageNumber - 1) * page.pageSize + 1}</strong>
                                đến <strong>
                                    <c:choose>
                                        <c:when test="${page.pageNumber * page.pageSize < page.totalData}">
                                            ${page.pageNumber * page.pageSize}
                                        </c:when>
                                        <c:otherwise>
                                            ${page.totalData}
                                        </c:otherwise>
                                    </c:choose>
                                </strong>
                                trong <strong>${page.totalData}</strong> chào bán
                            </small>
                        </div>

                        <nav>
                            <ul class="pagination justify-content-center">
                                <c:forEach var="i" begin="1" end="${page.totalPage}">
                                    <li class="page-item ${i == page.pageNumber ? 'active' : ''}">
                                        <form method="get" action="manage-offers" class="d-inline">
                                            <input type="hidden" name="page" value="${i}">
                                            <input type="hidden" name="farmId" value="${param.farmId}">
                                            <input type="hidden" name="search" value="${param.search}">
                                            <input type="hidden" name="status" value="${param.status}">
                                            <input type="hidden" name="sort" value="${param.sort}">
                                            <button type="submit" class="page-link">${i}</button>
                                        </form>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>

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
                                            <div class="container-fluid">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <h6 class="mb-3"><strong>🐷 Thông tin cơ bản</strong></h6>
                                                        <img src="${offer.imageURL}" alt="Hình ảnh heo" class="img-thumbnail mt-2" style="max-width: 100%;">
                                                        <p><strong>ID:</strong> ${offer.offerID}</p>
                                                        <p><strong>Tên:</strong> ${offer.name}</p>
                                                        <p><strong>Giống:</strong> ${offer.pigBreed}</p>
                                                        <p><strong>Danh mục:</strong> ${offer.category.name}</p>
                                                        <p><strong>Trại:</strong> ${offer.farm.farmName} - ${offer.farm.location}</p>
                                                        <p><strong>Mô tả:</strong><br/>${offer.description}</p>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <h6 class="mb-3"><strong>💰 Giá & Số lượng</strong></h6>
                                                        <p><strong>Số lượng:</strong> ${offer.quantity} con</p>
                                                        <p><strong>Tối thiểu:</strong> ${offer.minQuantity} con</p>
                                                        <p><strong>Tiền cọc:</strong> <fmt:formatNumber value="${offer.minDeposit}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/></p>
                                                        <p><strong>Giá lẻ:</strong> <fmt:formatNumber value="${offer.retailPrice}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/></p>
                                                        <p><strong class="text-danger">Tổng giá:</strong> <span class="text-danger font-weight-bold">
                                                                <fmt:formatNumber value="${offer.totalOfferPrice}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                                                            </span></p>
                                                        <h6 class="mt-4 mb-3"><strong>📅 Trạng thái & Thời gian</strong></h6>
                                                        <p><strong>Trạng thái:</strong>
                                                            <c:choose>
                                                                <c:when test="${offer.status == 'Available'}">
                                                                    <span class="text-success">🟢 Hoạt động</span>
                                                                </c:when>
                                                                <c:when test="${offer.status == 'Upcoming'}">
                                                                    <span class="text-warning">🕓 Sắp mở bán</span>
                                                                </c:when>
                                                                <c:when test="${offer.status == 'Unavailable'}">
                                                                    <span class="text-danger">🔴 Ngưng bán</span>
                                                                </c:when>
                                                                <c:when test="${offer.status == 'Banned'}">
                                                                    <span class="text-dark">🚫 Bị cấm</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="text-muted">-</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </p>
                                                        <p><strong>Ghi chú:</strong> ${offer.note}</p>
                                                        <p><strong>Đơn hàng:</strong> ${offer.orderCount}</p>
                                                        <p><strong>Ngày bắt đầu:</strong> <fmt:formatDate value="${offer.startDate}" pattern="dd/MM/yyyy"/></p>
                                                        <p><strong>Ngày kết thúc:</strong> <fmt:formatDate value="${offer.endDate}" pattern="dd/MM/yyyy"/></p>
                                                        <p><strong>Ngày tạo:</strong> <fmt:formatDate value="${offer.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning text-center">Không có chào bán nào.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
        <jsp:include page="component/footer.jsp"/>
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
                    </script
                        </body>
            </html>