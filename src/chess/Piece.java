/*
 * This is an abstract piece class to store in a square object. Implementations will be the actual specific chess piece types.
 */
package chess;

public abstract class Piece {
    
    // The color of the piece. True for white and false for black.
    private final boolean color;
    protected char identifier; 
    // An identifer is a single char that designates what type of piece it is. Used for printing the board to a terminal.
    
    // Allows the color of a piece to be set in the constructor.
    public Piece(boolean setColor)
    {
        color = setColor;
    }
    
    // Returns the color of the piece.
    public boolean getColor()
    {
        return color;
    }
    
    // Returns the identifier of the piece.
    public char getIdentifier()
    {
        return identifier;
    }
}