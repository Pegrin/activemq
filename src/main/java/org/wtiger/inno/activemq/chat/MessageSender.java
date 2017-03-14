package org.wtiger.inno.activemq.chat;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayDeque;

/**
 * @author Khayrutdinov Marat
 *         Email: mail@wtiger.org
 *         Created on 14.03.2017.
 */
public class MessageSender implements Runnable {
    private static boolean yankeeGoHome = false;
    private final String DESTINATION;
    private final String URL;
    private static ArrayDeque<Message> messages = new ArrayDeque<>();

    public MessageSender(String destination, String url) {
        this.DESTINATION = destination;
        URL = url;
    }


    public static void stop(){
        yankeeGoHome = true;
    }

    public static boolean haveToStop(){
        return yankeeGoHome;
    }

    private static synchronized void addMessage(Message message){
        messages.push(message);
    }

    public void addMessage(String textMessage){
        Message message = new Message(DESTINATION, textMessage);
        addMessage(message);
    }

    public static synchronized Message getMessage(){
        Message message = messages.poll();
        return message;
    }

    @Override
    public void run() {
        ActiveMQConnectionFactory factory =
                new ActiveMQConnectionFactory(this.URL); //"vm://localhost"
        try {
            Connection myConnection = factory.createConnection();
            myConnection.start();
            Session session = myConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(this.DESTINATION);
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            Message message = null;
            while (!haveToStop()) {
                message = getMessage();
                if (message == null) try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } else {
                    TextMessage textMessage = session.createTextMessage(message.getMessage());
                    producer.send(textMessage);
                }
            }
            session.close();
            myConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    static class Message{
        private String destination;
        private String message;

        public String getDestination() {
            return destination;
        }

        public String getMessage() {
            return message;
        }

        public Message(String destination, String message) {
            this.destination = destination;
            this.message = message;
        }
    }
}
