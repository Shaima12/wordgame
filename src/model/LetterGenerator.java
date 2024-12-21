package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class LetterGenerator {

    // Définir l'alphabet (A-Z)
    private static final char[] ALPHABETS = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    // Définir le nombre maximum autorisé pour chaque lettre
    private static final int[] MAX_COUNTS = {
        4, 2, 2, 2, 3, 2, 2, 2, 3, 2, 2, 2, 2,
        2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1
    }; // Ces nombres indiquent combien de fois chaque lettre peut apparaître (A-Z)

    // Chemin du fichier du dictionnaire
    private static final String DICTIONARY_PATH = "dictionary.txt";

    /**
     * Génère des lettres aléatoires et des longueurs de mots permettant la formation de 3 mots
     * de longueurs spécifiques. Les mots doivent avoir entre 3 et 6 caractères.
     * 
     * @param totalLetters Nombre total de lettres à générer.
     * @return Une carte contenant les lettres générées et les longueurs de mots cibles.
     */
    public static Map<String, Object> generateGameSetup(int totalLetters) {
        List<String> dictionary = loadDictionary(); // Charger le dictionnaire
        Random random = new Random(); // Objet pour générer des valeurs aléatoires
        List<Character> letters;
        List<Integer> wordLengths;

        System.out.println("Génération de la configuration du jeu...");

        do {
            wordLengths = generateRandomWordLengths(totalLetters, random); // Générer des longueurs de mots
            letters = generateRandomLetters(totalLetters, random); // Générer des lettres aléatoires

            // Affichage pour débogage des lettres et longueurs générées
            System.out.println("Lettres générées: " + letters);
            System.out.println("Longueurs des mots générées: " + wordLengths);
        } while (findWordsForLengths(letters, wordLengths, dictionary).size() < 1);  // Temporairement fixé à 1 mot

        // Retourner les lettres et longueurs des mots
        Map<String, Object> result = new HashMap<>();
        result.put("letters", letters);
        result.put("wordLengths", wordLengths);
        return result;
    }

    /**
     * Génère des longueurs de mots aléatoires qui somment à un nombre total de lettres
     * sans dépasser, en s'assurant que chaque longueur de mot est entre 3 et 6 caractères.
     * 
     * @param totalLetters Nombre total de lettres à générer.
     * @param random Objet pour générer des valeurs aléatoires.
     * @return Une liste de 3 longueurs de mots aléatoires.
     */
    private static List<Integer> generateRandomWordLengths(int totalLetters, Random random) {
        List<Integer> lengths = new ArrayList<>();
        int remaining = totalLetters;

        System.out.println("Génération des longueurs de mots...");

        // Assurez-vous que les longueurs de mots sont entre 3 et 6 caractères
        while (lengths.size() < 3) {
            // Si la somme des longueurs restantes est inférieure ou égale aux lettres restantes, on arrête
            int length = random.nextInt(4) + 3;  // Génère une longueur entre 3 et 6
            if (remaining - length >= (3 - lengths.size()) * 3) { // Vérifier s'il reste assez de lettres
                lengths.add(length);
                remaining -= length;
            }
        }
        System.out.println("Longueurs de mots finales: " + lengths);
        return lengths;
    }

 


    /**
     * Génère des lettres aléatoires en fonction des contraintes pré-définies pour la fréquence de chaque lettre.
     * 
     * @param count Le nombre total de lettres à générer.
     * @param random Objet pour générer des valeurs aléatoires.
     * @return Une liste de lettres générées aléatoirement.
     */
    private static List<Character> generateRandomLetters(int count, Random random) {
        List<Character> letters = new ArrayList<>();
        int[] currentCounts = new int[MAX_COUNTS.length]; // Compteur pour chaque lettre

        // Générer des lettres jusqu'à ce que nous ayons le nombre souhaité
        while (letters.size() < count) {
            // Choisir une lettre aléatoire parmi l'alphabet
            int index = random.nextInt(ALPHABETS.length);
            // Vérifier si nous pouvons encore utiliser cette lettre selon le nombre maximal autorisé
            if (currentCounts[index] < MAX_COUNTS[index]) {
                letters.add(ALPHABETS[index]);
                currentCounts[index]++;
            }
        }
        return letters;
    }

    /**
     * Charge le fichier dictionnaire contenant les mots valides.
     * Les mots sont lus et convertis en majuscules.
     * 
     * @return Une liste de mots provenant du dictionnaire.
     */
    private static List<String> loadDictionary() {
        List<String> dictionary = new ArrayList<>();
        try {
            // Tenter de charger dictionary.txt depuis le dossier des ressources
            InputStream stream = LetterGenerator.class.getClassLoader().getResourceAsStream("dictionary.txt");

            if (stream == null) {
                System.err.println("Impossible de trouver dictionary.txt dans le dossier des ressources.");
                return dictionary;
            }

            // Lire le dictionnaire
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    dictionary.add(line.trim().toUpperCase());
                }
            }
            System.out.println("Dictionnaire chargé avec succès, contenant " + dictionary.size() + " mots.");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du dictionnaire: " + e.getMessage());
        }
        return dictionary;
    }

    /**
     * Trouve les mots du dictionnaire qui correspondent aux longueurs données et peuvent être formés avec les lettres disponibles.
     * 
     * @param letters Une liste de lettres disponibles pour former des mots.
     * @param wordLengths Une liste des longueurs cibles des mots.
     * @param dictionary Une liste des mots valides.
     * @return Une liste des mots valides qui correspondent aux critères.
     */
    private static List<String> findWordsForLengths(List<Character> letters, List<Integer> wordLengths, List<String> dictionary) {
        List<String> validWords = new ArrayList<>();
        Map<Character, Integer> letterCounts = getLetterCounts(letters);

        System.out.println("Recherche de mots valides...");
        System.out.println("Comptes des lettres: " + letterCounts);  // Débogage des comptes de lettres

        for (String word : dictionary) {
            // Vérifier si la longueur du mot correspond à une des longueurs cibles
            if (wordLengths.contains(word.length())) {
                System.out.println("Vérification du mot: " + word);
                // Vérifier si le mot peut être formé avec les lettres disponibles
                if (canFormWord(word, letterCounts)) {
                    validWords.add(word);
                    System.out.println("Mot valide trouvé: " + word);
                } else {
                    System.out.println("Mot impossible à former: " + word);
                }
            }
        }

        System.out.println("Nombre de mots valides trouvés: " + validWords.size());
        return validWords;
    }

    /**
     * Vérifie si un mot peut être formé en utilisant les lettres disponibles.
     * 
     * @param word Le mot à vérifier.
     * @param letterCounts Une carte des lettres disponibles avec leurs comptes.
     * @return True si le mot peut être formé, false sinon.
     */
    private static boolean canFormWord(String word, Map<Character, Integer> letterCounts) {
        Map<Character, Integer> wordCounts = getLetterCounts(word.toCharArray());

        System.out.println("Vérification si le mot peut être formé: " + word);
        System.out.println("Comptes des lettres du mot: " + wordCounts);

        for (Map.Entry<Character, Integer> entry : wordCounts.entrySet()) {
            char letter = entry.getKey();
            int count = entry.getValue();
            int availableCount = letterCounts.getOrDefault(letter, 0);

            if (availableCount < count) {
                System.out.println("Pas assez de lettres pour: " + letter);
                return false;
            }
        }

        System.out.println("Le mot peut être formé: " + word);
        return true;
    }

    /**
     * Compte les occurrences de chaque caractère dans une liste de lettres.
     * 
     * @param letters Une liste de lettres.
     * @return Une carte contenant le nombre de chaque lettre.
     */
    private static Map<Character, Integer> getLetterCounts(List<Character> letters) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : letters) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        System.out.println("Comptes des lettres: " + counts);  // Débogage des comptes de lettres
        return counts;
    }

    /**
     * Version surchargée de getLetterCounts qui fonctionne avec un tableau de caractères.
     * 
     * @param letters Un tableau de caractères.
     * @return Une carte contenant le nombre de chaque lettre.
     */
    private static Map<Character, Integer> getLetterCounts(char[] letters) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : letters) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        return counts;
    }
}
