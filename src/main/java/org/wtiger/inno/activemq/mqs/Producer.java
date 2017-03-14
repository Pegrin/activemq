package org.wtiger.inno.activemq.mqs;
import org.apache.activemq.ActiveMQConnectionFactory;

        import javax.jms.*;

/**
 * @author Khayrutdinov Marat
 *         Email: mail@wtiger.org
 *         Created on 14.03.2017.
 */
public class Producer implements Runnable {
    private String message;
    public Producer(String msg) {
        message = msg;
    }

    @Override
    public void run() {
        ActiveMQConnectionFactory factory =
                new ActiveMQConnectionFactory("vm://localhost");
        try {
            Connection myConnection = factory.createConnection();
            myConnection.start();
            Session session = myConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("Dest");
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);
            session.close();
            myConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}