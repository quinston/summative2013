package summative2013.lifeform;

import java.awt.Point;
import summative2013.phenomena.Weather.WEATHER;
import summative2013.Summative;

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
    }
}
