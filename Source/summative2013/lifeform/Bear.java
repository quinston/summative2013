package summative2013.lifeform;

import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Bear extends Animal {

    public Bear() {
        preyList.add(new Tree());
        preyList.add(new Cattle());
    }
    
    @Override
    public void reproduce() {
        if (nearEmpty() == null) {
            hunger = hunger - 30;
        } else {
            summative.add(nearEmpty(), new Bear());
        }
    }
}
