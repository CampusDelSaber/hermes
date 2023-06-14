package com.isc.hermes.utils;

public class ConstantMessages {

    public static String getHTMLEmailMessage(String title, String code) {
        return "<h1 style=\"text-align: center;\"><span style=\"text-decoration: underline;\"><strong><em>" + "HERMES" + "</em></strong></span></h1>" +
                "<p style=\"text-align: center;>"+title+ "</p>" +
                "<h1 style=\"text-align: center;\"><span style=\"text-decoration: underline;\"><img src=\"https://i.ibb.co/H4xfr36/screenshot-from-2023-06-07-19-52-35.png\" alt=\"LogoApp\" width=\"165\" height=\"164\" border=\"0\" /></span></h1>" +
                "<p style=\"text-align: center;\">" + "Your secret key is:" + "</p>" +
                "<p style=\"text-align: center;\"><strong>" + code + "</strong></p>" +
                "<p style=\"text-align: center;\"><span style=\"text-decoration: underline;\">" +"If you did not request this code, please ignore the message."+"</span></p>" +
                "<p style=\"text-align: center;\"><strong>" + "From Hermes "+"</strong></p>";
    }
}
