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

    public ClientMain(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;
        initComponents();

        setTitle("Alespfer Cinema - Espace Client (" + clientConnecte.getNom() + ")");
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
        panneauSnacks = new SnackSelection(clientService); // CORRECTION: Instanciation de la bonne classe

        mainPanel.add(panneauProgrammation, "PROGRAMMATION");
        mainPanel.add(panneauDetailFilm, "FILM_DETAIL");
        mainPanel.add(panneauSieges, "SIEGES");
        mainPanel.add(panneauSnacks, "SNACKS");
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
                finaliserCommande(new ArrayList<LignePanier>()); // Appel avec une liste vide
            }
        });

        panneauSnacks.setListener(new SnackSelection.SnackSelectionListener() {
            public void onSnackSelectionCompleted(List<LignePanier> panier) {
                finaliserCommande(panier); // Transmission directe de la liste
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
    }

    private void finaliserCommande(List<LignePanier> panierSnacks) { // CONFORMITÉ ABSOLUE
        if (seanceEnCours == null || siegesSelectionnes == null || siegesSelectionnes.isEmpty() || tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue.", "Erreur Critique", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            List<Integer> idsSieges = new ArrayList<>();
            for (int i = 0; i < siegesSelectionnes.size(); i++) {
                idsSieges.add(siegesSelectionnes.get(i).getId());
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
            for (int i = 0; i < salles.size(); i++) {
                Salle s = salles.get(i);
                if (s.getId() == seanceEnCours.getIdSalle()) {
                    salleTrouvee = s;
                    break;
                }
            }
            infos.salleNumero = (salleTrouvee != null) ? salleTrouvee.getNumero() : "Salle " + seanceEnCours.getIdSalle();

            infos.seanceDateHeure = seanceEnCours.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
            infos.sieges = siegesSelectionnes;
            infos.tarifLibelle = tarifSelectionne.getLibelle();

            double prixBillets = tarifSelectionne.getPrix() * siegesSelectionnes.size();
            double prixSnacks = 0;
            if (panierSnacks != null) {
                for (int i = 0; i < panierSnacks.size(); i++) {
                    LignePanier ligne = panierSnacks.get(i);
                    prixSnacks = prixSnacks + (ligne.produit.getPrixVente() * ligne.quantite);
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
        int reponse = JOptionPane.showConfirmDialog(
                this, "Êtes-vous sûr de vouloir vous déconnecter ?",
                "Confirmation de déconnexion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

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
