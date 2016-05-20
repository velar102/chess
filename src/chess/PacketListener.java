/*
 * This class is run on a separate thread to handle packet listening, so that the main program does not lock up.
 */
package chess;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javafx.application.Platform;

/**
 * @author Austin Thiel
 * @author Rio Velarde
 */
public class PacketListener implements Runnable {

    // This variable contains the controller that called it, so it can send moves back to it.
    private final BoardController theController;
    
    // This variable contains the socket to listen on.
    private final DatagramSocket socket;

    // The constructor simply initialized the class variables.
    public PacketListener(BoardController bc, DatagramSocket sock) {
        theController = bc;
        socket = sock;
    }

    // The run method is executed when the new thread for this class is started.
    @Override
    public void run() {
        // Runs until the thread receives an interrupt signal.
        while (!Thread.interrupted()) {
            byte[] receiveData = new byte[1024];
            boolean noResponse = true;

            // Runs until it gets a response, then resets the byteArray and response boolean and loops again.
            while (noResponse) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    socket.receive(receivePacket);
                } catch (IOException ex) {
                    // Once the socket has been closed, this exception will be caught and the thread will interrupt itself to end.
                    System.out.println("Socket has been closed. Ending listener thread.");
                    Thread.currentThread().interrupt();
                    break;
                    // Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Makes a new string containing the sent data once a message is received.
                String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (!sentence.equals("")) {
                    switch (sentence) {
                        // This is the message a server first sends when connected to a host, to inform the host it connected.
                        case "Connecting.":
                            theController.setIPAddress(receivePacket.getAddress());
                            theController.setPort(receivePacket.getPort());
                            
                            // This sends the connected message to the main thread. Since JavaFX GUI elements can only by modified by
                            // the main thread, it must use Platform.runLater to queue the method in the main thread
                            // rather than running the method in this thread.
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    theController.handleMove(sentence);
                                }
                            }); break;
                        // This is the message the host sends back to the server to acknowledge its connection.
                        case "Confirmed.":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    theController.handleMove(sentence);
                                }
                            }); break;
                        default:
                            // This simply sends the move to the controller.
                            theController.handleMove(sentence);
                            break;
                    }
                    noResponse = false;
                }
            }
        }
    }
}
