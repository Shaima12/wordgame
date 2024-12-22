package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.LetterGenerator;

public class GameController {

    public void startGame(JPanel letterPanel, JPanel gridPanel) {
        // Appeler la méthode de génération du jeu et obtenir les résultats
        Map<String, Object> result = LetterGenerator.generateGameSetup(10); // Par exemple, générer 10 lettres

        // Obtenir les lettres générées et les longueurs des mots générés
        List<Character> letters = (List<Character>) result.get("letters");
        List<Integer> wordLengths = (List<Integer>) result.get("wordLengths");

        // Vider les panneaux avant de réajouter les nouvelles lettres et cases
        letterPanel.removeAll();
        gridPanel.removeAll();

        // Afficher les lettres générées sous forme de boutons
        for (Character letter : letters) {
            JButton letterButton = new JButton(letter.toString());
            letterButton.setFont(new Font("Arial", Font.PLAIN, 14));  // Police pour les boutons de lettre
            letterButton.setBackground(new Color(255, 255, 255));  // Couleur de fond des boutons
            letterButton.setForeground(new Color(0, 0, 0));  // Couleur du texte
            letterButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // Bordure légère
            letterButton.setFocusPainted(false); // Supprimer l'effet de focus
            letterButton.setPreferredSize(new Dimension(50, 50)); // Taille des boutons de lettres
            letterButton.addActionListener(createLetterButtonActionListener(letter, gridPanel));

            // Effet visuel au survol
            letterButton.setRolloverEnabled(true);
            letterButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    letterButton.setBackground(new Color(200, 255, 255));  // Changer la couleur au survol
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    letterButton.setBackground(new Color(255, 255, 255));  // Retour à la couleur d'origine
                }
            });

            letterPanel.add(letterButton);
        }

        // Ajouter des cases vides en fonction des longueurs des mots
        for (int length : wordLengths) {
            for (int i = 0; i < length; i++) {
                JTextField textField = new JTextField(1); // Une case vide pour chaque lettre
                textField.setEditable(false); // Empêche l'utilisateur de modifier la case directement
                textField.setHorizontalAlignment(JTextField.CENTER); // Centrer le texte
                textField.setFont(new Font("Arial", Font.PLAIN, 18)); // Police pour les cases vides
                textField.setBackground(new Color(240, 240, 240)); // Couleur de fond des cases vides
                textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // Bordure des cases
                gridPanel.add(textField);
            }
        }

        // Mettre à jour l'affichage pour afficher les nouvelles lettres et cases
        letterPanel.revalidate();
        letterPanel.repaint();
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // Méthode pour gérer l'action des boutons de lettres
    private ActionListener createLetterButtonActionListener(Character letter, JPanel gridPanel) {
        return e -> {
            // Trouver la première case vide dans le panneau de grille et y insérer la lettre
            for (Component component : gridPanel.getComponents()) {
                if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    if (textField.getText().isEmpty()) {
                        textField.setText(letter.toString());
                        break; // Sortir de la boucle dès que nous avons rempli une case
                    }
                }
            }
        };
    }
}