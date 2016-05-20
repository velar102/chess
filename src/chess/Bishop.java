/*
 * This is simply a Bishop implementation of the abstract Piece class.
 */
package chess;

public class Bishop extends Piece {
   
   public Bishop(boolean setColor)
   {
       super(setColor);
       identifier = 'b';
   }
}
