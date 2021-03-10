package Model.Expert;

import Model.Tile;

import java.util.List;
import java.util.SplittableRandom;

/**
 * random expert, it evaluates a random weight.
 */
public final class RandomExpert implements Expert {

    /**
     * weight of this feature.
     */
    private int weight;

    /**
     * random generator.
     */
    SplittableRandom random;

    public RandomExpert() {
        weight = 5;
        random = new SplittableRandom();
    }

    /**
     * gets the weight of this feature.
     * @return weight of this feature
     */
    @Override
    public int getWeight() {
        return weight;
    }

    /**
     * sets the weight of this expert.
     * @param weight new weight to assign to this expert
     */
    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * evaluates the weight of the random feature.
     * It assumes a non empty gameboard.
     * @return random number in [-weight,weight]
     */
    @Override
    public int getWeight(List<Tile> gameBoard) {
        return random.nextInt(-weight, weight + 1);
    }
}
