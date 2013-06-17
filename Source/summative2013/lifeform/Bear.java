package summative2013.lifeform;

import java.awt.Image;
import summative2013.SpriteAssigner;
import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Bear extends Animal {

    public Bear() {
		super();
        preyList.add(new Tree());
        preyList.add(new Cattle());
    }
    
    @Override
    public void reproduce() {
        if (nearEmpty() == null) {
            hunger = hunger - 30;
        } else {
            summative.addBear(nearEmpty().x,nearEmpty().y);
        }
    }
	
	public Image getSprite() {
		return SpriteAssigner.getSpriteOf(this);
	}

	@Override
	public String getName() {
		return "Bear";
	}
	
	
}
