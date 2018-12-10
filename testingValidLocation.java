import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class testingValidLocation {


    public static void main(String[] args) {
        Board b = new Board();
        b.placeWord("kiss", Location.CENTER, Location.VERTICAL);
        b.placeWord("aws", new Location(9,8), Location.HORIZONTAL);
        b.placeWord("succes", new Location(3,10), Location.VERTICAL);
        int[][][] lengths = findValidLocations(b);
        for(int i = 0; i< 15; i++){
            StdOut.println();
            for(int j = 0; j<15; j++){
                if(i < 10){ StdOut.print(" ");}
                if(j < 10){ StdOut.print(" ");}
                StdOut.print(i + "," + j + ": " + Arrays.toString(lengths[i][j]) + "  ");
            }
        }
        StdOut.println();
        ArrayList<Location[]> priority = createPriority(lengths,7, 5);
        StdOut.println(priority.size());
       // priority = createPriority(lengths,7);
       // StdOut.println(priority.size());
        /*for(int i = 0; i< 10; i++) {
            Location[] check = priority.get(i);
            StdOut.println(check[0].getRow() + " " + check[0].getColumn());
        }*/
        int size = 7;
        ArrayList<ArrayList<Location[]>> finalLocations = new ArrayList<ArrayList<Location[]>>(15*15);
        for(int i = 0; i<= size; i++){
            priority = createPriority(lengths,7, i);
            StdOut.println(priority.size() + " " + i);
            finalLocations.add(priority);
        }
        StdOut.println(finalLocations.get(6).get(8)[0].getRow() + " " + finalLocations.get(6).get(8)[0].getColumn());

    }

    /**
     * Helper method to find valid locations of full strings with a space at the beginning or end
     * @param lengths - array of size lengths from findValidLocations
     * @return
     */
    public static ArrayList<Location[]> createPriority(int[][][] lengths, int size){ // Finds priority of length of  a string in one chunk
        ArrayList<Location[]> priority = new ArrayList<Location[]>(15*15);
        for(int i = 0; i<15; i++){
            for(int j = 0; j<15; j++){
                Location[] location = new Location[2];
                if((lengths[i][j][0] >= size && size >= lengths[i][j][1])){
                    location[0] = new Location(i,j);
                    location[1] = Location.HORIZONTAL;
                    priority.add(location);
                }
                if((lengths[i][j][2] >= size && size >= lengths[i][j][3])){
                    location[0] = new Location(i,j);
                    location[1] = Location.VERTICAL;
                    priority.add(location);
                }
            }
        }
        return  priority;
    }

    /**
     * Creates a priority lists of locations to check based on a size of word and location of a space in the word
     * @param lengths - array of size lengths from findValidLocations
     * @param size - size of string trying to check
     * @param space - location of single space in a string
     * @return - all valid locations with the given space alignment, a 2D location array because [0] is the location and [1] is direction
     */

    public static ArrayList<Location[]> createPriority(int[][][] lengths, int size, int space) { // Finds priority of length of  a string in one chunk
        //if(space == 0 || space == size){ // helper method needed for when the beginning or end is a space
        //    return createPriority(lengths,size);
        //}
        ArrayList<Location[]> priority = new ArrayList<Location[]>(15 * 15);
        int split = size-space;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Location[] location = new Location[2];
                if (lengths[i][j][0] == space && j+space+1 < 15) {    //checks if the max length of that location corresponds to the length before space
                    if(lengths[i][j+space+1][0] >=split && split >= lengths[i][j+space+1][1]) {  // sees if the spot after the space can handle the remaining letters
                        location[0] = new Location(i, j);
                        location[1] = Location.HORIZONTAL;
                        priority.add(location);
                    }
                }
                if (lengths[i][j][2] == space && i+space+1<15) {    // same process vertically
                    if(lengths[i+space+1][j][2] >=split && split >= lengths[i+space+1][j][3]) {
                        location[0] = new Location(i, j);
                        location[1] = Location.VERTICAL;
                        priority.add(location);
                    }
                }
            }
        }
        return priority;
    }

    /**
     * This method looks over the board and for every location determines what the maximum and minimum play length at that location.
     * This is done in both horizontal and vertical direction. These 4 values are kept in an array and the method returns that array.
     * @param b - current board
     * @return lengths - a dD array with [row][column][4] where the 4 values are maxHorizontal, minHorizontal, maxVertical, and minVertical
     */
        public static int[][][] findValidLocations(Board b){
        int[][][] lengths = new int[15][15][4];
        for(int i = 0; i<15; i++){
            for(int j = 0; j<15; j++){
                Location current = new Location(i,j);
                if(b.isOccupied(current)){ // if occupied, can't play there
                    for(int k = 0; k<4; k++){
                        lengths[i][j][k] = 0;
                    }
                }
                else{//check path
                    int[] check;
                    check = findEmptyPath(b,current,Location.HORIZONTAL);
                    lengths[i][j][0] = check[0];
                    lengths[i][j][1] = check[1];
                    check = findEmptyPath(b,current,Location.VERTICAL);
                    lengths[i][j][2] = check[0];
                    lengths[i][j][3] = check[1];
                }
            }
        }
        return lengths;
    }

    /**
     * This helper method starts at a location and goes in a direction until it reaches an occupied tile, runs off the board, or exceeds max length
     * It returns a 2D array that contains the maximum and minimum length of a word that can be there
     * @param b - current board state
     * @return - max and min in given direction
     */
    public static int[] findEmptyPath(Board b, Location start, Location direction){
        int min = -1; // minimum length needed to play off
        int index = 0;
        Location current = start;
        while(index<8){
            if(!current.isOnBoard()){ // ran off board
                if(min == -1){
                    index = 8;
                    break;
                }
                else{ break; }
            }
            if(b.isOccupied(current)){ break;} // found a tile
            Location neighbor1 = current.neighbor(direction.opposite());
            Location antineighbor2 = current.antineighbor(direction.opposite());
            if(index == 0 && current.antineighbor(direction).isOnBoard() && b.isOccupied(current.antineighbor(direction))){ // check if it's playing off a word to the left or above
                min = 1;
            }
            boolean check1 = neighbor1.isOnBoard() && b.isOccupied(neighbor1);
            boolean check2 = antineighbor2.isOnBoard() && b.isOccupied(antineighbor2);
            if(min == -1 && (check1 || check2)){ // sees if there are adjacent tiles to play off of and sets the minimum distance
                min = index+1;
                if(min == 8) { min = -1;}
            }
            index++;                                // moves to next location
            current = current.neighbor(direction);
        }

        int[] values = new int[2];
        if(min == -1 && index == 8){ //cannot play off
            values[0] = 0;
            values[1] = 0;
        }
        else if (min == -1 && index < 8){ // ends by ramming into a word, but nothing before then
            values[0] = index;
            values[1] = index;
        }
        else{
            if(index == 8){ index = 7;} // can play off a word
            values[0] = index;
            values[1] = min;
        }
        return values; // returns the max in [0] and min in [1]
    }
}
