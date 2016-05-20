/*
 * This is the square class to fill up the actual board in the model.
 */
package chess;

public class Square {
    
    private boolean hasPiece; // Stores whether the square has a piece on it or not.
    private Piece piece; // Contains the piece on the square, if there is one.
    
    public Square() // The constructor for a square. It defaults to having no piece.
    {
        hasPiece = false;
        piece = null;
    }
    public Square(Piece newPiece) // An alternate constructor that defaults to having the piece passed to it.
    {
        hasPiece = true;
        piece = newPiece;
    }
    
    // Checks if the square has a piece on it or not.
    public boolean checkForPiece()
    {
        return hasPiece;
    }
    public Piece getPiece() // Returns the piece if the board does have one.
    {
        if (hasPiece)
        {
            return piece;
        }
        else
        {
            return null; // Returns null if there is no piece on the square.
        }
    }
    
    // Removes a piece from the square.
    public void setPiece()
    {
        piece = null;
        hasPiece = false;
    }
    
    // Sets the passed piece to be on the square.
    public void setPiece(Piece newPiece)
    {
        piece = newPiece;
        hasPiece = true;
    }
}
