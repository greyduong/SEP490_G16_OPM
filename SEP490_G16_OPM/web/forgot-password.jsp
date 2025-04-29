<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu | Online Pig Market</title>
    <jsp:include page="component/library.jsp" />
</head>
<body>
    <jsp:include page="component/header.jsp" />

    <main class="w-90 mx-auto mb-5">
        <div class="ring-1 ring-slate-500/10 rounded-lg p-4 max-w-md mx-auto">
            <div class="font-bold text-xl mb-2">Quên mật khẩu</div>
            <div class="text-slate-500 mb-4">Nhập email tài khoản của bạn để lấy mã OTP.</div>

            <!-- ✅ Nếu có lỗi hoặc thông báo -->
            <c:if test="${not empty error}">
                <div class="text-red-600 text-sm mb-3">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="text-green-600 text-sm mb-3">${success}</div>
            </c:if>

            <!-- ✅ Form gửi email -->
            <form action="${pageContext.request.contextPath}/forgot-password" method="POST">
                <div class="flex flex-col mb-4">
                    <label for="email" class="text-sm font-medium mb-1">
                        <span class="mdi mdi-email"></span> Email
                    </label>
                    <input id="email" name="email" type="email" 
                           class="focus:!border-slate-400 transition-all rounded-lg border border-slate-300 py-2 px-3" 
                           placeholder="Nhập email của bạn" required>
                </div>

                <button type="submit" 
                        class="!bg-gray-700 hover:!bg-gray-800 text-white w-full rounded-lg py-2">
                    Gửi OTP
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
