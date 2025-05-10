<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Giỏ hàng</title>
        <jsp:include page="component/library.jsp" />
        <style>
            .cart-item-image {
                width: 120px !important;
                height: auto;
                border-radius: 5px;
                border: 1px solid #ddd;
            }

            .cart-item-name {
                font-size: 1.1rem;
                font-weight: 600;
                color: #333;
            }
        </style>
    </head>
    <body>
        <jsp:include page="component/header.jsp" />

        <section class="shoping-cart py-4">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">

                        <h2 class="mb-4 font-weight-bold text-center">🛒 Giỏ hàng của bạn</h2>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger text-center">${error}</div>
                        </c:if>

                        <!-- Form tìm kiếm và sắp xếp -->
                        <form method="get" action="cart" class="form-inline mb-3 d-flex gap-2">
                            <input type="text" name="search" class="form-control" placeholder="Tìm kiếm..." value="${param.search}">
                            <select name="sort" class="form-control">
                                <option value="">Sắp xếp</option>
                                <option value="quantity_asc" ${param.sort == 'quantity_asc' ? 'selected' : ''}>SL ↑</option>
                                <option value="quantity_desc" ${param.sort == 'quantity_desc' ? 'selected' : ''}>SL ↓</option>
                                <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá ↑</option>
                                <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá ↓</option>
                                <option value="total_asc" ${param.sort == 'total_asc' ? 'selected' : ''}>Tổng ↑</option>
                                <option value="total_desc" ${param.sort == 'total_desc' ? 'selected' : ''}>Tổng ↓</option>
                            </select>
                            <button type="submit" class="btn btn-primary">Tìm</button>
                            <a href="cart" class="btn btn-secondary">Xoá lọc</a>
                        </form>

                        <div class="shoping__cart__table">
                            <div class="table-responsive">
                                <table class="table table-bordered text-center align-middle">
                                    <thead class="thead-dark">
                                        <tr>
                                            <th>Sản phẩm</th>
                                            <th>Số lượng</th><th>Tổng giá</th>

                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="cart" items="${cartList}">
                                            <tr>
                                                <td class="text-left">
                                                    <div class="d-flex align-items-center" style="gap: 10px;">
                                                        <img src="${cart.pigsOffer.imageURL}" alt="image" class="cart-item-image">
                                                        <div class="ml-2">
                                                            <a href="#" class="offer-detail-link cart-item-name"
                                                               data-name="${cart.pigsOffer.name}"
                                                               data-image="${cart.pigsOffer.imageURL}"
                                                               data-price="${cart.pigsOffer.retailPrice}"
                                                               data-min="${cart.pigsOffer.minQuantity}"
                                                               data-quantity="${cart.pigsOffer.quantity}"
                                                               data-description="${cart.pigsOffer.description}"
                                                               data-farm="${cart.pigsOffer.farm.farmName}"
                                                               data-category="${cart.pigsOffer.category.name}"
                                                               data-start="${cart.pigsOffer.startDate}"
                                                               data-end="${cart.pigsOffer.endDate}"
                                                               data-total="${cart.pigsOffer.totalOfferPrice}"
                                                               data-deposit="${cart.pigsOffer.minDeposit}"
                                                               data-retail="${cart.pigsOffer.retailPrice}">
                                                                ${cart.pigsOffer.name}
                                                            </a>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>${cart.quantity}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${cart.quantity == cart.pigsOffer.quantity}">
                                                            <fmt:formatNumber value="${cart.pigsOffer.totalOfferPrice}" type="number" groupingUsed="true" /> VND
                                                        </c:when>
                                                        <c:otherwise>
                                                            <fmt:formatNumber value="${cart.quantity * cart.pigsOffer.retailPrice}" type="number" groupingUsed="true" /> VND
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <div class="row no-gutters mb-2">
                                                        <div class="col-6 pr-1">
                                                            <a href="#" class="btn btn-warning btn-sm w-100 open-update-modal"
                                                               data-cart-id="${cart.cartID}"
                                                               data-name="${cart.pigsOffer.name}"
                                                               data-quantity="${cart.quantity}"
                                                               data-min="${cart.pigsOffer.minQuantity}"
                                                               data-max="${cart.pigsOffer.quantity}"
                                                               data-mode="${cart.quantity == cart.pigsOffer.quantity ? 'all' : 'custom'}">
                                                                <i class="fa fa-pencil-alt"></i>
                                                            </a>
                                                        </div>
                                                        <div class="col-6 pl-1">
                                                            <a href="remove-cart?id=${cart.cartID}" class="btn btn-danger btn-sm w-100">
                                                                <i class="fa fa-trash"></i>
                                                            </a>
                                                        </div>
                                                    </div>
                                                    <form action="checkout" method="post">
                                                        <input type="hidden" name="cartId" value="${cart.cartID}" />
                                                        <input type="hidden" name="offerId" value="${cart.pigsOffer.offerID}" />
                                                        <input type="hidden" name="quantity" value="${cart.quantity}" />
                                                        <button type="submit" class="btn btn-success btn-sm w-100">Thanh toán</button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>


                                </table>
                            </div>

                            <!-- Phân trang giữ search & sort -->
                            <div class="shoping__cart__pagination text-center mt-4">
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <a href="cart?page=${i}&search=${fn:escapeXml(param.search)}&sort=${fn:escapeXml(param.sort)}"
                                       class="${i == currentPage ? 'active' : ''}">${i}</a>
                                </c:forEach>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>

        <!-- Modal cập nhật số lượng -->
        <div class="modal fade" id="updateQuantityModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content p-4">
                    <h5 id="updateOfferName" class="mb-2 font-weight-bold text-center text-primary"></h5>
                    <p class="text-center mb-3">
                        <span>Số lượng mua tối thiểu: <strong id="updateMin"></strong></span><br>
                        <span>Số lượng tối đa còn lại: <strong id="updateMax"></strong></span>
                    </p>


                    <form action="update-cart" method="post" id="updateCartForm">
                        <input type="hidden" name="cartId" id="modalCartId" />
                        <input type="hidden" name="page" value="${currentPage}" />
                        <select name="mode" id="updateModeSelect" class="form-control mb-2">
                            <option value="all">Mua toàn bộ</option>
                            <option value="custom">Chọn số lượng</option>
                        </select>
                        <input type="number" name="quantity" id="updateModalQuantity" class="form-control" />
                        <button type="submit" class="btn btn-success w-100 mt-3">Cập nhật</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal chi tiết sản phẩm -->
        <div class="modal fade" id="offerDetailModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
                <div class="modal-content p-4">
                    <div class="modal-header">
                        <h5 class="modal-title">Chi tiết sản phẩm</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body row">
                        <div class="col-md-4">
                            <img id="offerImage" src="" class="img-fluid rounded border" />
                        </div>
                        <div class="col-md-8">
                            <h4 id="offerName" class="mb-2"></h4>
                            <p><strong>Giá bán lẻ:</strong> <span id="offerRetail"></span> VND</p>
                            <p><strong>Giá tổng:</strong> <span id="offerTotal"></span> VND</p>
                            <p><strong>Đặt cọc tối thiểu:</strong> <span id="offerDeposit"></span> VND</p>
                            <p><strong>Số lượng tối thiểu:</strong> <span id="offerMin"></span></p>
                            <p><strong>Số lượng còn lại:</strong> <span id="offerQuantity"></span></p>
                            <p><strong>Trang trại:</strong> <span id="offerFarm"></span></p>
                            <p><strong>Danh mục:</strong> <span id="offerCategory"></span></p>
                            <p><strong>Ngày bắt đầu:</strong> <span id="offerStart"></span></p>
                            <p><strong>Ngày kết thúc:</strong> <span id="offerEnd"></span></p>
                            <p><strong>Mô tả:</strong><br><span id="offerDescription"></span></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="component/footer.jsp" />

        <script>
            function formatVND(number) {
                return Number(number).toLocaleString('vi-VN') + ' VND';
            }

            $(document).ready(function () {
                $('.open-update-modal').click(function (e) {
                    e.preventDefault();
                    const cartId = $(this).data('cart-id');
                    const quantity = parseInt($(this).data('quantity'));
                    const min = parseInt($(this).data('min'));
                    const max = parseInt($(this).data('max'));
                    const name = $(this).data('name');

                    $('#modalCartId').val(cartId);
                    $('#updateModalQuantity').attr('min', min).attr('max', max).val(quantity);

                    $('#updateOfferName').text(name);
                    $('#updateMin').text(min);
                    $('#updateMax').text(max);

                    if (quantity < max) {
                        $('#updateModeSelect').val('custom');
                        $('#updateModalQuantity').prop('readonly', false);
                    } else {
                        $('#updateModeSelect').val('all');
                        $('#updateModalQuantity').val(max).prop('readonly', true);
                    }

                    $('#updateQuantityModal').modal('show');
                });

                $('#updateModeSelect').on('change', function () {
                    const mode = $(this).val();
                    const max = parseInt($('#updateModalQuantity').attr('max'));
                    const min = parseInt($('#updateModalQuantity').attr('min'));

                    if (mode === 'custom') {
                        $('#updateModalQuantity').prop('readonly', false).val(min);
                    } else {
                        $('#updateModalQuantity').prop('readonly', true).val(max);
                    }
                });

                $('.offer-detail-link').click(function (e) {
                    e.preventDefault();
                    $('#offerName').text($(this).data('name'));
                    $('#offerImage').attr('src', $(this).data('image'));
                    $('#offerRetail').text(formatVND($(this).data('retail')));
                    $('#offerDeposit').text(formatVND($(this).data('deposit')));
                    $('#offerTotal').text(formatVND($(this).data('total')));
                    $('#offerMin').text($(this).data('min'));
                    $('#offerQuantity').text($(this).data('quantity'));
                    $('#offerDescription').text($(this).data('description'));
                    $('#offerFarm').text($(this).data('farm'));
                    $('#offerCategory').text($(this).data('category'));
                    $('#offerStart').text($(this).data('start'));
                    $('#offerEnd').text($(this).data('end'));
                    $('#offerDetailModal').modal('show');
                });
            });
        </script>
        <script>
            document.querySelectorAll("form").forEach(form => {
                form.addEventListener("submit", function () {
                    if (document.getElementById("loading-overlay"))
                        return; // Đã có overlay thì không tạo nữa

                    const overlay = document.createElement("div");
                    overlay.id = "loading-overlay"; // Gán ID để xử lý khi back
                    overlay.style.position = "fixed";
                    overlay.style.top = 0;
                    overlay.style.left = 0;
                    overlay.style.width = "100%";
                    overlay.style.height = "100%";
                    overlay.style.backgroundColor = "rgba(0,0,0,0.5)";
                    overlay.style.display = "flex";
                    overlay.style.justifyContent = "center";
                    overlay.style.alignItems = "center";
                    overlay.style.zIndex = 9999;

                    const spinner = document.createElement("div");
                    spinner.innerHTML = `
                <div class="spinner-border text-light" role="status" style="width: 3rem; height: 3rem;">
                    <span class="sr-only">Loading...</span>
                </div>
                <div class="text-white mt-3">Đang xử lý, vui lòng chờ...</div>
            `;
                    spinner.style.textAlign = "center";

                    overlay.appendChild(spinner);
                    document.body.appendChild(overlay);
                });
            });

            // Xử lý khi người dùng quay lại trang bằng nút back
            window.addEventListener("pageshow", function () {
                const overlay = document.getElementById("loading-overlay");
                if (overlay)
                    overlay.remove();
            });
        </script>

    </body>
</html>
