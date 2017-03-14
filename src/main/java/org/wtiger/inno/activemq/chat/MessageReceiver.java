package org.wtiger.inno.activemq.chat;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author Khayrutdinov Marat
 *         Email: mail@wtiger.org
 *         Created on 14.03.2017.
 */
public class MessageReceiver implements Runnable {
    private static boolean yankeeGoHome = false;
    private final String DESTINATION;
    private final String URL;

    public MessageReceiver(String destination, String url) {
        DESTINATION = destination;
        URL = url;
    }

    public static void stop(){
        yankeeGoHome = true;
    }

    public static boolean haveToStop(){
        return yankeeGoHome;
    }

    @Override
    public void run() {
        ActiveMQConnectionFactory factory =
                new ActiveMQConnectionFactory(URL);
        try {
            Connection myConnection = factory.createConnection();
            myConnection.start();
            Session session = myConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
       //     Topic topic = session.createTopic(DESTINATION);
            Destination destination = session.createQueue(DESTINATION);
            MessageConsumer messageConsumer = session.createConsumer(destination);
            while (!haveToStop()) {
                Message message = messageConsumer.receive(10000);
                if (message != null) {
                    String textMessage = "I got mesage from "+DESTINATION+": "
                            +((TextMessage) message).getText();
                    System.out.println(textMessage);
                }
            }
            session.close();
            myConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
