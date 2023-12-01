# Enigma
The [Enigma machine](https://en.wikipedia.org/wiki/Enigma_machine) is a cipher machine used greatly by Nazi Germany during World War II to deliver top-secrete messages. Essentially, each input message is enciphered through permutations and can be deciphered subsequently by recipients. In this project, I built a simplified version of the Enigma machine.

For my machine, it contains reflectors, moving rotors, fixed rotors and permutations. Below is a list of all the features I implemented:<br>
```class Machine``` class that represents a complete enigma machine<br>
```class Alphabet``` an alphabet of encodable characters - provides a mapping from characters to and from indices into the alphabet<br>
```class FixedRotor``` class that represents a rotor that has no ratchet and does not advance<br>
```class MovingRotor``` class that represents a rotating rotor in the enigma machine<br>
```class Permutation``` represents a permutation of a range of integers starting at 0 corresponding to the characters of an alphabet<br>
```class Reflector``` class that represents a reflector in the enigma<br>
```class Rotor``` superclass that represents a rotor in the enigma machine<br>

For the detailed spec of this project: https://inst.eecs.berkeley.edu/~cs61b/fa21/materials/proj/proj1/index.html
