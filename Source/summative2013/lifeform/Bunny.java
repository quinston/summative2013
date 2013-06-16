package summative2013.lifeform;

import java.awt.Image;
import summative2013.SpriteAssigner;
import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Bunny extends Animal {

	public Bunny() {
		super();
		preyList.add(new Grass());
	}

	@Override
	public void reproduce() {
		if (nearEmpty() == null) {
			hunger = hunger - 30;
		} else {
			summative.add(nearEmpty(), new Bunny());
		}
	}

	public Image getSprite() {
		return SpriteAssigner.getSpriteOf(this);
	}
}
