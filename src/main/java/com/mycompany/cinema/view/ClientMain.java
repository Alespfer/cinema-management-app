/*
 * Fichier ClientMainFrame.java
 * Version finale en français, adaptée pour NetBeans et conforme au cours.
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
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * C'est la fenêtre principale de l'espace client. Elle gère la navigation entre
 * les différents écrans avec un CardLayout.
 */
public class ClientMain extends javax.swing.JFrame {

    // --- PARTIE MANUELLE : VOTRE LOGIQUE TRADUITE EN FRANÇAIS ---
    // --- VOS VARIABLES D'INSTANCE ---
    private final ClientService clientService;
    private final Client clientConnecte;
    private ProgrammationPanel panneauProgrammation;
    private FilmDetail panneauDetailFilm;
    private AffichageSieges panneauSieges;
    private SnackSelectionPanel panneauSnacks;
    private CardLayout gestionnaireDeCartes;
    private Seance seanceEnCours;
    private List<Siege> siegesSelectionnes;
    private Tarif tarifSelectionne;

    /**
     * CONSTRUCTEUR
     */
    public ClientMain(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;

        // Appel au code généré par NetBeans pour construire la structure visuelle.
        initComponents();

        // --- Notre logique ajoutée après la construction ---
        // Configuration de la fenêtre
        setTitle("Alespfer Cinema - Espace Client (" + clientConnecte.getNom() + ")");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // On récupère le CardLayout du panneau principal (mainPanel)
        this.gestionnaireDeCartes = (CardLayout) mainPanel.getLayout();

        // On crée et on ajoute les panneaux dynamiquement
        configurerPanneaux();

        // On attache les écouteurs d'événements
        attacherEcouteurs();
    }

    /**
     * Crée les panneaux personnalisés et les ajoute au CardLayout du mainPanel.
     */
    private void configurerPanneaux() {
        panneauProgrammation = new ProgrammationPanel(clientService);
        panneauDetailFilm = new FilmDetail(clientService, this);
        panneauSieges = new AffichageSieges(clientService, clientConnecte);
        panneauSnacks = new SnackSelectionPanel(clientService);

        mainPanel.add(panneauProgrammation, "PROGRAMMATION");
        mainPanel.add(panneauDetailFilm, "FILM_DETAIL");
        mainPanel.add(panneauSieges, "SIEGES");
        mainPanel.add(panneauSnacks, "SNACKS");
    }

    /**
     * Regroupe tous les écouteurs d'événements pour plus de clarté.
     */
    private void attacherEcouteurs() {
        // --- GESTION DES ÉVÉNEMENTS AVEC DES CLASSES ANONYMES ---

       

        panneauProgrammation.setSeanceSelectionListener(new ProgrammationPanel.SeanceSelectionListener() {
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

        panneauSnacks.setNavigationListener(new SnackSelectionPanel.NavigationListener() {
            public void onRetourToSieges() {
                gestionnaireDeCartes.show(mainPanel, "SIEGES");
            }

            public void onSkip() {
                finaliserCommande(Collections.emptyMap());
            }
        });

        panneauSnacks.setListener(new SnackSelectionPanel.SnackSelectionListener() {
            public void onSnackSelectionCompleted(Map<ProduitSnack, Integer> panier) {
                finaliserCommande(panier);
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

    /**
     * Gère la transaction finale (réservation + snacks).
     */
    private void finaliserCommande(Map<ProduitSnack, Integer> panierSnacks) {
        if (seanceEnCours == null || siegesSelectionnes == null || siegesSelectionnes.isEmpty() || tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue lors de la récupération de votre sélection.", "Erreur Critique", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Integer> idsSieges = new ArrayList<>();
            for (Siege s : siegesSelectionnes) {
                idsSieges.add(s.getId());
            }

            Reservation reservation = clientService.finaliserCommandeComplete(
                    clientConnecte.getId(), seanceEnCours.getId(), idsSieges, tarifSelectionne.getId(), panierSnacks
            );

            BilletInfo infos = new BilletInfo();
            infos.clientNom = clientConnecte.getNom();
            infos.reservationId = reservation.getId();
            infos.filmTitre = clientService.getFilmDetails(seanceEnCours.getIdFilm()).getTitre();

            Salle salleTrouvee = null;
            for (Salle s : clientService.getAllSalles()) {
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
                for (Map.Entry<ProduitSnack, Integer> entree : panierSnacks.entrySet()) {
                    prixSnacks += entree.getKey().getPrixVente() * entree.getValue();
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
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }// TODO add your handling code here:
    }//GEN-LAST:event_deconnexionButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deconnexionButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton monCompteButton;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
