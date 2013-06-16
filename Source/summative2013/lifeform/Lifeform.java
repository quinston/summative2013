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
     * Stores position on grid
     */
    protected Point location;
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
        location = new Point(0, 0);
        alive = true;
    }

    /**
     * Acts, is dummy in Lifeform, implemented further down
     */
    public abstract void act(WEATHER w);

    /**
     * Suicide
     */
    public void suicide() {
        summative.assistedSuicide(location);
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
        location = p;
    }

    /**
     * Returns the location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Finds all empty nearby spaces
     *
     * @return
     */
    public Point nearEmpty() {
        Point temp;
        ArrayList<Point> available = new ArrayList<Point>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                temp = new Point(location.x + x, location.y + y);
                if (summative.lifeGet(temp) == null && (summative.terrainGet(temp)) == TERRAIN.LAND) {
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
     * Refreshes nearby terrain
     */
    public void findWater() {
        ArrayList<Point> waterList = new ArrayList<Point>();
        water = null;
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
     * Produces a new lifeform, overridden in most of the classes
     */
    public void reproduce() {
    }

    /**
     * Returns the lifeform's sprite. Should be overriden in child classes with
     * the body:
     *
     * return SpriteAssigner.getSpriteOf(this);
     */
    public abstract Image getSprite();
}
