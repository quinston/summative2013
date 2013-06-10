package summative2013.lifeform;

import java.awt.Point;
<<<<<<< HEAD
=======
import summative2013.phenomena.Weather.WEATHER;
>>>>>>> 74c1ecf928fe20a8934e78820227d71802031979
import summative2013.Summative;

/**
 * Default living class parent ancestor
 *
 * @author 322303413
 */
<<<<<<< HEAD
public abstract class Lifeform {
=======
public class Lifeform {

>>>>>>> 74c1ecf928fe20a8934e78820227d71802031979
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
<<<<<<< HEAD
    public abstract void act();
    
=======
    public void act(WEATHER weather) {
    }

>>>>>>> 74c1ecf928fe20a8934e78820227d71802031979
    /**
     * Suicide
     */
    public void suicide() {
    }
}
