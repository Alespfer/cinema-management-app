// Fichier : src/main/java/com/mycompany/cinema/view/FilmDetailPanel.java
package com.mycompany.cinema.view;

import com.mycompany.cinema.Film;
import javax.swing.*;
import java.awt.*;

public class FilmDetailPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel posterLabel;
    private JTextArea synopsisArea;
    private JLabel infoLabel;

    public FilmDetailPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panneau pour le titre et les infos
        JPanel northPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Sélectionnez un film dans la liste", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(infoLabel, BorderLayout.CENTER);
        
        // Panneau pour l'affiche
        posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panneau pour le synopsis
        synopsisArea = new JTextArea("Synopsis...");
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setLineWrap(true);
        synopsisArea.setEditable(false);
        synopsisArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane synopsisScrollPane = new JScrollPane(synopsisArea);
        
        add(northPanel, BorderLayout.NORTH);
        add(posterLabel, BorderLayout.CENTER);
        add(synopsisScrollPane, BorderLayout.SOUTH);
    }

    /**
     * Méthode publique que le chef d'orchestre (MainFrame) appellera
     * pour mettre à jour l'affichage avec les détails d'un film.
     */
    public void displayFilm(Film film) {
        if (film != null) {
            titleLabel.setText(film.getTitre());
            infoLabel.setText("Durée: " + film.getDureeMinutes() + " min | Classification: " + film.getClassification());
            synopsisArea.setText(film.getSynopsis());
            
            // Gestion de l'affiche (à adapter avec le chemin de tes images)
            ImageIcon posterIcon = new ImageIcon("images/" + film.getUrlAffiche());
            // Redimensionner l'image pour qu'elle s'adapte
            Image image = posterIcon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(image));

        } else {
            // État par défaut si aucun film n'est sélectionné
            titleLabel.setText("Sélectionnez un film dans la liste");
            infoLabel.setText("");
            synopsisArea.setText("Synopsis...");
            posterLabel.setIcon(null);
        }
    }
}