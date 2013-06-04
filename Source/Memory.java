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
}
