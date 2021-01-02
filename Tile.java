import java.awt.Color;
import java.util.Random;

public class Tile {

    // FIELDS

    private int number;
    private Color color;

    // CONSTRUCTORS

    public Tile() {
        this.number = 0;
        this.color = getColor(0);
    }

    public Tile(int num) {
        this.number = num;
        this.color = getColor(num);
    }

    // METHODS

    // -------------GETTER FUNCTIONS-------------------------------

    // get the number on tile
    public int getNumber() {
        return number;
    }

    // get color of tile
    public Color getColor() {
        return color;
    }

    // -------------SETTER FUNCTIONS-------------------------------

    // set number on tile
    public void setNumber(int n) {
        this.number = n;
    }

    // reset tiles to empty, default state
    public void resetTile() {
        this.number = 0;
        this.color = getColor(0);
    }
    
    // set color
    public void setColor() {
        this.color = getColor(this.number);
    }

    // -------------METHODS-------------------------------

    // get color corresponding to number
    public static final Color getColor(int n) {
        Color color = null;
        switch (n) {
            case 2 :
                return new Color(238, 228, 218);
            case 4 :
                return new Color(238, 225, 201);
            case 8 :
                return new Color(243, 178, 122);
            case 16 :
                return new Color(247, 124, 95);
            case 32 :
                return new Color(246, 150, 100);
            case 64 :
                return new Color(247, 95, 59);
            case 128 :
                return new Color(237, 206, 113);
            case 256 :
                return new Color(237, 204, 99);
            case 512 :
                return new Color(249, 202, 88);
            case 1024 :
                return new Color(239, 198, 58);
            case 2048 :
                return new Color(98, 194, 6);
            default :
                break;
        }
        return color;
    }
    
    // get text color of tile
    public static final Color getTextColor(int n) {
        if (n <= 4) {
            return new Color(119, 110, 101);
        } else {
            return new Color(249, 246, 242);
        }
    }
    
    // new random tile (prob 0.1 of being 4, 0.9 of being 2) 
    public void randTile() {
        Random r = new Random();
        int num = r.nextInt(10) == 0 ? 4 : 2;
        this.number = num;
        this.color = getColor(num);
    }

    

}
