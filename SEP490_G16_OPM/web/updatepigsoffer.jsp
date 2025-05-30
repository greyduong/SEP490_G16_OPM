<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Cập nhật chào bán | Online Pig Market</title>
        <jsp:include page="component/library.jsp"/>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>

        <section class="product spad">
            <div class="container">
                <h4 class="mb-4">🛠 Cập nhật chào bán</h4>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>

                <form action="updateOffer" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="offerId" value="${offer.offerID}" />

                    <div class="row">
                        <!-- Left column -->
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Tên chào bán</label>
                                <input type="text" class="form-control" name="name" value="${offer.name}" required>
                                <c:if test="${not empty error_name}">
                                    <small class="text-danger">${error_name}</small>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label>Danh mục</label>
                                <select class="form-control" name="categoryId" required>
                                    <c:forEach var="cat" items="${categories}">
                                        <option value="${cat.categoryID}" ${cat.categoryID == offer.categoryID ? "selected" : ""}>${cat.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Trang trại</label>
                                <select class="form-control" name="farmId" required>
                                    <c:forEach var="farm" items="${myFarms}">
                                        <option value="${farm.farmID}" ${farm.farmID == offer.farmID ? "selected" : ""}>${farm.farmName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Giống heo</label>
                                <input type="text" class="form-control" name="pigBreed" value="${offer.pigBreed}" required>
                                <c:if test="${not empty error_breed}">
                                    <small class="text-danger">${error_breed}</small>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label>Mô tả</label>
                                <textarea class="form-control" name="description" rows="4">${offer.description}</textarea>
                                <c:if test="${not empty error_description}">
                                    <small class="text-danger">${error_description}</small>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label>Hình ảnh (tùy chọn)</label>
                                <input type="file" class="form-control-file" name="image" accept="image/*">
                                <c:if test="${not empty offer.imageURL}">
                                    <p class="mt-2"><img src="${offer.imageURL}" alt="Ảnh hiện tại" style="max-width: 200px;" /></p>
                                    </c:if>
                            </div>
                        </div>

                        <!-- Right column -->
                        <fmt:formatNumber value="${offer.minDeposit}" var="formattedMinDeposit" pattern="0.##"/>
                        <fmt:formatNumber value="${offer.retailPrice}" var="formattedRetailPrice" pattern="0.##"/>
                        <fmt:formatNumber value="${offer.totalOfferPrice}" var="formattedTotalPrice" pattern="0.##"/>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Số lượng</label>
                                <div class="flex gap-2 items-center">
                                    <input type="number" class="form-control" name="quantity" value="${offer.quantity}" required min="1">
                                    <span>con</span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Số lượng tối thiểu</label>
                                <div class="flex gap-2 items-center">
                                    <input type="number" class="form-control" name="minQuantity" value="${offer.minQuantity}" required min="1">
                                    <span>con</span>
                                </div>
                                <c:if test="${not empty error_quantity}">
                                    <small class="text-danger">${error_quantity}</small>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label>Tiền cọc (VNĐ)</label>
                                <input type="number" class="form-control" name="minDeposit" value="${formattedMinDeposit}" required min="0" step="0.01">
                            </div>

                            <div class="form-group">
                                <label>Giá lẻ (VNĐ)</label>
                                <input type="number" class="form-control" name="retailPrice" value="${formattedRetailPrice}" required min="0" step="0.01">
                            </div>

                            <div class="form-group">
                                <label>Tổng giá (VNĐ)</label>
                                <input type="number" class="form-control" name="totalOfferPrice" value="${formattedTotalPrice}" required min="0" step="0.01">
                                <c:if test="${not empty error_price}">
                                    <small class="text-danger">${error_price}</small>
                                </c:if>
                            </div>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label>Ngày bắt đầu</label>
                                    <input type="date" class="form-control" name="startDate" value="${offer.startDate}" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label>Ngày kết thúc</label>
                                    <input type="date" class="form-control" name="endDate" value="${offer.endDate}" required>
                                </div>
                                <c:if test="${not empty error_date}">
                                    <div class="col-md-12">
                                        <small class="text-danger">${error_date}</small>
                                    </div>
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label for="status">Trạng thái</label>
                                <c:choose>
                                    <c:when test="${offer.status == 'Banned'}">
                                        <input type="text" class="form-control" value="Bị cấm" disabled />
                                        <input type="hidden" name="status" value="Banned" />
                                        <small class="text-danger">Không thể thay đổi trạng thái vì chào bán này đã bị cấm.</small>
                                    </c:when>
                                    <c:otherwise>
                                        <select name="status" class="form-control" required>
                                            <option value="Available" ${offer.status == 'Available' ? 'selected' : ''}>Hoạt động</option>
                                            <option value="Unavailable" ${offer.status == 'Unavailable' ? 'selected' : ''}>Ngưng bán</option>
                                            <option value="Upcoming" ${offer.status == 'Upcoming' ? 'selected' : ''}>Sắp mở bán</option>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between mt-4">
                        <a href="my-offers?page=${page}&farmId=${farmIdParam}&search=${search}&status=${status}&sort=${sort}" 
                           class="btn btn-outline-secondary">⬅ Quay lại danh sách</a>
                        <button type="submit" class="btn btn-primary">💾 Cập nhật</button>
                    </div>

                </form>
            </div>
        </section>

        <jsp:include page="component/footer.jsp"/>
    </body>
</html>
