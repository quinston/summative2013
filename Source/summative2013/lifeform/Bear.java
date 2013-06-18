package summative2013.lifeform;

import java.awt.Image;
import summative2013.SpriteAssigner;
import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Bear extends Animal {

    public static int bearCount = 0;

    /**
     * Constructor
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
     * @return 
     */
    @Override
    public Image getSprite() {
        return SpriteAssigner.getSpriteOf(this);
    }

    /**
     * Returns the name
     * @return 
     */
    @Override
    public String getName() {
        return "Bear";
    }
}
