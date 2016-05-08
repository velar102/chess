/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

/**
 *
 * @author Drago
 */
public class Rook extends Piece {
    
   
   private boolean hasMoved;
   
   public Rook(boolean setColor)
   {
       super(setColor);
       identifier = 'r';
       hasMoved = false;
   }
   
   public boolean checkIfMoved()
   {
       return hasMoved;
   }
   
   public void setMoved()
   {
       hasMoved = true;
   }
}
