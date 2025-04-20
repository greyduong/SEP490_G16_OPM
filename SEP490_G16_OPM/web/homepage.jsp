<%-- 
    Document   : homepage
    Created on : Mar 24, 2025, 10:32:06 PM
    Author     : dangtuong
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zxx">

    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Ogani Template">
        <meta name="keywords" content="Ogani, unica, creative, html">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Online Pig Market</title>

        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css2?family=Cairo:wght@200;300;400;600;900&display=swap" rel="stylesheet">

        <!-- Css Styles -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="css/elegant-icons.css" type="text/css">
        <link rel="stylesheet" href="css/nice-select.css" type="text/css">
        <link rel="stylesheet" href="css/jquery-ui.min.css" type="text/css">
        <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
        <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">
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
        </style>


    </head>

    <body>

        <jsp:include page="component/header.jsp"></jsp:include>

            <!-- Hero Section Begin -->
            <section class="hero">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-3">
                            <div class="hero__categories">
                                <div class="hero__categories__all">
                                    <i class="fa fa-bars"></i>
                                    <span>All Categories</span>
                                </div>
                                <ul>
                                <c:forEach var="c" items="${categoryList}">
                                    <li><a href="category?cid=${c.categoryID}">${c.name}</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </div>

                    <div class="col-lg-9">
                        <div class="hero__search">
                            <div class="hero__search__form">
                                <form action="search">
                                    <input type="text" name="keyword" placeholder="What do you need?">
                                    <button type="submit" class="site-btn">SEARCH</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Filter by Price -->
                    <div class="hero__search__filters">
                        <select name="price" class="form-control">
                            <option value="all">All Prices</option>
                            <option value="low">Low to High</option>
                            <option value="high">High to Low</option>
                        </select>
                    </div>

                    <!-- Filter by Quantity -->
                    <div class="hero__search__filters">
                        <select name="quantity" class="form-control">
                            <option value="all">All Quantities</option>
                            <option value="low">Low Quantity</option>
                            <option value="high">High Quantity</option>
                        </select>
                    </div>
                </div>
            </div>
        </section>
        <!-- Hero Section End -->


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
        <!-- Featured Section End -->

        <!--         Blog Section Begin 
                <section class="from-blog spad">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="section-title from-blog__title">
                                    <h2>From The Blog</h2>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-4 col-md-4 col-sm-6">
                                <div class="blog__item">
                                    <div class="blog__item__pic">
                                        <img src="img/blog/blog-1.jpg" alt="">
                                    </div>
                                    <div class="blog__item__text">
                                        <ul>
                                            <li><i class="fa fa-calendar-o"></i> May 4,2019</li>
                                            <li><i class="fa fa-comment-o"></i> 5</li>
                                        </ul>
                                        <h5><a href="#">Cooking tips make cooking simple</a></h5>
                                        <p>Sed quia non numquam modi tempora indunt ut labore et dolore magnam aliquam quaerat </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-6">
                                <div class="blog__item">
                                    <div class="blog__item__pic">
                                        <img src="img/blog/blog-2.jpg" alt="">
                                    </div>
                                    <div class="blog__item__text">
                                        <ul>
                                            <li><i class="fa fa-calendar-o"></i> May 4,2019</li>
                                            <li><i class="fa fa-comment-o"></i> 5</li>
                                        </ul>
                                        <h5><a href="#">6 ways to prepare breakfast for 30</a></h5>
                                        <p>Sed quia non numquam modi tempora indunt ut labore et dolore magnam aliquam quaerat </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-6">
                                <div class="blog__item">
                                    <div class="blog__item__pic">
                                        <img src="img/blog/blog-3.jpg" alt="">
                                    </div>
                                    <div class="blog__item__text">
                                        <ul>
                                            <li><i class="fa fa-calendar-o"></i> May 4,2019</li>
                                            <li><i class="fa fa-comment-o"></i> 5</li>
                                        </ul>
                                        <h5><a href="#">Visit the clean farm in the US</a></h5>
                                        <p>Sed quia non numquam modi tempora indunt ut labore et dolore magnam aliquam quaerat </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
                 Blog Section End -->

        <jsp:include page="component/footer.jsp"></jsp:include>

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

        <!-- Js Plugins -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.nice-select.min.js"></script>
        <script src="js/jquery-ui.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>
        <script src="js/bootstrap.min.js"></script> 

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



    </body>

</html>