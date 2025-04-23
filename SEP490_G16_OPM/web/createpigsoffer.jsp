<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <section class="product-details spad">
            <div class="container">
                <h2 class="text-center mb-5">Create New Pigs Offer</h2>
                <form action="CreatePigsOffer" method="post" enctype="multipart/form-data">
                    <div class="row">
                        <div class="col-lg-6">
                            <label>Name</label>
                            <input type="text" name="name" class="form-control" required>

                            <label>Pig Breed</label>
                            <input type="text" name="pigBreed" class="form-control" required>

                            <label>Quantity</label>
                            <input type="number" name="quantity" class="form-control" required>

                            <label>Minimum Quantity</label>
                            <input type="number" name="minQuantity" class="form-control" required>

                            <label>Retail Price (VND)</label>
                            <input type="number" name="retailPrice" step="0.01" class="form-control" required>

                            <label>Total Offer Price (VND)</label>
                            <input type="number" name="totalOfferPrice" step="0.01" class="form-control" required>
                        </div>

                        <div class="col-lg-6">
                            <label>Minimum Deposit (VND)</label>
                            <input type="number" name="minDeposit" step="0.01" class="form-control" required>

                            <label>Start Date</label>
                            <input type="date" name="startDate" class="form-control" required>

                            <label>End Date</label>
                            <input type="date" name="endDate" class="form-control" required>

                            <label>Description</label>
                            <textarea name="description" class="form-control" rows="4" required></textarea>

                            <label>Upload Image</label>
                            <input type="file" name="imageFile" class="form-control" required>

                            <div class="mt-4 text-center">
                                <button type="submit" class="btn btn-success">Create Offer</button>
                                <a href="offerList.jsp" class="btn btn-secondary">Cancel</a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </section>
        <jsp:include page="component/footer.jsp" />
    </body>
</html>