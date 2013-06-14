package summative2013.lifeform;

import summative2013.phenomena.Weather.WEATHER;

/**
 * Parent class of immobile food generating autotrophic benevolent food chain
 * base
 *
 * @author 322303413
 */
public class Vegetable extends Lifeform {

    /**
     * Stores current level of being eaten regenerates over time dies at 0
     */
    protected int health;
    /**
     * Stores amount of happy life sustaining UV solar fusion glory praise the
     * sun required for life
     */
    protected int sun;
    /**
     * Amount of time to regenerate and produce more foodstuffs
     */
    protected int regenTime;
    /**
     * How close it is to regenerating
     */
    protected int regenCounter;
    /**
     * Maximum amount of food at any time, regeneration fills to max
     */
    protected int capacity;
    /**
     * Stores amount of food currently on plant
     */
    protected int current;

    /**
     * Default
     */
    public Vegetable() {
        super();
        health = 99;
        sun = 50;
    }

    /**
     * Will regenerate all the food and then reset the counter
     */
    protected void regenerate() {
        current = capacity;
        regenCounter = regenTime;
    }

    @Override
    /**
     * Changes variables depending on weather
     */
    public void act(WEATHER weather) {
        if (weather == WEATHER.SUN) {
            regenCounter = regenCounter - 2;
            sun = sun + 5;
            thirst = thirst + 2;
        } else if (weather == WEATHER.RAIN) {
            regenCounter = regenCounter - 1;
            thirst = thirst - 5;
            sun = sun - 2;
        } else if (weather == WEATHER.CLOUD) {
            regenCounter = regenCounter - 1;
            thirst = thirst + 1;
            sun = sun - 1;
        }

        if (sun < 0) {
            alive = false;
        } else if (thirst > 99) {
            alive = false;
        } else if (health < 0) {
            alive = false;
        }

        if (health < 0) {
            summative.assistedSuicide(location);
        } else {
            health = health + 5;
        }
    }

    public void changeHealth(int change) {
        health = health + change;
    }

    public void changeCapacity(int change) {
        capacity = capacity + change;
    }

    public int getCapacity() {
        return capacity;
    }
}
