<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Xem đơn - Chợ Heo Trực Tuyến</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <!-- Bảng hiển thị đơn đăng ký -->
        <section class="product-details spad">
            <div class="container">
                <h4 class="mb-3">Đơn đăng ký của tôi</h4>

                <!-- Thanh tìm kiếm và lọc -->
                <form class="form-row mb-3" method="GET" action="application">
                    <div class="col-md-3 mb-2">
                        <input type="text" name="keyword" class="form-control"
                               placeholder="Tìm theo mục đích..." value="${param.keyword}">
                    </div>
                    <div class="col-md-3 mb-2">
                        <select name="status" class="form-control" onchange="this.form.submit()">
                            <option value="">-- Tất cả trạng thái --</option>
                            <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Đang chờ xử lý</option>
                            <option value="Confirmed" ${param.status == 'Confirmed' ? 'selected' : ''}>Đã phê duyệt</option>
                            <option value="Rejected" ${param.status == 'Rejected' ? 'selected' : ''}>Đã từ chối</option>
                            <option value="Canceled" ${param.status == 'Canceled' ? 'selected' : ''}>Đã hủy</option>
                        </select>
                    </div>
                    <div class="col-md-3 mb-2">
                        <select name="sortByDate" class="form-control" onchange="this.form.submit()">
                            <option value="">-- Sắp xếp theo ngày gửi --</option>
                            <option value="newest" ${param.sortByDate == 'newest' ? 'selected' : ''}>Mới nhất trước</option>
                            <option value="oldest" ${param.sortByDate == 'oldest' ? 'selected' : ''}>Cũ nhất trước</option>
                        </select>
                    </div>
                    <div class="col-md-3 mb-2">
                        <button type="submit" class="btn btn-outline-primary w-100">Tìm kiếm</button>
                    </div>
                </form>

                <!-- Bảng kết quả -->
                <table class="table table-bordered table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>Mục đích</th>
                            <th>Ngày gửi</th>
                            <th>Tệp đính kèm</th>
                            <th>Trạng thái</th>
                            <th>Ngày xử lý</th>
                            <th>Phản hồi</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="application" items="${applicationList}">
                            <tr>
                                <td>${application.content}</td>
                                <td><fmt:formatDate value="${application.sentAt}" pattern="dd/MM/yyyy"/></td>
                                <td>
                                    <c:if test="${not empty application.file}">
                                        <a href="${pageContext.request.contextPath}/${application.file}" target="_blank">Xem ảnh</a>
                                    </c:if>
                                    <c:if test="${empty application.file}">
                                        Không có ảnh đính kèm
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${application.status == 'Đang chờ xử lý'}">Đang chờ xử lý</c:when>
                                        <c:when test="${application.status == 'Đã phê duyệt'}">Đã phê duyệt</c:when>
                                        <c:when test="${application.status == 'Đã từ chối'}">Đã từ chối</c:when>
                                        <c:when test="${application.status == 'Đã hủy'}">Đã hủy</c:when>
                                        <c:otherwise>${application.status}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${not empty application.processingDate}">
                                        <fmt:formatDate value="${application.processingDate}" pattern="dd/MM/yyyy"/>
                                    </c:if>
                                    <c:if test="${empty application.processingDate}">
                                        -
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty application.reply}">
                                            ${application.reply}
                                        </c:when>
                                        <c:otherwise>
                                            Chưa có phản hồi
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty applicationList}">
                            <tr>
                                <td colspan="6" class="text-center text-muted">Không có đơn đăng ký nào.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>

                <!-- PHÂN TRANG -->
                <c:if test="${totalPages > 1}">
                    <nav>
                        <ul class="pagination justify-content-center">
                            <c:if test="${currentPage > 1}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="application?page=${currentPage - 1}&keyword=${param.keyword}&status=${param.status}&sortByDate=${param.sortByDate}">
                                        « Trước
                                    </a>
                                </li>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link"
                                       href="application?page=${i}&keyword=${param.keyword}&status=${param.status}&sortByDate=${param.sortByDate}">
                                        ${i}
                                    </a>
                                </li>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="application?page=${currentPage + 1}&keyword=${param.keyword}&status=${param.status}&sortByDate=${param.sortByDate}">
                                        Sau »
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </section>

        <jsp:include page="component/footer.jsp" />
    </body>
</html>
