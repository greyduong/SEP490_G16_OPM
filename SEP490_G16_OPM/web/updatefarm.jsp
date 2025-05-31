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
                        <label for="farmName">Tên trang trại<span style="color: red">*</span></label>
                        <input type="text" class="form-control" id="farmName" name="farmName"
                               value="${farm.farmName}" required>
                        <c:if test="${not empty nameError}">
                            <small class="text-danger">${nameError}</small>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label for="location">Vị trí<span style="color: red">*</span></label>
                        <input type="text" class="form-control" id="location" name="location"
                               value="${farm.location}" required>
                        <c:if test="${not empty locationError}">
                            <small class="text-danger">${locationError}</small>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label for="description">Mô tả<span style="color: red">*</span></label>
                        <textarea class="form-control" id="description" name="description" rows="4" required>${farm.description}</textarea>
                        <c:if test="${not empty descriptionError}">
                            <small class="text-danger">${descriptionError}</small>
                        </c:if>
                    </div>

                    <c:choose>
                        <c:when test="${farm.status == 'Active'}">
                            <div class="form-group">
                                <label for="status">Trạng thái hoạt động<span style="color: red">*</span></label>
                                <select name="statusOption" id="status" class="form-control" required>
                                    <option value="Active" selected>Hoạt động</option>
                                    <option value="Inactive">Dừng hoạt động</option>
                                </select>
                            </div>
                        </c:when>
                        <c:when test="${farm.status == 'Inactive'}">
                            <div class="form-group">
                                <label for="status">Trạng thái hoạt động<span style="color: red">*</span></label>
                                <select name="statusOption" id="status" class="form-control" required>
                                    <option value="Inactive" selected>Dừng hoạt động</option>
                                    <option value="Active">Hoạt động</option>
                                </select>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="statusOption" value="${farm.status}" />
                        </c:otherwise>
                    </c:choose>

                    <div class="mb-3">
                        <div>
                            <label class="text-slate-600 !inline-flex items-center gap-2 border !border-dashed rounded-sm cursor-pointer p-2">
                                <span class="mdi mdi-upload text-2xl"></span>
                                <div>
                                    <div class="font-bold text-sm">Tải ảnh lên</div>
                                    <div class="font-normal text-sm">Tối đa 32MB</div>
                                </div>
                                <input id="imageInput" type="file" class="hidden" name="image" accept="image/*">
                            </label>
                        </div>
                        <div class="w-55 h-55">
                            <img class="border border-slate-300 rounded-sm w-full h-full object-contain" id="previewImage" src="${farm.imageURL}" alt="Ảnh hiện tại" />
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary">Cập nhật</button>
                    <a href="my-farms?page=${param.page}&sort=${param.sort}&search=${param.search}&status=${param.status}" class="btn btn-secondary">Hủy</a>
                </form>
            </div>
        </section>
        <script>
            document.querySelector("input[type=file]").addEventListener("change", function (event) {
                const file = event.target.files[0];
                const preview = document.getElementById("previewImage");
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        preview.src = e.target.result;
                    };
                    reader.readAsDataURL(file);
                }
            });
        </script>
        <jsp:include page="component/spinner.jsp" />
        <jsp:include page="component/footer.jsp" />
    </body>
</html>
