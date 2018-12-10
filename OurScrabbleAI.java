import java.util.ArrayList;


public class OurScrabbleAI implements ScrabbleAI {

    /**
     * Dumb AI that picks the highest-scoring one-tile move. Plays a two-tile move on the first turn. Exchanges all of its
     * letters if it can't find any other move.
     */

    /**
     * When exchanging, always exchange everything.
     */
    private static final boolean[] ALL_TILES = {true, true, true, true, true, true, true};

    /**
     * The GateKeeper through which this Incrementalist accesses the Board.
     */
    private GateKeeper gateKeeper;

    @Override
    public void setGateKeeper(GateKeeper gateKeeper) {
        this.gateKeeper = gateKeeper;
    }

    @Override
    public ScrabbleMove chooseMove() {
        if (gateKeeper.getSquare(Location.CENTER) == Board.DOUBLE_WORD_SCORE) {
            return findFirstMove();
        }
        return findFourTileMove();
    }

    private static char[] getHand(ArrayList<Character> hand) {
        char a = hand.get(0);
        char b = hand.get(1);
        char c = hand.get(2);
        char d = hand.get(3);
        char e = hand.get(4);
        char f = hand.get(5);
        char g = hand.get(6);

        return char[] set = {a, b, c, d, e, f, g, ' '};
    }

    /**
     * This is necessary for the first turn, as one-letter words are not allowed.
     */
    private ScrabbleMove findFirstMove() {
        ArrayList<Character> hand = gateKeeper.getHand();
        String bestWord = null;
        int bestScore = -1;
        char[] uH = new char[7]; // updated hand to account for blanks
        for (int i = 0; i < 7; i++) {
            if (hand.get(i) == ' ') {
                uH[i] = 'E';
            } else {
                uH[i] = hand.get(i);
            }
        }

        String elements = new String(uH);
        int n = uH.length;
        int k = n; //k needs to change in a for loop
        ArrayList<String> words = new ArrayList();
        perm1(words, elements);
        String[] permutations = new String[words.size()];
        words.toArray(permutations);
        for (String word : permutations) {
            try {
                StdOut.println("Trying " + word);
                gateKeeper.verifyLegality(word, Location.CENTER, Location.HORIZONTAL);
                int score = gateKeeper.score(word, Location.CENTER, Location.HORIZONTAL);
                if (score > bestScore) {
                    bestScore = score;
                    bestWord = word;
                }
            } catch (IllegalMoveException e) {
                // It wasn't legal; go on to the next one
            }
        }

        if (bestScore > -1) {
            return new PlayWord(bestWord, Location.CENTER, Location.HORIZONTAL);
        }
        //TODO if no 7 letter start, do a 6 letter start
        return new ExchangeTiles(ALL_TILES);
    }

    public static void perm1(ArrayList<String> w, String s) {
        perm1(w, "", s);
    }

    private static void perm1(ArrayList<String> w, String prefix, String s) {
        int n = s.length();
        if (n == 0) {
            w.add(prefix);
        } else {
            for (int i = 0; i < n; i++)
                perm1(w, prefix + s.charAt(i), s.substring(0, i) + s.substring(i + 1, n));
        }

    }

    private static void combination(ArrayList<char[]> w, int n, int r, int index, char[] data, int i, char[] arr) {
        if (index == r) {
            for (int j = 0; j < r; j++) {
                w.add(data);
                return;
            }

            if (i >= n) {
                return;
            }

            data[index] = arr[i];
            combination(w, n, r, index + 1, data, i + 1, arr);

            combination(w, n, r, index, data, i + 1, arr);
        }
    }


    // swap the characters at indices i and j
    private static void swap(char[] a, int i, int j) {
        char c = a[i];
        a[i] = a[j];
        a[j] = c;
    }

    private void saveCombos(ArrayList<char[]> w, char arr[], int n, int r) {
        char data[] = new char[r];

        combination(w, n, r, 0, data, 0, arr);
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

    /**
     * Technically this tries to make a two-letter word by playing one tile; it won't find words that simply add a
     * tile to the end of an existing word.
     */
    private ScrabbleMove findFourTileMove() {
        Board b = new Board() ;
        int[][][] lengths = findValidLocations(b) ;
        ArrayList<Character> hand = gateKeeper.getHand();
        PlayWord bestMove = null;
        int bestScore = -1;
        char[] set = getHand(hand);
        boolean foundWord = false;
        int plength = 8;
        while (!foundWord) {
            ArrayList<ArrayList<Location[]>> finalLocations = new ArrayList<ArrayList<Location[]>>(15*15);
            for(int i = 0; i< plength; i++){
                ArrayList<Location[]> priority = createPriority(lengths,plength-1, i);
                finalLocations.add(priority);
            }
            ArrayList<char[]> allCharArrays = new ArrayList<char[]>();
            combination(allCharArrays, 8, plength, 0, new char[plength], 0, set) ;
            for (char[] w : allCharArrays) { //for everything in the arraylist
                String elements = new String(w);
                ArrayList<String> words = new ArrayList();
                perm1(words, elements);
                String[] permutations = new String[words.size()];
                words.toArray(permutations);
                for (String word : permutations) { //for every permutation of a specific character array
                    int space = 0; 
                    for(int i = 0; i<plength; i++){
                     if(word.charAt(i) == ' ') { space = i; }    
                    }
                    priority = finalLocation.get(space);
                    for (Location[] validLocation : priority) {
                                try {
                                    gateKeeper.verifyLegality(word, validLocation[0], validLocation[1]);
                                    int score = gateKeeper.score(word, validLocation[0], validLocation[1]);
                                    if (score > bestScore) {
                                        bestScore = score;
                                        bestMove = new PlayWord(word, validLocation[0], validLocation[1]);
                                        foundWord = true;
                                    }
                                } catch (IllegalMoveException e) {
                                    System.err.println(e.getMessage());
                                }
                            }
                    
                }
            }

            plength--;
            if(plength==0 && !foundword){//if we have done absolutely everything and still have not found a word
                return new ExchangeTiles(ALL_TILES) ;
            }
        }
        return bestMove;
    }
}            
