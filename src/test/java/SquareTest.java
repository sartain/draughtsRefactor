import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SquareTest {

    static Stream<Square> getPieces(int pieces) {
        final int BLANK = 1;
        final int WHITE = 2;
        final int RED = 3;
        ArrayList<Square> pieceList = new ArrayList<>();
        Square[][] board = new Board().getSquare();
        for(int y_pos = 0; y_pos < board[0].length; y_pos ++) {
            for (int x_pos = 0; x_pos < board.length; x_pos++) {
                if ((y_pos + x_pos) % 2 == 0) {
                    if (y_pos > 4 && pieces == WHITE) {
                        pieceList.add(board[y_pos][x_pos]);
                    }
                    else if (y_pos < 3 && pieces == RED) {
                        pieceList.add(board[y_pos][x_pos]);
                    }
                    else if (y_pos <= 4 && y_pos >= 3 && pieces == BLANK) {
                        pieceList.add(board[y_pos][x_pos]);
                    }
                }
                else if (pieces == BLANK){
                    pieceList.add(board[y_pos][x_pos]);
                }
            }
        }
        return pieceList.stream();
    }

    static Stream<Square> getWhitePieces() {
        return getPieces(2);
    }

    static Stream<Square> getRedPieces() {
        return getPieces(3);
    }

    static Stream<Square> getBlankPieces() {
        return getPieces(1);
    }

    //whiteSquare is true for white, false for black
    static Stream<Square> getSquares(boolean isWhiteSquare) {
        ArrayList<Square> squares = new ArrayList<>();
        Square[][] board = new Board().getSquare();
        for(int y_pos = 0; y_pos < board[0].length; y_pos ++) {
            for (int x_pos = 0; x_pos < board.length; x_pos++) {
                if ((y_pos + x_pos) % 2 == 0) {
                    if(isWhiteSquare)
                        squares.add(board[y_pos][x_pos]);
                }
                else{
                    if(!isWhiteSquare)
                        squares.add(board[y_pos][x_pos]);
                }
            }
        }
        return squares.stream();
    }

    static Stream<Square> getWhiteSquares() {
        return getSquares(true);
    }

    static Stream<Square> getBlackSquares() {
        return getSquares(false);
    }

    static Stream<List<Square>> getPieceWithMoveOptions(boolean redPiece, boolean twoMoves) {
        ArrayList<List<Square>> pieceWithOptions = new ArrayList<>();
        Square[][] board = new Board().getSquare();
        for(int y_pos = 0; y_pos < board[0].length; y_pos ++) {
            for (int x_pos = 0; x_pos < board.length; x_pos++) {
                Square[] moves = new Square[65];
                if ((y_pos + x_pos) % 2 == 0) {
                    if (y_pos == 2 && redPiece) {
                        moves[0] = board[y_pos][x_pos];
                        if(redHasTwoMoves(x_pos) && twoMoves){
                            int move1 = x_pos + 1;
                            int move2 = x_pos - 1;
                            moves[1] = board[y_pos + 1][move1];
                            moves[2] = board[y_pos + 1][move2];
                            moves = getValidAndInvalidMoveOptions(3, move1, move2, 3, moves, board);
                            pieceWithOptions.add(Arrays.asList(moves));
                        }
                        if(redHasOneMove(x_pos) && !twoMoves){
                            int move1 = x_pos + 1;
                            moves[1] = board[y_pos + 1][move1];
                            moves = getValidAndInvalidMoveOptions(3, move1, move1, 2, moves, board);
                            pieceWithOptions.add(Arrays.asList(moves));
                        }
                    }
                    else if (y_pos == 5 && !redPiece) {
                        moves[0] = board[y_pos][x_pos];
                        if(whiteHasTwoMoves(x_pos) && twoMoves){
                            int move1 = x_pos + 1;
                            int move2 = x_pos - 1;
                            moves[1] = board[y_pos - 1][move1];
                            moves[2] = board[y_pos - 1][move2];
                            moves = getValidAndInvalidMoveOptions(4, move1, move2, 3, moves, board);
                            pieceWithOptions.add(Arrays.asList(moves));
                        }
                        if(whiteHasOneMove(x_pos) && !twoMoves){
                            int move1 = x_pos - 1;
                            moves[1] = board[y_pos - 1][move1];
                            moves = getValidAndInvalidMoveOptions(4, move1, move1, 2, moves, board);
                            pieceWithOptions.add(Arrays.asList(moves));
                        }
                    }
                }
            }
        }
        return pieceWithOptions.stream();
    }

    static Square[] getValidAndInvalidMoveOptions(int yToCheck, int xToCheck1, int xToCheck2, int indexStartFrom, Square[] currentSquares, Square[][] board) {
        int indexStart = indexStartFrom;
        for(int i = 0; i < 8; i ++) {
            for(int j = 0; j < 8; j ++) {
                if(yToCheck == i){
                    if (j != xToCheck1 && j != xToCheck2) {
                        currentSquares[indexStart] = board[i][j];
                        indexStart++;
                    }
                }
                else{
                    currentSquares[indexStart] = board[i][j];
                    indexStart++;
                }
            }
        }
        return currentSquares;
    }

    static Stream<List<Square>> getRedTwoMoveOptions() {
        return getPieceWithMoveOptions(true, true);
    }

    static Stream<List<Square>> getWhiteTwoMoveOptions() {
        return getPieceWithMoveOptions(false, true);
    }

    static Stream<List<Square>> getRedOneMoveOptions() {
        return getPieceWithMoveOptions(true, false);
    }

    static Stream<List<Square>> getWhiteOneMoveOptions() {
        return getPieceWithMoveOptions(false, false);
    }

    static boolean redHasTwoMoves(int xPos) {
        int[] possibleMoves = {2, 4, 6};
        for (int move : possibleMoves) {
            if (move == xPos)
                return true;
        }
        return false;
    }

    static boolean redHasOneMove(int xPos) {
        return xPos == 0;
    }

    static boolean whiteHasTwoMoves(int xPos) {
        int[] possibleMoves = {1, 3, 5};
        for (int move : possibleMoves) {
            if (move == xPos)
                return true;
        }
        return false;
    }

    static boolean whiteHasOneMove(int xPos) {
        return xPos == 7;
    }
    @ParameterizedTest
    @MethodSource("getWhitePieces")
    public void testPieceIsWhite(Square squareToTest) {
        assertEquals(Piece.WHITE, squareToTest.getCurrentPiece());
    }

    @ParameterizedTest
    @MethodSource("getRedPieces")
    public void testPieceIsRed(Square squareToTest) {
        assertEquals(Piece.RED, squareToTest.getCurrentPiece());
    }

    @ParameterizedTest
    @MethodSource("getBlankPieces")
    public void testPieceIsNone(Square squareToTest) {
        assertEquals(Piece.EMPTY, squareToTest.getCurrentPiece());
    }

    @ParameterizedTest
    @MethodSource("getBlackSquares")
    public void testSquareIsBlack(Square squareToTest) {
        assertEquals("BLACK", squareToTest.getColour());
    }

    @ParameterizedTest
    @MethodSource("getWhiteSquares")
    public void testSquareIsWhite(Square squareToTest) {
        assertEquals("WHITE", squareToTest.getColour());
    }

    //Used purely for testing purposes
    public boolean canMoveToNoSwitch(Square current, Square next)
    {
        if((current.getCurrentPiece() == Piece.WHITE || current.getCurrentPiece() == Piece.KINGRED) && next.getCurrentPiece() == Piece.EMPTY)
        {
            if(next.getYPos()==current.getYPos()-1)
            {
                return current.moveToUniversal(next);
            }
        }

        else if((current.getCurrentPiece() == Piece.RED || current.getCurrentPiece() == Piece.KINGWHITE) && next.getCurrentPiece() == Piece.EMPTY)
        {
            if(next.getYPos()==current.getYPos()+1)
            {
                return current.moveToUniversal(next);
            }
        }
        else
        {
            return false;
        }
        return false;
    }

    @ParameterizedTest
    @MethodSource("getRedTwoMoveOptions")
    public void testRedCanMoveTwoSpaces(List<Square> squareAndMoves) {
        Square currentSquare = squareAndMoves.get(0);
        Square potentialMove_1 = squareAndMoves.get(1);
        Square potentialMove_2 = squareAndMoves.get(2);
        assertTrue(canMoveToNoSwitch(currentSquare, potentialMove_1));
        assertTrue(canMoveToNoSwitch(currentSquare, potentialMove_2));
        for(int i = 3; i < squareAndMoves.size(); i ++){
            assertFalse(canMoveToNoSwitch(currentSquare, squareAndMoves.get(i)));
        }
    }

    @ParameterizedTest
    @MethodSource("getRedOneMoveOptions")
    public void testRedCanMoveOneSpaces(List<Square> squareAndMoves) {
        Square currentSquare = squareAndMoves.get(0);
        Square potentialMove_1 = squareAndMoves.get(1);
        assertTrue(canMoveToNoSwitch(currentSquare, potentialMove_1));
        for(int i = 2; i < squareAndMoves.size(); i ++){
            assertFalse(canMoveToNoSwitch(currentSquare, squareAndMoves.get(i)));
        }
    }

    @ParameterizedTest
    @MethodSource("getWhiteOneMoveOptions")
    public void testWhiteCanMoveOneSpaces(List<Square> squareAndMoves) {
        Square currentSquare = squareAndMoves.get(0);
        Square potentialMove_1 = squareAndMoves.get(1);
        assertTrue(canMoveToNoSwitch(currentSquare, potentialMove_1));
        for(int i = 2; i < squareAndMoves.size(); i ++){
            assertFalse(canMoveToNoSwitch(currentSquare, squareAndMoves.get(i)));
        }
    }

    @ParameterizedTest
    @MethodSource("getWhiteTwoMoveOptions")
    public void testWhiteCanMoveTwoSpaces(List<Square> squareAndMoves) {
        Square currentSquare = squareAndMoves.get(0);
        Square potentialMove_1 = squareAndMoves.get(1);
        Square potentialMove_2 = squareAndMoves.get(2);
        assertTrue(canMoveToNoSwitch(currentSquare, potentialMove_1));
        assertTrue(canMoveToNoSwitch(currentSquare, potentialMove_2));
        for(int i = 3; i < squareAndMoves.size(); i ++) {
            assertFalse(canMoveToNoSwitch(currentSquare, squareAndMoves.get(i)));
        }
    }

    @ParameterizedTest
    @MethodSource("getWhiteOneMoveOptions")
    public void testOptionTileBecomesSelected(List<Square> squareAndMoves) {
        Square currentSquare = squareAndMoves.get(0);
        Square potentialMove_1 = squareAndMoves.get(1);
        Board board = new Board();
        JButton moveFromButton = board.getSquare()[currentSquare.getYPos()][currentSquare.getXPos()].getButton();
        ActionEvent clickCurrentSquare = new ActionEvent(moveFromButton, 1, "click");
        board.actionPerformed(clickCurrentSquare);
        Square moveFrom = board.getSquare()[currentSquare.getYPos()][currentSquare.getXPos()];
        Square moveTo = board.getSquare()[potentialMove_1.getYPos()][potentialMove_1.getXPos()];
        assertEquals(Piece.WHITE, moveFrom.getCurrentPiece());
        assertEquals(Piece.SELECTED, moveTo.getCurrentPiece());
    }

    @ParameterizedTest
    @MethodSource("getWhiteOneMoveOptions")
    public void testMoveIsMade(List<Square> squareAndMoves) {
        Square currentSquare = squareAndMoves.get(0);
        Square potentialMove_1 = squareAndMoves.get(1);
        Board board = new Board();
        JButton moveFromButton = board.getSquare()[currentSquare.getYPos()][currentSquare.getXPos()].getButton();
        JButton moveToButton = board.getSquare()[potentialMove_1.getYPos()][potentialMove_1.getXPos()].getButton();
        ActionEvent clickCurrentSquare = new ActionEvent(moveFromButton, 1, "click");
        board.actionPerformed(clickCurrentSquare);
        ActionEvent clickSquareToMoveTo = new ActionEvent(moveToButton, 1, "click");
        board.actionPerformed(clickSquareToMoveTo);
        Square moveFrom = board.getSquare()[currentSquare.getYPos()][currentSquare.getXPos()];
        Square moveTo = board.getSquare()[potentialMove_1.getYPos()][potentialMove_1.getXPos()];
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.WHITE, moveTo.getCurrentPiece());
    }

    //Cannot play red move before a white move (turn based game)
    @ParameterizedTest
    @MethodSource("getRedOneMoveOptions")
    public void testMoveCannotBeMade(List<Square> squareAndMoves) {
        Square currentSquare = squareAndMoves.get(0);
        Square potentialMove_1 = squareAndMoves.get(1);
        Board board = new Board();
        JButton moveFromButton = board.getSquare()[currentSquare.getYPos()][currentSquare.getXPos()].getButton();
        JButton moveToButton = board.getSquare()[potentialMove_1.getYPos()][potentialMove_1.getXPos()].getButton();
        ActionEvent clickCurrentSquare = new ActionEvent(moveFromButton, 1, "click");
        board.actionPerformed(clickCurrentSquare);
        ActionEvent clickSquareToMoveTo = new ActionEvent(moveToButton, 1, "click");
        board.actionPerformed(clickSquareToMoveTo);
        Square moveFrom = board.getSquare()[currentSquare.getYPos()][currentSquare.getXPos()];
        Square moveTo = board.getSquare()[potentialMove_1.getYPos()][potentialMove_1.getXPos()];
        assertEquals(Piece.RED, moveFrom.getCurrentPiece());
        assertEquals(Piece.EMPTY, moveTo.getCurrentPiece());
    }

    @Test
    public void testWhitePieceCanJumpRedPieceSelectedOptionAppears() {
        Board board = new Board();
        int fromY = 5;
        int fromX = 1;
        int moveY = 4;
        int moveX = 2;
        JButton moveFromButton = board.getSquare()[fromY][fromX].getButton();
        JButton moveToButton = board.getSquare()[moveY][moveX].getButton();
        ActionEvent clickCurrentSquare = new ActionEvent(moveFromButton, 1, "click");
        board.actionPerformed(clickCurrentSquare);
        ActionEvent clickSquareToMoveTo = new ActionEvent(moveToButton, 1, "click");
        board.actionPerformed(clickSquareToMoveTo);
        //FIRST MOVE PERFORMED
        fromY = 2;
        fromX = 4;
        moveY = 3;
        moveX = 3;
        moveFromButton = board.getSquare()[fromY][fromX].getButton();
        moveToButton = board.getSquare()[moveY][moveX].getButton();
        clickCurrentSquare = new ActionEvent(moveFromButton, 1, "click");
        board.actionPerformed(clickCurrentSquare);
        Square moveFrom = board.getSquare()[fromY][fromX];
        Square moveTo = board.getSquare()[moveY][moveX];
        assertEquals(Piece.RED, moveFrom.getCurrentPiece());
        assertEquals(Piece.SELECTED, moveTo.getCurrentPiece());
        clickSquareToMoveTo = new ActionEvent(moveToButton, 1, "click");
        board.actionPerformed(clickSquareToMoveTo);
        moveFrom = board.getSquare()[fromY][fromX];
        moveTo = board.getSquare()[moveY][moveX];
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.RED, moveTo.getCurrentPiece());
        //SECOND MOVE NOW PERFORMED
        fromY = 4;
        fromX = 2;
        moveY = 2;
        moveX = 4;
        moveFromButton = board.getSquare()[fromY][fromX].getButton();
        clickCurrentSquare = new ActionEvent(moveFromButton, 1, "click");
        board.actionPerformed(clickCurrentSquare);
        moveFrom = board.getSquare()[fromY][fromX];
        moveTo = board.getSquare()[moveY][moveX];
        assertEquals(Piece.WHITE, moveFrom.getCurrentPiece());
        assertEquals(Piece.SELECTED, moveTo.getCurrentPiece());
    }

    public Board playMoveGivenPlacesToClick(int fromY, int fromX, int moveY, int moveX, Board board) {
        JButton moveFromButton = board.getSquare()[fromY][fromX].getButton();
        JButton moveToButton = board.getSquare()[moveY][moveX].getButton();
        ActionEvent clickCurrentSquare = new ActionEvent(moveFromButton, 1, "click");
        board.actionPerformed(clickCurrentSquare);
        ActionEvent clickSquareToMoveTo = new ActionEvent(moveToButton, 1, "click");
        board.actionPerformed(clickSquareToMoveTo);
        return board;
    }

    public Board setupPieceAt3Y3XTaken() {
        Board board = playMoveGivenPlacesToClick(5, 1, 4, 2, new Board()); //WHITE
        return playMoveGivenPlacesToClick(2, 4, 3, 3, board); //RED
    }

    public Board moveWhiteNonAggressive(Board b) {
        return playMoveGivenPlacesToClick(5, 7, 4, 6, b);
    }

    public Board setupPieceAt3Y1XTaken() {
        Board board = playMoveGivenPlacesToClick(5, 3, 4, 2, new Board()); //WHITE
        return playMoveGivenPlacesToClick(2, 0, 3, 1, board); //RED
    }

    @Test
    public void testWhiteTakesRedUpRight() {
        Board board = playMoveGivenPlacesToClick(5, 1, 4, 2, new Board()); //WHITE, RED
        board = playMoveGivenPlacesToClick(2, 4, 3, 3, board); //WHITE
        int whitePieceFromY = 4;
        int whitePieceFromX = 2;
        int redPieceFromY = 2;
        int redPieceFromX = 4;
        board = playMoveGivenPlacesToClick(whitePieceFromY, whitePieceFromX, redPieceFromY, redPieceFromX, board);
        int redPieceX = 3;
        int redPieceY = 3;
        Square moveFrom = board.getSquare()[whitePieceFromY][whitePieceFromX];
        Square moveTo = board.getSquare()[redPieceFromY][redPieceFromX];
        Square redToTake = board.getSquare()[redPieceY][redPieceX];
        assertEquals(Piece.EMPTY, redToTake.getCurrentPiece());
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.WHITE, moveTo.getCurrentPiece());
    }

    @Test
    public void testRedTakesWhiteDownLeft() {
        Board board = setupPieceAt3Y3XTaken(); //WHITE, RED
        board = moveWhiteNonAggressive(board);  //WHITE
        int fromY = 3;
        int fromX = 3;
        int toY = 5;
        int toX = 1;
        board = playMoveGivenPlacesToClick(fromY, fromX, toY, toX, board);  //RED
        int pieceTakenY = 4;
        int pieceTakenX = 2;
        Square moveFrom = board.getSquare()[fromY][fromX];
        Square moveTo = board.getSquare()[toY][toX];
        Square redToTake = board.getSquare()[pieceTakenY][pieceTakenX];
        assertEquals(Piece.EMPTY, redToTake.getCurrentPiece());
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.RED, moveTo.getCurrentPiece());
    }

    @Test
    public void testWhiteTakesRedUpLeft() {
        Board board = playMoveGivenPlacesToClick(5, 3, 4, 2, new Board()); //WHITE, RED
        board = playMoveGivenPlacesToClick(2, 0, 3, 1, board); //WHITE
        int fromY = 4;
        int fromX = 2;
        int toY = 2;
        int toX = 0;
        board = playMoveGivenPlacesToClick(fromY, fromX, toY, toX, board);  //RED
        int pieceTakenY = 3;
        int pieceTakenX = 1;
        Square moveFrom = board.getSquare()[fromY][fromX];
        Square moveTo = board.getSquare()[toY][toX];
        Square redToTake = board.getSquare()[pieceTakenY][pieceTakenX];
        assertEquals(Piece.EMPTY, redToTake.getCurrentPiece());
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.WHITE, moveTo.getCurrentPiece());
    }

    @Test
    public void testRedTakesWhiteDownRight() {
        Board board = playMoveGivenPlacesToClick(5, 3, 4, 2, new Board()); //WHITE, RED
        board = playMoveGivenPlacesToClick(2, 0, 3, 1, board); //WHITE
        board = moveWhiteNonAggressive(board);
        int fromY = 3;
        int fromX = 1;
        int toY = 5;
        int toX = 3;
        board = playMoveGivenPlacesToClick(fromY, fromX, toY, toX, board);  //RED
        int pieceTakenY = 4;
        int pieceTakenX = 2;
        Square moveFrom = board.getSquare()[fromY][fromX];
        Square moveTo = board.getSquare()[toY][toX];
        Square redToTake = board.getSquare()[pieceTakenY][pieceTakenX];
        assertEquals(Piece.EMPTY, redToTake.getCurrentPiece());
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.RED, moveTo.getCurrentPiece());
    }

    @Test
    public void whiteAtZeroYBecomesKingWhite() {
        //map out moves
        Board board = new Board();
        board = playMoveGivenPlacesToClick(5, 1, 4, 2, board); //WHITE
        board = playMoveGivenPlacesToClick(2, 4, 3, 3, board); //RED
        board = playMoveGivenPlacesToClick(6, 0, 5, 1, board); //WHITE
        board = playMoveGivenPlacesToClick(1, 5, 2, 4, board); //RED
        board = playMoveGivenPlacesToClick(5, 1, 4, 0, board); //WHITE
        board = playMoveGivenPlacesToClick(0, 6, 1, 5, board); //RED
        board = playMoveGivenPlacesToClick(4, 2, 0, 6, board); //WHITE
        int fromY = 4;
        int fromX = 2;
        int toY = 0;
        int toX = 6;
        int r1Y = 3;
        int r1X = 3;
        int r2Y = 2;
        int r2X = 4;
        int r3Y = 1;
        int r3X = 5;
        Square moveFrom = board.getSquare()[fromY][fromX];
        Square redTaken1 = board.getSquare()[r1Y][r1X];
        Square redTaken2 = board.getSquare()[r2Y][r2X];
        Square redTaken3 = board.getSquare()[r3Y][r3X];
        Square moveTo = board.getSquare()[toY][toX];
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.EMPTY, redTaken1.getCurrentPiece());
        assertEquals(Piece.EMPTY, redTaken2.getCurrentPiece());
        assertEquals(Piece.EMPTY, redTaken3.getCurrentPiece());
        assertEquals(Piece.KINGWHITE, moveTo.getCurrentPiece());
    }

    @Test
    public void redAtSevenBecomesKingRed() {
        //map out moves
        Board board = new Board();
        board = playMoveGivenPlacesToClick(5, 3, 4, 4, board); //WHITE
        board = playMoveGivenPlacesToClick(2, 6, 3, 5, board); //RED
        board = playMoveGivenPlacesToClick(6, 2, 5, 3, board); //WHITE
        board = playMoveGivenPlacesToClick(2, 0, 3, 1, board); //RED
        board = playMoveGivenPlacesToClick(7, 1, 6, 2, board); //WHITE
        board = playMoveGivenPlacesToClick(3, 5, 7, 1, board); //RED
        int fromY = 3;
        int fromX = 5;
        int toY = 7;
        int toX = 1;
        int w1Y = 4;
        int w1X = 4;
        int w2Y = 5;
        int w2X = 3;
        int w3Y = 6;
        int w3X = 2;
        Square moveFrom = board.getSquare()[fromY][fromX];
        Square redTaken1 = board.getSquare()[w1Y][w1X];
        Square redTaken2 = board.getSquare()[w2Y][w2X];
        Square redTaken3 = board.getSquare()[w3Y][w3X];
        Square moveTo = board.getSquare()[toY][toX];
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.EMPTY, redTaken1.getCurrentPiece());
        assertEquals(Piece.EMPTY, redTaken2.getCurrentPiece());
        assertEquals(Piece.EMPTY, redTaken3.getCurrentPiece());
        assertEquals(Piece.KINGRED, moveTo.getCurrentPiece());
    }

    @Test
    public void kingWhiteCanMoveBackwards() {
        //map out moves
        Board board = new Board();
        board = playMoveGivenPlacesToClick(5, 1, 4, 2, board); //WHITE
        board = playMoveGivenPlacesToClick(2, 4, 3, 3, board); //RED
        board = playMoveGivenPlacesToClick(6, 0, 5, 1, board); //WHITE
        board = playMoveGivenPlacesToClick(1, 5, 2, 4, board); //RED
        board = playMoveGivenPlacesToClick(5, 1, 4, 0, board); //WHITE
        board = playMoveGivenPlacesToClick(0, 6, 1, 5, board); //RED
        board = playMoveGivenPlacesToClick(4, 2, 0, 6, board); //WHITE
        //Post king white scenario
        board = playMoveGivenPlacesToClick(2, 0, 3, 1, board);  //RED
        board = playMoveGivenPlacesToClick(0, 6, 1, 5, board); //WHITE
        Square moveFrom = board.getSquare()[0][6];
        Square moveTo = board.getSquare()[1][5];
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.KINGWHITE, moveTo.getCurrentPiece());
    }

    @Test
    public void kingWhiteCanTakePieceBackwards() {
        //map out moves
        Board board = new Board();
        board = playMoveGivenPlacesToClick(5, 1, 4, 2, board); //WHITE
        board = playMoveGivenPlacesToClick(2, 4, 3, 3, board); //RED
        board = playMoveGivenPlacesToClick(6, 0, 5, 1, board); //WHITE
        board = playMoveGivenPlacesToClick(1, 5, 2, 4, board); //RED
        board = playMoveGivenPlacesToClick(5, 1, 4, 0, board); //WHITE
        board = playMoveGivenPlacesToClick(0, 6, 1, 5, board); //RED
        board = playMoveGivenPlacesToClick(4, 2, 0, 6, board); //WHITE
        //Post king white scenario
        board = playMoveGivenPlacesToClick(0, 4, 1, 5, board);  //RED
        board = playMoveGivenPlacesToClick(0, 6, 2, 4, board); //WHITE
        Square moveFrom = board.getSquare()[0][6];
        Square pieceTaken = board.getSquare()[1][5];
        Square moveTo = board.getSquare()[2][4];
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.EMPTY, pieceTaken.getCurrentPiece());
        assertEquals(Piece.KINGWHITE, moveTo.getCurrentPiece());
    }

    @Test
    public void kingRedCanMoveBackwards() {
        //map out moves
        Board board = new Board();
        board = playMoveGivenPlacesToClick(5, 3, 4, 4, board); //WHITE
        board = playMoveGivenPlacesToClick(2, 6, 3, 5, board); //RED
        board = playMoveGivenPlacesToClick(6, 2, 5, 3, board); //WHITE
        board = playMoveGivenPlacesToClick(2, 0, 3, 1, board); //RED
        board = playMoveGivenPlacesToClick(7, 1, 6, 2, board); //WHITE
        board = playMoveGivenPlacesToClick(3, 5, 7, 1, board); //RED
        //KING RED NOW AVAILABLE
        board = playMoveGivenPlacesToClick(5, 5, 4, 4, board); //WHITE
        board = playMoveGivenPlacesToClick(7, 1, 6, 2, board); //KINGRED
        Square moveFrom = board.getSquare()[7][1];
        Square moveTo = board.getSquare()[6][2];
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.KINGRED, moveTo.getCurrentPiece());
    }

    @Test
    public void kingRedCanTakePieceBackwards() {
        //map out moves
        Board board = new Board();
        board = playMoveGivenPlacesToClick(5, 3, 4, 4, board); //WHITE
        board = playMoveGivenPlacesToClick(2, 6, 3, 5, board); //RED
        board = playMoveGivenPlacesToClick(6, 2, 5, 3, board); //WHITE
        board = playMoveGivenPlacesToClick(2, 0, 3, 1, board); //RED
        board = playMoveGivenPlacesToClick(7, 1, 6, 2, board); //WHITE
        board = playMoveGivenPlacesToClick(3, 5, 7, 1, board); //RED
        //KING RED NOW AVAILABLE
        board = playMoveGivenPlacesToClick(7, 3, 6, 2, board); //WHITE
        board = playMoveGivenPlacesToClick(7, 1, 5, 3, board); //KINGRED
        Square moveFrom = board.getSquare()[7][1];
        Square pieceTaken = board.getSquare()[6][2];
        Square moveTo = board.getSquare()[5][3];
        assertEquals(Piece.EMPTY, moveFrom.getCurrentPiece());
        assertEquals(Piece.EMPTY, pieceTaken.getCurrentPiece());
        assertEquals(Piece.KINGRED, moveTo.getCurrentPiece());
    }
    /*
    ToDo:
        - Test squares are black and white = Done
        - Test pieces assigned correctly = Done
        - Test only valid starting moves = Done
        - Test move is made (positions switched) = Done
        - Test red can take white piece is valid move = Done
        - Test squares are updated = Done
        - Test white can take red piece is valid move = Done
        - Test squares are updated = Done
        - Test multiple white pieces can be taken = Done
        - Test multiple red pieces can be taken = Done
        - Test can only play white move first = Done
        - Test can only play red move after white move played = Done
        - Test king white when at end = Done
        - Test king red when at end = Done
        - Test king white can move backwards = Done
        - Test king red can move backwards = Done
        - Test king red can take piece
        - Test king white can take piece
     */

}
