// This is the Controller class that handles all nodes sent to the view and networking initialization.

package chess;

/**
 * @author Rio Velarde
 * @author Austin Thiel
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BoardController extends Scene {

    // The color variables used to declare the board tile colors.
    Color LIGHT_COLOR = Color.web("lemonchiffon");
    Color DARK_COLOR = Color.web("brown");

    public ArrayList<Rectangle> board; // Contains the rectangles that make up the board.
    public ImageView[] imgView; // Contains all the piece images.
    private ImageView sourcePiece; // Contains the piece currently being dragged.
    public MenuBar menuBar; // The menu bar at the top of the window.
    
    private int boardSourceIndex; // The origin board index of the piece being moved.
    private int boardTargetIndex; // The target rectangle of the piece being moved.
    
    private boolean isReset; // Stores if the board is at its initial state or not.
    private boolean networked; // Stores if there is an active network connection.
    private boolean isClient; // Stores if the applet is the client or the host.
    private boolean isTurn; // Stores whose turn it is.
    
    private Thread t; // The packet listener thread.
    private Dialog waitForConnect; // The dialog box for a connection wait.
    private DatagramSocket socket; // The connection socket.
    private InetAddress IPAddress; // The IP Address of the other player.
    private int port; // The connection port.

    private BoardModel model; // The board model containing the current game state.

    // Initializes the menu and defines all the methods run by the different options
    private void menuStuff() {

        // Makes the new menu and sets the effects.
        menuBar = new MenuBar();
        menuBar.setEffect(new SepiaTone());
        menuBar.setEffect(new Glow());
        menuBar.setEffect(new DropShadow());

        // The dropdown button on the menu.
        Menu menuNetwork = new Menu("Network");

        // The menu options in the dropdown:
        
        MenuItem host = new MenuItem("Host");
        host.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (socket == null || socket.isClosed()) { // Only allow hosting when there isn't already an active connection.
                    if (isReset) {  // Only allow hosting from an initial board state.
                        startHosting();
                    } else {
                        Dialog d = new Dialog();
                        d.setContentText("Please reset the board first!");
                        d.getDialogPane().getButtonTypes().add(ButtonType.OK);
                        d.showAndWait();
                    }
                } else {
                    Dialog d = new Dialog();
                    d.setContentText("You are already connected! Reset the board to disconnect before making a new connection.");
                    d.getDialogPane().getButtonTypes().add(ButtonType.OK);
                    d.showAndWait();
                }
            }
        });

        MenuItem client = new MenuItem("Connect...");
        client.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (socket == null || socket.isClosed()) {
                    if (isReset) {
                        startClient();
                    } else {
                        Dialog d = new Dialog();
                        d.setContentText("Please reset the board first!");
                        d.getDialogPane().getButtonTypes().add(ButtonType.OK);
                        d.showAndWait();
                    }
                } else {
                    Dialog d = new Dialog();
                    d.setContentText("You are already connected! Reset the board to disconnect before making a new connection.");
                    d.getDialogPane().getButtonTypes().add(ButtonType.OK);
                    d.showAndWait();
                }
            }
        });

        // Adds the dropdown items to the dropdown menu.
        menuNetwork.getItems().addAll(host, client);

        // The other dropdown menu.
        Menu menuBoard = new Menu("Board");

        // The reset option in the board menu that resets the board to the default state.
        MenuItem reset = new MenuItem("Reset");
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (socket != null) {
                    socket.close();
                }
                Chess.reset();
            }
        });

        // Adds the reset option to the board dropdown.
        menuBoard.getItems().addAll(reset);

        // Adds the dropdown menus to the menu.
        menuBar.getMenus().addAll(menuNetwork, menuBoard);
    }

    // This is run when someone attempts to connect to a host.
    private void startClient() {
        try {
            networked = true;
            socket = new DatagramSocket();

            Dialog d = new TextInputDialog("localhost");
            d.setTitle("Connect");
            d.setHeaderText("Connect to an IP Address");
            d.setContentText("Enter an IP Address to connect to: ");
            Optional<String> result = d.showAndWait();
            if (result.isPresent() && !result.get().equals("")) {
                IPAddress = InetAddress.getByName(result.get());
            } else {
                return; // Simply exits the menu and does nothing if the IP field is left blank.
            }
            
            // Sends the first packet to the host to inform it that it has connected.
            String sentence = "Connecting.";
            byte[] sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            socket.send(sendPacket);

            // Shows a dialog box to wait for confirmation response from the host...
            // Does not freeze program execution. It is simply closed later when a connection is established or 
            // the user cancels the connection attempt.
            waitForConnect = new Dialog();
            waitForConnect.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            waitForConnect.setContentText("Attempting to connect to host...");
            //waitForConnect.setX(this.getX() + this.getWidth() / 2 + waitForConnect.getWidth() / 2);
            //waitForConnect.setY(this.getY() + this.getHeight() / 2 + waitForConnect.getHeight() / 2);
            waitForConnect.show();

            /*for (ImageView image : imgView)
            {
            image.setRotate(180);
            }*/
            
            port = 9876;
            isClient = true;
            isTurn = false;

            // Starts the new thread to listen for packets.
            PacketListener pListener = new PacketListener(this, socket);
            t = new Thread(pListener);
            t.setDaemon(true);
            t.start();

            // Cancels all networking operations when the cancel button on the connection waiting dialog is pressed.
            Button b = (Button) waitForConnect.getDialogPane().lookupButton(ButtonType.CANCEL);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    t.interrupt();
                    networked = false;
                    isTurn = true;
                    socket.close();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // This method executes when a user attempts to host a game.
    private void startHosting() {

        try {
            socket = new DatagramSocket(9876);
        } catch (Exception e) {
            System.out.println("Failed to open UDP socket!");
            Dialog d = new Dialog();
            d.setContentText("Failed to open UDP socket! It is likely in use already.");
            d.getDialogPane().getButtonTypes().add(ButtonType.OK);
            d.showAndWait();
            return; // Cancels hosting if the UDP socket is in use.
        }

        networked = true;
        
        waitForConnect = new Dialog();
        waitForConnect.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        waitForConnect.setContentText("Waiting for connection...");
        //waitForConnect.setX(this.getX() + this.getWidth() / 2 + waitForConnect.getWidth() / 2);
        //waitForConnect.setY(this.getY() + this.getHeight() / 2 + waitForConnect.getHeight() / 2);
        waitForConnect.show();

        PacketListener pListener = new PacketListener(this, socket);
        t = new Thread(pListener);
        t.setDaemon(true);
        t.start();

        Button b = (Button) waitForConnect.getDialogPane().lookupButton(ButtonType.CANCEL);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                t.interrupt();
                networked = false;
                socket.close();
                isTurn = true;
            }
        });
    }

    // These methods are used by the packet listener to set the IPAddress and port of the client when the host gets its first packet.
    public void setIPAddress(InetAddress theAddress) {
        IPAddress = theAddress;
    }
    public void setPort(int thePort) {
        port = thePort;
    }

    // The constructor for the controller. Initializes the board view and all networking variables.
    public BoardController(Parent theParent, int XSize, int YSize, Color color, Stage primaryStage, BoardModel modelIn) {
        super(theParent, XSize, YSize, color);

        isReset = true;
        networked = false;
        isClient = false;
        isTurn = true;
        socket = null;
        
        model = modelIn;
        
        // Initializes the menus.
        menuStuff();

        // Creates the list of rectangles to make up the board.
        board = new ArrayList<>();
        Rectangle rectangle = new Rectangle();

        // Binds the size of the rectangles to match the scale of the window.
        DoubleBinding squareSize = new DoubleBinding() {
            {
                super.bind(primaryStage.heightProperty(), primaryStage.widthProperty());
            }

            @Override
            protected double computeValue() {
                if (primaryStage.widthProperty().get() > primaryStage.heightProperty().get()) {
                    return primaryStage.heightProperty().get() / 10;
                } else {
                    return primaryStage.widthProperty().get() / 10;
                }
            }
        };

        // Binds the position of the squares to match the window position.
        DoubleBinding xOffset = new DoubleBinding() {
            {
                super.bind(primaryStage.heightProperty(), primaryStage.widthProperty());
            }

            @Override
            protected double computeValue() {
                if (primaryStage.widthProperty().get() > primaryStage.heightProperty().get()) {
                    return ((primaryStage.widthProperty().get() - primaryStage.heightProperty().get()) / 2);
                } else {
                    return -5;
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
                    return -20;
                } else {
                    return ((primaryStage.heightProperty().get() - primaryStage.widthProperty().get()) / 2);
                }
            }
        };

        // Adds all the squares to the board list.
        for (int i = 0; i < 64; i++) {
            board.add(new Rectangle());
        }

        // Initializes the piece image view array and loads images from the locations of all the piece images.
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

        // Iterates through all the squares on the board...
        for (Rectangle square : board) {
            int i = board.indexOf(square);

            // Sets the correct colors for all the squares.
            if (i < 8 || (i > 15 && i < 24) || (i > 31 && i < 40) || (i > 47 && i < 56)) {
                if (i % 2 == 0) {
                    square.setFill(LIGHT_COLOR);
                } else {
                    square.setFill(DARK_COLOR);
                }
            } else if (i % 2 == 0) {
                square.setFill(DARK_COLOR);
            } else {
                square.setFill(LIGHT_COLOR);
            }

            // Applies the previous binding on the squares to match the window scale size.
            square.heightProperty().bind(squareSize);
            square.widthProperty().bind(squareSize);

            // Enforces the position bindings on each individual square.
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

            // Initializes all the imageViews with their proper piece images.
            boolean emptyImage = false; // Indicates the empty squares on the board.
            if (i == 0 || i == 7) {
                imgView[i] = new ImageView(blackRook);
            } else if (i == 1 || i == 6) {
                imgView[i] = new ImageView(blackKnight);
            } else if (i == 2 || i == 5) {
                imgView[i] = new ImageView(blackBishop);
            } else if (i == 3) {
                imgView[i] = new ImageView(blackQueen);
            } else if (i == 4) {
                imgView[i] = new ImageView(blackKing);
            } else if (i > 7 && i < 16) {
                imgView[i] = new ImageView(blackPawn);
            } else if (i > 47 && i < 56) {
                imgView[i] = new ImageView(whitePawn);
            } else if (i == 56 || i == 63) {
                imgView[i] = new ImageView(whiteRook);
            } else if (i == 57 || i == 62) {
                imgView[i] = new ImageView(whiteKnight);
            } else if (i == 58 || i == 61) {
                imgView[i] = new ImageView(whiteBishop);
            } else if (i == 59) {
                imgView[i] = new ImageView(whiteQueen);
            } else if (i == 60) {
                imgView[i] = new ImageView(whiteKing);
            } else {
                imgView[i] = new ImageView();
                emptyImage = true;
            }

            if (!emptyImage) { // Binds the pieces to the proper squares with an offset to avoid overlapping on the boundaries.
                               // This is helpful for piece detection on squares.
                DoubleBinding sizeOffset = new DoubleBinding() {
                    {
                        super.bind(squareSize);
                    }

                    @Override
                    protected double computeValue() {
                        return squareSize.get() - 2;
                    }
                };
                imgView[i].fitHeightProperty().bind(sizeOffset);
                imgView[i].fitWidthProperty().bind(sizeOffset);
                
                // Enforces the square binding on the proper pieces.
                sourcePiece = imgView[i];
                boardTargetIndex = i;
                bindImageToSquare(i);
            } else { // Gets the empty imageViews out of the way.
                imgView[i].setX(9999);
                imgView[i].setY(9999);
            }

            // Controls what happens when you click a piece.
            final Delta dragDelta = new Delta();
            imgView[i].setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // Record a delta distance for the drag and drop operation.
                    dragDelta.x = imgView[i].getX() - mouseEvent.getSceneX();
                    dragDelta.y = imgView[i].getY() - mouseEvent.getSceneY();

                    imgView[i].setCursor(Cursor.MOVE);

                    boardSourceIndex = -1;
                    
                    // Finds which square the piece you clicked on was in.
                    for (Rectangle square : board) {
                        if (square.contains(mouseEvent.getSceneX(), mouseEvent.getSceneY())) {
                            boardSourceIndex = board.indexOf(square);
                        }
                    }
                    sourcePiece = imgView[i];
                }
            });
            
            // Controls what happens when you release a piece on a square.
            imgView[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    imgView[i].setCursor(Cursor.HAND);

                    boardTargetIndex = 0;
                    int targetIndex = 0;
                    Rectangle target = new Rectangle();
                    for (Rectangle square : board) {
                        if (square.contains(mouseEvent.getSceneX(), mouseEvent.getSceneY())) {
                            target = square;
                            boardTargetIndex = board.indexOf(square);
                            targetIndex = board.indexOf(square);
                        }
                    }

                    // Only attempts to move the piece if it was the user's turn.
                    int worked = 0; // Records if the move was legal.
                    if (isTurn) {
                        if (!networked || (isClient && Arrays.asList(imgView).indexOf(sourcePiece) < 24) || (!isClient && Arrays.asList(imgView).indexOf(sourcePiece) > 24)) {
                            worked = model.movePiece(boardSourceIndex % 8, boardSourceIndex / 8, boardTargetIndex % 8, boardTargetIndex / 8);
                        }
                        model.printBoard();
                    }

                    // If the move was legal...
                    if (worked > 0) {
                        isReset = false;
                        try {
                            movePiece(targetIndex);

                            if (worked == 1) { // If it was a normal move, put the piece image there and delete any already existing piece images
                                               // on that square.
                                for (int z = 0; z < 64; z++) {
                                    if (imgView[z] != sourcePiece) {
                                        if (target.contains(imgView[z].getX(), imgView[z].getY())) {
                                            imgView[z].xProperty().unbind();
                                            imgView[z].yProperty().unbind();
                                            imgView[z].setX(999999);
                                            imgView[z].setY(999999);
                                        }
                                    }
                                }
                            }

                            // If the move was a castling move, move the king and rook to the proper places.
                            if (worked == 2) {
                                Rectangle rooktangle = new Rectangle();
                                Rectangle rooktarget = new Rectangle();
                                switch (board.indexOf(target)) {
                                    case 2:
                                        rooktangle = board.get(0);
                                        rooktarget = board.get(3);
                                        break;
                                    case 6:
                                        rooktangle = board.get(7);
                                        rooktarget = board.get(5);
                                        break;
                                    case 58:
                                        rooktangle = board.get(56);
                                        rooktarget = board.get(59);
                                        break;
                                    case 62:
                                        rooktangle = board.get(63);
                                        rooktarget = board.get(61);
                                        break;
                                    default:
                                        break;
                                }

                                for (int z = 0; z < 64; z++) {
                                    if (rooktangle.contains(imgView[z].getX(), imgView[z].getY())) {
                                        imgView[z].xProperty().unbind();
                                        imgView[z].yProperty().unbind();
                                        imgView[z].xProperty().bind(rooktarget.xProperty());
                                        imgView[z].yProperty().bind(rooktarget.yProperty());
                                    }
                                }
                            }

                            // If there is an active network connection, send the move to the other player.
                            if (networked) {
                                String k = Integer.toString(boardSourceIndex);
                                String q = Integer.toString(boardTargetIndex);
                                String send = k + "," + q;
                                byte[] sendData = send.getBytes();
                                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

                                socket.send(sendPacket);

                                isTurn = false;
                            }

                        } catch (IOException ex) {
                            Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else { // If it was not the user's turn or the move was not legal, put the piece back on the square you started
                             // dragging it from.
                        Rectangle source = board.get(boardSourceIndex);
                        sourcePiece.xProperty().bind(source.xProperty());
                        sourcePiece.yProperty().bind(source.yProperty());
                    }
                }
            });
            
            // Controls what happens when you move the mouse while dragigng a piece.
            // (makes the piece image follow the cursor as you move it)
            imgView[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    imgView[i].xProperty().unbind();
                    imgView[i].yProperty().unbind();
                    imgView[i].setX(mouseEvent.getSceneX() + dragDelta.x);
                    imgView[i].setY(mouseEvent.getSceneY() + dragDelta.y);
                }
            });
            imgView[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    imgView[i].setCursor(Cursor.HAND);
                }
            });
        }
    }

    // Takes a piece and does the actual binding to the square.
    private void bindImageToSquare(int targetIndex) {
        DoubleBinding recXOffset = new DoubleBinding() {
            {
                super.bind(board.get(targetIndex).xProperty());
            }

            @Override
            protected double computeValue() {
                return (board.get(targetIndex).getX() + 1);
            }
        };
        DoubleBinding recYOffset = new DoubleBinding() {
            {
                super.bind(board.get(targetIndex).yProperty());
            }

            @Override
            protected double computeValue() {
                return (board.get(targetIndex).getY() + 1);
            }
        };
        sourcePiece.xProperty().unbind();
        sourcePiece.yProperty().unbind();
        sourcePiece.xProperty().bind(recXOffset);
        sourcePiece.yProperty().bind(recYOffset);

    }

    // Currently just moves the actual piece image to the right square by callign the binding method.
    // Will be expanded later to contain more of the movement code.
    private void movePiece(int targetIndex) {
        bindImageToSquare(targetIndex);
    }

    // This is the method for handling moves received by the other player.
    public void handleMove(String sentence) {
        if (sentence.equals("Connecting.")) { // If it's the first message from a client when the connection is established...
            waitForConnect.close(); // Close the waiting for a connection dialog box.
            
            // Sends the confirmation of connection back to the client.
            String response = "Confirmed.";
            byte[] sendData = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            try {
                socket.send(sendPacket);
            } catch (IOException ex) {
                Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return; // Ends the movement handling.
        } else if (sentence.equals("Confirmed.")) { // If it's the client getting the connection confirmation from the host...
            waitForConnect.close(); // Close the attempting to connect to host dialog.
            return; // Ends movement handling.
        }
        
        // Takes a default move message and breaks it into the needed variables.
        String[] parts = sentence.split("\\,");
        System.out.println("This is parts: " + parts[0] + ", " + parts[1]);
        int sourceIndex = Integer.parseInt(parts[0].trim());
        int targetIndex = Integer.parseInt(parts[1].trim());

        // Attempts to perform the move stated in the message.
        int worked = model.movePiece(sourceIndex % 8, sourceIndex / 8, targetIndex % 8, targetIndex / 8);
        if (worked > 0) {
            isReset = false;
            int imgViewSrcIndex = -1;
            for (int m = 0; m < 64; m++) {
                if (board.get(sourceIndex).contains(imgView[m].getX(), imgView[m].getY())) {
                    imgViewSrcIndex = m;
                }
            }
            Rectangle targetSquare = board.get(targetIndex);
            sourcePiece = imgView[imgViewSrcIndex];
            boardTargetIndex = targetIndex;
            movePiece(targetIndex);

            // Moved the piece if it was a normal move that was legal.
            if (worked == 1) {
                for (int z = 0; z < 64; z++) {
                    if (z != imgViewSrcIndex) {
                        if (targetSquare.contains(imgView[z].getX(), imgView[z].getY())) {
                            imgView[z].xProperty().unbind();
                            imgView[z].yProperty().unbind();
                            imgView[z].setX(999999);
                            imgView[z].setY(999999);
                        }
                    }
                }
            }

            // Moves the king and rook if it was a castling move that was legal.
            if (worked == 2) {
                Rectangle rooktangle = new Rectangle();
                Rectangle rooktarget = new Rectangle();
                switch (targetIndex) {
                    case 2:
                        rooktangle = board.get(0);
                        rooktarget = board.get(3);
                        break;
                    case 6:
                        rooktangle = board.get(7);
                        rooktarget = board.get(5);
                        break;
                    case 58:
                        rooktangle = board.get(56);
                        rooktarget = board.get(59);
                        break;
                    case 62:
                        rooktangle = board.get(63);
                        rooktarget = board.get(61);
                        break;
                    default:
                        break;
                }

                for (int z = 0; z < 64; z++) {
                    if (rooktangle.contains(imgView[z].getX(), imgView[z].getY())) {
                        imgView[z].xProperty().unbind();
                        imgView[z].yProperty().unbind();
                        imgView[z].xProperty().bind(rooktarget.xProperty());
                        imgView[z].yProperty().bind(rooktarget.yProperty());
                    }
                }
            }
        }
        model.printBoard();

        // Sends a move was just received from the other player, it is now this player's turn.
        isTurn = true;
    }
}

// A simple class with two doubles to use for mouse dragging coordinates to update the image to follow the cursor.
class Delta {

    double x, y;
}