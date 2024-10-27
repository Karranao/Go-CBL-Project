import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/** Game initiation menu class. Creates all components of the menu and defines their
 *  behavior, including calling the required methods to start up the games.
 * 
 */
public class Menu extends JFrame {
    JPanel menuPanel;
    GridBagLayout layout;
    GridBagConstraints constraints;
    private FiveInRowBoard fiveBoard;
    private GoBoard goBoard;

    /** Menu constructor.
     */
    public Menu() {

        menuPanel = new JPanel();
        layout = new GridBagLayout();   //layout manager
        //holds values of different constraints to be applied to components in the layout
        constraints = new GridBagConstraints();
        menuPanel.setLayout(layout);
        getContentPane().add(menuPanel);

        //setting background color
        Color menuColor = new Color(70, 156, 62);
        menuPanel.setBackground(menuColor);

        //welcome label
        JLabel titleLabel = new JLabel("Welcome to GO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 52));
        titleLabel.setForeground(Color.BLACK);
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(titleLabel, menuPanel, layout, constraints, 3, 0, 40, 80, 1, 1, 6, 1);

        //button for entering the first game
        JButton game1btn = new JButton("Go");
        game1btn.setFont(new Font("Serif", Font.PLAIN, 30));
        game1btn.setForeground(Color.BLACK);
        addComponent(game1btn, menuPanel, layout, constraints, 2, 2, 100, 50, 0.75, 0.25, 2, 1);

        game1btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initiateBoard();    //method initiates Go game
            }
        });

        //button for entering the second game
        JButton game2btn = new JButton("5 in a row");
        game2btn.setFont(new Font("Serif", Font.PLAIN, 30));
        game2btn.setForeground(Color.BLACK);
        addComponent(game2btn, menuPanel, layout, constraints, 6, 2, 100, 50, 0.5, 0.25, 2, 1);

        
        game2btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initiateFiveInARow();   //method initiates five-in-a-row game
            }
        });

        constraints.fill = GridBagConstraints.NONE; //returning fill value to default

        //adding empty space for formatting
        JPanel emptyPanel1 = new JPanel();
        addComponent(emptyPanel1, menuPanel, layout, constraints, 0, 0, 10, 5, 1, 1, 1, 1);
        emptyPanel1.setBackground(menuColor);

        //adding empty space for formatting
        JPanel emptyPanel2 = new JPanel();
        addComponent(emptyPanel2, menuPanel, layout, constraints, 9, 0, 10, 5, 1, 1, 1, 1);
        emptyPanel2.setBackground(menuColor);

        //adding empty space for formatting
        JPanel emptyPanel3 = new JPanel();
        addComponent(emptyPanel3, menuPanel, layout, constraints, 0, 3, 10, 5, 1, 0.5, 1, 1);
        emptyPanel3.setBackground(menuColor);

        //adding empty space for formatting
        JPanel emptyPanel4 = new JPanel();
        addComponent(emptyPanel4, menuPanel, layout, constraints, 5, 3, 10, 5, 1, 0.5, 1, 1);
        emptyPanel4.setBackground(menuColor);


        menuPanel.setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setVisible(true);

    }




    /** Adds a component to the menuPanel, taking input for constraints to be added.
     * 
     * @param addedComponent    //component to be added
     * @param panel //current panel
     * @param layout    //GridBagLayout
     * @param constraints   //constraints to be applied
     * @param x //x location of element in grid
     * @param y //y location of element in grid
     * @param width //width of grid tile
     * @param height    //height of grid tile
     * @param weightx   //tile weight x
     * @param weighty   //tile weight y
     */
    
    public void addComponent(Component addedComponent, JPanel panel, GridBagLayout layout,
        GridBagConstraints constraints, int x, int y, int width, float height, double weightx,
        double weighty, int gridWidth, int gridHeight) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;

        layout.setConstraints(addedComponent, constraints);
        menuPanel.add(addedComponent);

    }

    /** Method for returning to Menu from a game.
     */
    public void returnTo() {
        if (this.fiveBoard == (null)) {
            goBoard.dispose();
        } else {
            fiveBoard.dispose();
        }
        setVisible(true);
    }

    /** Initiates a Go game board.
    */
    public void initiateBoard() {
        this.goBoard = new GoBoard(9, this);
        setVisible(false);
    }

    /** Initiates a five in a row game board.
     */
    public void initiateFiveInARow() {
        this.fiveBoard = new FiveInRowBoard(9,  this);
        setVisible(false);
    }
}