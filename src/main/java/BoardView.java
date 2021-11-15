import javax.swing.*;
import java.awt.*;

class BoardView {
    private final JPanel boardPanel = new JPanel();
    private final GridLayout gridBoard = new GridLayout(8, 8);
    private final JFrame board = new JFrame();

    public BoardView() {
        setUpUI();
    }

    public void addBoardPanel(JButton gridButton) {
        boardPanel.add(gridButton);
    }

    public void setBoardVisible() {
        board.setContentPane(boardPanel); //sets the panel to the board frame so the board is correctly initialised
        board.setVisible(true); //sets the board visible
    }

    public void setUpUI() {
        board.setSize(800, 800); //size of board. image is 100x100 and 8x8 layout so 800x800
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //when closed program shuts down
        boardPanel.setLayout(gridBoard); //sets the 8x8 board layout. an added component is arranged in 8x8 layout
    }
}
