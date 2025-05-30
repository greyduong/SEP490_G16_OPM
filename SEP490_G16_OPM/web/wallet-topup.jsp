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
        <main class="text-center mb-5">
            <div class="font-bold text-2xl mb-3">Nạp tiền vào ví</div>
            <c:if test="${not empty error}">
                <div class="mb-2 text-red-600 font-bold">${error}</div>
            </c:if>
            <form class="mb-3" id="topupForm" method="POST">
                <label class="mb-2 flex gap-2"><input <c:if test="${method == 'picker' or empty method}">checked</c:if> type="radio" name="method" value="picker"/><span class="ms-1">Chọn mệnh giá</span></label>
                    <div class="flex justify-center mb-3">
                        <div class="grid grid-cols-3 w-70 gap-3 *:has-[:disabled]:bg-slate-100 *:has-[:disabled]:text-black *:has-[:checked]:bg-slate-700 *:has-[:checked]:text-white *:p-3 *:shadow *:transition-all *:after:font-light *:after:text-xs *:after:block *:after:content-['VND'] *:font-medium *:rounded-sm *:hover:cursor-pointer *:hover:bg-slate-600 *:hover:text-white">
                            <label><input class="hidden" type="radio" name="amount" value="10000" />10,000</label>
                            <label><input class="hidden" type="radio" name="amount" value="20000" />20,000</label>
                            <label><input class="hidden" type="radio" name="amount" value="50000" />50,000</label>
                            <label><input class="hidden" type="radio" name="amount" value="100000" />100,000</label>
                            <label><input class="hidden" type="radio" name="amount" value="200000" />200,000</label>
                            <label><input class="hidden" type="radio" name="amount" value="500000" />500,000</label>
                        </div>
                    </div>
                    <label class="mb-2 flex gap-2"><input <c:if test="${method == 'input'}">checked</c:if> type="radio" name="method" value="input" /><span class="ms-1">Hoặc nhập mệnh giá (là bội số của 10,000)</span></label>
                    <div class="flex justify-center mb-3">
                        <div>
                            <div class="border border-slate-300 transition-all px-2 py-1 has-[:focus]:!border-slate-400 rounded-sm has-[:disabled]:bg-slate-100">
                                <input disabled name="amountInput" value="${amountInput}" class="block w-full" type="number" />
                        </div>
                    </div>
                </div>
                <button type="submit" class="bg-slate-600 transition-all hover:bg-slate-700 text-white !p-2 w-35 rounded-sm">Xác nhận</button>
            </form>
            <div>
                <a class="!no-underline text-xs !text-slate-500" href="${pageContext.request.contextPath}/wallet"><span class="mdi mdi-chevron-left"></span>Quay về lịch sử giao dịch</a>
            </div>
        </main>
        <script>
            function toggle() {
                const method = document.forms.topupForm.method.value;
                if (method === "picker") {
                    $("input[name=amount]").attr("disabled", false);
                    $("input[name=amountInput]").attr("disabled", true).val("");
                } else {
                    $("input[name=amount]").attr("disabled", true);
                    $("input[name=amountInput]").attr("disabled", false);
                }
            }
            toggle();
            $("input[name=method]").on("click", function () {
                toggle();
            });
        </script>
        <jsp:include page="component/spinner.jsp" />
        <jsp:include page="component/footer.jsp" />
    </body>
</html>
