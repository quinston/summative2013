package summative2013;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import summative2013.lifeform.Lifeform;
import summative2013.phenomena.Weather;

public class Summative extends JPanel {

    private static GraphicsEnvironment ge;
    private static GraphicsDevice gd;
    private HashMap<Point, Lifeform> locToLife;
    private HashMap<Point,TERRAIN> locToTerrain;
    private Object lock = new Object();
    private ArrayList<Weather> activeWeather;

    /**
     * Default constr
     */
    public Summative() {
        Lifeform.summative = this;
        locToLife = new HashMap<Point, Lifeform>();
        locToTerrain = new HashMap<Point,TERRAIN>();
        setSize(gd.getFullScreenWindow().getSize());
    }

    //Terrain making
    public enum TERRAIN {

        LAND, SEA
    };

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
        Summative s = new Summative();
        frame.add(s);

        s.locToTerrain.put(new Point(0, 0),TERRAIN.LAND);
        s.doMapGen(new Point(0, 0), 10);

        s.repaint();
        frame.setVisible(true);
    }

    //Part of the map generator
    public void doMapGen(Point p, int i) {
        if (i >= 0) {
            for (int x = p.x - 1; x <= p.x + 1; x++) {
                for (int y = p.y - 1; y <= p.y + 1; y++) {
                    Point genPoint = new Point(x, y);
                    if ((x != p.x || y != p.y) && !locToTerrain.containsKey(new Point(x, y))) {
                        mapGen(genPoint);
                    }
                    doMapGen(genPoint, i - 1);
                }
            }
        }
    }

    //Map generator
    public void mapGen(Point point) {
        int lands = 0, seas = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                synchronized (lock) {
                    if (locToTerrain.get(new Point(point.x + x, point.y + y)) ==TERRAIN.LAND) {
                        lands++;
                    } else if (locToTerrain.get(new Point(point.x + x, point.y + y)) ==TERRAIN.SEA) {
                        seas++;
                    } else {
                    }
                }
            }
        }
        //Only land, have a small chance to generate a sea block
        if (seas == 0) {
            if (Math.random() < .95) {
                locToTerrain.put(point,TERRAIN.LAND);
            } else {
                locToTerrain.put(point,TERRAIN.SEA);
            }
            //Mostly land, but some sea, generate more sea
        } else if (lands > seas && seas > 0) {
            if (Math.random() < .95) {
                locToTerrain.put(point,TERRAIN.SEA);
            } else {
                locToTerrain.put(point,TERRAIN.LAND);
            }
            //Mostly sea, but some land, generate more land
        } else if (seas > lands && lands > 0) {
            if (Math.random() < 0.95) {
                locToTerrain.put(point,TERRAIN.LAND);
            } else {
                locToTerrain.put(point,TERRAIN.SEA);
            }
        } else {
            if (Math.random() < 0.5) {
                locToTerrain.put(point,TERRAIN.LAND);
            } else {
                locToTerrain.put(point,TERRAIN.SEA);
            }
        }
    }

    //moves the whole system ahead
    public void advance() {
        Iterator it = locToLife.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry<Point, Lifeform> pairs = (Map.Entry) it.next();

            Weather.WEATHER current = Weather.WEATHER.CLOUD;

            for (Weather w : activeWeather) {
                if (w.getArea().contains((pairs.getKey()))) {
                    if (current != Weather.WEATHER.RAIN) {
                        if (w.getType() == Weather.WEATHER.RAIN) {
                            current = Weather.WEATHER.RAIN;
                        } else if (w.getType() == Weather.WEATHER.SUN) {
                            current = Weather.WEATHER.SUN;
                        } else {
                            current = Weather.WEATHER.CLOUD;
                        }
                    }
                }
            }
            pairs.getValue().act(current);
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTerrain(g);
    }

    //draws terrain
    public void drawTerrain(Graphics g) {
        synchronized (lock) {
            for (Entry<Point,TERRAIN> e : locToTerrain.entrySet()) {
                Point p = e.getKey();
                if (e.getValue() ==TERRAIN.LAND) {
                    g.setColor(Color.MAGENTA);
                } else if (e.getValue() ==TERRAIN.SEA) {
                    g.setColor(Color.BLUE);
                }
                g.fillRect(p.x * 10, p.y * 10, 10, 10);
            }
        }
    }

    //The lifeforms call this to kill themselves
    public void assistedSuicide(Point location) {
        locToLife.remove(location);
    }

    //Sends over the object in the hashmap
    public Lifeform lifeGet(Point location) {
        return locToLife.get(location);
    }

    //Sends over the object in the hashmap
    public TERRAIN terrainGet(Point location) {
        return locToTerrain.get(location);
    }
}