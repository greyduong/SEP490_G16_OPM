<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    model.User user = (model.User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login-register.jsp");
        return;
    }
    if (user.getRoleID() != 2 && user.getRoleID() != 3) {
        response.sendRedirect("login-register.jsp?error=access-denied");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Xem đơn đăng ký - Chợ Heo Trực Tuyến</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <script src="js/jquery-3.3.1.min.js"></script>
    </head>
    <body>  

        <jsp:include page="component/library.jsp" />
        <jsp:include page="component/header.jsp" />

        <c:if test="${not empty sessionScope.successMsg}">
            <div class="alert alert-success">${sessionScope.successMsg}</div>
            <c:remove var="successMsg" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.errorMsg}">
            <div class="alert alert-danger">${sessionScope.errorMsg}</div>
            <c:remove var="errorMsg" scope="session"/>
        </c:if>

        <!-- Danh sách đơn -->
        <section class="product-details spad">
            <div class="container">
                <h4 class="mb-3">Danh sách đơn đăng ký</h4>
                <table class="table table-bordered table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>Mục đích</th>
                            <th>Ngày gửi</th>
                            <th>Tệp đính kèm</th>
                            <th>Trạng thái</th>
                            <th>Ngày xử lý</th>
                            <th>Phản hồi</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="application" items="${applicationList}">
                            <tr>
                                <td>${application.content}</td>
                                <td><fmt:formatDate value="${application.sentAt}" pattern="dd/MM/yyyy" /></td>
                                <td>
                                    <c:if test="${not empty application.file}">
                                        <a href="${application.file}" target="_blank">${application.file}</a>
                                    </c:if>
                                    <c:if test="${empty application.file}">
                                        Không có tệp đính kèm
                                    </c:if>
                                </td>
                                <td>${application.status}</td>
                                <td><fmt:formatDate value="${application.processingDate}" pattern="dd/MM/yyyy" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty application.reply}">
                                            ${application.reply}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Chưa có phản hồi</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${application.status eq 'Pending'}">
                                        <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#applicationModal" data-id="${application.applicationID}" data-status="${application.status}" data-content="${application.content}">Xử lý</button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty applicationList}">
                            <tr>
                                <td colspan="7" class="text-center text-muted">Không có đơn đăng ký nào.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>

        <!-- Modal -->
        <div class="modal fade" id="applicationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Xử lý đơn đăng ký</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Đóng">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="applicationForm" action="ProcessApplication" method="POST">
                            <input type="hidden" id="applicationId" name="applicationId" />

                            <p><strong>Mục đích:</strong> <span id="appContent"></span></p>

                            <div class="form-group">
                                <label for="replyText">Phản hồi:</label>
                                <textarea class="form-control" id="replyText" name="reply" rows="3" placeholder="Nhập phản hồi..."></textarea>
                            </div>

                            <div class="form-group d-flex align-items-center">
                                <label for="statusSelect" class="mr-2 mb-0" style="min-width: 60px;">Trạng thái:</label>
                                <select class="form-control" id="statusSelect" name="action"
                                        style="height: 38px; padding: 6px 12px; line-height: 1.5; appearance: none;">
                                    <option value="approve">Phê duyệt</option>
                                    <option value="reject">Từ chối</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <button type="submit" class="btn btn-primary">Gửi phản hồi</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="component/footer.jsp" />

        <script src="js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>

        <script>
            // Khi hiển thị modal, cập nhật dữ liệu
            $('#applicationModal').on('show.bs.modal', function (event) {
                const button = $(event.relatedTarget);
                const appId = button.data('id');
                const appContent = button.data('content');

                $('#applicationId').val(appId);
                $('#appContent').text(appContent);
            });

            // Xác nhận trước khi gửi
            document.getElementById("applicationForm").addEventListener("submit", function (e) {
                e.preventDefault();

                const status = document.getElementById("statusSelect").value;
                const confirmText = status === 'approve'
                        ? "Bạn có chắc chắn muốn PHÊ DUYỆT đơn đăng ký này?"
                        : "Bạn có chắc chắn muốn TỪ CHỐI đơn đăng ký này?";

                if (confirm(confirmText)) {
                    this.submit();
                }
            });
        </script>

    </body>
</html>
