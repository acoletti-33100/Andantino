package Model.Expert;

import Model.Tile;

import java.util.List;

/**
 * interface of an expert, it determines the weight of a
 * given feature.
 * It might be useful for future improvements to let
 * the program set his own weights,
 * if used with "temporal-difference learning"
 */
public interface Expert {
    /**
     * gets the weight of this expert.
     * @return weight field
     */
    int getWeight();

    /**
     * sets the weight of this expert.
     * @param weight new value for the weight field
     */
    void setWeight(int weight);

    /**
     * evaluates the weight of this feature.
     * @param gameBoard gameboard to evaluate
     * @return weight of this feature
     */
    int getWeight(List<Tile> gameBoard);
}
