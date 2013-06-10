package summative2013.phenomena;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * Of the rain and the lightning weather sky torrential climate
 *
 * @author 322303413
 */
public class Weather {

    protected Area area;
    protected WEATHER type;

    /**
     * Creates a weather enum thing
     */
    public enum WEATHER {

        RAIN, SUN, CLOUD
    };

    public Weather(int x, int y, int size) {
        area = new Area(new Ellipse2D.Double(x, y, size, size));
    }

    public Area getArea() {
        return area;
    }

    public WEATHER getType() {
        return type;
    }
}
