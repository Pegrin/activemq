package org.wtiger.inno.activemq;

import org.wtiger.inno.activemq.chat.MessageReceiver;
import org.wtiger.inno.activemq.chat.MessageSender;
import org.wtiger.inno.activemq.mqs.Consumer;
import org.wtiger.inno.activemq.mqs.Producer;

import java.util.Scanner;

/**
 * @author Khayrutdinov Marat
 *         Email: mail@wtiger.org
 *         Created on 14.03.2017.
 */
public class Main {
    private static final String DESTINATION = "Me";
    private static final String URL = "tcp://localhost:61616";
    public static void main(String[] args) {
        MessageReceiver messageReceiver = new MessageReceiver(DESTINATION, URL);
        MessageSender messageSender = new MessageSender(DESTINATION, URL);
        Thread mr = new Thread(messageReceiver);
        mr.start();
        Thread ms = new Thread(messageSender);
        ms.start();
        Scanner in = new Scanner(System.in);
        String msg = "";
        while (!msg.equals("stop")){
            msg = in.nextLine();
            System.out.println("I'm sending message: "+msg);
            messageSender.addMessage(msg);
        }
        MessageSender.stop();
        MessageReceiver.stop();
        try {
            ms.join();
            mr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
