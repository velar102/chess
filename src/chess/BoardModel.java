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
                boolean color = (y >= 3);
                
                if (y == 1 || y == 6)
                {
                    board[x][y] = new Square();
                    board[x][y].setPiece(new Pawn(color));
                }
                else if (y > 1 && y < 6)
                {
                    board[x][y] = new Square(); // Empty Squares
                }
                else if (y == 0 || y == 7)
                {
                    board[x][y] = new Square();
                    switch (x) {
                        case 0:
                        case 7:
                            board[x][y].setPiece(new Rook(color));
                            break;
                        case 1:
                        case 6:
                            board[x][y].setPiece(new Knight(color));
                            break;
                        case 2:
                        case 5:
                            board[x][y].setPiece(new Bishop(color));
                            break;
                        case 3:
                            board[x][y].setPiece(new Queen(color));
                            break;
                        case 4:
                            board[x][y].setPiece(new King(color));
                            break;                    
                        default:
                            break;
                    }
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
        else if (board[fromX][fromY].getPiece() instanceof Rook)
        {
            return moveRook(fromX, fromY, toX, toY);
        }
        else if (board[fromX][fromY].getPiece() instanceof Knight)
        {
            return moveKnight(fromX, fromY, toX, toY);
        }
        else if (board[fromX][fromY].getPiece() instanceof Bishop)
        {
            return moveBishop(fromX, fromY, toX, toY);
        }
        else if (board[fromX][fromY].getPiece() instanceof King)
        {
            return moveKing(fromX, fromY, toX, toY);
        }
        else if (board[fromX][fromY].getPiece() instanceof Queen)
        {
            return moveQueen(fromX, fromY, toX, toY);
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
    
    private int moveKing(int fromX, int fromY, int toX, int toY)
    {
        King king = (King) board[fromX][fromY].getPiece();
        if ( ( Math.abs(toX - fromX) == 1 || Math.abs(toX - fromX) == 0 ) && ( Math.abs(toY - fromY) == 1 || Math.abs(toY - fromY) == 0 ))
        {
            king.setMoved();
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(king);
            return 1;
        }
            
        return 0;
    }
    
    private int moveKnight(int fromX, int fromY, int toX, int toY)
    {
        Knight knight = (Knight) board[fromX][fromY].getPiece();
        if ( ( Math.abs(toX - fromX) == 2 && Math.abs(toY - fromY) == 1 ) || ( Math.abs(toY - fromY) == 2 && Math.abs(toX - fromX) == 1 ))
        {
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(knight);
            return 1;
        }
            
        return 0;
    }
    
    private int moveBishop(int fromX, int fromY, int toX, int toY)
    {
        Bishop bishop = (Bishop) board[fromX][fromY].getPiece();
        if (Math.abs(toX - fromX) == Math.abs(toY - fromY))
        {
            int offsetX;
            int offsetY;
            
            if (toX > fromX) {  offsetX = 1;  }
            else {  offsetX = -1;  }
            
            if (toY > fromY) {  offsetY = 1;  }
            else {  offsetY = -1;  }
            
            for (int i=1; i < Math.abs(toX-fromX); i++) {
                if (board[fromX+i*offsetX][fromY+i*offsetY].checkForPiece()) {
                  return 0;
                }
            }
            
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(bishop);
            return 1;
        }
        
        return 0;
    }
    
    private int moveQueen(int fromX, int fromY, int toX, int toY)
    {
        Queen queen = (Queen) board[fromX][fromY].getPiece();

        if ( toX == fromX )
        {
            int offsetY;
            if (toY > fromY) {  offsetY = 1;  }
            else {  offsetY = -1;  }
            
            for (int i = 1; i < Math.abs(toY - fromY); i++)
            {
                if (board[fromX][fromY + i*offsetY].checkForPiece())
                {
                    return 0;
                }
            }

            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(queen);
            return 1;
        }
        else if (toY == fromY)
        {
            int offsetX;
            
            if (toX > fromX) {  offsetX = 1;  }
            else {  offsetX = -1;  }
            
            for (int i = 1; i < Math.abs(toX - fromX); i++)
            {
                if (board[fromX + i*offsetX][fromY].checkForPiece())
                {
                    return 0;
                }
            }

            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(queen);
            return 1;
        }
        else if (Math.abs(toX - fromX) == Math.abs(toY - fromY))
        {
            int offsetX;
            int offsetY;
            
            if (toX > fromX) {  offsetX = 1;  }
            else {  offsetX = -1;  }
            
            if (toY > fromY) {  offsetY = 1;  }
            else {  offsetY = -1;  }
            
            for (int i=1; i < Math.abs(toX-fromX); i++) {
                if (board[fromX+i*offsetX][fromY+i*offsetY].checkForPiece()) {
                  return 0;
                }
            }
            
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(queen);
            return 1;
        }
        return 0;
    }
    
    private int moveRook(int fromX, int fromY, int toX, int toY)
    {
        Rook rook = (Rook) board[fromX][fromY].getPiece();

        if ( toX == fromX )
        {
            int offsetY;
            if (toY > fromY) {  offsetY = 1;  }
            else {  offsetY = -1;  }
            
            for (int i = 1; i < Math.abs(toY - fromY); i++)
            {
                if (board[fromX][fromY + i*offsetY].checkForPiece())
                {
                    return 0;
                }
            }

            rook.setMoved();
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(rook);
            return 1;
        }
        else if (toY == fromY)
        {
            int offsetX;
            
            if (toX > fromX) {  offsetX = 1;  }
            else {  offsetX = -1;  }
            
            for (int i = 1; i < Math.abs(toX - fromX); i++)
            {
                if (board[fromX + i*offsetX][fromY].checkForPiece())
                {
                    return 0;
                }
            }

            rook.setMoved();
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(rook);
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
