package jqsx;

import javax.swing.*;
import java.awt.*;

public class ColoredText extends JLabel {

    public ColoredText(String text) {
        super(text);
    }

    @Override
    public void paint(Graphics g) {
//        super.paint(g);

        setBackground(Color.BLACK);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth(), getWidth());
        g.setColor(Color.WHITE);
        g.drawString(getText(), 0, getFont().getSize());
    }

    @Override
    protected void paintComponent(Graphics g) {
        paint(g);
    }
}
