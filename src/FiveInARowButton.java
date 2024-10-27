import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;
import javax.swing.border.Border;

/**
 * This class represents a button for the Five in a Row game.
 */
public class FiveInARowButton extends JButton {
    int row;    //row position of button
    int col;    //col position of button
        
    /**
     * FiveInRowButton constructor. Inherits the constructor method from the superclass.
     * 
     * @paramrow
     * @paramcol
     * @paramboard
     */
    public FiveInARowButton(int row, int col, FiveInRowBoard board) {
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
 
        addListener(board); //method to add listener
    }
    
    /**
     * Adds an ActionListener to the button, allowing it to respond to user clicks.
     * 
     * @paramboard
     */   
    private void addListener(FiveInRowBoard board) {
    
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //stopping if tile has already been clicked
                if (getBackground().equals(Color.BLACK) || getBackground().equals(Color.WHITE)) {
                    return;
                }

                int row = FiveInARowButton.this.row;
                int col = FiveInARowButton.this.col;

                //gets current turn 
                int turn = board.getTurn();
                //sets new tile value
                board.setTile(col, row);
                //setting tile's color         
                if (turn == 1) {
                    setBackground(Color.BLACK);
                } else if (turn == 2) {
                    setBackground(Color.WHITE);

                }
                board.fiveCheck(row, col);  //checks whether the move has resulted in a player's win
            }
        }
        );
    }
}
    