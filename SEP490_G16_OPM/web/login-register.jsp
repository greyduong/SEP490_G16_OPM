<%-- 
    Document   : login
    Created on : Mar 28, 2025, 7:35:17 PM
    Author     : tuan
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!-- Website - www.codingnepalweb.com -->
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="description" content=" Today in this blog you will learn how to create a responsive Login & Registration Form in HTML CSS & JavaScript. The blog will cover everything from the basics of creating a Login & Registration in HTML, to styling it with CSS and adding with JavaScript." />
        <meta
            name="keywords"
            content=" 
            Animated Login & Registration Form,Form Design,HTML and CSS,HTML CSS JavaScript,login & registration form,login & signup form,Login Form Design,registration form,Signup Form,HTML,CSS,JavaScript,
            "
            />

        <meta name="viewport" content="width=device-width, initial-scale=1.0" />

        <title>Online Pig Market</title>
        <link rel="stylesheet" href="css/loginStyle.css" />
        <script src="../custom-scripts.js" defer></script>
    </head>
    <body>
        <section class="wrapper">
            <!-- SIGNUP FORM -->
            <div class="form signup">
                <header>Signup</header>
                <form action="auth" method="post" class="signup-form">
                    <input type="hidden" name="action" value="signup" />
                    <input type="text" name="fullname" placeholder="Full name" value="${fullname != null ? fullname : ''}" required />
                    <input type="text" name="username" placeholder="Username" value="${username != null ? username : ''}" required />
                    <input type="email" name="email" placeholder="Email" value="${email}" required />
                    <select name="role">
                        <option value="Choose Role" selected>Choose Role</option>
                        <option value="Seller" ${role == 'Seller' ? 'selected' : ''}>Seller</option>
                        <option value="Dealer" ${role == 'Dealer' ? 'selected' : ''}>Dealer</option>
                    </select>
                    <input type="password" name="password" placeholder="Password" required />
                    <input type="password" name="cfpassword" placeholder="Confirm Password" required />

                    <!-- ✅ Checkbox điều khoản -->
                    <div class="checkbox">
                        <input type="checkbox" id="signupCheck" name="terms" value="accepted"
                               <c:if test="${param.terms == 'accepted'}">checked</c:if> />
                               <label for="signupCheck">
                                   I accept all <a href="terms.jsp" target="_blank">terms & conditions</a>
                               </label>
                        </div>

                        <!-- Hiển thị lỗi -->
                    <c:if test="${not empty msg}">
                        <div style="color: white; font-weight: bold; margin-top: 10px;">${msg}</div>
                    </c:if>

                    <!-- Hiển thị thành công -->
                    <c:if test="${not empty successMsg}">
                        <div style="color: white; font-weight: bold; margin-top: 10px;">${successMsg}</div>
                    </c:if>

                    <input type="submit" value="Signup" />
                </form>
            </div>


            <!-- LOGIN FORM -->
            <div class="form login">
                <header>Login</header>
                <form action="auth" method="post" class="signin-form">
                    <input type="hidden" name="action" value="login" />
                    <input type="text" name="username" placeholder="Username" value="${username != null ? username : ''}" required />
                    <input type="password" name="password" placeholder="Password" required />
                    <a href="#">Forgot password?</a>
                    <input type="submit" value="Login" />
                    <c:if test="${not empty msg}">
                        <div style="color: red; font-weight: bold; margin-bottom: 10px;">${msg}</div>
                    </c:if>

                </form>
            </div>

            <script>
                const wrapper = document.querySelector(".wrapper"),
                        signupHeader = document.querySelector(".signup header"),
                        loginHeader = document.querySelector(".login header");

                loginHeader.addEventListener("click", () => {
                    wrapper.classList.add("active");
                });
                signupHeader.addEventListener("click", () => {
                    wrapper.classList.remove("active");
                });
            </script>
        </section>
        <script>
            // Check for access denied via query string
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.get("error") === "access-denied") {
                alert("You do not have permission to access that page.");
                // Remove the query param so it doesn't alert again on refresh
                window.history.replaceState({}, document.title, window.location.pathname);
            }
        </script>
    </body>
</html>

