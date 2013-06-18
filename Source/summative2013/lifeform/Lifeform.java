package summative2013.lifeform;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import summative2013.phenomena.Weather.WEATHER;
import summative2013.Summative;
import summative2013.Summative.TERRAIN;

/**
 * Default living class parent ancestor
 *
 * @author 322303413
 */
public abstract class Lifeform {

    /**
     * Store the panel
     */
    public static Summative summative;
    /**
     * Thirst, from 0-99
     */
    protected int thirst;
    /**
     * Are you diseased?
     */
    protected boolean diseased;
    /**
     * Stores the current weather
     */
    protected WEATHER weather;
    /**
     * Is it alive?
     */
    protected boolean alive;
    /**
     * Stores the image for display
     */
    protected Image icon;
    /**
     * The nearby organisms
     */
    protected ArrayList<Lifeform> nearbyLife;
    /**
     * Is it an animal
     */
    protected boolean mobile;
    /**
     * The closest water
     */
    protected Point water;
    /**
     * Stores vision range
     */
    protected int sight;

    /**
     * Default constructor, gives no thirst, (0,0) location
     */
    public Lifeform() {
        thirst = 50;
        alive = true;
        sight = 20;
        diseased = false;
    }

    /**
     * Acts, is dummy in Lifeform, implemented further down
     */
    public abstract void act(WEATHER w);

    /**
     * Suicide
     */
    public void suicide() {
		decreasePopulation();
        isDead = true;
    }

    /**
     * Returns mobility, i.e. whether is animal
     */
    public boolean getMobile() {
        return mobile;
    }

    /**
     * Sets the location of the lifeform
     *
     * @param p
     */
    public void setLocation(Point p) {
        summative.moveTo(this, p);
    }

    /**
     * Returns the location
     */
    public Point getLocation() {
        return summative.getLocation(this);
    }

    /**
     * Finds all empty nearby spaces
     *
     * @return a point that is adjacent and empty
     */
    public Point nearEmpty() {
        Point temp;
        ArrayList<Point> available = new ArrayList<Point>();
        final Point location = summative.getLocation(this);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                temp = new Point(location.x + x, location.y + y);
                if (summative.emptyAt(temp) && (summative.terrainGet(temp)) == TERRAIN.LAND) {
                    available.add(temp);
                }
            }
        }
        if (available.size() > 0) {
            return available.get((int) (Math.random() * available.size()));
        } else {
            return null;
        }
    }

    /**
     * Refreshes knowledge of nearby terrain
     */
    public void findWater() {
        ArrayList<Point> waterList = new ArrayList<>();
        water = null;
        final Point location = summative.getLocation(this);

        for (int x = -sight; x <= sight; x++) {
            for (int y = -sight; y <= sight; y++) {
                if (Math.abs(x) + Math.abs(y) <= sight) {
                    if (summative.terrainGet(new Point(location.x + x, location.y + y)) == Summative.TERRAIN.SEA) {
                        waterList.add(new Point(location.x + x, location.y + y));
                    }
                }
            }
        }
        if (waterList.size() > 0) {
            water = waterList.get(0);
            for (Point p : waterList) {
                if (Math.abs(p.x - location.x) + Math.abs(p.y - location.y) < Math.abs(water.x - location.x) + Math.abs(water.y - location.y)) {
                    water = p;
                }
            }
        }
    }

    /**
     * Refreshes to store all the nearby lifeforms
     */
    public void findNearbyLife() {
        nearbyLife = new ArrayList<Lifeform>();
        final Point location = summative.getLocation(this);
        for (int x = -sight; x <= sight; x++) {
            for (int y = -sight; y <= sight; y++) {
                if (Math.abs(x) + Math.abs(y) <= sight) {
                    if (summative.lifeGet(new Point(location.x + x, location.y + y)) != null) {
                        nearbyLife.add(summative.lifeGet(new Point(location.x + x, location.y + y)));
                    } else if (summative.grassGet(new Point(location.x + x, location.y + y)) != null) {
                        nearbyLife.add(summative.grassGet(new Point(location.x + x, location.y + y)));
                    }
                }
            }
        }
    }

    /**
     * Produces a new lifeform, overridden in most of the classes
     */
    public abstract void reproduce(Point p);

    /**
     * Returns the lifeform's sprite. Should be overriden in child classes with
     * the body:
     *
     * return SpriteAssigner.getSpriteOf(this);
     */
    public abstract Image getSprite();

    /**
     * Diseases itself
     */
    public void disease() {
        diseased = true;
    }

    public String toString() {
        return getName()
                + "\nHealth: " + ((diseased) ? "Diseased" : "Healthy")
                + "\nThirst: " + thirst + "%";
    }

	/**
	 * Gets the name of the lifeform
	 * @return  The name of the lifeform
	 */
    public abstract String getName();
	
	/**
	 * Reduces the internal population variable of this animal
	 */
	protected abstract void decreasePopulation();
	
	/**
	 * Marks that this lifeform is ready for deletion
	 */
	private boolean isDead = false;
	
	/**
	 * Checks if this lifeform is ready for deletion
	 * @return If this lifeform has commited suicide, this is true
	 */
	public boolean isDead() {
		return isDead;
	}
}
