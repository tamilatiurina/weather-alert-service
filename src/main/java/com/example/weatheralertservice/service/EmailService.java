package com.example.weatheralertservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendNotification(String to, String city, String condition) {
        System.out.println("Sending email to " + to);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Weather Alert");
        if(condition.equals("temperature")){
            msg.setText("Temperature is below 0°C in " + city);
        }
        if(condition.equals("rain")){
            msg.setText("It's raining in " + city);
        }
        try {
            mailSender.send(msg);
        } catch (Exception e) {
            System.out.println("Error during sending the message to subscriber: "+ e.getMessage());
        }
    }
}
