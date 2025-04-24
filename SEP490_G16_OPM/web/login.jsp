<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <main class="w-90 mx-auto mb-5">
            <div class="ring-1 ring-slate-500/10 rounded-lg p-4">
                <div class="font-bold text-xl">Đăng nhập</div>
                <div class="text-slate-500 mb-3">Nhập tài khoản và mật khẩu để đăng nhập vào tài khoản của bạn</div>
                <c:if test="${not empty error}">
                    <div class="text-red-600 text-sm mb-3">${error}</div>
                </c:if>
                <form action="" method="POST">
                    <div class="flex flex-col mb-3">
                        <label for="username" class="text-sm font-medium">
                            <span class="mdi mdi-account"></span>
                            Tài khoản
                        </label>
                        <input value="${username}" id="username" name="username" class="focus:!border-slate-400 transition-all rounded-lg border border-slate-300 py-1 px-3" type="text">
                    </div>
                    <div class="flex flex-col mb-3">
                        <label for="password" class="text-sm font-medium ">
                            <span class="mdi mdi-lock"></span>
                            Mật khẩu
                        </label>
                        <input value="${password}" id="password" name="password" type="password" class="focus:!border-slate-400 transition-all rounded-lg border border-slate-300 py-1 px-3">
                    </div>
                    <div class="text-center mb-3">Chưa có tài khoản? <a class="!text-slate-500 hover:!text-slate-800" href="${pageContext.request.contextPath}/register">Đăng ký</a></div>
                    <button class="!bg-gray-700 hover:!bg-gray-800 text-white w-full rounded-lg py-1">Đăng nhập</button>
                </form>
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />
    </body>
</html>