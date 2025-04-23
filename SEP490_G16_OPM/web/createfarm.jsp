<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tạo Trang trại | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
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
                <h4 class="mb-4">Tạo trang trại mới</h4>

                <c:if test="${not empty msg}">
                    <div class="alert alert-info" role="alert">
                        ${msg}
                    </div>
                </c:if>

                <form action="createFarm" method="post">
                    <div class="form-group">
                        <label for="farmName">Tên trang trại</label>
                        <input type="text" class="form-control" id="farmName" name="farmName" 
                               value="${farmName}" required>
                        <c:if test="${not empty nameError}">
                            <small class="text-danger">${nameError}</small>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label for="location">Vị trí</label>
                        <input type="text" class="form-control" id="location" name="location" 
                               value="${location}" required>
                        <c:if test="${not empty locationError}">
                            <small class="text-danger">${locationError}</small>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label for="description">Mô tả</label>
                        <textarea class="form-control" id="description" name="description" rows="4" required>${description}</textarea>
                        <c:if test="${not empty descriptionError}">
                            <small class="text-danger">${descriptionError}</small>
                        </c:if>
                    </div>

                    <button type="submit" class="btn btn-primary">Tạo trang trại</button>
                    <a href="my-farms?page=${param.page}&sort=${param.sort}&search=${param.search}&status=${param.status}" class="btn btn-secondary">Hủy</a>
                </form>
            </div>
        </section>
        <jsp:include page="component/footer.jsp" />
    </body>
</html>
