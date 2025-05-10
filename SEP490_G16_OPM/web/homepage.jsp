<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Online Pig Market">
        <meta name="keywords" content="pig, market">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
        <style>
            .modal-dialog-centered-custom {
                display: flex;
                align-items: center;
                justify-content: center;
                min-height: 100vh;
            }

            .modal-content {
                width: 100%;
                max-width: 350px;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            }

            #addToCartModal .form-control,
            #addToCartModal .btn {
                font-size: 14px;
            }
            /* Giữ nguyên danh sách dọc, nhưng không đẩy xuống các phần khác */
            .hero__categories {
                position: relative;
            }

            .hero__categories ul {
                position: absolute;
                top: 100%;
                left: 0;
                z-index: 10;
                background: white;
                width: 100%;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                display: none;
            }

            .hero__categories.open ul {
                display: block;
            }
        </style>
    </head>
    <body>
        <jsp:include page="component/header.jsp"></jsp:include>
            <!-- Hero Section Begin -->
            <section class="hero">
                <div class="container">
                    <div class="row">
                        <!-- Cột bên trái: Danh mục -->
                        <div class="col-lg-3">
                            <div class="hero__categories">
                                <div class="hero__categories__all">
                                    <i class="fa fa-bars"></i>
                                    <span>
                                    <c:choose>
                                        <c:when test="${not empty param.categoryName}">
                                            ${param.categoryName}
                                        </c:when>
                                        <c:otherwise>
                                            Loại Heo
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            <ul>
                                <li><a href="home">Tất cả</a></li>
                                    <c:forEach var="c" items="${categoryList}">
                                    <li><a href="home?categoryName=${c.name}">${c.name}</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </div>

                    <!-- Cột bên phải: Tìm kiếm -->
                    <div class="col-lg-9">
                        <div class="hero__search">
                            <div class="hero__search__form">
                                <form action="home" method="get">
                                    <input type="text" name="keyword" placeholder="Nhập tên chào bán..." value="${param.keyword}">
                                    <c:if test="${not empty param.categoryName}">
                                        <input type="hidden" name="categoryName" value="${param.categoryName}" />
                                    </c:if>
                                    <button type="submit" class="site-btn">TÌM</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Sort dropdown container riêng -->
            <div class="container my-3">
                <form action="home" method="get" class="form-inline">
                    <input type="hidden" name="keyword" value="${param.keyword}">
                    <input type="hidden" name="categoryName" value="${param.categoryName}">

                    <label class="mr-2 font-weight-bold">Sắp xếp:</label>
                    <select name="sort" class="form-control mr-2" onchange="this.form.submit()">
                        <option value="none">-- Chọn --</option>
                        <option value="quantity_asc" ${param.sort == 'quantity_asc' ? 'selected' : ''}>Số lượng ↑</option>
                        <option value="quantity_desc" ${param.sort == 'quantity_desc' ? 'selected' : ''}>Số lượng ↓</option>
                        <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá cả ↑</option>
                        <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá cả ↓</option>
                    </select>
                </form>
            </div>
        </section>
        <!-- Hero Section End -->

        <c:if test="${not empty msg}">
            <div class="alert alert-warning">${msg}</div>
        </c:if>

        <!-- Featured Section Begin -->
        <section class="featured spad">
            <div class="container">

                <div class="row featured__filter">
                    <c:forEach var="o" items="${offerList}">
                        <div class="col-lg-3 col-md-4 col-sm-6 mix fresh-meat">
                            <div class="featured__item">
                                <div class="featured__item__pic set-bg" data-setbg="${o.imageURL}">
                                    <ul class="featured__item__pic__hover">
                                        <li>
                                            <a href="#" class="open-cart-modal" data-offer-id="${o.offerID}" data-max="${o.quantity}" data-min="${o.minQuantity}">
                                                <i class="fa fa-shopping-cart"></i>
                                            </a>

                                        </li>
                                    </ul>
                                </div>
                                <div class="featured__item__text">
                                    <h6><a href="PigsOfferDetails?offerId=${o.offerID}">${o.name}</a></h6>
                                    <h5>Tổng giá: 
                                        <fmt:formatNumber value="${o.totalOfferPrice}" type="number" groupingUsed="true"/> VND
                                    </h5>
                                    <p>Số lượng: ${o.quantity} con</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="pagination-area mt-4 text-center">
                    <nav>
                        <ul class="pagination justify-content-center">
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link" href="home?page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </nav>
                </div>
            </div>
        </section>

        <jsp:include page="component/footer.jsp" />

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

        <c:if test="${not empty param.error and param.error == '403'}">
            alert("Bạn không có quyền thực hiện việc này");
        </c:if>

    </body>
</html>