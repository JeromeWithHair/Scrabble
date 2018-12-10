import java.util.ArrayList;

public class Tibbopottomus implements ScrabbleAI {

    /**
     * Our ScrabbleAI that searches valid locations and to find the best move
     */

    /**
     * When exchanging, always exchange everything.
     */
    private static final boolean[] ALL_TILES = {true, true, true, true, true, true, true};

    /**
     * The GateKeeper through which this AI accesses the Board.
     */
    private GateKeeper gateKeeper;

    @Override
    public void setGateKeeper(GateKeeper gateKeeper) {
        this.gateKeeper = gateKeeper;
    }

    /**
     * Similar to incrimentalist, we have a separate move method for the first move
     */
    @Override
    public ScrabbleMove chooseMove() {
        if (gateKeeper.getSquare(Location.CENTER) == Board.DOUBLE_WORD_SCORE) {
            return findFirstMove(7);
        }
        return findNextMove();
    }

    /**
     * This helper method allows us to quickly convert our hand to a character array and turn all blank tiles into e's
     * We also add a pace to our set to allow it to play off of other words
     */
    private static char[] getHand(ArrayList<Character> hand) {
        // int check = hand.size();
        char a = hand.get(0);
        if (a == '_') a = 'E';
        char b = hand.get(1);
        if (b == '_') b = 'E';
        char c = hand.get(2);
        if (c == '_') c = 'E';
        char d = hand.get(3);
        if (d == '_') d = 'E';
        char e = hand.get(4);
        if (e == '_') e = 'E';
        char f = hand.get(5);
        if (f == '_') f = 'E';
        char g = hand.get(6);
        if (g == '_') g = 'E';

        char[] set = {a, b, c, d, e, f, g, ' '};
        return set;
    }

    /**
     * This method takes our hand and creates all permutations on it using a brute force approach.
     * It can be used on smaller sizes.
     * @param set - the character array of our hand
     * @param size - size of permutation wanted
     * @return - ArrayList of character arrays containing all permutations of our hand of a given size
     */
    public static ArrayList<char[]> permuteWithSize(char[] set, int size) {
        char[] singleCombo = new char[size];
        ArrayList<char[]> result = new ArrayList<char[]>(40321);

        if (size == 0) {
            return result;
        }

        int elements = set.length;

        for (int i = 0; i < elements; i++) { // 1
            singleCombo[0] = set[i];
            if (size >= 2) {
                for (int j = 0; j < elements; j++) { // 2
                    if (j == i) {
                        continue;
                    }
                    singleCombo[1] = set[j];
                    if (size >= 3) {
                        for (int k = 0; k < elements; k++) { // 3
                            if (k == j || k == i) {
                                continue;
                            }
                            singleCombo[2] = set[k];
                            if (size >= 4) {
                                for (int l = 0; l < elements; l++) { // 4
                                    if (l == k || l == j || l == i) {
                                        continue;
                                    }
                                    singleCombo[3] = set[l];
                                    if (size >= 5) {
                                        for (int m = 0; m < elements; m++) { //5
                                            if (m == l || m == k || m == j || m == i) {
                                                continue;
                                            }
                                            singleCombo[4] = set[m];
                                            if (size >= 6) {
                                                for (int n = 0; n < elements; n++) { //6
                                                    if (n == m || n == l || n == k || n == j || n == i) {
                                                        continue;
                                                    }
                                                    singleCombo[5] = set[n];
                                                    if (size >= 7) {
                                                        for (int p = 0; p < elements; p++) { // 7
                                                            if (p == n || p == m || p == l || p == k || p == j || p == i) {
                                                                continue;
                                                            }
                                                            singleCombo[6] = set[p];
                                                            if(size == 8){
                                                                for(int q = 0; q < elements; q++) {
                                                                    if (q == p || q == n || q == m || q == l || q == k || q == j || q == i) {
                                                                        continue;
                                                                    }
                                                                    singleCombo[7] = set[q];
                                                                    char[] copy = singleCombo.clone();
                                                                    result.add(copy); // ending 8
                                                                }
                                                            }
                                                            else {
                                                                char[] copy = singleCombo.clone();
                                                                result.add(copy); // ending 7
                                                            }
                                                        }

                                                    } else {
                                                        char[] copy = singleCombo.clone();
                                                        result.add(copy);
                                                    } // ending 6
                                                }
                                            } else {
                                                char[] copy = singleCombo.clone();
                                                result.add(copy);
                                            } // ending 5
                                        }
                                    } else {
                                        char[] copy = singleCombo.clone();
                                        result.add(copy);
                                    } // ending 4
                                }
                            } else {
                                char[] copy = singleCombo.clone();
                                result.add(copy);
                            } // ending 3
                        }
                    } else {
                        char[] copy = singleCombo.clone();
                        result.add(copy);
                    } // ending 2
                }
            } else {
                char[] copy = singleCombo.clone();
                result.add(copy);  // ending 1
            }
        }
        return result;
    }





    /**
     * This method plays the best first word, as the the only valid location is the center.
     * It's called recursive with a size for simplicity
     */
    private ScrabbleMove findFirstMove(int wordLength) {
        ArrayList<Character> hand = gateKeeper.getHand();
        String bestWord = null;
        int bestScore = -1;
        char[] uH = new char[wordLength]; // updated hand to account for blanks
        for (int i = 0; i < wordLength; i++) {
            char element = hand.get(i);
            if (element == '_') element = 'E';
            uH[i] = element;
        }
        ArrayList<char[]> wordsA = permuteWithSize(uH, wordLength);
        ArrayList<String> words = new ArrayList<>();
        for (char[] word : wordsA) { // permute outputs character arrays, so we convert them to Strings here
            words.add(new String(word));
        }
        for (String word : words) {
            try {
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

        if (wordLength == 0) return new ExchangeTiles(ALL_TILES); // physically can't play a word from hand

        if (bestScore > -1) {
            return new PlayWord(bestWord, Location.CENTER, Location.HORIZONTAL);
        }

        return findFirstMove(wordLength - 1); // go down a word length
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
     * This is done in both horizontal and vertical direction. These 4 values are kept in an array and the method returns that array
     * @return lengths - a dD array with [row][column][4] where the 4 values are maxHorizontal, minHorizontal, maxVertical, and minVertical
     */
    public static int[][][] findValidLocations(GateKeeper gateKeeper){
        int[][][] lengths = new int[15][15][4];
        for(int i = 0; i<15; i++){
            for(int j = 0; j<15; j++){
                Location current = new Location(i,j);
                if((Character.isAlphabetic(gateKeeper.getSquare(current)))){ // if occupied, can't play there
                    for(int k = 0; k<4; k++){
                        lengths[i][j][k] = 0;
                    }
                }
                else{//check path
                    int[] check;
                    check = findEmptyPath(gateKeeper,current,Location.HORIZONTAL);
                    lengths[i][j][0] = check[0];
                    lengths[i][j][1] = check[1];
                    check = findEmptyPath(gateKeeper,current,Location.VERTICAL);
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
     * @return - max and min in given direction
     */
    public static int[] findEmptyPath(GateKeeper gateKeeper, Location start, Location direction){
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
            if((Character.isAlphabetic(gateKeeper.getSquare(current)))){ break;} // found a tile
            Location neighbor1 = current.neighbor(direction.opposite());
            Location antineighbor2 = current.antineighbor(direction.opposite());
            if(index == 0 && current.antineighbor(direction).isOnBoard() && Character.isAlphabetic(gateKeeper.getSquare(current.antineighbor(direction)))){ // check if it's playing off a word to the left or above
                min = 1;
            }
            boolean check1 = neighbor1.isOnBoard() && Character.isAlphabetic(gateKeeper.getSquare(neighbor1));
            boolean check2 = antineighbor2.isOnBoard() && Character.isAlphabetic(gateKeeper.getSquare(antineighbor2));
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
     * Creates an array of valid locations. Then creates a list of permutaions and checks those against valid locations
     */
    private ScrabbleMove findNextMove() {
        ArrayList<Character> hand = gateKeeper.getHand();
        if(hand.size()!=7){
            return new ExchangeTiles(ALL_TILES) ;
        }
        PlayWord bestMove = null;
        int bestScore = -1;
        char[] set = getHand(hand);
        boolean foundWord = false;
        int plength = 8;
        int[][][] lengths = findValidLocations(gateKeeper); // finds valid locations on the board

        while (!foundWord && plength>=2) { // if we've found a word by the time we get to small lengths, we just skip the shorter ones
            ArrayList<ArrayList<Location[]>> finalLocations = new ArrayList<ArrayList<Location[]>>(8);
            for(int i = 0; i< plength; i++){
                ArrayList<Location[]> priority = createPriority(lengths,plength-1, i);  // add validLocations for each location of the space in the word
                finalLocations.add(priority);
            }
            ArrayList<char[]> permutations = permuteWithSize(set,plength);

            for (char[] w : permutations) {
                String word = String.copyValueOf(w);
                ArrayList<Location[]> priority;
                int space = -1;
                for(int i = 0; i<plength; i++){ // find the location of the space in the word
                    if(w[i] == ' ') { space = i; }
                }
                if(space != -1) {
                    priority = finalLocations.get(space); // use the corresponding legal locations
                }
                else{
                    priority = createPriority(lengths, plength, plength); // if no space, use this special case
                }

                for (Location[] validLocation : priority) {
                    try {
                        gateKeeper.verifyLegality(word, validLocation[0], validLocation[1]); // attempt to play the word
                        int score = gateKeeper.score(word, validLocation[0], validLocation[1]);
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new PlayWord(word, validLocation[0], validLocation[1]);
                            foundWord = true;
                        }
                    } catch (IllegalMoveException e) {
                        //If illegal, move on
                    }
                }
            }

            plength--;
            if(plength==0 && !foundWord){//if we have done absolutely everything and still have not found a word
                return new ExchangeTiles(ALL_TILES) ;
            }
        }
        return bestMove;
    }
}
