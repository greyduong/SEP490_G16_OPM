<%-- 
    Document   : shoppingcart
    Created on : Apr 3, 2025, 11:03:54 PM
    Author     : duong
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <title>Ogani | Template</title>

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
            .shoping__cart__quantity form {
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .shoping__cart__quantity input {
                height: 35px;
                padding: 0;
                font-size: 16px;
            }

            .shoping__cart__quantity .btn {
                height: 35px;
                width: 35px;
                padding: 0;
                font-size: 18px;
            }

        </style>
    </head>

    <body>
        <div id="preloder">
            <div class="loader"></div>
        </div>

        <!-- Humberger Begin -->
        <div class="humberger__menu__overlay"></div>
        <div class="humberger__menu__wrapper">
            <div class="humberger__menu__logo">
                <a href="#"><img src="img/logo.png" alt=""></a>
            </div>
            <div class="humberger__menu__cart">
                <ul>
                    <li><a href="#"><i class="fa fa-heart"></i> <span>1</span></a></li>
                    <li><a href="#"><i class="fa fa-shopping-bag"></i> <span>3</span></a></li>
                </ul>
                <div class="header__cart__price">item: <span>$150.00</span></div>
            </div>
            <div class="humberger__menu__widget">
                <div class="header__top__right__language">
                    <img src="img/language.png" alt="">
                    <div>English</div>
                    <span class="arrow_carrot-down"></span>
                    <ul>
                        <li><a href="#">Spanis</a></li>
                        <li><a href="#">English</a></li>
                    </ul>
                </div>
                <div class="header__top__right__auth">
                    <a href="#"><i class="fa fa-user"></i> Login</a>
                </div>
            </div>
            <nav class="humberger__menu__nav mobile-menu">
                <ul>
                    <li class="active"><a href="./index.html">Home</a></li>
                    <li><a href="./shop-grid.html">Shop</a></li>
                    <li><a href="#">Pages</a>
                        <ul class="header__menu__dropdown">
                            <li><a href="./shop-details.html">Shop Details</a></li>
                            <li><a href="./shoping-cart.html">Shoping Cart</a></li>
                            <li><a href="./checkout.html">Check Out</a></li>
                            <li><a href="./blog-details.html">Blog Details</a></li>
                        </ul>
                    </li>
                    <li><a href="./blog.html">Blog</a></li>
                    <li><a href="./contact.html">Contact</a></li>
                </ul>
            </nav>
            <div id="mobile-menu-wrap"></div>
            <div class="header__top__right__social">
                <a href="#"><i class="fa fa-facebook"></i></a>
                <a href="#"><i class="fa fa-twitter"></i></a>
                <a href="#"><i class="fa fa-linkedin"></i></a>
                <a href="#"><i class="fa fa-pinterest-p"></i></a>
            </div>
            <div class="humberger__menu__contact">
                <ul>
                    <li><i class="fa fa-envelope"></i> hello@colorlib.com</li>
                    <li>Free Shipping for all Order of $99</li>
                </ul>
            </div>
        </div>
        <!-- Humberger End -->

        <!-- Header Section Begin -->
        <header class="header">
            <div class="header__top">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-6 col-md-6">
                            <div class="header__top__left">
                                <ul>
                                    <li><i class="fa fa-envelope"></i> onlinepigmaket</li>
                                </ul>
                            </div>
                        </div>
                        <div class="col-lg-6 col-md-6">
                            <div class="header__top__right">                               
                                <div class="header__top__right__auth">
                                    <c:if test="${empty sessionScope.user}">
                                        <a href="login-register.jsp" class="nav-item nav-link">Login</a>
                                    </c:if>

                                    <c:if test="${not empty sessionScope.user}">
                                        <span class="nav-item nav-link nav-link d-inline-block">Hello, ${sessionScope.user.fullName}</span>
                                        <a href="logout" class="nav-item nav-link nav-link d-inline-block">Logout</a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container">
                <div class="row">
                    <div class="col-lg-3">
                        <div class="header__logo">
                            <a href="./index.html">
                                <img src="img/logo.svg" alt="" width="50">
                            </a>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <nav class="header__menu">
                            <ul>
                                <li class="active"><a href="./index.html">Home</a></li>
                                <li><a href="./shop-grid.html">Shop</a></li>
                                <li><a href="#">Pages</a>
                                    <ul class="header__menu__dropdown">
                                        <li><a href="./shop-details.html">Shop Details</a></li>
                                        <li><a href="./shoping-cart.html">Shopping Cart</a></li>
                                        <li><a href="./checkout.html">Check Out</a></li>
                                        <li><a href="./blog-details.html">Blog Details</a></li>
                                    </ul>
                                </li>
                                <li><a href="./blog.html">Blog</a></li>
                                <li><a href="./contact.html">Contact</a></li>
                            </ul>
                        </nav>
                    </div>
                    <div class="col-lg-3">
                        <div class="header__cart">
                            <ul>
                                <li><a href="#"><i class="fa fa-heart"></i> <span>1</span></a></li>
                                <li><a href="#"><i class="fa fa-shopping-bag"></i> <span>3</span></a></li>
                            </ul>
                            <div class="header__cart__price">item: <span>$150.00</span></div>
                        </div>
                    </div>
                </div>
                <div class="humberger__open">
                    <i class="fa fa-bars"></i>
                </div>
            </div>
        </header>
        <!-- Header Section End -->

        <!-- Hero Section Begin -->
        <section class="hero">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3">
                    </div>
                    <div class="col-lg-9">
                        <div class="hero__search">
                            <div class="hero__search__form">
                                <form action="#">
                                    <div class="hero__search__categories">
                                        All Categories
                                        <span class="arrow_carrot-down"></span>
                                    </div>
                                    <input type="text" placeholder="What do you need?">
                                    <button type="submit" class="site-btn">SEARCH</button>
                                </form>
                            </div>                           
                        </div>                       
                    </div>
                </div>
            </div>
        </section>
        <!-- Hero Section End -->

        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <div class="breadcrumb__text">
                            <h2>Shopping Cart</h2>
                            <div class="breadcrumb__option">
                                <a href="./index.html">Home</a>
                                <span>Shopping Cart</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- Shoping Cart Section Begin -->
        <section class="shoping-cart spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="shoping__cart__table">
                            <table>
                                <thead>
                                    <tr>
                                        <th class="shoping__product">Products</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Total(VND)</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="cart" items="${cartList}">
                                        <tr>
                                            <td class="shoping__cart__item">
                                                <img src="ImageServlet?folder=pigs&file=${cart.pigsOffer.imageURL}" alt="" style="width: 100px; height: auto;">
                                                <h5>${cart.pigsOffer.name}</h5>
                                            </td>
                                            <td class="shoping__cart__price">
                                                $${cart.pigsOffer.retailPrice}
                                            </td>
                                            <td class="shoping__cart__quantity text-center">
                                                <h6>${cart.quantity}</h6>

                                            </td>
                                            <td class="shoping__cart__total">
                                                <fmt:formatNumber value="${cart.quantity * cart.pigsOffer.retailPrice}" type="number" groupingUsed="true" />
                                            </td>
                                            <td class="shoping__cart__item__close text-center">
                                                <div class="d-flex justify-content-center mb-2" style="gap: 6px;">
                                                    <a href="#"
                                                       class="btn btn-sm btn-warning open-update-modal"
                                                       title="Cập nhật số lượng"
                                                       data-cart-id="${cart.cartID}"
                                                       data-quantity="${cart.quantity}"
                                                       data-min="${cart.pigsOffer.minQuantity}"
                                                       data-max="${cart.pigsOffer.quantity}"
                                                       data-mode="${cart.quantity == cart.pigsOffer.quantity ? 'all' : 'custom'}">
                                                        <i class="fa fa-pencil"></i>
                                                    </a>
                                                    <a href="remove-cart?id=${cart.cartID}" class="btn btn-sm btn-danger" title="Xoá">
                                                        <i class="fa fa-trash"></i>
                                                    </a>
                                                </div>

                                                <form action="checkout" method="post">
                                                    <input type="hidden" name="cartId" value="${cart.cartID}">
                                                    <input type="hidden" name="offerId" value="${cart.pigsOffer.offerID}">
                                                    <input type="hidden" name="quantity" value="${cart.quantity}">
                                                    <button type="submit" class="btn btn-sm btn-success">Checkout</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <!-- Phân trang -->
                            <div class="shoping__cart__pagination text-center mt-4">
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <a href="cart?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
                                </c:forEach>
                            </div>
                        </div>

                    </div>
                </div>
            </div>              
        </section>
        <!-- Shoping Cart Section End -->

        <!-- Footer Section Begin -->
        <footer class="footer spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-md-6 col-sm-6">
                        <div class="footer__about">
                            <div class="footer__about__logo">
                                <a href="./index.html"><img src="img/logo.png" alt=""></a>
                            </div>
                            <ul>
                                <li>Address: 60-49 Road 11378 New York</li>
                                <li>Phone: +65 11.188.888</li>
                                <li>Email: hello@colorlib.com</li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6 col-sm-6 offset-lg-1">
                        <div class="footer__widget">
                            <h6>Useful Links</h6>
                            <ul>
                                <li><a href="#">About Us</a></li>
                                <li><a href="#">About Our Shop</a></li>
                                <li><a href="#">Secure Shopping</a></li>
                                <li><a href="#">Delivery infomation</a></li>
                                <li><a href="#">Privacy Policy</a></li>
                                <li><a href="#">Our Sitemap</a></li>
                            </ul>
                            <ul>
                                <li><a href="#">Who We Are</a></li>
                                <li><a href="#">Our Services</a></li>
                                <li><a href="#">Projects</a></li>
                                <li><a href="#">Contact</a></li>
                                <li><a href="#">Innovation</a></li>
                                <li><a href="#">Testimonials</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-12">
                        <div class="footer__widget">
                            <h6>Join Our Newsletter Now</h6>
                            <p>Get E-mail updates about our latest shop and special offers.</p>
                            <form action="#">
                                <input type="text" placeholder="Enter your mail">
                                <button type="submit" class="site-btn">Subscribe</button>
                            </form>
                            <div class="footer__widget__social">
                                <a href="#"><i class="fa fa-facebook"></i></a>
                                <a href="#"><i class="fa fa-instagram"></i></a>
                                <a href="#"><i class="fa fa-twitter"></i></a>
                                <a href="#"><i class="fa fa-pinterest"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-12">
                        <div class="footer__copyright">
                            <div class="footer__copyright__text"><p><!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
                                    Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | This template is made with <i class="fa fa-heart" aria-hidden="true"></i> by <a href="https://colorlib.com" target="_blank">Colorlib</a>
                                    <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. --></p></div>
                            <div class="footer__copyright__payment"><img src="img/payment-item.png" alt=""></div>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
        <!-- Footer Section End -->

        <!-- Modal Cập nhật số lượng -->
        <div class="modal fade" id="updateQuantityModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content p-4">
                    <form action="update-cart" method="post" id="updateCartForm">
                        <input type="hidden" name="cartId" id="modalCartId" />
                        <input type="hidden" name="page" value="${currentPage}" />

                        <select name="mode" id="updateModeSelect" class="form-control mb-2">
                            <option value="all">Mua toàn bộ</option>
                            <option value="custom">Chọn số lượng</option>
                        </select>

                        <input type="number" name="quantity" id="updateModalQuantity" class="form-control" />

                        <button type="submit" class="btn btn-success w-100 mt-3">Cập nhật</button>
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
        <script>
                                        $(document).ready(function () {
                                            $('.open-update-modal').click(function (e) {
                                                e.preventDefault();

                                                const cartId = $(this).data('cart-id');
                                                const quantity = parseInt($(this).data('quantity'));
                                                const min = parseInt($(this).data('min'));
                                                const max = parseInt($(this).data('max'));
                                                const mode = $(this).data('mode');

                                                $('#modalCartId').val(cartId);
                                                $('#updateModalQuantity')
                                                        .attr('min', min)
                                                        .attr('max', max)
                                                        .val(quantity)
                                                        .show();

                                                if (mode === 'custom') {
                                                    $('#updateModeSelect').val('custom');
                                                    $('#updateModalQuantity').prop('readonly', false);
                                                } else {
                                                    $('#updateModeSelect').val('all');
                                                    $('#updateModalQuantity').val(max).prop('readonly', true);
                                                }

                                                $('#updateQuantityModal').modal('show');
                                            });


                                            $('#updateModeSelect').on('change', function () {
                                                const mode = $(this).val();
                                                const max = parseInt($('#updateModalQuantity').attr('max'));
                                                const min = parseInt($('#updateModalQuantity').attr('min'));

                                                if (mode === 'custom') {
                                                    $('#updateModalQuantity').val(min).prop('readonly', false);
                                                } else {
                                                    $('#updateModalQuantity').val(max).prop('readonly', true);
                                                }
                                            });
                                        });
        </script>
    </body>

</html>
