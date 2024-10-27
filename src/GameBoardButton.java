import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;
import javax.swing.border.Border;

/** Custom class for board buttons. Changes their appearance, and implements
 *  a listener for behavior on click.
 */
public class GameBoardButton extends JButton {
    int row;
    int col;

    /** GameBoardButton constructor.
     * 
     * @param row   //row of buttons location
     * @param col   //column of button's location
     */
    public GameBoardButton(int row, int col, Board board) {
        super();

        //creating button appearance in the GUI
        Color defaultColor = new Color(209, 123, 79);
        //creates a black border surrounding the button
        Border buttonBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        setPreferredSize(new Dimension(40, 20));
        setBorder(buttonBorder);
        setBackground(defaultColor);
        setOpaque(true);

        this.row = row;
        this.col = col;

        addListener(board); //adds listener
    }

    /** Adds an action listener to the button, dictating color change behavior,
     * and calling function to save result of click in board class.
     * 
     * @param board //board that button is a component on
     */
    private void addListener(Board board) {
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int turn = board.getTurn();
                board.setTile(col, row);    //method saves value of tile after click

                //stopping activity if tile has already been clicked
                if (getBackground().equals(Color.BLACK) || getBackground().equals(Color.WHITE)) {
                    return;
                }

                if (turn == 1) {    //applying color based on turn of player who clicked the button
                    setBackground(Color.BLACK);
                    turn = 2;
                } else if (turn == 2) {
                    setBackground(Color.WHITE);
                    turn = 1;
                }
            }
        }
        );
    }

}