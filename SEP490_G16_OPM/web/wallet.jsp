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
                <div class="text-xl font-bold mb-3">Lịch sử nạp</div>
                <c:if test="${topup.data.isEmpty()}">
                    <div class="text-slate-500">Không có lịch sử nạp</div>
                </c:if>
                <div>
                    <c:forEach var="item" items="${topup.data}">
                        <div class="border-b border-slate-300 p-3">
                            <div class="text-slate-500 text-sm">${item.getCreatedAtAsString('HH:mm dd/MM/yyyy')}</div>
                            <div>
                                <c:choose>
                                    <c:when test="${item.status == 'Cancelled'}">
                                        <div class="text-red-500 font-bold text-xl"><fmt:formatNumber currencyCode="VND" value="${item.amount}" />đ</div>
                                    </c:when>
                                    <c:when test="${item.status == 'Success'}">
                                        <div class="text-lime-600 font-bold text-xl">+<fmt:formatNumber currencyCode="VND" value="${item.amount}" />đ</div>
                                    </c:when>
                                        <c:when test="${item.status == 'Pending'}">
                                        <div class="text-gray-500 font-bold text-xl"><fmt:formatNumber currencyCode="VND" value="${item.amount}" />đ</div>
                                    </c:when>
                                </c:choose>
                                <div class="text-slate-600">Mã giao dịch: ${item.txnRef}</div>
                                <div class="text-slate-600">Trạng thái:
                                    <c:choose>
                                        <c:when test="${item.status == 'Cancelled'}"><span class="text-red-600">Hủy</span></c:when>
                                        <c:when test="${item.status == 'Success'}"><span class="text-lime-600">Thành công</span></c:when>
                                        <c:when test="${item.status == 'Pending'}"><span class="text-gray-500">Chờ thanh toán</span></c:when>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />     
    </body>
</html>
