<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chỉnh sửa hồ sơ - Chợ Heo Trực Tuyến</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Giao diện CSS & JS từ thư viện chung -->
        <jsp:include page="component/library.jsp" />
    </head>
    <body>

        <!-- Kiểm tra đăng nhập -->
        <c:if test="${empty sessionScope.user}">
            <script>
                window.location.href = "login-register.jsp";
            </script>
        </c:if>

        <!-- Header -->
        <jsp:include page="component/header.jsp" />

        <!-- Hiển thị thông báo thành công -->
        <c:if test="${not empty sessionScope.success}">
            <script>alert("${sessionScope.success}");</script>
            <c:remove var="success" scope="session"/>
        </c:if>

        <!-- Hiển thị thông báo lỗi -->
        <c:if test="${not empty sessionScope.error}">
            <script>alert("${sessionScope.error}");</script>
            <c:remove var="error" scope="session"/>
        </c:if>

        <!-- Bắt đầu phần chỉnh sửa hồ sơ -->
        <section class="checkout spad">
            <div class="container">
                <div class="checkout__form">
                    <h4>Chỉnh sửa thông tin cá nhân</h4>
                    <form action="profile" method="post">
                        <div class="row">
                            <div class="col-lg-8 col-md-6">
                                <div class="checkout__input">
                                    <p>Họ và tên<span>*</span></p>
                                    <input type="text" name="fullname" value="${sessionScope.user.fullName}" required>
                                </div>
                                <div class="checkout__input">
                                    <p>Email<span>*</span></p>
                                    <input type="email" name="email" value="${sessionScope.user.email}" required>
                                </div>
                                <div class="checkout__input">
                                    <p>Số điện thoại<span>*</span></p>
                                    <input type="text" name="phone" value="${sessionScope.user.phone}" required>
                                </div>
                                <div class="checkout__input">
                                    <p>Địa chỉ<span>*</span></p>
                                    <input type="text" name="address" value="${sessionScope.user.address}" required>
                                </div>
                                <button type="submit" class="site-btn mt-3">Cập nhật thông tin</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
        <!-- Kết thúc phần chỉnh sửa hồ sơ -->

        <!-- Footer -->
        <jsp:include page="component/footer.jsp" />
    </body>
</html>
