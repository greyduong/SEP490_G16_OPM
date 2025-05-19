<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý đơn đăng ký</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <script src="js/jquery-3.3.1.min.js"></script>
    </head>
    <body>
        <jsp:include page="component/library.jsp" />
        <jsp:include page="component/header.jsp" />
        <!-- Modal xử lý đơn -->
        <div class="modal fade" id="processModal" tabindex="-1" role="dialog" aria-labelledby="processModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <form action="manage-application" method="post" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="processModalLabel">Xử lý đơn đăng ký</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Đóng">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="applicationID" id="modalApplicationID">

                        <div class="form-group">
                            <label>Nội dung đơn</label>
                            <textarea class="form-control" id="modalContent" rows="2" readonly></textarea>
                        </div>

                        <div class="form-group">
                            <label for="reply">Phản hồi</label>
                            <textarea class="form-control" name="reply" id="reply" rows="3" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="status">Trạng thái</label>
                            <select class="form-control" name="status" id="status" required>
                                <option value="Đã phê duyệt">Phê duyệt</option>
                                <option value="Đã từ chối">Từ chối</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-success">Gửi xử lý</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                    </div>
                </form>
            </div>
        </div>

        <c:if test="${not empty sessionScope.successMsg}">
            <div class="alert alert-success">${sessionScope.successMsg}</div>
            <c:remove var="successMsg" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.errorMsg}">
            <div class="alert alert-danger">${sessionScope.errorMsg}</div>
            <c:remove var="errorMsg" scope="session"/>
        </c:if>

        <section class="product-details spad">
            <div class="container">
                <h4 class="mb-3">Danh sách đơn đăng ký</h4>

                <form method="GET" action="manage-application" class="form-inline mb-3">
                    <input type="text" name="keyword" class="form-control mr-2" placeholder="Tìm theo nội dung..."
                           value="${param.keyword}" />
                    <select name="status" class="form-control mr-2" onchange="this.form.submit()">
                        <option value="">-- Tất cả trạng thái --</option>
                        <option value="Đang chờ xử lý" ${param.status == 'Đang chờ xử lý' ? 'selected' : ''}>Đang chờ xử lý</option>
                        <option value="Đã phê duyệt" ${param.status == 'Đã phê duyệt' ? 'selected' : ''}>Đã phê duyệt</option>
                        <option value="Đã từ chối" ${param.status == 'Đã từ chối' ? 'selected' : ''}>Đã từ chối</option>
                    </select>
                    <select name="sort" class="form-control mr-2" onchange="this.form.submit()">
                        <option value="">-- Sắp xếp theo ngày gửi --</option>
                        <option value="asc" ${param.sort == 'asc' ? 'selected' : ''}>Cũ nhất</option>
                        <option value="desc" ${param.sort == 'desc' ? 'selected' : ''}>Mới nhất</option>
                    </select>
                    <button type="submit" class="btn btn-outline-primary">Tìm kiếm</button>
                </form>

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
                                <!-- Mục đích -->
                                <td>${application.content}</td>

                                <!-- Ngày gửi -->
                                <td><fmt:formatDate value="${application.sentAt}" pattern="dd/MM/yyyy" /></td>

                                <!-- Tệp đính kèm -->
                                <td>
                                    <c:if test="${not empty application.file}">
                                        <a href="${application.file}" target="_blank">Xem ảnh</a>
                                    </c:if>
                                    <c:if test="${empty application.file}">
                                        Không có ảnh đính kèm
                                    </c:if>
                                </td>

                                <!-- Trạng thái -->
                                <td>${application.status}</td>

                                <!-- Ngày xử lý -->
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty application.processingDate}">
                                            <fmt:formatDate value="${application.processingDate}" pattern="dd/MM/yyyy" />
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>

                                <!-- Phản hồi -->
                                <td>
                                    <c:choose>
                                        <c:when test="${application.status == 'Đang chờ xử lý'}">
                                            <button type="button" class="btn btn-sm btn-primary"
                                                    data-toggle="modal"
                                                    data-target="#processModal"
                                                    data-id="${application.applicationID}"
                                                    data-content="${application.content}">
                                                Xử lý
                                            </button>
                                        </c:when>
                                        <c:when test="${not empty application.reply}">
                                            ${application.reply}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Chưa có phản hồi</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty applicationList}">
                            <tr><td colspan="6" class="text-center text-muted">Không có đơn đăng ký nào.</td></tr>
                        </c:if>
                    </tbody>
                </table>

                <c:if test="${totalPages > 1}">
                    <nav aria-label="Page navigation">
                        <ul class="pagination justify-content-center">
                            <c:forEach begin="1" end="${totalPages}" var="p">
                                <li class="page-item ${p == currentPage ? 'active' : ''}">
                                    <a class="page-link"
                                       href="manage-application?page=${p}&keyword=${paramKeyword}&status=${paramStatus}&sort=${paramSort}">${p}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </section>

        <jsp:include page="component/footer.jsp" />
        <script src="js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>
        <script>
                        $('#processModal').on('show.bs.modal', function (event) {
                            var button = $(event.relatedTarget);
                            var appId = button.data('id');
                            var content = button.data('content');

                            var modal = $(this);
                            modal.find('#modalApplicationID').val(appId);
                            modal.find('#modalContent').val(content);
                            modal.find('#reply').val('');
                            modal.find('#status').val('Đã phê duyệt');
                        });
        </script>
    </body>
</html>