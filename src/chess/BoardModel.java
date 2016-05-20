/*
 * This is the board model that controls the state of the chess board and where all the pieces are.
 */
package chess;

/**
 * @author Rio Velarde
 * @author Austin Thiel
 */
public class BoardModel {

    // The 2D array to store the board squares in.
    private final Square[][] board;

    // The constructor to initalize a new board.
    public BoardModel() {
        board = new Square[8][8];
        // Fills the board with Square objects (defined in Square.java).

        // The following simply puts all the pieces in their proper starting spaces on the board and assigns the proper color boolean to them.
        // True for white and false for black.
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                boolean color = (y >= 3);

                if (y == 1 || y == 6) {
                    board[x][y] = new Square();
                    board[x][y].setPiece(new Pawn(color));
                } else if (y > 1 && y < 6) {
                    board[x][y] = new Square(); // Empty Squares
                } else if (y == 0 || y == 7) {
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

    // An alternate constructor allows the creation of a model with the specified structure. Unused as of now.
    public BoardModel(Square[][] board) {
        this.board = board;
    }

    // This is the method the controller will call to send moves to the model.
    // Basically, it just checks to make sure a piece is not moving onto a piece of the same color
    // and then calls the proper method for the piece being moved.
    public int movePiece(int fromX, int fromY, int toX, int toY) {
        if (board[toX][toY].checkForPiece()) {
            if (board[fromX][fromY].getPiece().getColor() == board[toX][toY].getPiece().getColor()) {
                return 0;
            }
        }

        if (board[fromX][fromY].getPiece() instanceof Pawn) {
            return movePawn(fromX, fromY, toX, toY);
        } else if (board[fromX][fromY].getPiece() instanceof Rook) {
            return moveRook(fromX, fromY, toX, toY);
        } else if (board[fromX][fromY].getPiece() instanceof Knight) {
            return moveKnight(fromX, fromY, toX, toY);
        } else if (board[fromX][fromY].getPiece() instanceof Bishop) {
            return moveBishop(fromX, fromY, toX, toY);
        } else if (board[fromX][fromY].getPiece() instanceof King) {
            return moveKing(fromX, fromY, toX, toY);
        } else if (board[fromX][fromY].getPiece() instanceof Queen) {
            return moveQueen(fromX, fromY, toX, toY);
        }

        return 0;
    }
    // 0 is returned if the move is not allowed and a positive number is returned if the move was successful.

    // Checks if the move meets one of the valid move types for a pawn and updates the model if so.
    private int movePawn(int fromX, int fromY, int toX, int toY) {
        int offset;

        // Casts the piece from the board into a pawn object to test for valid movement.
        Pawn pawn = (Pawn) board[fromX][fromY].getPiece();

        if (pawn.getColor()) {
            offset = 1;
        } else {
            offset = -1;
        }
        if ((toY == fromY - offset && ((toX == fromX && !board[toX][toY].checkForPiece()) || (board[toX][toY].checkForPiece() && (toX == fromX - 1 || toX == fromX + 1)))) || (!pawn.checkIfMoved() && toY == fromY - (2 * offset))) {
            pawn.setMoved();
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(pawn);

            return 1;
        }

        return 0;
    }

    private int moveKing(int fromX, int fromY, int toX, int toY) {
        King king = (King) board[fromX][fromY].getPiece();
        if ((Math.abs(toX - fromX) == 1 || Math.abs(toX - fromX) == 0) && (Math.abs(toY - fromY) == 1 || Math.abs(toY - fromY) == 0)) {
            king.setMoved(); // If the move was valid, states in a variable that the king has moved. Castling is no longer possible.
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(king);
            return 1;

            // Checks for the four valid castling possibilities if the king has yet to move and the corresponding rook has yet to move.
        } else if (!king.checkIfMoved() && Math.abs(toX - fromX) == 2 && ((fromY == 0 && toY == 0) || (fromY == 7 && toY == 7))) {
            if (toX - fromX > 0) {
                for (int i = 1; i < 3; i++) {
                    if (board[fromX + i][fromY].checkForPiece()) {
                        return 0;
                    }
                }
                if (board[toX + 1][fromY].checkForPiece()) {
                    if (board[toX + 1][fromY].getPiece() instanceof Rook) {
                        Rook rook = (Rook) board[toX + 1][fromY].getPiece();
                        if (!rook.checkIfMoved()) {
                            king.setMoved();
                            board[fromX][fromY].setPiece();
                            board[toX][toY].setPiece(king);
                            rook.setMoved();
                            board[toX + 1][toY].setPiece();
                            board[toX - 1][toY].setPiece(rook);
                            return 2;
                        }
                    }
                }
            } else {
                for (int i = 1; i < 4; i++) {
                    if (board[fromX - i][fromY].checkForPiece()) {
                        return 0;
                    }
                }
                if (board[toX - 2][fromY].checkForPiece()) {
                    if (board[toX - 2][fromY].getPiece() instanceof Rook) {
                        Rook rook = (Rook) board[toX - 2][fromY].getPiece();
                        if (!rook.checkIfMoved()) {
                            king.setMoved();
                            board[fromX][fromY].setPiece();
                            board[toX][toY].setPiece(king);
                            rook.setMoved();
                            board[toX - 2][toY].setPiece();
                            board[toX + 1][toY].setPiece(rook);
                            return 2;
                        }
                    }
                    return 0;
                }
            }
        }
        return 0;
    }

    // Checks if a knight move is valid.
    private int moveKnight(int fromX, int fromY, int toX, int toY) {
        Knight knight = (Knight) board[fromX][fromY].getPiece();
        if ((Math.abs(toX - fromX) == 2 && Math.abs(toY - fromY) == 1) || (Math.abs(toY - fromY) == 2 && Math.abs(toX - fromX) == 1)) {
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(knight);
            return 1;
        }

        return 0;
    }

    // Checks if a bishop move is valid.
    private int moveBishop(int fromX, int fromY, int toX, int toY) {
        Bishop bishop = (Bishop) board[fromX][fromY].getPiece();
        if (Math.abs(toX - fromX) == Math.abs(toY - fromY)) {
            int offsetX;
            int offsetY;

            if (toX > fromX) {
                offsetX = 1;
            } else {
                offsetX = -1;
            }

            if (toY > fromY) {
                offsetY = 1;
            } else {
                offsetY = -1;
            }

            for (int i = 1; i < Math.abs(toX - fromX); i++) {
                if (board[fromX + i * offsetX][fromY + i * offsetY].checkForPiece()) {
                    return 0;
                }
            }

            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(bishop);
            return 1;
        }

        return 0;
    }

    // Checks if a Rook move is valid.
    private int moveRook(int fromX, int fromY, int toX, int toY) {
        Rook rook = (Rook) board[fromX][fromY].getPiece();

        if (toX == fromX) {
            int offsetY;
            if (toY > fromY) {
                offsetY = 1;
            } else {
                offsetY = -1;
            }

            for (int i = 1; i < Math.abs(toY - fromY); i++) {
                if (board[fromX][fromY + i * offsetY].checkForPiece()) {
                    return 0;
                }
            }

            rook.setMoved();
            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(rook);
            return 1;
        } else if (toY == fromY) {
            int offsetX;

            if (toX > fromX) {
                offsetX = 1;
            } else {
                offsetX = -1;
            }

            for (int i = 1; i < Math.abs(toX - fromX); i++) {
                if (board[fromX + i * offsetX][fromY].checkForPiece()) {
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

    // Checks if a Queen move is valid (just copies and puts together the rook and bishop movement code.)
    private int moveQueen(int fromX, int fromY, int toX, int toY) {
        Queen queen = (Queen) board[fromX][fromY].getPiece();

        if (toX == fromX) {
            int offsetY;
            if (toY > fromY) {
                offsetY = 1;
            } else {
                offsetY = -1;
            }

            for (int i = 1; i < Math.abs(toY - fromY); i++) {
                if (board[fromX][fromY + i * offsetY].checkForPiece()) {
                    return 0;
                }
            }

            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(queen);
            return 1;
        } else if (toY == fromY) {
            int offsetX;

            if (toX > fromX) {
                offsetX = 1;
            } else {
                offsetX = -1;
            }

            for (int i = 1; i < Math.abs(toX - fromX); i++) {
                if (board[fromX + i * offsetX][fromY].checkForPiece()) {
                    return 0;
                }
            }

            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(queen);
            return 1;
        } else if (Math.abs(toX - fromX) == Math.abs(toY - fromY)) {
            int offsetX;
            int offsetY;

            if (toX > fromX) {
                offsetX = 1;
            } else {
                offsetX = -1;
            }

            if (toY > fromY) {
                offsetY = 1;
            } else {
                offsetY = -1;
            }

            for (int i = 1; i < Math.abs(toX - fromX); i++) {
                if (board[fromX + i * offsetX][fromY + i * offsetY].checkForPiece()) {
                    return 0;
                }
            }

            board[fromX][fromY].setPiece();
            board[toX][toY].setPiece(queen);
            return 1;
        }
        return 0;
    }

    // A publicly accessible method to print the board when called.
    public void printBoard() {
        System.out.println("-------------");
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[x][y].checkForPiece()) {
                    System.out.print(board[x][y].getPiece().getIdentifier());
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println("-------------");
    }
}
