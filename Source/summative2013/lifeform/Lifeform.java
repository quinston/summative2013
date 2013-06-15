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
     * @param p 
     */
    public void setLocation(Point p) {
        location = p;
    }

    /**
     * Finds all empty nearby spaces
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
     * Produces a new lifeform, overridden in most of the classes
     */
    public void reproduce() {
    }
    
    /**
     * Draws the lifeform using its specified icon
     */
    public void draw(){}
}
