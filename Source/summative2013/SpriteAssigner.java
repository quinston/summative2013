package summative2013;

import java.awt.Image;
import java.util.HashMap;
import lifeform.Bat;
import summative2013.lifeform.Bear;
import summative2013.lifeform.Bunny;
import summative2013.lifeform.Cattle;
import summative2013.lifeform.Grass;
import summative2013.lifeform.Tree;
import summative2013.lifeform.Vegetable;

/**
 * Lifeforms will call this object's getSpriteOf method on themselves in order
 * to receive what sprites with which they are to be drawn, which in turn will
 * be returned to the drawing loop in Summative
 *
 * @author quincy
 */
public class SpriteAssigner {

	public static HashMap<String, Image> sprites;

	public static Image getSpriteOf(Bear l) {
		return sprites.get("bear");
	}

	public static Image getSpriteOf(Bunny l) {
		return sprites.get("bunny");
	}

	public static Image getSpriteOf(Cattle l) {
		return sprites.get("cattle");
	}

	public static Image getSpriteOf(Grass l) {
		return sprites.get("grass");
	}

	public static Image getSpriteOf(Tree l) {
		return sprites.get("tree");
	}

	public static Image getSpriteOf(Vegetable l) {
		return sprites.get("vegetable");
	}
	
	public static Image getSpriteOf(Bat l) {
		return sprites.get("bat");
	}
}
