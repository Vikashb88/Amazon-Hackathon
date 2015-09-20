/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amazon.notification.utils;

/**
 *
 * @author Vbalu
 */
import com.amazon.notification.service.NotificationManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver implements MessageListener{

    @Autowired
    private NotificationManager notificationManager;

    
    
    @Override
    public void onMessage(Message msg) {
        if (msg instanceof TextMessage) {
            try {
                TextMessage text = (TextMessage) msg;

                String[] toArr = {text.getText().split(",")[0]};
                String[] bccArr = {};
                
                System.out.println("********************************");
                System.out.println("********************************");
                System.out.println(notificationManager);
                System.out.println("********************************");
                System.out.println("********************************");

                notificationManager.sendEmail("amazehackathon2015@gmail.com", toArr,
                        bccArr, text.getText().split(",")[1], "hi");

                System.out.println("Message is : " + text.getText());
            } catch (JMSException ex) {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
           }

}
