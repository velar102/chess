/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eagle
 */
public class PacketListener implements Runnable {

    private final BoardController theController;
    private final DatagramSocket socket;
    
    public PacketListener(BoardController bc, DatagramSocket sock) {
        theController = bc;
        socket = sock;
    }

    @Override
    public void run() {
        while(true)
        {
            byte[] receiveData = new byte[1024];
            boolean noResponse = true;

            while (noResponse) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    socket.receive(receivePacket);
                } catch (IOException ex) {
                    Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
                }
                String sentence = new String(receivePacket.getData());
                if (!sentence.equals("")) {
                    noResponse = false;
                    theController.handleMove(sentence);
                }
            }
        }
    }
}
