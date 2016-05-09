    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import java.util.AbstractMap.SimpleEntry;
import javafx.scene.paint.Color;
import java.util.Map.Entry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Eagle
 */
public class Chess extends Application {
    
    
        /*
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/
    final Entry<String, Effect>[] effects = new Entry[] {
        new SimpleEntry<String, Effect>("Sepia Tone", new SepiaTone()),
        new SimpleEntry<String, Effect>("Glow", new Glow()),
        new SimpleEntry<String, Effect>("Shadow", new DropShadow())
    };
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chess");
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setWidth(1600);
        primaryStage.setHeight(900);

        Group root = new Group();
        
        BoardModel model = new BoardModel();
        BoardController board = new BoardController(root, 0, 0, Color.BURLYWOOD, primaryStage, model);
        
        model.printBoard();
 
        root.getChildren().addAll(board.menuBar);
        
        root.getChildren().addAll(board.board);
        root.getChildren().addAll(board.imgView);
        
 
        
        primaryStage.setScene(board);
        primaryStage.show();
        
        /*
        primaryStage.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                double value = (double) t1;
                System.out.println("Scene Width :" + value);
            } 
        }); 
        
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
            }
        });
        
        */
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }
    
}
