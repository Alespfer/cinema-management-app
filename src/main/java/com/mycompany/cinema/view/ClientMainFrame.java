package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.Film;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fenêtre principale de l'espace client.
 * Utilise un CardLayout pour naviguer entre la vue "Programmation" et les autres écrans.
 */
public class ClientMainFrame extends JFrame {
    
    private final ClientService clientService;
    private final Client clientConnecte;

    private ProgrammationPanel programmationPanel;
    private FilmDetailPanel filmDetailPanel;

    private SiegePanel siegePanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public ClientMainFrame(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;
        
        setTitle("Alespfer Cinema - Espace Client (" + clientConnecte.getNom() + ")");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Le conteneur principal de la fenêtre utilise un BorderLayout.
        getContentPane().setLayout(new BorderLayout());

        // =====================================================================
        // === DÉBUT DE L'AJOUT : Panneau supérieur pour les actions globales ===
        // =====================================================================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JButton monCompteButton = new JButton("Mon Compte");
        topPanel.add(monCompteButton);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        // =====================================================================
        // === FIN DE L'AJOUT                                                ===
        // =====================================================================

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        programmationPanel = new ProgrammationPanel(clientService);
        filmDetailPanel = new FilmDetailPanel(clientService);
        siegePanel = new SiegePanel(clientService, clientConnecte);
        
        mainPanel.add(programmationPanel, "PROGRAMMATION");
        mainPanel.add(filmDetailPanel, "FILM_DETAIL");
        mainPanel.add(siegePanel, "SIEGES");
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // --- Orchestration de la navigation ---

        monCompteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ouvre la nouvelle fenêtre de dialogue pour la gestion du compte.
                EspaceClientDialog espaceClientDialog = new EspaceClientDialog(ClientMainFrame.this, clientService, clientConnecte);
                espaceClientDialog.setVisible(true);
            }
        });

        programmationPanel.setSeanceSelectionListener(new ProgrammationPanel.SeanceSelectionListener() {
            public void onSeanceSelected(Seance seance) {
                if (seance != null) {
                    Film film = clientService.getFilmDetails(seance.getIdFilm());
                    filmDetailPanel.displayFilmAndSeances(film, seance.getDateHeureDebut().toLocalDate());
                    cardLayout.show(mainPanel, "FILM_DETAIL");
                }
            }
        });

        filmDetailPanel.setSeanceSelectionListener(new FilmDetailPanel.SeanceSelectionListener() {
            public void onSeanceSelected(Seance seance) {
                siegePanel.displaySieges(seance);
                cardLayout.show(mainPanel, "SIEGES");
            }
        });

        filmDetailPanel.setRetourListener(new FilmDetailPanel.RetourListener() {
            public void onRetourClicked() {
                cardLayout.show(mainPanel, "PROGRAMMATION");
            }
        });
        
        siegePanel.setRetourListener(new SiegePanel.RetourListener() {
            public void onRetourClicked() {
                cardLayout.show(mainPanel, "FILM_DETAIL");
            }
        });
    }
}