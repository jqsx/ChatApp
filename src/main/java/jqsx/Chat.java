package jqsx;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Chat {
    Font font = getFont();

    public String name = "USER";

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
                    chat.updateUI();
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
        else
            createNewText(name + " > " + text);
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
            name = args[1];
            createNewText("SYS > set name to " + name);
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
        else if (args[0].equalsIgnoreCase("hello")) {
            createErrorText("Searching for other users on the local network.");
        }
        else
            printERRCMD();
    }

    private void printHelp() {
        createNewText("===========");
        createNewText("!help - this help menu");
        createNewText("!credits - software credits");
        createNewText("!setname <name> - set your name");
        createNewText("!hello - search for friends on local network");
        createNewText("===========");
    }

    private void printERRCMD() {
        createErrorText("SYS > ERR Unknown CMD: !help for help");
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

        createNewText("WELCOME TO SHIT CHAT");

        main.setBackground(Color.BLACK);

        main.setBorder(null);
        chat.setBorder(null);
        chat.setBackground(Color.BLACK);

        return main;
    }

    public void createNewText(String text) {

        JLabel text1 = new JLabel("> " + text);
        text1.setFont(font);
        int shade = (int)Math.floor(150 + 100 * Math.random());
        text1.setForeground(new Color(shade, shade, shade));
        text1.setBorder(null);

        content.add(text1);
    }

    public void createErrorText(String text) {
        JLabel text1 = new JLabel(text);
        text1.setFont(font);
        int shade = (int)Math.floor(150 + 100 * Math.random());
        text1.setForeground(new Color(shade, 0, 0));
        text1.setBorder(null);

        content.add(text1);
    }

    public void removeText() {
        if (content.getComponents().length > 0)
            content.remove(0);
    }
}
