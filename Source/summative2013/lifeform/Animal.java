package summative2013.lifeform;

import java.awt.Point;
import java.util.ArrayList;
import summative2013.Summative;
import summative2013.Summative.TERRAIN;
import static summative2013.lifeform.Lifeform.summative;
import summative2013.memory.Memory;
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
    /*public LinkedList<Animal> getInGroup() {
     return inGroup;
     }*/
    /**
     * @return the outGroup
     */
    /*public LinkedList<Animal> getOutGroup() {
     return outGroup;
     }*/
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
    //protected LinkedList inGroup;
    /**
     * Stores the angry enemies murder hatred villain gang
     */
    //protected LinkedList outGroup;
    /**
     * Stores the location the animal is currently moving to
     */
    protected Point destination;
    /**
     * Stores how messed up that animal losing its mind crazies asylum bar level
     */
    protected int depravity;
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
    protected ArrayList<Class> preyList;
    /**
     * Works at night
     */
    protected boolean nocturnal;

    /**
     * Constructor
     */
    public Animal() {
        super();
        setDestination();
        hunger = 50;
        gender = Math.random() < 0.5 ? GENDER.MALE : GENDER.FEMALE;
        depravity = 0;
        preyList = new ArrayList<Class>();
    }

    /**
     * Finds the closest prey
     */
    public void findFood(ArrayList<Lifeform> list) {
        ArrayList<Point> foodList = new ArrayList<Point>();
        food = null;
        for (Lifeform l : list) {
            for (Class m : preyList) {
                if (l.getClass().equals(m)) {
                    if (l instanceof Tree || l instanceof Grass) {
                        Vegetable temp = (Vegetable) l;
                        if (temp.getCurrent() > 0) {
                            foodList.add(l.getLocation());
                        }
                    }
                }
            }
        }
        if (foodList.size() > 0) {
            food = foodList.get(0);
            final Point location = summative.getLocation(this);
            for (Point p : foodList) {
                if (Math.abs(p.x - location.x) + Math.abs(p.y - location.y) < Math.abs(food.x - location.x) + Math.abs(food.y - location.y)) {
                    food = p;
                }
            }
        } else {
            for (Lifeform l : list) {
                for (Class m : preyList) {
                    if (l.getClass().equals(m)) {
                        if (l instanceof Tree || l instanceof Grass) {
                            foodList.add(l.getLocation());
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
        ArrayList<Point> mateList = new ArrayList<Point>();
        mate = null;
        for (Lifeform l : list) {
            if (l.getMobile()) {
                if (canMate((Animal) l)) {
                    mateList.add(l.getLocation());
                }
            }
        }

        if (mateList.size() > 0) {
            mate = mateList.get(0);
            final Point location = summative.getLocation(this);
            for (Point p : mateList) {
                if (Math.abs(p.x - location.x) + Math.abs(p.y - location.y) < Math.abs(mate.x - location.x) + Math.abs(mate.y - location.y)) {
                    mate = p;
                }
            }
        }
    }

    public void findVictim(ArrayList<Lifeform> list) {
        ArrayList<Point> hitList = new ArrayList<Point>();
        murder = null;
        for (Lifeform l : list) {
            if (l.getClass().equals(this.getClass())) {
                hitList.add(l.getLocation());
            }
        }
        if (hitList.size() > 0) {
            murder = hitList.get(0);
            for (Point p : hitList) {
                final Point location = summative.getLocation(this);
                if (Math.abs(p.x - location.x) + Math.abs(p.y - location.y) < Math.abs(mate.x - location.x) + Math.abs(mate.y - location.y)) {
                    murder = p;
                }
            }
        }
    }

    /**
     * Checks if the lifeform is prey
     *
     * @param l the lifeform we want to eat
     * @return if it is prey
     */
    public boolean isPrey(Lifeform l) {
        for (Class m : preyList) {
            if (l.getClass().equals(m)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the destination of the animal
     */
    public void setDestination() {
        if (thirst > 50 && water != null) {
            destination = water;
        } else if (hunger > 50 && food != null) {
            destination = food;
        } else if (mate != null) {
            destination = mate;
        }

        if (destination == null) {
            destination = water;
        }
    }

    /**
     * Gives the direction in which the point is located relative to the animal
     *
     * @param p the Point we are going to
     * @return the Direction to get there
     */
    public DIRECTION getDirection(Point p) {
        final Point location = summative.getLocation(this);
        if (p.x == location.x && p.y == location.y) {
            return DIRECTION.CENTER;
        } else if (Math.abs(p.x - location.x) > Math.abs(p.y - location.y)) {
            if (p.x > location.x) {
                return DIRECTION.EAST;
            } else {
                return DIRECTION.WEST;
            }
        } else if (p.y > location.y) {
            return DIRECTION.SOUTH;
        } else {
            return DIRECTION.NORTH;
        }
    }

    /**
     * Returns the animal's gender
     *
     * @return the Gender of the animal
     */
    public GENDER getGender() {
        return gender;
    }

    /**
     * Returns whether the animal can mate with the specified lifeform
     *
     * @param l the lifeform to mate with
     * @return Can we mate?
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
        if (summative.emptyAt(p) && summative.terrainGet(p) == TERRAIN.LAND) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether or not the animal is drowning
     *
     * @return if we are drowning
     */
    public boolean drowning() {
        if (summative.terrainGet(summative.getLocation(this)) == TERRAIN.SEA) {
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

        boolean walked = false;

        if (diseased) {
            int temp = sight;
            sight = 4;
            findNearbyLife();
            for (Lifeform l : nearbyLife) {
                l.disease();
            }
            sight = temp;
            hunger = hunger + 20;
            thirst = thirst + 20;
        } else {
            if (Math.random() < 0.01) {
                disease();
            }
        }

        findNearbyLife();
        findWater();
        findFood(nearbyLife);
        findMate(nearbyLife);

        hunger = hunger + 1;
        thirst = thirst + 1;

        if (hunger > 70 || thirst > 70) {
            setDestination();
        }

        if (Weather == WEATHER.NIGHT && !nocturnal) {
        } else {
            final Point location = summative.getLocation(this);

            if (destination == null) {
            } else if (getDirection(destination) == DIRECTION.SOUTH) {
                Point temp = new Point(location.x, location.y + 1);
                if (canWalk(temp)) {
                    walked = true;
                    setLocation( temp);
                }
            } else if (getDirection(destination) == DIRECTION.NORTH) {
                Point temp = new Point(location.x, location.y - 1);
                if (canWalk(temp)) {
                    walked = true;
                    setLocation( temp);
                }
            } else if (getDirection(destination) == DIRECTION.WEST) {
                Point temp = new Point(location.x - 1, location.y);
                if (canWalk(temp)) {
                    walked = true;
                    setLocation(temp);
                }
            } else if (getDirection(destination) == DIRECTION.EAST) {
                Point temp = new Point(location.x + 1, location.y);
                if (canWalk(temp)) {
                    walked = true;
                    setLocation( temp);
                }
            }
            if (walked == false) {
                Point tempS = new Point(location.x, location.y + 1);
                Point tempN = new Point(location.x, location.y - 1);
                Point tempW = new Point(location.x - 1, location.y);
                Point tempE = new Point(location.x + 1, location.y);
                boolean S = true;
                boolean W = true;
                boolean N = true;
                boolean E = true;
                int counter = 0;
                while (!walked) {
                    double x = Math.random();
                    if (x < 0.25 && canWalk(tempS)) {
                        setLocation(tempS);
                        walked = true;
                    } else if (S && !canWalk(tempS)) {
                        S = false;
                        counter = counter + 1;
                    } else if (x < 0.5 && canWalk(tempN)) {
                        setLocation(tempN);
                        walked = true;
                    } else if (N && !canWalk(tempN)) {
                        S = false;
                        counter = counter + 1;
                    } else if (x < 0.75 && canWalk(tempW)) {
                        setLocation( tempW);
                        walked = true;
                    } else if (W && !canWalk(tempW)) {
                        W = false;
                        counter = counter + 1;
                    } else if (canWalk(tempE)) {
                        setLocation( tempE);
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
            if (destination != null
                    && Math.abs(destination.x - location.x) <= 1 && Math.abs(destination.y - location.y) <= 1
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
                        } else if (preyList.contains(Grass.class) && summative.lifeGet(destination) instanceof Grass) {
                            Grass tempg = summative.grassGet(destination);
                            if (tempg.getCurrent() <= 0) {
                                tempg.changeHealth(-50);
                            } else {
                                tempg.changeCurrent(-1);
                            }
                        } else {
							Animal hunted = (Animal) summative.lifeGet(destination);
							if (summative.lifeGet(destination) != null) {
								summative.lifeGet(destination).suicide();
							}
							else {
								destination = null;
							}
                        }
                    } else if (destination == mate) {
						Point nearbySpace = nearEmpty();
						if (nearbySpace != null) {
							hunger = hunger + 30;
							reproduce(nearbySpace);
							setDestination();
						}
                    } else if (destination == murder) {
                        summative.lifeGet(destination).suicide();
                        setDestination();
                    }
                }
            }
        }
        if (hunger > 100 || thirst > 100) {
            suicide();
        } else if (drowning()) {
            suicide();
        }
    }

    /**
     * Prints some info about the animal
     *
     * @return a String representing the animal
     */
    @Override
    public String toString() {
        return super.toString()
                + "\nGender: " + gender
                + "\nDestination: " + ((destination != null)
                ? destination.x + "," + destination.y : "")
                + "\nHunger: " + hunger + "%";
    }
}
