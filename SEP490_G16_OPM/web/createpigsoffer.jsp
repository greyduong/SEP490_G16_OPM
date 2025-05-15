<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tạo chào bán | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <section class="product spad">
            <div class="container">
                <h4 class="mb-4">🐖 Tạo chào bán mới</h4>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form action="createOffer" method="post" enctype="multipart/form-data">
                    <div class="row">
                        <!-- Cột trái -->
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="name">Tên chào bán <span style="color: red">*</span></label>
                                <input type="text" class="form-control" id="name" name="name" value="${name}" required>
                                <c:if test="${not empty error_name}">
                                    <small class="text-danger">${error_name}</small>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label for="categoryId">Danh mục <span style="color: red">*</span></label>
                                <select class="form-control" id="categoryId" name="categoryId" required>
                                    <c:forEach var="cat" items="${categories}">
                                        <option value="${cat.categoryID}" ${cat.categoryID == categoryId ? "selected" : ""}>${cat.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="farmId">Trang trại <span style="color: red">*</span></label>
                                <select class="form-control" id="farmId" name="farmId" required>
                                    <c:forEach var="farm" items="${myFarms}">
                                        <option value="${farm.farmID}" ${farm.farmID == farmId ? "selected" : ""}>${farm.farmName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="pigBreed">Giống heo <span style="color: red">*</span></label>
                                <input type="text" class="form-control" id="pigBreed" name="pigBreed" value="${pigBreed}" required>
                                <c:if test="${not empty error_breed}">
                                    <small class="text-danger">${error_breed}</small>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label for="description">Mô tả <span style="color: red">*</span></label>
                                <textarea class="form-control" id="description" name="description" rows="5" required>${description}</textarea>
                                <c:if test="${not empty error_description}">
                                    <small class="text-danger">${error_description}</small>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label for="image">Hình ảnh <span style="color: red">*</span></label>

                                <div class="custom-file">
                                    <input type="file" class="custom-file-input" id="image" name="image" accept="image/*" required>
                                    <label class="custom-file-label" for="image">Chọn ảnh...</label>
                                </div>

                                <img id="previewImage" src="#" alt="Xem trước ảnh" class="img-thumbnail mt-2" style="max-height: 250px; display: none;">
                            </div>

                        </div>

                        <!-- Cột phải -->
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="quantity">Số lượng <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="quantity" name="quantity" value="${quantity}" required min="1">
                            </div>

                            <div class="form-group">
                                <label for="minQuantity">Số lượng tối thiểu <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="minQuantity" name="minQuantity" value="${minQuantity}" required min="1">
                                <c:if test="${not empty error_quantity}">
                                    <small class="text-danger">${error_quantity}</small>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label for="minDeposit">Tiền cọc (VNĐ) <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="minDeposit" name="minDeposit" value="${minDeposit}" required min="0">
                            </div>

                            <div class="form-group">
                                <label for="retailPrice">Giá lẻ (VNĐ) <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="retailPrice" name="retailPrice" value="${retailPrice}" required min="0">
                            </div>

                            <div class="form-group">
                                <label for="totalOfferPrice">Tổng giá (VNĐ) <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="totalOfferPrice" name="totalOfferPrice" value="${totalOfferPrice}" required min="0">
                                <c:if test="${not empty error_price}">
                                    <small class="text-danger">${error_price}</small>
                                </c:if>
                            </div>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label for="startDate">Ngày bắt đầu <span style="color: red">*</span></label>
                                    <input type="date" class="form-control" id="startDate" name="startDate" value="${startDate}" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label for="endDate">Ngày kết thúc <span style="color: red">*</span></label>
                                    <input type="date" class="form-control" id="endDate" name="endDate" value="${endDate}" required>
                                </div>
                                <c:if test="${not empty error_date}">
                                    <div class="col-md-12">
                                        <small class="text-danger">${error_date}</small>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="d-flex justify-content-between mt-4">
                        <a href="my-offers?page=${page}&farmId=${farmIdParam}&search=${search}&status=${status}&sort=${sort}"
                           class="btn btn-outline-secondary">
                            ⬅ Quay lại danh sách
                        </a>
                        <button type="submit" class="btn btn-primary">📤 Tạo chào bán</button>
                    </div>
                </form>
            </div>
        </section>

        <jsp:include page="component/footer.jsp" />
        <script>
            document.getElementById("image").addEventListener("change", function (event) {
                const file = event.target.files[0];
                const preview = document.getElementById("previewImage");

                // Cập nhật label tên file
                const fileName = file ? file.name : "Chọn ảnh...";
                event.target.nextElementSibling.innerText = fileName;

                // Hiển thị ảnh xem trước
                if (file && file.type.startsWith("image/")) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        preview.src = e.target.result;
                        preview.style.display = "block";
                    };
                    reader.readAsDataURL(file);
                } else {
                    preview.src = "#";
                    preview.style.display = "none";
                }
            });
        </script>

    </body>
</html>
