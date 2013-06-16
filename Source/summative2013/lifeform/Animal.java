package summative2013.lifeform;

import java.awt.Point;
import java.util.ArrayList;
import summative2013.Summative;
import summative2013.Summative.TERRAIN;
import static summative2013.lifeform.Lifeform.summative;
import summative2013.memory.Memory;
import summative2013.phenomena.Disease;
import summative2013.phenomena.Weather.WEATHER;
import java.util.LinkedList;

/**
 * Parent class of all animal moving lifeform living creatures
 *
 * @author 322303413
 */
public abstract class Animal extends Lifeform {

    /**
     * Stores hunger level, from 0-99
     */
    protected int hunger;

    /**
     * @return the inGroup
     */
    public LinkedList<Animal> getInGroup() {
        return inGroup;
    }

    /**
     * @return the outGroup
     */
    public LinkedList<Animal> getOutGroup() {
        return outGroup;
    }

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
    protected LinkedList inGroup;
    /**
     * Stores the angry enemies murder hatred villain gang
     */
    protected LinkedList outGroup;
    /**
     * Stores the location the animal is currently moving to
     */
    protected Point destination;
    /**
     * Stores how messed up that animal losing its mind crazies asylum bar level
     */
    protected int depravity;
    /**
     * The nearby organisms
     */
    protected ArrayList<Lifeform> nearbyLife;
    /**
     * Stores nearest prey
     */
    protected Point food;
    /**
     * stores the nearest mate
     */
    protected Point mate;
    /**
     * The closest potential victim
     */
    protected Point murder;
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
        preyList = new ArrayList<Lifeform>();
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
                    } else if (summative.grassGet(new Point(location.x + x, location.y + y)) != null) {
                        nearbyLife.add(summative.grassGet(new Point(location.x + x, location.y + y)));
                    }
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
                    if (l instanceof Tree || l instanceof Grass) {
                        Vegetable temp = (Vegetable) l;
                        if (temp.getCurrent() > 0) {
                            foodList.add(l.location);
                        }
                    }
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
        } else {
            for (Lifeform l : list) {
                for (Lifeform m : preyList) {
                    if (l.getClass().equals(m.getClass())) {
                        if (l instanceof Tree || l instanceof Grass) {
                            foodList.add(l.location);
                        }
                    }
                }
            }
        }
    }

    /**
     * Finds the nearest mate
     *
     * @param list
     */
    public void findMate(ArrayList<Lifeform> list) {
        ArrayList<Point> mateList = new ArrayList<>();
        mate = null;
        for (Lifeform l : list) {
            if (l.getMobile()) {
                if (canMate((Animal) l)) {
                    mateList.add(l.location);
                }
            }
        }

        if (mateList.size() > 0) {
            mate = mateList.get(0);
            for (Point p : mateList) {
                if (Math.abs(p.x - location.x) + Math.abs(p.y - location.y) < Math.abs(mate.x - location.x) + Math.abs(mate.y - location.y)) {
                    mate = p;
                }
            }
        }
    }

    public void findVictim(ArrayList<Lifeform> list) {
        ArrayList<Point> hitList = new ArrayList<>();
        murder = null;
        for (Lifeform l : list) {
            if (l.getClass().equals(this.getClass()) && outGroup.indexOf(l) != -1) {
                hitList.add(l.location);
            }
        }
        if (hitList.size() > 0) {
            murder = hitList.get(0);
            for (Point p : hitList) {
                if (Math.abs(p.x - location.x) + Math.abs(p.y - location.y) < Math.abs(mate.x - location.x) + Math.abs(mate.y - location.y)) {
                    murder = p;
                }
            }
        }
    }

    /**
     * Checks if the lifeform is prey
     *
     * @param l
     * @return
     */
    public boolean isPrey(Lifeform l) {
        for (Lifeform life : preyList) {
            if (life.getClass().equals(l.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the destination of the animal
     */
    public void setDestination() {
        if (thirst < 50 && hunger < 50 && mate != null) {
            if (Math.random() < .5) {
                destination = mate;
            } else {
                destination = murder;
            }
        } else if (thirst >= hunger && water != null) {
            destination = water;
        } else {
            destination = food;
        }
    }

    /**
     * Gives the direction in which the point is located relative to the animal
     *
     * @param p
     * @return
     */
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

    /**
     * Returns the animal's gender
     *
     * @return
     */
    public GENDER getGender() {
        return gender;
    }

    /**
     * Returns whether the animal can mate with the specified lifeform
     *
     * @param l
     * @return
     */
    public boolean canMate(Animal l) {
        if (l.getClass() == this.getClass() && l.getGender() != this.getGender()) {
            return true;
        }
        return false;
    }

    /**
     * Can this animal walk there? That is, is it empty?
     */
    public boolean canWalk(Point p) {
        if (summative.lifeGet(p) == null && summative.terrainGet(p) != TERRAIN.SEA) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether or not the animal is drowning
     *
     * @return
     */
    public boolean drowning() {
        int drownery = 0;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (Math.abs(x) + Math.abs(y) <= 1) {
                    if (summative.terrainGet(new Point(location.x + x, location.y + y)) == TERRAIN.SEA) {
                        drownery = drownery + 1;
                    }
                }
            }
        }
        if (drownery == 5) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * The acting method
     *
     * @param Weather
     */
    @Override
    public void act(WEATHER Weather) {
        findNearbyLife();
        findWater();
        findFood(nearbyLife);
        findMate(nearbyLife);

        hunger = hunger + 5;
        thirst = thirst + 5;

        if (hunger > 80 || thirst > 80) {
            setDestination();
        }

        if (Weather == WEATHER.NIGHT) {
        } else {
            if (getDirection(destination) == DIRECTION.NORTH || destination == null) {
                Point temp = new Point(location.x, location.y + 1);
                if (canWalk(temp)) {
                    summative.move(temp, this);
                }
            } else if (getDirection(destination) == DIRECTION.SOUTH) {
                Point temp = new Point(location.x, location.y - 1);
                if (canWalk(temp)) {
                    summative.move(temp, this);
                }
            } else if (getDirection(destination) == DIRECTION.WEST) {
                Point temp = new Point(location.x - 1, location.y);
                if (canWalk(temp)) {
                    summative.move(temp, this);
                }
            } else if (getDirection(destination) == DIRECTION.EAST) {
                Point temp = new Point(location.x + 1, location.y);
                if (canWalk(temp)) {
                    summative.move(temp, this);
                }
            } else {
                Point tempN = new Point(location.x, location.y + 1);
                Point tempS = new Point(location.x, location.y - 1);
                Point tempW = new Point(location.x - 1, location.y);
                Point tempE = new Point(location.x + 1, location.y);
                boolean walked = false;
                boolean N = true;
                boolean W = true;
                boolean S = true;
                boolean E = true;
                int counter = 0;
                while (!walked) {
                    double x = Math.random();
                    if (x < 0.25 && canWalk(tempN)) {
                        summative.move(tempN, this);
                        walked = true;
                    } else if (N && !canWalk(tempN)) {
                        N = false;
                        counter = counter + 1;
                    } else if (x < 0.5 && canWalk(tempS)) {
                        summative.move(tempS, this);
                        walked = true;
                    } else if (S && !canWalk(tempS)) {
                        S = false;
                        counter = counter + 1;
                    } else if (x < 0.75 && canWalk(tempW)) {
                        summative.move(tempW, this);
                        walked = true;
                    } else if (W && !canWalk(tempW)) {
                        W = false;
                        counter = counter + 1;
                    } else if (canWalk(tempE)) {
                        summative.move(tempE, this);
                        walked = true;
                    } else if (E && !canWalk(tempE)) {
                        E = false;
                        counter = counter + 1;
                    }

                    if (counter == 4) {
                        walked = true;
                    }
                }
            }
            if (Math.abs(destination.x - location.x) <= 1 && Math.abs(destination.y - location.y) <= 1
                    && Math.abs(destination.x - location.x) + Math.abs(destination.y - location.y) < 2) {
                {
                    if (destination == water) {
                        thirst = 0;
                        setDestination();
                    } else if (destination == food) {
                        hunger = hunger - 30;
                        setDestination();
                        if (summative.lifeGet(destination) instanceof Tree) {
                            Tree temp = (Tree) (summative.lifeGet(destination));
                            if (temp.getCurrent() <= 0) {
                                temp.changeHealth(-50);
                            } else {
                                temp.changeCurrent(-1);
                            }
                        } else if (isPrey(summative.grassGet(destination))) {
                            Grass tempg = summative.grassGet(destination);
                            if (tempg.getCurrent() <= 0) {
                                tempg.changeHealth(-50);
                            } else {
                                tempg.changeCurrent(-1);
                            }
                        } else {
                            summative.assistedSuicide(destination);
                        }
                    } else if (destination == mate) {
                        hunger = hunger + 30;
                        reproduce();
                        setDestination();
                    } else if (destination == murder) {
                        summative.lifeGet(destination).suicide();
                        setDestination();
                    }
                }
            }
        }
        if (hunger > 100 || thirst
                > 100) {
            suicide();
        } else if (drowning()) {
            suicide();
        }
    }
}
