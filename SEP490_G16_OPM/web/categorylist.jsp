<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>



<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh mục | Chợ Heo Trực Tuyến</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <!-- Form Thêm Danh Mục Mới -->
        <div class="row mb-4">
            <div class="col-lg-8 offset-lg-2">
                <h4 class="mb-3">Thêm danh mục mới</h4>
                <form action="category" method="post">
                    <input type="hidden" name="action" value="add" />
                    <div class="form-group">
                        <label for="name">Tên danh mục</label>
                        <input type="text" name="name" class="form-control" id="name" required>
                    </div>
                    <div class="form-group">
                        <label for="desc">Mô tả</label>
                        <textarea name="description" class="form-control" id="desc" rows="3"></textarea>
                    </div>
                    <button type="submit" class="site-btn">Thêm danh mục</button>
                </form>
            </div>
        </div>

        <!-- Khu vực quản lý danh mục -->
        <section class="product-details spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <h4 class="mb-3">Danh sách danh mục hiện có</h4>
                        <table class="table table-bordered table-hover">
                            <thead class="thead-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Tên</th>
                                    <th>Mô tả</th>
                                    <th style="width: 150px;">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="category" items="${categoryList}">
                                    <tr>
                                        <td>${category.categoryID}</td>
                                        <td>${category.name}</td>
                                        <td>${category.description}</td>
                                        <td>
                                            <!-- Nút sửa mở Modal -->
                                            <button type="button" class="btn btn-warning btn-sm" data-toggle="modal" data-target="#editCategoryModal"
                                                    data-id="${category.categoryID}" 
                                                    data-name="${category.name}" 
                                                    data-description="${category.description}">
                                                Sửa
                                            </button>
                                            <!-- Nút xóa -->
                                            <form action="category" method="post" style="display:inline-block;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa danh mục này không?');">
                                                <input type="hidden" name="action" value="delete" />
                                                <input type="hidden" name="categoryID" value="${category.categoryID}" />
                                                <button class="btn btn-danger btn-sm">Xóa</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty categoryList}">
                                    <tr>
                                        <td colspan="4" class="text-center text-muted">Chưa có danh mục nào.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>
        <!-- Kết thúc quản lý danh mục -->

        <!-- Modal chỉnh sửa danh mục -->
        <div class="modal fade" id="editCategoryModal" tabindex="-1" role="dialog" aria-labelledby="editCategoryModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editCategoryModalLabel">Chỉnh sửa danh mục</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Đóng">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="category" method="post">
                            <input type="hidden" name="action" value="update" />
                            <input type="hidden" name="categoryID" id="editCategoryID" />

                            <div class="form-group">
                                <label for="editName">Tên danh mục</label>
                                <input type="text" name="name" id="editName" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label for="editDescription">Mô tả</label>
                                <textarea name="description" id="editDescription" class="form-control" rows="3" required></textarea>
                            </div>
                            <button type="submit" class="site-btn">Cập nhật</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="component/footer.jsp" />

        <script>
            $('#editCategoryModal').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var categoryID = button.data('id');
                var name = button.data('name');
                var description = button.data('description');

                var modal = $(this);
                modal.find('#editCategoryID').val(categoryID);
                modal.find('#editName').val(name);
                modal.find('#editDescription').val(description);
            });
        </script>

    </body>
</html>
