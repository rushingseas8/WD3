import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.ArrayList;

//A class that represents a single Location within a room.
public class Location {
    //Everything that is in this area. Torches, monsters, obstacles, dropped items, everything.
    private ArrayList<Object> occupants;

    //These two determine whether monsters (and you) can or can't walk here. 
    private boolean walkable;
    private boolean flyable;

    //Determines texture, walkable and flyable.
    private boolean isWall;
    private boolean isChasm;

    //Changed to be the ID of the room. 
    /*
     * 0 = normal floor
     * 1 = top edge; 2 = right edge; 3 = bottom edge; 4 = left edge; 
     * 5 = top-left corner; 6 = top-right corner; 7 = bottom-left corner; 8 = bottom-right corner;
     * 9 = connected north; 10 = connected east; 11 = connected south; 12 = connected west; 
     * 13 = T-shape down; 14 = T-shape left; 15 T-shape up; 16 = T-shape right;
     * 17 = left-to-right; 18 = top-to-bottom; 19 = no connections; 20 = connected all sides;
     */
    private int id = -1;

    //Determines the light level. Currently from 0-15.
    private int lightLevel;

    //Will be used for shading the texture of the area. Useful for lighting effects.
    private int redFilter, greenFilter, blueFilter, alphaFilter;

    //Toolkit used in everything related to graphics
    private Toolkit tk = Toolkit.getDefaultToolkit();

    //Not sure how to bind textures yet.. But this is the variable that holds this rooms default 
    //texture.
    private Image defaultTexture;

    //Used to determine how, if need be, the texture will be modified. 
    //Corner shapes = black overlay at an angle, Rounded shapes = special overlay, etc
    //private AreaShape shape;

    //Default constructor
    public Location() {
        walkable = true;
        flyable = true;

        //Default is no change to the original texture
        lightLevel = 15;

        //The percent light level, converted out of 255, to set the darkness
        int filter = (int)(((lightLevel+1)/16.0) * 255);

        //sets the shading to the proper level based on light
        redFilter = greenFilter = blueFilter = alphaFilter = filter;

        //Default texture is blackness
        defaultTexture = tk.getImage(getURL("black.png"));
    }

    //Commonly used constructor
    public Location(boolean wk, boolean fly, String textureName) {
        walkable = wk;
        flyable = fly;
        defaultTexture = tk.getImage(getURL(textureName));

    }
    //Sets this Location's texture to a String name of a texture in the folder.
    public Location setTexture(String textureName) {
        defaultTexture = tk.getImage(getURL(textureName));
        return this;
    }

    //Returns the texture for rendering reasons
    public Image getTexture() {
        return defaultTexture;
    }

    //Helper method that gets the location of this class, used in texture binding.
    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        }
        catch(Exception e) {
            try {
                url = this.getClass().getResource("unknown.png");
            } catch(Exception b) {
                System.out.println("Failed to load unknown texture.");
            }
            System.out.println("Failed to load texture: " + filename);
        }
        return url;
    }
    
    //Light beta method.
    public void shade(int lightLvl) {
        //Will be used later on when I figure out how to add different color shading.
        lightLevel = lightLvl;
        redFilter = greenFilter = blueFilter = (int)(((lightLevel+1)/16.0) * 255);
        //int filter = (redFilter * (256*256)) + (greenFilter * 256) + blueFilter;
        //double factor = filter / (256.0*256*256)+(256*256)+256;
        
        //Temporary variable used to darken the image
        BufferedImage temp = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = temp.createGraphics();
        g.drawImage(defaultTexture, 0, 0, null);
        g.dispose();
        
        //Darkens the image based on light level
        float scaleFactor = (lightLevel+1)/16.0f;
        RescaleOp op = new RescaleOp(scaleFactor, 0, null);
        temp = op.filter(temp, null);
        
        
    }

    //Marks this Location as a wall; default wall rendering (all-side wall)
    public void setWall(boolean to) {
        walkable = flyable = !to;
        isWall = to;
        id = 19; //Unconnected wall texture
    }

    //Specifies what this Location is. Wall texture, floor, etc
    public void setID(int id) {
        if (id>0 && id<=20) {
            walkable = flyable = false;
            isWall = true;       
        } 
        this.id = id;
    }

    //Returns what the render/type of this Location is
    public int getID() {
        return id;
    }

    //Returns whether or not this is a wall.
    public boolean isWall() {
        return isWall;
    }

    //Marks this Location as a chasm
    public void setChasm(boolean to) {
        walkable = flyable = !to;
        isChasm = to;
    }

    //Toggles walkable
    public void setWalkable(boolean to) {
        walkable = to;
    }

    //Toggles flyable
    public void setFlyable(boolean to) {
        flyable = to;
    }
}