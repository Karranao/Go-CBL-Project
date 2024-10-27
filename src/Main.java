import javax.swing.*;

/** Initializes the game by opening start menu.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu());
    }
}