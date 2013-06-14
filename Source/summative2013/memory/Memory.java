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
	/**
	 * Puts the memory into a forgotten state
	 */
	protected abstract void forget();
	
	/**
	 * Given a number, returns it increased or decreased by a value
	 * in [-CORRUPTION_DELTA, CORRUPTION_DELTA]
	 * @param n Value to corrupt
	 * @return n, increased or decreased by a value in [-CORRUPTION_DELTA, CORRUPTION_DELTA]
	 */
	protected int corruptNumber(long n) {
		return (int)( n + (Math.random() - 0.5)*CORRUPTION_DELTA);
	}
	
	/**
	 * Hinging on the value of oblivionChance, the memory may enter a 
	 * forgotten state
	 */
	protected void tryForget() {
		if (Math.random() < oblivionChance) {
			forget();
		}
	}
	
	/**
	 * Maximum corruption value
	 */
	protected final int CORRUPTION_DELTA = 2;
	
	/**
	 * Chance that the memory will enter a forgotten state
	 */
	protected final double oblivionChance = 0.01;
}
