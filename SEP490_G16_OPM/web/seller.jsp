<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Trang chủ người bán | Online Pig Market</title>
        <jsp:include page="component/library.jsp"/>
        <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0"></script>
        <script>
            function toggleInputs() {
                const type = document.querySelector("select[name='type']").value;
                const yearGroup = document.getElementById("yearGroup");
                const monthGroup = document.getElementById("monthGroup");
                const dayGroup = document.getElementById("dayGroup");
                yearGroup.style.display = "none";
                monthGroup.style.display = "none";
                dayGroup.style.display = "none";
                if (type === "month") {
                    yearGroup.style.display = "block";
                } else if (type === "day") {
                    yearGroup.style.display = "block";
                    monthGroup.style.display = "block";
                } else if (type === "hour") {
                    yearGroup.style.display = "block";
                    monthGroup.style.display = "block";
                    dayGroup.style.display = "block";
                }
            }

            document.addEventListener("DOMContentLoaded", () => {
                toggleInputs();
                Chart.register(ChartDataLabels);
            });
        </script>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>

        <div class="container mt-5">
            <div class="row justify-content-center g-4">
                <% java.util.Calendar cal = java.util.Calendar.getInstance(); request.setAttribute("currentYear", cal.get(java.util.Calendar.YEAR)); %>
                <!-- Cards content giữ nguyên -->
                <div class="col-auto">
                    <div class="card bg-success text-white dashboard-card">
                        <div class="card-header">Trang trại</div>
                        <div class="card-body">
                            <h5 class="card-title mb-1">
                                <a href="my-farms" class="text-white fw-bold">${totalFarms}</a>
                            </h5>
                            <p class="card-text m-0">
                                <a href="my-farms?status=Active" class="text-white text-decoration-underline">
                                    Hoạt động: ${activeFarms}
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col-auto">
                    <div class="card bg-warning text-white dashboard-card">
                        <div class="card-header">Chào bán</div>
                        <div class="card-body">
                            <h5 class="card-title mb-1">
                                <a href="my-offers" class="text-white fw-bold">${totalOffers}</a>
                            </h5>
                            <p class="card-text m-0">
                                <a href="my-offers?status=Available" class="text-white text-decoration-underline">
                                    Đang bán: ${availableOffers}
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col-auto">
                    <div class="card bg-primary text-white dashboard-card">
                        <div class="card-header">Đơn hàng</div>
                        <div class="card-body d-flex align-items-center justify-content-center">
                            <h5 class="card-title fw-bold">${totalOrders}</h5>
                        </div>
                    </div>
                </div>
                <div class="col-auto">
                    <div class="card bg-info text-white dashboard-card">
                        <div class="card-header">Doanh thu</div>
                        <div class="card-body d-flex align-items-center justify-content-center">
                            <h5 class="card-title fw-bold">
                                <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                            </h5>
                        </div>
                    </div>
                </div>
                <div class="col-auto">
                    <div class="card bg-danger text-white dashboard-card">
                        <div class="card-header">Công nợ</div>
                        <div class="card-body d-flex align-items-center justify-content-center">
                            <h5 class="card-title fw-bold">
                                <fmt:formatNumber value="${totalDebt}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                            </h5>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Form Filter -->
        <form class="container mt-5" method="GET" action="seller">
            <div class="row mb-4">
                <div class="col-md-3">
                    <label class="form-label fw-bold">Thống kê theo:</label>
                    <select name="type" class="form-select" onchange="toggleInputs(); this.form.submit();">
                        <option value="year" ${param.type == 'year' ? 'selected' : ''}>Năm</option>
                        <option value="month" ${param.type == 'month' ? 'selected' : ''}>Tháng</option>
                        <option value="day" ${param.type == 'day' ? 'selected' : ''}>Ngày</option>
                        <option value="hour" ${param.type == 'hour' ? 'selected' : ''}>Giờ</option>
                    </select>
                </div>
                <div class="col-md-3" id="yearGroup">
                    <label class="form-label fw-bold">Chọn năm:</label>
                    <input type="number" name="year" class="form-control"
                           value="${param.year != null ? param.year : currentYear}" onchange="this.form.submit()"/>
                </div>
                <div class="col-md-3" id="monthGroup">
                    <label class="form-label fw-bold">Chọn tháng:</label>
                    <input type="number" name="month" class="form-control"
                           value="${param.month}" onchange="updateDayLimit(); this.form.submit()"/>
                </div>
                <div class="col-md-3" id="dayGroup">
                    <label class="form-label fw-bold">Chọn ngày:</label>
                    <input type="number" name="day" class="form-control"
                           value="${param.day}" onchange="updateDayLimit(); this.form.submit()"/>
                </div>
            </div>
        </form>


        <!-- Biểu đồ đơn hàng -->
        <div class="container mb-5">
            <div class="card">
                <div class="card-header fw-bold">Biểu đồ đơn hàng theo <c:choose><c:when test="${statType == 'month'}">Tháng</c:when><c:when test="${statType == 'day'}">Ngày</c:when><c:when test="${statType == 'hour'}">Giờ</c:when><c:otherwise>Năm</c:otherwise></c:choose></div>
                        <div class="card-body">
                            <canvas id="orderChart" height="100"></canvas>
                        </div>
                    </div>
                </div>

                <script>
                    const labels = [<c:forEach var="s" items="${orderStats}" varStatus="loop">"${s.label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
                            const values = [<c:forEach var="s" items="${orderStats}" varStatus="loop">${s.total}<c:if test="${!loop.last}">,</c:if></c:forEach>];
                    const ctx = document.getElementById('orderChart').getContext('2d');
                    new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: labels,
                            datasets: [{
                                    label: 'Số đơn hàng',
                                    data: values,
                                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                                    borderColor: 'rgba(54, 162, 235, 1)',
                                    borderWidth: 1,
                                    borderRadius: 10,
                                    barThickness: 40
                                }]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                datalabels: {
                                    anchor: 'end',
                                    align: 'end',
                                    font: {weight: 'bold', size: 12},
                                    formatter: value => value,
                                    color: '#000'
                                },
                                legend: {
                                    labels: {
                                        font: {size: 14, weight: 'bold'}
                                    }
                                },
                                tooltip: {
                                    callbacks: {
                                        label: ctx => `Số đơn hàng: ${ctx.parsed.y}`
                                    }
                                }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Số lượng',
                                        font: {size: 14, weight: 'bold'}
                                    },
                                    ticks: {
                                        stepSize: 1,
                                        font: {size: 13, weight: 'bold'}
                                    }
                                },
                                x: {
                                    title: {
                                        display: true,
                                        text: '<c:choose><c:when test="${statType == 'month'}">Tháng</c:when><c:when test="${statType == 'day'}">Ngày</c:when><c:when test="${statType == 'hour'}">Giờ</c:when><c:otherwise>Năm</c:otherwise></c:choose>',
                                                                font: {size: 14, weight: 'bold'}
                                                            },
                                                            ticks: {
                                                                font: {size: 13, weight: 'bold'}
                                                            }
                                                        }
                                                    }
                                                },
                                                plugins: [ChartDataLabels]
                                            });
                </script>
                <script>
                    function updateDayLimit() {
                        const year = parseInt(document.querySelector("input[name='year']").value) || new Date().getFullYear();
                        const month = parseInt(document.querySelector("input[name='month']").value);
                        const dayInput = document.querySelector("input[name='day']");

                        if (!month)
                            return;

                        let maxDay;
                        switch (month) {
                            case 2:
                                // Năm nhuận
                                maxDay = (year % 4 === 0 && (year % 100 !== 0 || year % 400 === 0)) ? 29 : 28;
                                break;
                            case 4:
                            case 6:
                            case 9:
                            case 11:
                                maxDay = 30;
                                break;
                            default:
                                maxDay = 31;
                        }

                        dayInput.max = maxDay;
                        if (parseInt(dayInput.value) > maxDay) {
                            dayInput.value = maxDay;
                        }
                    }

                </script>

        <jsp:include page="component/footer.jsp"/>
    </body>
</html>
