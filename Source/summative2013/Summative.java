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

public class Summative extends JPanel implements KeyListener, MouseMotionListener, MouseListener {

    private static GraphicsEnvironment ge;
    private static GraphicsDevice gd;
    private HashMap<Point, Lifeform> locToLife;
    private HashMap<Point, Grass> locToGrass;
    private HashMap<Point, TERRAIN> locToTerrain;
    private final Object lock = new Object();
    private ArrayList<Weather> activeWeather;
    private ArrayList<String> events;
    private static JFrame frame;
    private Rectangle screen, logButton, hud;
    private boolean upPressed = false, downPressed = false, rightPressed = false, leftPressed = false, logOpen = false;
    ;
	private final int gridSize = 10;
<<<<<<< HEAD
	private String mouseOnLife = "";
	private Point mouse = new Point();
	private int batCount = 0, bearCount = 0, bunnyCount = 0, cattleCount = 0, grassCount = 0,
			treeCount = 0, numHours = 0;

	/**
	 * Default constructor, Generates a Summative object that is the size of the
	 * screen, with a map to fill it
	 */
	public Summative() {
		Lifeform.summative = this;//sets the panel for all of the lifeforms to be this
		locToLife = new HashMap<Point, Lifeform>();//initializes our point, lifeform hashmap
		addBear(0, 0);
		addBunny(0, 10);
		addCattle(0, 20);
		addGrass(60, 20);
		addGrass(70, 20);
		addGrass(80, 20);
		addTree(-30, -30);
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

		logButton = new Rectangle(getWidth() - 200, getHeight() - 40, 200, 40);
		hud = new Rectangle(getWidth() - 600, getHeight() - 200, 620, 220);
		events = new ArrayList<String>();
		events.add("Test0");
		events.add("Test1");
		events.add("Test2");

		Area a = (new Area(new Ellipse2D.Double(0, 0, 30, 30)));
		a.add(new Area(new Ellipse2D.Double(5, 20, 40, 60)));
		activeWeather.add(new summative2013.phenomena.AirLock(a));

		a = new Area(new Rectangle(-30, -60, 20, 69));
		a.add(new Area(new Ellipse2D.Double(-90, -90, 70, 70)));
		activeWeather.add(new summative2013.phenomena.Drizzle(a));

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
	final int FPS = 60;

	/**
	 * Adds a new bear at p and increments bear count.
	 * The rest are similar.
	 * @param x X-coordinate of bear's location
	 * @param y Y-coordinate of bear's location
	 */
	public void addBear(int x, int y) {
		locToLife.put(new Point(x, y), new Bear());
		++bearCount;
	}

	public void addBat(int x, int y) {
		locToLife.put(new Point(x, y), new Bat());
		++batCount;
	}

	public void addBunny(int x, int y) {
		locToLife.put(new Point(x, y), new Bunny());
		++bunnyCount;
	}

	public void addCattle(int x, int y) {
		locToLife.put(new Point(x, y), new Cattle());
		++cattleCount;
	}

	public void addGrass(int x, int y) {
		locToLife.put(new Point(x, y), new Grass());
		++grassCount;
	}

	public void addTree(int x, int y) {
		locToLife.put(new Point(x, y), new Tree());
		++treeCount;
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
		Summative s = new Summative();
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
	 * advances the map one iteration
	 */
	public void advance() {
		numHours++;
		pushWeather();
		for (Weather w : activeWeather) {
			Iterator lifeIt = locToLife.entrySet().iterator();
			while (lifeIt.hasNext()) {

				Map.Entry<Point, Lifeform> pairs = (Map.Entry) lifeIt.next();

				if (w.contains(pairs.getKey())) {//if we are on the weather
					pairs.getValue().act(w.getType());//act based on weather
				}
			}
			Iterator terIt = locToTerrain.entrySet().iterator();
			HashMap<Point, TERRAIN> temp = new HashMap<Point, TERRAIN>();
			while (terIt.hasNext()) {
				Map.Entry<Point, TERRAIN> pairs = (Map.Entry) terIt.next();
				if (w.contains(pairs.getKey())) {
					if (w.getType() == Weather.WEATHER.RAIN && pairs.getValue() == TERRAIN.SEA) {
						//expand water
						Point[] points = {new Point(pairs.getKey().x, pairs.getKey().y), new Point(pairs.getKey().x - 1, pairs.getKey().y), new Point(pairs.getKey().x + 1, pairs.getKey().y), new Point(pairs.getKey().x, pairs.getKey().y - 1), new Point(pairs.getKey().x, pairs.getKey().y + 1)};
						for (Point p : points) {
							temp.put(p, TERRAIN.SEA);
						}
					}
					if (w.getType() == Weather.WEATHER.SUN && pairs.getValue() == TERRAIN.LAND) {
						//radiate land
						Point[] points = {new Point(pairs.getKey().x, pairs.getKey().y), new Point(pairs.getKey().x - 1, pairs.getKey().y), new Point(pairs.getKey().x + 1, pairs.getKey().y), new Point(pairs.getKey().x, pairs.getKey().y - 1), new Point(pairs.getKey().x, pairs.getKey().y + 1)};
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

	/**
	 * Draws a HUD that gives information on what is happening or has happened in the sim
	 * @param g The graphics object that the HUD should be drawn on
	 */
	public void drawHUD(Graphics g) {
		g.setColor(new Color(0,0,255,128));
		g.fillRoundRect(hud.x, hud.y, hud.width, hud.height, 20, 20);
		g.setColor(new Color(255,255,204,128));
		g.fillRect(getWidth() - 580, getHeight() - 180, 580, 180);
		g.setColor(new Color(0,0,0,128));

		//draw grid to hold frequency table of lifeforms
		g.drawRect(getWidth() - 560, getHeight() - 160, 130, 120);
		g.drawLine(getWidth() - 560, getHeight() - 140, getWidth() - 430, getHeight() - 140);
		g.drawLine(getWidth() - 560, getHeight() - 120, getWidth() - 430, getHeight() - 120);
		g.drawLine(getWidth() - 560, getHeight() - 100, getWidth() - 430, getHeight() - 100);
		g.drawLine(getWidth() - 560, getHeight() - 80, getWidth() - 430, getHeight() - 80);
		g.drawLine(getWidth() - 560, getHeight() - 60, getWidth() - 430, getHeight() - 60);
		g.drawLine(getWidth() - 500, getHeight() - 160, getWidth() - 500, getHeight() - 40);
		g.setFont(new Font(Font.SERIF, Font.BOLD, 12));
		g.drawString("Lifeform", getWidth() - 550, getHeight() - 145);
		g.drawString("Frequency", getWidth() - 490, getHeight() - 145);

		//fill grid with types of lifeforms
		g.setFont(new Font(Font.SERIF, Font.ROMAN_BASELINE, 12));
		g.drawString("Bunny", getWidth() - 550, getHeight() - 125);
		g.drawString("Bear", getWidth() - 550, getHeight() - 105);
		g.drawString("Cattle", getWidth() - 550, getHeight() - 85);
		g.drawString("Grass", getWidth() - 550, getHeight() - 65);
		g.drawString("Trees", getWidth() - 550, getHeight() - 45);

		//fill grid with frequencies
		g.drawString("" + bunnyCount, getWidth() - 490, getHeight() - 125);
		g.drawString("" + bearCount, getWidth() - 490, getHeight() - 105);
		g.drawString("" + cattleCount, getWidth() - 490, getHeight() - 85);
		g.drawString("" + grassCount, getWidth() - 490, getHeight() - 65);
		g.drawString("" + treeCount, getWidth() - 490, getHeight() - 45);

		//draws other information onto the HUD
		g.setFont(new Font(Font.SERIF, Font.ROMAN_BASELINE, 16));
		g.drawString("You are centred at " + (screen.x + screen.width / 2) + "," + (screen.y + screen.height / 2), getWidth() - 420, getHeight() - 140);
		g.drawString(numHours + " hours have passed since the beginning of time", getWidth() - 420, getHeight() - 100);
		if (mouseOnLife != "") {
			g.drawString("The mouse is over a " + mouseOnLife + " at point " + mouse.x + "," + mouse.y, getWidth() - 420, getHeight() - 60);
		}

		g.setColor(Color.BLUE);
		g.fillRoundRect(logButton.x - 10, logButton.y - 10, logButton.width + 20, logButton.height + 20, 10, 10);
		g.setColor(Color.WHITE);
		g.fillRect(logButton.x, logButton.y, logButton.width, logButton.height);
		g.setColor(Color.BLACK);
		g.drawString("Open log", logButton.x + 20, logButton.y + logButton.height - 10);

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

	/**Right now, I have it so that a square is highlig
	 * Draws the lifeforms in the sim
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
	 * @param location the point at which we are looking for grass
	 * @return The grass object at that point
	 */
	public Grass grassGet(Point location) {
		return locToGrass.get(location);
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
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		} else if (keyCode == KeyEvent.VK_ESCAPE) {
			logOpen = false;
		} else if (keyCode == KeyEvent.VK_SPACE) {
			advance();
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
	 * @param e 
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * Checks where the mouse is and prints if there is a lifeform there
	 * @param e The MouseEvent from moving the mouse
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		mouse = new Point(e.getPoint().x / 10 + screen.x, e.getPoint().y / 10 + screen.y);
		if (locToLife.containsKey(mouse)) {
			mouseOnLife = locToLife.get(mouse).getName();

		} else {
			synchronized (lock) {
				mouseOnLife = "";
			}
		}
	}

	/**
	 * Checks to see if you clicked in one of the "button" rectangles, acts accordingle
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
						e.getPoint().x / gridSize + screen.x,
						e.getPoint().y / gridSize + screen.y);
			}
		}
	}
	/**
	 * Point that is selected. Display info on it.
	 */
	Point selectedPoint;

	/**
	 * Called when the mouse is pressed down
	 * @param e The MouseEvent fired by pressing the mouse
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Called when the mouse is released
	 * @param e The MouseEvent fired by releasing the mouse
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Called when the mouse enters the screen
	 * @param e The MouseEvent when the mouse enters the screen
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Called when the mouse exits the screen
	 * @param e The MouseEvent fired when the mouse exits
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Loads the images into a hash map
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
	 * Wind speed, affects pushWeather
	 */
	private HashMap<String, Image> sprites;
	private Point2D.Double hourlyWind = new Point2D.Double(-0.03, 0.03);
	/**
	 * Used to make loops regular
	 */
	private long refFrame = System.currentTimeMillis();

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
	 * @param l The lifeform to move
	 * @param p Its new location
	 */
	public void moveTo(Lifeform l, Point p) {
		synchronized (lock) {
			locToLife.remove(getLocation(l));
			locToLife.put(p, l);
		}
	}
}