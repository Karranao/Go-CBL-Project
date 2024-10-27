import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;
import javax.swing.border.Border;

/** Button for Go board to end the game. Initiates methods needed to count score.
 *  Dictates appearance of button.
 */
public class EndGameButton extends JButton {

    /** EndGameButton constructor.
     * 
     * @param board board that button is added to
     * @param boardPositions    positions of tiles and their values on the board
     */
    public EndGameButton(Board board, int[][] boardPositions) {

        Color buttonColor = new Color(145, 70, 32);

        setBackground(buttonColor);
        setPreferredSize(new Dimension(180, 50));
        Border buttonBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        setBorder(buttonBorder);
        setFocusPainted(false);
        
        //label for button's writing
        JLabel buttonLabel = new JLabel("    End Game");
        buttonLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        buttonLabel.setForeground(Color.BLACK);
        add(buttonLabel);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckGo check = new CheckGo(boardPositions);
                check.checkPoints(board);    
                board.disableAllButtons();
                board.endGame();
            }
        });
    }
}