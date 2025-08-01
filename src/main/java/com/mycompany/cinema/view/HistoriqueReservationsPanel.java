package com.mycompany.cinema.view;

import com.mycompany.cinema.*;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panneau affichant l'historique des réservations d'un client dans un tableau.
 * Permet également d'annuler une réservation sélectionnée.
 */
public class HistoriqueReservationsPanel extends JPanel {
    
    private final ClientService clientService;
    private final Client clientConnecte;

    private JTable historiqueTable;
    private DefaultTableModel tableModel;
    private JButton annulerButton;

    public HistoriqueReservationsPanel(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialisation des composants.
        String[] columnNames = {"ID Res.", "Date Réservation", "Film", "Séance", "Nb. Billets", "Prix Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table non éditable.
            }
        };
        historiqueTable = new JTable(tableModel);
        add(new JScrollPane(historiqueTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        annulerButton = new JButton("Annuler la réservation sélectionnée");
        annulerButton.setEnabled(false); // Désactivé par défaut.
        bottomPanel.add(annulerButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        historiqueTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                // Le bouton n'est activé que si une ligne est sélectionnée.
                annulerButton.setEnabled(historiqueTable.getSelectedRow() != -1);
            }
        });
        
        annulerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleAnnulerReservation();
            }
        });

        // Chargement des données initiales.
        loadHistorique();
    }

    /**
     * Charge l'historique des réservations et peuple le tableau.
     */
    public void loadHistorique() {
        tableModel.setRowCount(0); // Vide le tableau avant de le remplir.
        List<Reservation> reservations = clientService.getHistoriqueReservationsClient(clientConnecte.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DecimalFormat df = new DecimalFormat("#,##0.00 €");

        for (Reservation reservation : reservations) {
            // Pour chaque réservation, nous devons récupérer des informations supplémentaires.
            List<Billet> billets = clientService.getBilletsPourSeance(reservation.getId());
            if (billets.isEmpty()) continue; // Ignore les réservations sans billets (cas anormal).
            
            // On se base sur le premier billet pour obtenir les infos de la séance et du film.
            Billet premierBillet = billets.get(0);
            Seance seance = clientService.findSeancesFiltrees(null, null, null).stream().filter(s -> s.getId() == premierBillet.getIdSeance()).findFirst().orElse(null);
            Film film = (seance != null) ? clientService.getFilmDetails(seance.getIdFilm()) : null;

            // Calcul du prix total de la réservation.
            double prixTotal = 0.0;
            for(Billet b : billets){
                prixTotal += clientService.getAllTarifs().stream().filter(t -> t.getId() == b.getIdTarif()).findFirst().get().getPrix();
            }


            // Création de la ligne du tableau.
            Object[] rowData = {
                reservation.getId(),
                reservation.getDateReservation().format(formatter),
                (film != null) ? film.getTitre() : "N/A",
                (seance != null) ? seance.getDateHeureDebut().format(formatter) : "N/A",
                billets.size(),
                df.format(prixTotal)
            };
            tableModel.addRow(rowData);
        }
    }

    private void handleAnnulerReservation() {
        int selectedRow = historiqueTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation à annuler.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Récupération de l'ID de la réservation depuis la première colonne du tableau.
        int reservationId = (int) tableModel.getValueAt(selectedRow, 0);

        int response = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir annuler la réservation N°" + reservationId + " ?",
                "Confirmation d'annulation",
                JOptionPane.YES_NO_OPTION);
                
        if (response == JOptionPane.YES_OPTION) {
            try {
                clientService.annulerReservation(reservationId);
                JOptionPane.showMessageDialog(this, "Réservation annulée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadHistorique(); // Recharger le tableau pour voir les changements.
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'annulation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}