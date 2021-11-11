import javax.swing.*;
import java.awt.*;
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
    private JPanel boardPanel = new JPanel();
    private GridLayout gridBoard = new GridLayout(8,8);
    private JFrame board = new JFrame();
    private Square[][] square = new Square[8][8];
    private int currentX =-1;
    private int currentY =-1;
    /**
     *
     * Initialises an 8x8 board followed by adding 64 squares in the relevant
     * position for a draughts game.
     */
    public Board() {
        setUpGameBoard();
    }

    public void setUpGameBoard() {
        setUpUI();
        setUpBoard();
        setBoardVisible();
    }

    public void setBoardVisible() {
        board.setContentPane(boardPanel); //sets the panel to the board frame so the board is correctly initialised
        board.setVisible(true); //sets the board visible
    }

    public void setUpUI(){
        board.setSize(800,800); //size of board. image is 100x100 and 8x8 layout so 800x800
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //when closed program shuts down
        boardPanel.setLayout(gridBoard); //sets the 8x8 board layout. an added component is arranged in 8x8 layout
    }

    public void setUpBoard(){
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                //Set square[y][x] to something we choose
                square[y][x] = getStartingSquare(y, x);
                square[y][x].getButton().addActionListener(this);
                boardPanel.add(square[y][x].getButton());
            }
        }
    }

    public Square getStartingSquare(int yPos, int xPos) {
        boolean evenSquare = (yPos + xPos) % 2 == 0;
        if(evenSquare) {
            return getStartingSquareContainingPiece(yPos, xPos);
        }
        else {
            return new Square(yPos, xPos, "BLACK", "NONE");
        }
    }

    public Square getStartingSquareContainingPiece(int yPos, int xPos) {
        if (yPos < 3)
            return new Square(yPos, xPos, "WHITE", "RED");
        else if (yPos > 4)
            return new Square(yPos, xPos, "WHITE", "WHITE");
        else
            return new Square(yPos, xPos, "WHITE", "NONE");
    }

    public Square[][] getSquare(){
        return square;
    }

    public void clearSelected() {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(square[y][x].getCurrentPiece().equals("SELECTED")){
                    square[y][x].setCurrentPiece("NONE");
                }
            }
        }
    }

    public boolean canTakePiece(String currentPiece, Square next) {
        if(currentPiece.equals("WHITE") || currentPiece.equals("KINGWHITE"))
            return next.getCurrentPiece().equals("RED") || next.getCurrentPiece().equals("KINGRED");
        else if(currentPiece.equals("RED") || currentPiece.equals("KINGRED"))
            return next.getCurrentPiece().equals("WHITE") || next.getCurrentPiece().equals("KINGWHITE");
        return false;
    }

    public boolean validTurn() {
        if(whiteTurn)
            return square[currentY][currentX].getCurrentPiece().equals("WHITE") || square[currentY][currentX].getCurrentPiece().equals("KINGWHITE");
        else
            return square[currentY][currentX].getCurrentPiece().equals("RED") || square[currentY][currentX].getCurrentPiece().equals("KINGRED");
    }

    public void markSelectableSquares(Square current, Square next, String currentPiece)
    {
        if((currentPiece.equals("WHITE") || currentPiece.equals("KINGRED")))
        {
            if (next.getCurrentPiece().equals("NONE")) {
                next.setCurrentPiece("SELECTED");
            }
            else if(canTakePiece(currentPiece, next)) {
                if (next.getXPos() > current.getXPos()) {
                    int nextX = next.getXPos() + 1;
                    int nextY = next.getYPos() - 1;
                    if (nextX < 8 && nextY >= 0)
                        markSelectableSquares(next, square[nextY][nextX], currentPiece);
                }
                else if(next.getXPos() < current.getXPos()){
                    int nextX = next.getXPos() - 1;
                    int nextY = next.getYPos() - 1;
                    if (nextX >= 0 && nextY >= 0)
                        markSelectableSquares(next, square[nextY][nextX], currentPiece);
                }
            }
        }
        else if((currentPiece.equals("RED") || currentPiece.equals("KINGWHITE")))
        {
            if (next.getCurrentPiece().equals("NONE")) {
                next.setCurrentPiece("SELECTED");
            }
            else if(canTakePiece(currentPiece, next)) {
                if(next.getXPos() > current.getXPos()) {
                    int nextX = next.getXPos() + 1;
                    int nextY = next.getYPos() + 1;
                    if(nextX < 8 && nextY < 8)
                        markSelectableSquares(next, square[nextY][nextX], currentPiece);
                }
                else if(next.getXPos() < current.getXPos()) {
                    int nextX = next.getXPos() - 1;
                    int nextY = next.getYPos() + 1;
                    if(nextX >= 0 && nextY < 8)
                        markSelectableSquares(next, square[nextY][nextX], currentPiece);
                }
            }
        }
    }

    public void getMoveOptions() {
        if(validTurn()) {
            int yDirection = 1;
            if(whiteTurn && square[currentY][currentX].getCurrentPiece().equals("WHITE") || !whiteTurn && square[currentY][currentX].getCurrentPiece().equals("KINGRED")) {
                yDirection = -yDirection;
            }
            int yToCheck = currentY + yDirection;
            int xToCheck = currentX - 1;    //Check in left direction
            if(xToCheck >= 0)
                markSelectableSquares(square[currentY][currentX], square[yToCheck][xToCheck], square[currentY][currentX].getCurrentPiece());
            xToCheck = currentX + 1;    //Check in right direction
            if(xToCheck <= 7)
                markSelectableSquares(square[currentY][currentX], square[yToCheck][xToCheck], square[currentY][currentX].getCurrentPiece());
        }
    }

    public void playMove(int moveFromY, int moveFromX, int moveToY, int moveToX) {
        int piecesToTake = Math.abs(moveFromX - moveToX) - 1;
        String pieceToMove = square[moveFromY][moveFromX].getCurrentPiece();
        if(piecesToTake >= 0) {
            for(int i = piecesToTake; i > 0; i --) {
                //RIGHT MOVE
                if(moveFromX - moveToX < 0) {
                    //RIGHT MOVE DOWNWARDS
                    if(moveFromY < moveToY) {
                        square[moveFromY + i][moveFromX + i].setCurrentPiece("NONE");
                    }
                    //RIGHT MOVE UPWARDS
                    else {
                        square[moveFromY - i][moveFromX + i].setCurrentPiece("NONE");
                    }
                }
                //LEFT MOVE
                else{
                    //LEFT MOVE DOWNWARDS
                    if(moveFromY < moveToY) {
                        square[moveFromY + i][moveFromX - i].setCurrentPiece("NONE");
                    }
                    //LEFT MOVE UPWARDS
                    else {
                        square[moveFromY - i][moveFromX - i].setCurrentPiece("NONE");
                    }
                }
            }
        }
        square[moveFromY][moveFromX].setCurrentPiece("NONE");
        if(pieceToMove.equals("WHITE") && moveToY == 0)
            square[moveToY][moveToX].setCurrentPiece("KINGWHITE");
        else if(pieceToMove.equals("RED") && moveToY == 7)
            square[moveToY][moveToX].setCurrentPiece("KINGRED");
        else
            square[moveToY][moveToX].setCurrentPiece(pieceToMove);
        whiteTurn = !whiteTurn;
    }

    public void actionPerformed(ActionEvent e) {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(square[y][x].getButton()==e.getSource()) {
                    if(square[y][x].getCurrentPiece().equals("SELECTED")) {
                        this.playMove(currentY, currentX, y, x);//If clicked we want to make a move if current piece is selected.
                        clearSelected();
                    }
                    else{
                        clearSelected();
                        currentY = y;
                        currentX = x;
                        getMoveOptions();
                    }
                }
            }
        }
    }

}