<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chỉnh sửa hồ sơ - Chợ Heo Trực Tuyến</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <jsp:include page="component/library.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <style>
            input[readonly], .form-control {
                color: black !important;
            }
        </style>
    </head>
    <body>

        <c:if test="${empty sessionScope.user}">
            <script>window.location.href = "login-register.jsp";</script>
        </c:if>

        <jsp:include page="component/header.jsp" />

        <c:if test="${not empty sessionScope.success}">
            <script>alert("${sessionScope.success}");</script>
            <c:remove var="success" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.error}">
            <script>alert("${sessionScope.error}");</script>
            <c:remove var="error" scope="session"/>
        </c:if>

        <!-- Hiển thị thông tin hồ sơ (readonly) -->
        <section class="checkout spad">
            <div class="container">
                <div class="checkout__form">
                    <h4>Thông tin cá nhân</h4>
                    <div class="row">
                        <div class="col-lg-8 col-md-6">
                            <div class="checkout__input">
                                <p>Họ và tên</p>
                                <input type="text" value="${sessionScope.user.fullName}" readonly>
                            </div>
                            <div class="checkout__input">
                                <p>Email</p>
                                <input type="text" value="${sessionScope.user.email}" readonly>
                            </div>
                            <div class="checkout__input">
                                <p>Số điện thoại</p>
                                <input type="text" value="${sessionScope.user.phone}" readonly>
                            </div>
                            <div class="checkout__input">
                                <p>Địa chỉ</p>
                                <input type="text" value="${sessionScope.user.address}" readonly>
                            </div>
                            <button type="button" class="site-btn mt-3" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                                Chỉnh sửa hồ sơ
                            </button>
                            <button type="button" class="site-btn mt-3 ms-2" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                                Đổi mật khẩu
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Modal hiển thị form cập nhật -->
        <div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form id="updateForm" action="profile" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editProfileLabel">Chỉnh sửa thông tin cá nhân</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="fullname" class="form-label">Họ và tên</label>
                                <input type="text" class="form-control" name="fullname" value="${sessionScope.user.fullName}" required>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" value="${sessionScope.user.email}" required>
                            </div>
                            <div class="mb-3">
                                <label for="phone" class="form-label">Số điện thoại</label>
                                <input type="text" class="form-control" name="phone" value="${sessionScope.user.phone}" required>
                            </div>
                            <div class="mb-3">
                                <label for="address" class="form-label">Địa chỉ</label>
                                <input type="text" class="form-control" name="address" value="${sessionScope.user.address}" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Modal đổi mật khẩu -->
        <div class="modal fade" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="change-password" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title" id="changePasswordLabel">Đổi mật khẩu</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label">Mật khẩu hiện tại</label>
                                <input type="password" name="currentPassword" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Mật khẩu mới</label>
                                <input type="password" name="newPassword" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Nhập lại mật khẩu mới</label>
                                <input type="password" name="confirmNewPassword" class="form-control" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="component/footer.jsp" />

    </body>
</html>
