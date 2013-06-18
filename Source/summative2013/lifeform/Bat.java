package summative2013.lifeform;

import java.awt.Point;
import java.awt.Image;
import summative2013.SpriteAssigner;
import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Bat extends Animal {

    public static int batCount = 0;
    /**
     * Default constructor
     */
    public Bat() {
        super();
        preyList.add(Tree.class);
        nocturnal = true;
        ++batCount;
    }

    /**
     * baby furry fliers
     */
    @Override
    public void reproduce(Point p) {
            summative.addBat(p.x, p.y);
       
    }
    /**
     * 
     * @return the sprite of a bat
     */
    public Image getSprite() {
        return SpriteAssigner.getSpriteOf(this);
    }
    /**
     * 
     * @return the name of this species
     */
    @Override
    public String getName() {
        return "Bat";
    }

	@Override
	public void decreasePopulation() {
		--batCount;
	}
	
	
}
