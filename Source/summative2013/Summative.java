package summative2013;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import summative2013.lifeform.Lifeform;

public class Summative extends JPanel {

    private HashMap locToLife;
    private HashMap locToTerrain;

    /**
     * Default constr
     */
    public Summative() {
        Lifeform.summative = this;
        locToLife = new HashMap();
        locToTerrain = new HashMap();
    }

    //Terrain making
    public enum Terrain {

        LAND, SEA
    };

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

    //Map generator
    public void mapGen(Point point) {
        int lands = 0, seas = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (locToTerrain.get(new Point(point.x + x, point.y + y)) == Terrain.LAND) {
                    lands = lands + 1;
                } else if (locToTerrain.get(new Point(point.x + x, point.y + y)) == Terrain.SEA) {
                    seas = seas + 1;
                } else {
                }
            }
        }
        //Only land, have a small chance to generate a sea block
        if (seas == 0) {
            if (Math.random() < .95) {
                locToTerrain.put(point, Terrain.LAND);
            } else {
                locToTerrain.put(point, Terrain.SEA);
            }
            //Mostly land, but some sea, generate more sea
        } else if (lands > seas && seas > 0) {
            if (Math.random() < .95) {
                locToTerrain.put(point, Terrain.SEA);
            } else {
                locToTerrain.put(point, Terrain.LAND);
            }
            //Mostly sea, but some land, generate more land
        } else if (seas > lands && lands > 0) {
            if (Math.random() < 0.95) {
                locToTerrain.put(point, Terrain.LAND);
            } else {
                locToTerrain.put(point, Terrain.SEA);
            }
        } else {
            if (Math.random() < 0.5) {
                locToTerrain.put(point, Terrain.LAND);
            } else {
                locToTerrain.put(point, Terrain.SEA);
            }
        }
    }
}