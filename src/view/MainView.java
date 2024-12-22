package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.GameController;

public class MainView {

    private GameController controller;

    public MainView() {
        controller = new GameController();
    }

    public void createAndShowGUI() {
        // Créer la fenêtre principale
        JFrame frame = new JFrame("Jeu des Mots");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500); // Taille de la fenêtre

        // Créer les composants
        JButton startButton = new JButton("Démarrer le jeu");
        JPanel letterPanel = new JPanel();
        JPanel gridPanel = new JPanel();
        
        letterPanel.setLayout(new FlowLayout()); // Organisation horizontale pour les lettres
        gridPanel.setLayout(new GridLayout(0, 5)); // 5 colonnes pour les cases vides

        // Ajouter l'action au bouton
        startButton.addActionListener(e -> controller.startGame(letterPanel, gridPanel));

        // Disposition avec un gestionnaire de mise en page
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(startButton);
        panel.add(new JLabel("Lettres générées :"));
        panel.add(letterPanel);
        panel.add(new JLabel("Cases pour les mots à trouver :"));
        panel.add(gridPanel);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainView().createAndShowGUI();
        });
    }
} 