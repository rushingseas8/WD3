import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;

public class Game extends JFrame implements Runnable, KeyListener {
    //Double buffering objects. You draw to this image using this graphics object.
    BufferedImage backbuffer;
    Graphics2D g2d;

    //The main Thread that this game runs in.
    Thread gameloop;

    //The toolkit that the game uses to load images from a file.
    Toolkit tk = Toolkit.getDefaultToolkit();    

    //Currently unused. Will be used for holding multiple keys in memory, for multiple key press support
    private final Set<Character> pressed = new HashSet<Character>();

    //The Floor that is currently in use.
    Floor floor = new Floor(0,1);

    //This is you.
    Player player = new Player(floor, floor.x, floor.y);

    //An integer variable used to hold the currently pressed key.
    int keyCode;

    //Debugging variables.
    //Whether or not to display the debug menu.
    boolean debug = true;    
    //Counts how many ticks have passed since the first draw (game start)
    int runLoopDisplayCounter = 0;    
    //Allows for free roaming camera. Default true in alpha.
    boolean freeCamera = true;

    //Shows minimap
    boolean showMipMap = true;

    //Main method. Starts everything.
    public static void main() {
        new Game();
    }

    //Creates the window for the game, starts a key listener, starts the gameloop up.
    public Game() {
        //Creates the window.
        super("Warping Dungeons 2D Alpha");
        //setSize(640,640);
        setSize((int)tk.getScreenSize().getWidth(), (int)tk.getScreenSize().getHeight() );
        setUndecorated(true);        
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  
        
        //Adds key listener.
        addKeyListener(this);        

        //Begins the game thread.
        gameloop = new Thread(this);
        gameloop.start();       
    }

    //Goes deeper
    private void goDeeper() {
        floor = new Floor(floor.size, floor.floorNum + 1);
        player = new Player(floor, floor.x, floor.y);
    }

    //Goes higher
    private void goHigher() {
        floor = new Floor(floor.size, floor.floorNum - 1);
        player = new Player(floor, floor.x, floor.y);        
    }

    //Toggles whether or not free camera is on.
    private void toggleCamera() {
        freeCamera = !freeCamera;
    }

    //Centers the view on the default position.
    //**Make the floor.x and floor.y affected by this, too
    private void center() {
        floor.baseX = -((8*16) + ((floor.rooms.length/2) * (18*16))) + 320 + 32 + 182;
        floor.baseY = -((8*16) + ((floor.rooms.length/2) * (18*16))) + 320 + 64 + 182;        
    }

    //Draws the viewport and everything in it.
    public void paint(Graphics graph) {  
        //Calls to redraw to the BufferedImage background.
        update();        
        //Draws the BufferedImage to the foreground.
        graph.drawImage(backbuffer, 0, 0, this);
    }

    //Helper method that loads the URL and handles erros. Used for rendering and loading images.
    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        }
        catch(Exception e) {}
        return url;
    }    

    //The main loop that runs the game itself. 
    public void run() {
        Thread t = Thread.currentThread();
        while (t == gameloop) {
            repaint();
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }        
        //Debugging, this tells you when the game is over for any reason except when you close it.
        System.out.println("*****Game stopped*****");
    }

    //Updates the background.
    public void update() {
        //Creates a new image that is then drawn to. This image is the size of the window, allowing for resizing.
        backbuffer = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();

        //Draws the background.
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,getSize().width,getSize().height);

        //Draws the floor itself in the appropriate place.
        //drawFloor();

        //Draws the floor, by drawing all the Rooms and their Locations.
        drawFloorB();

        //Draws you; you're the center of the game. Woo!
        drawPlayer();

        //Draws the debug information.
        if (debug) { drawDebug(); }

        //Draws the minimap.
        if (showMipMap) { drawMipMap(); }

        //Updates the tick counter, for debugging purposes.
        runLoopDisplayCounter++;
    }

    //Helper method that draws the floor itself.
    /*
    private void drawFloor() {
        //This draws every Location in the currently used floor's array of Locations, by invoking their default textures.
        for (int i = 0; i < floor.theFloor.length; i++) {
            for (int j = 0; j < floor.theFloor[0].length; j++) {
                g2d.drawImage(floor.theFloor[i][j].getTexture(),
                    floor.baseX + i*16, floor.baseY + j*16, this);
            }
        }            
    }
    */

    //Helper method that draws the floor. Uses the new Rooms.
    private void drawFloorB() {
        for (int i = 0; i < floor.rooms.length; i++) {
            for (int j = 0; j < floor.rooms[0].length; j++) {
                for (int k = 0; k < 18; k++) {
                    for (int l = 0; l < 18; l++) {
                        //For some reason, I'm guessing due to the way Java draws things, the x and y variables for rooms have to be
                        //switched for the game to draw the textures in the right orders. At any rate, this works.
                        if (floor.rooms[i][j] != null) {
                            g2d.drawImage(floor.rooms[i][j].theRoom[k][l].getTexture(),
                                floor.baseX + i*18*16 + l*16, floor.baseY + j*18*16 + k*16, this);                
                        }
                    }
                }
            }
        }
    }

    //Helper method that draws useful information.
    private void drawDebug() {
        g2d.setColor(Color.WHITE);
        g2d.drawString("Warping Dungeons Alpha", 10, 40);
        g2d.drawString("Last key pressed: " + keyCode, 10, 55);   
        g2d.drawString("Level: " + floor.floorNum + " ('-' to go down/'+' to go up)", 10, 70);
        g2d.drawString("Ticks: " + runLoopDisplayCounter, 10, 85);  
        g2d.drawString("Seconds: " + runLoopDisplayCounter/50.0, 10, 100);  
        g2d.drawString("baseX: " + floor.baseX, 10, 115);
        g2d.drawString("baseY: " + floor.baseY, 10, 130); 
        g2d.drawString("Player's X: " + player.x, 10, 145);
        g2d.drawString("Player's Y: " + player.y, 10, 160);        
        
        int offset1 = 160;
        if (freeCamera) {
            g2d.drawString("Free camera controls:", 10, offset1+15);
            g2d.drawString(" - Arrow keys to move", 10, offset1+30);    
            g2d.drawString(" - 'q' to toggle camera", 10, offset1+45);
            g2d.drawString(" - 'w' to go to center room", 10, offset1+60); 
        } else {
            g2d.drawString("Normal camera controls:", 10, offset1+15);
            g2d.drawString(" - Arrow keys to move", 10, offset1+30);    
            g2d.drawString(" - 'q' to toggle camera", 10, offset1+45);
            //g2d.drawString(" - 'w' to go to center room", 10, 160);             
        }
    }

    //Draws the minimap
    private void drawMipMap() {
        g2d.drawImage(tk.getImage(getURL("mipMapFrame.png")), 10, getSize().height - 120, null);
        g2d.setColor(Color.WHITE);

        //Variables to offset the x and y variables so that the minimap is properly centered & rendere
        int xBoost = 0; int yBoost = 0;

        switch(floor.size) {
            case 0: xBoost = yBoost = 4; break;
            case 1: xBoost = 4; yBoost = 2; break;
            case 2: xBoost = yBoost = 2; break;
            case 3: xBoost = yBoost = 0; break;
        }

        
        for (int i = 0; i < 12-(2 * xBoost); i++) {
            for (int j = 0; j < 12-(2 * yBoost); j++) {
                if (floor.rooms[i][j] != null) {
                    g2d.drawImage(tk.getImage(getURL("mipMapRoom" + getMipMapRoomTexture(floor.rooms[i][j]) + ".png")),
                        10 + 1 + ((i + xBoost) * 9), getSize().height - 120 + 1 + ((j + yBoost) * 9), null);
                }
            }
        }
    }

    //Helps determine what room texture to draw for the minimap.
    //0 = north, 1 = east, 2 = south, 3 = west, 4 = eastt&south, 5 = west&south, 6 = north&west, 7 = north&east
    //8 = T shape down, 9 = T shape left, 10 = T shape up, 11 = T shape right, 12 = west&east, 13 = north&south, 14 = all
    private int getMipMapRoomTexture(Room r) {
        if (r.connectedNorth) {
            if (r.connectedEast) {
                if (r.connectedSouth) {
                    if (r.connectedWest) {
                        return 14;
                    }
                    return 11;
                }
                else if (r.connectedWest) {
                    return 10;
                }
                return 7;
            }
            else if (r.connectedSouth) {
                if (r.connectedWest) {
                    return 9;
                }
                return 13;
            }
            else if (r.connectedWest) {
                return 6;
            }
            return 0;
        }
        else if (r.connectedEast) {
            if (r.connectedSouth) {
                if (r.connectedWest) {
                    return 8;
                }
                return 4;
            }
            else if (r.connectedWest) {
                return 12;
            }
            return 1;
        }
        else if (r.connectedSouth) {
            if (r.connectedWest) {
                return 5;
            }
            return 2;
        }
        else if (r.connectedWest) {
            return 3;
        }

        return -1;
    }

    //Draws you.
    private void drawPlayer() {
        g2d.drawImage(player.getTexture(), 320-16, 320-16, null);
    }

    //Turns the debug menu on or off.
    private void toggleDebug() {
        debug = !debug;
    }

    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();
        //Free camera controls
        if (freeCamera) {
            switch(keyCode) {
                case KeyEvent.VK_RIGHT: floor.moveRight(16); floor.x-=16; player.x-=16; repaint(); break;
                case KeyEvent.VK_LEFT: floor.moveLeft(16); floor.x+=16; player.x+=16; repaint(); break;
                case KeyEvent.VK_UP: floor.moveUp(16); floor.y+=16; player.y+=16; repaint(); break;
                case KeyEvent.VK_DOWN: floor.moveDown(16); floor.y-=16; player.y-=16; repaint(); break;
                case 87 /* 'w' */: center(); repaint(); break;
            }
        }        
        //Normal camera controls
        else {
            switch(keyCode) {
                case KeyEvent.VK_RIGHT: player.moveRight(4); repaint(); break;
                case KeyEvent.VK_LEFT: player.moveLeft(4); repaint(); break;
                case KeyEvent.VK_UP: player.moveUp(4); repaint(); break;
                case KeyEvent.VK_DOWN: player.moveDown(4); repaint(); break;                      
            }   
        }

        //Controls that are there no matter what.
        switch(keyCode) {
            case 81 /* 'q' */: toggleCamera(); repaint(); break;            
            case 192 /* '`' */: toggleDebug(); repaint(); break;
            case 45 /* '-' */: goDeeper(); repaint(); break;
            case 61 /* '= ' */: goHigher(); repaint(); break;          
        }
        
        if (keyCode == (KeyEvent.VK_ESCAPE)) { setVisible(false); try { Thread.sleep((int)(Math.random() * 2000)); } catch (Exception f) {} setVisible(true); }

        repaint();
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {
        keyCode = e.getKeyChar();
        switch(keyCode) {
            case KeyEvent.VK_RIGHT: floor.moveRight(5); repaint(); break;
            case KeyEvent.VK_LEFT: floor.moveLeft(5); repaint(); break;
            case KeyEvent.VK_UP: floor.moveUp(5); repaint(); break;
            case KeyEvent.VK_DOWN: floor.moveDown(5); repaint(); break;
        }       
        repaint();
    }    
}