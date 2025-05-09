/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 *
 * @author duong
 */
public class Email {

    //Email: onlinepigmarket@gmail.com
    //Password: eEI2oGq4DHmJElalXpZj 
    //AppPassword: dmgfxldscdkeqymz
    public static void sendEmail(String toEmail, String subject, String messageContent) throws MessagingException, UnsupportedEncodingException {
        final String fromEmail = "onlinepigmarket@gmail.com";
        final String appPassword = "dmgfxldscdkeqymz"; // App password 16 ký tự

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
        msg.setContent(messageContent, "text/plain; charset=UTF-8"); // Nội dung hỗ trợ tiếng Việt

        Transport.send(msg);
    }

    public static boolean sendOtpEmail(String toEmail, String otp) {
        String subject = "Mã OTP xác thực của bạn";
        String messageContent = "Xin chào,\n\nMã OTP xác thực của bạn là: " + otp
                + "\n\nOTP có hiệu lực trong 5 phút.\n\nOnline Pig Market.";

        try {
            Email.sendEmail(toEmail, subject, messageContent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
