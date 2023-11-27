package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

public class AlphabetTest {

    @Test
    public void sizeTest() {
        String input = "ABCDE";
        Alphabet alphabet = new Alphabet(input);
        int size = 5;
        assertEquals(size, alphabet.size());
    }

    @Test
    public void containsTest() {
        String input = "ABCDE";
        Alphabet alphabet = new Alphabet(input);
        assertTrue(alphabet.contains('B'));
        assertFalse(alphabet.contains('F'));
    }

    @Test
    public void toCharTest() {
        String input = "ABCDE";
        Alphabet alphabet = new Alphabet(input);
        assertEquals('A', alphabet.toChar(0));
    }

    @Test
    public void toIntTest() {
        String input = "ABCDE";
        Alphabet alphabet = new Alphabet(input);
        assertEquals(0, alphabet.toInt('A'));
    }
}
