package summative2013.lifeform;

import static summative2013.lifeform.Lifeform.summative;

/**
 *
 * @author 322303413
 */
public class Cattle extends Animal {

    public Cattle() {
        preyList.add(new Grass());
    }

    @Override
    public void reproduce() {
        if (nearEmpty() == null) {
            hunger = hunger - 30;
        } else {
            summative.add(nearEmpty(), new Cattle());
        }
    }
}
