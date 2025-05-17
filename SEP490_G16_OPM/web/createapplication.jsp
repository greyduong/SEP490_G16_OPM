<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    model.User user = (model.User) session.getAttribute("user");
    if (user == null || (user.getRoleID() != 4 && user.getRoleID() != 5)) {
        response.sendRedirect("home?error=access-denied");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Tạo đơn đề nghị - Chợ Heo Trực Tuyến</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <!-- Form tạo đơn đề nghị -->
        <section class="application-form spad">
            <div class="container">
                <h4 class="mb-3">Tạo đơn đề nghị mới</h4>

                <form action="CreateApplication" method="POST" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="purpose">Nội dung đề nghị</label>
                        <textarea name="content" class="form-control" minlength="10" maxlength="1000" required></textarea>
                    </div>

                    <div class="form-group">
                        <label for="file">Tệp đính kèm (không bắt buộc)</label>
                        <input type="file" name="file" class="form-control" accept=".pdf,.doc,.docx,.png,.jpg,.jpeg">
                    </div>

                    <button type="submit" class="site-btn">Gửi đơn đề nghị</button>
                </form>

                <c:if test="${not empty msg}">
                    <div class="alert alert-info mt-3">${msg}</div>
                </c:if>
            </div>
        </section>

        <jsp:include page="component/footer.jsp" />
    </body>
</html>
