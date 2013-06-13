package summative2013;

import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import summative2013.lifeform.Lifeform;
import summative2013.phenomena.Weather;

public class Summative extends JPanel implements KeyListener {

    private static GraphicsEnvironment ge;
    private static GraphicsDevice gd;
    private HashMap<Point, Lifeform> locToLife;
    private HashMap<Point, Terrain> locToTerrain;
    private final Object lock = new Object();
    private ArrayList<Weather> activeWeather;
    private static JFrame frame;
    private Rectangle screen;
    private boolean upPressed = false,downPressed = false,rightPressed = false,leftPressed = false;

    /**
     * Default constructor, Generates a Summative object that is the size of the screen, with a map to fill it
     */
    public Summative() {
        Lifeform.summative = this;//sets the panel for all of the lifeforms to be this
        locToLife = new HashMap<Point, Lifeform>();//initializes our point, lifeform hashmap
        locToTerrain = new HashMap<Point, Terrain>();//initializes our point, terrain hashmap
        setSize(gd.getFullScreenWindow().getSize());//fullscreen the panel
        screen = new Rectangle(-1 * getWidth() / 2, -1 * getHeight() / 2, getWidth(), getHeight());//sets up our screen rectangle to define our screen
        for (int i = -1 * getWidth() / 20; i <= getWidth() / 20; i++) {
            for (int j = -1 * getHeight() / 20; j <= getHeight() / 20; j++) {
                mapGen(new Point(i, j));//generate initial point
            }
        }
        setFocusable(true);//allows us to actually use the keylistener
        requestFocusInWindow();
        addKeyListener(this);//makes keys do something
    }
    /**
     * Types of terrain available
     */
    public enum Terrain {

        LAND, SEA
    };
    /**
     * The Driver method to run the program
     * @param args The command line parameters
     */
    public static void main(String[] args) {
        frame = new JFrame();//initializes our frame

        frame.setResizable(false);//can't resize, about to be fullscreen
        frame.setUndecorated(true);//no border
        frame.setLayout(null);//no layout, no components
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//stops the program when we close

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);//makes full screen
        Summative s = new Summative();
        frame.add(s);
        s.repaint();
        s.requestFocusInWindow();//keyListener activated
        frame.setVisible(true);
    }
    /**
     * Generates a map that radiates out from an initial point, not used anymore
     * @param p The point to centre the generation around
     * @param i The number of iterations
     */
    public void doMapGen(Point p, int i) {
        if (i >= 0) {
            for (int x = p.x - 1; x <= p.x + 1; x++) {
                for (int y = p.y - 1; y <= p.y + 1; y++) {
                    Point genPoint = new Point(x, y);
                    if ((x != p.x || y != p.y) && !locToTerrain.containsKey(new Point(x, y))) {//loop through points around
                        mapGen(genPoint);//fit that point
                    }
                    doMapGen(genPoint, i - 1);//radiate out
                }

            }
        }
    }

    /**
     * Generates what type of land should be at the point
     * @param point The point in which we want to generate some terrain
     */
    public void mapGen(Point point) {
        int lands = 0, seas = 0;//count adjacent land, sea
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                synchronized (lock) {
                    if (locToTerrain.get(new Point(point.x + x, point.y + y)) == Terrain.LAND) {
                        lands++;//add a land for each land within the 3x3 surrounding the block
                    } else if (locToTerrain.get(new Point(point.x + x, point.y + y)) == Terrain.SEA) {
                        seas++;//add a sea for each sea within the 3x3 surrounding block
                    }
                }
            }
        }
        //Only land, have a small chance to generate a sea block
        if (seas == 0) {
            if (Math.random() < .95) {
                locToTerrain.put(point, Terrain.LAND);//more likely to have land
            } else {
                locToTerrain.put(point, Terrain.SEA);//less likely to have sea
            }
        } else if (lands > seas && seas > 0) {//if more land, more likely to have land, creates landmass
            if (Math.random() < .8 + .05*lands) {
                locToTerrain.put(point, Terrain.LAND);
            } else {
                locToTerrain.put(point, Terrain.SEA);
            }
        } else if (seas > lands && lands > 0) {//if more see, more likely to have sea, creates lakes, bodies of water
            if (Math.random() < .8 + .05*seas) {
                locToTerrain.put(point, Terrain.SEA);
            } else {
                locToTerrain.put(point, Terrain.LAND);
            }
        } else {//base case, even chance
            if (Math.random() < 0.5) {
                locToTerrain.put(point, Terrain.LAND);
            } else {
                locToTerrain.put(point, Terrain.SEA);
            }
        }
    }
    /**
     * advances the stuff
     */
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
    /**
     * draws the graphics on the screen
     * @param g The graphics object needed
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTerrain(g);//draws the terrain of the map
    }
    /**
     * Draws terrain, Green for lane, Blue for sea
     * @param g The graphics object to draw on
     */
    public void drawTerrain(Graphics g) {
        synchronized (lock) {
            for(int i = screen.x/10;i<(screen.x+screen.width)/10;i++){
                for(int j = screen.y/10;j<(screen.y+screen.height)/10;j++){
                    if(locToTerrain.get(new Point(i,j))==Terrain.LAND)//if land, draw green
                        g.setColor(Color.GREEN);
                    else if (locToTerrain.get(new Point(i,j))==Terrain.SEA)//if sea draw blue
                        g.setColor(Color.BLUE);
                    g.fillRect(i*10 - screen.x,j*10 - screen.y,10,10);//draw the block
                }
            }
        }
    }
    /**
     * Unused method
     * @param e KeyEvent fired as a type event
     */
    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }
    /**
     * Checks which of the arrow keys have been hit and moves the perspective accordingly
     * @param e 
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("keyPressed");
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_RIGHT)
            rightPressed = true;//check off that right has been pressed
        else if (keyCode == KeyEvent.VK_LEFT)
            leftPressed = true;//check off that left has been pressed
        else if (keyCode == KeyEvent.VK_UP)
            upPressed = true;//check off that up has been pressed
        else if (keyCode == KeyEvent.VK_DOWN)
            downPressed = true;//check off that down has been pressed
        if(rightPressed)
            moveRight();
        if(leftPressed)
            moveLeft();
        if(upPressed)
            moveUp();
        if(downPressed)
            moveDown();
        updateMap();//generates map as needed
        repaint();

    }
    /**
     * Stores when the arrow keys have been released
     * @param e The KeyEvent fired
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_RIGHT)//right isn't pressed anymore
            rightPressed = false;
        if(keyCode == KeyEvent.VK_LEFT)//left isn't pressed anymore
            leftPressed = false;
        if(keyCode == KeyEvent.VK_UP)//up isn't pressed anymore
            upPressed = false;
        if(keyCode == KeyEvent.VK_DOWN)//down isn't pressed anymore
            downPressed = false;
    }
    /**
     * Generates more map as it becomes visible to the user
     */
    public void updateMap(){
        for(int i = screen.x/10;i<=(screen.x+screen.width)/10;i++){//runs through x values
            for(int j = screen.y/10;j<=(screen.y+screen.height)/10;j++){//runs through y values
                if(!locToTerrain.containsKey(new Point(i,j)))//do we have a point there?
                    mapGen(new Point(i,j));//create point in that map
            }
        }
    }
    /**
     * Moves the perspective towards the right
     */
    public void moveRight() {
        screen.translate(10, 0);//moves the perspective
    }
    /**
     * Moves the perspective to the left
     */
    public void moveLeft() {
        screen.translate(-10, 0);//moves "camera"
    }
    /**
     * Moves the perspective upwards
     */
    public void moveUp() {
        screen.translate(0, -10);//moves viewpoint
    }
    /**
     * Moves the perspective downward
     */
    public void moveDown() {
        screen.translate(0, 10);//moves centre of field of view
    }
}