<%-- 
    Document   : editfarm
    Created on : Apr 23, 2025, 8:02:01 PM
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

            <section class="spad" style="padding-top: 30px;">
                <div class="container">
                    <h4 class="mb-4">Chỉnh sửa trang trại</h4>
                <c:if test="${not empty msg}">
                    <div class="alert alert-info" role="alert">
                        ${msg}
                    </div>
                </c:if>
                <form action="editFarm" method="post">
                    <input type="hidden" name="farmId" value="${farm.farmID}" />
                    <input type="hidden" name="page" value="${page != null ? page : param.page}" />
                    <input type="hidden" name="sort" value="${sort != null ? sort : param.sort}" />
                    <input type="hidden" name="search" value="${search != null ? search : param.search}" />
                    <input type="hidden" name="status" value="${status != null ? status : param.status}" />

                    <div class="form-group">
                        <label for="farmName">Tên trang trại</label>
                        <input type="text" class="form-control" id="farmName" name="farmName"
                               value="${farm.farmName}" required>
                        <c:if test="${not empty nameError}">
                            <small class="text-danger">${nameError}</small>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label for="location">Vị trí</label>
                        <input type="text" class="form-control" id="location" name="location"
                               value="${farm.location}" required>
                        <c:if test="${not empty locationError}">
                            <small class="text-danger">${locationError}</small>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label for="description">Mô tả</label>
                        <textarea class="form-control" id="description" name="description" rows="4" required>${farm.description}</textarea>
                        <c:if test="${not empty descriptionError}">
                            <small class="text-danger">${descriptionError}</small>
                        </c:if>
                    </div>

                    <button type="submit" class="btn btn-primary">Cập nhật</button>
                    <a href="ViewMyFarmsController?page=${param.page}&sort=${param.sort}&search=${param.search}&status=${param.status}" class="btn btn-secondary">Hủy</a>
                </form>
            </div>
        </section>

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
