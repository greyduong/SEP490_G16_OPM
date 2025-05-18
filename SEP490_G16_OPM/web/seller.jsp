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
    </head>
    <body>
        <jsp:include page="component/header.jsp"/>
        <div class="px-5 mb-3">
			<div class="mb-3">
				<div class="text-sm font-semibold mb-2">Khoảng thời gian</div>
				<div class="inline-flex gap-3 border border-slate-300 rounded-sm p-3">
					<div>
						<div class="text-sm font-semibold">Từ</div>
						<div class="p-1 border border-slate-300 rounded-sm"><input type="date"></div>
					</div>
					<div>
						<div class="text-sm font-semibold">Đến</div>
						<div class="p-1 border border-slate-300 rounded-sm"><input type="date"></div>
					</div>
				</div>
			</div>
			<div class="text-sm font-semibold mb-2">Tổng quan</div>
			<div class="grid grid-cols-5 gap-3">
				<div class="p-3 rounded-sm border border-slate-300">
					<div class="text-slate-600 text-sm font-semibold">Trang trại</div>
					<div class="text-xl text-slate-600 font-semibold">${activeFarms}/${totalFarms} <span class="font-normal text-sm">hoạt động</span></div>
				</div>
				<div class="p-3 rounded-sm border border-slate-300">
					<div class="text-slate-600 text-sm font-semibold">Chào bán</div>
					<div class="text-xl text-slate-600 font-semibold">${totalOffers} <span class="font-normal text-sm">chào bán</span></div>
				</div>
				<div class="p-3 rounded-sm border border-slate-300">
					<div class="text-slate-600 text-sm font-semibold">Đơn hàng</div>
					<div class="text-xl text-slate-600 font-semibold">${totalOrders} <span class="font-normal text-sm">đơn</span></div>
				</div>
				<div class="p-3 rounded-sm border border-slate-300">
					<div class="text-slate-600 text-sm font-semibold">Doanh thu</div>
					<div class="text-xl text-slate-600 font-semibold"><fmt:formatNumber value="${totalRevenue}" groupingUsed="true"/> <span class="font-normal text-sm">VND</span></div>
				</div>
				<div class="p-3 rounded-sm border border-slate-300">
					<div class="text-slate-600 text-sm font-semibold">Công nợ</div>
					<div class="text-xl text-slate-600 font-semibold"><fmt:formatNumber value="${totalDebt}" groupingUsed="true"/> <span class="font-normal text-sm">VND</span></div>
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
				<div class="border border-slate-300 rounded-sm p-3 text-slate-600">
					<div class="text-sm font-semibold">Biểu đồ chào bán</div>
					<div class="card-body">
						<canvas id="offersChart" height="100"></canvas>
					</div>
				</div>
			</div>
		</div>

		<script type="module">
            const labels = [<c:forEach var="s" items="${orderStats}" varStatus="loop">"${s.label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
                    const values = [<c:forEach var="s" items="${orderStats}" varStatus="loop">${s.total}<c:if test="${!loop.last}">,</c:if></c:forEach>];
            const ctx = document.getElementById('offersChart').getContext('2d');
            new Chart(ctx, {
                type: 'line',
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
                                text: 'Ngày',
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
		<script type="module">
            const labels = [<c:forEach var="s" items="${orderStats}" varStatus="loop">"${s.label}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
                    const values = [<c:forEach var="s" items="${orderStats}" varStatus="loop">${s.total}<c:if test="${!loop.last}">,</c:if></c:forEach>];
            const ctx = document.getElementById('orderChart').getContext('2d');
            new Chart(ctx, {
                type: 'line',
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
                                text: 'Ngày',
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
        <jsp:include page="component/footer.jsp"/>
    </body>
</html>
