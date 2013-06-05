package summative2013.lifeform;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class Summative extends JPanel {

    public Summative() {
        Lifeform.summative = this;
        HashMap locToLife = new HashMap();
        HashMap locToTerrain = new HashMap();
    }
    
    public enum Terrain {

        LAND, SEA

        );

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