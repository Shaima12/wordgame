package model;

import java.util.*;

public class Main {

    public static void main(String[] args) {
    	System.out.println("Starting the game setup...");
        // Define the total number of letters to generate (adjustable value)
        int totalLetters = 8;

        // Call the method to generate the game setup
        Map<String, Object> gameSetup = LetterGenerator.generateGameSetup(totalLetters);

        if (gameSetup != null) {
            // Retrieve the generated letters and word lengths
            List<Character> letters = (List<Character>) gameSetup.get("letters");
            List<Integer> wordLengths = (List<Integer>) gameSetup.get("wordLengths");

            // Display the results
            System.out.println("Generated Letters: " + letters);
            System.out.println("Target Word Lengths: " + wordLengths);
        } else {
            System.err.println("Error: No game setup generated.");
        }
    }
}
