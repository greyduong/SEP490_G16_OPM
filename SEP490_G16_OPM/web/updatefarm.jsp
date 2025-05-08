<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Sửa Trang trại | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <section class="spad" style="padding-top: 30px;">
            <div class="container">
                <h4 class="mb-4">Chỉnh sửa trang trại</h4>
                <c:if test="${not empty msg}">
                    <div class="alert alert-info" role="alert">
                        ${msg}
                    </div>
                </c:if>
                <form action="updateFarm" method="post" enctype="multipart/form-data">
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

                    <div class="form-group">
                        <label for="status">Trạng thái hoạt động</label>
                        <select name="statusOption" id="status" class="form-control" required>
                            <option value="Active" ${farm.status == 'Active' ? 'selected' : ''}>Hoạt động</option>
                            <option value="Inactive" ${farm.status == 'Inactive' ? 'selected' : ''}>Dừng hoạt động</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="image">Ảnh trang trại</label><br>
                        <c:if test="${not empty farm.imageURL}">
                            <img src="${farm.imageURL}" alt="Ảnh hiện tại" style="width: 150px; height: auto; margin-bottom: 10px;" /><br>
                        </c:if>
                        <input type="file" class="form-control-file" name="image" accept="image/*">
                    </div>

                    <button type="submit" class="btn btn-primary">Cập nhật</button>
                    <a href="my-farms?page=${param.page}&sort=${param.sort}&search=${param.search}&status=${param.status}" class="btn btn-secondary">Hủy</a>
                </form>
            </div>
        </section>
        <jsp:include page="component/footer.jsp" />
    </body>
</html>
