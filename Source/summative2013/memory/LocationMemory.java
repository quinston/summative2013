package summative2013.memory;

/**
 * A memory that describes the knowledge of the existence of something
 * at some location, and the location's terrain
 * @author quincy
 */

import java.awt.Point;
import summative2013.Summative;
import summative2013.lifeform.Lifeform;
import summative2013.lifeform.Animal;

public class LocationMemory extends Memory {
	/**
	 * Location remembered
	 */
	private Point location;
	/**
	 * Object at location. May be null.
	 */
	private Lifeform object;
	/**  
	 * Terrain at location
	 */
	private Summative.Terrain terrain;
	/**
	 * Maximum corruption value
	 */
	private final int CORRUPTION_DELTA = 3;
	/**
	 * Increases or decreases both coordinates by a random amount in 
	 * [-CORRUPTION_DELTA, CORRUPTION_DELTA]
	 */
	public void corrupt() {
		location.x += (Math.random() - 0.5) * CORRUPTION_DELTA;
		location.y += (Math.random() - 0.5) * CORRUPTION_DELTA;
		
		if (Math.random() < 0.01) {
			
		}
	}
	/**
	 * ?
	 * @param l  The animal to affect, probably the owner of this memory
	 */
	public void affect(Animal l) {
		
	}
	
	/**
	 * 
	 * @return If the terrain of the location being remembered is null
	 */
	public boolean isForgotten() {
		return terrain == null;
	}
}
