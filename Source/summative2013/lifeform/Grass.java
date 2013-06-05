package summative2013.lifeform;


/**
 * Grass
 *
 * @author 322303413
 */
public class Grass extends Vegetable {

    /**
     * Default constructor
     */
    public Grass() {
        super();
        regenTime = 5;
        regenCounter = regenTime;
        capacity = 1;
        current = capacity;
    }
}
