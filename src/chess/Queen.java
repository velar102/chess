/*
 * This is simply a Queen implementation of the abstract Piece class.
 */
package chess;

public class Queen extends Piece {
   
   public Queen(boolean setColor)
   {
       super(setColor);
       identifier = 'q';
   }
}
