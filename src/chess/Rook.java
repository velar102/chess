/*
 * This is simply a Rook implementation of the abstract Piece class.
 */
package chess;

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
