import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import java.util.ArrayList;

//An individual room. A floor contains several of these. Each contains an
//18x18 array of Locations.
public class Room {
    //The array of Locations this room controls
    public Location[][] theRoom = new Location[18][18];

    //Floor number
    private int floorNum;

    //Whether or not this room was discovered, and should show up on the arena.
    private boolean isHidden;

    //Used as storage for generation
    private RoomShape shape;
    
    //The center tile of this room. Useful for Player spawning.
    public Location centerTile;
    
    //Whether or not there is a room in this direction.
    public boolean connectedNorth = false, connectedEast = false,
        connectedSouth = false, connectedWest = false;

    public Room(int fn, RoomShape shp) {
        floorNum = fn;
        shape = shp;
        isHidden = false;

        generateI();
        generateII();
        
        centerTile = theRoom[9][9];
    }

    //Instantiates all the Locations
    private void generateI() {
        System.out.println("INFO: Room generateI has started");
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                theRoom[i][j] = new Location();
            }
        }
    }

    //Generates the walls and such based on the shape of the room
    private void generateII() {
        System.out.println("INFO: Room generateII has started");
        int[][] generationCode = shape.getGenerationCode();

        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                switch(generationCode[i][j]) {
                    //Sets texture to a floor texture
                    case 0: 
                    theRoom[i][j].setTexture(getNameFromFloor(floorNum) + "Floor1.png");
                    //Used to print out floor textures, will change with new rooms
                    //System.out.println("The texture at " + i + ", " + j + " is " + getNameFromFloor(floorNum) + "Floor1.png");
                    break;
                    //Sets texture to a wall texture
                    default: 
                    if (generationCode[i][j] > 0 && generationCode[i][j] < 21) {
                        theRoom[i][j].setID(shape.getGenerationCode()[i][j]); //*Changed Location so that ID matches with GenCode
                        theRoom[i][j].setTexture(getNameFromFloor(floorNum) + "Wall" + ((shape.getGenerationCode()[i][j]) - 1) + ".png");
                        //This was used to print out wall textures for debugging; will come back with new generation codes
                        //System.out.println("The texture at " + i + ", " + j + " is " + 
                        //    getNameFromFloor(floorNum) + "Wall" + ((shape.getGenerationCode()[i][j]) - 1) + ".png");  
                        //break;
                    }
                }
            }
        }
    }
    
    //Sets up textures for doors based on connections. Call this to update the room.
    public void generateIII() {
        System.out.println("INFO: updating a room's door textures..");
        if (connectedNorth) {
            theRoom[0][8].setTexture(getNameFromFloor(floorNum) + "Door1a.png");
            theRoom[0][9].setTexture(getNameFromFloor(floorNum) + "Door1b.png");
        }
        if (connectedEast) {
            theRoom[8][17].setTexture(getNameFromFloor(floorNum) + "Door2a.png");
            theRoom[9][17].setTexture(getNameFromFloor(floorNum) + "Door2b.png");            
        }
        if (connectedSouth) {
            theRoom[17][8].setTexture(getNameFromFloor(floorNum) + "Door3a.png");
            theRoom[17][9].setTexture(getNameFromFloor(floorNum) + "Door3b.png");            
        }
        if (connectedWest) {
            theRoom[8][0].setTexture(getNameFromFloor(floorNum) + "Door4a.png");
            theRoom[9][0].setTexture(getNameFromFloor(floorNum) + "Door4b.png");    
        }
    }

    //Helps assign wall types
    private int wallRenderHelper(int x, int y, Location[][] room) {
        //Boolean flags. True if there is a wall in the direction.
        boolean north = false, east = false, south = false, west = false;

        //Do-not-check flags. True if the direction is out of bounds, do not check there if true.
        boolean north2 = false, east2 = false, south2 = false, west2 = false;

        //These four establish where not to look, helps avoid errors.
        if (x - 1 < 0) { north2 = true; }        
        if (x + 1 > 17) { south2 = true; }
        if (y - 1 < 0) { west2 = true; }
        if (y + 1 > 17) { east2 = true; }

        //These four check in each direction. 
        if (!north2) { if (room[x-1][y].isWall()) { north = true; } }
        if (!south2) { if (room[x+1][y].isWall()) { south = true; } }
        if (!west2) { if (room[x][y-1].isWall()) { west = true; } }
        if (!east2) { if (room[x][y+1].isWall()) { east = true; } }

        //Checks for all possibilities 
        //These four are the edges
        if (east && west && north2 && !south2) { return 0; }
        if (north && south && east2 && !west2) { return 1; }
        if (east && west && south2 && !north2) { return 2; }
        if (north && south && west2 && !east2) { return 3; }

        //These four are corners
        if (east && south && !north && !west) { return 4; }
        if (west && south && !north && !east) { return 5; }
        if (north && west && !south && !east) { return 6; }
        if (north && east && !south && !west) { return 7; }

        //These four are single connections
        if (north && !east && !south && !west) { return 8; }
        if (east && !north && !south && !west) { return 9; }
        if (south && !north && !east && !west) { return 10; }
        if (west && !north && !east && !south) { return 11; }

        //These four are t-shaped connections
        if (!north && east && south && west) { return 12; }
        if (north && !east && south && west) { return 13; }
        if (north && east && !south && west) { return 14; }
        if (north && east && south && !west) { return 15; }        

        //Last four are other
        if (west && east && !north && !south) { return 16; }
        if (north && south && !west && !east) { return 17; }
        if (!north && !east && !south && !west) { return 18; }
        if (north && south && east && west) { return 19; }

        return -1;
    }

    //A helper method that returns the warped levels based on the floor number.
    private String getNameFromFloor(int floorNum) {
        if (floorNum >= 0 && floorNum <= 20) {
            return "Frozen";
        } else if (floorNum >= 21 && floorNum <= 40) {
            return "Abandoned";
        } else if (floorNum >= 41 && floorNum <= 60) {
            return "Furnished";
        } else if (floorNum >= 61 && floorNum <= 80) {
            return "AbandonedII";
        } else if (floorNum >= 81 && floorNum <= 100) {
            return "Warped";
        } else if (floorNum >= 101 && floorNum <= 120) {
            return "Cosmic";
        } else if (floorNum >= 121 && floorNum <= 150) {
            return "WarpedII";
        } else if (floorNum >= 151 && floorNum <= 200) {
            return "Challenge";
        } else {
            return "null";
        }
    }    
}