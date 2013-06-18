package summative2013.lifeform;

import java.awt.Point;
import java.awt.Image;
import summative2013.SpriteAssigner;
import static summative2013.lifeform.Lifeform.summative;

/**
 * Bear class, describes a bear
 * @author Bobby Chiang
 */
public class Bear extends Animal {

    public static int bearCount = 0;

    /**
     * Default Constructor, creates a bear
     */
    public Bear() {
        super();
        preyList.add(Tree.class);
        preyList.add(Cattle.class);
        ++bearCount;
    }



    /**
     * Reproduces
     */
    @Override
    public void reproduce(Point p) {
            summative.addBear(p.x, p.y);
        
    }

    /**
     * Returns the image
     * @return Sprite for bear
     */
    @Override
    public Image getSprite() {
        return SpriteAssigner.getSpriteOf(this);
    }

    /**
     * Returns the name
     * @return the Name of the type of animal
     */
    @Override
    public String getName() {
        return "Bear";
    }

	@Override
	public void decreasePopulation() {
		--bearCount;
	}
}
