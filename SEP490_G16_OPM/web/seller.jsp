<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang chủ người bán | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <div class="container mt-5">

            <!-- Dòng Farm -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <a href="my-farms" style="text-decoration: none;">
                        <div class="card text-white bg-secondary mb-3">
                            <div class="card-header">Tổng số trang trại</div>
                            <div class="card-body">
                                <h5 class="card-title">${totalFarms}</h5>
                            </div>
                        </div>
                    </a>
                </div>

                <div class="col-md-6">
                    <a href="my-farms?status=Active" style="text-decoration: none;">
                        <div class="card text-white bg-success mb-3">
                            <div class="card-header">Trang trại đang hoạt động</div>
                            <div class="card-body">
                                <h5 class="card-title">${activeFarms}</h5>
                            </div>
                        </div>
                    </a>
                </div>
            </div>

            <!-- Dòng Offer -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <a href="my-offers" style="text-decoration: none;">
                        <div class="card text-white bg-primary mb-3">
                            <div class="card-header">Tổng số chào bán</div>
                            <div class="card-body">
                                <h5 class="card-title">${totalOffers}</h5>
                            </div>
                        </div>
                    </a>
                </div>

                <div class="col-md-6">
                    <a href="my-offers?status=Available" style="text-decoration: none;">
                        <div class="card text-white bg-warning mb-3">
                            <div class="card-header">Đang chào bán</div>
                            <div class="card-body">
                                <h5 class="card-title">${availableOffers}</h5>
                            </div>
                        </div>
                    </a>
                </div>
            </div>

            <!-- Dòng Order -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <div class="card text-white bg-success mb-3">
                        <div class="card-header">Tổng số đơn hàng</div>
                        <div class="card-body">
                            <h5 class="card-title">${totalOrders}</h5>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Dòng Doanh thu & Công nợ -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <div class="card text-white bg-info mb-3">
                        <div class="card-header">Tổng doanh thu</div>
                        <div class="card-body">
                            <h5 class="card-title">
                                <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                            </h5>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card text-white bg-danger mb-3">
                        <div class="card-header">Tổng công nợ</div>
                        <div class="card-body">
                            <h5 class="card-title">
                                <fmt:formatNumber value="${totalDebt}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                            </h5>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="component/footer.jsp" />
    </body>
</html>