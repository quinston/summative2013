package summative2013.phenomena;

import java.awt.geom.Area;

/**
 *
 * @author 322303413
 */
public class Drought extends Weather {

    public Drought(int x, int y, int size) {
        super(x, y, size);
		type = WEATHER.SUN;
    }
	
	public Drought(Area a) {
		super(a);
		type = WEATHER.SUN;
	}
	
}
