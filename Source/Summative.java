
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Summative extends JPanel {

    public Summative() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
        frame.add(new Summative());

        
                frame.setVisible(true);

    }
}