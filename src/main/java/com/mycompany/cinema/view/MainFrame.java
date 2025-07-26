// Fichier : src/main/java/com/mycompany/cinema/view/MainFrame.java
package com.mycompany.cinema.view;

import com.mycompany.cinema.service.CinemaService;
import com.mycompany.cinema.service.impl.CinemaServiceImpl;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CinemaService cinemaService;
    private FilmListPanel filmListPanel;
    private FilmDetailPanel filmDetailPanel;

    public MainFrame() {
        this.cinemaService = new CinemaServiceImpl();
        setTitle("Gestion de Cinéma - Projet PISE");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 0));

        // 1. Créer les panneaux spécialisés
        filmListPanel = new FilmListPanel(this.cinemaService);
        filmDetailPanel = new FilmDetailPanel(); // N'a pas besoin du service, il est stupide

        // 2. Mettre en place la communication (le rôle du chef d'orchestre)
        filmListPanel.setFilmSelectionListener(film -> {
            filmDetailPanel.displayFilm(film);
        });

        // 3. Organiser les panneaux dans la fenêtre
        add(filmListPanel, BorderLayout.WEST);
        add(filmDetailPanel, BorderLayout.CENTER);
        
        // Définir une taille préférée pour la liste des films
        filmListPanel.setPreferredSize(new Dimension(300, 0));
    }
}