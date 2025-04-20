<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login-register.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Create Application - Online Pig Market</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Include necessary CSS and JS for the template -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">
    </head>
    <body>

        <jsp:include page="component/library.jsp" />
        <jsp:include page="component/header.jsp" />

        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container text-center">
                <div class="breadcrumb__text">
                    <h2>Create Application</h2>
                    <div class="breadcrumb__option">
                        <a href="home">Home</a>
                        <span>Create Application</span>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- Application Form Section Begin -->
        <section class="application-form spad">
            <div class="container">
                <h4 class="mb-3">Create a New Application</h4>
                <form action="CreateApplication" method="POST" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="purpose">Purpose</label>
                        <textarea name="content" class="form-control" minlength="10" maxlength="1000" required></textarea>
                    </div>

                    <!-- File Upload Field -->
                    <div class="form-group">
                        <label for="file">Upload File (Optional)</label>
                        <input type="file" name="file" class="form-control" accept=".pdf,.doc,.docx,.png,.jpg,.jpeg">
                    </div>

                    <button type="submit" class="site-btn">Submit Application</button>
                </form>
            </div>
            <c:if test="${not empty msg}">
                <div class="alert alert-info">${msg}</div>
            </c:if>
        </section>
        <!-- Application Form Section End -->

        <jsp:include page="component/footer.jsp" />

        <!-- Include necessary JS -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>

    </body>
</html>
