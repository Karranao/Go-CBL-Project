import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/** Panel for the board's background.
 */
public class BackPanel extends JPanel {

    /** Constructor.
     * 
     * @param boardColor    //color of background
     * @param boardBorderColor  //background border
     */
    public BackPanel(Color boardColor, Color boardBorderColor) {

        setBackground(boardColor);
        //creating border for the panel
        Border boardBorder = BorderFactory.createLineBorder(boardBorderColor, 8);
        setBorder(boardBorder);
        setVisible(true);

    }
}