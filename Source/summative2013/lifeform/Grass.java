package summative2013.lifeform;

import summative2013.SpriteAssigner;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import summative2013.Summative;
import static summative2013.lifeform.Lifeform.summative;

/**
 * Grass
 *
 * @author 322303413
 */
public class Grass extends Vegetable {

	/**
	 * Default constructor
	 */
	public Grass() {
		super();
		regenTime = 5;
		regenCounter = regenTime;
		capacity = 1;
		current = capacity;
		maxHealth = 1;
		reproTime = 5;
                sight =1;
	}

	/**
	 * Special grass edition of nearEmpty, checks only whether there's grass
	 * @return 
	 */
	@Override
	public Point nearEmpty() {
		Point temp;
		ArrayList<Point> available = new ArrayList<Point>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				temp = new Point(location.x + x, location.y + y);
				if (summative.grassGet(temp) == null && (summative.terrainGet(temp)) == Summative.TERRAIN.LAND) {
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
	 * Grass reproduction
	 */
	@Override
	public void reproduce() {
		if (nearEmpty() == null) {
		} else {
			summative.add(nearEmpty(), new Grass());
		}
	}

	public Image getSprite() {
		return SpriteAssigner.getSpriteOf(this);
	}
}
