package jqsx;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");

        JFrame frame = new JFrame("SHITCHAT");

        Chat chat = new Chat();

        frame.getContentPane().add(chat.getComponent());

        frame.setBackground(Color.black);

        frame.setSize(800, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}