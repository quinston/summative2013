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
public class Lifeform {

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
     * The nearby organisms
     */
    protected ArrayList<Lifeform> nearbyLife;
    /**
     * The closest water
     */
    protected Point water;
    /**
     * Stores vision range
     */
    protected int sight;
    /**
     * Stores nearest prey
     */
    protected Point food;

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
    public void act(WEATHER weather) {
    }

    /**
     * Suicide
     */
    public void suicide() {
        summative.assistedSuicide(location);
    }

    /**
     * Refreshes to store all the nearby lifeforms
     */
    public void findNearbyLife() {
        nearbyLife = new ArrayList<Lifeform>();
        for (int x = -sight; x <= sight; x++) {
            for (int y = -sight; y <= sight; y++) {
                if (Math.abs(x) + Math.abs(y) <= sight) {
                    if (summative.lifeGet(new Point(location.x + x, location.y + y)) != null) {
                        nearbyLife.add(summative.lifeGet(new Point(location.x + x, location.y + y)));
                    }
                }
            }
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
                    if (summative.terrainGet(new Point(location.x + x, location.y + y)) == TERRAIN.SEA) {
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
}
