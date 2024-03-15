package jqsx;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class AnimatedText extends JLabel implements Runnable {
    private final Thread thread;
    private long start = System.currentTimeMillis();
    private String text;

    public double revolveSpeed = 5.0;
    public AnimatedText(String text) {
        super(text);
        this.text = text;
        this.thread = new Thread(this);
        this.thread.start();
    }

    private double getTime() {
        return (System.currentTimeMillis() - start) / 1000.0;
    }
    @Override
    public void run() {
        while (getParent() == null)
            sleep(1);
        while (getParent() != null) {
            if (!getParent().isVisible()) continue;
            int x = Math.min((int)Math.floor(getTime() * revolveSpeed) % (text.length() + 4), text.length());
            String t = text.substring(x) + text.substring(0, x);
            if (!getText().equals(t)) {
                setText(t);
            }
        }
        System.out.println("Stopped");
    }

    private void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
