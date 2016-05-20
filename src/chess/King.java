/*
 * This is simply a King implementation of the abstract Piece class.
 */
package chess;


public class King extends Piece {
    
   
   private boolean hasMoved;
   
   public King(boolean setColor)
   {
       super(setColor);
       identifier = 'k';
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
