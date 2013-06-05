package summative2013.lifeform;


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
    }
}
