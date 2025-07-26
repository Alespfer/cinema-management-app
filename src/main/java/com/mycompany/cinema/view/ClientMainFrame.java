package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.service.ClientService;
import javax.swing.*;
import java.awt.*;

public class ClientMainFrame extends JFrame {
    
    private final ClientService clientService;
    private final Client clientConnecte;

    // Panneaux de sélection (à gauche)
    private FilmListPanel filmListPanel;
    private SeancePanel seancePanel;
    
    // Panneaux d'affichage (à droite, gérés par le CardLayout)
    private FilmDetailPanel filmDetailPanel;
    private SiegePanel siegePanel;

    // Le gestionnaire de "paquet de cartes" pour le panneau de droite
    private CardLayout cardLayout;
    private JPanel rightPanel;

    public ClientMainFrame(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;
        
        setTitle("Espace Client - " + clientConnecte.getNom());
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setResizeWeight(0.25);
        add(mainSplit);

        // --- PARTIE GAUCHE : Sélection Film et Séance ---
        JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        filmListPanel = new FilmListPanel(this.clientService);
        seancePanel = new SeancePanel(this.clientService);
        leftSplit.setTopComponent(filmListPanel);
        leftSplit.setBottomComponent(seancePanel);
        leftSplit.setResizeWeight(0.5);
        mainSplit.setLeftComponent(leftSplit);
        
        // --- PARTIE DROITE : Le "Paquet de Cartes" ---
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);

        // On crée les deux "cartes" (panneaux)
        filmDetailPanel = new FilmDetailPanel();
        siegePanel = new SiegePanel(this.clientService, this.clientConnecte); // On lui passe aussi le client !
        
        // On ajoute les cartes au paquet avec un nom pour les identifier
        rightPanel.add(filmDetailPanel, "DETAILS");
        rightPanel.add(siegePanel, "SIEGES");
        
        mainSplit.setRightComponent(rightPanel);
        
        // =========================================================================
        // ORCHESTRATION DES PANNEAUX
        // =========================================================================
        
        // Quand un film est sélectionné...
        filmListPanel.setFilmSelectionListener(film -> {
            // 1. On affiche les détails du film sur la première carte
            filmDetailPanel.displayFilm(film);
            // 2. On DIT au CardLayout de MONTRER la carte "DETAILS"
            cardLayout.show(rightPanel, "DETAILS");
            // 3. On charge les séances dans le panneau de gauche
            seancePanel.loadSeances(film);
            // 4. On vide le panneau des sièges au cas où une séance précédente était affichée
            siegePanel.clearPanel();
        });
        
        // Quand une séance est sélectionnée...
        seancePanel.setSeanceSelectionListener(seance -> {
            // 1. On affiche le plan de la salle pour cette séance sur la deuxième carte
            siegePanel.displaySieges(seance);
            // 2. On DIT au CardLayout de MONTRER la carte "SIEGES"
            cardLayout.show(rightPanel, "SIEGES");
        });
    }
}