    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Eagle
 */
public class Chess extends Application {
    
    @Override
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
    
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chess");
        primaryStage.setX(100);
        primaryStage.setY(100);
        primaryStage.setWidth(1600);
        primaryStage.setHeight(900);
        Group root = new Group();
        //Canvas canvas = new Canvas( 488, 640 );
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        
  //      Image img = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/pawn.png");
  //      ImageView imgView = new ImageView(img);
        


        Board board = new Board(root, 300, 250, Color.BURLYWOOD, primaryStage);
        
//        imgView.fitHeightProperty().bind(board.squareSize);
        
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
