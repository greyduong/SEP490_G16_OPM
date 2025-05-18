<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
            <div class="row justify-content-start mb-5">
                <div class="col-12 col-md-8 col-lg-6">
                    <div class="p-4 border rounded shadow-sm">
                        <div class="text-center mb-3">
                            <img src="img/farm-default.png" alt="Ảnh trang trại" class="rounded-circle" width="150">
                        </div>
                        <div class="ps-3">
                            <h4 class="mb-2"><strong>${farm.farmName}</strong></h4>
                            <p><strong>Địa điểm:</strong> ${farm.location}</p>
                            <p><strong>Mô tả:</strong> ${farm.description}</p>                         
                            <p><strong>Ngày tạo:</strong> ${farm.createdAt}</p>

                        </div>
                    </div>
                </div>
            </div>
            <!-- Thanh lọc và tìm kiếm chào bán -->
            <section class="hero">
                <div class="container">
                    <div class="row">
                        <!-- Cột trái: Lọc theo loại heo -->
                        <div class="col-lg-3">
                            <div class="hero__categories">
                                <div class="hero__categories__all">
                                    <i class="fa fa-bars"></i>
                                    <span>Lọc theo loại heo</span>
                                </div>
                                <ul>
                                    <li><a href="farm-detail?farmId=${farm.farmID}">Tất cả</a></li>
                                        <c:forEach var="c" items="${categoryList}">
                                        <li><a href="farm-detail?farmId=${farm.farmID}&categoryName=${c.name}">${c.name}</a></li>
                                        </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <!-- Cột phải: Tìm kiếm chào bán -->
                        <div class="col-lg-9">
                            <div class="hero__search">
                                <div class="hero__search__form">
                                    <form action="farm-detail" method="get">
                                        <input type="hidden" name="farmId" value="${farm.farmID}" />
                                        <input type="text" name="keyword" placeholder="Tìm theo tên chào bán..." value="${param.keyword}">
                                        <c:if test="${not empty param.categoryName}">
                                            <input type="hidden" name="categoryName" value="${param.categoryName}" />
                                        </c:if>
                                        <button type="submit" class="site-btn">Tìm kiếm</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Sắp xếp chào bán -->
                <div class="container my-3">
                    <form action="farm-detail" method="get" class="form-inline">
                        <input type="hidden" name="farmId" value="${farm.farmID}" />
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <input type="hidden" name="categoryName" value="${param.categoryName}">
                        <label class="mr-2 font-weight-bold">Sắp xếp chào bán:</label>
                        <select name="sort" class="form-control mr-2" onchange="this.form.submit()">
                            <option value="none">-- Chọn --</option>
                            <option value="quantity_asc" <c:if test="${param.sort == 'quantity_asc'}">selected</c:if>>Số lượng ↑</option>
                            <option value="quantity_desc" <c:if test="${param.sort == 'quantity_desc'}">selected</c:if>>Số lượng ↓</option>
                            <option value="price_asc" <c:if test="${param.sort == 'price_asc'}">selected</c:if>>Giá cả ↑</option>
                            <option value="price_desc" <c:if test="${param.sort == 'price_desc'}">selected</c:if>>Giá cả ↓</option>
                            </select>
                        </form>
                    </div>
                </section>


                <!-- Danh sách Chào bán Heo -->
                <h4 class="mb-3 text-center">Danh sách chào bán của ${farm.farmName}</h4>
            <div class="row featured__filter">
                <c:forEach var="o" items="${offers}">
                    
                    <div class="col-lg-3 col-md-4 col-sm-6 mix fresh-meat">
                        <div class="featured__item">
                            <div class="featured__item__pic set-bg position-relative" data-setbg="${o.imageURL}">
                                <!-- Link toàn ảnh -->
                                <a href="PigsOfferDetails?offerId=${o.offerID}" class="offer-link"></a>

                                <!-- Nút giỏ hàng -->
                                <ul class="featured__item__pic__hover">
                                    <li>
                                        <a href="PigsOfferDetails?offerId=${o.offerID}" 
                                           data-offer-id="${o.offerID}"
                                           data-max="${o.quantity}"
                                           data-min="${o.minQuantity}">
                                            <i class="fa fa-shopping-cart"></i>
                                        </a>
                                    </li>
                                </ul>
                            </div>

                            <div class="featured__item__text">
                                <h6><a href="PigsOfferDetails?offerId=${o.offerID}">${o.name}</a></h6>
                                <h5>
                                    Tổng giá:
                                    <fmt:formatNumber value="${o.totalOfferPrice}" type="number" groupingUsed="true"/> VND
                                </h5>
                                <p>Số lượng: ${o.quantity} con</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty offers}">
                    <div class="col-12">
                        <div class="alert alert-info text-center">Trang trại này chưa có chào bán nào.</div>
                    </div>
                </c:if>
            </div>
        </main>

        <jsp:include page="component/footer.jsp"/>
    </body>
</html>
