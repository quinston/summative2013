package summative2013;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;
import summative2013.lifeform.Lifeform;

public class Summative extends JPanel {

    private static GraphicsEnvironment ge;
    private static GraphicsDevice gd;
    private HashMap<Point,Lifeform> locToLife;
    private HashMap<Point,Terrain> locToTerrain;
    /**
     * Default constr
     */
    public Summative() {
        Lifeform.summative = this;
        locToLife = new HashMap<Point,Lifeform>();
        locToTerrain = new HashMap<Point,Terrain>();
        setSize(gd.getFullScreenWindow().getSize());
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

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
        Summative s = new Summative();
        frame.add(s);
        
        s.locToTerrain.put(new Point(0,0),Terrain.LAND);
        s.doMapGen(new Point(0,0),10);
        
        s.repaint();
        frame.setVisible(true);
    }
    public synchronized void doMapGen(Point p,int i){
        if(i>=0){
            for(int x = p.x-1;x<=p.x+1;x++){
                for(int y = p.y-1;y<=p.y+1;y++){
                    Point genPoint = new Point(x,y);
                    if((x!=p.x||y!=p.y)&&!locToTerrain.containsKey(new Point(x,y)))
                        mapGen(genPoint);
                    doMapGen(genPoint,i-1);
                }
            }
        }
    }
    //Map generator
    public synchronized void mapGen(Point point) {
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
        @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawTerrain(g);
    }
    public synchronized void drawTerrain(Graphics g){
        for(Entry<Point, Terrain> e:locToTerrain.entrySet()){
            Point p = e.getKey();
            if(e.getValue() ==Terrain.LAND)
                g.setColor(Color.MAGENTA);
            else if(e.getValue() == Terrain.SEA)
                g.setColor(Color.BLUE);
            g.fillRect(p.x*10,p.y*10,10,10);
        }
    }
}