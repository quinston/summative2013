package summative2013.lifeform;

/**
 * Knowledge of an attribute of an object
 * @author quincy
 */
import summative2013.memory.Memory;
public class AttributeMemory extends Memory {
	private long value;
	private String identifier;
	
	public void corrupt() {
		value += (Math.random() - 0.5) * 2 * CORRUPTION_DELTA;
		tryForget();
	}
	
	public boolean isForgotten() {
		return identifier == null;
	}
	
	protected void forget() {
		identifier=null;
	}
	
	public void affect(Animal a) {
		
	}
}
