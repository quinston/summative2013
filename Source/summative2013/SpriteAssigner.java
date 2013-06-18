package summative2013;

import java.awt.Image;
import java.util.HashMap;
import summative2013.lifeform.Bat;
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

    /**
     * The hashmap relating a string to an image
     */
    public static HashMap<String, Image> sprites;

    /**
     * returns the sprite of a Bear object
     *
     * @param l a Bear object that wants its sprite
     * @return the sprite for a bear
     */
    public static Image getSpriteOf(Bear l) {
        return sprites.get("bear");
    }

    /**
     * returns the sprite of a Bunny object
     *
     * @param l a Bunny object that wants its sprite
     * @return the sprite for a Bunny
     */
    public static Image getSpriteOf(Bunny l) {
        return sprites.get("bunny");
    }

    /**
     * returns the sprite of a Cattle object
     *
     * @param l a Cattle object that wants its sprite
     * @return the sprite for a Cattle
     */
    public static Image getSpriteOf(Cattle l) {
        return sprites.get("cattle");
    }

    /**
     * returns the sprite of a Grass object
     *
     * @param l a Grass object that wants its sprite
     * @return the sprite of Grass
     */
    public static Image getSpriteOf(Grass l) {
        return sprites.get("grass");
    }

    /**
     * returns the sprite of a Tree object
     *
     * @param l a Tree object that wants its sprite
     * @return the sprite of a Tree
     */
    public static Image getSpriteOf(Tree l) {
        return sprites.get("tree");
    }

    /**
     * returns the sprite of a Vegetable object
     *
     * @param l a Vegetable object that wants its sprite
     * @return the sprite of a Vegetable
     */
    public static Image getSpriteOf(Vegetable l) {
        return sprites.get("vegetable");
    }

    /**
     * returns the sprite of a Bat object
     *
     * @param l a Bat that wants its sprite
     * @return the sprite of a Bat
     */
    public static Image getSpriteOf(Bat l) {
        return sprites.get("bat");
    }
}
