package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class BoardController extends Scene {
    Color LIGHT_COLOR = Color.web("lemonchiffon");
    Color DARK_COLOR = Color.web("brown");

    public ArrayList<Rectangle> board;
    public ImageView[] imgView;
    private ImageView sourcePiece;
    public MenuBar menuBar;
    
    private int clickStartIndex;
    
    private void menuStuff()
    {
        menuBar = new MenuBar();
        
        Menu menuFile = new Menu("File");
        Menu menuNetwork = new Menu("Network");
        Menu menuView = new Menu("View");
        
        MenuItem host = new MenuItem("Host");
        host.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                startHosting();
            }
        });        
        
        MenuItem client = new MenuItem("Connect...");
        client.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                startClient();
            }
        }); 
 
        menuNetwork.getItems().addAll(host, client);
 
        menuBar.getMenus().addAll(menuFile, menuNetwork, menuView);
    }
    
    private void startClient()
    {
        try {
            socket = new DatagramSocket();
            IPAddress = InetAddress.getByName("localhost");

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            String sentence = "Connecting.";
            sendData = sentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

            socket.send(sendPacket);
        }
        catch (Exception e) { System.out.println("Connection failed!"); };

        /*for (ImageView image : imgView)
        {
            image.setRotate(180);
        }*/
        port = 9876;
        isClient = true;
        byte[] receiveData = new byte[1024];
        boolean noResponse = true;
        String sentence = "";
        while (noResponse)
                        {
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                              try {
                                  socket.receive(receivePacket);
                              } catch (IOException ex) {
                                  Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
                              }
                              sentence = new String(receivePacket.getData());
                              if (!sentence.equals(""))
                              {
                                  noResponse = false;
                                  System.out.println("This is sentence: " + sentence);
                              }
                        }
                        String[] parts = sentence.split(",");
                        System.out.println("This is parts: " + parts[0] + ", " + parts[1]);
                                                
                        int sourceIndex = Integer.parseInt(parts[1].trim());
                        int targetIndex = Integer.parseInt(parts[0].trim());
                        
                        System.out.println("This is sourceIndex: " + sourceIndex);
                        System.out.println("This is targetIndex: " + targetIndex);
                        
                        int worked2 = model.movePiece(sourceIndex % 8, sourceIndex / 8, targetIndex % 8, targetIndex / 8);
                        System.out.println("This is worked2: " + worked2);
                        if (worked2 == 1)
                        {
                            Rectangle target2 = board.get(targetIndex);
                            imgView[sourceIndex].xProperty().bind(target2.xProperty());
                            imgView[sourceIndex].yProperty().bind(target2.yProperty());

                            for (int z = 0; z < 64; z++)
                            {
                                if (z != sourceIndex)
                                {
                                    if (target2.contains(imgView[z].getX(), imgView[z].getY()))
                                    {
                                        imgView[z].xProperty().unbind();
                                        imgView[z].yProperty().unbind();
                                        imgView[z].setX(999999);
                                        imgView[z].setY(999999);
                                    }
                                }
                            }
                        }
                        
                        model.printBoard();
    }
    
    private void startHosting()
    {
        socket = null;
	  
	try
	{
            socket = new DatagramSocket(9876);
	}
	catch(Exception e)
	{
            System.out.println("Failed to open UDP socket!");
            System.exit(0);
	}

      byte[] receiveData = new byte[1024];
      
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            socket.receive(receivePacket);
        } catch (IOException ex) {
            Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sentence = new String(receivePacket.getData());
        IPAddress = receivePacket.getAddress();
        port = receivePacket.getPort();
    }
    
    private DatagramSocket socket;
    private InetAddress IPAddress;
    private int port;
    private boolean isClient;
    private BoardModel model;

    public BoardController(Parent theParent, int XSize, int YSize, Color color, Stage primaryStage, BoardModel modelIn) {
        super(theParent, XSize, YSize, color);
        
        isClient = false;
        model = modelIn;
        
        menuStuff();
        
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
        
        Image whiteRook = new Image("resources/pieceImages/rook.png");
        Image blackRook = new Image("resources/pieceImages/rookB.png");
        Image whiteKnight = new Image("resources/pieceImages/knight.png");
        Image blackKnight = new Image("resources/pieceImages/knightB.png");
        Image whiteBishop = new Image("resources/pieceImages/bishop.png");
        Image blackBishop = new Image("resources/pieceImages/bishopB.png");
        Image whiteQueen = new Image("resources/pieceImages/queen.png");
        Image blackQueen = new Image("resources/pieceImages/queenB.png");
        Image whiteKing = new Image("resources/pieceImages/king.png");
        Image blackKing = new Image("resources/pieceImages/kingB.png");
        Image whitePawn = new Image("resources/pieceImages/pawn.png");
        Image blackPawn = new Image("resources/pieceImages/pawnB.png");
        
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
                    try {
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
                        byte[] sendData  = new byte[1024];
                        String k = Integer.toString(board.indexOf(target));
                        String q = Integer.toString(Arrays.asList(imgView).indexOf(sourcePiece));
                        String send = k + "," + q;
                        sendData = send.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                        
                        socket.send(sendPacket);
                        byte[] receiveData = new byte[1024];
                        boolean noResponse = true;
                        String sentence = "";
                        while (noResponse)
                        {
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                              try {
                                  socket.receive(receivePacket);
                              } catch (IOException ex) {
                                  Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
                              }
                              sentence = new String(receivePacket.getData());
                              if (!sentence.equals(""))
                              {
                                  noResponse = false;
                              }
                        }
                        String[] parts = sentence.split("\\,");
                        int sourceIndex = Integer.parseInt(parts[1].trim());
                        int targetIndex = Integer.parseInt(parts[0].trim());
                        
                        int worked2 = model.movePiece(sourceIndex % 8, sourceIndex / 8, targetIndex % 8, targetIndex / 8);
                        if (worked2 == 1)
                        {
                            Rectangle target2 = board.get(targetIndex);
                            imgView[sourceIndex].xProperty().bind(target2.xProperty());
                            imgView[sourceIndex].yProperty().bind(target2.yProperty());

                            for (int z = 0; z < 64; z++)
                            {
                                if (z != sourceIndex)
                                {
                                    if (target2.contains(imgView[z].getX(), imgView[z].getY()))
                                    {
                                        imgView[z].xProperty().unbind();
                                        imgView[z].yProperty().unbind();
                                        imgView[z].setX(999999);
                                        imgView[z].setY(999999);
                                    }
                                }
                            }
                        }
                        
                        model.printBoard();
                        
                    } catch (IOException ex) {
                        Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
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