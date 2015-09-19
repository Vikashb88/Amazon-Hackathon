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
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    private NotificationManager notificationManager;

    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageConsumer consumer = null;

    public Receiver() {
     
    }

    public void receiveMessage() {
        try {
            factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = factory.createConnection();
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("notificationQueue");
            consumer = session.createConsumer(destination);
            Message message = consumer.receive();

            if (message instanceof TextMessage) {
                TextMessage text = (TextMessage) message;

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
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
