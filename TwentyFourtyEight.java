import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * CIS 120 Final Project - 2048 Game
 * (c) University of Pennsylvania
 * Created by Sharon Kuo in Fall 2020
 */

/**
 * This class is a model for 2048.
 */
public class TwentyFourtyEight {

    private Tile[][] board;
    private boolean gameWon;
    private int score;
    private LinkedList<Tile[][]> history;
    private LinkedList<Integer> scoreHist;
    private List<String> leaderUsers;
    private List<Integer> leaderScores;

    /**
     * Constructor sets up game state.
     */
    public TwentyFourtyEight() {
        fileInit();
        reset();
        update();
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new Tile[4][4];
        gameWon = false;
        score = 0;
        history = new LinkedList<Tile[][]>();
        scoreHist = new LinkedList<Integer>();

        // initialize tiles in board
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = new Tile();
            }
        }

        // add initial tiles
        int a = (int) (Math.random() * 4);
        int b = (int) (Math.random() * 4);
        int c = a;
        int d = b;
        // to ensure that the they aren't put in the same grid cell
        while (c == a && d == b) {
            c = (int) (Math.random() * 4);
            d = (int) (Math.random() * 4);
        }

        Tile t1 = new Tile();
        Tile t2 = new Tile();
        t1.randTile();
        t2.randTile();
        board[a][b] = t1;
        board[c][d] = t2;
    }

    /**
     * update() updates the game state and records it in LinkedLists history and
     * score history. It also generates a random tile if the game state changes.
     * 
     */
    public void update() {
        Tile[][] newBoard = new Tile[4][4];
        int newScore = score;

        // add new random tile if game state changed
        if (stateChanged()) {
            int a = (int) (Math.random() * 4);
            int b = (int) (Math.random() * 4);
            while (getCell(b, a) != 0) {
                a = (int) (Math.random() * 4);
                b = (int) (Math.random() * 4);
            }

            Tile t = new Tile();
            t.randTile();
            board[a][b] = t;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j].setColor();
                newBoard[i][j] = new Tile(board[i][j].getNumber());
            }
        }

        history.add(newBoard);
        scoreHist.add(newScore);
        printGameState();
    }

    // RESPONSE TO KEYBOARDS

    // merges/shifts tiles up
    public void mergeUp() {
        moveUp();
        moveUp();
        combineUp();
        moveUp();
        moveUp();
    }

    // merges/shifts tiles down
    public void mergeDown() {
        moveDown();
        moveDown();
        combineDown();
        moveDown();
        moveDown();
    }

    // merges/shifts tiles left
    public void mergeLeft() {
        moveLeft();
        moveLeft();
        combineLeft();
        moveLeft();
        moveLeft();
    }

    // merges/shifts tiles right
    public void mergeRight() {
        moveRight();
        moveRight();
        combineRight();
        moveRight();
        moveRight();
    }

    // combines tiles in right direction
    public void combineRight() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int x = board[i][j].getNumber();
                int y = board[i][j + 1].getNumber();
                if (x == y) {
                    board[i][j].setNumber(2 * y);
                    board[i][j + 1] = new Tile();
                    score += 2 * y;
                }
                if (x + y == 2048) {
                    gameWon = true;
                }
            }
        }
    }

    // combines tiles downward
    public void combineDown() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int x = board[i][j].getNumber();
                int y = board[i + 1][j].getNumber();
                if (x == y) {
                    board[i][j].setNumber(2 * y);
                    board[i + 1][j] = new Tile();
                    score += 2 * y;
                }
                if (x + y == 2048) {
                    gameWon = true;
                }
            }
        }
    }

    // combines tiles in left direction
    public void combineLeft() {
        for (int i = 3; i > -1; i--) {
            for (int j = 3; j > 0; j--) {
                int x = board[i][j].getNumber();
                int y = board[i][j - 1].getNumber();
                if (x == y) {
                    board[i][j].setNumber(2 * y);
                    board[i][j - 1] = new Tile();
                    score += 2 * y;
                }
                if (x + y == 2048) {
                    gameWon = true;
                }
            }
        }
    }

    // combines tiles upward
    public void combineUp() {
        for (int i = 3; i > 0; i--) {
            for (int j = 3; j > -1; j--) {
                int x = board[i][j].getNumber();
                int y = board[i - 1][j].getNumber();
                if (x == y) {
                    board[i][j].setNumber(2 * y);
                    board[i - 1][j] = new Tile();
                    score += 2 * y;
                }
                if (x + y == 2048) {
                    gameWon = true;
                }
            }
        }
    }

    // shifts tiles up if there's space (no combining)
    public void moveUp() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j].getNumber() == 0) {
                    board[i][j].setNumber(board[i + 1][j].getNumber());
                    board[i + 1][j].setNumber(0);
                }
            }
        }
    }

    // shifts tiles down if there's space (no combining)
    public void moveDown() {
        for (int i = 3; i > 0; i--) {
            for (int j = 3; j > -1; j--) {
                if (board[i][j].getNumber() == 0) {
                    board[i][j].setNumber(board[i - 1][j].getNumber());
                    board[i - 1][j].setNumber(0);
                }
            }
        }
    }

    // shifts tiles left if there's space (no combining)
    public void moveLeft() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getNumber() == 0) {
                    board[i][j].setNumber(board[i][j + 1].getNumber());
                    board[i][j + 1].setNumber(0);
                }
            }
        }
    }

    // shifts tiles right if there's space (no combining)
    public void moveRight() {
        for (int i = 3; i > -1; i--) {
            for (int j = 3; j > 0; j--) {
                if (board[i][j].getNumber() == 0) {
                    board[i][j].setNumber(board[i][j - 1].getNumber());
                    board[i][j - 1].setNumber(0);
                }
            }
        }
    }

    // ------------------------ GETTER FUNCTIONS
    // ----------------------------------

    // gets number value of a particular tile on the board
    public int getCell(int c, int r) {
        return board[r][c].getNumber();
    }

    // gets color of particular tile
    public Color getColor(int c, int r) {
        return board[r][c].getColor();
    }

    // gets text color of particular tile
    public Color getTextColor(int c, int r) {
        return Tile.getTextColor(getCell(c, r));
    }

    // returns current score
    public int getScore() {
        return score;
    }

    // returns list of highest scores
    public List<Integer> getLeaderScores() {
        return leaderScores;
    }

    // returns list of users corresponding to highest scores
    public List<String> getLeaderUsers() {
        return leaderUsers;
    }

    // returns boolean of whether or not game has been won
    public boolean gameWon() {
        return gameWon;
    }

    // ------------------ METHODS FOR TESTING -------------------------------

    // game board state as integer 2d array (for testing)
    public int[][] boardToInt() {
        int[][] output = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                output[i][j] = board[i][j].getNumber();
            }
        }
        return output;
    }

    // 2d int array to game board state
    public void intToBoard(int[][] arr) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j].setNumber(arr[i][j]);
                board[i][j].setColor();
            }
        }
    }

    // ------------------------ OTHER METHODS -------------------------
    // Undo move
    public void undo(int count) {
        while (count > 0) {
            if (history.size() > 1) {
                history.removeLast();
                board = history.getLast();
            } else if (history.size() == 1) {
                board = history.get(0);
            }

            if (scoreHist.size() > 1) {
                scoreHist.removeLast();
                score = scoreHist.getLast();
            } else if (scoreHist.size() == 1) {
                score = scoreHist.get(0);
            }
            count--;
        }
    }

    // check if game state has changed
    private boolean stateChanged() {
        if (!history.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (board[i][j].getNumber() != history.getLast()[i][j]
                            .getNumber()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // printGameState prints the current game state for debugging.
    public void printGameState() {
        System.out.println("\n Board:");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j].getNumber());
                if (j < 3) {
                    System.out.print(" | ");
                }
            }
            if (i < 3) {
                System.out.println("\n---------");
            }
        }
    }

    // ----------------------GAME OVER----------------------------------

    // check game over (lost)
    public boolean isGameOver() {
        if (!verticalMove() && !horizontalMove() || gameWon) {
            return true;
        }
        return false;
    }

    // check if any vertical moves can be made
    private boolean verticalMove() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int x = board[i][j].getNumber();
                int y = board[i + 1][j].getNumber();
                if (x == y || x == 0 || y == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // check if any horizontal moves can be made
    private boolean horizontalMove() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int x = board[i][j].getNumber();
                int y = board[i][j + 1].getNumber();
                if (x == y || x == 0 || y == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // ----------------------LEADERBOARD---------------------------------

    // initialize high score file
    private void fileInit() {
        File tempFile = new File("highscores.txt");
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // init leaderScores and leaderUsers
        if (leaderScores == null) {
            leaderScores = new ArrayList<Integer>();
            leaderUsers = new ArrayList<String>();
        }
        readHighscore();
    }

    // read high scores
    public void readHighscore() {
        try {
            FileReader r = new FileReader("highscores.txt");
            BufferedReader br = new BufferedReader(r);
            String line = "";
            int rank = 0;
            while ((line = br.readLine()) != null && rank <= 10) {
                int score = Integer.parseInt(line);
                String user = br.readLine();
                if (user == null) {
                    user = "unknown";
                }
                leaderScores.add(rank, score);
                leaderUsers.add(rank, user);
                rank += 1;
            }
            br.close();
        } catch (NumberFormatException e) {
            // create new file if file is bad
            new File("highscores.txt");
        } catch (IOException e) {
            fileInit();
        }
    }

    // update leaderScores and leaderUsers
    public void updateLeaders(String currUser, int currScore) {
        if (leaderScores == null || leaderScores.isEmpty()) {
            leaderScores = new ArrayList<Integer>();
            leaderUsers = new ArrayList<String>();
            leaderScores.add(currScore);
            leaderUsers.add(currUser);
        } else {
            for (int i = 0; i < leaderScores.size() && i < 10; i++) {
                if (currScore > leaderScores.get(i)) {
                    leaderScores.add(i, currScore);
                    leaderUsers.add(i, currUser);
                    while (leaderScores.size() > 10) {
                        leaderScores.remove(leaderScores.size() - 1);
                        leaderUsers.remove(leaderScores.size() - 1);
                    }
                    return;
                }
            }
            if (leaderScores.size() < 10) {
                leaderScores.add(currScore);
                leaderUsers.add(currUser);
            }
        }
        writeHighscore();
    }

    // overwrite high score file
    public void writeHighscore() {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter("highscores.txt", false));
            for (int i = 0; i < 10 && i < leaderScores.size(); i++) {
                bw.write(Integer.toString(leaderScores.get(i)));
                bw.newLine();
                bw.write(leaderUsers.get(i));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
        }
    }
}
