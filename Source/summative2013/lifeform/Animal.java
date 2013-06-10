package summative2013.lifeform;

import java.awt.Point;
import java.util.ArrayList;
import summative2013.memory.Memory;
import summative2013.phenomena.Disease;

/**
 * Parent class of all animal moving lifeform living creatures
 *
 * @author 322303413
 */
public class Animal extends Lifeform {

    /**
     * Stores hunger level, from 0-99
     */
    protected int hunger;

    /**
     * Creates the gender enum thing
     */
    public enum GENDER {

        MALE, FEMALE
    };
    /**
     * Stores gender
     */
    protected GENDER gender;
    /**
     * Store the friends happy no fighting membership club
     */
    protected ArrayList inGroup;
    /**
     * Stores the angry enemies murder hatred villain gang
     */
    protected ArrayList outGroup;
    /**
     * Stores the location the animal is currently moving to
     */
    protected Point destination;
    /**
     * Stores how messed up that animal losing its mind crazies asylum bar level
     */
    protected int depravity;
    /**
     * Stores past memories and regrets and nostalgic moments in the sun of
     * childhood glorious
     */
    protected ArrayList<Memory> knowledge;
    /**
     * Stores eating food delicious untermensch inferior prey animals
     */
    protected ArrayList<Lifeform> preyList;
    /**
     * Stores how diseased plague leprosy shun contagion SARS level of multiple
     */
    protected ArrayList<Disease> disease;

    public Animal() {
        hunger = 50;
        gender = GENDER.MALE;
        depravity = 0;
        knowledge = new ArrayList<Memory>();
        disease = new ArrayList<Disease>();
    }
    
    /**
     * Finds the closest prey
     */
    public void findFood(ArrayList<Lifeform> list) {
        ArrayList<Point> foodList = new ArrayList<Point>();
        food = null;
        for (Lifeform l : list) {
            for (Lifeform m : preyList) {
                if (l.getClass().equals(m.getClass())) {
                    foodList.add(l.location);
                }
            }
        }
        if (foodList.size() > 0) {
            food = foodList.get(0);
            for (Point p : foodList) {
                if (Math.abs(p.x - location.x) + Math.abs(p.y - location.y) < Math.abs(food.x - location.x) + Math.abs(food.y - location.y)) {
                    food = p;
                }
            }
        }
    }
    
    public void setDestination(){
        if(thirst>=hunger)
            destination = water;
        else
            destination = food;
    }
}
