package summative2013.lifeform;

import java.awt.Image;
import summative2013.SpriteAssigner;
import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Bunny extends Animal {
	public static int bunnyCount = 0;
	public Bunny() {
		super();
		preyList.add(new Grass());
		++bunnyCount;
	}
	
	public void suicide() {
		--bunnyCount;
		super.suicide();
	}

	@Override
	public void reproduce() {
		if (nearEmpty() == null) {
			hunger = hunger - 30;
		} else {
			summative.addBunny(nearEmpty().x,nearEmpty().y);
		}
	}

	public Image getSprite() {
		return SpriteAssigner.getSpriteOf(this);
	}

	@Override
	public String getName() {
		return "Bunny";
	}
}
