<%-- 
    Document   : customerorderdetail
    Created on : Apr 16, 2025, 8:28:07 PM
    Author     : duong
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- Check if the user is logged in and has the Seller role --%>
<c:if test="${empty sessionScope.user}">
    <script>
        window.location.href = "login-register.jsp"; // Redirect to login if not logged in
    </script>
</c:if>

<c:if test="${sessionScope.user.roleID != 4}">
    <script>
        window.location.href = "home"; // Redirect if the user is not a Seller
    </script>
</c:if>

<!-- Display error or success message -->
<c:if test="${not empty msg}">
    <div class="alert alert-info" role="alert">
        ${msg}  <!-- This will display the message set in the controller -->
    </div>
</c:if>

<!DOCTYPE html>
<html lang="zxx">

    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Customer Order Detail">
        <meta name="keywords" content="Customer Order, Delivery, Order Info">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Customer Order Detail</title>
        <!-- Link to styles -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">
    </head>

    <body>

        <!-- Page Preloder -->
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
                                <li class="active"><a href="home">Home</a></li>
                                <li><a href="./shop-grid.html">Shop</a></li>
                                <li><a href="#">Pages</a>
                                    <ul class="header__menu__dropdown">
                                        <li><a href="CustomerOrderPageController">Customer Order</a></li>
                                        <li><a href="OrdersRequestController">Customer Order Request</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </nav>
                    </div>

                </div>
                <div class="humberger__open">
                    <i class="fa fa-bars"></i>
                </div>
            </div>
        </header>
        <!-- Header Section End -->

        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <div class="breadcrumb__text">
                            <h2>Customer Order Detail</h2>
                            <div class="breadcrumb__option">
                                <a href="home">Home</a>
                                <span>Customer Order Detail</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Order and Dealer Information Section -->
        <div class="container mt-5">
            <c:if test="${not empty order}">
                <div class="row">
                    <!-- Order Information Table (takes up 6 columns) -->
                    <div class="col-md-4">
                        <h3>Order Information</h3>
                        <table class="table table-bordered">
                            <tr>
                                <th>Order ID</th>
                                <td>${order.orderID}</td>
                            </tr>
                            <tr>
                                <th>Status</th>
                                <td>${order.status}</td>
                            </tr>
                            <tr>
                                <th>Dealer's Name</th>
                                <td>${order.dealer.fullName}</td>
                            </tr>
                            <tr>
                                <th>Offer</th>
                                <td>${order.pigsOffer.name}</td>
                            </tr>
                            <tr>
                                <th>Total Price (VND)</th>
                                <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true" /></td>
                            </tr>
                            <tr>
                                <th>Quantity</th>
                                <td>${order.quantity}</td>
                            </tr>
                            <tr>
                                <th>Created At</th>
                                <td><fmt:formatDate value="${order.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                            </tr>
                        </table>
                    </div>

                    <!-- Delivery Information Table (takes up 6 columns) -->
                    <div class="col-md-7">
                        <h3>Delivery Information</h3>
                        <!-- Check if deliveryList is empty -->
                        <c:if test="${empty deliveryList}">
                            <p class="text-center">No deliveries found for this order.</p>
                        </c:if>

                        <c:if test="${not empty deliveryList}">
                            <table class="table table-bordered text-center">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Delivery ID</th>
                                        <th>Delivery Status</th>
                                        <th>Recipient Name</th>
                                        <th>Quantity</th>
                                        <th>Total Price</th>
                                        <th>Created At</th>
                                        <th>Comments</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="d" items="${deliveryList}">
                                        <tr>
                                            <td>${d.deliveryID}</td>
                                            <td>${d.deliveryStatus}</td>
                                            <td>${d.recipientName}</td>
                                            <td>${d.quantity}</td>
                                            <td><fmt:formatNumber value="${d.totalPrice}" type="number" groupingUsed="true" /></td>
                                            <td><fmt:formatDate value="${d.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                            <td>${d.comments}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>

        <!--         Footer Section Begin 
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
                                    <div class="footer__copyright__text"><p> Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. 
                                            Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | This template is made with <i class="fa fa-heart" aria-hidden="true"></i> by <a href="https://colorlib.com" target="_blank">Colorlib</a>
                                             Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. </p></div>
                                    <div class="footer__copyright__payment"><img src="img/payment-item.png" alt=""></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </footer>
                 Footer Section End -->

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

    </body>

</html>
