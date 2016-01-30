
//A class that helps to determine how to generate a room. 
//Has a shape that's defined below, and a direction.
public class RoomShape {
    /*
     * In the generationCode:
     * 0 = empty space/floor
     * 1 = top edge
     * 2 = right edge
     * 3 = bottom edge
     * 4 = left edge
     * 5 = top-left corner
     * 6 = top-right corner
     * 7 = bottom-right corner
     * 8 = botton-left corner
     * 
     */

    //0 = facing north, 1 = facing east, 2 = facing south, 3 = facing west
    int direction;

    private int[][] generationCode = new int[18][18];

    public RoomShape(String shape, int direction) {
        //Instantiates the array
        for (int a = 0; a < 18; a++) for (int b = 0; b < 18; b++) generationCode[a][b] = 0;

        switch(shape) {
            case "start": generateSquare(); addStart(); break;
            default: /*"square":*/ generateSquare(); break;
        }
    }

    //Makes a basic square shape
    private void generateSquare() {
        //i is the x variable, j is the y variable; bottom-right quadrant on a graph.
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                if (i == 0) {
                    generationCode[i][j] = 1; //Top edge                    
                    if (j == 0) {
                        generationCode[i][j] = 5; //Top-left corner
                    }
                    else if (j == 17) {
                        generationCode[i][j] = 6; //Top-right corner
                    }
                }
                else if (i == 17) {
                    generationCode[i][j] = 3; //Bottom edge                       
                    if (j == 0) {
                        generationCode[i][j] = 8; //Bottom-left corner
                    }
                    else if (j == 17) {
                        generationCode[i][j] = 7; //Bottom-right corner
                    }            
                }
                else if (j == 0) { 
                    generationCode[i][j] = 4; //Left edge                      
                    if (i == 0) {
                        generationCode[i][j] = 5; //Top-left corner                        
                    } 
                    else if (i == 17) {
                        generationCode[i][j] = 8; //Bottom-left corner                        
                    }                
                }
                else if (j == 17) {
                    generationCode[i][j] = 2; //Right edge                    
                    if (i == 0) {
                        generationCode[i][j] = 6; //Top-right corner
                    }
                    else if (i == 17) {
                        generationCode[i][j] = 7; //Bottom-right corner                        
                    }                   
                }
            }
        }
    }

    //Adds to the basic square shape the items needed for a start room. 
    private void addStart() {

    }

    public int[][] getGenerationCode() {
        //for (int a = 0; a < 18; a++) {
        //    for (int b = 0; b < 18; b++) {
        //        System.out.print(generationCode[a][b] + " ");
        //    }
        //    System.out.println();
        //}
        return generationCode;
    }
}