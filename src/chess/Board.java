package chess;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import java.util.ArrayList;
import javafx.stage.Stage;

public class Board extends Scene {
    Color LIGHT_COLOR = Color.web("lemonchiffon");
    Color DARK_COLOR = Color.web("brown");

    public double squareSize;
    public ArrayList<Rectangle> board;
    
    public Board(Parent theParent, int XSize, int YSize, Color color, Stage primaryStage) {
        super(theParent, XSize, YSize, color);
        
        board = new ArrayList<>();
        Rectangle rectangle = new Rectangle();
        
        DoubleBinding squareSize = new DoubleBinding() {
            {
                super.bind(primaryStage.heightProperty(), primaryStage.widthProperty());
            }
 
            @Override
            protected double computeValue() {
                if (primaryStage.widthProperty().get() > primaryStage.heightProperty().get()) {
                    return primaryStage.heightProperty().get() / 10;
                }
                else {
                    return primaryStage.widthProperty().get() / 10;
                }
            }
        };
        
        DoubleBinding xOffset = new DoubleBinding() {
            {
                super.bind(primaryStage.heightProperty(), primaryStage.widthProperty());
            }
            
            @Override
            protected double computeValue() {
                if (primaryStage.widthProperty().get() > primaryStage.heightProperty().get()) {
                    return ( (primaryStage.widthProperty().get() - primaryStage.heightProperty().get()) / 2);
                }
                else {
                    return 0;
                }
            }
        };
        
        DoubleBinding yOffset = new DoubleBinding() {
            {
                super.bind(primaryStage.heightProperty(), primaryStage.widthProperty());
            }
            
            @Override
            protected double computeValue() {
                if (primaryStage.widthProperty().get() > primaryStage.heightProperty().get()) {
                    return 0;
                }
                else {
                    return ( (primaryStage.heightProperty().get() - primaryStage.widthProperty().get()) / 2);
                }
            }
        };
        
        rectangle.setFill(DARK_COLOR);
        
        for (int i = 0; i < 64; i++)
        {
            board.add(new Rectangle());
        }
        
        for (Rectangle square : board) {
            
            int i = board.indexOf(square);
            if ( i < 8 || (i > 15 && i < 24) || (i > 31 && i < 40) || (i > 47 && i < 56) )
            {
                if (i % 2 == 0) {
                    square.setFill(LIGHT_COLOR);
                }
                else {
                    square.setFill(DARK_COLOR);
                }
            }
            else
            {
                if (i % 2 == 0) {
                    square.setFill(DARK_COLOR);
                }
                else {
                    square.setFill(LIGHT_COLOR);
                }
            }
            
            square.heightProperty().bind(squareSize);
            square.widthProperty().bind(squareSize);
  
            DoubleBinding xPos = new DoubleBinding() {
                {
                    super.bind(xOffset, squareSize);
                }
                
                @Override
                protected double computeValue() {
                    return (xOffset.get() + ((i % 8) + 1) * squareSize.get());
                }
            };

            square.xProperty().bind(xPos);
            
            DoubleBinding yPos = new DoubleBinding() {
                {
                    super.bind(yOffset, squareSize);
                }
                
                @Override
                protected double computeValue() {
                    return (yOffset.get() + ((i / 8) + 1) * squareSize.get());
                }
            };
             
            square.yProperty().bind(yPos);
        }
    }
    
/*
    def board = [ Coord.A8, Coord.B8, Coord.C8, Coord.D8,
			Coord.E8, Coord.F8, Coord.G8, Coord.H8,
                  Coord.A7, Coord.B7, Coord.C7, Coord.D7,
			Coord.E7, Coord.F7, Coord.G7, Coord.H7,
                  Coord.A6, Coord.B6, Coord.C6, Coord.D6,
			Coord.E6, Coord.F6, Coord.G6, Coord.H6,
                  Coord.A5, Coord.B5, Coord.C5, Coord.D5,
			Coord.E5, Coord.F5, Coord.G5, Coord.H5,
                  Coord.A4, Coord.B4, Coord.C4, Coord.D4,
			Coord.E4, Coord.F4, Coord.G4, Coord.H4,
                  Coord.A3, Coord.B3, Coord.C3, Coord.D3,
			Coord.E3, Coord.F3, Coord.G3, Coord.H3,
                  Coord.A2, Coord.B2, Coord.C2, Coord.D2,
			Coord.E2, Coord.F2, Coord.G2, Coord.H2,
                  Coord.A1, Coord.B1, Coord.C1, Coord.D1,
			Coord.E1, Coord.F1, Coord.G1, Coord.H1 ];

    */
}