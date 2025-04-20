<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    // Access control: Only allow Staff (2) and Manager (3)
    model.User user = (model.User) session.getAttribute("user");
    if (user == null || (user.getRoleID() != 2 && user.getRoleID() != 3)) {
        response.sendRedirect("login-register.jsp?error=access-denied");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Category List - Online Pig Market</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css2?family=Cairo:wght@200;300;400;600;900&display=swap" rel="stylesheet">

        <!-- Ogani CSS Styles -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="css/elegant-icons.css" type="text/css">
        <link rel="stylesheet" href="css/nice-select.css" type="text/css">
        <link rel="stylesheet" href="css/jquery-ui.min.css" type="text/css">
        <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
        <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">
    </head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <body>

        <jsp:include page="component/library.jsp" />
        <jsp:include page="component/header.jsp" />

        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container text-center">
                <div class="breadcrumb__text">
                    <h2>Category List</h2>
                    <div class="breadcrumb__option">
                        <a href="home">Home</a>
                        <span>Category List</span>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- Add New Category Form -->
        <div class="row mb-4">
            <div class="col-lg-8 offset-lg-2">
                <h4 class="mb-3">Add New Category</h4>
                <form action="category" method="post">
                    <input type="hidden" name="action" value="add" />
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" name="name" class="form-control" id="name" required>
                    </div>
                    <div class="form-group">
                        <label for="desc">Description</label>
                        <textarea name="description" class="form-control" id="desc" rows="3"></textarea>
                    </div>
                    <button type="submit" class="site-btn">Add Category</button>
                </form>
            </div>
        </div>

        <!-- Category Management Section Begin -->
        <section class="product-details spad">
            <div class="container">
                <!-- Display Categories as Table -->
                <div class="row">
                    <div class="col-lg-12">
                        <h4 class="mb-3">Existing Categories</h4>
                        <table class="table table-bordered table-hover">
                            <thead class="thead-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th style="width: 150px;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="category" items="${categoryList}">
                                    <tr>
                                        <td>${category.categoryID}</td>
                                        <td>${category.name}</td>
                                        <td>${category.description}</td>
                                        <td>
                                            <!-- Edit Button triggers Modal -->
                                            <button type="button" class="btn btn-warning btn-sm" data-toggle="modal" data-target="#editCategoryModal"
                                                    data-id="${category.categoryID}" 
                                                    data-name="${category.name}" 
                                                    data-description="${category.description}">
                                                Edit
                                            </button>
                                            <!-- Delete Button -->
                                            <form action="category" method="post" style="display:inline-block;" onsubmit="return confirm('Are you sure you want to delete this category?');">
                                                <input type="hidden" name="action" value="delete" />
                                                <input type="hidden" name="categoryID" value="${category.categoryID}" />
                                                <button class="btn btn-danger btn-sm">Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty categoryList}">
                                    <tr>
                                        <td colspan="4" class="text-center text-muted">No categories available.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>
        <!-- Category Management Section End -->

        <!-- Edit Category Modal -->
        <div class="modal fade" id="editCategoryModal" tabindex="-1" role="dialog" aria-labelledby="editCategoryModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editCategoryModalLabel">Edit Category</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="category" method="post">
                            <input type="hidden" name="action" value="update" />
                            <input type="hidden" name="categoryID" id="editCategoryID" />

                            <div class="form-group">
                                <label for="editName">Name</label>
                                <input type="text" name="name" id="editName" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label for="editDescription">Description</label>
                                <textarea name="description" id="editDescription" class="form-control" rows="3" required></textarea>
                            </div>
                            <button type="submit" class="site-btn">Update Category</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="component/footer.jsp" />

        <!-- Ogani JS -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.nice-select.min.js"></script>
        <script src="js/jquery-ui.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>

        <!-- Populate Edit Modal -->
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
