package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    };

    // Chemin du répertoire contenant les fichiers du dictionnaire
    private static final String dictionaryPath = "C:/Users/lenovo/OneDrive/Bureau/projet cours java (jeu des mots)avec nour/WordGameProject/src/dictionary/";

    public static Map<String, Object> generateGameSetup(int totalLetters) {
        Random random = new Random();
        List<Character> letters;
        List<Integer> wordLengths;
        List<String> foundWords = new ArrayList<>(); // Liste pour stocker les mots trouvés

        System.out.println("=== Début de la génération de la configuration du jeu ===");

        do {
            // Étape 1 : Générer les longueurs de mots aléatoires
            wordLengths = generateRandomWordLengths(random);  // Génération des longueurs aléatoires

            // Étape 2 : Générer des lettres aléatoires
            letters = generateRandomLetters(totalLetters, random);  // Génération des lettres

            // Réinitialiser les mots trouvés à chaque itération
            foundWords.clear();

        } while (!findOneWordForEachLength(letters, wordLengths, foundWords));  // Continue tant que nous n'avons pas trouvé 1 mot valide pour chaque longueur

        System.out.println("=== Configuration du jeu générée avec succès ===");

        // Affichage des résultats finaux
        System.out.println("Longueurs de mots générées: " + wordLengths);
        System.out.println("Lettres générées: " + letters);
        
        // Afficher les mots trouvés pour chaque longueur qui peuvent être formés avec les lettres générées
        System.out.println("Mots trouvés pour chaque longueur:");
        for (String word : foundWords) {
            System.out.println(word);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("letters", letters);
        result.put("wordLengths", wordLengths);
        result.put("foundWords", foundWords);
        return result;
    }

    private static List<Integer> generateRandomWordLengths(Random random) {
        // Générer 3 longueurs de mots distinctes entre 3 et 6
        Set<Integer> lengths = new HashSet<>();
        while (lengths.size() < 3) {
            int length = random.nextInt(4) + 3;  // Longueur entre 3 et 6
            lengths.add(length);  // Un Set garantit l'unicité
        }

        // Convertir le Set en List et trier par ordre croissant
        List<Integer> lengthList = new ArrayList<>(lengths);
        Collections.sort(lengthList);  // Tri croissant des longueurs
        return lengthList;
    }

    private static List<Character> generateRandomLetters(int count, Random random) {
        // Générer des lettres aléatoires avec des limitations par lettre
        List<Character> letters = new ArrayList<>();
        int[] currentCounts = new int[MAX_COUNTS.length];

        while (letters.size() < count) {
            int index = random.nextInt(ALPHABETS.length);
            if (currentCounts[index] < MAX_COUNTS[index]) {
                letters.add(ALPHABETS[index]);
                currentCounts[index]++;
            }
        }
        return letters;
    }

    private static boolean findOneWordForEachLength(List<Character> letters, List<Integer> wordLengths, List<String> foundWords) {
        Map<Character, Integer> letterCounts = getLetterCounts(letters);

        // Assurer qu'on trouve 1 mot valide pour chaque longueur
        for (int length : wordLengths) {
            List<String> words = loadWordsByLength(length);

            // Compteur pour les mots valides trouvés
            boolean wordFound = false;

            for (String word : words) {
                if (canFormWord(word, letterCounts)) {

                    // Vérifier si le mot a déjà été ajouté (unicité)
                    if (!foundWords.contains(word)) {
                        foundWords.add(word);  // Ajouter le mot trouvé à la liste des mots trouvés
                        wordFound = true;
                        break;  // Si un mot valide est trouvé, on passe à la longueur suivante
                    }
                }
            }

            // Si aucun mot valide n'est trouvé pour cette longueur, retourner false pour recommencer
            if (!wordFound) {
                return false;
            }
        }

        return true;  // Retourne true si nous avons trouvé un mot valide pour chaque longueur
    }

    private static List<String> loadWordsByLength(int length) {
        List<String> words = new ArrayList<>();
        File file = new File(dictionaryPath + length + "_LETTRES.txt");

        if (!file.exists()) {
            System.err.println("Fichier introuvable pour les mots de longueur " + length);
            return words;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim().toUpperCase());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des mots de longueur " + length + ": " + e.getMessage());
        }

        return words;
    }

    private static boolean canFormWord(String word, Map<Character, Integer> letterCounts) {
        Map<Character, Integer> wordCounts = getLetterCounts(word.toCharArray());

        // Vérifier si chaque lettre du mot peut être formée avec les lettres disponibles
        for (Map.Entry<Character, Integer> entry : wordCounts.entrySet()) {
            char letter = entry.getKey();
            int count = entry.getValue();
            if (letterCounts.getOrDefault(letter, 0) < count) {
                return false;  // Si on ne peut pas former le mot, retourner false
            }
        }
        return true;
    }

    private static Map<Character, Integer> getLetterCounts(List<Character> letters) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : letters) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        return counts;
    }

    private static Map<Character, Integer> getLetterCounts(char[] letters) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : letters) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        return counts;
    }
}
