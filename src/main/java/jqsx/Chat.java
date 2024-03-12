package jqsx;

import jqsx.MNET.MulticastPublisher;
import jqsx.MNET.MulticastReceiver;
import jqsx.Net.NetworkClient;
import jqsx.Net.NetworkConnectionToClient;
import jqsx.Net.NetworkServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Arrays;
import java.util.Random;

public class Chat {
    Font font = getFont();

    public String name = "USER";

    public final MessageRoute messageRoute = new MessageRoute(this);

    private final MulticastReceiver receiver = new MulticastReceiver();

    private static Chat instance;

    private Font getFont() {
        try {
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
            assert stream != null;
            return Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(15f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    private JTextField textField1() {
        JTextField field = new JTextField();

        field.setForeground(Color.WHITE);
        field.setBackground(new Color(10, 10, 10));

        field.setFont(font);
        field.setBorder(null);

        DefaultCaret c = new DefaultCaret();
        c.width = 15;

        field.setCaret(c);

        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10 && !field.getText().replaceAll(" ", "").isEmpty()) {
                    handleChat(field.getText());
                    field.setText("");
                    chat.getVerticalScrollBar().setValue(999999);
                    chat.getVerticalScrollBar().setValue(999999);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        return field;
    }

    private JPanel content = new JPanel();

    private JScrollPane chat = Chat();

    private void handleChat(String text) {
        if (text.startsWith("!") && text.length() > 1)
            handleCommand(text.substring(1).split(" "));
        else {
            String msg = name + " > " + text;
            createNewText(msg);
            messageRoute.sendMessage(msg);
        }
    }

    private void handleCommand(String[] args) {
        if (args[0].equalsIgnoreCase("help"))
            printHelp();
        else if (args[0].equalsIgnoreCase("credits")) {
            createNewText("### ### ### ### ###");
            createNewText("Created by JQSx");
            createNewText("Some random shit i cooked up cos i was bored");
            createNewText("### ### ### ### ###");
        }
        else if (args[0].equalsIgnoreCase("setname") && args.length >= 2) {
            name = args[1].substring(0, Math.min(args[1].length(), 16));
            createInfoText("(SYS) set name to " + name);
        }
        else if (args[0].equalsIgnoreCase("setsize") && args.length >= 2) {
            try {
                int parse = Math.min(Math.max(Integer.parseInt(args[1]), 1), 120);

                font = font.deriveFont((float)parse);

                Component[] components = content.getComponents();
                for (int i = 0; i < components.length; i++) {
                    components[i].setFont(font);
                }

            } catch (NumberFormatException e) {
                createErrorText("Thats not an integer...");
            }
        }
        else if (args[0].equalsIgnoreCase("clear")) {
            content.removeAll();
            createInfoText("Chat conent cleared");
        }
        else if (args[0].equalsIgnoreCase("users")) {
            createNewText("# Connected users #");
            for (NetworkConnectionToClient conn : NetworkServer.clients) {
                createNewText(" - " + conn.socket.getInetAddress().getHostName() + " - " + conn.socket.getInetAddress().getHostAddress());
            }
            for (NetworkClient conn : NetworkClient.clients) {
                createNewText(" - " + conn.socket.getInetAddress().getHostAddress());
            }
        }
        else if (args[0].equalsIgnoreCase("server")) {
            createInfoText("HOST: " + NetworkServer.host.getHostAddress());
            createInfoText("PORT: " + NetworkServer.PORT);

        }
        else
            printERRCMD();
    }

    private void printHelp() {
        createInfoText("===========");
        createInfoText("!help - this help menu");
        createInfoText("!credits - software credits");
        createInfoText("!setname <name> - set your name");
        createInfoText("!setsize <size:int> - set text size");
        createInfoText("!clear - clears the chat box / removes all messages locally");
        createInfoText("===========");
    }

    private void printERRCMD() {
        createErrorText("(SYS) ERR Unknown CMD: !help for help");
    }

    private JScrollPane Chat() {
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.setBackground(Color.BLACK);
        content.setBorder(null);
        return new JScrollPane(content);
    }
    private JPanel Container;

    public JPanel getComponent() {
        JPanel main = new JPanel();

        main.setLayout(new BorderLayout());

        chat.getVerticalScrollBar().setUI(new CustomScrollBar());

        chat.getVerticalScrollBar().setBackground(Color.BLACK);

        main.add(chat, BorderLayout.CENTER);

        main.add(textField1(), BorderLayout.SOUTH);

        {
            JLabel text1 = new AnimatedText(" --- WELCOME TO SHIT CHAT --- ");
            text1.setFont(font);
            int shade = (int)Math.floor(150 + 100 * Math.random());
            text1.setForeground(new Color(0, shade, 0));
            text1.setBorder(null);

            content.add(text1);
            chat.updateUI();
        }

        createNewText("");
        createNewText("# # ###");
        createNewText("# # #");
        createNewText("##  ###");
        createNewText("# #   #");
        createNewText("# # ###");
        createNewText("");

        {
            AnimatedText t = new AnimatedText("# = # Created by JQSx # = #");

            t.setFont(font);
            int shade = (int)Math.floor(150 + 100 * Math.random());
            t.setForeground(new Color(shade, shade, shade));
            t.setBorder(null);

            content.add(t);
        }

        main.setBackground(Color.BLACK);

        main.setBorder(null);
        chat.setBorder(null);
        chat.setBackground(Color.BLACK);

        net();

        return main;
    }

    private void net() {
        try {
            NetworkServer.host = getLocalAddress();
            NetworkServer.PORT = new Random().nextInt(6000, 15000);

            System.out.println(NetworkServer.host.getHostAddress());
            System.out.println(NetworkServer.PORT);

            NetworkServer.StartServer();

            receiver.start();

            new MulticastPublisher().multicast( NetworkServer.PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InetAddress getLocalAddress() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            return socket.getLocalAddress();
        } catch (IOException e) {
            return null;
        }
    }

    public void createNewText(String text) {

        JLabel text1 = new JLabel("> " + text);
        text1.setFont(font);
        int shade = (int)Math.floor(150 + 100 * Math.random());
        text1.setForeground(new Color(shade, shade, shade));
        text1.setBorder(null);

        content.add(text1);
        chat.updateUI();
    }

    public void createInfoText(String text) {
        JLabel text1 = new JLabel(text);
        text1.setFont(font);
        int shade = (int)Math.floor(150 + 100 * Math.random());
        text1.setForeground(new Color(0, shade, 0));
        text1.setBorder(null);

        content.add(text1);
        chat.updateUI();
    }

    public void createErrorText(String text) {
        JLabel text1 = new JLabel(text);
        text1.setFont(font);
        int shade = (int)Math.floor(150 + 100 * Math.random());
        text1.setForeground(new Color(shade, 0, 0));
        text1.setBorder(null);

        content.add(text1);
        chat.updateUI();
    }

    public void removeText() {
        if (content.getComponents().length > 0)
            content.remove(0);
    }

    public Chat() {
        instance = this;
    }

    public static void logErr(Exception e) {
        instance.createErrorText(e.getLocalizedMessage());
        throw new RuntimeException(e);
    }

    public static void logInfo(String text) {
        instance.createInfoText("(SYS) " + text);
    }
}
