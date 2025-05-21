<%-- 
    Document   : admin
    Created on : Mar 28, 2025, 9:34:48 PM
    Author     : tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="component/library.jsp"/>
        <title>Admin Page</title>
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>
        <div class="px-5 mb-3">
            <div class="text-sm font-semibold mb-2">Tổng quan</div>
            <div class="grid grid-cols-4 gap-3">
                <div class="p-3 rounded-sm border border-slate-300">
                    <div class="text-slate-600 text-sm font-semibold">Tổng số người dùng</div>
                    <div class="text-xl text-slate-600 font-semibold">${totalUser} <span class="font-normal text-sm">người dùng</span></div>
                </div>
            </div>
        </div>

        <div class="px-5 mb-5">
            <div class="text-sm font-semibold mb-2 text-slate-600">Biểu đồ</div>
            <div class="grid grid-cols-2 gap-3 mb-3">
                <div class="border border-slate-300 rounded-sm p-3 text-slate-600">
                    <div class="text-sm font-semibold">Biểu đồ người dùng</div>
                    <div class="w-45">
                        <canvas id="userChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
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
