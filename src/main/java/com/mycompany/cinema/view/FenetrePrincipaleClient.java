/*
 * FenetrePrincipaleClient.java
 * Fenêtre principale de l'application côté client.
 * Elle orchestre la navigation entre les différents panneaux (programmation, détails, sièges, etc.)
 */
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

public class FenetrePrincipaleClient extends javax.swing.JFrame {

    // --- Services et Données ---
    private final ClientService clientService;
    private final Client clientConnecte;

    // --- Panneaux de l'interface  ---
    private PanneauProgrammation panneauProgrammation;
    private PanneauDetailsFilm panneauDetailFilm;
    private PanneauSelectionSieges panneauSieges;
    private PanneauSelectionSnack panneauSnacks;

    private CardLayout gestionnaireDeCartes;

    // --- Données de la réservation en cours ---
    private Seance seanceEnCours;
    private List<Siege> siegesSelectionnes;
    private Tarif tarifSelectionne;

    private PanneauPaiement panneauPaiement;
    private List<LignePanier> panierSnacksEnCours;  

    /**
     * Constructeur de la fenêtre principale.
     */
    public FenetrePrincipaleClient(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;
        initComponents();

        setTitle("Cinema - PISE 2025 (" + clientConnecte.getNom() + ")");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation de la navigation et des panneaux
        this.gestionnaireDeCartes = (CardLayout) penneauPrincipal.getLayout();
        initialiserPanneaux();
        attacherEcouteurs();
    }

    /**
     * Crée les instances de tous les panneaux de l'application et les ajoute au
     * CardLayout.
     */
    private void initialiserPanneaux() {
        panneauProgrammation = new PanneauProgrammation(clientService);
        panneauDetailFilm = new PanneauDetailsFilm(clientService, this);
        panneauSieges = new PanneauSelectionSieges(clientService, clientConnecte);
        panneauSnacks = new PanneauSelectionSnack(clientService);
        panneauPaiement = new PanneauPaiement();

        penneauPrincipal.add(panneauProgrammation, "PROGRAMMATION");
        penneauPrincipal.add(panneauDetailFilm, "FILM_DETAIL");
        penneauPrincipal.add(panneauSieges, "SIEGES");
        penneauPrincipal.add(panneauSnacks, "SNACKS");
        penneauPrincipal.add(panneauPaiement, "PAIEMENT");
    }

    /**
     * Méthode centrale qui configure toutes les interactions entre les
     * panneaux. La fenêtre principale (this) "écoute" les événements envoyés
     * par ses panneaux enfants.
     */
    private void attacherEcouteurs() {

        // --- Étape 1 : De la PanneauProgrammation aux Détails du Film ---
        panneauProgrammation.setSeanceSelectionListener(new PanneauProgrammation.SeanceSelectionListener() {
      
            public void gererSeanceSelectionnee(Seance seance) {
                if (seance != null) {
                    Film film = clientService.trouverDetailsFilm(seance.getIdFilm());
                    panneauDetailFilm.afficherFilmEtSeances(film, seance.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
                    gestionnaireDeCartes.show(penneauPrincipal, "FILM_DETAIL");
                }
            }


        });

        // --- Étape 2 : Des Détails aux Sièges ---
        panneauDetailFilm.setSeanceSelectionListener(new PanneauDetailsFilm.SeanceSelectionListener() {
            public void gererSeanceSelectionnee(Seance seance) {
                seanceEnCours = seance;
                panneauSieges.initialiserPlanDeSalle(seance);
                gestionnaireDeCartes.show(penneauPrincipal, "SIEGES");
            }
        });

        // --- Étape 3 : Des Sièges aux Snacks ---
        panneauSieges.setEcouteurReservation(new PanneauSelectionSieges.EcouteurReservation() {
            public void detailsReservationSelectionnes(List<Siege> sieges, Tarif tarif) {
                siegesSelectionnes = sieges;
                tarifSelectionne = tarif;
                panneauSnacks.reinitialiserPanneau();
                gestionnaireDeCartes.show(penneauPrincipal, "SNACKS");
            }
        });

        // --- Étape 4 : Navigation depuis les Snacks ---
        panneauSnacks.setNavigationListener(new PanneauSelectionSnack.NavigationListener() {
            public void RetourVersSieges() {
                gestionnaireDeCartes.show(penneauPrincipal, "SIEGES");
            }

            public void Skip() {
                panierSnacksEnCours = new ArrayList<>();
                procederAuPaiement();
            }

        });
        // Quand l'utilisateur a fini sa sélection de snacks, on mémorise le panier et 
        // on lance l'étape de paiement
        panneauSnacks.setListener(new PanneauSelectionSnack.SnackSelectionListener() {
            public void SelectionSnacksTerminee(List<LignePanier> panier) {
                panierSnacksEnCours = panier;
                procederAuPaiement();
            }
        });

        // --- Gestion des Retours en arrière ---
        panneauDetailFilm.setRetourListener(new PanneauDetailsFilm.RetourListener() {
            public void gererRetour() {
                gestionnaireDeCartes.show(penneauPrincipal, "PROGRAMMATION");
            }
        });

        panneauSieges.setEcouteurRetour(new PanneauSelectionSieges.EcouteurRetour() {
            public void gererRetour() {
                if (seanceEnCours != null) {
                    Film filmPrecedent = clientService.trouverDetailsFilm(seanceEnCours.getIdFilm());
                    panneauDetailFilm.afficherFilmEtSeances(filmPrecedent, seanceEnCours.getDateHeureDebut().toLocalDate(), clientConnecte.getId());
                }
                gestionnaireDeCartes.show(penneauPrincipal, "FILM_DETAIL");
            }
        });

        panneauPaiement.setListener(new PanneauPaiement.PaiementListener() {
            @Override
            public void PaiementValide() {
                // Le paiement est "OK", on peut finaliser la commande
                finaliserCommande(panierSnacksEnCours);
            }

            @Override
            public void gererRetour() {
                gestionnaireDeCartes.show(penneauPrincipal, "SNACKS");
            }
        });
    }

    /**
     * Prépare et affiche le panneau de paiement. Cette méthode centralise le
     * calcul du montant total et la transition de la vue.
     */
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
        panneauPaiement.reinitialiser();
        gestionnaireDeCartes.show(penneauPrincipal, "PAIEMENT");
    }

    /**
     * Étape finale : le paiement est validé. On enregistre tout en base de
     * données et on affiche le ticket de confirmation.
     *
     * @param panierDeSnacks La liste des snacks choisis par l'utilisateur.
     */
    private void finaliserCommande(List<LignePanier> panierSnacks) { 
        if (seanceEnCours == null || siegesSelectionnes == null || siegesSelectionnes.isEmpty() || tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue.", "Erreur Critique", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            
            // 1. On prépare les données pour le service
            List<Integer> idsSieges = new ArrayList<>();
            for (Siege siege : siegesSelectionnes) {
                idsSieges.add(siege.getId());
            }
            
            
            // 2. On appelle le service pour enregistrer la commande complète (réservation + vente de snacks)
            Reservation reservation = clientService.finaliserCommandeComplete(
                    clientConnecte.getId(), seanceEnCours.getId(), idsSieges, tarifSelectionne.getId(), panierSnacks
            );

            
            // 3. On prépare les informations pour l'affichage du ticket de confirmation
            BilletInfo infos = new BilletInfo();
            infos.clientNom = clientConnecte.getNom();
            infos.reservationId = reservation.getId();
            infos.filmTitre = clientService.trouverDetailsFilm(seanceEnCours.getIdFilm()).getTitre();

            Salle salleTrouvee = null;
            List<Salle> salles = clientService.trouverToutesLesSalles();
            for (Salle s : salles) {
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
                // Pour chaque LignePanier 'ligne' dans le panierSnacks nous utilisons les getters
                for (LignePanier ligne : panierSnacks) {
                    prixSnacks = prixSnacks + (ligne.getProduit().getPrixVente() * ligne.getQuantite());
                }
            }
            infos.prixTotal = String.format("%.2f €", prixBillets + prixSnacks);
            infos.panierSnacks = panierSnacks;

            // On affiche la fenêtre de dialogue (le ticket)
            BilletDialog billetDialog = new BilletDialog(this, true, infos);
            billetDialog.setVisible(true);

            gestionnaireDeCartes.show(penneauPrincipal, "PROGRAMMATION");
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
        penneauPrincipal = new javax.swing.JPanel();

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

        penneauPrincipal.setLayout(new java.awt.CardLayout());
        getContentPane().add(penneauPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void monCompteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monCompteButtonActionPerformed
        FenetreEspaceClient espaceClient = new FenetreEspaceClient(this, true, clientService, clientConnecte);
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
            new FenetreConnexion().setVisible(true);
        }// TODO add your handling code here:
    }//GEN-LAST:event_deconnexionButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deconnexionButton;
    private javax.swing.JButton monCompteButton;
    private javax.swing.JPanel penneauPrincipal;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
