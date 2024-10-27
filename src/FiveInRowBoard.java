import java.awt.*;
import javax.swing.*;

/** Class extending board to contain methods unique to five-in-a-row
 * boards.
 * 
 */
public class FiveInRowBoard extends Board {

    //constructor
    public FiveInRowBoard(int boardSize, Menu menu) {
        super(boardSize, menu);
    }

    /** Creates an array containing buttons for each tile on the board, then
     * adds them to the board's panel.
     */
    public void generateButtons(int boardSize, Board board, JLayeredPane boardPanel,
            GridBagLayout layout, GridBagConstraints constraints) {
        //holds board buttons
        FiveInARowButton[][] buttons = new FiveInARowButton[boardSize][boardSize];  
        //populating buttons array with FiveInARowButtons
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                buttons[row][col] = new FiveInARowButton(row, col, this);
            }
        }

        //adding all buttons in buttons array to the boardPanel
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                //adds component in correct grid location to the boardPanel
                addComponent(buttons[row][col], boardPanel, layout, constraints, col + 1,
                    row + 3, 50, 50, 1, 1, 1, 1);
            }
        }
    }

    /** Creates a JLabel displaying the name of game being played.
     */
    public JLabel generateLabel() {
    
        //label for game currently being played
        boardLabel = new JLabel("Five-In-A-Row");
        boardLabel.setFont(new Font("Serif", Font.BOLD, 40));
        boardLabel.setForeground(Color.BLACK);
        
        return boardLabel;
    }

    /** Calls methods to check whether someone has won five-in-a-row.
     * 
     * @param row   row of recently placed tile
     * @param col   column of recently placed tile
     */
    public void fiveCheck(int row, int col) {
        FiveInRow fiveInstance = new FiveInRow(this);
        if (fiveInstance.checkWin(row, col)) {  //ends game if someone has won
            disableAllButtons();
        }

    }


    /** Disables all buttons.
     */
    public void disableAllButtons() {
        for (Component component : boardPanel.getComponents()) {
            if (component instanceof JButton) {
                component.setEnabled(false);
            }
        }
        this.backButton.setEnabled(true);
    }
}