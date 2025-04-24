<%-- 
    Document   : shoppingcart
    Created on : Apr 3, 2025, 11:03:54 PM
    Author     : duong
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Shopping Cart</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <div class="breadcrumb__text">
                            <h2>Shopping Cart</h2>
                            <div class="breadcrumb__option">
                                <a href="./index.html">Home</a>
                                <span>Shopping Cart</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- Shoping Cart Section Begin -->
        <section class="shoping-cart spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="shoping__cart__table">
                            <table>
                                <thead>
                                    <tr>
                                        <th class="shoping__product">Products</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Total(VND)</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="cart" items="${cartList}">
                                        <tr>
                                            <td class="shoping__cart__item">
                                                <img src="ImageServlet?folder=pigs&file=${cart.pigsOffer.imageURL}" alt="" style="width: 100px; height: auto;">
                                                <h5>${cart.pigsOffer.name}</h5>
                                            </td>
                                            <td class="shoping__cart__price">
                                                $${cart.pigsOffer.retailPrice}
                                            </td>
                                            <td class="shoping__cart__quantity text-center">
                                                <h6>${cart.quantity}</h6>

                                            </td>
                                            <td class="shoping__cart__total">
                                                <fmt:formatNumber value="${cart.quantity * cart.pigsOffer.retailPrice}" type="number" groupingUsed="true" />
                                            </td>
                                            <td class="shoping__cart__item__close text-center">
                                                <div class="d-flex justify-content-center mb-2" style="gap: 6px;">
                                                    <a href="#"
                                                       class="btn btn-sm btn-warning open-update-modal"
                                                       title="Cập nhật số lượng"
                                                       data-cart-id="${cart.cartID}"
                                                       data-quantity="${cart.quantity}"
                                                       data-min="${cart.pigsOffer.minQuantity}"
                                                       data-max="${cart.pigsOffer.quantity}"
                                                       data-mode="${cart.quantity == cart.pigsOffer.quantity ? 'all' : 'custom'}">
                                                        <i class="fa fa-pencil"></i>
                                                    </a>
                                                    <a href="remove-cart?id=${cart.cartID}" class="btn btn-sm btn-danger" title="Xoá">
                                                        <i class="fa fa-trash"></i>
                                                    </a>
                                                </div>

                                                <form action="checkout" method="post">
                                                    <input type="hidden" name="cartId" value="${cart.cartID}">
                                                    <input type="hidden" name="offerId" value="${cart.pigsOffer.offerID}">
                                                    <input type="hidden" name="quantity" value="${cart.quantity}">
                                                    <button type="submit" class="btn btn-sm btn-success">Checkout</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <!-- Phân trang -->
                            <div class="shoping__cart__pagination text-center mt-4">
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <a href="cart?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
                                </c:forEach>
                            </div>
                        </div>

                    </div>
                </div>
            </div>              
        </section>
        <!-- Shoping Cart Section End -->

        <!-- Modal Cập nhật số lượng -->
        <div class="modal fade" id="updateQuantityModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content p-4">
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

        <jsp:include page="component/footer.jsp" />
        <script>
            $(document).ready(function () {
                $('.open-update-modal').click(function (e) {
                    e.preventDefault();

                    const cartId = $(this).data('cart-id');
                    const quantity = parseInt($(this).data('quantity'));
                    const min = parseInt($(this).data('min'));
                    const max = parseInt($(this).data('max'));

                    // Set cartId and quantity
                    $('#modalCartId').val(cartId);
                    $('#updateModalQuantity')
                            .attr('min', min)
                            .attr('max', max)
                            .val(quantity);

                    // Dynamically set the selected value for the dropdown
                    if (quantity < max) {
                        $('#updateModeSelect').val('custom');
                        $('#updateModalQuantity').prop('readonly', false);
                    } else {
                        $('#updateModeSelect').val('all');
                        $('#updateModalQuantity').val(max).prop('readonly', true);
                    }

                    // Show the modal
                    $('#updateQuantityModal').modal('show');
                });

                // Handle change in mode selection
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
            });

        </script>
    </body>
</html>
