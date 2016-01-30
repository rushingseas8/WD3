import java.awt.*;
import java.net.*;
import java.util.ArrayList;

//This is the object you control. :D
public class Player {
    //Shows you your location. You are not bound to any specific Location.
    //Instead, you get an X and a Y that are relative to the floor's base x and base y
    public int x, y;

    //Your current floor. Used for many things, including telling you when and where you can move.
    private Floor floor;

    //Maximum limits on where you can go.
    private int xLimit;
    private int yLimit;

    //The texture that is you.
    private Toolkit tk = Toolkit.getDefaultToolkit();;
    private Image texture;

    public Player(Floor f, int spawnX, int spawnY) {
        floor = f;
        x = spawnX;
        y = spawnY;
        texture = tk.getImage(getURL("player.png"));
        xLimit = floor.xEdge;
        yLimit = floor.yEdge;
    }

    //*****FIX THE FLOORS HAVING 7 TEXTURE WIDE BORDERS AND ALSO FIX THE PLAYERS MOVING THROUGH WALLS.

    //These four are used to determine if, when, and where you can move. The amount is in pixels.
    public void moveUp(int amount) {
        if (checkWalkable(x, y+amount-16)) {
            if (floor.y+amount <= yLimit) {
                floor.baseY+=amount;
                floor.y+=amount;
                y+=amount;
            }
        }
    }

    //Note: The 7x16 is due to the 7 texture boundary around every floor, and the other numbers are offsets used during drawing. These counteract them.
    //Also: added 16 to account for the size of the texture of the player itself.
    public void moveDown(int amount) {
        if (checkWalkable(x, y-amount-32)) {
            if (floor.y-amount >= 32) {        
                floor.baseY-=amount;
                floor.y-=amount;
                y-=amount;
            }
        }
    }

    public void moveRight(int amount) {
        if (checkWalkable(x-amount-32, y)) {        
            if (floor.x-amount >= 64) {        
                floor.baseX-=amount;
                floor.x-=amount;
                x-=amount;
            }
        }
    }

    public void moveLeft(int amount) {
        if (checkWalkable(x+amount-16, y)) {         
            if (floor.x+amount <= xLimit) {        
                floor.baseX+=amount;
                floor.x+=amount;
                x+=amount;
            }
        }
    }    

    //Returns true if you can walk in this location, false if you can't.
    private boolean checkWalkable(int x, int y) {
        if (floor.checkLocation(x, y) > 0 && floor.checkLocation(x, y) <= 20) {
            return false;
        }
        return true;
    }

    //Gives you the player's texture.
    public Image getTexture() {
        return texture;
    }

    //Helper method that gets the location of this class, used in texture binding.
    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        }
        catch(Exception e) {
            //try {
            url = this.getClass().getResource("unknown.png");
            //} catch(Exception b) {
            //    System.out.println("Failed to load unknown texture.");
            //}
            System.out.println("Failed to load texture: " + filename);
        }
        if (url == null) { System.out.println("URL is null."); }
        return url;
    }    
}