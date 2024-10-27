import javax.swing.*;

/**
 * This program implements the logic for a Five in a Row game.
 * 
 */
public class FiveInRow extends JFrame {

    private Board board;
    private int[][] boardPositions;
    
    /**
     * initialise the board and button positions.
     * @paramboard
     */
    public FiveInRow(Board board) {
        this.board = board;
        this.boardPositions = board.getBoardPositions();
    }


    /**
     * CheckWin method checks whether a winning condition has been met.
     * 
     * @paramrow
     * @paramcol
     * @return True if one player wins, otherwise false.
     */

    public boolean checkWin(int row, int col) {
        int player;

        //finding turn of the player who clicked
        if (board.getTurn() == 1) {
            player = 2;
        } else {
            player = 1;
        }

        //checks whether there are five continuous buttons
        if (checkDirection(row, col, player)) {
            //displays who won
            printWinner(player);
            board.disableAllButtons();
            return true;
        } else {
            return false;
        }
    }
    /**
     * checkDirection method checks if there are five consecutive buttons.
     * 4 directions will be checked.
     * 
     * @paramrow
     * @paramcol
     * @paramplayer
     * @return True if there are five buttons in a row, otherwise false.
     */

    private boolean checkDirection(int row, int col, int player) {

        // 4 possible directions
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int[] direction : directions) {
            int count = 1;
            // positive direction
            count += countInDirection(row, col, player, direction[0], direction[1]);
            // negative direction
            count += countInDirection(row, col, player, -direction[0], -direction[1]);

            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * countInDirection method counts the number of consecutive buttons.
     * The buttons are in a specific direction.
     * 
     * @paramrow
     * @paramcol
     * @paramplayer
     * @paramrowDelta
     * @paramcolDelta
     * @return The number of consecutive pieces found in the given direction.
     */
    private int countInDirection(int row, int col, int player, int rowDelta, int colDelta) {
        int amount = 0;
        int nextRow = row + rowDelta;
        int nextCol = col + colDelta;
        
        //iterates through next rows and columns if the position is valid, and belongs to the player
        while (isValidPosition(nextRow, nextCol) && boardPositions[nextCol][nextRow] == player) { 
            amount++;
            nextRow += rowDelta;
            nextCol += colDelta;
        }
        return amount;
    }

    /**
     * isValidPosition method checks if the given row and column positions.
     * are valid or not
     * @paramrow
     * @paramcol
     * @return True if the position is valid, otherwise false.
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 9 && col >= 0 && col < 9;
    }

    /**
     * printWinner method prints the winner by showing a dialog box.
     * 
     * @paramplayer
     */
    private void printWinner(int player) {
        String winner;
        if (player == 1) {
            winner = "Black";
        } else {
            winner = "White";
        }
        JOptionPane.showMessageDialog(board, "Player " + winner + " wins!");
    }
}