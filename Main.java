package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Melody Ma
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        String setting = _input.nextLine();
        setUp(machine, setting);
        if (!setting.contains("*")) {
            throw error("The asterisk must appear in the first column.");
        }
        while (_input.hasNextLine()) {
            String curr = _input.nextLine();
            if (curr.contains("*")) {
                setUp(machine, curr);
                if (!_input.hasNextLine()) {
                    break;
                }
                curr = _input.nextLine();
                String converted = machine.convert(curr.replaceAll(" ", ""));
                printMessageLine(converted);
                _output.println();
            } else {
                String converted = machine.convert(curr.replaceAll(" ", ""));
                printMessageLine(converted);
                _output.println();
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.next();
            _alphabet = new Alphabet(alphabet);
            int numRotor = _config.nextInt();
            int numPawl = _config.nextInt();
            _allRotors = new HashMap<String, Rotor>();
            rotorName = _config.next();
            while (_config.hasNext()) {
                Rotor curr = readRotor();
                String name = curr.name();
                _allRotors.put(name, curr);
            }
            return new Machine(_alphabet, numRotor, numPawl, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = rotorName;
            String info = _config.next();
            char attribute = info.charAt(0);
            String notches = info.substring(1);
            String permutation = "";
            String next = _config.next();
            while (_config.hasNext() && next.contains("(")) {
                permutation += next;
                next = _config.next();
            }
            if (!_config.hasNext()) {
                permutation += next;
            }
            rotorName = next;
            Permutation perm = new Permutation(permutation, _alphabet);
            if (attribute == 'M') {
                return new MovingRotor(name, perm, notches);
            } else if (attribute == 'N') {
                return new FixedRotor(name, perm);
            } else {
                return new Reflector(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] input = settings.split(" ");
        if (!input[0].equals("*")) {
            throw error("Input format is off.");
        }
        String[] rotors = new String[M.numRotors()];
        for (int i = 1; i <= M.numRotors(); i++) {
            String curr = input[i];
            rotors[i - 1] = curr;
        }
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j]) && i != j) {
                    throw error("Rotors have been repeated.");
                }
            }
        }
        M.insertRotors(rotors);
        String setting = input[M.numRotors() + 1];
        M.setRotors(setting);
        String pluginfo = "";
        for (int i = M.numRotors() + 2; i < input.length; i++) {
            pluginfo = pluginfo.concat(input[i] + " ");
        }
        Permutation plugboard = new Permutation(pluginfo, _alphabet);
        M.setPlugboard(plugboard);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 1) {
            _output.print(msg.charAt(i));
            if (((i + 1) % 5 == 0) && (i != msg.length() - 1)) {
                _output.print(" ");
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** All available rotors. */
    private HashMap<String, Rotor> _allRotors;

    /** Name of the rotor. */
    private String rotorName;
}
