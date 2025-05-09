<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Trang trại của tôi | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <section class="featured spad">
            <div class="container">
                <div class="section-title">
                    <h2>Danh sách trang trại</h2>
                </div>
                <div class="container mb-4">
                    <div class="row justify-content-center">
                        <div class="col-lg-8">
                            <div class="hero__search">
                                <div class="hero__search__form">
                                    <form action="dealer-farms" method="get">
                                        <input type="text" name="search" placeholder="Tìm kiếm tên trang trại..." value="${param.search}">
                                        <button type="submit" class="site-btn">SEARCH</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row featured__filter">
                    <c:forEach var="f" items="${farmList}">
                        <div class="col-lg-3 col-md-4 col-sm-6 mix">
                            <div class="featured__item">
                                <div class="featured__item__pic set-bg" data-setbg="${f.imageURL}">
                                    <ul class="featured__item__pic__hover">
                                        <li>
                                            <a href="farm-detail?id=${f.farmID}">
                                                <i class="fa fa-eye"></i>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                                <div class="featured__item__text">
                                    <h6><a href="farm-detail?id=${f.farmID}">${f.farmName}</a></h6>
                                    <p>Địa điểm: ${f.location}</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${empty farmList}">
                        <div class="col-12 text-center text-muted">Không tìm thấy trang trại nào.</div>
                    </c:if>
                </div>

                <c:if test="${not empty totalPages}">
                    <div class="pagination-area mt-4 text-center">
                        <nav>
                            <ul class="pagination justify-content-center">
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="dealer-farms?page=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </div>
                </c:if>
            </div>
        </section>

        <jsp:include page="component/footer.jsp" />

        <script>
            $('.set-bg').each(function () {
                var bg = $(this).attr("data-setbg");
                $(this).css('background-image', 'url(' + bg + ')');
            });
        </script>
        <c:if test="${empty farmList}">
            <div class="text-center text-danger">Không có trang trại nào.</div>
        </c:if>
    </body>
</html>
