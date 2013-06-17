package summative2013.lifeform;

import java.awt.Image;
import summative2013.SpriteAssigner;
import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Bat extends Animal {

    public Bat() {
        super();
        preyList.add(new Tree());
        nocturnal = true;
    }

    @Override
    public void reproduce() {
        if (nearEmpty() == null) {
            hunger = hunger - 30;
        } else {
            summative.addBat(nearEmpty().x, nearEmpty().y);
        }
    }

    public Image getSprite() {
        return SpriteAssigner.getSpriteOf(this);
    }

	@Override
	public String getName() {
		return "Bat";
	}
	
}
