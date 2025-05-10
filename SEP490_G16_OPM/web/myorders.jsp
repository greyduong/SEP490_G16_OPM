<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Đơn hàng của tôi</title>
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
            <h3 class="mb-4">📦 Đơn hàng đã đặt</h3>

            <c:if test="${not empty param.msg}">
                <div class="alert alert-success text-center">${param.msg}</div>
            </c:if>

            <form class="form-inline mb-3" method="get" action="myorders">
                <input type="text" name="search" class="form-control form-control-sm mr-2" placeholder="Tìm theo tên chào bán" value="${param.search}" />
                <select name="status" class="form-control form-control-sm mr-2">
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
                <button type="submit" class="btn btn-sm btn-success mr-2">Lọc</button>
                <a href="myorders" class="btn btn-sm btn-outline-secondary">Bỏ lọc</a>
            </form>

            <c:set var="currentSort" value="${param.sort}" />
            <c:set var="nextQuantitySort" value="${currentSort == 'quantity_desc' ? 'quantity_asc' : 'quantity_desc'}" />
            <c:set var="nextPriceSort" value="${currentSort == 'totalprice_desc' ? 'totalprice_asc' : 'totalprice_desc'}" />
            <c:set var="nextCreatedAtSort" value="${currentSort == 'createdat_desc' ? 'createdat_asc' : 'createdat_desc'}" />
            <c:set var="nextProcessedDateSort" value="${currentSort == 'processeddate_desc' ? 'processeddate_asc' : 'processeddate_desc'}" />

            <c:if test="${empty page or empty page.data}">
                <div class="alert alert-warning text-center">Bạn chưa có đơn hàng nào.</div>
            </c:if>

            <c:if test="${not empty page and not empty page.data}">
                <div class="table-responsive">
                    <table class="table table-bordered text-center">
                        <thead class="thead-dark">
                            <tr>
                                <th>#</th>
                                <th>Chào bán</th>
                                <th>Số lượng
                                    <a href="myorders?sort=${nextQuantitySort}&search=${param.search}&status=${param.status}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'quantity_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'quantity_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Tổng giá
                                    <a href="myorders?sort=${nextPriceSort}&search=${param.search}&status=${param.status}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'totalprice_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'totalprice_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Trạng thái</th>
                                <th>Ngày tạo
                                    <a href="myorders?sort=${nextCreatedAtSort}&search=${param.search}&status=${param.status}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'createdat_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'createdat_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>
                                <th>Ngày xử lý
                                    <a href="myorders?sort=${nextProcessedDateSort}&search=${param.search}&status=${param.status}" class="btn btn-sm btn-outline-light ml-1">
                                        <c:choose>
                                            <c:when test="${currentSort == 'processeddate_asc'}">▲</c:when>
                                            <c:when test="${currentSort == 'processeddate_desc'}">▼</c:when>
                                            <c:otherwise>⇅</c:otherwise>
                                        </c:choose>
                                    </a>
                                </th>

                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${page.data}" varStatus="loop">
                                <tr>
                                    <td>
                                        <form action="view-order-detail" method="get" class="d-inline">
                                            <input type="hidden" name="id" value="${o.orderID}" />
                                            <button type="submit" class="btn btn-link p-0">${(page.pageNumber - 1) * page.pageSize + loop.index + 1}</button>
                                        </form>
                                    </td>
                                    <td>${o.pigsOffer.name}</td>
                                    <td>${o.quantity}</td>
                                    <td><fmt:formatNumber value="${o.totalPrice}" type="number" groupingUsed="true"/></td>
                                    <td class="status-${o.status}">
                                        <c:choose>
                                            <c:when test="${o.status == 'Pending'}">Chờ xác nhận</c:when>
                                            <c:when test="${o.status == 'Confirmed'}">Đã xác nhận
                                                <form action="deposit-order" method="post" class="d-inline ml-2">
                                                    <input type="hidden" name="orderId" value="${o.orderID}" />
                                                    <input type="hidden" name="search" value="${param.search}" />
                                                    <input type="hidden" name="status" value="${param.status}" />
                                                    <input type="hidden" name="sort" value="${param.sort}" />
                                                    <input type="hidden" name="page" value="${page.pageNumber}" />
                                                    <button type="submit" class="btn btn-sm btn-outline-primary">Đặt cọc</button>
                                                </form>
                                            </c:when>
                                            <c:when test="${o.status == 'Rejected'}">Đã từ chối</c:when>
                                            <c:when test="${o.status == 'Canceled'}">Đã hủy</c:when>
                                            <c:when test="${o.status == 'Processing'}">Đang xử lý</c:when>
                                            <c:when test="${o.status == 'Deposited'}">Đã đặt cọc</c:when>
                                            <c:when test="${o.status == 'Completed'}">Hoàn thành</c:when>
                                            <c:otherwise>${o.status}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty o.processedDate}">
                                                <fmt:formatDate value="${o.processedDate}" pattern="dd/MM/yyyy"/>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

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


                <!-- Phân trang -->
                <nav>
                    <ul class="pagination justify-content-center">
                        <c:forEach begin="1" end="${page.totalPage}" var="i">
                            <li class="page-item ${i == page.pageNumber ? 'active' : ''}">
                                <form method="get" action="myorders" class="d-inline">
                                    <input type="hidden" name="page" value="${i}" />
                                    <input type="hidden" name="search" value="${param.search}" />
                                    <input type="hidden" name="status" value="${param.status}" />
                                    <input type="hidden" name="sort" value="${param.sort}" />
                                    <button class="page-link">${i}</button>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </c:if>
        </div>

        <jsp:include page="component/footer.jsp" />
        <script>
            // Xử lý submit
            document.querySelectorAll("form").forEach(form => {
                form.addEventListener("submit", function () {
                    const overlay = document.createElement("div");
                    overlay.id = "loading-overlay"; // Gán ID để dễ xử lý khi quay lại

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

            // Khi quay lại trang bằng nút Back, loại bỏ overlay
            window.addEventListener("pageshow", function (event) {
                const overlay = document.getElementById("loading-overlay");
                if (overlay) {
                    overlay.remove();
                }
            });
        </script>

    </body>
</html>
