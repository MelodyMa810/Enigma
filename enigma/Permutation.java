package enigma;

import java.util.Objects;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Melody Ma
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycle = cycles;
        if (Objects.equals(cycles, "()")) {
            throw error("Wrong cycles format.");
        }
        String storage = _cycle.replace("(", "");
        String finalcycle = storage.replace(")", " ");
        _cycleArray = finalcycle.split(" ");
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String[] newCycle = new String[_cycleArray.length + 1];
        for (int i = 0; i < newCycle.length - 1; i++) {
            newCycle[i] = _cycleArray[i];
        }
        newCycle[_cycleArray.length] = cycle;
        _cycleArray = newCycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int pWrap = wrap(p);
        if (pWrap >= _alphabet.size()) {
            throw error("Index not contained in the alphabet.");
        }
        char letter = _alphabet.toChar(pWrap);
        for (int i = 0; i < _cycleArray.length; i++) {
            if (_cycleArray[i].contains(String.valueOf(letter))) {
                int index = _cycleArray[i].indexOf(letter);
                int length = _cycleArray[i].length();
                if (index + 1 < length) {
                    char result = _cycleArray[i].charAt(index + 1);
                    int perm = _alphabet.toInt(result);
                    return perm;
                } else {
                    char result = _cycleArray[i].charAt(0);
                    int perm = _alphabet.toInt(result);
                    return perm;
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int cWrap = wrap(c);
        if (cWrap >= _alphabet.size()) {
            throw error("Index not contained in the alphabet.");
        }
        char letter = _alphabet.toChar(cWrap);
        for (int i = 0; i < _cycleArray.length; i++) {
            if (_cycleArray[i].contains(String.valueOf(letter))) {
                int index = _cycleArray[i].indexOf(letter);
                int length = _cycleArray[i].length();
                if (index - 1 >= 0) {
                    char result = _cycleArray[i].charAt(index - 1);
                    int perm = _alphabet.toInt(result);
                    return perm;
                } else {
                    int pos = _cycleArray[i].length() - 1;
                    char result = _cycleArray[i].charAt(pos);
                    int perm = _alphabet.toInt(result);
                    return perm;
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw error("Character not in the alphabet.");
        }
        for (int i = 0; i < _cycleArray.length; i++) {
            if (_cycleArray[i].contains(String.valueOf(p))) {
                int index = _cycleArray[i].indexOf(p);
                int length = _cycleArray[i].length();
                int newIndex = (index + 1) % length;
                char letter = _cycleArray[i].charAt(newIndex);
                return letter;
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw error("Character not in the alphabet.");
        }
        for (int i = 0; i < _cycleArray.length; i++) {
            if (_cycleArray[i].contains(String.valueOf(c))) {
                int index = _cycleArray[i].indexOf(c);
                int length = _cycleArray[i].length();
                if (index - 1 >= 0) {
                    char letter = _cycleArray[i].charAt(index - 1);
                    return letter;
                } else {
                    char letter = _cycleArray[i].charAt(length - 1);
                    return letter;
                }
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int cycleSize = 0;
        for (int i = 0; i < _cycleArray.length; i++) {
            cycleSize += _cycleArray[i].length();
        }
        if (cycleSize != _alphabet.size()) {
            return false;
        } else if (cycleSize == _alphabet.size()) {
            for (String element : _cycleArray) {
                if (element.length() == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycle of this permutation in array of strings. */
    private String[] _cycleArray;

    /** Cycle of this permutation in string. */
    private String _cycle;
}
