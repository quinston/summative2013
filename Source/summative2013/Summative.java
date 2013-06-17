package summative2013;

import java.awt.*;
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
import java.util.Random;
import javax.swing.JButton;
import summative2013.phenomena.AirLock;
import summative2013.phenomena.Drizzle;
import summative2013.phenomena.Drought;

/**
 * Driver class for the program
 *
 * @author Stephen
 */
public class Summative extends JPanel implements KeyListener, MouseMotionListener, MouseListener, ActionListener {

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
	private final int gridSize = 20;
	private String mouseOnLife = "";
	private JPanel buttonPanel;
	private Point mouse = new Point();
	private JButton addBear, addBunny, addBat, addCattle, addGrass, addTree;
	private static Summative s;
	//private int batCount = 0, bearCount = 0, bunnyCount = 0, cattleCount = 0, grassCount = 0,
	//		treeCount = 0;
			private int numHours = 0;
	private Random rand;
	// World limits
	private int outerNegX = 0, outerNegY = 0,
			outerPosX = 0, outerPosY = 0;

	/**
	 * Default constructor, Generates a Summative object that is the size of the
	 * screen, with a map to fill it
	 */
	public Summative() {
		Lifeform.summative = this;//sets the panel for all of the lifeforms to be this
		events = new ArrayList<String>();
		setLayout(new BorderLayout());
		locToLife = new HashMap<Point, Lifeform>();//initializes our point, lifeform hashmap
		locToTerrain = new HashMap<Point, TERRAIN>();//initializes our point, terrain hashmap
		activeWeather = new ArrayList<Weather>();
		locToGrass = new HashMap<Point, Grass>();

		/*
		addBear(0, 0);
		addBunny(0, 10);
		addCattle(0, 20);
		addGrass(60, 20);
		addGrass(70, 20);
		addGrass(80, 20);
		addTree(-30, -30);
		 * */
		 

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

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 6));

		addBear = new JButton("Add a bear");
		addBear.setEnabled(false);
		addBear.addActionListener(this);
		buttonPanel.add(addBear);

		addBunny = new JButton("Add a bunny");
		addBunny.setEnabled(false);
		addBunny.addActionListener(this);
		buttonPanel.add(addBunny);

		addCattle = new JButton("Add a cow");
		addCattle.setEnabled(false);
		addCattle.addActionListener(this);
		buttonPanel.add(addCattle);

		addBat = new JButton("Add a bat");
		addBat.setEnabled(false);
		addBat.addActionListener(this);
		buttonPanel.add(addBat);

		addTree = new JButton("Add a tree");
		addTree.setEnabled(false);
		addTree.addActionListener(this);
		buttonPanel.add(addTree);

		addGrass = new JButton("Add some grass");
		addGrass.setEnabled(false);
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

		rand = new Random();
	}
	final int FPS = 60;

	/**
	 * Adds a new bear at point x,y and increments bear count. 
	 *
	 * @param x X-coordinate of bear's location
	 * @param y Y-coordinate of bear's location
	 */
	public void addBear(int x, int y) {
		synchronized (lock) {
			if (!locToLife.containsKey(new Point(x, y))) {
				locToLife.put(new Point(x, y), new Bear());
				//++bearCount;
				addToLog("Bear spawned at " + x + "," + y);
			}
		}
	}

	/**
	 * Adds a new bat at point x,y and increments bear count. 
	 *
	 * @param x X-coordinate of bear's location
	 * @param y Y-coordinate of bear's location
	 */
	public void addBat(int x, int y) {
		synchronized (lock) {
			if (!locToLife.containsKey(new Point(x, y))) {
				locToLife.put(new Point(x, y), new Bat());
				//++batCount;
				addToLog("Bat spawned at " + x + "," + y);
			}
		}
	}

	/**
	 * Adds a new bunny at point x,y and increments bear count. 
	 *
	 * @param x X-coordinate of bear's location
	 * @param y Y-coordinate of bear's location
	 */
	public void addBunny(int x, int y) {
		synchronized (lock) {
			if (!locToLife.containsKey(new Point(x, y))) {
				locToLife.put(new Point(x, y), new Bunny());
				//++bunnyCount;
				addToLog("Bunny spawned at " + x + "," + y);
			}
		}
	}

	/**
	 * Adds a new cattle at point x,y and increments bear count. 
	 *
	 * @param x X-coordinate of bear's location
	 * @param y Y-coordinate of bear's location
	 */
	public void addCattle(int x, int y) {
		synchronized (lock) {
			if (!locToLife.containsKey(new Point(x, y))) {
				locToLife.put(new Point(x, y), new Cattle());
				//++cattleCount;
				addToLog("Cattle spawned at " + x + "," + y);
			}
		}
	}

	/**
	 * Adds a new grass at point x,y and increments bear count. 
	 *
	 * @param x X-coordinate of bear's location
	 * @param y Y-coordinate of bear's location
	 */
	public void addGrass(int x, int y) {
		synchronized (lock) {
			if (!locToLife.containsKey(new Point(x, y)) && !locToGrass.containsKey(new Point(x, y))) {
				Grass g = new Grass();
				Point p = new Point(x, y);
				//locToLife.put(p, g);
				locToGrass.put(p, g);
				//++grassCount;
				addToLog("Grass placed at " + x + "," + y);
			}
		}
	}

	/**
	 * Adds a new tree at point x,y and increments bear count. 
	 *
	 * @param x X-coordinate of bear's location
	 * @param y Y-coordinate of bear's location
	 */
	public void addTree(int x, int y) {
		synchronized (lock) {
			if (!locToLife.containsKey(new Point(x, y))) {
				locToLife.put(new Point(x, y), new Tree());
				//++treeCount;
				addToLog("Tree spawned at " + x + "," + y);
			}
		}
	}

	/**
	 * Identifies which button was pressed and adds a lifeform accordingly
	 * @param e 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addBear)) {
			addBear(selectedPoint.x, selectedPoint.y);
		} else if (e.getSource().equals(addBunny)) {
			addBunny(selectedPoint.x, selectedPoint.y);
		} else if (e.getSource().equals(addBat)) {
			addBat(selectedPoint.x, selectedPoint.y);
		} else if (e.getSource().equals(addCattle)) {
			addCattle(selectedPoint.x, selectedPoint.y);
		} else if (e.getSource().equals(addTree)) {
			addTree(selectedPoint.x, selectedPoint.y);
		} else if (e.getSource().equals(addGrass)) {
			addGrass(selectedPoint.x, selectedPoint.y);
		}
		disableAddButtons();
		s.requestFocusInWindow();
	}

	private void enableAddButtons() {
		addBat.setEnabled(true);
		addBear.setEnabled(true);
		addBunny.setEnabled(true);
		addCattle.setEnabled(true);
		addGrass.setEnabled(true);
		addTree.setEnabled(true);
	}

	private void disableAddButtons() {
		addBat.setEnabled(false);
		addBear.setEnabled(false);
		addBunny.setEnabled(false);
		addCattle.setEnabled(false);
		addGrass.setEnabled(false);
		addTree.setEnabled(false);
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
				if (point.x + x < outerNegX) {
					outerNegX = point.x + x;
				} else if (point.x + x > outerPosX) {
					outerPosX = point.x + x;
				}
				if (point.y + y < outerNegY) {
					outerNegY = point.y + y;
				} else if (point.y + y > outerPosY) {
					outerPosY = point.y + y;
				}

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
		HashMap<Point, TERRAIN> temp = new HashMap<Point, TERRAIN>();
		//Iterate over copy to prevent concurrent modification
		//because act() methods will surely modify it.
		HashMap<Point,Lifeform> temp2 = new HashMap<Point,Lifeform>();
		temp2.putAll(locToLife);
		
		manageWeather();
		pushWeather();
		synchronized (lock) {
			for (Map.Entry<Point, Lifeform> pair : temp2.entrySet()) {
				pair.getValue().act(getActiveWeather(pair.getKey().x,
						pair.getKey().y));
			}
			
			for (Map.Entry<Point, TERRAIN> pair : locToTerrain.entrySet()) {
				Point original = pair.getKey();
				int lands = 0, seas = 0;//count adjacent land, sea
				for (int x = -1; x <= 1; x++) {
					for (int y = -1; y <= 1; y++) {
						if (locToTerrain.get(new Point(original.x + x, original.y + y)) != TERRAIN.LAND) {
							lands++;//add a land for each land within the 3x3 surrounding the block
						} else if (locToTerrain.get(new Point(original.x + x, original.y + y)) != TERRAIN.SEA) {
							seas++;//add a sea for each sea within the 3x3 surrounding block
						}
					}
				}
				if (getActiveWeather(original.x, original.y) == Weather.WEATHER.SUN) {
					if (Math.random() < 0.01 && locToTerrain.get(original) != TERRAIN.LAND && lands < seas) {
						temp.put(original, TERRAIN.LAND);
					}
				} else if (getActiveWeather(original.x, original.y) == Weather.WEATHER.RAIN) {
					if (Math.random() < 0.1 && locToTerrain.get(original) != TERRAIN.SEA && seas < lands) {
						temp.put(original, TERRAIN.SEA);
					}
				}
			}
			for (Map.Entry<Point, TERRAIN> pair : temp.entrySet()) {
				locToTerrain.put(pair.getKey(), pair.getValue());
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
						g2.setColor(Color.red);
						g2.setStroke(new BasicStroke(2));
						g2.drawRect(
								(selectedPoint.x - screen.x) * gridSize,
								(selectedPoint.y - screen.y) * gridSize,
								gridSize, gridSize);

						g.setColor(new Color(0, 0, 0, 204));
						int height = 120;
						g.fillRect(0, getHeight() - height,
								250, height);
						g.setColor(Color.WHITE);

						String[] desc = {
							selectedPoint.x + "," + selectedPoint.y,
							"Terrain: " + locToTerrain.get(selectedPoint).toString(),
							"Weather: " + getActiveWeather(selectedPoint.x,
							selectedPoint.y).toString()
						};
						for (int i = 0; i < desc.length; ++i) {
							g.drawString(desc[i], 0,
									getHeight() - height + (i + 1) * 12);
						}
					}
				}

				if (selectedLifeform != null) {
					Point location = getLocation(selectedLifeform);
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.red);
					g2.setStroke(new BasicStroke(2));
					g2.drawOval((location.x - screen.x) * gridSize - gridSize,
							(location.y - screen.y) * gridSize - gridSize,
							gridSize * 3, gridSize * 3);

					g.setColor(new Color(0, 0, 0, 204));
					int height = 120;
					g.fillRect(0, getHeight() - height,
							250, height);
					g.setColor(Color.WHITE);

					g.drawString(location.x + "," + location.y,
							0, getHeight() - height + 12);
					String[] desc = selectedLifeform.toString().split("\n");
					for (int i = 1; i < desc.length; ++i) {
						g.drawString(desc[i], 0,
								getHeight() - height + (i + 1) * 12);
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
	 * Draws a HUD that gives information on what is happening or has happened
	 * in the sim
	 *
	 * @param g The graphics object that the HUD should be drawn on
	 */
	public void drawHUD(Graphics g) {
		logButton.translate((hud.x + 400) - logButton.x, (hud.y + 160) - logButton.y);
		g.setColor(new Color(0,0,255,180));
		g.fillRoundRect(hud.x, hud.y, hud.width, hud.height, 20, 20);
		g.setColor(new Color(200,200,200,180));
		g.fillRect(hud.x + 20, hud.y + 20, hud.width - 40, hud.height - 40);
		g.setColor(new Color(0,0,0,180));

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
		g.drawString("" + Bunny.bunnyCount, hud.x + 110, hud.y + 75);
		g.drawString("" + Bear.bearCount, hud.x + 110, hud.y + 95);
		g.drawString("" + Cattle.cattleCount, hud.x + 110, hud.y + 115);
		g.drawString("" + Bat.batCount, hud.x + 110, hud.y + 135);
		g.drawString("" + Grass.grassCount, hud.x + 110, hud.y + 155);
		g.drawString("" + Tree.treeCount, hud.x + 110, hud.y + 175);

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
		for (Map.Entry<Point, Grass> pair : locToGrass.entrySet()) {
			if (screen.contains(pair.getKey())) {
				// Draw the sprites so that its centre is at the centre
				// of the grid square
				Image sprite = pair.getValue().getSprite();
				int x = (pair.getKey().x - screen.x) * gridSize ;
				int y = (pair.getKey().y - screen.y) * gridSize ;
				g.drawImage(sprite, x, y,gridSize,gridSize,null);
			}
		}
		for (Iterator<Map.Entry<Point, Lifeform>> iter = locToLife.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<Point, Lifeform> pair = iter.next();
			if (screen.contains(pair.getKey())) {
				// Draw the sprites so that its centre is at the centre
				// of the grid square
				Image sprite = pair.getValue().getSprite();
				int x = (pair.getKey().x - screen.x) * gridSize ;
				int y = (pair.getKey().y - screen.y) * gridSize ;
				g.drawImage(sprite, x, y,gridSize,gridSize,null);
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
		g.drawString("Log of what has happened, Press Esc to return", 100, 40);
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
		synchronized (lock) {
			locToLife.remove(getLocation(l));
			if (l == selectedLifeform) {
				selectedLifeform = null;
			}
		}
	}

	/**
	 * Kills the lifeform at the given point
	 *
	 * @param location the place at which a lifeform is to be killed
	 */
	public void kill(Point location) {
		synchronized (lock) {
			kill(locToLife.get(location)); 
		}
	}

	/**
	 * Returns the lifeform at a given point
	 *
	 * @param location the point that's lifeform is being checked
	 * @return The lifeform at point location
	 */
	public Lifeform lifeGet(Point location) {
		synchronized (lock) {
			return locToLife.get(location);
		}
	}

	/**
	 * Returns the type of terrain at a given point
	 *
	 * @param location The point at which the terrain is desired
	 * @return the type of terrain at location
	 */
	public TERRAIN terrainGet(Point location) {
		synchronized (lock) {
			return locToTerrain.get(location);
		}
	}

	/**
	 * Returns the grass at that location
	 *
	 * @param location the point at which we are looking for grass
	 * @return The grass object at that point
	 */
	public Grass grassGet(Point location) {
		synchronized (lock) {
			return locToGrass.get(location);
		}
	}

	/**
	 * Moves the lifeform
	 */
	public void move(Point p, Lifeform l) {
		synchronized (lock) {
			locToLife.remove(l.getLocation());
			locToLife.put(p, l);
			l.setLocation(p);
		}
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

		synchronized (lock) {
			if (logButton.contains(e.getPoint())) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else {
				mouse = new Point(e.getPoint().x / 10 + screen.x, e.getPoint().y / 10 + screen.y);
				if (locToLife.containsKey(mouse)) {
					mouseOnLife = locToLife.get(mouse).getName();
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					mouseOnLife = "";
					setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				}
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
						e.getPoint().x / gridSize + screen.x,
						e.getPoint().y / gridSize + screen.y);
				selectedLifeform = null;
				enableAddButtons();

				Lifeform l = locToLife.get(selectedPoint);
				if (l != null) {
					selectedLifeform = l;
					selectedPoint = null;
					disableAddButtons();
				}
			}
		}
	}
	/**
	 * Point that is selected.
	 */
	Point selectedPoint;
	Lifeform selectedLifeform;

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

		if (Math.sin(2 * Math.PI / 1000 * x + 2 * Math.PI * numHours / 48) > 0) {
			ret = Weather.WEATHER.SUN;
		} else {
			ret = Weather.WEATHER.NIGHT;
		}
		synchronized (lock) {
			for (Weather w : activeWeather) {
				if (w.contains(x, y)) {
					if (ret == Weather.WEATHER.RAIN) {
						break;
					}
					ret = w.getType();
				}
			}
		}
		return ret;
	}

	/**
	 * Moves the clouds a bit
	 */
	public void pushWeather() {
		synchronized (lock) {
			for (Weather w : activeWeather) {
				w.translate(hourlyWind.x, hourlyWind.y);
			}
		}
	}
	/**
	 * Wind speed, affects pushWeather
	 */
	private HashMap<String, Image> sprites;
	private Point2D.Double hourlyWind = new Point2D.Double(-3, 3);
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
		synchronized (lock) {
			for (Map.Entry<Point, Lifeform> e : locToLife.entrySet()) {
				if (e.getValue() == l) {
					return e.getKey();
				}
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
	 * Spawns weather until activeWeather is MAX_WEATHER long Despawns weather
	 * that moves beyond the edge of the generated world
	 */
	public void manageWeather() {
		synchronized (lock) {
			for (Iterator<Weather> i = activeWeather.iterator();
					i.hasNext();) {
				Weather w = i.next();
				if (!w.intersects(outerNegX, outerNegY,
						outerPosX - outerNegX, outerPosY - outerNegY)) {
					i.remove();
				}
			}

			for (int i = 0; i < 10 - activeWeather.size(); ++i) {
				Point weatherCentre = new Point(
						rand.nextInt(outerPosX - outerNegX + 1) + outerNegX,
						rand.nextInt(outerPosY - outerNegY + 1) + outerNegY);


				Area a = new Area(new Ellipse2D.Double(
						weatherCentre.x - rand.nextInt(10),
						weatherCentre.y - rand.nextInt(10),
						20, 20));

				a.add(new Area(new Ellipse2D.Double(
						weatherCentre.x - rand.nextInt(10),
						weatherCentre.y - rand.nextInt(10),
						20, 20)));

				a.add(new Area(new Ellipse2D.Double(
						weatherCentre.x - rand.nextInt(10),
						weatherCentre.y - rand.nextInt(10),
						20, 20)));

				switch (rand.nextInt(2)) {
					case 0:
						activeWeather.add(new AirLock(a));
						break;
					case 1:
						activeWeather.add(new Drizzle(a));
						break;
				}

			}
		}
	}
	private final int MAX_WEATHER = 10;

}