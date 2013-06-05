package summative2013.lifeform;


import java.awt.Point;
import java.util.ArrayList;

/**
 * Parent class of all animal moving lifeform living creatures
 *
 * @author 322303413
 */
public class Animal extends Lifeform {

    /**
     * Stores hunger level, from 0-99
     */
    private int hunger;

    /**
     * Creates the gender enum thing
     */
    public enum Gender {

        MALE, FEMALE
    };
    /**
     * Stores gender
     */
    private Gender gender;
    /**
     * Store the friends happy no fighting membership club
     */
    private ArrayList inGroup;
    /**
     * Stores the angry enemies murder hatred villain gang
     */
    private ArrayList outGroup;
    /**
     * Stores the location the animal is currently moving to
     */
    private Point destination;
    /**
     * Stores how messed up that animal losing its mind crazies asylum bar level
     */
    private int depravity;
    /**
     * Stores past memories and regrets and nostalgic moments in the sun of
     * childhood glorious
     */
    private ArrayList<Memory> knowledge;
    /**
     * Stores eating food delicious untermensch inferior prey animals
     */
    private ArrayList<Animal> preyList;
    /**
     * Stores how diseased plague leprosy shun contagion SARS level of multiple
     */
    private ArrayList<Disease> disease;
}
