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
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="name">Tên chào bán <span style="color: red">*</span></label>
                                <input type="text" class="form-control" id="name" name="name" required>
                            </div>

                            <div class="form-group">
                                <label for="categoryId">Danh mục <span style="color: red">*</span></label>
                                <select class="form-control" id="categoryId" name="categoryId" required>
                                    <c:forEach var="cat" items="${categories}">
                                        <option value="${cat.categoryID}">${cat.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="farmId">Trang trại <span style="color: red">*</span></label>
                                <select class="form-control" id="farmId" name="farmId" required>
                                    <c:forEach var="farm" items="${myFarms}">
                                        <option value="${farm.farmID}">${farm.farmName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="pigBreed">Giống heo <span style="color: red">*</span></label>
                                <input type="text" class="form-control" id="pigBreed" name="pigBreed" required>
                            </div>

                            <div class="form-group">
                                <label for="description">Mô tả <span style="color: red">*</span></label>
                                <textarea class="form-control" id="description" name="description" rows="5" required></textarea>
                            </div>

                            <div class="form-group">
                                <label for="image">Hình ảnh <span style="color: red">*</span></label>
                                <input type="file" class="form-control-file" id="image" name="image" accept="image/*" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="quantity">Số lượng <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="quantity" name="quantity" required min="1">
                            </div>

                            <div class="form-group">
                                <label for="minQuantity">Số lượng tối thiểu <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="minQuantity" name="minQuantity" required min="1">
                            </div>

                            <div class="form-group">
                                <label for="minDeposit">Tiền cọc (VNĐ) <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="minDeposit" name="minDeposit" required min="0">
                            </div>

                            <div class="form-group">
                                <label for="retailPrice">Giá lẻ (VNĐ) <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="retailPrice" name="retailPrice" required min="0">
                            </div>

                            <div class="form-group">
                                <label for="totalOfferPrice">Tổng giá (VNĐ) <span style="color: red">*</span></label>
                                <input type="number" class="form-control" id="totalOfferPrice" name="totalOfferPrice" required min="0">
                            </div>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label for="startDate">Ngày bắt đầu <span style="color: red">*</span></label>
                                    <input type="date" class="form-control" id="startDate" name="startDate" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label for="endDate">Ngày kết thúc <span style="color: red">*</span></label>
                                    <input type="date" class="form-control" id="endDate" name="endDate" required>
                                </div>
                            </div>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100 mt-4">📤 Tạo chào bán</button>
                </form>
            </div>
        </section>

        <jsp:include page="component/footer.jsp" />
    </body>
</html>