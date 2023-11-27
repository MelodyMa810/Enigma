package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Melody Ma
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    @Test
    public void sizeTest() {
        String cycle = "(ACB) (ED)";
        String input = "ABCDE";
        Alphabet alphabet = new Alphabet(input);
        Permutation permutation = new Permutation(cycle, alphabet);
        int size = 5;
        assertEquals(size, permutation.size());
    }

    @Test
    public void permuteIntTest() {
        String cycle1 = "(BACD)";
        String input1 = "ABCDE";
        Alphabet alphabet1 = new Alphabet(input1);
        Permutation p1 = new Permutation(cycle1, alphabet1);
        assertEquals(2, p1.permute(0));
        assertEquals(2, p1.permute(10));
        assertEquals(1, p1.permute(3));
        assertEquals(4, p1.permute(4));

        String cycle2 = "(ACB) (D)";
        String input2 = "ABCDE";
        Alphabet alphabet2 = new Alphabet(input2);
        Permutation p2 = new Permutation(cycle2, alphabet2);
        assertEquals(0, p2.permute(1));
        assertEquals(3, p2.permute(3));
        assertEquals(4, p2.permute(4));

        String cycle3 = "";
        String input3 = "ABCDE";
        Alphabet alphabet3 = new Alphabet(input3);
        Permutation p3 = new Permutation(cycle3, alphabet3);
        assertEquals(0, p3.permute(0));
    }

    @Test
    public void invertIntTest() {
        String cycle1 = "(BACD)";
        String input1 = "ABCDE";
        Alphabet alphabet1 = new Alphabet(input1);
        Permutation p1 = new Permutation(cycle1, alphabet1);
        assertEquals(0, p1.invert(2));
        assertEquals(0, p1.invert(7));
        assertEquals(3, p1.invert(1));
        assertEquals(4, p1.invert(4));

        String cycle2 = "(ACB) (D)";
        String input2 = "ABCDE";
        Alphabet alphabet2 = new Alphabet(input2);
        Permutation p2 = new Permutation(cycle2, alphabet2);
        assertEquals(1, p2.invert(0));
        assertEquals(3, p2.invert(3));
        assertEquals(4, p2.invert(4));

        String cycle3 = "";
        String input3 = "ABCDE";
        Alphabet alphabet3 = new Alphabet(input3);
        Permutation p3 = new Permutation(cycle3, alphabet3);
        assertEquals(0, p3.invert(0));
    }

    @Test
    public void permuteCharTest() {
        String cycle1 = "(BACD)";
        String input1 = "ABCDE";
        Alphabet alphabet1 = new Alphabet(input1);
        Permutation p1 = new Permutation(cycle1, alphabet1);
        assertEquals('C', p1.permute('A'));
        assertEquals('B', p1.permute('D'));
        assertEquals('E', p1.permute('E'));

        String cycle2 = "(ACB) (D)";
        String input2 = "ABCDE";
        Alphabet alphabet2 = new Alphabet(input2);
        Permutation p2 = new Permutation(cycle2, alphabet2);
        assertEquals('A', p2.permute('B'));
        assertEquals('D', p2.permute('D'));
        assertEquals('E', p2.permute('E'));

        String cycle3 = "";
        String input3 = "ABCDE";
        Alphabet alphabet3 = new Alphabet(input3);
        Permutation p3 = new Permutation(cycle3, alphabet3);
        assertEquals('A', p3.permute('A'));
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        String cycle = "(BACD)";
        String input = "ABCD";
        Alphabet alphabet = new Alphabet(input);
        Permutation p = new Permutation(cycle, alphabet);
        p.invert('F');
        p.permute('F');
        p.invert(5);
        p.permute(5);
    }

    @Test
    public void invertCharTest() {
        String cycle1 = "(BACD)";
        String input1 = "ABCDE";
        Alphabet alphabet1 = new Alphabet(input1);
        Permutation p1 = new Permutation(cycle1, alphabet1);
        assertEquals('B', p1.invert('A'));
        assertEquals('D', p1.invert('B'));
        assertEquals('E', p1.invert('E'));

        String cycle2 = "(ACB) (D)";
        String input2 = "ABCDE";
        Alphabet alphabet2 = new Alphabet(input2);
        Permutation p2 = new Permutation(cycle2, alphabet2);
        assertEquals('B', p2.invert('A'));
        assertEquals('D', p2.invert('D'));
        assertEquals('E', p2.invert('E'));

        String cycle3 = "";
        String input3 = "ABCDE";
        Alphabet alphabet3 = new Alphabet(input3);
        Permutation p3 = new Permutation(cycle3, alphabet3);
        assertEquals('A', p3.invert('A'));
    }

    @Test
    public void derangementTest() {
        String cycle1 = "(ABCDE)";
        String input1 = "ABCDE";
        Alphabet alphabet1 = new Alphabet(input1);
        Permutation p1 = new Permutation(cycle1, alphabet1);
        assertTrue(p1.derangement());

        String cycle2 = "(ACB) (D)";
        String input2 = "ABCDE";
        Alphabet alphabet2 = new Alphabet(input2);
        Permutation p2 = new Permutation(cycle2, alphabet2);
        assertFalse(p2.derangement());
    }

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

}
