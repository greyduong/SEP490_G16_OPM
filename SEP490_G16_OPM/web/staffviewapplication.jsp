<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    model.User user = (model.User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login-register.jsp");
        return;
    }
    if (user.getRoleID() != 2 && user.getRoleID() != 3) {
        response.sendRedirect("login-register.jsp?error=access-denied");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>View Application - Online Pig Market</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <script src="js/jquery-3.3.1.min.js"></script>
    </head>
    <body>  

        <jsp:include page="component/library.jsp" />
        <jsp:include page="component/header.jsp" />
        <c:if test="${not empty sessionScope.successMsg}">
            <div class="alert alert-success">${sessionScope.successMsg}</div>
            <c:remove var="successMsg" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.errorMsg}">
            <div class="alert alert-danger">${sessionScope.errorMsg}</div>
            <c:remove var="errorMsg" scope="session"/>
        </c:if>

        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container text-center">
                <div class="breadcrumb__text">
                    <h2>Application List</h2>
                    <div class="breadcrumb__option">
                        <a href="home">Home</a>
                        <span>Applications</span>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- Application Display Table -->
        <section class="product-details spad">
            <div class="container">
                <h4 class="mb-3">Applications</h4>
                <table class="table table-bordered table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>Purpose</th>
                            <th>Create Date</th>
                            <th>File</th>
                            <th>Status</th>
                            <th>Processing Date</th>
                            <th>Reply</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="application" items="${applicationList}">
                            <tr>
                                <td>${application.content}</td>
                                <td><fmt:formatDate value="${application.sentAt}" pattern="dd/MM/yyyy" /></td>
                                <td>
                                    <c:if test="${not empty application.file}">
                                        <a href="${application.file}" target="_blank">${application.file}</a>
                                    </c:if>
                                    <c:if test="${empty application.file}">
                                        No file available
                                    </c:if>
                                </td>
                                <td>${application.status}</td>
                                <td><fmt:formatDate value="${application.processingDate}" pattern="dd/MM/yyyy" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty application.reply}">
                                            ${application.reply}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">No reply</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${application.status eq 'Pending'}">
                                        <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#applicationModal" data-id="${application.applicationID}" data-status="${application.status}" data-content="${application.content}">Process</button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty applicationList}">
                            <tr>
                                <td colspan="7" class="text-center text-muted">No applications available.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>

        <!-- Modal -->
        <div class="modal fade" id="applicationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Process Application</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="applicationForm" action="ProcessApplication" method="POST">
                            <input type="hidden" id="applicationId" name="applicationId" />

                            <p><strong>Purpose:</strong> <span id="appContent"></span></p>

                            <div class="form-group">
                                <label for="replyText">Reply:</label>
                                <textarea class="form-control" id="replyText" name="reply" rows="3" placeholder="Write your reply here..."></textarea>
                            </div>

                            <!-- Status Row -->
                            <div class="form-group d-flex align-items-center">
                                <label for="statusSelect" class="mr-2 mb-0" style="min-width: 60px;">Status:</label>
                                <select class="form-control" id="statusSelect" name="action"
                                        style="height: 38px; padding: 6px 12px; line-height: 1.5; appearance: none;">
                                    <option value="approve">Approve</option>
                                    <option value="reject">Reject</option>
                                </select>
                            </div>

                            <!-- Submit Button Row -->
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary">Submit Reply</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="component/footer.jsp" />

        <script src="js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>

        <script>
            // On modal show, populate fields
            $('#applicationModal').on('show.bs.modal', function (event) {
                const button = $(event.relatedTarget);
                const appId = button.data('id');
                const appContent = button.data('content');


                $('#applicationId').val(appId);
                $('#appContent').text(appContent);
            });

            // Intercept form submit with confirmation
            document.getElementById("applicationForm").addEventListener("submit", function (e) {
                e.preventDefault(); // prevent default submit

                const status = document.getElementById("statusSelect").value;
                const confirmText = `Are you sure you want to ${status.toUpperCase()} this application?`;

                if (confirm(confirmText)) {
                    this.submit(); // continue submit if confirmed
                }
            });
        </script>

    </body>
</html>
