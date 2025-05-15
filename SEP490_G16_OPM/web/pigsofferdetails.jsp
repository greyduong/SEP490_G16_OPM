<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết chào bán | Chợ Heo Trực Tuyến</title>
        <jsp:include page="component/library.jsp"/>
        <style>
            /* Nếu cần, bạn có thể thêm style tùy chỉnh cho thẻ a phủ toàn bộ */
        </style>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>

        <c:if test="${not empty msg}">
            <div class="alert alert-info text-center">${msg}</div>
        </c:if>

        <section class="featured spad">
            <div class="container">
                <div class="row">
                    <!-- Toàn bộ nội dung chiếm full width -->
                    <div class="col-lg-12">

                        <!-- Header thông tin tóm tắt chào bán -->
                        <div class="mb-4 p-3 bg-light rounded shadow-sm text-center">
                            <h2 class="mb-2">${offer.name}</h2>
                        </div>

                        <!-- Chi tiết chào bán -->
                        <div class="row mb-5">
                            <!-- Ảnh sản phẩm -->
                            <div class="col-lg-5">
                                <div class="card shadow-sm">
                                    <img src="${offer.imageURL}" class="card-img-top img-fluid rounded" alt="${offer.name}">
                                </div>
                            </div>

                            <!-- Thông tin sản phẩm -->
                            <div class="col-lg-7">
                                <div class="card p-4 shadow-sm">
                                    <h3 class="text-success mb-3">Thông tin</h3>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <p><strong>Giống heo:</strong> ${offer.pigBreed}</p>
                                            <p><strong>Trang trại:</strong> ${offer.farm.farmName}</p>
                                            <p><strong>Số lượng có sẵn:</strong> ${offer.quantity} con</p>
                                            <p><strong>Số lượng tối thiểu:</strong> ${offer.minQuantity} con</p>
                                        </div>
                                        <div class="col-md-6">
                                            <p><strong>Giá mỗi con:</strong> <fmt:formatNumber value="${offer.retailPrice}" type="number" groupingUsed="true"/> đ</p>
                                            <p><strong>Tổng giá trị:</strong> <fmt:formatNumber value="${offer.totalOfferPrice}" type="number" groupingUsed="true"/> đ</p>
                                            <p><strong>Tiền cọc tối thiểu:</strong> <fmt:formatNumber value="${offer.minDeposit}" type="number" groupingUsed="true"/> đ</p>
                                        </div>
                                    </div>

                                    <!-- Thông tin chi tiết mô tả -->
                                    <div class="card p-3 mt-4 shadow-sm">
                                        <h5>Thông tin chi tiết</h5>
                                        <p>${offer.description}</p>
                                    </div>

                                    <hr>

                                    <p><strong>Ngày bắt đầu:</strong> <fmt:formatDate value="${offer.startDate}" pattern="dd/MM/yyyy"/></p>
                                    <p><strong>Ngày kết thúc:</strong> <fmt:formatDate value="${offer.endDate}" pattern="dd/MM/yyyy"/></p>

                                    <div class="mt-3 d-flex gap-2">
                                        <!-- Mua ngay: gửi form POST tới AddToCartController với mode=buyNow -->
                                        <form action="AddToCartController" method="post" class="d-inline">
                                            <input type="hidden" name="offerId" value="${offer.offerID}" />
                                            <input type="hidden" name="quantity" value="${offer.quantity}" />
                                            <input type="hidden" name="mode" value="buyNow" />
                                            <button type="submit" class="btn btn-success">Mua ngay</button>
                                        </form>

                                        <!-- Thêm vào giỏ: mở modal chọn số lượng -->
                                        <a href="#" class="btn btn-outline-secondary open-cart-modal"
                                           data-offer-id="${offer.offerID}"
                                           data-max="${offer.quantity}"
                                           data-min="${offer.minQuantity}">
                                            Thêm vào giỏ
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Gợi ý chào bán -->
                        <div class="section-title d-flex justify-content-between align-items-center">
                            <h4>Các chào bán gợi ý</h4>
                            <c:choose>
                                <c:when test="${suggestedSource == 'category'}">
                                    <a href="home?categoryName=${offer.category.name}" class="btn btn-sm btn-outline-primary">Xem thêm</a>
                                </c:when>
                                <c:when test="${suggestedSource == 'farm'}">
                                    <a href="farm-detail?id=${offer.farm.farmID}" class="btn btn-sm btn-outline-primary">Xem thêm</a>
                                </c:when>
                                <c:when test="${suggestedSource == 'latest'}">
                                    <a href="home?sort=newest" class="btn btn-sm btn-outline-primary">Xem thêm</a>
                                </c:when>
                            </c:choose>
                        </div>

                        <c:choose>
                            <c:when test="${not empty suggestedOffers}">
                                <div class="row">
                                    <c:forEach var="o" items="${suggestedOffers}">
                                        <div class="col-lg-4 col-md-6 col-sm-6">
                                            <div class="product__item">
                                                <div class="product__item__pic set-bg" data-setbg="${o.imageURL}" style="position: relative;">
                                                    <!-- Thêm thẻ <a> phủ toàn bộ ảnh để click ảnh dẫn tới chi tiết -->
                                                    <a href="PigsOfferDetails?offerId=${o.offerID}" style="position: absolute;top:0;left:0;width:100%;height:100%;z-index:1;display:block;"></a>
                                                    <ul class="product__item__pic__hover" style="position: relative; z-index: 2;">
                                                        <li><a href="PigsOfferDetails?offerId=${o.offerID}"><i class="fa fa-eye"></i></a></li>
                                                        <li>
                                                            <a href="#" class="open-cart-modal" 
                                                               data-offer-id="${o.offerID}" 
                                                               data-max="${o.quantity}" 
                                                               data-min="${o.minQuantity}">
                                                                <i class="fa fa-shopping-cart"></i>
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </div>
                                                <div class="product__item__text">
                                                    <h6><a href="PigsOfferDetails?offerId=${o.offerID}">${o.name}</a></h6>
                                                    <h5><fmt:formatNumber value="${o.retailPrice}" type="number" groupingUsed="true"/> đ</h5>
                                                    <p>Số lượng: ${o.quantity} con</p>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted text-center">Không có chào bán tương tự nào.</p>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
            </div>
        </section>

        <!-- Modal Add to Cart -->
        <div class="modal fade" id="addToCartModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered-custom" role="document">
                <div class="modal-content p-4">
                    <form action="AddToCartController" method="post" id="addToCartForm">
                        <input type="hidden" name="offerId" id="modalOfferId" />

                        <select name="mode" id="modeSelect" class="form-control mb-2">
                            <option value="all" selected>Mua toàn bộ</option>
                            <option value="custom">Chọn số lượng</option>
                        </select>

                        <input type="number" name="quantity" id="modalQuantity" class="form-control" style="display: none;" />

                        <button type="submit" class="btn btn-success w-100 mt-3">Thêm vào giỏ</button>
                    </form>
                </div>
            </div>
        </div>

        <script>
            $(document).ready(function () {
                let max = 0;
                let min = 0;

                $('.open-cart-modal').click(function (e) {
                    e.preventDefault();

                    const offerId = $(this).data('offer-id');
                    max = parseInt($(this).data('max'));
                    min = parseInt($(this).data('min'));

                    $('#modalOfferId').val(offerId);
                    $('#modeSelect').val('all');
                    $('#modalQuantity')
                            .attr('min', min)
                            .attr('max', max)
                            .val(max)
                            .prop('readonly', true)
                            .show();

                    $('#addToCartModal').modal('show');
                });

                $('#modeSelect').on('change', function () {
                    const mode = $(this).val();
                    if (mode === 'custom') {
                        $('#modalQuantity')
                                .val(min)
                                .prop('readonly', false)
                                .attr('min', min)
                                .attr('max', max)
                                .show();
                    } else {
                        $('#modalQuantity')
                                .val(max)
                                .prop('readonly', true)
                                .show();
                    }
                });
            });
        </script>

        <jsp:include page="component/footer.jsp"/>
    </body>
</html>
