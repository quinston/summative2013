package summative2013.lifeform;

/**
 * Knowledge of an attribute of an object
 * @author quincy
 */
import summative2013.memory.Memory;
public class AttributeMemory extends Memory {
	private long value;
	private String identifier;
	/**
         * maybe forget?
         */
	public void corrupt() {
		value += (Math.random() - 0.5) * 2 * CORRUPTION_DELTA;
		tryForget();
	}
	/**
         * 
         * @return still there?
         */
	public boolean isForgotten() {
		return identifier == null;
	}
	/**
         * no more memory
         */
	protected void forget() {
		identifier=null;
	}
	/**
         * Affect an animal
         * @param a Animal to be affected
         */
	public void affect(Animal a) {
		
	}
}
