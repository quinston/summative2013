package summative2013;

import java.awt.BasicStroke;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import summative2013.lifeform.Bear;
import summative2013.lifeform.Bunny;
import summative2013.lifeform.Cattle;
import summative2013.lifeform.Grass;
import summative2013.lifeform.Lifeform;
import summative2013.lifeform.Tree;
import summative2013.lifeform.Bat;
import summative2013.phenomena.Weather;

public class Summative extends JPanel implements KeyListener, MouseMotionListener, MouseListener, ActionListener {

    private static GraphicsEnvironment ge;
    private static GraphicsDevice gd;
    private JPanel buttonPanel;
    private JButton addBear, addBunny, addCattle, addGrass, addTree, addBat;
    private HashMap<Point, Lifeform> locToLife;
    private HashMap<Point, Grass> locToGrass;
    private HashMap<Point, TERRAIN> locToTerrain;
    private HashMap<String, Image> sprites;
    private final Object lock = new Object();
    private ArrayList<Weather> activeWeather;
    private ArrayList<String> events;
    private static JFrame frame;
    private static Summative s;
    /**
     * rectangles to hold the entire screen, where to click to open the log and
     * where the HUD is
     */
    private Rectangle screen, logButton, hud;
    /**
     * stores which keys have been pressed
     */
    private boolean upPressed = false, downPressed = false, rightPressed = false, leftPressed = false;
    /**
     * stores if we are showing the log or not
     */
    private boolean logOpen = false;
    /**
     * stores which lifeform we are creating
     */
    private boolean makeBear = false, makeBunny = false, makeCattle = false, makeGrass = false, makeTree = false, makeBat = false;
    private final int gridSize = 10;
    private final int FPS = 60;
    /**
     * stores what lifeform the mouse is over
     */
    private String mouseOnLife = "";
    private Point mouse = new Point();
    /**
     * Point that is selected. Display info on it.
     */
    private Point selectedPoint;
    /**
     * counts of each of the lifeforms
     */
    private int batCount = 0, bearCount = 0, bunnyCount = 0, cattleCount = 0, grassCount = 0, treeCount = 0;
    private int numHours = 0;
    /**
     * Wind speed, affects pushWeather
     */
    private Point2D.Double hourlyWind = new Point2D.Double(-0.03, 0.03);
    private long refFrame = System.currentTimeMillis();

    /**
     * Default constructor, Generates a Summative object that is the size of the
     * screen, with a map to fill it
     */
    public Summative() {
        Lifeform.summative = this;//sets the panel for all of the lifeforms to be this
        locToLife = new HashMap<Point, Lifeform>();//initializes our point, lifeform hashmap
        locToGrass = new HashMap<Point, Grass>();
        events = new ArrayList<String>();
        addBear(0, 0);
        addBunny(0, 10);
        addCattle(0, 20);
        addGrass(60, 20);
        addGrass(70, 20);
        addGrass(80, 20);
        addTree(-30, -30);
        addBat(10, 10);
        locToTerrain = new HashMap<Point, TERRAIN>();//initializes our point, terrain hashmap
        activeWeather = new ArrayList<Weather>();
        locToGrass = new HashMap<Point, Grass>();
        setSize(frame.getSize());//fullscreen the panel
        screen = new Rectangle(-1 * getWidth() / (2 * gridSize) - 1, -1 * getHeight() / (2 * gridSize) - 1,
                getWidth() / gridSize + 2, getHeight() / gridSize + 2);//sets up our screen rectangle to define our screen
        for (int i = screen.x; i <= screen.x + screen.width; i++) {
            for (int j = screen.y; j <= screen.y + screen.height; j++) {
                mapGen(new Point(i, j));//generate initial point
            }
        }
        setFocusable(true);//allows us to actually use the keylistener
        addKeyListener(this);//makes keys do something
        addMouseListener(this);
        addMouseMotionListener(this);

        hud = new Rectangle(getWidth() - 600, getHeight() - 220, 620, 220);
        logButton = new Rectangle(hud.x + 400, hud.y + 160, 200, 40);

        Area a = (new Area(new Ellipse2D.Double(0, 0, 30, 30)));
        a.add(new Area(new Ellipse2D.Double(5, 20, 40, 60)));
        activeWeather.add(new summative2013.phenomena.AirLock(a));

        a = new Area(new Rectangle(-30, -60, 20, 69));
        a.add(new Area(new Ellipse2D.Double(-90, -90, 70, 70)));
        activeWeather.add(new summative2013.phenomena.Drizzle(a));


        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 6));

        addBear = new JButton("Add a bear");
        addBear.addActionListener(this);
        buttonPanel.add(addBear);

        addBunny = new JButton("Add a bunny");
        addBunny.addActionListener(this);
        buttonPanel.add(addBunny);

        addCattle = new JButton("Add a cow");
        addCattle.addActionListener(this);
        buttonPanel.add(addCattle);

        addBat = new JButton("Add a bat");
        addBat.addActionListener(this);
        buttonPanel.add(addBat);

        addTree = new JButton("Add a tree");
        addTree.addActionListener(this);
        buttonPanel.add(addTree);

        addGrass = new JButton("Add some grass");
        addGrass.addActionListener(this);
        buttonPanel.add(addGrass);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);
        try {
            loadSprites();
        } catch (IOException e) {
            System.out.println("Failed to load images.");
            System.exit(-1);
        }

        //Paint thread
        Thread paintThread = (new Thread(new Runnable() {

            public void run() {
                while (true) {
                    if (System.currentTimeMillis() - refFrame > 1000. / FPS) {
                        repaint();
                        refFrame = System.currentTimeMillis();
                    }
                }
            }
        }));
        paintThread.start();
    }

    /**
     * Adds a new bear at p and increments bear count. The rest are similar.
     *
     * @param x X-coordinate of bear's location
     * @param y Y-coordinate of bear's location
     */
    public void addBear(int x, int y) {
        synchronized (lock) {
            locToLife.put(new Point(x, y), new Bear());
            ++bearCount;
            addToLog("Bear spawned at " + x + "," + y);
        }
    }

    public void addBat(int x, int y) {
        synchronized (lock) {
            locToLife.put(new Point(x, y), new Bat());
            ++batCount;
            addToLog("Bat spawned at " + x + "," + y);
        }
    }

    public void addBunny(int x, int y) {
        synchronized (lock) {
            locToLife.put(new Point(x, y), new Bunny());
            ++bunnyCount;
            addToLog("Bunny spawned at " + x + "," + y);
        }
    }

    public void addCattle(int x, int y) {
        synchronized (lock) {
            locToLife.put(new Point(x, y), new Cattle());
            ++cattleCount;
            addToLog("Cattle spawned at " + x + "," + y);
        }
    }

    public void addGrass(int x, int y) {
        synchronized (lock) {
            Grass g = new Grass();
            Point p = new Point(x, y);
            locToLife.put(p, g);
            locToGrass.put(p, g);
            ++grassCount;
            addToLog("Grass placed at " + x + "," + y);
        }
    }

    public void addTree(int x, int y) {
        synchronized (lock) {
            locToLife.put(new Point(x, y), new Tree());
            ++treeCount;
            addToLog("Tree spawned at " + x + "," + y);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(addBear)) {
            makeBear = !makeBear;
        } else if (e.getSource().equals(addBunny)) {
            makeBunny = !makeBunny;
        } else if (e.getSource().equals(addBat)) {
            makeBat = !makeBat;
        } else if (e.getSource().equals(addCattle)) {
            makeCattle = !makeCattle;
        } else if (e.getSource().equals(addTree)) {
            makeTree = !makeTree;
        } else if (e.getSource().equals(addGrass)) {
            makeGrass = !makeGrass;
        }
        s.requestFocusInWindow();
    }

    /**
     * Types of terrain available
     */
    public enum TERRAIN {

        LAND, SEA
    }

    /**
     * The Driver method to run the program
     *
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

        s = new Summative();
        frame.add(s);
        s.requestFocusInWindow();//keyListener activated
        frame.setVisible(true);
    }

    /**
     * Generates a map that radiates out from an initial point, not used anymore
     *
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
     *
     * @param point The point in which we want to generate some terrain
     */
    public void mapGen(Point point) {
        int lands = 0, seas = 0;//count adjacent land, sea
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (locToTerrain.get(new Point(point.x + x, point.y + y)) == TERRAIN.LAND) {
                    lands++;//add a land for each land within the 3x3 surrounding the block
                } else if (locToTerrain.get(new Point(point.x + x, point.y + y)) == TERRAIN.SEA) {
                    seas++;//add a sea for each sea within the 3x3 surrounding block
                }
            }

        }

        //Only land, have a small chance to generate a sea block


        if (seas == 0) {
            if (Math.random() < .95) {
                synchronized (lock) {
                    locToTerrain.put(point, TERRAIN.LAND);
                }
            } else {
                synchronized (lock) {
                    locToTerrain.put(point, TERRAIN.SEA);
                }
            }
            //Mostly land, but some sea, generate more sea
        } else if (lands > seas && seas > 0) {
            if (Math.random() < .95) {
                synchronized (lock) {
                    locToTerrain.put(point, TERRAIN.LAND);
                }
            } else {
                synchronized (lock) {
                    locToTerrain.put(point, TERRAIN.SEA);
                }
            }
            //Mostly sea, but some land, generate more land
        } else if (seas > lands && lands > 0) {
            if (Math.random() < 0.95) {
                synchronized (lock) {
                    locToTerrain.put(point, TERRAIN.SEA);
                }
            } else {
                synchronized (lock) {
                    locToTerrain.put(point, TERRAIN.LAND);
                }
            }
        } else {
            if (Math.random() < 0.5) {
                synchronized (lock) {
                    locToTerrain.put(point, TERRAIN.LAND);
                }
            } else {
                synchronized (lock) {
                    locToTerrain.put(point, TERRAIN.SEA);
                }
            }
        }
    }

    /**
     * Draws a HUD that gives information on what is happening or has happened
     * in the sim
     *
     * @param g The graphics object that the HUD should be drawn on
     */
    public void drawHUD(Graphics g) {
        logButton.translate((hud.x + 400) - logButton.x, (hud.y + 160) - logButton.y);
        g.setColor(Color.BLUE);
        g.fillRoundRect(hud.x, hud.y, hud.width, hud.height, 20, 20);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(hud.x + 20, hud.y + 20, hud.width - 40, hud.height - 40);
        g.setColor(Color.BLACK);

        //draw grid to hold frequency table of lifeforms
        g.drawRect(hud.x + 40, hud.y + 40, 130, 140);
        g.drawLine(hud.x + 40, hud.y + 60, hud.x + 170, hud.y + 60);
        g.drawLine(hud.x + 40, hud.y + 80, hud.x + 170, hud.y + 80);
        g.drawLine(hud.x + 40, hud.y + 100, hud.x + 170, hud.y + 100);
        g.drawLine(hud.x + 40, hud.y + 120, hud.x + 170, hud.y + 120);
        g.drawLine(hud.x + 40, hud.y + 140, hud.x + 170, hud.y + 140);
        g.drawLine(hud.x + 40, hud.y + 160, hud.x + 170, hud.y + 160);
        g.drawLine(hud.x + 100, hud.y + 40, hud.x + 100, hud.y + 180);
        g.setFont(new Font(Font.SERIF, Font.BOLD, 12));
        g.drawString("Lifeform", hud.x + 50, hud.y + 55);
        g.drawString("Frequency", hud.x + 110, hud.y + 55);

        //fill grid with types of lifeforms
        g.setFont(new Font(Font.SERIF, Font.ROMAN_BASELINE, 12));
        g.drawString("Bunny", hud.x + 50, hud.y + 75);
        g.drawString("Bear", hud.x + 50, hud.y + 95);
        g.drawString("Cattle", hud.x + 50, hud.y + 115);
        g.drawString("Bat", hud.x + 50, hud.y + 135);
        g.drawString("Grass", hud.x + 50, hud.y + 155);
        g.drawString("Trees", hud.x + 50, hud.y + 175);

        //fill grid with frequencies
        g.drawString("" + bunnyCount, hud.x + 110, hud.y + 75);
        g.drawString("" + bearCount, hud.x + 110, hud.y + 95);
        g.drawString("" + cattleCount, hud.x + 110, hud.y + 115);
        g.drawString("" + batCount, hud.x + 110, hud.y + 135);
        g.drawString("" + grassCount, hud.x + 110, hud.y + 155);
        g.drawString("" + treeCount, hud.x + 110, hud.y + 175);

        //draws other information onto the HUD
        g.setFont(new Font(Font.SERIF, Font.ROMAN_BASELINE, 16));
        g.drawString("You are centred at " + (screen.x + screen.width / 2) + "," + (screen.y + screen.height / 2), hud.x + 180, hud.y + 60);
        g.drawString(numHours + " hours have passed since the beginning of time", hud.x + 180, hud.y + 100);
        if (mouseOnLife != "") {
            g.drawString("The mouse is over a " + mouseOnLife + " at point " + mouse.x + "," + mouse.y, hud.x + 180, hud.y + 140);
        }

        g.setColor(Color.BLUE);
        g.fillRoundRect(logButton.x - 10, logButton.y - 10, logButton.width + 20, logButton.height + 20, 10, 10);
        g.setColor(Color.WHITE);
        g.fillRect(logButton.x, logButton.y, logButton.width, logButton.height);
        g.setColor(Color.BLACK);
        g.drawString("Open log", logButton.x + 20, logButton.y + 25);

    }

    /**
     * Draws terrain, Green for lane, Blue for sea
     *
     * @param g The graphics object to draw on
     */
    public void drawTerrain(Graphics g) {
        for (int i = screen.x; i <= screen.x + screen.width; i++) {
            for (int j = screen.y; j <= screen.y + screen.height; j++) {
                g.drawImage(sprites.get((locToTerrain.get(new Point(i, j))).toString()),
                        (i - screen.x) * gridSize, (j - screen.y) * gridSize,
                        gridSize, gridSize, null);
            }
        }
    }

    /**
     * Right now, I have it so that a square is highlig Draws the lifeforms in
     * the sim
     *
     * @param g the graphics onject that the lifeforms should be drawn on
     */
    public void drawLifeforms(Graphics g) {
        for (Iterator<Map.Entry<Point, Lifeform>> iter = locToLife.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<Point, Lifeform> pair = iter.next();
            if (screen.contains(pair.getKey())) {
                // Draw the sprites so that its centre is at the centre
                // of the grid square
                Image sprite = pair.getValue().getSprite();
                int x = (pair.getKey().x - screen.x) * gridSize + gridSize / 2 - sprite.getWidth(null) / 2;
                int y = (pair.getKey().y - screen.y) * gridSize + gridSize / 2 - sprite.getWidth(null) / 2;
                g.drawImage(sprite, x, y, null);
            }
        }
    }

    /**
     * Draws the weather
     *
     * @param g
     */
    public void drawWeather(Graphics g) {
        for (int i = screen.x; i < screen.x + screen.width; i++) {
            for (int j = screen.y; j < screen.y + screen.height; j++) {
                switch (getActiveWeather(i, j)) {
                    case CLOUD:
                        g.setColor(new Color(153, 153, 153, 153));
                        break;
                    case RAIN:
                        g.setColor(new Color(33, 33, 33, 153));
                        break;
                    case NIGHT:
                        g.setColor(new Color(0, 0, 62, 153));
                        break;
                    case SUN:
                        g.setColor(new Color(0, 0, 0, 0));
                        break;
                }
                g.fillRect((i - screen.x) * gridSize,
                        (j - screen.y) * gridSize,
                        gridSize, gridSize);
            }
        }

        for (Weather w : activeWeather) {
            Point centre = w.getCentre();
            if (screen.x <= centre.x && centre.x < screen.x + screen.width
                    && screen.y <= centre.y && centre.y < screen.y + screen.height) {
                g.drawImage(sprites.get(w.getType().toString()),
                        (int) (centre.x - screen.x) * gridSize,
                        (int) (centre.y - screen.y) * gridSize,
                        64, 64, null);
            }
        }
    }

    /**
     * Draws a log of what has happened so far
     *
     * @param g the graphics object to draw on
     */
    public void drawLog(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SERIF, Font.ROMAN_BASELINE, 20));
        g.drawString("Log of what has happened", 100, 40);
        for (int i = events.size() - 1; i >= 0; i--) {
            g.drawString(events.get(i), 100, 40 + (events.size() - i) * 40);
        }
    }

    /**
     * Adds a string to be added to the log of events
     *
     * @param s The string representing what happened
     */
    public void addToLog(String s) {
        events.add(s);
    }

    /**
     * Kills the lifeform
     *
     * @param l the lifeform to kill
     */
    public void kill(Lifeform l) {
        locToLife.remove(getLocation(l));
    }

    /**
     * Kills the lifeform at the given point
     *
     * @param location the place at which a lifeform is to be killed
     */
    public void kill(Point location) {
        locToLife.remove(location);
    }

    /**
     * Returns the lifeform at a given point
     *
     * @param location the point that's lifeform is being checked
     * @return The lifeform at point location
     */
    public Lifeform lifeGet(Point location) {
        return locToLife.get(location);
    }

    /**
     * Returns the type of terrain at a given point
     *
     * @param location The point at which the terrain is desired
     * @return the type of terrain at location
     */
    public TERRAIN terrainGet(Point location) {
        return locToTerrain.get(location);
    }

    /**
     * Returns the grass at that location
     *
     * @param location the point at which we are looking for grass
     * @return The grass object at that point
     */
    public Grass grassGet(Point location) {
        return locToGrass.get(location);
    }

    /**
     * Adds in a new baby animal
     *
     * @param p where the baby animal will be placed
     * @param l the baby animal
     */
    public void add(Point p, Lifeform l) {
        locToLife.put(p, l);
        l.setLocation(p);
        if (l instanceof Bear) {
            bearCount++;
        } else if (l instanceof Bunny) {
            bunnyCount++;
        } else if (l instanceof Cattle) {
            cattleCount++;
        } else if (l instanceof Grass) {
            grassCount++;
        } else if (l instanceof Tree) {
            treeCount++;
        }
    }

    /**
     * Moves the lifeform
     */
    public void move(Point p, Lifeform l) {
        locToLife.remove(l.getLocation());
        locToLife.put(p, l);
        l.setLocation(p);
    }

    /**
     * Unused method
     *
     * @param e KeyEvent fired as a type event
     */
    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    /**
     * Checks which of the arrow keys have been hit and moves the perspective
     * accordingly
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("keyPressed");
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = true;//check off that right has been pressed
        } else if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = true;//check off that left has been pressed
        } else if (keyCode == KeyEvent.VK_UP) {
            upPressed = true;//check off that up has been pressed
        } else if (keyCode == KeyEvent.VK_DOWN) {
            downPressed = true;//check off that down has been pressed
        } else if (keyCode == KeyEvent.VK_ESCAPE && !logOpen) {
            System.exit(0);
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            logOpen = false;
        } else if (keyCode == KeyEvent.VK_SPACE) {
            //advance();
        }
        if (rightPressed) {
            moveRight();
        }
        if (leftPressed) {
            moveLeft();
        }
        if (upPressed) {
            moveUp();
        }
        if (downPressed) {
            moveDown();
        }
        updateMap();//generates map as needed
    }

    /**
     * Stores when the arrow keys have been released
     *
     * @param e The KeyEvent fired
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_RIGHT)//right isn't pressed anymore
        {
            rightPressed = false;
        }
        if (keyCode == KeyEvent.VK_LEFT)//left isn't pressed anymore
        {
            leftPressed = false;
        }
        if (keyCode == KeyEvent.VK_UP)//up isn't pressed anymore
        {
            upPressed = false;
        }
        if (keyCode == KeyEvent.VK_DOWN)//down isn't pressed anymore
        {
            downPressed = false;
        }
    }

    /**
     * Generates more map as it becomes visible to the user
     */
    public void updateMap() {
        for (int i = screen.x; i <= screen.x + screen.width; i++) {//runs through x values
            for (int j = screen.y; j <= screen.y + screen.height; j++) {//runs through y values
                if (!locToTerrain.containsKey(new Point(i, j)))//do we have a point there?
                {
                    mapGen(new Point(i, j));//create point in that map
                }
            }
        }
    }

    /**
     * Moves the perspective towards the right
     */
    public void moveRight() {
        screen.translate(1, 0);//moves the perspective
    }

    /**
     * Moves the perspective to the left
     */
    public void moveLeft() {
        screen.translate(-1, 0);//moves "camera"
    }

    /**
     * Moves the perspective upwards
     */
    public void moveUp() {
        screen.translate(0, -1);//moves viewpoint
    }

    /**
     * Moves the perspective downward
     */
    public void moveDown() {
        screen.translate(0, 1);//moves centre of field of view
    }

    /**
     * Called when the mouse is dragged
     *
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * Checks where the mouse is and prints if there is a lifeform there
     *
     * @param e The MouseEvent from moving the mouse
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouse = new Point(e.getPoint().x / 10 + screen.x, e.getPoint().y / 10 + screen.y);
        if (locToLife.containsKey(mouse)) {
            Lifeform l = locToLife.get(mouse);
            if (l instanceof Bear) {
                synchronized (lock) {
                    mouseOnLife = "bear";
                }
            } else if (l instanceof Bunny) {
                synchronized (lock) {
                    mouseOnLife = "bunny";
                }
            } else if (l instanceof Cattle) {
                synchronized (lock) {
                    mouseOnLife = "cow";
                }
            } else if (l instanceof Bat) {
                synchronized (lock) {
                    mouseOnLife = "bat";
                }
            } else if (l instanceof Grass) {
                synchronized (lock) {
                    mouseOnLife = "grass";
                }
            } else if (l instanceof Tree) {
                synchronized (lock) {
                    mouseOnLife = "tree";
                }
            }

        } else {
            synchronized (lock) {
                mouseOnLife = "";
            }
        }
    }

    /**
     * Checks to see if you clicked in one of the "button" rectangles, acts
     * accordingle
     *
     * @param e the MouseEvent from clicking
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Point clickPoint = e.getPoint();
        if (logButton.contains(clickPoint)) {
            logOpen = true;
        } else if (!hud.contains(clickPoint)) {
            synchronized (lock) {
                selectedPoint = new Point(
                        clickPoint.x / gridSize + screen.x,
                        clickPoint.y / gridSize + screen.y);
            }
        }
    }

    /**
     * Called when the mouse is pressed down
     *
     * @param e The MouseEvent fired by pressing the mouse
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Called when the mouse is released
     *
     * @param e The MouseEvent fired by releasing the mouse
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Called when the mouse enters the screen
     *
     * @param e The MouseEvent when the mouse enters the screen
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Called when the mouse exits the screen
     *
     * @param e The MouseEvent fired when the mouse exits
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Loads the images into a hash map
     *
     * @throws IOException
     */
    public void loadSprites() throws IOException {
        sprites = new HashMap<String, Image>();
        SpriteAssigner.sprites = sprites;
        Class c = getClass();

        sprites.put(Weather.WEATHER.RAIN.toString(), ImageIO.read(
                c.getResource("images/weather-showers-scattered.png")));

        sprites.put(Weather.WEATHER.SUN.toString(), ImageIO.read(
                c.getResource("images/weather-clear.png")));

        sprites.put(Weather.WEATHER.CLOUD.toString(), ImageIO.read(
                c.getResource("images/weather-overcast.png")));


        sprites.put(TERRAIN.LAND.toString(), ImageIO.read(
                c.getResource("images/waste.png")));

        sprites.put(TERRAIN.SEA.toString(), ImageIO.read(
                c.getResource("images/sea.png")));

        sprites.put("bear", ImageIO.read(
                c.getResource("images/216.png")));
        sprites.put("bunny", ImageIO.read(
                c.getResource("images/427.png")));
        sprites.put("cattle", ImageIO.read(
                c.getResource("images/128.png")));
        sprites.put("grass", ImageIO.read(
                c.getResource("images/grassy.png")));
        sprites.put("tree", ImageIO.read(
                c.getResource("images/185.png")));
        sprites.put("vegetable", ImageIO.read(
                c.getResource("images/420.png")));
        sprites.put("bat", ImageIO.read(
                c.getResource("images/041.png")));


    }

    /**
     * Gets the weather in the mentioned square
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @return The weather in the mentioned square
     */
    public Weather.WEATHER getActiveWeather(int x, int y) {
        Weather.WEATHER ret;

        if (Math.sin(2 * Math.PI / 1000 * x + 2 * Math.PI * numHours) > 0) {
            ret = Weather.WEATHER.SUN;
        } else {
            ret = Weather.WEATHER.NIGHT;
        }
        for (Weather w : activeWeather) {
            if (w.contains(x, y)) {
                if (ret == Weather.WEATHER.RAIN) {
                    break;
                }
                ret = w.getType();
            }
        }
        return ret;
    }

    /**
     * Moves the clouds a bit
     */
    public void pushWeather() {
        for (Weather w : activeWeather) {
            w.translate(hourlyWind.x, hourlyWind.y);
        }
    }

    /**
     * Used to make loops regular
     */
    /**
     * Gets the location of a lifeform
     *
     * @param l The lifeform to locate
     * @return l's location
     */
    public Point getLocation(Lifeform l) {
        for (Map.Entry<Point, Lifeform> e : locToLife.entrySet()) {
            if (e.getValue() == l) {
                return e.getKey();
            }
        }
        return null;
    }

    /**
     * Moves a lifeform
     *
     * @param l The lifeform to move
     * @param p Its new location
     */
    public void moveTo(Lifeform l, Point p) {
        synchronized (lock) {
            locToLife.remove(getLocation(l));
            locToLife.put(p, l);
        }
    }

    /**
     * advances the map one iteration
     */
    public void advance() {
        numHours++;
        pushWeather();
        synchronized (lock) {
            for (Weather w : activeWeather) {
                for (Map.Entry<Point, Lifeform> pair : locToLife.entrySet()) {
                    if (w.contains(pair.getKey())) {//if we are on the weather
                        pair.getValue().act(w.getType());//act based on weather
                    }
                }

                HashMap<Point, TERRAIN> temp = new HashMap<Point, TERRAIN>();
                for (Map.Entry<Point, TERRAIN> pair : locToTerrain.entrySet()) {
                    if (w.contains(pair.getKey())) {
                        if (w.getType() == Weather.WEATHER.RAIN && pair.getValue() == TERRAIN.SEA) {
                            //expand water
                            Point original = pair.getKey();
                            Point[] points = {new Point(original.x, original.y),
                                new Point(original.x - 1, original.y),
                                new Point(original.x + 1, original.y),
                                new Point(original.x, original.y - 1),
                                new Point(original.x, original.y + 1)};
                            for (Point p : points) {
                                temp.put(p, TERRAIN.SEA);
                            }
                        } else if (w.getType() == Weather.WEATHER.SUN && pair.getValue() == TERRAIN.LAND) {
                            //radiate land
                            Point original = pair.getKey();
                            Point[] points = {new Point(original.x, original.y),
                                new Point(original.x - 1, original.y),
                                new Point(original.x + 1, original.y),
                                new Point(original.x, original.y - 1),
                                new Point(original.x, original.y + 1)};
                            for (Point p : points) {
                                temp.put(p, TERRAIN.LAND);
                            }
                        }
                    }
                }
                for (Map.Entry<Point, TERRAIN> m : temp.entrySet()) {
                    locToTerrain.put(m.getKey(), m.getValue());
                }
            }
        }
    }

    /**
     * draws the graphics on the screen
     *
     * @param g The graphics object needed
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!logOpen) {
            synchronized (lock) {
                drawTerrain(g);//draws the terrain of the map

                if (selectedPoint != null) {
                    if (!screen.contains(selectedPoint)) {
                        selectedPoint = null;
                    } else {
                        Graphics2D g2 = (Graphics2D) g;
                        g.setColor(Color.red);
                        g2.setStroke(new BasicStroke(2));
                        g.drawRect(
                                (selectedPoint.x - screen.x) * gridSize,
                                (selectedPoint.y - screen.y) * gridSize,
                                gridSize, gridSize);
                    }
                }

                drawLifeforms(g);
                drawWeather(g);
                drawHUD(g);


            }
        } else {
            drawLog(g);
        }
    }
}