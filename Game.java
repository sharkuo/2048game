/**
 * CIS 120 HW09 - 2048
 * (c) University of Pennsylvania
 * Created by Sharon Kuo in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. 
 * In a Model-View-Controller framework, Game initializes the view, implements a
 * bit of controller functionality through the reset button, and then
 * instantiates a GameBoard. The GameBoard will handle the rest of the game's
 * view and controller functionality, and it will instantiate a 2048 object
 * to serve as the game's model.
 */
public class Game implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local
        // variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("2048");
        frame.setLocation(700, 250);

        // Score panel
        final JPanel score_panel = new JPanel();
        frame.add(score_panel, BorderLayout.SOUTH);
        final JLabel score = new JLabel("Score: 0");
        score_panel.add(score);
        score_panel.setBackground(new Color(250, 248, 239));

        // Game board
        final GameBoard board = new GameBoard(score);
        frame.add(board, BorderLayout.CENTER);
        
        // Button panel
        final JPanel control_panel = new JPanel();
        
        // Instructions button
        final JButton instruct = new JButton("How to Play");
        instruct.setBackground(new Color(143, 122, 102));
        instruct.setForeground(new Color(249, 246, 242));
        instruct.setBorderPainted(false);
        instruct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // instructions pop up
                final JFrame instruct = new JFrame("HOW TO PLAY");
                JOptionPane.showMessageDialog(instruct,
                        "Use your arrow keys to move tiles. "
                        + "Tiles with the same number merge into one when they touch. \n"
                        + "Add them up to reach 2048!" + "\n"
                        + "Note: Every new number created, you will earn that many points");
                board.reset();
            }
        });
        control_panel.add(instruct);

        // Reset button
        frame.add(control_panel, BorderLayout.NORTH);
        control_panel.setBackground(new Color(250, 248, 239));
       
        // Note here that when we add an action listener to the reset button, we
        // define it as an
        // anonymous inner class that is an instance of ActionListener with its
        // actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will
        // be called.
        final JButton reset = new JButton("Reset");
        reset.setToolTipText("Click this to reset game");
        reset.setBackground(new Color(143, 122, 102));
        reset.setForeground(new Color(249, 246, 242));
        reset.setBorderPainted(false);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // Undo button
        final JButton undo = new JButton("Undo");
        undo.setToolTipText("Click this to undo a move");
        undo.setBackground(new Color(143, 122, 102));
        undo.setForeground(new Color(249, 246, 242));
        undo.setBorderPainted(false);
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int c = board.getCount();
                board.undo(c + 1); // implement undo
            }
        });
        control_panel.add(undo);
        
        // Leaderboard button
        final JButton leader = new JButton("Leaderboard");
        leader.setToolTipText("Click this to see high scores");
        leader.setBackground(new Color(143, 122, 102));
        leader.setForeground(new Color(249, 246, 242));
        leader.setBorderPainted(false);
        leader.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFrame lead = new JFrame("LEADERBOARD");
                
                // get leaderboard data
                String str = "";
                if (board.getLeaderScores() == null || board.getLeaderScores().isEmpty()) {
                    str = "No scores stored";
                } else {
                    for (int i = 0; i < board.getLeaderScores().size(); i++) {
                        String a = board.getLeaderUsers().get(i);
                        Integer b = board.getLeaderScores().get(i);
                        str = str + a + ": " + b + "\n";
                    }
                }
      
                JOptionPane.showMessageDialog(lead, str, "LEADERBOARD", JOptionPane.PLAIN_MESSAGE);
                board.reset();
            }
        });
        control_panel.add(leader);
        

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        

        // Start the game
        board.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements
     * specified in Game and runs it. IMPORTANT: Do NOT delete! You MUST include
     * this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}