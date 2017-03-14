package org.wtiger.inno.activemq.mqs;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author Khayrutdinov Marat
 *         Email: mail@wtiger.org
 *         Created on 14.03.2017.
 */
public class Consumer implements Runnable {
    @Override
    public void run() {
        ActiveMQConnectionFactory factory =
                new ActiveMQConnectionFactory("vm://localhost");
        try {
            Connection myConnection = factory.createConnection();
            myConnection.start();
            Session session = myConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("Dest");
            MessageConsumer messageConsumer = session.createConsumer(destination);
            String textMessage = "";
            while (!textMessage.equals("stop")) {
                Message message = messageConsumer.receive(10000);
                if (message != null) {
                    textMessage = "Consumer got message: "+((TextMessage) message).getText();
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
