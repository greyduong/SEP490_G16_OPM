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
        <title>Thống kê | Online Pig Market</title>
        <jsp:include page="component/library.jsp"/>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>
        <div class="px-5 mb-3">
            <div class="mb-3">
                <div class="text-sm font-semibold mb-2">Khoảng thời gian</div>
                <form id="dateRangeForm" class="inline-flex gap-3 border border-slate-300 rounded-sm p-3">
                    <div>
                        <div class="text-sm font-semibold">Từ</div>
                        <div class="p-1 border border-slate-300 rounded-sm text-md"><input name="from" type="date" value="${from}"></div>
                    </div>
                    <div>
                        <div class="text-sm font-semibold">Đến</div>
                        <div class="p-1 border border-slate-300 rounded-sm text-md"><input name="to" type="date" value="${to}"></div>
                    </div>
                </form>
            </div>
            <div class="text-sm font-semibold mb-2">Tổng quan</div>
            <div class="grid grid-cols-4 gap-3">
                <div class="p-3 rounded-sm border border-slate-300">
                    <div class="text-slate-600 text-sm font-semibold">Tổng chi</div>
                    <div class="text-xl text-slate-600 font-semibold"><fmt:formatNumber value="${totalSpend}" /> <span class="font-normal text-sm">VND</span></div>
                </div>
                <div class="p-3 rounded-sm border border-slate-300">
                    <div class="text-slate-600 text-sm font-semibold">Đơn đã đặt</div>
                    <div class="text-xl text-slate-600 font-semibold">${totalOrders} <span class="font-normal text-sm">đơn</span></div>
                </div>
                <div class="p-3 rounded-sm border border-slate-300">
                    <div class="text-slate-600 text-sm font-semibold">Tổng nạp</div>
                    <div class="text-xl text-slate-600 font-semibold">${totalTopup} <span class="font-normal text-sm">VND</span></div>
                </div>
            </div>
        </div>

        <!-- Biểu đồ đơn hàng -->
        <div class="px-5 mb-5">
            <div class="text-sm font-semibold mb-2 text-slate-600">Biểu đồ</div>
            <div class="grid grid-cols-2 gap-3">
                <div class="border border-slate-300 rounded-sm p-3 text-slate-600">
                    <div class="text-sm font-semibold">Biểu đồ đơn hàng</div>
                    <div class="card-body">
                        <canvas id="orderChart" height="100"></canvas>
                    </div>
                </div>
            </div>
        </div>
        <script>
            $("#dateRangeForm input").on("change", function () {
                $("#dateRangeForm").submit();
            });
        </script>
        <script>
            function showLineChart(chart) {
                const name = chart.name;
                const labels = chart.labels;
                const datasets = chart.datasets;
                const ctx = document.getElementById(name).getContext("2d");
                new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: datasets
                    }
                });
            }

            fetch("", {
                method: "POST"
            }).then(res => res.json()).then(charts => {
                charts.forEach(chart => {
                    showLineChart(chart);
                });
            });
        </script>
        <jsp:include page="component/footer.jsp"/>
    </body>
</html>

