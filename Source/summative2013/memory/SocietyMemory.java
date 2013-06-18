package summative2013.memory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import summative2013.lifeform.Animal;
import java.util.Random;

/**
 * A memory that describes relationships with other Animals
 *
 * @author quincy
 */
public class SocietyMemory extends Memory {

    /**
     *Creates a new SocietyMemory of an animal and its score
     * @param object the animal remembered
     * @param score the score of the animal
     */
    public SocietyMemory(Animal object, int score) {
        this.object = object;
        this.score = score;

    }
    /**
     * Target of this relationship
     */
    private Animal object;
    /**
     * Describes amity with the target Animal. Positive is better, negative is
     * worst, zero is neutral.
     */
    private int score;

    /**
     * Updates the in-group and out-group lists of the target Animal.
     *
     * @param a Animal to affect
     *//*
    public void affect(Animal a) {
        LinkedList<Animal> inGroup = a.getInGroup(),
                outGroup = a.getOutGroup();

        if (score > 0) {
            if (inGroup.indexOf(object) == -1) {
                //Collections.swap(inGroup, inGroup.indexOf(object), Math.max(inGroup.indexOf(object) - score, 0));
                inGroup.add(object);
            }
        } else if (score < 0) {
            if (outGroup.indexOf(object) == -1) {
                outGroup.add(object);
            }
        }




        Collections.swap(outGroup, inGroup.indexOf(object),
                Math.max(outGroup.indexOf(object) - score, 0));
    }*/

    /**
     * Increases or decreases the score by a random amount in
     * [-CORRUPTION_DELTA, CORRUPTION_DELTA]
     */
    @Override
    public void corrupt() {
        score = corruptNumber(score);
        tryForget();
    }
    /**
     * forgets the memory
     */
    protected void forget() {
        object = null;
    }
    /**
     * check if the memory is forgotten
     * @return if the memory is forgotten
     */
    public boolean isForgotten() {
        return object == null;
    }

    @Override
    public void affect(Animal a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
