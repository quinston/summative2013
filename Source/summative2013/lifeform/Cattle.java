package summative2013.lifeform;

import summative2013.SpriteAssigner;
import java.awt.Image;
import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Cattle extends Animal {

	public Cattle() {
		super();
		preyList.add(new Grass());
	}

	@Override
	public void reproduce() {
		if (nearEmpty() == null) {
			hunger = hunger - 30;
		} else {
			summative.add(nearEmpty(), new Cattle());
		}
	}

	public Image getSprite() {
		return SpriteAssigner.getSpriteOf(this);
	}
}
