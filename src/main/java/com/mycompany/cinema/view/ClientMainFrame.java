package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.Film;
import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.Reservation;
import com.mycompany.cinema.Salle; // IMPORT AJOUTÉ
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.Siege;
import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; // IMPORT AJOUTÉ
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientMainFrame extends JFrame {
    
    // ... (le code de ce fichier est maintenant correct et n'a plus besoin d'être modifié) ...
    // ... (je le fournis à nouveau pour confirmation de l'intégralité)
    
    private final ClientService clientService;
    private final Client clientConnecte;

    private ProgrammationPanel programmationPanel;
    private FilmDetailPanel filmDetailPanel;
    private SiegePanel siegePanel;
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

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JButton monCompteButton = new JButton("Mon Compte");
        topPanel.add(monCompteButton);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        programmationPanel = new ProgrammationPanel(clientService);
        filmDetailPanel = new FilmDetailPanel(clientService);
        siegePanel = new SiegePanel(clientService, clientConnecte);
        snackSelectionPanel = new SnackSelectionPanel(clientService);
        
        mainPanel.add(programmationPanel, "PROGRAMMATION");
        mainPanel.add(filmDetailPanel, "FILM_DETAIL");
        mainPanel.add(siegePanel, "SIEGES");
        mainPanel.add(snackSelectionPanel, "SNACKS");
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        monCompteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EspaceClientDialog espaceClientDialog = new EspaceClientDialog(ClientMainFrame.this, clientService, clientConnecte);
                espaceClientDialog.setVisible(true);
            }
        });

        programmationPanel.setSeanceSelectionListener(new ProgrammationPanel.SeanceSelectionListener() {
            public void onSeanceSelected(Seance seance) {
                if (seance != null) {
                    Film film = clientService.getFilmDetails(seance.getIdFilm());
                    filmDetailPanel.displayFilmAndSeances(film, seance.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
                    cardLayout.show(mainPanel, "FILM_DETAIL");
                }
            }
        });
        
        filmDetailPanel.setSeanceSelectionListener(new FilmDetailPanel.SeanceSelectionListener() {
            public void onSeanceSelected(Seance seance) {
                seanceEnCours = seance;
                siegePanel.displaySieges(seance);
                cardLayout.show(mainPanel, "SIEGES");
            }
        });

        siegePanel.setReservationListener(new SiegePanel.ReservationListener() {
            public void onReservationDetailsSelected(List<Siege> sieges, Tarif tarif) {
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
                handleFinalisation(Collections.<ProduitSnack, Integer>emptyMap());
            }
        });

        snackSelectionPanel.setListener(new SnackSelectionPanel.SnackSelectionListener() {
            public void onSnackSelectionCompleted(Map<ProduitSnack, Integer> cart) {
                handleFinalisation(cart);
            }
        });

        filmDetailPanel.setRetourListener(new FilmDetailPanel.RetourListener() {
            public void onRetourClicked() {
                cardLayout.show(mainPanel, "PROGRAMMATION");
            }
        });
        
        siegePanel.setRetourListener(new SiegePanel.RetourListener() {
            public void onRetourClicked() {
                if (seanceEnCours != null) {
                    Film filmPrecedent = clientService.getFilmDetails(seanceEnCours.getIdFilm());
                    filmDetailPanel.displayFilmAndSeances(filmPrecedent, seanceEnCours.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
                }
                cardLayout.show(mainPanel, "FILM_DETAIL");
            }
        });
    }

    private void handleFinalisation(Map<ProduitSnack, Integer> panierSnacks) {
        if (seanceEnCours == null || siegesSelectionnes == null || siegesSelectionnes.isEmpty() || tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue, des informations de réservation sont manquantes.", "Erreur Critique", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Integer> siegeIds = new ArrayList<>();
            for (Siege s : siegesSelectionnes) {
                siegeIds.add(s.getId());
            }

            Reservation reservation = clientService.finaliserCommandeComplete(
                clientConnecte.getId(),
                seanceEnCours.getId(),
                siegeIds,
                tarifSelectionne.getId(),
                panierSnacks
            );
            
            BilletInfo infos = new BilletInfo();
            infos.clientNom = clientConnecte.getNom();
            infos.reservationId = reservation.getId();
            infos.filmTitre = clientService.getFilmDetails(seanceEnCours.getIdFilm()).getTitre();
            
            Optional<Salle> salleOpt = clientService.getAllSalles().stream().filter(s -> s.getId() == seanceEnCours.getIdSalle()).findFirst();
            infos.salleNumero = salleOpt.isPresent() ? salleOpt.get().getNumero() : "Salle " + seanceEnCours.getIdSalle();
            
            infos.seanceDateHeure = seanceEnCours.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
            infos.sieges = siegesSelectionnes;
            infos.tarifLibelle = tarifSelectionne.getLibelle();
            
            double prixBillets = tarifSelectionne.getPrix() * siegesSelectionnes.size();
            double prixSnacks = 0;
            for(Map.Entry<ProduitSnack, Integer> entry : panierSnacks.entrySet()){
                prixSnacks += entry.getKey().getPrixVente() * entry.getValue();
            }
            infos.prixTotal = String.format("%.2f €", prixBillets + prixSnacks);
            infos.panierSnacks = panierSnacks;

            BilletDialog billetDialog = new BilletDialog(this, infos);
            billetDialog.setVisible(true);

            cardLayout.show(mainPanel, "PROGRAMMATION");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la finalisation : \n" + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}