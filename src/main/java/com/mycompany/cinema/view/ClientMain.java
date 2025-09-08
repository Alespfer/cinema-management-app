/*
 * Fichier ClientMainFrame.java
 * Version finale en français, adaptée pour NetBeans et conforme au cours.
 */
// Contenu complet et corrigé pour ClientMain.java
package com.mycompany.cinema.view;

import com.mycompany.cinema.LignePanier;
import com.mycompany.cinema.*;
import com.mycompany.cinema.service.ClientService;
import java.awt.CardLayout;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientMain extends javax.swing.JFrame {

    private final ClientService clientService;
    private final Client clientConnecte;
    private Programmation panneauProgrammation;
    private FilmDetail panneauDetailFilm;
    private AffichageSieges panneauSieges;
    private SnackSelection panneauSnacks;
    private CardLayout gestionnaireDeCartes;
    private Seance seanceEnCours;
    private List<Siege> siegesSelectionnes;
    private Tarif tarifSelectionne;

    private PaiementPanel panneauPaiement;
    private List<LignePanier> panierSnacksEnCours;  // <-- LIGNE MANQUANTE
// <-- LIGNE MANQUANTE

    public ClientMain(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;
        initComponents();

        setTitle("Cinema - PISE 2025 (" + clientConnecte.getNom() + ")");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.gestionnaireDeCartes = (CardLayout) mainPanel.getLayout();
        configurerPanneaux();
        attacherEcouteurs();
    }

    private void configurerPanneaux() {
        panneauProgrammation = new Programmation(clientService);
        panneauDetailFilm = new FilmDetail(clientService, this);
        panneauSieges = new AffichageSieges(clientService, clientConnecte);
        panneauSnacks = new SnackSelection(clientService);

        // --- DEBUT DE LA CORRECTION ---
        // Il manquait la ligne qui crée réellement l'objet PaiementPanel
        panneauPaiement = new PaiementPanel();
        // --- FIN DE LA CORRECTION ---

        mainPanel.add(panneauProgrammation, "PROGRAMMATION");
        mainPanel.add(panneauDetailFilm, "FILM_DETAIL");
        mainPanel.add(panneauSieges, "SIEGES");
        mainPanel.add(panneauSnacks, "SNACKS");

        // --- DEBUT DE LA CORRECTION ---
        // Il manquait la ligne qui ajoute le nouveau panneau au CardLayout
        mainPanel.add(panneauPaiement, "PAIEMENT");
        // --- FIN DE LA CORRECTION ---
    }

    private void attacherEcouteurs() {
        panneauProgrammation.setSeanceSelectionListener(new Programmation.SeanceSelectionListener() {
            public void onSeanceSelected(Seance seance) {
                if (seance != null) {
                    Film film = clientService.getFilmDetails(seance.getIdFilm());
                    panneauDetailFilm.displayFilmAndSeances(film, seance.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
                    gestionnaireDeCartes.show(mainPanel, "FILM_DETAIL");
                }
            }
        });

        panneauDetailFilm.setSeanceSelectionListener(new FilmDetail.SeanceSelectionListener() {
            public void onSeanceSelected(Seance seance) {
                seanceEnCours = seance;
                panneauSieges.afficherSieges(seance);
                gestionnaireDeCartes.show(mainPanel, "SIEGES");
            }
        });

        panneauSieges.setEcouteurReservation(new AffichageSieges.EcouteurReservation() {
            public void detailsReservationSelectionnes(List<Siege> sieges, Tarif tarif) {
                siegesSelectionnes = sieges;
                tarifSelectionne = tarif;
                panneauSnacks.resetPanel();
                gestionnaireDeCartes.show(mainPanel, "SNACKS");
            }
        });

        panneauSnacks.setNavigationListener(new SnackSelection.NavigationListener() {
            public void onRetourToSieges() {
                gestionnaireDeCartes.show(mainPanel, "SIEGES");
            }

            public void onSkip() {
                // APRÈS : On fait la même chose, mais en sauvegardant un panier vide.
                panierSnacksEnCours = new ArrayList<>(); // Panier vide
                procederAuPaiement();
            }
        });

        panneauSnacks.setListener(new SnackSelection.SnackSelectionListener() {
            public void onSnackSelectionCompleted(List<LignePanier> panier) {
                // APRÈS : On sauvegarde le panier dans une variable de la classe...
                panierSnacksEnCours = panier;
                // ...et on lance la nouvelle étape de paiement.
                procederAuPaiement();
            }
        });

        panneauDetailFilm.setRetourListener(new FilmDetail.RetourListener() {
            public void onRetourClicked() {
                gestionnaireDeCartes.show(mainPanel, "PROGRAMMATION");
            }
        });

        panneauSieges.setEcouteurRetour(new AffichageSieges.EcouteurRetour() {
            public void auClicSurRetour() {
                if (seanceEnCours != null) {
                    Film filmPrecedent = clientService.getFilmDetails(seanceEnCours.getIdFilm());
                    panneauDetailFilm.displayFilmAndSeances(filmPrecedent, seanceEnCours.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
                }
                gestionnaireDeCartes.show(mainPanel, "FILM_DETAIL");
            }
        });
        panneauPaiement.setListener(new PaiementPanel.PaiementListener() {
            @Override
            public void onPaiementValide() {
                // Le paiement est "OK", on peut finaliser la commande
                finaliserCommande(panierSnacksEnCours);
            }

            // VOICI LA PARTIE À AJOUTER
            @Override
            public void onRetour() {
                // L'utilisateur veut revenir en arrière, on affiche le panneau des snacks.
                // On utilise le gestionnaire de cartes pour changer de vue.
                gestionnaireDeCartes.show(mainPanel, "SNACKS");
            }
        });
    }

    // Nouvelle méthode pour gérer la transition vers le paiement
    private void procederAuPaiement() {
        // Calculer le montant total
        double prixBillets = tarifSelectionne.getPrix() * siegesSelectionnes.size();
        double prixSnacks = 0;
        for (LignePanier ligne : panierSnacksEnCours) {
            prixSnacks += ligne.getProduit().getPrixVente() * ligne.getQuantite();
        }
        double montantTotal = prixBillets + prixSnacks;

        // Passer le montant au panneau de paiement et l'afficher
        panneauPaiement.setMontantTotal(montantTotal);
        panneauPaiement.reset(); // On vide les champs avant d'afficher
        gestionnaireDeCartes.show(mainPanel, "PAIEMENT");
    }

    private void finaliserCommande(List<LignePanier> panierSnacks) { // CONFORMITÉ ABSOLUE
        if (seanceEnCours == null || siegesSelectionnes == null || siegesSelectionnes.isEmpty() || tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue.", "Erreur Critique", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            List<Integer> idsSieges = new ArrayList<>();
// "Pour chaque Siege 'siege' dans la liste siegesSelectionnes..."
            for (Siege siege : siegesSelectionnes) {
                idsSieges.add(siege.getId());
            }

            Reservation reservation = clientService.finaliserCommandeComplete(
                    clientConnecte.getId(), seanceEnCours.getId(), idsSieges, tarifSelectionne.getId(), panierSnacks
            );

            BilletInfo infos = new BilletInfo();
            infos.clientNom = clientConnecte.getNom();
            infos.reservationId = reservation.getId();
            infos.filmTitre = clientService.getFilmDetails(seanceEnCours.getIdFilm()).getTitre();

            Salle salleTrouvee = null;
            List<Salle> salles = clientService.getAllSalles();
// "Pour chaque Salle 's' dans la liste salles..."
            for (Salle s : salles) {
                if (s.getId() == seanceEnCours.getIdSalle()) {
                    salleTrouvee = s;
                    break; // Le break est parfaitement valide dans une boucle for-each.
                }
            }
            infos.salleNumero = (salleTrouvee != null) ? salleTrouvee.getNumero() : "Salle " + seanceEnCours.getIdSalle();

            infos.seanceDateHeure = seanceEnCours.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
            infos.sieges = siegesSelectionnes;
            infos.tarifLibelle = tarifSelectionne.getLibelle();

            double prixBillets = tarifSelectionne.getPrix() * siegesSelectionnes.size();
            double prixSnacks = 0;
            if (panierSnacks != null) {
                // "Pour chaque LignePanier 'ligne' dans le panierSnacks..."
                for (LignePanier ligne : panierSnacks) {
                    // Nous utilisons les getters, conformément à notre correction sur l'encapsulation de LignePanier
                    prixSnacks = prixSnacks + (ligne.getProduit().getPrixVente() * ligne.getQuantite());
                }
            }
            infos.prixTotal = String.format("%.2f €", prixBillets + prixSnacks);
            infos.panierSnacks = panierSnacks;

            Ticket billetDialog = new Ticket(this, true, infos);
            billetDialog.setVisible(true);

            gestionnaireDeCartes.show(mainPanel, "PROGRAMMATION");
            panneauProgrammation.rechercher();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la finalisation : \n" + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        monCompteButton = new javax.swing.JButton();
        deconnexionButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        monCompteButton.setText("Mon Compte");
        monCompteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monCompteButtonActionPerformed(evt);
            }
        });
        topPanel.add(monCompteButton);

        deconnexionButton.setText("Déconnexion");
        deconnexionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deconnexionButtonActionPerformed(evt);
            }
        });
        topPanel.add(deconnexionButton);

        getContentPane().add(topPanel, java.awt.BorderLayout.PAGE_START);

        mainPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void monCompteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monCompteButtonActionPerformed
        EspaceClient espaceClient = new EspaceClient(this, true, clientService, clientConnecte);
        espaceClient.setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_monCompteButtonActionPerformed

    private void deconnexionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnexionButtonActionPerformed
        Object[] options = {"Oui", "Non"};
        int reponse = JOptionPane.showOptionDialog(
                this, "Êtes-vous sûr de vouloir vous déconnecter ?",
                "Confirmation de déconnexion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, // Pas d'icône personnalisée
                options, // Nos boutons personnalisés
                options[0] // Le bouton par défaut ("Oui")
        );

        if (reponse == JOptionPane.YES_OPTION) {
            // 1. On ferme la fenêtre actuelle.
            dispose();

            // 2. CORRECTION : On crée ET affiche la nouvelle fenêtre de connexion directement.
            new Login().setVisible(true);
        }// TODO add your handling code here:
    }//GEN-LAST:event_deconnexionButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deconnexionButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton monCompteButton;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
