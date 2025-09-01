/*
 * Fichier ClientMainFrame.java
 * Version corrigée et finale.
 */
package com.mycompany.cinema.view;

// Imports nécessaires
import com.mycompany.cinema.Client;
import com.mycompany.cinema.Film;
import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.Reservation;
import com.mycompany.cinema.Salle;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.Siege;
import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * C'est la fenêtre principale de l'espace client. Elle gère la navigation entre
 * les différents écrans avec un CardLayout.
 */
public class ClientMainFrame extends JFrame {

    // Variables d'instance
    private final ClientService clientService;
    private final Client clientConnecte;
    private ProgrammationPanel programmationPanel;
    private FilmDetail filmDetail;
    private AffichageSieges siegePanel;
    private SnackSelectionPanel snackSelectionPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Seance seanceEnCours;
    private List<Siege> siegesSelectionnes;
    private Tarif tarifSelectionne;

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
        getContentPane().setLayout(new BorderLayout());

        // Panneau supérieur
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JButton monCompteButton = new JButton("Mon Compte");
        JButton deconnexionButton = new JButton("Déconnexion");
        topPanel.add(monCompteButton);
        topPanel.add(deconnexionButton);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        // Panneau principal avec CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Instanciation des panneaux
        programmationPanel = new ProgrammationPanel(clientService);
        filmDetail = new FilmDetail(clientService, this);
        siegePanel = new AffichageSieges(clientService, clientConnecte);
        snackSelectionPanel = new SnackSelectionPanel(clientService);

        // Ajout des panneaux au CardLayout
        mainPanel.add(programmationPanel, "PROGRAMMATION");
        mainPanel.add(filmDetail, "FILM_DETAIL");
        mainPanel.add(siegePanel, "SIEGES");
        mainPanel.add(snackSelectionPanel, "SNACKS");

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // --- GESTION DES ÉVÉNEMENTS ---
        deconnexionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        ClientMainFrame.this,
                        "Êtes-vous sûr de vouloir vous déconnecter ?",
                        "Confirmation de déconnexion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (response == JOptionPane.YES_OPTION) {
                    dispose();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new LoginFrame().setVisible(true);
                        }
                    });
                }
            }
        });

        monCompteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EspaceClient espaceClient = new EspaceClient(ClientMainFrame.this, true, clientService, clientConnecte);
                espaceClient.setVisible(true);
            }
        });

        programmationPanel.setSeanceSelectionListener(new ProgrammationPanel.SeanceSelectionListener() {
            public void onSeanceSelected(Seance seance) {
                if (seance != null) {
                    Film film = clientService.getFilmDetails(seance.getIdFilm());
                    filmDetail.displayFilmAndSeances(film, seance.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
                    cardLayout.show(mainPanel, "FILM_DETAIL");
                }
            }
        });

        filmDetail.setSeanceSelectionListener(new FilmDetail.SeanceSelectionListener() {
            public void onSeanceSelected(Seance seance) {
                seanceEnCours = seance;
                siegePanel.afficherSieges(seance);
                cardLayout.show(mainPanel, "SIEGES");
            }
        });

        // CORRECTION 1 : Le nom de la méthode est maintenant 'detailsReservationSelectionnes'
        siegePanel.setEcouteurReservation(new AffichageSieges.EcouteurReservation() {
            public void detailsReservationSelectionnes(List<Siege> sieges, Tarif tarif) {
                siegesSelectionnes = sieges;
                tarifSelectionne = tarif;
                snackSelectionPanel.resetPanel();
                cardLayout.show(mainPanel, "SNACKS");
            }
        });

        snackSelectionPanel.setNavigationListener(new SnackSelectionPanel.NavigationListener() {
            public void onRetourToSieges() {
                cardLayout.show(mainPanel, "SIEGES");
            }

            public void onSkip() {
                handleFinalisation(Collections.emptyMap());
            }
        });

        snackSelectionPanel.setListener(new SnackSelectionPanel.SnackSelectionListener() {
            public void onSnackSelectionCompleted(Map<ProduitSnack, Integer> cart) {
                handleFinalisation(cart);
            }
        });

        filmDetail.setRetourListener(new FilmDetail.RetourListener() {
            public void onRetourClicked() {
                cardLayout.show(mainPanel, "PROGRAMMATION");
            }
        });

        // CORRECTION 2 : Le nom de la méthode est maintenant 'auClicSurRetour'
        siegePanel.setEcouteurRetour(new AffichageSieges.EcouteurRetour() {
            public void auClicSurRetour() {
                if (seanceEnCours != null) {
                    Film filmPrecedent = clientService.getFilmDetails(seanceEnCours.getIdFilm());
                    filmDetail.displayFilmAndSeances(filmPrecedent, seanceEnCours.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
                }
                cardLayout.show(mainPanel, "FILM_DETAIL");
            }
        });
    }

    private void handleFinalisation(Map<ProduitSnack, Integer> panierSnacks) {
        if (seanceEnCours == null || siegesSelectionnes == null || siegesSelectionnes.isEmpty() || tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue lors de la récupération de votre sélection.", "Erreur Critique", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Integer> siegeIds = new ArrayList<>();
            for (Siege s : siegesSelectionnes) {
                siegeIds.add(s.getId());
            }

            Reservation reservation = clientService.finaliserCommandeComplete(
                    clientConnecte.getId(), seanceEnCours.getId(), siegeIds, tarifSelectionne.getId(), panierSnacks
            );

            BilletInfo infos = new BilletInfo();
            infos.clientNom = clientConnecte.getNom();
            infos.reservationId = reservation.getId();
            infos.filmTitre = clientService.getFilmDetails(seanceEnCours.getIdFilm()).getTitre();

            Optional<Salle> salleOpt = clientService.getAllSalles().stream()
                    .filter(s -> s.getId() == seanceEnCours.getIdSalle())
                    .findFirst();
            infos.salleNumero = salleOpt.isPresent() ? salleOpt.get().getNumero() : "Salle " + seanceEnCours.getIdSalle();

            infos.seanceDateHeure = seanceEnCours.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
            infos.sieges = siegesSelectionnes;
            infos.tarifLibelle = tarifSelectionne.getLibelle();

            double prixBillets = tarifSelectionne.getPrix() * siegesSelectionnes.size();
            double prixSnacks = 0;
            if (panierSnacks != null) {
                for (Map.Entry<ProduitSnack, Integer> entry : panierSnacks.entrySet()) {
                    prixSnacks += entry.getKey().getPrixVente() * entry.getValue();
                }
            }
            infos.prixTotal = String.format("%.2f €", prixBillets + prixSnacks);
            infos.panierSnacks = panierSnacks;

            Ticket billetDialog = new Ticket(this, true, infos);
            billetDialog.setVisible(true);

            cardLayout.show(mainPanel, "PROGRAMMATION");
            // Assurez-vous que la méthode rechercher() est publique dans ProgrammationPanel
            programmationPanel.rechercher();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la finalisation : \n" + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
