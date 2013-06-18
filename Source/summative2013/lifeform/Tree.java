package summative2013.lifeform;

import java.awt.Point;
import summative2013.SpriteAssigner;
import java.awt.Image;
import static summative2013.lifeform.Lifeform.summative;

/**
 * Tree
 *
 * @author 322303413
 */
public class Tree extends Vegetable {

    /**
     * Default constructor
     */
    public Tree() {
        super();
        regenTime = 20;
        regenCounter = regenTime;
        capacity = 5;
        current = capacity;
        maxHealth = 100;
        reproTime = 50;
        sight = 1;
        ++treeCount;
    }
    /**
     * Makes babies
     */
    @Override
    public void reproduce(Point p) {
            summative.addTree(p.x, p.y);
    }
    /**
     * 
     * @return the sprite of a Tree
     */
    @Override
    public Image getSprite() {
        return SpriteAssigner.getSpriteOf(this);
    }
    /**
     * 
     * @return the name of this lifeform
     */
    @Override
    public String getName() {
        return "Tree";
    }
    public static int treeCount = 0;


	@Override
	public void decreasePopulation() {
		--treeCount;
	}
	
	protected int getRefractory() {
		return 100;
	}
}
