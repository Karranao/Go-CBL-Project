import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/** Class extending board to contain methods unique to Go
 * boards.
 * 
 */
public class GoBoard extends Board {

    /** Constructor. Calls superclass Board's constructor on initiation.
     * 
     * @param boardSize integer value of x and y dimensions of board
     * @param menu  instance of the menu that initiated board
     */
    public GoBoard(int boardSize, Menu menu) {
        super(boardSize, menu);
        endGamebtn = new EndGameButton(this, boardPositions);
        //adding endgamebtn to board
        super.addComponent(endGamebtn, boardPanel, layout, constraints, 1, 2, 50, 50, 1, 3, 3, 1);
    }

    /** Creates an array containing buttons for each tile on the board, then
     * adds them to the board's panel.
     */
    public void generateButtons(int boardSize, Board board, JLayeredPane boardPanel,
        GridBagLayout layout, GridBagConstraints constraints) {
        
        //array to hold GameBoardButtons belonging to the board
        GameBoardButton[][] buttons = new GameBoardButton[boardSize][boardSize];
        
        //creates 2d array of GameBoardButtons corresponding to board spaces
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                buttons[row][col] = new GameBoardButton(row, col, board);
            }
        }

        //populates board with GameBoardButtons
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                addComponent(buttons[row][col], boardPanel, layout, constraints, col + 1,
                    row + 3, 50, 50, 1, 1, 1, 1);
            }
        }

        
    }

    /** Creates a JLabel displaying the name of game being played.
     */
    public JLabel generateLabel() {
        //label for game currently being played
        boardLabel = new JLabel("Go");
        boardLabel.setFont(new Font("Serif", Font.BOLD, 40));
        boardLabel.setForeground(Color.BLACK);
        
        return boardLabel;
    }

    /** Calls creates an instance of a button to end the game then adds
     * it to the GUI.
     * 
     * @param boardColor    color of board
     * @param buttonBorder  color of button's border
     * @param boardPositions    array containing positions of all tiles placed on the board
     */
    public void generateEndGameButton(Color boardColor, Border buttonBorder,
            int[][] boardPositions) {
        
        endGamebtn = new EndGameButton(this, boardPositions);
        super.addComponent(endGamebtn, boardPanel, layout, constraints, 1, 2, 50, 50, 1, 3, 3, 1);
    }

    /** Disables all buttons.
     */
    @Override
    public void disableAllButtons() {
        for (Component component : boardPanel.getComponents()) {
            //disabling any instance of JButton or GameBoardButton
            if (component instanceof JButton || component instanceof GameBoardButton) {
                component.setEnabled(false);
            }
        }
        this.backButton.setEnabled(true);
    }
}