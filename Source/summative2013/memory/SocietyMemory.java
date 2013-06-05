package summative2013.memory;

import summative2013.lifeform.Animal;
import java.util.Random;
/**
 * A memory that describes relationships with other Animals
 * @author quincy
 */
public class SocietyMemory extends Memory {
	public SocietyMemory(Animal object, int score) {
		this.object = object;
		this.score = score;
	}
	/**
	 * Target of this relationship
	 */
	private Animal object;
	/**
	 * Describes amity with the target Animal. Positive is better, negative
	 * is worst, zero is neutral.
	 */
	private int score;
	/**
	 * Updates the in-group and out-group lists of the target Animal.
	 * Precondition: l is an Animal object
	 * @param l Animal to affect
	 */
	public void affect(Animal l) {
		
	}
	/**
	 * Increases or decreases the score by a random amount in 
	 * [-CORRUPTION_DELTA, CORRUPTION_DELTA]
	 */
	@Override
	public void corrupt() {
		score += ((Math.random() - 0.5) * CORRUPTION_DELTA);
	}
	
	public final int CORRUPTION_DELTA = 3;
}
