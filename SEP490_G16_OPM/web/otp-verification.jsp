<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Verify OTP</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f1f1f1;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
            }

            .otp-container {
                background: white;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                width: 360px;
                text-align: center;
            }

            h2 {
                margin-bottom: 20px;
            }

            input[type="text"] {
                width: 90%;
                padding: 10px;
                margin-top: 10px;
            }

            input[type="submit"], button {
                margin-top: 20px;
                padding: 10px 20px;
                background-color: #4CAF50;
                border: none;
                color: white;
                font-weight: bold;
                cursor: pointer;
                border-radius: 5px;
            }

            input[type="submit"]:hover, button:disabled {
                background-color: #3e8e41;
            }

            .msg {
                margin-top: 15px;
                color: red;
            }

            .timer {
                font-size: 14px;
                color: #666;
                margin-top: 10px;
                white-space: nowrap;
            }

            button:disabled {
                background-color: gray;
                cursor: not-allowed;
            }
        </style>
    </head>
    <body>
        <div class="otp-container">
            <h2>Email Verification</h2>

            <form action="verify-otp" method="post">
                <label>Enter OTP sent to your email:</label><br>
                <input type="text" name="otp" required /><br>
                <input type="submit" value="Verify" />
            </form>

            <c:if test="${not empty msg}">
                <div class="msg">${msg}</div>
            </c:if>

            <div class="timer">
                OTP will expire in <span id="countdown">5:00</span>
            </div>

            <form action="auth" method="post" style="margin-top: 15px;">
                <input type="hidden" name="action" value="resend-otp" />
                <button id="resendBtn" type="submit" disabled>Resend OTP (30s)</button>
            </form>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const countdownElement = document.getElementById("countdown");
                const resendBtn = document.getElementById("resendBtn");

                let otpDuration = <c:out value="${otpRemainingSeconds != null ? otpRemainingSeconds : 0}" />;
                let resendCooldown = <c:out value="${resendCooldownLeft != null ? resendCooldownLeft : 0}" />;

                function formatTime(seconds) {
                    const m = Math.floor(seconds / 60);
                    const s = seconds % 60;
                    return m.toString().padStart(2, '0') + ":" + s.toString().padStart(2, '0');
                }

                function updateOtpCountdown() {
                    if (otpDuration > 0) {
                        countdownElement.textContent = formatTime(otpDuration);
                        otpDuration--;
                    } else {
                        countdownElement.textContent = "Expired";
                        clearInterval(otpTimer);
                    }
                }

                function updateResendButton() {
                    if (resendCooldown > 0) {
                        resendBtn.disabled = true;
                        resendBtn.textContent = "Resend OTP (" + resendCooldown-- + "s)";
                    } else {
                        resendBtn.disabled = false;
                        resendBtn.textContent = "Resend OTP";
                        clearInterval(resendTimer);
                    }
                }

                // 🟢 Cập nhật hiển thị và khóa verify nếu OTP đã hết hạn
                if (otpDuration <= 0) {
                    countdownElement.textContent = "Expired";
                    document.querySelector('input[type="submit"]').disabled = true;
                }

                updateOtpCountdown();
                const otpTimer = setInterval(updateOtpCountdown, 1000);

                updateResendButton();
                const resendTimer = setInterval(updateResendButton, 1000);
            });
        </script>

    </body>
</html>
