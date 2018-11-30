import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TibbopotomusTest {

    private Board board;
    // Jerome is the best person ever
    @BeforeEach
    public void setUp() {
        board = new Board();
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
    public void isEmptyDirFalseForNonemptyDir() {

    }

    @Test
    public void isEmptyDirFalseIfDirOffBoard() {

    }

    @Test
    public void isEmptyDirTrueForEmptyDirInMiddle() {

    }

    @Test
    public void isEmptyDirTrueForEmptyDirAtEdge() {

    }

    @Test
    public void getEmptyDirGivesEmptyDir() {

    }

    @Test
    public void getEmptyDirDoesNotGiveNonemptyDir() {

    }

}
