package summative2013.phenomena;

import java.awt.geom.Area;

/**
 *
 * @author 322303413
 */
public class Drizzle extends Weather {

    public Drizzle(int x, int y, int size) {
        super(x, y, size);
    }
	
	public Drizzle(Area area){
		super(area);

	}
	
	protected WEATHER type = WEATHER.RAIN;
}
