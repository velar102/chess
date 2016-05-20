/*
 * This is simply a Pawn implementation of the abstract Piece class.
 */
package chess;

public class Pawn extends Piece {
    
   
   private boolean hasMoved;
   
   public Pawn(boolean setColor)
   {
       super(setColor);
       identifier = 'p';
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
