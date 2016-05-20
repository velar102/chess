/**
 * This is a networked chess game that uses UDP.
 */
/**
 * @author Rio Velarde
 * @author Austin Thiel
 */
package chess;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Chess extends Application {

    private static Stage primStage;

    @Override
    public void start(Stage primaryStage) {
        // The start method is automatically passed a stage for the main window when a JavaFX application is launched.

        // Sets all the needed properties for the main stage and makes the class variable reference it.
        primaryStage.setTitle("Chess");
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setWidth(900);
        primaryStage.setHeight(900);
        primaryStage.setMinHeight(225);
        primaryStage.setMinWidth(180);
        primStage = primaryStage;

        // Creates a new root group that holds all the element nodes to be seen in the window.
        Group root = new Group();

        // Creates a new chess board model that contains the state of the board.
        BoardModel model = new BoardModel();

        // Creates a new controller that will communicate what to place in the view on the stage and communicate moves to the model.
        BoardController board = new BoardController(root, 0, 0, Color.BURLYWOOD, primaryStage, model);

        // This simply prints out the fresh model to the command line so you can see where it has all the pieces.
        model.printBoard();

        // This adds the elements initialized in the constructor of the Controller to the root group for display.
        root.getChildren().addAll(board.menuBar);
        root.getChildren().addAll(board.board);
        root.getChildren().addAll(board.imgView);

        // This sets the board controller (which extends scene) as the default display state for the stage and then activates the stage view.
        primaryStage.setScene(board);
        primaryStage.show();
    }

    // This is simply the default main method to start a JavaFX application.
    public static void main(String[] args) {
        launch(args);
    }

    // This method is for resetting to a new board. It creates a new group, new model, new controller, adds everything
    // to the root group and then replaces the old board scene with the new one on the primary viewing stage.
    public static void reset() {
        Group root = new Group();
        BoardModel model = new BoardModel();
        BoardController board = new BoardController(root, 0, 0, Color.BURLYWOOD, primStage, model);

        model.printBoard();

        root.getChildren().addAll(board.menuBar);
        root.getChildren().addAll(board.board);
        root.getChildren().addAll(board.imgView);

        primStage.setScene(board);
    }
}
