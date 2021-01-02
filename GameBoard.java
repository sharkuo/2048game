/** CIS 120 HW09 Final Project - 2048 **/

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * This class instantiates a TwentyFourtyEight object, which is the model for the game.
 * As the user pressed keyboard arrows, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its score JLabel to
 * reflect the current state of the model.
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private TwentyFourtyEight ttt;  // model for the game
    private JLabel score;           // current score text
    private int c;                  // undo count

    // Game constants
    public static final int BOARD_WIDTH = 410;
    public static final int BOARD_HEIGHT = 410;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel scoreInit) {
        // creates border around the court area, JComponent method with
        // thickness of 8px
        setBorder(BorderFactory.createLineBorder(new Color(187, 173, 160), 10));
        setBackground(new Color(205, 193, 180));

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by
        // its key listener.
        setFocusable(true);

        ttt = new TwentyFourtyEight();  // initializes model for the game
        score = scoreInit;              // initialize score JLabel
        
        /*
         * Listens for key presses. Updates the model, then updates the game
         * board and score based off of the updated model.
         */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP :
                        ttt.mergeUp();
                        break;
                    case KeyEvent.VK_DOWN :
                        ttt.mergeDown();
                        break;
                    case KeyEvent.VK_LEFT :
                        ttt.mergeLeft();
                        break;
                    case KeyEvent.VK_RIGHT :
                        ttt.mergeRight();
                        break;
                    default :
                        break;
                }
                setCount(0); // reset counter
                ttt.update();
                updateScore();
                repaint(); // repaints the game board

                // check for game over
                if (ttt.isGameOver()) {
                    gameOver();
                }
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        ttt.reset();
        score.setText("START");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    // UNDO
    public void undo(int count) {
        setCount(count);
        ttt.undo(count);
        ttt.update();
        updateScore();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        setFocusable(true);
        requestFocusInWindow();
    }
    
    // COUNTER
    // get counter value
    public int getCount() {
        return c;
    }
    
    // set counter value
    public void setCount(int n) {
        c = n;
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach will not be
     * sufficient for most games, because it is not modular. All of the logic
     * for drawing the game board is in this method, and it does not take
     * advantage of helper methods. Consider breaking up your paintComponent
     * logic into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Set color
        g.setColor(new Color(187, 173, 160));
        g.setFont(new Font("Helvetica Neue", Font.BOLD, 50));

        // Draws board grid
        g2.setStroke(new BasicStroke(10)); // set line thickness
        g2.drawLine(105, 0, 105, 400);
        g2.drawLine(205, 0, 205, 400);
        g2.drawLine(305, 0, 305, 400);
        g2.drawLine(0, 105, 400, 105);
        g2.drawLine(0, 205, 400, 205);
        g2.drawLine(0, 305, 400, 305);

        // Draw tiles
        paintTiles(g);
    }

    // Draws Tiles
    public void paintTiles(Graphics g) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int val = ttt.getCell(i, j);
                if (val != 0) {
                    // tile background
                    g.setColor(ttt.getColor(i, j));
                    g.fillRoundRect(10 + i * 100, 10 + j * 100, 90, 90, 10, 10);

                    // tile number
                    g.setColor(ttt.getTextColor(i, j));
                    String s = Integer.toString(val);
                    int l = s.length();
                    g.drawString(s, 10 + i * 100 + (3 - l) * 14, j * 100 + 72);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    // -----------------------GAME OVER-----------------------------------
    // display game over
    private void gameOver() {
        final JFrame end = new JFrame("GAME OVER");
        String str = "GAME OVER. Solid try bud. Input your name to see if you make the leaderboard";
        if (ttt.gameWon()) {
            str = "WOOHOO! You beat the game! Input your name to see if you make the leaderboard";
        }
        String str2 = "NAME";
        String name = (String) JOptionPane.showInputDialog(end, str, str2);
        if (name == null || name == "INSERT NAME") {
            name = "unknown";
        }
        ttt.updateLeaders(name, ttt.getScore());
    }

    // ---------------------------SCORES------------------------------------

    // returns list of users on leaderboard
    public List<String> getLeaderUsers() {
        return ttt.getLeaderUsers();
    }
    
    // returns list of highest scores
    public List<Integer> getLeaderScores() {
        return ttt.getLeaderScores();
    }

    // updates score JLabel on score panel
    private void updateScore() {
        score.setText("Score: " + ttt.getScore());
    }

}