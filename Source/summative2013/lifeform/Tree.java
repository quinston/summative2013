package summative2013.lifeform;

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
        maxHealth=100;
        reproTime=50;
    }
    
    @Override
    public void reproduce() {
        if (nearEmpty() == null) {
        } else {
            summative.add(nearEmpty(), new Tree());
        }
    }
	
	public Image getSprite() {
		return SpriteAssigner.getSpriteOf(this);
	}
}
