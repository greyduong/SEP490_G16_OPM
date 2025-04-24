<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Wallet Topup | Online Pig Market</title>
        <jsp:include page="component/library.jsp" />
    </head>
    <body>
        <jsp:include page="component/header.jsp" />
        <main class="container mb-5">
            <div class="w-25 mx-auto">
                <h4 class="font-weight-bold">Wallet Topup</h4>
                <p class="text-danger">${error}</p>
                <form action="" method="POST">
                    <div class="mb-3">
                        <label for="amount">Amount</label>
                        <input class="form-control" type="number" name="amount" id="amount" />
                    </div>
                    <button class="btn btn-primary">Confirm</button>
                </form>
            </div>
        </main>
        <jsp:include page="component/footer.jsp" />     
    </body>
</html>
