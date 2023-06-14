package com.isc.hermes.utils;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    private String codigoVerificacion = generarCodigoVerificacion();
    private static final String EMAIL_FROM = "hermes.map.app@gmail.com";
    private static final String EMAIL_PASSWORD = "jRdWQ4KS674XjtYR";
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 587;
    private static SendEmail instance;

    public static SendEmail getInstance() {
        if (instance == null) instance = new SendEmail();
        return instance;
    }

    private String generarCodigoVerificacion() {
        int codigoVerificacion = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(codigoVerificacion);
    }

    public void sendEmailVerifyingCode(String email) throws MessagingException {
        SendEmail.getInstance().sendEmail(
                email, "Verify Email with your secret key",
                ConstantMessages.getHTMLEmailMessage("Verify Secret Key Email", codigoVerificacion)
        );
    }

    private void sendEmail(String userEmail, String messageContent, String title) throws MessagingException {
        // Establecer properties para el servidor de correo
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.HOST", HOST);
        properties.put("mail.smtp.port", PORT);

        // Crear una sesión con el servidor de correo
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });
        session.setDebug(true);
        sendEmailAsMessage(userEmail, messageContent, title, session);
    }

    private void sendEmailAsMessage(String userEmail, String messageContent, String title, Session session) throws MessagingException {

        //Crear un nuevo mensaje
        MimeMessage mensaje = new MimeMessage(session);

        // Establecer las direcciones de remitente y destinatario
        mensaje.setFrom(new InternetAddress(EMAIL_FROM));
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));

        // Establecer el asunto
        mensaje.setSubject(title); //"Verificación de código"

        // Establecer el contenido del mensaje
        mensaje.setText(messageContent, "text/html; charset=utf-8");

        // Enviar el mensaje
        Transport.send(mensaje);

        // Registrar el envío exitoso del correo electrónico
        System.out.println("Correo electrónico de código de verificación enviado correctamente.");
    }
}
