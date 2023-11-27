package enigma;

import java.util.HashMap;
import static enigma.EnigmaException.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Melody Ma
 */
class Alphabet {
    /** Alphabet. */
    private HashMap<Integer, Character> _alphabet;
    /** Alphabet in array form. */
    private char[] _AlphabetArray;

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _alphabet = new HashMap<Integer, Character>();
        _AlphabetArray = new char[chars.length()];
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            _alphabet.put(i, c);
            _AlphabetArray[i] = c;
        }
        if (_AlphabetArray.length != _alphabet.size()) {
            throw error("No character may be duplicated.");
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alphabet.size();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _alphabet.containsValue(ch);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index > size()) {
            throw error("Index is out of bound.");
        }
        return _alphabet.get(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error("Character is not in the alphabet.");
        }
        for (int i = 0; i < size(); i++) {
            if (_alphabet.get(i) == ch) {
                return i;
            }
        }
        return 0;
    }

}
