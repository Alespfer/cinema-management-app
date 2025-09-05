/*
 * Ce fichier a été généré par NetBeans.
 * Il a ensuite été modifié pour intégrer la logique métier.
 */
package com.mycompany.cinema.view;

// Imports nécessaires, regroupés et sans doublons
import com.mycompany.cinema.Billet;
import com.mycompany.cinema.Client;
import com.mycompany.cinema.Film;
import com.mycompany.cinema.Reservation;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.service.ClientService;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Panneau pour afficher l'historique des réservations.
 *
 * @author albertoesperon
 */
public class HistoriqueReservations extends javax.swing.JPanel {

    // --- VARIABLES D'INSTANCE ---
    private final ClientService clientService;
    private final Client clientConnecte;
    private DefaultTableModel tableModel;

    /**
     * CONSTRUCTEUR CORRECT ET COMPLET
     */
    public HistoriqueReservations(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;

        initComponents();

        this.setupTableModel();
        this.addListeners();
        this.loadHistorique();
    }

    // --- MÉTHODES DE LOGIQUE PRIVÉES ---
    private void setupTableModel() {
        String[] columnNames = {"ID Res.", "Date Réservation", "Film", "Séance", "Nb. Billets", "Prix Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historiqueTable.setModel(tableModel);
    }

    private void addListeners() {
        historiqueTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                annulerButton.setEnabled(historiqueTable.getSelectedRow() != -1);
            }
        });
    }

    private String formaterPrix(double valeur) {
        double valeurDecalee = valeur * 100;
        double valeurPourArrondi = valeurDecalee + 0.5;
        int valeurEntiereArrondie = (int) valeurPourArrondi;
        double valeurFinaleArrondie = valeurEntiereArrondie / 100.0;

        String valeurString = String.valueOf(valeurFinaleArrondie);
        int positionPoint = valeurString.indexOf('.');
        if (positionPoint != -1 && (valeurString.length() - positionPoint - 1) < 2) {
            valeurString = valeurString + "0";
        }

        String valeurAvecVirgule = valeurString.replace('.', ',');
        return valeurAvecVirgule + " €";
    }

    /**
     * Charge l'historique des réservations avec l'algorithme "de base".
     */
    public void loadHistorique() {
        tableModel.setRowCount(0);
        List<Reservation> reservations = clientService.getHistoriqueReservationsClient(clientConnecte.getId());
        List<Tarif> tousLesTarifs = clientService.getAllTarifs();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Reservation reservation : reservations) {
            List<Billet> billets = clientService.getBilletsByReservationId(reservation.getId());
            if (billets.isEmpty()) {
                continue;
            }

            Billet premierBillet = billets.get(0);

            // CORRECTION : Appel direct à la méthode. La vérification de nullité se fait ensuite.
            Seance seance = clientService.getSeanceById(premierBillet.getIdSeance());
            Film film = (seance != null) ? clientService.getFilmDetails(seance.getIdFilm()) : null;

            double prixTotal = 0.0;
            for (Billet billet : billets) {
                for (Tarif tarif : tousLesTarifs) {
                    if (tarif.getId() == billet.getIdTarif()) {
                        prixTotal += tarif.getPrix();
                        break;
                    }
                }
            }

            String nomFilm = (film != null) ? film.getTitre() : "N/A";
            String dateSeance = (seance != null) ? seance.getDateHeureDebut().format(formatter) : "N/A";

            String prixAffiche = formaterPrix(prixTotal);

            Object[] rowData = {
                reservation.getId(),
                reservation.getDateReservation().format(formatter),
                nomFilm,
                dateSeance,
                billets.size(),
                prixAffiche
            };
            tableModel.addRow(rowData);
        }
    }

    private void gererAnnulerReservation() {
        int selectedRow = historiqueTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation à annuler.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int reservationId = (int) tableModel.getValueAt(selectedRow, 0);
        int response = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir annuler la réservation N°" + reservationId + " ?",
                "Confirmation d'annulation",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            try {
                clientService.annulerReservation(reservationId);
                JOptionPane.showMessageDialog(this, "Réservation annulée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadHistorique();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'annulation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        historiqueTable = new javax.swing.JTable();
        annulerButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new java.awt.BorderLayout());

        historiqueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(historiqueTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        annulerButton.setText("Annuler la réservation sélectionnée");
        annulerButton.setEnabled(false);
        annulerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerButtonActionPerformed(evt);
            }
        });
        add(annulerButton, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void annulerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerButtonActionPerformed
        gererAnnulerReservation(); // TODO add your handling code here:
    }//GEN-LAST:event_annulerButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton annulerButton;
    private javax.swing.JTable historiqueTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
