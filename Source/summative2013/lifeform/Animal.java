package summative2013.lifeform;

import java.awt.Point;
import java.util.ArrayList;
import summative2013.Summative;
import static summative2013.lifeform.Lifeform.summative;
import summative2013.memory.Memory;
import summative2013.phenomena.Disease;
import summative2013.phenomena.Weather.WEATHER;

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
     * Some kind of direction variable
     */
    public enum DIRECTION {

        NORTH, SOUTH, EAST, WEST, CENTER
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
     * Refreshes to store all the nearby lifeforms
     */
    public void findNearbyLife() {
        nearbyLife = new ArrayList<Lifeform>();
        for (int x = -sight; x <= sight; x++) {
            for (int y = -sight; y <= sight; y++) {
                if (Math.abs(x) + Math.abs(y) <= sight) {
                    if (summative.lifeGet(new Point(location.x + x, location.y + y)) != null) {
                        nearbyLife.add(summative.lifeGet(new Point(location.x + x, location.y + y)));
                    }
                }
            }
        }
    }

    /**
     * Refreshes nearby terrain
     */
    public void findWater() {
        ArrayList<Point> waterList = new ArrayList<Point>();
        water = null;
        for (int x = -sight; x <= sight; x++) {
            for (int y = -sight; y <= sight; y++) {
                if (Math.abs(x) + Math.abs(y) <= sight) {
                    if (summative.terrainGet(new Point(location.x + x, location.y + y)) == Summative.TERRAIN.SEA) {
                        waterList.add(new Point(location.x + x, location.y + y));
                    }
                }
            }
        }
        if (waterList.size() > 0) {
            water = waterList.get(0);
            for (Point p : waterList) {
                if (Math.abs(p.x - location.x) + Math.abs(p.y - location.y) < Math.abs(water.x - location.x) + Math.abs(water.y - location.y)) {
                    water = p;
                }
            }
        }
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

    public void setDestination() {
        if (thirst >= hunger) {
            destination = water;
        } else {
            destination = food;
        }
    }

    public DIRECTION getDirection(Point p) {
        if (p.x == location.x && p.y == location.y) {
            return DIRECTION.CENTER;
        } else if (Math.abs(p.x - location.x) > Math.abs(p.y - location.y)) {
            if (p.x > location.x) {
                return DIRECTION.EAST;
            } else {
                return DIRECTION.WEST;
            }
        } else if (p.y > location.y) {
            return DIRECTION.NORTH;
        } else {
            return DIRECTION.SOUTH;
        }
    }

    @Override
    public void act(WEATHER Weather) {
        if (getDirection(destination) == DIRECTION.NORTH) {
            location.y = location.y + 1;
        } else if (getDirection(destination) == DIRECTION.SOUTH) {
            location.y = location.y - 1;
        } else if (getDirection(destination) == DIRECTION.WEST) {
            location.x = location.x - 1;
        } else if (getDirection(destination) == DIRECTION.EAST) {
            location.x = location.x + 1;
        } else {
        }
        
    }
}
