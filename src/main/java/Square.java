import javax.swing.*;
import java.util.Objects;

/**
 * The square class represents a clickable button containing an image to represent a tile on a draughts
 * board and whether it contains a piece or no
 *  squareWhite is the imageIcon for an empty white square.
 *  squareBlack is the imageIcon for an empty black square.
 *  tileWhite is the imageIcon for a white piece.
 *  tileRed is the imageIcon for a red piece.
 *  kingWhite is the imageIcon for a king white piece.
 *  kingRed is the imageIcon for a king red piece.
 *  squareSelected is the imageIcon for a yellow/highlighted square.
 *  squareButton is the button that the user clicks, and contains an image of the above options.
 *  xPosition is the xPosition of the square.
 *  yPosition is the yPosition of the square.
 *  currentPiece is a string to represent the state of the square and what it contains.
 *  colour is a string to represent the colour of the square. The colour is static and cannot be changed.
 */
public class Square 
{
    private ImageIcon squareWhite = new ImageIcon("src/main/resources/empty.png");
    private ImageIcon squareBlack = new ImageIcon("src/main/resources/empty2.png");
    private ImageIcon tileWhite = new ImageIcon("src/main/resources/white.png");
    private ImageIcon tileRed = new ImageIcon("src/main/resources/red.png");
    private ImageIcon kingWhite = new ImageIcon("src/main/resources/white-king.png");
    private ImageIcon kingRed = new ImageIcon("src/main/resources/red-king.png");
    private ImageIcon squareSelected = new ImageIcon("src/main/resources/selected.png");
    private JButton squareButton = new JButton(squareWhite);
    private int xPosition=0;
    private int yPosition=0;
    private Piece currentPiece;
    private String colour; //makes it easier to know what colour the square tile is

    /**
     * Creates a square based on parameters inputted by the user
     * @param y Integer of the y Position of the square
     * @param x Integer of x Position of the square
     * @param c String for the state and currentPiece a square holds. WHITE = white piece. RED = red piece. NONE = no piece. SELECTED = highlighted tile in which a valid move can be made to.
     * @param p The colour of the square. WHITE or BLACK.
     */
    public Square(int y, int x, String c, Piece p) //initialises piece in correct starting position
    {
        xPosition = x;
        yPosition = y;
        currentPiece = p;
        colour = c;
        switch (colour) {
            case "WHITE":
                switch (currentPiece) {
                    case WHITE:
                        squareButton.setIcon(tileWhite);
                        break;
                    case RED:
                        squareButton.setIcon(tileRed);
                        break;
                    case EMPTY:
                        squareButton.setIcon(squareWhite);
                        break;
                }
                break;
            case "BLACK":
                squareButton.setIcon(squareBlack);
                break;
        }
    }
    /**
     * @return the current yPosition.
     */
    public int getYPos()
    {
        return yPosition;
    }

    /**
     * @return the current xPosition.
     */
    public int getXPos()
        {
            return xPosition;
        }

    /**
     *
     * @return the button located on a square.
     */
    public JButton getButton()
    {
        return squareButton;
    }

    /**
     *
     * @return string of the current colour on a square.
     */
    public String getColour()
    {
        return colour;
    }

    /**
     * @return a string of the current piece on a square.
     */
    public Piece getCurrentPiece()
    {
        return currentPiece;
    }

    /**
     *
     * @param piece A string to change the current piece. WHITE = white piece. NONE = no piece. RED = red piece. SELECTED = selected piece. KINGWHITE = king white piece. KINGRED = king red piece.
     */

    public void setCurrentPiece(Piece piece)
    {
        switch (piece) {
            case EMPTY:
                this.squareButton.setIcon(squareWhite);
                currentPiece = Piece.EMPTY;
                break;
            case WHITE:
                this.squareButton.setIcon(tileWhite);
                currentPiece = Piece.WHITE;
                break;
            case RED:
                this.squareButton.setIcon(tileRed);
                currentPiece = Piece.RED;
                break;
            case SELECTED:
                this.squareButton.setIcon(squareSelected);
                currentPiece = Piece.SELECTED;
                break;
            case KINGWHITE:
                this.squareButton.setIcon(kingWhite);
                currentPiece = Piece.KINGWHITE;
                break;
            case KINGRED:
                this.squareButton.setIcon(kingRed);
                currentPiece = Piece.KINGRED;
                break;
            default:
                break;
        }
    }
    /**
    * Returns true if move if one step to the left / right
    * @return A bool value depending on whether the move is valid or not
    *
     */
    public boolean moveToUniversal(Square next) {
        return next.getXPos() == this.getXPos() + 1 || next.getXPos() == this.getXPos() - 1;
    }

    public boolean squareIsSelected() {
        return this.getCurrentPiece() == Piece.SELECTED;
    }

    @Override
    public String toString() {
        return "Square: " + yPosition + "|" + xPosition;
    }
}