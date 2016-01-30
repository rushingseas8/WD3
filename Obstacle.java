import java.awt.*;
import java.net.*;
import java.util.ArrayList;

//A class representing all obstacles that can be in a location.
//These include fog covering the area, boulders, ice, and many more.
public class Obstacle {
    //The texture of this obstacle.
    private Image texture;
    
    //Toolkit used in getting textures.
    private Toolkit tk;
    
    //Whether or not this changes the walkability of this Location.
    private boolean isSolid;

    public Obstacle() {
        texture = tk.getImage(getURL("black.png"));
        isSolid = false;
    }
    
    public Obstacle(String Texture, boolean solid) {
        texture = tk.getImage(getURL(Texture));
        isSolid = solid;
    }
    
    public void setTexture(String Texture) {
        texture = tk.getImage(getURL(Texture));
    }
    
    public Image getTexture() {
        return texture;
    }
    
    public void setSolidity(boolean sol) {
        isSolid = sol;
    }
    
    public boolean getSolidity() {
        return isSolid;
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
}