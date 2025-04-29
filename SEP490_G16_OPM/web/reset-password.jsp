<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu | Online Pig Market</title>
    <jsp:include page="component/library.jsp" />
</head>
<body>
    <jsp:include page="component/header.jsp" />
    <main class="w-90 mx-auto mb-5">
        <div class="ring-1 ring-slate-500/10 rounded-lg p-4 max-w-md mx-auto">
            <div class="font-bold text-xl mb-2">Đặt lại mật khẩu</div>
            <div class="text-slate-500 mb-4">Nhập mật khẩu mới cho tài khoản của bạn</div>

            <c:if test="${not empty msg}">
                <div class="text-red-600 text-sm mb-3">${msg}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/reset-password" method="POST">
                <div class="flex flex-col mb-4">
                    <label for="password" class="text-sm font-medium mb-1">Mật khẩu mới</label>
                    <input id="password" name="password" type="password" 
                           class="focus:!border-slate-400 transition-all rounded-lg border border-slate-300 py-2 px-3" 
                           required>
                </div>

                <div class="flex flex-col mb-4">
                    <label for="confirmPassword" class="text-sm font-medium mb-1">Xác nhận mật khẩu mới</label>
                    <input id="confirmPassword" name="confirmPassword" type="password" 
                           class="focus:!border-slate-400 transition-all rounded-lg border border-slate-300 py-2 px-3" 
                           required>
                </div>

                <button type="submit" 
                        class="!bg-gray-700 hover:!bg-gray-800 text-white w-full rounded-lg py-2">
                    Đặt lại mật khẩu
                </button>
            </form>

            <div class="text-center mt-4">
                <a class="!text-slate-500 hover:!text-slate-800 text-sm" href="${pageContext.request.contextPath}/login">
                    Quay lại đăng nhập
                </a>
            </div>
        </div>
    </main>
    <jsp:include page="component/footer.jsp" />
</body>
</html>
