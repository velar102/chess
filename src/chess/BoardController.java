package chess;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BoardController extends Scene {
    Color LIGHT_COLOR = Color.web("lemonchiffon");
    Color DARK_COLOR = Color.web("brown");

    public double squareSize;
    public ArrayList<Rectangle> board;
    public ImageView[] imgView;
    ImageView sourcePiece;
    
    private int clickStartIndex;
    
    public BoardController(Parent theParent, int XSize, int YSize, Color color, Stage primaryStage, BoardModel model) {
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
        
        imgView = new ImageView[64];
        Image whiteRook = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/rook.png");
        Image blackRook = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/rookB.png");
        Image whitePawn = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/pawn.png");
        Image blackPawn = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/pawnB.png");
        Image blackKnight = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/knightB.png");
        Image blackBishop = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/bishopB.png");
        Image blackQueen = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/queenB.png");
        Image blackKing = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/kingB.png");
        
        Image whiteKnight = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/knight.png");
        Image whiteBishop = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/bishop.png");
        Image whiteQueen = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/queen.png");
        Image whiteKing = new Image("file:///C:/Users/Drago/OneDrive/Documents/GitHub/chess/src/chess/king.png");
        
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
            
            if (i == 0 || i == 7)
            {
                imgView[i] = new ImageView(blackRook);
            }
            else if (i == 1 || i == 6)
            {
                imgView[i] = new ImageView(blackKnight);
            }
            else if (i == 2 || i == 5)
            {
                imgView[i] = new ImageView(blackBishop);
            }
            else if (i == 3)
            {
                imgView[i] = new ImageView(blackQueen);
            }            
            else if (i == 4)
            {
                imgView[i] = new ImageView(blackKing);
            }  
            else if (i > 7 && i < 16)
            {
                imgView[i] = new ImageView(blackPawn);
            }
            else if (i > 47 && i < 56)
            {
                imgView[i] = new ImageView(whitePawn);
            }
            else if (i == 56 || i == 63)
            {
                imgView[i] = new ImageView(whiteRook);
            }
            else if (i == 57 || i == 62)
            {
                imgView[i] = new ImageView(whiteKnight);
            }
            else if (i == 58 || i == 61)
            {
                imgView[i] = new ImageView(whiteBishop);
            }
            else if (i == 59)
            {
                imgView[i] = new ImageView(whiteQueen);
            }
            else if (i == 60)
            {
                imgView[i] = new ImageView(whiteKing);
            }
            else {
                imgView[i] = new ImageView();
            }
            
            imgView[i].fitHeightProperty().bind(squareSize);
            imgView[i].fitWidthProperty().bind(squareSize);
            imgView[i].xProperty().bind(square.xProperty());
            imgView[i].yProperty().bind(square.yProperty());     
                
            final Delta dragDelta = new Delta();
            imgView[i].setOnMousePressed(new EventHandler<MouseEvent>() {
              @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = imgView[i].getX() - mouseEvent.getSceneX();
                dragDelta.y = imgView[i].getY() - mouseEvent.getSceneY();
                imgView[i].setCursor(Cursor.MOVE);
                
                clickStartIndex = -1;
                for (Rectangle square : board)
                {
                    if (square.contains(mouseEvent.getSceneX(), mouseEvent.getSceneY()))
                    {
                        clickStartIndex = board.indexOf(square);
                    }
                }
                sourcePiece = imgView[i];
              }
            });
            imgView[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
              @Override public void handle(MouseEvent mouseEvent) {
                imgView[i].setCursor(Cursor.HAND);
                 
                int i = 0;
                Rectangle target = new Rectangle();
                for (Rectangle square : board)
                {
                    if (square.contains(mouseEvent.getSceneX(), mouseEvent.getSceneY()))
                    {
                        target = square;
                        i = board.indexOf(square);
                    }
                }
                
                int worked = model.movePiece(clickStartIndex % 8, clickStartIndex / 8, i % 8, i / 8);
                model.printBoard();
                
                if (worked == 1)
                {
                    sourcePiece.xProperty().bind(target.xProperty());
                    sourcePiece.yProperty().bind(target.yProperty());
                    
                    for (int z = 0; z < 64; z++)
                    {
                        if (imgView[z] != sourcePiece)
                        {
                            if (target.contains(imgView[z].getX(), imgView[z].getY()))
                            {
                                imgView[z].xProperty().unbind();
                                imgView[z].yProperty().unbind();
                                imgView[z].setX(999999);
                                imgView[z].setY(999999);
                            }
                        }
                    }
                }
                else
                {
                    Rectangle source = board.get(clickStartIndex);
                    sourcePiece.xProperty().bind(source.xProperty());
                    sourcePiece.yProperty().bind(source.yProperty());
                }
              }
            });
            imgView[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
              @Override public void handle(MouseEvent mouseEvent) {
                imgView[i].xProperty().unbind();
                imgView[i].yProperty().unbind();
                imgView[i].setX(mouseEvent.getSceneX() + dragDelta.x);
                imgView[i].setY(mouseEvent.getSceneY() + dragDelta.y);
              }
            });
            imgView[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
              @Override public void handle(MouseEvent mouseEvent) {
                imgView[i].setCursor(Cursor.HAND);
              }
            });
        }
    }
}

class Delta { double x, y; }