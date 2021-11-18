import java.awt.event.*;

/**
* The Board class produces an empty draughts board, if the square class contains values will
* fill the board with those squares. The class implements an action listener for any tile
* found on the board, linking to a button from the square class. This can move pieces
* across the board.
*  boardPanel represents the panel to add a square.
*  gridBoard represents the grid layout of the board to match a typical draughts board.
*  square represents an instance of the square class, there are as many squares as options on the grid.
*  board represents the JFrame of the board, the background for the panel to be added to.
*  currentX represents the XPosition of a 'clicked' square on the board.
*  currentY represents the YPosition of a 'clicked' square on the board.
*  jumpY represents YPosition of an opposing piece where a jump is available.
*  jumpX represents XPosition of an opposing piece where a jump is available.
*  click represents the no. clicks the user has made on the board.
*  <turn represents an int for the turn. 0 is currently white turn and 1 is red.
 **/
public class Board implements ActionListener{ //allows the class to be setup for an Action listener

    private boolean whiteTurn = true;
    private Square[][] square = new Square[8][8];
    private int currentX = -1;
    private int currentY = -1;
    private BoardView view = new BoardView();
    /**
     *
     * Initialises an 8x8 board followed by adding 64 squares in the relevant
     * position for a draughts game.
     */
    public Board() {
        setUpGameBoard();
    }

    public void setUpGameBoard() {
        setUpBoard();
        view.setBoardVisible();
    }

    public void setUpBoard(){
        for(int y = 0; y < square.length; y++) {
            for(int x = 0; x < square.length; x++) {
                //Set square[y][x] to something we choose
                square[y][x] = getStartingSquare(y, x);
                square[y][x].getButton().addActionListener(this);
                view.addBoardPanel(square[y][x].getButton());
            }
        }
    }

    public Square getStartingSquare(int yPos, int xPos) {
        boolean evenSquare = (yPos + xPos) % 2 == 0;
        if(evenSquare) {
            return getStartingSquareContainingPiece(yPos, xPos);
        }
        else {
            return new Square(yPos, xPos, "BLACK", Piece.EMPTY);
        }
    }

    public Square getStartingSquareContainingPiece(int yPos, int xPos) {
        if (yPos < (square.length / 2) - 1)
            return new Square(yPos, xPos, "WHITE", Piece.RED);
        else if (yPos > square.length / 2)
            return new Square(yPos, xPos, "WHITE", Piece.WHITE);
        else
            return new Square(yPos, xPos, "WHITE", Piece.EMPTY);
    }

    public Square[][] getSquare(){
        return square;
    }

    public void clearSelected() {
        for (Square[] squares : square) {
            for (int x = 0; x < square.length; x++) {
                if (squares[x].getCurrentPiece() == Piece.SELECTED) {
                    squares[x].setCurrentPiece(Piece.EMPTY);
                }
            }
        }
    }
    
    public boolean pieceCanKeepJumping(Piece currentPiece, Square next) {
        if(currentPiece == Piece.WHITE || currentPiece == Piece.KINGWHITE)
            return next.getCurrentPiece() == Piece.RED || next.getCurrentPiece() == Piece.KINGRED;
        else if(currentPiece == Piece.RED || currentPiece == Piece.KINGRED)
            return next.getCurrentPiece() == Piece.WHITE || next.getCurrentPiece() == Piece.KINGWHITE;
        return false;
    }

    public boolean validTurn() {
        if(whiteTurn)
            return square[currentY][currentX].getCurrentPiece() == Piece.WHITE || square[currentY][currentX].getCurrentPiece() == Piece.KINGWHITE;
        else
            return square[currentY][currentX].getCurrentPiece() == Piece.RED || square[currentY][currentX].getCurrentPiece() == Piece.KINGRED;
    }

    public void highlightAvailableMoves(Square current, Square next, Piece currentPiece) {
        boolean moveUpwards = false;
        if((currentPiece == Piece.WHITE || currentPiece == Piece.KINGRED))
            moveUpwards = true;
        else if((currentPiece == Piece.RED || currentPiece == Piece.KINGWHITE))
            moveUpwards = false;
        else
            return;
        highlightMoveIfEmpty(next);
        if(pieceCanKeepJumping(currentPiece, next)) {
            highlightAvailableMovesWhilstJumping(current, next, currentPiece, moveUpwards);
        }
    }

    public void highlightMoveIfEmpty(Square pieceToCheck) {
        if (pieceToCheck.getCurrentPiece() == Piece.EMPTY) {
            pieceToCheck.setCurrentPiece(Piece.SELECTED);
        }
    }

    public void highlightAvailableMovesWhilstJumping(Square current, Square next, Piece currentPiece, boolean moveUpwards) {
        if (next.getXPos() > current.getXPos()) {
            int nextX = next.getXPos() + 1;
            if(moveUpwards) {
                int nextY = next.getYPos() - 1;
                if (nextX < square.length && nextY >= 0)
                    highlightAvailableMoves(next, square[nextY][nextX], currentPiece);
            }
            else {
                int nextY = next.getYPos() + 1;
                if(nextX < 8 && nextY < square.length)
                    highlightAvailableMoves(next, square[nextY][nextX], currentPiece);
            }
        }
        else if(next.getXPos() < current.getXPos()){
            int nextX = next.getXPos() - 1;
            if(moveUpwards) {
                int nextY = next.getYPos() - 1;
                if (nextX >= 0 && nextY >= 0)
                    highlightAvailableMoves(next, square[nextY][nextX], currentPiece);
            }
            else {
                int nextY = next.getYPos() + 1;
                if(nextX >= 0 && nextY < square.length)
                    highlightAvailableMoves(next, square[nextY][nextX], currentPiece);
            }
        }
    }

    public void getMoveOptions() {
        if(validTurn()) {
            int yDirection = 1;
            if(whiteTurn && square[currentY][currentX].getCurrentPiece() == Piece.WHITE || !whiteTurn && square[currentY][currentX].getCurrentPiece() == Piece.KINGRED) {
                yDirection = -yDirection;
            }
            int yToCheck = currentY + yDirection;
            int xToCheck = currentX - 1;    //Check in left direction
            if(xToCheck >= 0)
                highlightAvailableMoves(square[currentY][currentX], square[yToCheck][xToCheck], square[currentY][currentX].getCurrentPiece());
            xToCheck = currentX + 1;    //Check in right direction
            if(xToCheck <= square.length - 1)
                highlightAvailableMoves(square[currentY][currentX], square[yToCheck][xToCheck], square[currentY][currentX].getCurrentPiece());
        }
    }

    //A move is valid at this point in the game board so we are making the move

    public void playMove(int moveFromY, int moveFromX, int moveToY, int moveToX) {
        int piecesToTake = Math.abs(moveFromX - moveToX) - 1;
        Piece pieceToMove = square[moveFromY][moveFromX].getCurrentPiece();
        if(piecesToTake >= 0) {
            for(int i = piecesToTake; i > 0; i --) {
                //RIGHT MOVE
                if(moveFromX - moveToX < 0) {
                    //RIGHT MOVE DOWNWARDS
                    if(moveFromY < moveToY) {
                        square[moveFromY + i][moveFromX + i].setCurrentPiece(Piece.EMPTY);
                    }
                    //RIGHT MOVE UPWARDS
                    else {
                        square[moveFromY - i][moveFromX + i].setCurrentPiece(Piece.EMPTY);
                    }
                }
                //LEFT MOVE
                else{
                    //LEFT MOVE DOWNWARDS
                    if(moveFromY < moveToY) {
                        square[moveFromY + i][moveFromX - i].setCurrentPiece(Piece.EMPTY);
                    }
                    //LEFT MOVE UPWARDS
                    else {
                        square[moveFromY - i][moveFromX - i].setCurrentPiece(Piece.EMPTY);
                    }
                }
            }
        }
        square[moveFromY][moveFromX].setCurrentPiece(Piece.EMPTY);
        if(pieceToMove == Piece.WHITE && moveToY == 0)
            square[moveToY][moveToX].setCurrentPiece(Piece.KINGWHITE);
        else if(pieceToMove == Piece.RED && moveToY == square.length - 1)
            square[moveToY][moveToX].setCurrentPiece(Piece.KINGRED);
        else
            square[moveToY][moveToX].setCurrentPiece(pieceToMove);
        whiteTurn = !whiteTurn;
    }

    public void actionPerformed(ActionEvent e) {
        for(int y = 0; y < square.length; y++) {
            for(int x = 0; x < square.length; x++) {
                if(square[y][x].getButton()==e.getSource()) {
                    if(square[y][x].squareIsSelected()) {
                        this.playMove(currentY, currentX, y, x);//If clicked we want to make a move if current piece is selected.
                        clearSelected();
                    }
                    else{
                        clearSelected();
                        currentY = y;
                        currentX = x;
                        getMoveOptions();
                    } //End of checking moves
                }   //End of finding square clicked
            }   //End of x co-ordinate loop
        }   //End of y-coordinate loop
    }   //End of function

}