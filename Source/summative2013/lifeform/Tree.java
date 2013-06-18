package summative2013.lifeform;

import summative2013.SpriteAssigner;
import java.awt.Image;
import static summative2013.lifeform.Lifeform.summative;

/**
 * Tree
 *
 * @author 322303413
 */
public class Tree extends Vegetable {

    /**
     * Default constructor
     */
    public Tree() {
        super();
        regenTime = 20;
        regenCounter = regenTime;
        capacity = 5;
        current = capacity;
        maxHealth = 100;
        reproTime = 50;
        sight = 1;
        ++treeCount;
    }

    @Override
    public void reproduce() {
        if (nearEmpty() == null) {
        } else {
            summative.addTree(nearEmpty().x, nearEmpty().y);

            reproTime = 960;
        }
    }

    @Override
    public Image getSprite() {
        return SpriteAssigner.getSpriteOf(this);
    }

    @Override
    public String getName() {
        return "Tree";
    }
    public static int treeCount = 0;

    @Override
    public void suicide() {
        --treeCount;
        super.suicide();
    }
}
