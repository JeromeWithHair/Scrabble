import java.util.ArrayList;
import java.util.io.*;

public class OurScrabbleAI {

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
            return findTwoTileMove();
        }
        return findFourTileMove();
    }

    public int factorial(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        if (n == 3) {
            return 6;
        }
        if (n == 4) {
            return 24;
        }
        if (n == 5) {
            return 120;
        }
        if (n == 6) {
            return 720;
        }
        if (n == 7) {
            return 5040;
        } else return -1;
    }

    
    static Arraylist<String> createSubSets (ArrayList<Character> hand, int size){
        Arraylist<char[]> sets = new Arraylist<char[]>(factorial(size));
        char subSet[] = new char[8];
        if(size==7){
        //return hand with a space    
        }
         for (int i = 0; i < hand.size(); i++) { // zero: just adds a space
           for (int j = i+1; j < hand.size(); j++) { // one
               for (int k = j+1; k < hand.size(); k++) { // 2
                   for (int m = k+1; m < hand.size(); m++) { // 3
                       for (int n = m+1; n < hand.size(); n++) { //4
                          for (int o = n+1; o < hand.size(); o++) { //5
                             for (int p = o+1; p < hand.size(); p++) {  //6
                                  for (int q = p+1; q < hand.size(); q++) { //7
                                     
        
        
        
    }


    /**
     * This is necessary for the first turn, as one-letter words are not allowed.
     */
    private ScrabbleMove findTwoTileMove() {
        ArrayList<Character> hand = gateKeeper.getHand();
        String bestWord = null;
        int bestScore = -1;
        for (int i = 0; i < hand.size(); i++) {
            for (int j = 0; j < hand.size(); j++) {
                if (i != j) {
                    try {
                        char a = hand.get(i);
                        if (a == '_') {
                            a = 'E'; // This could be improved slightly by trying all possibilities for the blank
                        }
                        char b = hand.get(j);
                        if (b == '_') {
                            b = 'E'; // This could be improved slightly by trying all possibilities for the blank
                        }
                        String word = "" + a + b;
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
            }
        }
        if (bestScore > -1) {
            return new PlayWord(bestWord, Location.CENTER, Location.HORIZONTAL);
        }
        return new ExchangeTiles(ALL_TILES);
    }
    
    private static void permute(ArrayList<String> w, String str, int l, int r){
        if(l==r){
            w.add(str) ;
            return ;
        }
        else{
            for (int i = 0; i < r; i++) {
                str = swap(str, l, i);
                permute(w, str, l+1, r) ;
                str = swap(str, l, i) ;
            }
        }
    }
    
    private static String swap(String a, int i, int j){
        char temp ;
        char[] charArray = a.toCharArray() ;
        temp = charArray[i] ;
        charArray[i] = charArray[j] ;
        charArray[j] = temp ;
        return String.valueOf(charArray) ;
    }
    
    /**
     * Technically this tries to make a two-letter word by playing one tile; it won't find words that simply add a
     * tile to the end of an existing word.
     */
    private ScrabbleMove findFourTileMove() {
        ArrayList<Character> hand = gateKeeper.getHand();
        PlayWord bestMove = null;
        int bestScore = -1;
                                //int fact = factorial(4);
                                //char[] set = {a, b, c, d} ;
                                //String string = new String(set) ;
                                //int n = set.length() ;
                                ArrayList<String> words = permute(hand) ;
                               // permute(words, string, 0, n-1) ;
                                //String[] permutations = new String[words.size()] ;
                                words.toArray(permutations) ;
                                for (String word : permutations) {
                                    for (int row = 0; row < Board.WIDTH; row++) {
                                        for (int col = 0; col < Board.WIDTH; col++) {
                                            Location location = new Location(row, col);
                                            for (Location direction : new Location[]{Location.HORIZONTAL, Location.VERTICAL}) {
                                                try {
                                                    gateKeeper.verifyLegality(word, location, direction);
                                                    int score = gateKeeper.score(word, location, direction);
                                                    if (score > bestScore) {
                                                        bestScore = score;
                                                        bestMove = new PlayWord(word, location, direction);
                                                    }
                                                } catch (IllegalMoveException e) {
                                                }
                                            }
                                        }
                                    }
                                }
            }
            if (bestMove != null) {
                return bestMove;
            }
        }
        return new ExchangeTiles(ALL_TILES);
    }
}
