<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>



<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Tạo đơn đề nghị - Chợ Heo Trực Tuyến</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <jsp:include page="component/library.jsp" />
        <style>
            #imagePreview {
                display: none;
                max-width: 300px;
                margin-top: 10px;
                border: 1px solid #ccc;
                padding: 5px;
            }
        </style>
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
                        <label for="image">Ảnh đơn</label>
                        <input type="file" id="image" name="image" accept="image/*" class="form-control" onchange="previewImage(event)">
                        <img id="imagePreview" src="#" alt="Ảnh xem trước" />
                        <c:if test="${not empty imageURLError}">
                            <small class="text-danger">${imageURLError}</small>
                        </c:if>
                    </div>

                    <button type="submit" class="site-btn">Gửi đơn đề nghị</button>
                </form>

                <c:if test="${not empty msg}">
                    <div class="alert alert-info mt-3">${msg}</div>
                </c:if>
            </div>
        </section>

        <jsp:include page="component/footer.jsp" />

        <script>
            function previewImage(event) {
                var reader = new FileReader();
                reader.onload = function () {
                    var output = document.getElementById('imagePreview');
                    output.src = reader.result;
                    output.style.display = 'block';
                };
                reader.readAsDataURL(event.target.files[0]);
            }
        </script>
    </body>
</html>