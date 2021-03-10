import Controller.AndantinoController;
import Controller.GameController;
import Model.AndantinoGameBoard;
import Model.GameBoard;
import View.AndantinoView;

public class App {

    /*
    * Instantiate Model, View. Controller components.
    * */
    public static void main(String[] args) {
        GameBoard model = new AndantinoGameBoard();
        GameController controller = new AndantinoController(model);
    }

}
