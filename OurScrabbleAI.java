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

    private static char[] getHand() {
        char a = getHand(0);
        char b = getHand(1);
        char c = getHand(2);
        char d = getHand(3);
        char e = getHand(4);
        char f = getHand(5);
        char g = getHand(6);

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

    private static void combination(ArrayList<char[]> allCharArrays, int n, int r, int index, ArrayList<char data[]>, int i) {
        if (index == r) {
            for (int j = 0; j < r; j++) {
                w.add(data[j]);
                return;
            }

            if (i >= n) {
                return;
            }

            data[index] = arr[i];
            combination(w, arr, n, r, index + 1, data, i + 1);

            combination(w, arr, n, r, index, data, i + 1);
        }
    }


    // swap the characters at indices i and j
    private static void swap(char[] a, int i, int j) {
        char c = a[i];
        a[i] = a[j];
        a[j] = c;
    }

    private void saveCombos(ArrayList<String> w, int arr[], int n, int r) {
        int data[] = new int[r];

        combination(w, arr, n, r, 0, data, 0);
    }

    /**
     * Technically this tries to make a two-letter word by playing one tile; it won't find words that simply add a
     * tile to the end of an existing word.
     */
    private ScrabbleMove findFourTileMove() {
        Board b = new Board();
        int[][][] lengths = findValidLocations(b);
        ArrayList<Character> hand = gateKeeper.getHand();
        PlayWord bestMove = null;
        int bestScore = -1;
        char[] set = getHand();
        boolean foundWord = false;
        int plength = 8;
        while (!foundWord) {
            ArrayList<Location[]> priority = createPriority(lengths,plength);
            ArrayList<char[]> allCharArrays = new ArrayList<char[]>();
            allCharArrays = combination(allCharArrays, 8, plength, 0, ArrayList<char data[]>, 0) ;
            for (int w : allCharArrays) { //for everything in the arraylist
                String elements = new String(set);
                ArrayList<String> words = new ArrayList();
                perm1(words, elements);
                String[] permutations = new String[words.size()];
                words.toArray(permutations);
                for (String word : permutations) { //for every permutation of a specific character array
                    for(Location thing : priority){
                        for(int j=0; j<priority.length; j++){
                            //for (Location direction : new Location[]{Location.HORIZONTAL, Location.VERTICAL}) {
                                try {
                                    gateKeeper.verifyLegality(word, thing[j], thing[j+1]);
                                    int score = gateKeeper.score(word, thing[j], thing[j+1]);
                                    if (score > bestScore) {
                                        bestScore = score;
                                        bestMove = new PlayWord(word, location, direction);
                                        foundWord = true;
                                    }
                                } catch (IllegalMoveException e) {
                                    System.err.println(e.getMessage());
                                }
                            }
                    }
                }
            }

            plength--;
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}
                }
                        }
                        if(bestMove!=null){
                        return bestMove;
                        }
                        }
                        return new ExchangeTiles(ALL_TILES);
                        }
                        }
