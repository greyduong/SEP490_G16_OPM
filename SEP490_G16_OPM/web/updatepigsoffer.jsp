<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Update Pig Offer</title>
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

        <!-- Update Pigs Offer Section Begin -->
        <section class="product-details spad">
            <div class="container">
                <h2 class="text-center mb-5">Update Pigs Offer</h2>
                <form action="UpdatePigsOffer" method="post" enctype="multipart/form-data">
                    <!-- Hidden OfferID để update -->
                    <input type="hidden" name="offerId" value="${offer.offerID}">

                    <div class="row">
                        <div class="col-lg-6">
                            <label>Name</label>
                            <input type="text" name="name" class="form-control" value="${offer.name}" required>

                            <label>Pig Breed</label>
                            <input type="text" name="pigBreed" class="form-control" value="${offer.pigBreed}" required>

                            <label>Quantity</label>
                            <input type="number" name="quantity" class="form-control" value="${offer.quantity}" required>

                            <label>Minimum Quantity</label>
                            <input type="number" name="minQuantity" class="form-control" value="${offer.minQuantity}" required>

                            <label>Retail Price (VND)</label>
                            <input type="text" name="retailPrice" class="form-control" value="${offer.retailPrice}" required>

                            <label>Total Offer Price (VND)</label>
                            <input type="text" name="totalOfferPrice" class="form-control" value="${offer.totalOfferPrice}" required>
                        </div>

                        <div class="col-lg-6">
                            <label>Minimum Deposit (VND)</label>
                            <input type="text" name="minDeposit" class="form-control" value="${offer.minDeposit}" required>

                            <label>Start Date</label>
                            <input type="date" name="startDate" class="form-control" value="${offer.startDate}" required>

                            <label>End Date</label>
                            <input type="date" name="endDate" class="form-control" value="${offer.endDate}" required>

                            <label>Description</label>
                            <textarea name="description" class="form-control" rows="4" required>${offer.description}</textarea>

                            <label>Current Image</label><br>
                            <img src="ImageServlet?folder=pigs&file=${offer.imageURL}" alt="Current Image" style="max-width: 300px; height: auto; border:1px solid #ccc;"><br><br>

                            <label>Upload New Image (optional)</label>
                            <input type="file" name="imageFile" class="form-control">

                            <div class="mt-4 text-center">
                                <button type="submit" class="btn btn-primary">Update Offer</button>
                                <a href="offerList.jsp" class="btn btn-secondary">Cancel</a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </section>
        <!-- Update Pigs Offer Section End -->
        <jsp:include page="component/footer.jsp" />
    </body>

</html>