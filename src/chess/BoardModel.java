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
public class BoardModel {
    
    private final Square[][] board;
    
    public BoardModel()
    {
        board = new Square[8][8];
        
        for (int y = 0; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                boolean color;
                if (y < 2)
                {
                    color = false; // Black
                }
                else
                {
                    color = true; // White
                }
                if (y == 1 || y == 6)
                {
                    board[x][y] = new Square();
                    board[x][y].setPiece(new Pawn(color));
                }
                else if (y > 1 && y < 6)
                {
                    board[x][y] = new Square(); // Empty Squares
                }
                else
                {
                    board[x][y] = new Square(); // Squares I didn't populate with proper pieces yet.
                }
            }
        }
    }
    
    public int movePiece(int fromX, int fromY, int toX, int toY)
    {
        if (board[toX][toY].checkForPiece())
        {
            if (board[fromX][fromY].getPiece().getColor() == board[toX][toY].getPiece().getColor())
            {
                return 0;
            }
        }
        
        if (board[fromX][fromY].getPiece() instanceof Pawn)
        {
            return movePawn(fromX, fromY, toX, toY);
        }
        return 0;
    }
    
    private int movePawn(int fromX, int fromY, int toX, int toY)
    {
        int offset;
        Pawn pawn = (Pawn) board[fromX][fromY].getPiece();
        
        if (pawn.getColor())
        {
            offset = 1;
        }
        else
        {
            offset = -1;
        }
        if ( (toY == fromY-offset && ((toX == fromX && !board[toX][toY].checkForPiece())|| ( board[toX][toY].checkForPiece() && (toX == fromX-1 || toX == fromX+1) ) )) || (!pawn.checkIfMoved() && toY == fromY-(2*offset)))
        {
            pawn.setMoved();
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(pawn);

            return 1;
        }
        
        return 0;
    }
    
    public void printBoard()
    {
        System.out.println("-------------");
        for (int y = 0; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                if (board[x][y].checkForPiece())
                {
                    System.out.print(board[x][y].getPiece().getIdentifier());
                }
                else
                {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println("-------------");
    }
}
