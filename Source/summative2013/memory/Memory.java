package summative2013.memory;
import summative2013.lifeform.Animal;

/**
 * A base class for everything an animal will know
 * @author quincy
 */
public abstract class Memory {
	/**
	 * Causes the memory to corrupt somehow
	 */
	public abstract void corrupt();
	/**
	 * Affects the characteristics of the target animal by
	 * the values in this Memory
	 * @param a Target animal
	 */
	public abstract void affect(Animal a);
	/**
	 * Checks if this memory can be deleted since it is useless
	 * @return Whether this memory is ready for deletion
	 */
	public abstract boolean isForgotten();
}
