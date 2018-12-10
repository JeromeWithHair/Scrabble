import java.util.ArrayList;
import java.util.Arrays;


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
            return findFirstMove(7);
        }
        return findNextMove();
    }

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

    // SIZE INCLUDES THE SPACE
    public static ArrayList<char[]> permuteWithSize(char[] set, int size) {
        char[] singleCombo = new char[size];
        ArrayList<char[]> result = new ArrayList<char[]>(40321);

        if (size == 0) {
            return result;
        }

        int elements = set.length;

        for (int i = 0; i < elements; i++) { // one
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
     * This is necessary for the first turn, as one-letter words are not allowed.
     */
    private ScrabbleMove findFirstMove(int wordLength) {
        ArrayList<Character> hand = gateKeeper.getHand();
        String bestWord = null;
        int bestScore = -1;
        char[] uH = new char[wordLength]; // updated hand to account for blanks
        for (int i = 0; i < wordLength; i++) {
            uH[i] = hand.get(i);
        }

        String elements = new String(uH);
        int n = uH.length;
        int k = n; //k needs to change in a for loop
        ArrayList<char[]> wordsA = permuteWithSize(uH, wordLength);
        ArrayList<String> words = new ArrayList<>();
        for (char[] word : wordsA) {
            words.add(new String(word));
        }
        String[] permutations = new String[words.size()];
        words.toArray(permutations);
        for (String word : permutations) {
            try {
                // StdOut.println("Trying " + word);
                gateKeeper.verifyLegality(word, Location.CENTER, Location.HORIZONTAL);
                int score = gateKeeper.score(word, Location.CENTER, Location.HORIZONTAL);
                if (score > bestScore) {
                    bestScore = score;
                    bestWord = word;
                    return new PlayWord(bestWord, Location.CENTER, Location.HORIZONTAL);
                }
            } catch (IllegalMoveException e) {
                // It wasn't legal; go on to the next one
            }
        }

        if (wordLength == 0) return new ExchangeTiles(ALL_TILES);

        if (bestScore > -1) {
            return new PlayWord(bestWord, Location.CENTER, Location.HORIZONTAL);
        }

        return findFirstMove(wordLength - 1);
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

    private static ArrayList<char[]> why;

    private static void combination(/*ArrayList<char[]> w,*/ int n, int r, int index, char[] data, int i, char[] arr) {

        for (int j = 0; j < r; j++) {
            if (index == r-1) {
                StdOut.println("completed " + Arrays.toString(data));
                why.add(data);
                return;
            }
        }

        if (i >= n) {
            return;
        }

        data[index] = arr[i];
        // StdOut.println(Arrays.toString(data) + " data and original " + Arrays.toString(arr));
        combination(/*w,*/ n, r, index + 1, data, i + 1, arr);

        combination(/*w,*/ n, r, index, data, i + 1, arr);
    }


    // swap the characters at indices i and j
    private static void swap(char[] a, int i, int j) {
        char c = a[i];
        a[i] = a[j];
        a[j] = c;
    }

    /*private void saveCombos(ArrayList<char[]> w, char arr[], int n, int r) {
        char data[] = new char[r];

        combination(w, n, r, 0, data, 0, arr);
    }*/

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
     * Technically this tries to make a two-letter word by playing one tile; it won't find words that simply add a
     * tile to the end of an existing word.
     */
    private ScrabbleMove findNextMove() {
        //Board b = gateKeeper. ;
        int[][][] lengths = findValidLocations(gateKeeper) ;
        ArrayList<Character> hand = gateKeeper.getHand();
        if(hand.size()!=7){
            return new ExchangeTiles(ALL_TILES) ;
        }
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
            ArrayList<char[]> permutations = permuteWithSize(set,plength);
            //ArrayList<char[]> allCharArrays = new ArrayList<char[]>();
            // StdOut.println(Arrays.toString(set));
            // combination(/*allCharArrays,*/ 8, plength, 0, new char[plength], 0, set) ;
            // StdOut.println(" found all combinations of size " + plength);

            for (char[] w : permutations) { //for everything in the arraylist
                // StdOut.println(Arrays.toString(w));
                String word = String.copyValueOf(w);
                //StdOut.println(word);
                // ArrayList<String> words = new ArrayList();
                // perm1(words, elements);
                // String[] permutations = new String[words.size()];
                // StdOut.println(words.get(0) + " is the first word" + elements);
                // words.toArray(permutations);
                // StdOut.println(permutations.length);
                // for (String word : words) { //for every permutation of a specific char array
                // StdOut.println("testing " + word.toUpperCase() );
                ArrayList<Location[]> priority;
                int space = -1;
                for(int i = 0; i<plength; i++){
                    if(w[i] == ' ') { space = i; }
                }
                if(space != -1) {
                    priority = finalLocations.get(space);
                }
                else{
                    priority = createPriority(lengths, plength, plength);
                }

                for (Location[] validLocation : priority) {
                    try {
                        // StdOut.println("checking " + word + " at " + validLocation[0] + " ");
                        gateKeeper.verifyLegality(word, validLocation[0], validLocation[1]);
                        int score = gateKeeper.score(word, validLocation[0], validLocation[1]);
                        if (score > bestScore) {
                            bestScore = score;
                            //return new PlayWord(word, validLocation[0], validLocation[1]);
                            bestMove = new PlayWord(word, validLocation[0], validLocation[1]);
                            foundWord = true;
                        }
                    } catch (IllegalMoveException e) {
                        //System.err.println(e.getMessage());
                    }
                }

                //}
            }

            plength--;
            if(plength==0 && !foundWord){//if we have done absolutely everything and still have not found a word
                return new ExchangeTiles(ALL_TILES) ;
            }
        }
        return bestMove;
    }
}
