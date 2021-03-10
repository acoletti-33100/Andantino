package Model.Expert;

import Model.Supervisor.AndantinoSupervisor;
import Model.Supervisor.GameSupervisor;
import Model.Tile;

import java.util.List;

/**
 * win expert, it evaluates the weight of a victory.
 */
public final class WinExpert implements Expert {

    /** supervisor to determine loss.
     *
     */
    GameSupervisor supervisor;

    /**
     * weight of this feature.
     */
    private int weight;

    public WinExpert() {
        supervisor = new AndantinoSupervisor();
        weight = 20;
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
     * evaluates the weight of this feature.
     * It assumes a non empty gameboard.
     * @return weight of a victory
     */
    @Override
    public int getWeight(List<Tile> gameBoard) {
        if(supervisor.hasWon(gameBoard.get(gameBoard.size() - 1).getPlayer(), gameBoard))
            return weight;
        return 0;
    }
}

