/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

public class Square {
    
    private boolean hasPiece;
    private Piece piece;
    
    public Square()
    {
        hasPiece = false;
        piece = null;
    }
    public Square(Piece newPiece)
    {
        hasPiece = true;
        piece = newPiece;
    }
    
    public boolean checkForPiece()
    {
        return hasPiece;
    }
    public Piece getPiece()
    {
        if (hasPiece)
        {
            return piece;
        }
        else
        {
            return null;
        }
    }
    
    public void setPiece()
    {
        piece = null;
        hasPiece = false;
    }
    public void setPiece(Piece newPiece)
    {
        piece = newPiece;
        hasPiece = true;
    }
}
