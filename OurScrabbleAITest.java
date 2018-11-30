import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OurScrabbleAITest {

    private Board board;

    @BeforeEach
    public void setUp() {
      board = new Board();
      board.placeWord("Blue",Location.CENTER,Location.VERTICAL);
    }

    /** Returns the characters in s as an ArrayList of Characters. */
    private ArrayList<Character> asCharList(String s) {
        ArrayList<Character> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            result.add(c);
        }
        return result;
    }
 
    @Test
  public void CombinationsGetsBestWord;
  
  @Test
  public void CombinationsGetsLengthPriority;
  
  @Test
  public void handlesBoardLetter;
  
  @Test
  public void placesFourLetterWord;
  
}
