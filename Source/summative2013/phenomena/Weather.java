package summative2013.phenomena;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 * Of the rain and the lightning weather sky torrential climate
 *
 * @author 322303413
 */
public abstract class Weather {

    protected Area area;
    protected WEATHER type;	

    /**
     * Creates a weather enum thing
     */
    public enum WEATHER {

        RAIN, SUN, CLOUD, NIGHT
    };

    public Weather(int x, int y, int size) {
        area = new Area(new Ellipse2D.Double(x, y, size, size));
    }
	
	public Weather(Area area) {
		this.area = area;
	}
/*
    public Area getArea() {
        return new Area(area);
    }
*/
    public WEATHER getType() {
        return type;
    }
	
	public boolean contains(int x, int y) {
		return area.contains(x,y);
	}
	
	public boolean contains(Point p) {
		return area.contains(p);
	}
	
	public boolean intersects(int x, int y, int w, int h) {
		return area.intersects(x,y,w,h);
	}
	
	public void translate(double x, double y) {
		area.transform(AffineTransform.getTranslateInstance(x,y));
	}
	
	public Point getCentre() {
		Rectangle r = area.getBounds();
		return new Point((int) r.getCenterX(),
				(int) r.getCenterY());
	}
}
