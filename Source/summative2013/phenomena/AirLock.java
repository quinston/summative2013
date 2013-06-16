package summative2013.phenomena;

import java.awt.geom.Area;

/**
 *
 * @author 322303413
 */
public class AirLock extends Weather {

    public AirLock(int x, int y, int size) {
        super(x, y, size);
		type = WEATHER.CLOUD;
    }
	
	public AirLock(Area a){
		super(a);
		type = Weather.WEATHER.CLOUD;
	}
}