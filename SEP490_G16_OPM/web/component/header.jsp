<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String currentPath = request.getRequestURI();
    out.println("<!-- currentPath: " + currentPath + " -->");
%>

<!-- Guest Header -->
<c:if test="${empty sessionScope.user}">
    <!-- Page Preloder -->
    <div id="preloder">
        <div class="loader"></div>
    </div>

    <!-- Humberger Begin -->
    <div class="humberger__menu__overlay"></div>
    <div class="humberger__menu__wrapper">
        <div class="humberger__menu__logo">
            <a href="home"><img src="img/logo.svg" alt="" width="50"></a>
        </div>
        <div class="header__top__right__auth">
            <c:if test="${empty sessionScope.user}">
                <a href="login-register.jsp" class="nav-item nav-link">Login</a>
            </c:if>

            <c:if test="${not empty sessionScope.user}">
                <span class="nav-item nav-link nav-link d-inline-block">Hello, ${sessionScope.user.fullName}</span>
                <a href="auth?action=logout" class="nav-item nav-link nav-link d-inline-block">Logout</a>
            </c:if>
        </div>
        <nav class="humberger__menu__nav mobile-menu">
            <ul>
                <li class="${currentPath.contains('/home') ? 'active' : ''}"><a href="home">Home</a></li>
                <li class="${currentPath.contains('/shop-grid') ? 'active' : ''}"><a href="./shop-grid.html">Farm</a></li>
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
                                    <a href="auth?action=logout" class="nav-item nav-link nav-link d-inline-block">Logout</a>
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
                        <a href="home">
                            <img src="img/logo.svg" alt="" width="50">
                        </a>
                    </div>
                </div>
                <div class="col-lg-6">
                    <nav class="header__menu">
                        <ul>
                            <li class="${currentPath.contains('/home') ? 'active' : ''}"><a href="home">Home</a></li>
                            <li class="${currentPath.contains('/shop-grid') ? 'active' : ''}"><a href="./shop-grid.html">Farm</a></li>                       
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
</c:if> 

<!-- Seller Header -->
<c:if test="${not empty sessionScope.user and sessionScope.user.roleID == 4}"> 
    <!-- Page Preloder -->
    <div id="preloder">
        <div class="loader"></div>
    </div>

    <!-- Humberger Begin -->
    <div class="humberger__menu__overlay"></div>
    <div class="humberger__menu__wrapper">
        <div class="humberger__menu__logo">
            <a href="home"><img src="img/logo.svg" alt="" width="50"></a>
        </div>
        <div class="header__top__right__auth">
            <c:if test="${empty sessionScope.user}">
                <a href="login-register.jsp" class="nav-item nav-link">Login</a>
            </c:if>

            <c:if test="${not empty sessionScope.user}">
                <span class="nav-item nav-link nav-link d-inline-block">Hello, ${sessionScope.user.fullName}</span>
                <a href="auth?action=logout" class="nav-item nav-link nav-link d-inline-block">Logout</a>
            </c:if>
        </div>
        <nav class="humberger__menu__nav mobile-menu">
            <ul>
                <li class="${currentPath.contains('/home') ? 'active' : ''}"><a href="home">Home</a></li>
                <li class="${currentPath.contains('/shop-grid') ? 'active' : ''}"><a href="./shop-grid.html">Farm</a></li>
                <li><a href="CustomerOrderPageController">Customer Order</a>
                    <ul class="header__menu__dropdown">
                        <li><a href="CustomerOrderPageController">Customer Order</a></li>
                        <li><a href="OrdersRequestController">Customer Order Request</a></li>
                    </ul>
                </li>
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
                                    <a href="auth?action=logout" class="nav-item nav-link nav-link d-inline-block">Logout</a>
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
                        <a href="home">
                            <img src="img/logo.svg" alt="" width="50">
                        </a>
                    </div>
                </div>
                <div class="col-lg-6">
                    <nav class="header__menu">
                        <ul>
                            <li class="${currentPath.contains('/home') ? 'active' : ''}"><a href="home">Home</a></li>
                            <li class="${currentPath.contains('/shop-grid') ? 'active' : ''}"><a href="./shop-grid.html">Farm</a></li>
                            <li><a href="CustomerOrderPageController">Customer Order</a>
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
</c:if>

<!-- Dealer Header -->
<c:if test="${not empty sessionScope.user and sessionScope.user.roleID == 5}"> 
    <!-- Page Preloder -->
    <div id="preloder">
        <div class="loader"></div>
    </div>

    <!-- Humberger Begin -->
    <div class="humberger__menu__overlay"></div>
    <div class="humberger__menu__wrapper">
        <div class="humberger__menu__logo">
            <a href="home"><img src="img/logo.svg" alt="" width="50"></a>
        </div>
        <div class="header__top__right__auth">
            <c:if test="${empty sessionScope.user}">
                <a href="login-register.jsp" class="nav-item nav-link">Login</a>
            </c:if>

            <c:if test="${not empty sessionScope.user}">
                <span class="nav-item nav-link nav-link d-inline-block">Hello, ${sessionScope.user.fullName}</span>
                <a href="auth?action=logout" class="nav-item nav-link nav-link d-inline-block">Logout</a>
            </c:if>
        </div>
        <nav class="humberger__menu__nav mobile-menu">
            <ul>
                <li class="${currentPath.contains('/home') ? 'active' : ''}"><a href="home">Home</a></li>
                <li class="${currentPath.contains('/shop-grid') ? 'active' : ''}"><a href="./shop-grid.html">Farm</a></li>
                <li class="${currentPath.contains('/cart') ? 'active' : ''}"><a href="cart">Shopping Cart</a></li>
                <li class="${currentPath.contains('/myorders') ? 'active' : ''}"><a href="myorders">My Orders</a></li>
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
                                    <a href="auth?action=logout" class="nav-item nav-link nav-link d-inline-block">Logout</a>
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
                        <a href="home">
                            <img src="img/logo.svg" alt="" width="50">
                        </a>
                    </div>
                </div>
                <div class="col-lg-6">
                    <nav class="header__menu">
                        <ul>
                            <li class="${currentPath.contains('/home') ? 'active' : ''}"><a href="home">Home</a></li>
                            <li class="${currentPath.contains('/shop-grid') ? 'active' : ''}"><a href="./shop-grid.html">Farm</a></li>
                            <li class="${currentPath.contains('/cart') ? 'active' : ''}"><a href="cart">Shopping Cart</a></li>
                            <li class="${currentPath.contains('/myorders') ? 'active' : ''}"><a href="myorders">My Orders</a></li>
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
</c:if>