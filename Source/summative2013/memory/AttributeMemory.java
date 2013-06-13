package summative2013.memory;
import summative2013.lifeform.Animal;
/**
 * Memory of a quality of an Animal
 * @author quincy
 */
public class AttributeMemory extends Memory {
	public void forget() {
		quality = null;
	}
	public AttributeMemory(Animal owner, String quality, int value) {
		this.owner = owner;
		this.quality = quality;
		this.value = value;
		
	}
	private Animal owner;
	private String quality;
	private int value;
	public void corrupt() {
		value = corruptNumber(value);
		if (Math.random() < 0.01) {
			this.owner = null;
		}
		tryForget();
	}
	public boolean isForgotten() {
		return quality==null;
	}
	
	/**
	 * Probably doesn't affect the animal directly
	 * @param a  
	 */
	public void affect(Animal a) {
		
	}
}
