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
        /**
         * default constructor
         */
	public Bunny() {
		super();
		preyList.add(Grass.class);
		++bunnyCount;
	}

        /**
         * make babies like rabbits!
         */
	@Override
	public void reproduce() {
		if (nearEmpty() == null) {
			hunger = hunger - 30;
		} else {
			summative.addBunny(nearEmpty().x,nearEmpty().y);
		}
	}
        /**
         * 
         * @return the sprite of a Bunny
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
		return "Bunny";
	}

	@Override
	public void decreasePopulation() {
		--bunnyCount;
	}
}
