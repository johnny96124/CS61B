package creatures;

import huglife.Occupant;
import huglife.Creature;
import huglife.Empty;
import huglife.Direction;
import huglife.Action;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * An implementation of a motile pacifist photosynthesizer.
 *
 * @author Josh Hug
 */
public class Plip extends Creature {

    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;

    /**
     * creates plip with energy equal to E.
     */
    public Plip(double e) {
        super("plip");
        r = 99;
        g = 0;
        b = 76;
        energy = e;
    }

    /**
     * creates a plip with energy equal to 1.
     */
    public Plip() {
        this(1);
    }

    /**
     * Should return a color with red = 99, blue = 76, and green that varies
     * linearly based on the energy of the Plip. If the plip has zero energy,
     * it should have a green value of 63. If it has max energy, it should
     * have a green value of 255. The green value should vary with energy
     * linearly in between these two extremes. It's not absolutely vital
     * that you get this exactly correct.
     */
    public Color color() {
        g = (96 * ((int) this.energy) + 63);
        return color(r, g, b);
    }

    /**
     * Do nothing with C, Plips are pacifists.
     */
    public void attack(Creature c) {
        // do nothing.
    }

    /**
     * Plips should lose 0.15 units of energy when moving. If you want to
     * to avoid the magic number warning, you'll need to make a
     * private static final variable. This is not required for this lab.
     */
    public void move() {
        this.energy -= 0.15;
        checkEnergy();
    }


    /**
     * Plips gain 0.2 energy when staying due to photosynthesis.
     */
    public void stay() {
        this.energy += 0.2;
        checkEnergy();
    }

    /*
     * Checks to see if Plip's energy is above 2 or less than 0 and adjust accordingly
     * to never be greater than or less than either, respectively.
     */
    private void checkEnergy() {
        if (this.energy < 0.0) {
            this.energy = 0.0;
        } else if (this.energy > 2.0) {
            this.energy = 2.0;
        }
    }

    /**
     * Plips and their offspring each get 50% of the energy, with none
     * lost to the process. Now that's efficiency! Returns a baby
     * Plip.
     */
    public Plip replicate() {
        Plip dup = new Plip(this.energy / 2.0);
        this.energy = this.energy / 2.0;
        return dup;
    }

    /**
     * Plips take exactly the following actions based on NEIGHBORS:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise, if energy >= 1, REPLICATE towards an empty direction
     * chosen at random.
     * 3. Otherwise, if any Cloruses, MOVE with 50% probability,
     * towards an empty direction chosen at random.
     * 4. Otherwise, if nothing else, STAY
     * <p>
     * Returns an object of type Action. See Action.java for the
     * scoop on how Actions work. See SampleCreature.chooseAction()
     * for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        // Rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        boolean anyClorus = false;

        for (Map.Entry<Direction, Occupant> item : neighbors.entrySet()) {
            if (item.getValue() instanceof Empty) {
                emptyNeighbors.add(item.getKey());
            }
        }

        if (emptyNeighbors.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        }

        // Rule 2
        if (!emptyNeighbors.isEmpty() && this.energy >= 1.0) {
            return new Action(Action.ActionType.REPLICATE, randomEntry(emptyNeighbors));
        }

        // Rule 3
        for (Occupant occupant : neighbors.values()) {
            if (occupant.name().equals("clorus")) {
                if (((int) (Math.random() * 2) == 1)) {
                    return new Action(Action.ActionType.MOVE, randomEntry(emptyNeighbors));
                }
            }
        }
        // Rule 4
        return new Action(Action.ActionType.STAY);
    }

    private Direction randomEntry(Deque<Direction> emptyNeigh) {
        int random = (int) (Math.random() * emptyNeigh.size()) + 1;
        if (random == 1) {
            return emptyNeigh.removeFirst();
        } else if (random == 2) {
            emptyNeigh.removeFirst();
            return emptyNeigh.removeFirst();
        } else if (random == 3) {
            emptyNeigh.removeFirst();
            emptyNeigh.removeFirst();
            return emptyNeigh.removeFirst();
        } else {
            emptyNeigh.removeFirst();
            emptyNeigh.removeFirst();
            emptyNeigh.removeFirst();
            return emptyNeigh.removeFirst();
        }
    }
}
