<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <!-- Hero Section Begin -->
        <section class="hero">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3">
                        <div class="hero__categories">
                            <div class="hero__categories__all">
                                <i class="fa fa-bars"></i>
                                <span>All departments</span>
                            </div>
                            <ul style="display: none;">
                                <li><a href="#">Fresh Meat</a></li>
                                <li><a href="#">Vegetables</a></li>
                                <li><a href="#">Fruit & Nut Gifts</a></li>
                                <li><a href="#">Fresh Berries</a></li>
                                <li><a href="#">Ocean Foods</a></li>
                                <li><a href="#">Butter & Eggs</a></li>
                                <li><a href="#">Fastfood</a></li>
                                <li><a href="#">Fresh Onion</a></li>
                                <li><a href="#">Papayaya & Crisps</a></li>
                                <li><a href="#">Oatmeal</a></li>
                                <li><a href="#">Fresh Bananas</a></li>
                            </ul>
                        </div>
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
                            <h2>Vegetable’s Package</h2>
                            <div class="breadcrumb__option">
                                <a href="./index.html">Home</a>
                                <a href="./index.html">Vegetables</a>
                                <span>Vegetable’s Package</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- Product Details Section Begin -->
        <section class="product-details spad">
            <div class="container">
                <div class="row">
                    <!-- Image Section -->
                    <div class="col-lg-6 col-md-6">
                        <div class="product__details__pic">
                            <img class="product__details__pic__item--large" 
                                 src="ImageServlet?folder=pigs&file=${offer.imageURL}" 
                                 alt="${offer.name}" 
                                 style="max-width: 100%; height: auto; border-radius: 10px;">
                        </div>
                    </div>

                    <!-- Offer Information Section -->
                    <div class="col-lg-6 col-md-6">
                        <div class="product__details__text" style="line-height: 1.6;">
                            <div class="product__details__text">
                                <h3 style="color: #28a745; margin-bottom: 16px;">${offer.name}</h3>

                                <p style="margin-bottom: 8px;"><strong>Breed:</strong> ${offer.pigBreed}</p>
                                <p style="margin-bottom: 8px;"><strong>Farm:</strong> Trang Trại 1</p>
                                <p style="margin-bottom: 8px;"><strong>Available Quantity:</strong> ${offer.quantity} pigs</p>
                                <p style="margin-bottom: 8px;"><strong>Minimum Order:</strong> ${offer.minQuantity} pigs</p>

                                <div style="background-color: #f8f9fa; padding: 15px; border-radius: 8px; margin-top: 15px;">
                                    <h4 style="color: #d43f3a; margin-bottom: 10px;">Price Details</h4>
                                    <p style="margin-bottom: 8px;"><strong>Per Pig:</strong> <fmt:formatNumber value="${offer.retailPrice}" type="number" groupingUsed="true"/> đ</p>
                                    <p style="margin-bottom: 8px;"><strong>Total Offer:</strong> <fmt:formatNumber value="${offer.totalOfferPrice}" type="number" groupingUsed="true"/> đ</p>
                                    <p style="margin-bottom: 8px;"><strong>Minimum Deposit:</strong> <fmt:formatNumber value="${offer.minDeposit}" type="number" groupingUsed="true"/> đ</p>
                                </div>

                                <p style="margin-top: 15px; margin-bottom: 8px;"><strong>Start Date:</strong> <fmt:formatDate value="${offer.startDate}" pattern="dd/MM/yyyy"/></p>
                                <p style="margin-bottom: 8px;"><strong>End Date:</strong> <fmt:formatDate value="${offer.endDate}" pattern="dd/MM/yyyy"/></p>
                            </div>

                            <div class="my-3">
                                <h5>Description</h5>
                                <p>${offer.description}</p>
                            </div>

                            <div class="mt-4">
                                <a href="#" class="primary-btn">Buy Now</a>
                                <a href="#" class="btn btn-outline-secondary ml-3">Add to Cart</a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Other Offers Section -->
                <div class="mt-5">
                    <h4 class="mb-4">Other Offers</h4>
                    <div class="row">
                        <c:forEach var="o" items="${otherOffers}">
                            <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                                <div class="product__item shadow-sm rounded">
                                    <div class="product__item__pic set-bg" data-setbg="ImageServlet?folder=pigs&file=${o.imageURL}" style="height:200px; border-radius:10px;"></div>
                                    <div class="product__item__text p-3" style="line-height: 1.6;">
                                        <h6><a href="PigsOfferDetails?offerId=${o.offerID}">${o.name}</a></h6>
                                        <p>Farm: Trang Trại ${o.farmID}</p>
                                        <p>Price: <fmt:formatNumber value="${o.retailPrice}" type="number" groupingUsed="true"/> đ / pig</p>
                                        <p>Quantity: ${o.quantity} pigs</p>
                                        <p>Due: <fmt:formatDate value="${o.endDate}" pattern="dd/MM/yyyy"/></p>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </section>
        <jsp:include page="component/footer.jsp" />
    </body>

</html>