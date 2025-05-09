<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết Trang trại | Online Pig Market</title>
        <jsp:include page="component/library.jsp"/>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>

        <main class="container my-5">
            <!-- Thông tin trang trại -->
            <div class="row justify-content-start">
                <div class="col-12 col-md-8 col-lg-6">
                    <div class="p-4 border rounded shadow-sm">
                        <div class="text-center mb-3">
                            <img src="img/farm-default.png" alt="Ảnh trang trại" class="rounded-circle" width="150">
                        </div>
                        <div class="ps-3">
                            <h4 class="mb-2"><strong>${farm.farmName}</strong></h4>
                            <p><strong>Địa điểm:</strong> ${farm.location}</p>
                            <p><strong>Mô tả:</strong> ${farm.description}</p>
                            <p><strong>Ghi chú:</strong> ${farm.note}</p>
                            <p><strong>Trạng thái:</strong> ${farm.status}</p>
                            <p><strong>Ngày tạo:</strong> ${farm.createdAt}</p>

                        </div>
                    </div>
                </div>
            </div>

            <!-- Danh sách Chào bán Heo -->
            <h4 class="mb-3 text-center">Danh sách chào bán của ${farm.farmName}</h4>
            <div class="row">
                <c:forEach var="offer" items="${offers}">
                    <div class="col-md-3 mb-4">
                        <div class="card h-100">
                            <img src="${offer.imageURL}" class="card-img-top" alt="Ảnh chào bán">
                            <div class="card-body">
                                <h6 class="card-title">${offer.name}</h6>
                                <p>Giá: ${offer.retailPrice} / kg</p>
                                <p>Số lượng: ${offer.quantity}</p>
                                <p>Loại heo: ${offer.pigBreed}</p>
                                <p>Hạn chót: ${offer.endDate}</p>
                                <p class="text-muted small">${offer.description}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty offers}">
                    <div class="col-12">
                        <div class="alert alert-info">Trang trại này chưa có chào bán nào.</div>
                    </div>
                </c:if>
            </div>
        </main>

        <jsp:include page="component/footer.jsp"/>
    </body>
</html>
