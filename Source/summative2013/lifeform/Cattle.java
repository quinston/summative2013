package summative2013.lifeform;

import summative2013.SpriteAssigner;
import java.awt.Image;
import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Cattle extends Animal {

    public static int cattleCount = 0;

    public Cattle() {
        super();
        preyList.add(Grass.class);
        ++cattleCount;
    }

    public void suicide() {
        --cattleCount;
        super.suicide();
    }

    @Override
    public void reproduce() {
        if (nearEmpty() == null) {
            hunger = hunger - 30;
        } else {
            summative.addCattle(nearEmpty().x, nearEmpty().y);
        }
    }

    @Override
    public Image getSprite() {
        return SpriteAssigner.getSpriteOf(this);
    }

    @Override
    public String getName() {
        return "Cattle";
    }
}
