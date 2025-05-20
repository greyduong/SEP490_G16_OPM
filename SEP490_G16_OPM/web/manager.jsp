<%-- 
    Document   : manager
    Created on : Mar 28, 2025, 9:36:01 PM
    Author     : tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="component/library.jsp" />
        <title>Manager Page</title>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>
        <div class="px-5 flex flex-col gap-3">
            <div>
                <div class="text-sm font-semibold mb-2">Khoảng thời gian</div>
                <form id="dateRangeForm" class="inline-flex gap-3 border border-slate-300 rounded-sm p-3 text-slate-600">
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
            <div class="grid grid-cols-2 gap-3">
                <div class="p-3 border border-slate-300 rounded-sm">
                    <div class="text-sm font-semibold text-slate-600">Trang trại</div>
                    <canvas id="farmChart"></canvas>
                </div>
                <div class="p-3 border border-slate-300 rounded-sm">
                    <div class="text-sm font-semibold text-slate-600">Chào bán</div>
                    <canvas id="offerChart"></canvas>
                </div>
                <div class="p-3 border border-slate-300 rounded-sm">
                    <div class="text-sm font-semibold text-slate-600">Đơn hàng</div>
                    <canvas id="orderChart"></canvas>
                </div>
            </div>
        </div>
        <script>
            $("#dateRangeForm input").on("change", function () {
                $("#dateRangeForm").submit();
            });
        </script>
        <script>
            function showChart(chart) {
                const type = chart.type === undefined ? "line" : chart.type;
                const name = chart.name;
                const labels = chart.labels;
                const datasets = chart.datasets;
                const ctx = document.getElementById(name).getContext("2d");
                new Chart(ctx, {
                    type: type,
                    data: {
                        labels: labels,
                        datasets: datasets
                    }
                });
            }

            fetch("", {
                method: "POST"
            }).then(res => res.json()).then(charts => {
                console.log(charts);
                for (let chart of charts) {
                    showChart(chart);
                }
            });
        </script>
        <jsp:include page="component/footer.jsp"/>
    </body>
</html>
