package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.*;
import com.mycompany.cinema.service.AdminService;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class RapportVentes extends javax.swing.JPanel {

    private final AdminService adminService;
    private DefaultTableModel reservationsTableModel;
    private DefaultTableModel ventesSnackTableModel;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,##0.00 €");

    public RapportVentes(AdminService adminService) {
        this.adminService = adminService;

        initComponents();
        initModels();

        loadAllTables();
    }

    private void initModels() {
        String[] reservationsColumns = {"ID Res.", "Client", "Film", "Séance", "Prix Total"};
        reservationsTableModel = new DefaultTableModel(reservationsColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationsTable.setModel(reservationsTableModel);

        String[] ventesSnackColumns = {"ID Vente", "Date", "Canal de Vente", "Vendeur", "Client Associé", "Total Vente"};
        ventesSnackTableModel = new DefaultTableModel(ventesSnackColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ventesSnackTable.setModel(ventesSnackTableModel);
    }

    private void actionCalculer() {
        String dateText = dateField.getText();
        if (dateText == null || dateText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une date pour le calcul.", "Date manquante", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate date = LocalDate.parse(dateText, DATE_FORMATTER);
            double caBillets = adminService.calculerChiffreAffairesPourJour(date);
            double caSnacks = adminService.calculerChiffreAffairesSnackPourJour(date);
            resultatCaBilletsLabel.setText("Billets : " + CURRENCY_FORMATTER.format(caBillets));
            resultatCaSnacksLabel.setText("Snacks : " + CURRENCY_FORMATTER.format(caSnacks));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez jj/MM/yyyy.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllTables() {
        loadReservationsTable();
        loadVentesSnackTable();
    }

    /**
     * Charge la table des réservations. VERSION CORRIGÉE CONFORME À LA DOCTRINE
     * (SANS STREAM).
     */
    private void loadReservationsTable() {
        reservationsTableModel.setRowCount(0);
        List<Reservation> reservations = adminService.getAllReservations();

        for (Reservation reservation : reservations) {
            Optional<Client> clientOpt = adminService.getClientById(reservation.getIdClient());
            String clientNom = "Client inconnu";
            if (clientOpt.isPresent()) {
                clientNom = clientOpt.get().getNom();
            }

            List<Billet> billets = adminService.getBilletsByReservationId(reservation.getId());
            if (billets.isEmpty()) {
                continue;
            }

            Billet premierBillet = billets.get(0);
            Optional<Seance> seanceOpt = adminService.getSeanceById(premierBillet.getIdSeance());

            String filmTitre = "Film inconnu";
            String seanceDate = "Date inconnue";
            if (seanceOpt.isPresent()) {
                Seance seance = seanceOpt.get();
                seanceDate = seance.getDateHeureDebut().format(DATETIME_FORMATTER);
                Film film = adminService.getFilmDetails(seance.getIdFilm());
                if (film != null) {
                    filmTitre = film.getTitre();
                }
            }

            double prixTotal = 0;
            for (Billet b : billets) {
                final int tarifId = b.getIdTarif();

                // --- DÉBUT DE LA CORRECTION CONFORME À LA DOCTRINE ---
                Optional<Tarif> tarifOpt = Optional.empty();
                // On parcourt la liste des tarifs avec une boucle for classique.
                for (Tarif t : adminService.getAllTarifs()) {
                    if (t.getId() == tarifId) {
                        tarifOpt = Optional.of(t);
                        break; // On a trouvé le tarif, on arrête la boucle.
                    }
                }
                // --- FIN DE LA CORRECTION ---

                if (tarifOpt.isPresent()) {
                    prixTotal += tarifOpt.get().getPrix();
                }
            }

            Object[] rowData = {
                reservation.getId(), clientNom, filmTitre, seanceDate, CURRENCY_FORMATTER.format(prixTotal)
            };
            reservationsTableModel.addRow(rowData);
        }
    }

    private void loadVentesSnackTable() {
        ventesSnackTableModel.setRowCount(0);
        List<VenteSnack> ventes = adminService.getAllVentesSnack();

        for (VenteSnack vente : ventes) {
            String canalDeVente, vendeur, clientAssocie;

            if (vente.getIdPersonnel() >= 0) {
                Optional<Personnel> pOpt = adminService.getPersonnelById(vente.getIdPersonnel());
                vendeur = pOpt.isPresent() ? pOpt.get().getPrenom() + " " + pOpt.get().getNom() : "ID " + vente.getIdPersonnel();

                Optional<Caisse> cOpt = adminService.getCaisseById(vente.getIdCaisse());
                canalDeVente = cOpt.isPresent() ? cOpt.get().getNom() : "Caisse ID " + vente.getIdCaisse();
            } else {
                vendeur = "N/A";
                canalDeVente = "Achat en Ligne";
            }

            if (vente.getIdClient() != null) {
                Optional<Client> cOpt = adminService.getClientById(vente.getIdClient());
                clientAssocie = cOpt.isPresent() ? cOpt.get().getNom() : "Client ID " + vente.getIdClient();
            } else {
                clientAssocie = "Vente Anonyme";
            }

            double totalVente = adminService.calculerTotalPourVenteSnack(vente);

            Object[] rowData = {vente.getIdVente(), vente.getDateVente().format(DATETIME_FORMATTER), canalDeVente, vendeur, clientAssocie, CURRENCY_FORMATTER.format(totalVente)};
            ventesSnackTableModel.addRow(rowData);
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
        filterPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        calcCaJourButton = new javax.swing.JButton();
        summaryPanel = new javax.swing.JPanel();
        resultatCaBilletsLabel = new javax.swing.JLabel();
        resultatCaSnacksLabel = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        reservationsTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ventesSnackTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        topPanel.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Date (jj/MM/aaaa) : ");

        dateField.setColumns(10);

        calcCaJourButton.setText("Calculer pour la date");
        calcCaJourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcCaJourButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(41, 41, 41)
                .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(calcCaJourButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(calcCaJourButton))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        topPanel.add(filterPanel, java.awt.BorderLayout.PAGE_START);

        summaryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Chiffre d'affaires du jour"));
        summaryPanel.setLayout(new java.awt.GridLayout(2, 1));

        resultatCaBilletsLabel.setText("Billets : 0,00 €");
        summaryPanel.add(resultatCaBilletsLabel);

        resultatCaSnacksLabel.setText("Snacks : 0,00 €");
        summaryPanel.add(resultatCaSnacksLabel);

        topPanel.add(summaryPanel, java.awt.BorderLayout.CENTER);

        add(topPanel, java.awt.BorderLayout.PAGE_START);

        reservationsTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(reservationsTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Détail des réservations", jPanel1);

        ventesSnackTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(ventesSnackTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Détail des ventes de snacks", jPanel2);

        add(jTabbedPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void calcCaJourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcCaJourButtonActionPerformed
        actionCalculer();        // TODO add your handling code here:
    }//GEN-LAST:event_calcCaJourButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton calcCaJourButton;
    private javax.swing.JTextField dateField;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable reservationsTable;
    private javax.swing.JLabel resultatCaBilletsLabel;
    private javax.swing.JLabel resultatCaSnacksLabel;
    private javax.swing.JPanel summaryPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTable ventesSnackTable;
    // End of variables declaration//GEN-END:variables
}
