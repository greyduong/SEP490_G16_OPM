<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Online Pig Market">
        <meta name="keywords" content="pig, market">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
        <style>
            .modal-dialog-centered-custom {
                display: flex;
                align-items: center;
                justify-content: center;
                min-height: 100vh;
            }

            .modal-content {
                width: 100%;
                max-width: 350px;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            }

            #addToCartModal .form-control,
            #addToCartModal .btn {
                font-size: 14px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <c:if test="${not empty msg}">
            <div class="alert alert-warning">${msg}</div>
        </c:if>
		<div class="px-5">
			<div class="flex relative">
				<div class="sticky top-0 rounded-sm border border-slate-300 p-3 w-65 flex flex-col gap-5">
					<form class="flex flex-col gap-2">
						<label class="font-bold text-slate-600 text-sm !m-0">Sắp xếp</label>
						<select name="sort" class="form-control" onchange="this.form.submit()">
							<option value="none">-- Chọn --</option>
							<option value="quantity_asc" ${param.sort == 'quantity_asc' ? 'selected' : ''}>Số lượng ↑</option>
							<option value="quantity_desc" ${param.sort == 'quantity_desc' ? 'selected' : ''}>Số lượng ↓</option>
							<option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá cả ↑</option>
							<option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá cả ↓</option>
						</select>
					</form>
					<div class="flex flex-col gap-2">
						<div class="font-bold text-sm text-slate-600">Loại heo</div>
						<div class="*:!text-slate-600 flex flex-col gap-2">
							<a href="home">Tất cả</a>
							<c:forEach var="c" items="${categoryList}">
								<a href="home?categoryName=${c.name}">${c.name}</a>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="grow-1">
					<div class="mx-3 mb-2">
						<form action="home" method="get" class="inline-flex items-center border border-slate-300 has-[:focus]:!border-slate-400 transitio-all rounded-sm overflow-hidden">
							<input class="p-2" type="text" name="keyword" placeholder="Nhập tên chào bán..." value="${param.keyword}">
							<c:if test="${not empty param.categoryName}">
								<input type="hidden" name="categoryName" value="${param.categoryName}" />
							</c:if>
							<button type="submit" class="px-3 py-2 flex items-center bg-lime-600 text-white"><span class="mdi mdi-magnify text-lg"></span></button>
						</form>
					</div>
					<div class="grid grid-cols-4 gap-5 grow-1 p-3">
						<c:forEach var="o" items="${offerList}">
							<div class="border border-slate-300 flex flex-col rounded-sm overflow-hidden">
								<div class="relative">
									<img src="${o.imageURL}" class="object-cover h-70" />
									<a class="absolute block !text-slate-200 !text-shadow-sm !text-shadow-slate-600 bottom-0 p-2 right-0 left-0 text-lg truncate font-bold !no-underline" href="PigsOfferDetails?offerId=${o.offerID}">${o.name}</a>
								</div>
								<div class="p-3 flex flex-col gap-3 grow-1">
									<div class="text-slate-600">${o.description}</div>
									<div class="mt-auto flex flex-col gap-2">
										<div class="text-yellow-600 text-md font-semibold text-slate-600">
											<fmt:formatNumber value="${o.totalOfferPrice}" type="number" groupingUsed="true"/> VND / <span>${o.quantity} con</span>
										</div>
										<div>
											<a href="#" class="open-cart-modal inline-flex items-center justify-center !bg-lime-600 hover:!bg-lime-700 text-white py-1 px-2 rounded-sm text-sm gap-2 !no-underline" data-offer-id="${o.offerID}" data-max="${o.quantity}" data-min="${o.minQuantity}">
												<i class="fa fa-shopping-cart"></i>
												<span>Thêm vào giỏ</span>
											</a>
										</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
					<div class="pagination-area mt-4 text-center">
						<nav>
							<ul class="pagination justify-content-center">
								<c:forEach var="i" begin="1" end="${totalPages}">
									<li class="page-item ${i == currentPage ? 'active' : ''}">
										<a class="page-link" href="home?page=${i}">${i}</a>
									</li>
								</c:forEach>
							</ul>
						</nav>
					</div>
				</div>
			</div>
		</div>
        <jsp:include page="component/footer.jsp" />

        <!-- Modal Add to Cart -->
        <div class="modal fade" id="addToCartModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered-custom" role="document">
                <div class="modal-content p-4">
                    <form action="AddToCartController" method="post" id="addToCartForm">
                        <input type="hidden" name="offerId" id="modalOfferId" />

                        <select name="mode" id="modeSelect" class="form-control mb-2">
                            <option value="all" selected>Mua toàn bộ</option>
                            <option value="custom">Chọn số lượng</option>
                        </select>

                        <input type="number" name="quantity" id="modalQuantity" class="form-control" style="display: none;" />

                        <button type="submit" class="btn btn-success w-100 mt-3">Thêm vào giỏ</button>
                    </form>
                </div>
            </div>
        </div>
        <script>
            $(document).ready(function () {
                let max = 0;
                let min = 0;

                $('.open-cart-modal').click(function (e) {
                    e.preventDefault();

                    const offerId = $(this).data('offer-id');
                    max = parseInt($(this).data('max'));
                    min = parseInt($(this).data('min'));

                    $('#modalOfferId').val(offerId);
                    $('#modeSelect').val('all');
                    $('#modalQuantity')
                            .attr('min', min)
                            .attr('max', max)
                            .val(max)
                            .prop('readonly', true)
                            .show();

                    $('#addToCartModal').modal('show');
                });

                $('#modeSelect').on('change', function () {
                    const mode = $(this).val();
                    if (mode === 'custom') {
                        $('#modalQuantity')
                                .val(min)
                                .prop('readonly', false)
                                .attr('min', min)
                                .attr('max', max)
                                .show();
                    } else {
                        $('#modalQuantity')
                                .val(max)
                                .prop('readonly', true)
                                .show();
                    }
                });
            });
        </script>

        <c:if test="${not empty param.error and param.error == '403'}">
            alert("Bạn không có quyền thực hiện việc này");
        </c:if>

    </body>
</html>