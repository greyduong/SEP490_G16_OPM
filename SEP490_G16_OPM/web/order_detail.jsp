<%-- 
    Document   : order_detail
    Created on : Apr 10, 2025, 7:58:06 PM
    Author     : duong
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Order Detail</title>
    <link rel="stylesheet" href="css/bootstrap.min.css"/>
</head>
<body>
    <div class="container mt-4">
        <h2 class="mb-4">Order Detail - Order ID: ${order.orderID}</h2>

        <!-- Seller + Buyer in same row -->
        <div class="row">
            <!-- Buyer -->
            <div class="col-md-6">
                <h4>Buyer Information</h4>
                <p><strong>Name:</strong> ${buyer.fullName}</p>
                <p><strong>Email:</strong> ${buyer.email}</p>
                <p><strong>Phone:</strong> ${buyer.phone}</p>
                <p><strong>Address:</strong> ${buyer.address}</p>
            </div>

            <!-- Seller -->
            <div class="col-md-6">
                <h4>Seller Information</h4>
                <p><strong>Name:</strong> ${seller.fullName}</p>
                <p><strong>Email:</strong> ${seller.email}</p>
                <p><strong>Phone:</strong> ${seller.phone}</p>
                <p><strong>Address:</strong> ${seller.address}</p>
            </div>
        </div>

        <!-- Order Info -->
        <h4 class="mt-4">Order Information</h4>
        <p><strong>Order Date:</strong> ${order.createdAt}</p>
        <p><strong>Total Quantity:</strong> ${order.quantity}</p>
        <p><strong>Total Price:</strong> ${order.totalPrice}</p>
        <p><strong>Order Status:</strong> ${order.status}</p>
    </div>

    <script src="js/bootstrap.min.js"></script>
</body>
</html>



