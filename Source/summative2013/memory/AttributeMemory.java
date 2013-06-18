package summative2013.memory;

import summative2013.lifeform.Animal;

/**
 * Memory of a quality of an Animal
 *
 * @author quincy
 */
public class AttributeMemory extends Memory {

    /**
     * forget the memory
     */
    public void forget() {
        quality = null;
    }
    /**
     * Creates a new AttributeMemory
     * @param owner Which animal remembers
     * @param quality What they remember
     * @param value What they think of their memory
     */
    public AttributeMemory(Animal owner, String quality, int value) {
        this.owner = owner;
        this.quality = quality;
        this.value = value;

    }
    private Animal owner;
    private String quality;
    private int value;
    /**
     * try to forget something
     */
    public void corrupt() {
        value = corruptNumber(value);
        if (Math.random() < 0.01) {
            this.owner = null;
        }
        tryForget();
    }
    /**
     * 
     * @return if the memory is forgotten
     */
    public boolean isForgotten() {
        return quality == null;
    }

    /**
     * Probably doesn't affect the animal directly
     *
     * @param a the animal to be affected
     */
    public void affect(Animal a) {
    }
}
