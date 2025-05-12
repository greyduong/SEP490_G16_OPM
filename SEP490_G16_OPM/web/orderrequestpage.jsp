<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Orders Request Page</title>
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

        <div class="container mt-4">
            <h3 class="mb-4">Danh sách đơn hàng đang chờ</h3>

            <c:if test="${not empty msg}">
                <div class="alert alert-info text-center">${msg}</div>
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
            <c:set var="nextOrderSort" value="${currentSort == 'orderid_desc' ? 'orderid_asc' : 'orderid_desc'}" />
            <c:set var="nextQuantitySort" value="${currentSort == 'quantity_desc' ? 'quantity_asc' : 'quantity_desc'}" />
            <c:set var="nextPriceSort" value="${currentSort == 'price_desc' ? 'price_asc' : 'price_desc'}" />
            <c:set var="nextCreatedAtSort" value="${currentSort == 'createdAt_desc' ? 'createdAt_asc' : 'createdAt_desc'}" />

            <!-- Bảng -->
            <c:if test="${empty page or empty page.data}">
                <div class="alert alert-warning text-center">Không có đơn hàng nào đang chờ.</div>
            </c:if>

            <c:if test="${not empty page and not empty page.data}">
                <table class="table table-bordered text-center">
                    <thead class="thead-dark">
                        <tr>
                            <th>
                                Mã
                                <a href="orders-request?sort=${nextOrderSort}&search=${param.search}&farmId=${param.farmId}" class="btn btn-sm btn-outline-light ml-1">
                                    <c:choose>
                                        <c:when test="${currentSort == 'orderid_asc'}">▲</c:when>
                                        <c:when test="${currentSort == 'orderid_desc'}">▼</c:when>
                                        <c:otherwise>⇅</c:otherwise>
                                    </c:choose>
                                </a>
                            </th>

                            <th>Dealer</th>
                            <th>Offer</th>
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
                            <th style="min-width: 140px;">Trạng thái</th>
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
                        <c:forEach var="o" items="${page.data}" varStatus="loop">
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
                                <td><fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td>
                                    <c:if test="${o.status == 'Pending'}">
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
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Hiển thị tổng dòng -->
                <div class="text-right">
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

                <!-- Phân trang -->
                <nav>
                    <ul class="pagination justify-content-center">
                        <c:forEach begin="1" end="${page.totalPage}" var="i">
                            <li class="page-item ${i == page.pageNumber ? 'active' : ''}">
                                <form method="get" action="orders-request" class="d-inline">
                                    <input type="hidden" name="page" value="${i}" />
                                    <input type="hidden" name="farmId" value="${param.farmId}" />
                                    <input type="hidden" name="search" value="${param.search}" />
                                    <input type="hidden" name="sort" value="${param.sort}" />
                                    <button class="page-link">${i}</button>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>

                <!-- Modal hiển thị chi tiết đơn hàng -->
                <c:forEach var="o" items="${page.data}">
                    <div class="modal fade" id="orderModal${o.orderID}" tabindex="-1" role="dialog" aria-labelledby="orderModalLabel${o.orderID}" aria-hidden="true">
                        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">Chi tiết đơn hàng #${o.orderID}</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Đóng">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <!-- Bên trái: Thông tin chào bán -->
                                        <div class="col-md-6 border-right">
                                            <h6><strong>Thông tin chào bán</strong></h6>
                                            <div class="mb-3">
                                                <img src="${o.pigsOffer.imageURL}" class="img-fluid img-thumbnail" alt="Ảnh chào bán"/>
                                            </div>
                                            <p><strong>Tên chào bán:</strong> ${o.pigsOffer.name}</p>
                                            <p><strong>Trang trại:</strong> ${o.farm.farmName} - ${o.farm.location}</p>
                                            <p><strong>Mô tả:</strong><br/>${o.pigsOffer.description}</p>
                                            <p><strong>Số lượng tối thiểu:</strong> ${o.pigsOffer.minQuantity}</p>
                                            <p><strong>Giá lẻ:</strong> 
                                                <fmt:formatNumber value="${o.pigsOffer.retailPrice}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                            </p>
                                            <p><strong>Tiền cọc:</strong> 
                                                <fmt:formatNumber value="${o.pigsOffer.minDeposit}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                            </p>
                                            <p><strong>Tổng giá chào bán:</strong> 
                                                <fmt:formatNumber value="${o.pigsOffer.totalOfferPrice}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                            </p>
                                        </div>

                                        <!-- Bên phải: Thông tin đơn hàng -->
                                        <div class="col-md-6">
                                            <h6><strong>Thông tin đơn hàng</strong></h6>
                                            <table class="table table-sm table-bordered">
                                                <tr>
                                                    <th>Người mua</th>
                                                    <td>${o.dealer.fullName}</td>
                                                </tr>
                                                <tr>
                                                    <th>Số lượng đặt</th>
                                                    <td>${o.quantity}</td>
                                                </tr>
                                                <tr>
                                                    <th>Tổng giá đơn</th>
                                                    <td>
                                                        <fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>Trạng thái</th>
                                                    <td>${o.status}</td>
                                                </tr>
                                                <tr>
                                                    <th>Ngày tạo</th>
                                                    <td>
                                                        <fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="modal-footer">
                                                <c:if test="${o.status == 'Pending'}">
                                                    <form action="confirm-order" method="post" class="d-inline">
                                                        <input type="hidden" name="orderID" value="${o.orderID}" />
                                                        <button type="submit" class="btn btn-success">Xác nhận</button>
                                                    </form>

                                                    <form action="reject-order" method="post" class="d-inline ml-2">
                                                        <input type="hidden" name="orderID" value="${o.orderID}" />
                                                        <button type="submit" class="btn btn-outline-danger"
                                                                onclick="return confirm('Bạn có chắc chắn muốn từ chối đơn hàng này không?')">
                                                            Từ chối
                                                        </button>
                                                    </form>
                                                </c:if>
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

            </c:if>
        </div>

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

        <jsp:include page="component/footer.jsp" />
    </body>
</html>