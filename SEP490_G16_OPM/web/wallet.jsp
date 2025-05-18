<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Wallet Topup | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <main>
            <div class="container">
                <div class="text-xl font-bold mb-3">Ví của bạn</div>
                <div class="!bg-lime-50 p-3 rounded-sm flex items-center mb-3">
                    <div>
                        <div class="text-xs text-lime-600">Số tiền trong ví</div>
                        <div class="text-3xl text-lime-600 font-bold">
                            <fmt:formatNumber currencyCode="VND" value="${sessionScope.user.wallet}" />đ
                        </div>
                    </div>
                    <div class="ml-auto">
                        <a href="${pageContext.request.contextPath}/wallet-topup" class="!no-underline p-2 !bg-lime-600 !text-white hover:!bg-lime-700 transition-all rounded-sm">
                            <span class="mdi mdi-plus"></span>
                            Nạp vào ví
                        </a>
                    </div>
                </div>
                <div class="text-xl font-bold mb-2">Lịch sử nạp</div>
                <div class="mb-3">
					<c:if test="${topup.data.isEmpty()}">
						<div class="text-slate-500">Không có lịch sử nạp</div>
					</c:if>
                    <c:forEach var="item" items="${topup.data}">
						<table class="table table-bordered">
							<thead class="thead-dark">
								<tr>
									<th>Thời gian</th>
									<th>Mã giao dịch</th>
									<th>Lượng tiền</th>
									<th>Trạng thái</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										${item.getCreatedAtAsString('HH:mm dd/MM/yyyy')}
									</td>
									<td>
										${item.txnRef}
									</td>
									<td>
										<fmt:formatNumber currencyCode="VND" value="${item.amount}" />đ
									</td>
									<td>

										<c:choose>
											<c:when test="${item.status == 'Cancelled'}"><span class="text-red-600">Hủy</span></c:when>
											<c:when test="${item.status == 'Success'}"><span class="text-lime-600">Thành công</span></c:when>
											<c:when test="${item.status == 'Pending'}"><span class="text-gray-500">Chờ thanh toán</span></c:when>
										</c:choose>
									</td>
								</tr>
							</tbody>
						</table>
					</c:forEach>
				</div>
				<hr>
				<div class="text-xl font-bold mb-2">Lịch sử sử dụng</div>
                <div class="mb-3">
					<c:if test="${topup.data.isEmpty()}">
						<div class="text-slate-500">Không có lịch sử sử dụng</div>
					</c:if>
                    <c:forEach var="item" items="${use.data}">
						<table class="table table-bordered">
							<thead class="thead-dark">
								<tr>
									<th>Thời gian</th>
									<th>Mã giao dịch</th>
									<th>Lượng tiền</th>
									<th>Ghi chú</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										${item.getCreatedAtAsString('HH:mm dd/MM/yyyy')}
									</td>
									<td>
										${item.transactionID}
									</td>
									<td>
										<fmt:formatNumber currencyCode="VND" value="${item.amount}" />đ
									</td>
									<td>
										${item.note}
									</td>
								</tr>
							</tbody>
						</table>
					</c:forEach>
				</div>
			</div>
		</main>
		<jsp:include page="component/footer.jsp" />     
	</body>
</html>
