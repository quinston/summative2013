package summative2013.lifeform;

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
     * Kills itself
     */
    @Override
    public void suicide() {
        --bearCount;
        super.suicide();
    }

    /**
     * Reproduces
     */
    @Override
    public void reproduce() {
        if (nearEmpty() == null) {
            hunger = hunger - 30;
        } else {
            summative.addBear(nearEmpty().x, nearEmpty().y);
        }
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
}
