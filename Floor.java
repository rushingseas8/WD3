import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import java.util.ArrayList;

public class Floor {
    //This is the overall group of Locations on this floor.
    //public Location[][] theFloor;

    //The group of Rooms on this floor
    public Room[][] rooms;

    Room centerRoom;
    int centerRoomX, centerRoomY;

    //The size of the floor.
    //0 = 4x4
    //1 = 4x8
    //2 = 8x8
    //3 = 12x12
    int size;

    //What actual floor number this floor is, for calculating difficulty
    int floorNum;

    //The base x and y coordinates. Represent the top-left corner of the game arena. These move based on where the player is. 
    int baseX;
    int baseY;
    
    //The normal x and y coordinates. Used for player motion.
    int x, y;
    
    //The edges of the x-y plane. Where the player cannot go farther.
    int xEdge, yEdge;

    //***Fix tomorrow: Get rid of that damn boundary surrounding the rooms. It just makes calculations harder and the background is black anyway.
    public Floor(int sz, int fn) {
        size = sz;
        floorNum = fn;
        generateI();
        generateII();
        x = rooms.length * 9 * 16 + (8*16);// - 22; //Starting x and y; should be the center room location plus the offset to get the 
        y = rooms[0].length * 9 * 16 + (8*16);// + 10; //center location in the center room. 
        xEdge = rooms.length * 18 * 16 + 32;//The amount of locations times the size of their textures, plus a little offset
        yEdge = rooms[0].length * 18 * 16 + 16; //x + 41; y + 9
        System.out.println("INFO: the floor has been created");
    }

    //Stage 1 of generation. Instantiates the array of Locations, and Rooms.
    private void generateI() {
        System.out.println("INFO: generateI has started");
        switch(size) {
            case 0:
            System.out.println("INFO: detected size = 0");
            //Instantiates locations. The Floor gets a master plan of all the Locations on it, while the rooms get smaller copies that relate to
            //the 18x18 space they control. Each Room relays everything it does to the Floor whenever it does anything.
            //theFloor = new Location[72][72];
            for (int a = 0; a < 72; a++) {
                for (int b = 0; b < 72; b++) {
                    //theFloor[a][b] = new Location();
                }
            }
            System.out.println("INFO: instantiated size to a 72x72 of locations,");            

            //Instantiates rooms
            rooms = new Room[4][4];
            System.out.println("INFO: and to a 4x4 of rooms");
            break;

            case 1:
            //theFloor = new Location[86][158];
            for (int a = 0; a < 86; a++) {
                for (int b = 0; b < 158; b++) {
                    //theFloor[a][b] = new Location();
                }
            }
            rooms = new Room[4][8];
            //for(int a = 0; a < 4; a++) {
            //    for(int b = 0; b < 8; b++) {
            //        rooms[a][b] = new Room(floorNum, new RoomShape("square", 0));
            //    }
            //}               
            break;
            case 2:
            //theFloor = new Location[158][158];
            for (int a = 0; a < 158; a++) {
                for (int b = 0; b < 158; b++) {
                    //theFloor[a][b] = new Location();
                }
            }
            rooms = new Room[8][8];
            //for(int a = 0; a < 8; a++) {
            //    for(int b = 0; b < 8; b++) {
            //        rooms[a][b] = new Room(floorNum, new RoomShape("square", 0));
            //    }
            //}                 
            break;
            case 3:
            //theFloor = new Location[230][230];
            for (int a = 0; a < 230; a++) {
                for (int b = 0; b < 230; b++) {
                    //theFloor[a][b] = new Location();
                }
            }
            rooms = new Room[12][12];
            //for(int a = 0; a < 12; a++) {
            //    for(int b = 0; b < 12; b++) {
            //        rooms[a][b] = new Room(floorNum, new RoomShape("square", 0));
            //    }
            //}                
            break;
        }
        //The center room, ideally the start room. The game centers on this room initially.
        centerRoom = rooms[rooms.length/2][rooms[0].length/2];
        //Creates the start room
        rooms[rooms.length/2][rooms[0].length/2] = new Room(floorNum, new RoomShape("start", 0));
        
        //Darkens the start room. This is for testing purposes.
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                rooms[rooms.length/2][rooms[0].length/2].theRoom[i][j].shade(4);
            }
        }

        //The center coordinates. Coords of center room, converted to pixels, plus the initial size of window, plus a few balancing constants
        baseX = -((8*16) + ((rooms.length/2) * (18*16))) + 320 + 32 + 182;
        baseY = -((8*16) + ((rooms.length/2) * (18*16))) + 320 + 64 + 182;     

        centerRoomX = rooms.length/2; centerRoomY = rooms[0].length/2;
    }

    //Sets the base textures of each Location.
    //This is depricated, as the Floor gets its textures from the room.
    private void generateIIOld() {
        /*
         * Just for reference, this is how the edge textures are arranged
         * 51116
         * 4   2
         * 4   2
         * 4   2
         * 83337
         */

        String texture = "";
        //Adds the name of the floor

        //First two loops go through each room in the floor. i is the x, j is the y
        //         for (int i = 0; i < 4; i++) {
        //             for (int j = 0; j < 4; j++) {
        //                 //Second two loops go through each location in the room. k is x, l is y
        //                 for (int k = (i*18)+8; k < (i*18)+8+18; k++) {
        //                     for (int l = (j*18)+8; l < (j*18)+8+18; l++) {
        //                         texture = (getNameFromFloor(floorNum));                        
        //                         if (k-8 == (i*18) || k-8 == (i*18)+17 ||
        //                         l-8 == (j*18) || l-8 == (j*18)+17) {
        //                             texture = "unknown";
        //                         } else {
        //                             texture+=("Floor");
        // 
        //                             //Just in case there is special code to be added for different floor types
        //                             texture+=("1");
        //                         }
        // 
        //                         texture+=(".png");
        //                         theFloor[k][l].setTexture(texture);
        //                         System.out.println("Texture at " + k + ", " + l + " is " + texture);
        //                     }
        //                 }
        //             }
        //         }    

    }

    //Makes a few paths through the floor.
    private void generateII() {
        System.out.println("INFO: generateII has started");
        generateRecursive(centerRoomX, centerRoomY);

        //Updates all the locations in the floor, just to be safe
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] != null) {
                    rooms[i][j].generateIII(); //Updates all of the room's textures
                    for (int k = 0; k < 18; k++) {
                        for (int l = 0; l < 18; l++) {
                            //theFloor[(i*18) + k][(j*18) + l] = rooms[i][j].theRoom[k][l];
                        }
                    }
                }
            }
        }
        
        
    }

    //Temporary variables to help in recursive generation.
    private int tempX = -1, tempY = -1;

    //Recursive generation method. Takes a seed room's x and y as parameters.
    private void generateRecursive(int x, int y) {
        System.out.println("INFO: generateRecursive added a room at " + x + ", " + y);
        //Get an ArrayList of all the room's open connections
        ArrayList<Integer> openConnections = getOpenConnections(x, y);
        Random random = new Random();
        //Search through all the open connections,
        for (int dir: openConnections) {
            //if (chance), 
            //*update: added a "decay"; the more connections there are the less chance there is of making another
            if (random.nextInt(getDecayNumber(openConnections.size())) != 0) {
                //getRoomInDir(arraylist of connections),
                getRoomInDir(x, y, dir);

                //If there isn't already a room there
                if (rooms[tempX][tempY] == null) {
                    //make room there (default shape),                    
                    rooms[tempX][tempY] = new Room(floorNum, new RoomShape("default", 0));

                    //Sets connections, but only if there isn't a room already there
                    switch(dir) {
                        case 0: rooms[x][y].connectedNorth = true; rooms[tempX][tempY].connectedSouth = true; break;
                        case 1: rooms[x][y].connectedEast = true; rooms[tempX][tempY].connectedWest = true; break;
                        case 2: rooms[x][y].connectedSouth = true; rooms[tempX][tempY].connectedNorth = true; break;
                        case 3: rooms[x][y].connectedWest = true; rooms[tempX][tempY].connectedEast = true; break;                    
                    }                    
                }

                //generateRecursive(new room)     
                //rooms[tempX][tempY].generateIII();                
                generateRecursive(tempX, tempY);
            }         
        }

        //Updates all textures
        //rooms[x][y].generateIII();
    }

    //Provides the decay factor based on how many connections there are and the size of the floor.
    private int getDecayNumber(int numConnections) {
        switch(size) {
            case 0:
            switch(numConnections) {
                case 0: return 0; //Can't have this, ever
                case 1: return 3; //Default amount of decay
                case 2: return 4; //Bit harder to get a split
                case 3: return 6; //Tough to get a 4 way
                case 4: //Doesn't matter
            }
            break;
            case 1:
            switch(numConnections) {
                case 0: return 0; 
                case 1: return 3; 
                case 2: return 3; 
                case 3: return 5; 
                case 4: 
            }
            break;
            case 2:
            switch(numConnections) {
                case 0: return 0; 
                case 1: return 2; //Encourages getting rid of dead ends
                case 2: return 4; 
                case 3: return 4; 
                case 4: 
            }
            break;                
            case 3:
            switch(numConnections) {
                case 0: return 0; 
                case 1: return 2; //Encourages getting rid of dead ends in large dungeons
                case 2: return 2; //Ditto
                case 3: return 3; 
                case 4: 
            }
            break;            
        }
        return 100;
    }

    //Updates tempX and tempY to the location of the room in the given direction.
    //Again, for some reason, the x and y variables here have to b swapped. Possibly due to the way Java draws graphics.
    private void getRoomInDir(int x, int y, int direction) {
        tempX = x; tempY = y;
        switch(direction) {
            //             case 0: tempX = x-1; break; 
            //             case 1: tempY = y+1; break;
            //             case 2: tempX = x+1; break;
            //             case 3: tempY = y-1; break;
            case 0: tempY = y-1; break;
            case 1: tempX = x+1; break;
            case 2: tempY = y+1; break;
            case 3: tempX = x-1; break;
        }
    }

    //Checks if the direction being searched in is within the array of rooms.
    //Yet again, as above, x and y flipflop.    
    private boolean isValidInDir(int x, int y, int direction) {
        switch(direction) {
            //             case 0: if ((x - 1 < 0)) return false;
            //             case 1: if ((y + 1 >= rooms[0].length)) return false;
            //             case 2: if ((x + 1 >= rooms.length)) return false;
            //             case 3: if ((y - 1 < 0)) return false;
            case 0: if ((y - 1 < 0)) return false;
            case 1: if ((x + 1 >= rooms.length)) return false;
            case 2: if ((y + 1 >= rooms[0].length)) return false;
            case 3: if ((x - 1 < 0)) return false;

        }
        return true;
    }

    //Returns a scrambled arraylist of directions in which there are no rooms.
    private ArrayList<Integer> getOpenConnections(int x, int y) {
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            //If the location being searched is in rooms
            if (isValidInDir(x, y, i)) {
                //Sets the temporary x, y storage to the room in the given direction
                getRoomInDir(x, y, i);
                //If this room's location is empty
                if (rooms[tempX][tempY] == null) {
                    //Add it to the list
                    toReturn.add(i);
                }
            }
        }
        //Shuffles the ArrayList
        java.util.Collections.shuffle(toReturn, new Random(System.nanoTime()));   
        return toReturn;
    }
    
    //Checks the given coordinates' corresponding Location, returns ID of it
    public int checkLocation(int x, int y) {
        int x1 = x; int y1 = y;
        x1-=22; y1+=10;
        //return theFloor[x1/16][y1/16].getID();
        return rooms[x1/18][y1/18].theRoom[x1/18/16][y1/18/16].getID();
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

    //These four methods are used to move the base x and y variables. This is used for drawing purposes, as the
    //base x and y determine what part of the game arena will be drawn.
    public void moveRight(int amount) {
        baseX-=amount;
    }

    public void moveLeft(int amount) {
        baseX+=amount;
    }

    public void moveUp(int amount) {
        baseY+=amount;
    }

    public void moveDown(int amount) {
        baseY-=amount;
    }
}