package summative2013.lifeform;

import java.awt.Point;
import summative2013.SpriteAssigner;
import java.awt.Image;
import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Cattle extends Animal {

    public static int cattleCount = 0;
    /**
     * default constructor
     */
    public Cattle() {
        super();
        preyList.add(Grass.class);
        ++cattleCount;
    }

    /**
     * cow babies!
     */
    @Override
    public void reproduce(Point p) {
            summative.addCattle(p.x, p.y);
        
    }
    /**
     * 
     * @return the sprite for a cow
     */
    @Override
    public Image getSprite() {
        return SpriteAssigner.getSpriteOf(this);
    }
    /**
     * 
     * @return the name of this species of lifeform
     */
    @Override
    public String getName() {
        return "Cattle";
    }

	@Override
	public void decreasePopulation() {
		--cattleCount;
	}
}
