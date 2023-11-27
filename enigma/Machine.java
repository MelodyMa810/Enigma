package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Melody Ma
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            HashMap<String, Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _rotors = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            String name = rotors[i];
            Rotor curr = _allRotors.get(name);
            if (curr == null) {
                throw error("No such Rotor.");
            }
            _rotors[i] = curr;
        }
        if (!_rotors[0].reflecting()) {
            throw error("First rotor is not a reflector.");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("Input has the wrong length.");
        }
        for (int i = 1; i < _rotors.length; i++) {
            Rotor curr = _rotors[i];
            char setto = setting.charAt(i - 1);
            curr.set(setto);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] move = new boolean[numRotors()];
        move[move.length - 1] = true;
        for (int i = 0; i < numRotors() - 1; i++) {
            if (_rotors[i].rotates() && _rotors[i + 1].atNotch()) {
                move[i] = true;
            }
        }
        for (int i = 0; i < numRotors(); i++) {
            if (move[i]) {
                _rotors[i].advance();
                if (i < numRotors() - 1) {
                    _rotors[i + 1].advance();
                    i += 1;
                }
            }
        }
        int input = _plugboard.permute(c);
        for (int i = _rotors.length - 1; i >= 0; i--) {
            Rotor curr = _rotors[i];
            input = curr.convertForward(input);
        }
        for (int i = 1; i < _rotors.length; i++) {
            Rotor curr = _rotors[i];
            input = curr.convertBackward(input);
        }
        return _plugboard.invert(input);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String message = "";
        for (int i = 0; i < msg.length(); i++) {
            int result = convert(_alphabet.toInt(msg.charAt(i)));
            char output = _alphabet.toChar(result);
            message += output;
        }
        return message;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors in the machine. */
    private final int _numRotors;

    /** Number of pawls in the machine. */
    private final int _pawls;

    /** Collection of all the rotors. */
    private final HashMap<String, Rotor> _allRotors;

    /** Collection of selected rotors. */
    private Rotor[] _rotors;

    /** Plugboard of the machine. */
    private Permutation _plugboard;
}
