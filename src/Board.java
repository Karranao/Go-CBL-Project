import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;
import javax.swing.border.Border;


/** JFrame containing all components of the game board,
 *  for either available game mode. Contains methods for 
 *  management of the game.
 * 
 */
public abstract class Board extends JFrame {
    protected JLayeredPane boardPanel;
    protected GridBagLayout layout;
    protected GridBagConstraints constraints;
    protected int[][] boardPositions;
    private JLabel turnLabel;
    protected JLabel boardLabel;
    protected JButton endGamebtn;
    private int blackPoints;
    private int whitePoints;
    private String winner;
    protected FiveInRow fiveInstance;
    protected Color boardColor;
    protected Border buttonBorder;
    protected JButton backButton;

    public int turn = 1;

    /** Board constructor, creates all components on the board.
     * 
     * @param boardSize integer value of x and y dimensions of board
     * @param menu  instance of the menu that initiated board
     */
    public Board(int boardSize, Menu menu) {
        
        boardPositions = new int[9][9]; //array holding positions of players tiles
    
        boardPanel = new JLayeredPane();
        layout = new GridBagLayout();   //layout manager
        constraints = new GridBagConstraints(); //constraints to be applied to each component
        boardPanel.setLayout(layout);
        getContentPane().add(boardPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        Color backgroundColor = new Color(70, 156, 62);
        boardPanel.setBackground(backgroundColor);
        boardPanel.setOpaque(true); 

        //creating then adding GameBoardButtons to panel
        generateButtons(boardSize, this, boardPanel, layout, constraints);

        //adding board background panel
        Color boardColor = new Color(145, 70, 32);
        Color boardBorderColor = new Color(84, 32, 6);
        BackPanel backgroundPanel = new BackPanel(boardColor, boardBorderColor);

        constraints.fill = GridBagConstraints.BOTH;
        addComponent(backgroundPanel, boardPanel, layout, constraints, 1, 3, 10, 5, 1, 1, 9, 9);
        constraints.fill = GridBagConstraints.NONE;

        //JLabel displaying current game
        JLabel gameLabel;
        gameLabel = generateLabel();
        addComponent(gameLabel, boardPanel, layout, constraints, 4, 1, 50, 30, 1, 1, 3, 1);




        //JLabel displaying current player's turn
        this.turnLabel = new JLabel("Turn: Black");
        turnLabel.setBackground(backgroundColor);
        addComponent(turnLabel, boardPanel, layout, constraints, 7, 1, 50, 30, 1, 1, 3, 1);
        turnLabel.setFont(new Font("Serif", Font.PLAIN, 32));
        turnLabel.setForeground(Color.BLACK);
        turnLabel.setOpaque(true);
        turnLabel.setVisible(true);


        //back button
        backButton = new JButton("Return to menu");
        backButton.setBackground(boardColor);
        backButton.setFont(new Font("Serif", Font.PLAIN, 24));
        backButton.setForeground(Color.BLACK);
        Border buttonBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        backButton.setPreferredSize(new Dimension(180, 50));
        backButton.setBorder(buttonBorder);
        backButton.setFocusPainted(false);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.returnTo();    //function to return to menu
            }
        });

        addComponent(backButton, boardPanel, layout, constraints, 1, 1, 50, 50, 1, 1, 3, 1);



        //adding blank spaces surrounding board
        JPanel emptyPanel1 = new JPanel();
        emptyPanel1.setBackground(backgroundColor);
        addComponent(emptyPanel1, boardPanel, layout, constraints, 3, 12, 5, 10, 1, 1, 1, 1);

        JPanel emptyPanel2 = new JPanel();
        emptyPanel2.setBackground(backgroundColor);
        addComponent(emptyPanel2, boardPanel, layout, constraints, 0, 11, 10, 5, 1, 1, 1, 1);

        JPanel emptyPanel3 = new JPanel();
        emptyPanel3.setBackground(backgroundColor);
        addComponent(emptyPanel3, boardPanel, layout, constraints, 11, 11, 10, 5, 1, 1, 1, 1);

        JPanel emptyPanel4 = new JPanel();
        emptyPanel4.setBackground(backgroundColor);
        addComponent(emptyPanel4, boardPanel, layout, constraints, 1, 2, 10, 5, 1, 0.1, 1, 1);
        emptyPanel4.setVisible(false);

        setVisible(true);
    }


    //abstract methods

    //generates and implements board's buttons
    public abstract void generateButtons(int boardSize, Board board, JLayeredPane boardPanel,
        GridBagLayout layout, GridBagConstraints constraints);

    //generates title label
    public abstract JLabel generateLabel();

    /** Disables all buttons.
     */
    public abstract void disableAllButtons();



    //getters

    public int getTurn() {
        return turn;
    }

    public Board getBoard() {
        return this;
    }

    public int[][] getBoardPositions() {
        return boardPositions;
    }

    /**Sets value of clicked tile in boardPositions.
     * 
     * @param col   //column of tile's location
     * @param row   //row of tile's location
     */
    public void setTile(int col, int row) {
        if (boardPositions[col][row] == 0) {
            boardPositions[col][row] = turn;
            
            if (turn == 1) {
                turn = 2;
            } else {
                turn = 1;
            }
        }
        displayTurn(turn);
    }


    /** Sets text of label to show which player's turn it is.
     * 
     * @param turn  //integer value tracking player's turn
     */
    public void displayTurn(int turn) {
        if (turn == 1) {
            turnLabel.setText("Turn: Black");
        } else {
            turnLabel.setText("Turn: White");
        }
    }

    /** //saving information on player points, and who won from checkGo's functions
     *  as instance variables.
     * 
     * @param blackPoints   //black's points at the end of the game
     * @param whitePoints   //white's points at the end of the game
     * @param winner    //Winner of the game
     */
    public void endgameData(int blackPoints, int whitePoints, String winner) {
        this.blackPoints = blackPoints;
        this.whitePoints = whitePoints;
        this.winner = winner;
    }


    /** Configures the JFrame, hiding and adding componenets to display game over information.
     */
    public void endGame() {
        turnLabel.setVisible(false);
        boardLabel.setVisible(false);
        endGamebtn.setVisible(false);

        Color backColor = new Color(71, 121, 173);
        Color borderColor = new Color(38, 38, 38);
        BackPanel endBackground = new BackPanel(backColor, borderColor);
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(endBackground, boardPanel, layout, constraints, 1, 1,  20, 20, 1, 1, 9, 2);
        constraints.fill = GridBagConstraints.NONE;

        endBackground.setVisible(true);

        //creating label displaying the winner
        JLabel winnerLabel;
        if (blackPoints == whitePoints) {   //determining whether a tie has taken place
            winnerLabel = new JLabel("Tie!");
        } else {
            winnerLabel = new JLabel("Winner: " + winner);
        }
        winnerLabel.setFont(new Font("Serif", Font.BOLD, 30));
        winnerLabel.setForeground(Color.BLACK);
        winnerLabel.setOpaque(true);
        winnerLabel.setVisible(true);
        boardPanel.setLayer(winnerLabel, 2);    //placing label's layer above background layer
        winnerLabel.setBackground(backColor);

        addComponent(winnerLabel, boardPanel, layout, constraints, 4, 1,  20, 20, 1, 1, 3, 1);


        //label displaying black's points
        JLabel blackLabel = new JLabel("Black: " + blackPoints);
        blackLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        blackLabel.setForeground(Color.BLACK);
        blackLabel.setOpaque(true);
        blackLabel.setVisible(true);
        boardPanel.setLayer(blackLabel, 2); //placing label's layer above background layer
        blackLabel.setBackground(backColor);
        addComponent(blackLabel, boardPanel, layout, constraints, 4, 2,  20, 20, 1, 1, 3, 1);

        //label displaying white's points
        JLabel whiteLabel = new JLabel("White: " + whitePoints);
        whiteLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        whiteLabel.setForeground(Color.BLACK);
        whiteLabel.setOpaque(true);
        whiteLabel.setVisible(true);
        boardPanel.setLayer(whiteLabel, 2); //placing label's layer above background layer
        whiteLabel.setBackground(backColor);
        addComponent(whiteLabel, boardPanel, layout, constraints, 7, 2,  20, 20, 1, 1, 3, 1);

        
    }
    

    /** Adds a component to the panel specified, with the constraints passed as parameters.
     * 
     * @param addedComponent    //component to be added
     * @param panel //panel component is to be added to
     * @param layout    //GridBagLayout being used
     * @param constraints   //GridBagConstraints to be applied
     * @param x //x location in grid
     * @param y //y location in gridt
     * @param width //width of element in pixels
     * @param height    //height of element in pixels
     * @param weightx   //how horizontal space is distributed
     * @param weighty   //how vertical space is distributed
     * @param gridWidth //how many cells, horizontally, are occupied
     * @param gridHeight    //how many cells, vertically, are occupied
     */
    public void addComponent(Component addedComponent, JLayeredPane panel, GridBagLayout layout,
        GridBagConstraints constraints, int x, int y, int width, float height, double weightx,
        double weighty, int gridWidth, int gridHeight) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;

        layout.setConstraints(addedComponent, constraints);
        boardPanel.add(addedComponent);

    }

}