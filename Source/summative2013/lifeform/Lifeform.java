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
}
