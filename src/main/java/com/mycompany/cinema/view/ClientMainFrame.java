package com.mycompany.cinema.view;

// Importe tous les modèles et le service nécessaires à la logique de cette fenêtre.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * C'est la fenêtre principale de l'espace client.
 * Elle est le conteneur central qui gère la navigation entre les différents écrans
 * du processus de réservation (programmation, détail du film, choix des sièges, snacks).
 * Elle utilise un 'CardLayout' pour afficher un seul panneau à la fois.
 */
public class ClientMainFrame extends JFrame {
    
    // Le service qui contient toute la logique métier côté client.
    private final ClientService clientService;
    // L'objet du client actuellement connecté, pour savoir qui fait la réservation.
    private final Client clientConnecte;

    // Déclaration des différents panneaux qui seront affichés dans le CardLayout.
    private ProgrammationPanel programmationPanel;
    private FilmDetailPanel filmDetailPanel;
    private SiegePanel siegePanel;
    private SnackSelectionPanel snackSelectionPanel;
    
    // Le gestionnaire de layout qui permet de "naviguer" entre les panneaux.
    private CardLayout cardLayout;
    private JPanel mainPanel; // Le panneau qui contient tous les autres panneaux.

    // Variables pour stocker les choix de l'utilisateur au fur et à mesure du processus.
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

        // Panneau supérieur avec le bouton "Mon Compte".
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JButton monCompteButton = new JButton("Mon Compte");
        topPanel.add(monCompteButton);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        // Initialisation du CardLayout et du panneau principal qui l'utilisera.
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Instanciation de chaque panneau de notre workflow.
        programmationPanel = new ProgrammationPanel(clientService);
        filmDetailPanel = new FilmDetailPanel(clientService);
        siegePanel = new SiegePanel(clientService, clientConnecte);
        snackSelectionPanel = new SnackSelectionPanel(clientService);
        
        // Ajout de chaque panneau au CardLayout avec un nom unique pour pouvoir les appeler.
        mainPanel.add(programmationPanel, "PROGRAMMATION");
        mainPanel.add(filmDetailPanel, "FILM_DETAIL");
        mainPanel.add(siegePanel, "SIEGES");
        mainPanel.add(snackSelectionPanel, "SNACKS");
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // --- GESTION DES ÉVÉNEMENTS ET DE LA NAVIGATION ---
        // Chaque bloc ci-dessous définit comment on passe d'un panneau à l'autre.

        // Quand on clique sur "Mon Compte", on ouvre la boîte de dialogue de l'espace client.
        monCompteButton.addActionListener(e -> {
            EspaceClientDialog espaceClientDialog = new EspaceClientDialog(this, clientService, clientConnecte);
            espaceClientDialog.setVisible(true);
        });

        // Quand l'utilisateur choisit une séance dans le panneau de programmation...
        programmationPanel.setSeanceSelectionListener(seance -> {
            if (seance != null) {
                // ...on récupère les détails du film correspondant via le service...
                Film film = clientService.getFilmDetails(seance.getIdFilm());
                // ...on demande au panneau de détail d'afficher ces informations...
                filmDetailPanel.displayFilmAndSeances(film, seance.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
                // ...et on bascule l'affichage sur le panneau de détail du film.
                cardLayout.show(mainPanel, "FILM_DETAIL");
            }
        });
        
        // Quand l'utilisateur choisit un horaire dans le panneau de détail du film...
        filmDetailPanel.setSeanceSelectionListener(seance -> {
            seanceEnCours = seance; // On mémorise la séance choisie.
            siegePanel.displaySieges(seance); // On affiche le plan de la salle pour cette séance.
            cardLayout.show(mainPanel, "SIEGES"); // On bascule sur le panneau de sélection des sièges.
        });

        // Quand l'utilisateur a choisi ses sièges et son tarif...
        siegePanel.setReservationListener((sieges, tarif) -> {
            siegesSelectionnes = sieges; // On mémorise les sièges.
            tarifSelectionne = tarif; // On mémorise le tarif.
            snackSelectionPanel.resetPanel(); // On réinitialise le panneau des snacks.
            cardLayout.show(mainPanel, "SNACKS"); // On passe au panneau de sélection des snacks.
        });

        // Gestion des boutons dans le panneau des snacks.
        snackSelectionPanel.setNavigationListener(new SnackSelectionPanel.NavigationListener() {
            public void onRetourToSieges() { cardLayout.show(mainPanel, "SIEGES"); }
            public void onSkip() { handleFinalisation(Collections.emptyMap()); } // Si on ignore les snacks.
        });
        snackSelectionPanel.setListener(this::handleFinalisation); // Quand on valide la sélection de snacks.

        // Gestion des boutons "Retour" sur les différents panneaux.
        filmDetailPanel.setRetourListener(() -> cardLayout.show(mainPanel, "PROGRAMMATION"));
        siegePanel.setRetourListener(() -> {
            if (seanceEnCours != null) {
                Film filmPrecedent = clientService.getFilmDetails(seanceEnCours.getIdFilm());
                filmDetailPanel.displayFilmAndSeances(filmPrecedent, seanceEnCours.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
            }
            cardLayout.show(mainPanel, "FILM_DETAIL");
        });
    }

    /**
     * Gère la finalisation de la commande (billets + snacks).
     * C'est la méthode qui appelle le service pour tout enregistrer.
     * @param panierSnacks Le panier de snacks (peut être vide).
     */
    private void handleFinalisation(Map<ProduitSnack, Integer> panierSnacks) {
        if (seanceEnCours == null || siegesSelectionnes == null || siegesSelectionnes.isEmpty() || tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue...", "Erreur Critique", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Integer> siegeIds = new ArrayList<>();
            for (Siege s : siegesSelectionnes) { siegeIds.add(s.getId()); }

            // Appel CRUCIAL au service métier pour enregistrer la commande complète.
            Reservation reservation = clientService.finaliserCommandeComplete(
                clientConnecte.getId(), seanceEnCours.getId(), siegeIds, tarifSelectionne.getId(), panierSnacks
            );
            
            // --- Préparation des informations pour l'affichage du billet ---
            BilletInfo infos = new BilletInfo();
            infos.clientNom = clientConnecte.getNom();
            infos.reservationId = reservation.getId();
            infos.filmTitre = clientService.getFilmDetails(seanceEnCours.getIdFilm()).getTitre();
            
            // Récupère le nom de la salle.
            Optional<Salle> salleOpt = clientService.getAllSalles().stream().filter(s -> s.getId() == seanceEnCours.getIdSalle()).findFirst();
            infos.salleNumero = salleOpt.isPresent() ? salleOpt.get().getNumero() : "Salle " + seanceEnCours.getIdSalle();
            
            infos.seanceDateHeure = seanceEnCours.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
            infos.sieges = siegesSelectionnes;
            infos.tarifLibelle = tarifSelectionne.getLibelle();
            
            // Calcul du prix total pour affichage.
            double prixBillets = tarifSelectionne.getPrix() * siegesSelectionnes.size();
            double prixSnacks = 0;
            for(Map.Entry<ProduitSnack, Integer> entry : panierSnacks.entrySet()){
                prixSnacks += entry.getKey().getPrixVente() * entry.getValue();
            }
            infos.prixTotal = String.format("%.2f €", prixBillets + prixSnacks);
            infos.panierSnacks = panierSnacks;

            // Affiche la boîte de dialogue de confirmation avec toutes les informations.
            BilletDialog billetDialog = new BilletDialog(this, infos);
            billetDialog.setVisible(true);

            // Une fois la commande terminée, on retourne à l'écran de programmation.
            cardLayout.show(mainPanel, "PROGRAMMATION");

        } catch (Exception ex) {
            // Si le service a renvoyé une erreur (ex: sièges déjà pris), on l'affiche à l'utilisateur.
            JOptionPane.showMessageDialog(this, "Erreur lors de la finalisation : \n" + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Affiche l'erreur détaillée dans la console pour le débogage.
        }
    }
}