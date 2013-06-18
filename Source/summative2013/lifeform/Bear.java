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

    public Bear() {
        super();
        preyList.add(Tree.class);
        preyList.add(Cattle.class);
        ++bearCount;
    }

    @Override
    public void suicide() {
        --bearCount;
        super.suicide();
    }

    @Override
    public void reproduce() {
        if (nearEmpty() == null) {
            hunger = hunger - 30;
        } else {
            summative.addBear(nearEmpty().x, nearEmpty().y);
        }
    }

    @Override
    public Image getSprite() {
        return SpriteAssigner.getSpriteOf(this);
    }

    @Override
    public String getName() {
        return "Bear";
    }
}
