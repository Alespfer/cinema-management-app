package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.*;
import com.mycompany.cinema.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Panneau de reporting pour l'administrateur.
 * Affiche les chiffres d'affaires pour une date donnée et liste
 * le détail des réservations de billets et des ventes de snacks.
 */
public class ReportingPanel extends JPanel {

    private final AdminService adminService;

    // --- Composants de l'interface ---
    private JTextField dateField;
    private JButton calcCaJourButton;
    private JLabel resultatCaBilletsLabel;
    private JLabel resultatCaSnacksLabel;

    private JTable reservationsTable;
    private DefaultTableModel reservationsTableModel;
    private JTable ventesSnackTable;
    private DefaultTableModel ventesSnackTableModel;

    // Formateurs pour un affichage propre des nombres et des dates
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,##0.00 €");

    public ReportingPanel(AdminService adminService) {
        this.adminService = adminService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        initListeners();
        
        loadAllTables();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateField = new JTextField(10);
        calcCaJourButton = new JButton("Calculer pour la date");
        
        filterPanel.add(new JLabel("Date (jj/MM/aaaa):"));
        filterPanel.add(dateField);
        filterPanel.add(calcCaJourButton);

        JPanel summaryPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Chiffre d'Affaires du Jour"));
        resultatCaBilletsLabel = new JLabel("Billets : 0,00 €");
        resultatCaBilletsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultatCaSnacksLabel = new JLabel("Snacks : 0,00 €");
        resultatCaSnacksLabel.setFont(new Font("Arial", Font.BOLD, 14));
        summaryPanel.add(resultatCaBilletsLabel);
        summaryPanel.add(resultatCaSnacksLabel);

        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(summaryPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        String[] reservationsColumns = {"ID Res.", "Client", "Film", "Séance", "Prix Total"};
        reservationsTableModel = new DefaultTableModel(reservationsColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        reservationsTable = new JTable(reservationsTableModel);
        tabbedPane.addTab("Détail des Réservations", new JScrollPane(reservationsTable));

        String[] ventesSnackColumns = {"ID Vente", "Date", "Personnel", "Total Vente"};
        ventesSnackTableModel = new DefaultTableModel(ventesSnackColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        ventesSnackTable = new JTable(ventesSnackTableModel);
        tabbedPane.addTab("Détail des Ventes de Snacks", new JScrollPane(ventesSnackTable));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void initListeners() {
        calcCaJourButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCalculer();
            }
        });
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

    private void loadReservationsTable() {
        reservationsTableModel.setRowCount(0);
        List<Reservation> reservations = adminService.getAllReservations();
        
        for (Reservation reservation : reservations) {
            Client client = null;
            Optional<Client> clientOpt = adminService.getClientById(reservation.getIdClient());
            if (clientOpt.isPresent()) {
                client = clientOpt.get();
            }

            List<Billet> billets = adminService.getBilletsByReservationId(reservation.getId());
            if (billets.isEmpty()) continue;
            
            Billet premierBillet = billets.get(0);
            
            Seance seance = null;
            Optional<Seance> seanceOpt = adminService.getSeanceById(premierBillet.getIdSeance());
            if (seanceOpt.isPresent()){
                seance = seanceOpt.get();
            }
            
            Film film = (seance != null) ? adminService.getFilmDetails(seance.getIdFilm()) : null;

            double prixTotal = 0;
            for (Billet b : billets) {
                for (Tarif t : adminService.getAllTarifs()) {
                    if (t.getId() == b.getIdTarif()) {
                        prixTotal += t.getPrix();
                        break;
                    }
                }
            }

            Object[] rowData = {
                reservation.getId(),
                client != null ? client.getNom() : "Client inconnu",
                film != null ? film.getTitre() : "Film inconnu",
                seance != null ? seance.getDateHeureDebut().format(DATETIME_FORMATTER) : "Date inconnue",
                CURRENCY_FORMATTER.format(prixTotal)
            };
            reservationsTableModel.addRow(rowData);
        }
    }

    private void loadVentesSnackTable() {
        ventesSnackTableModel.setRowCount(0);
        List<VenteSnack> ventes = adminService.getAllVentesSnack();

        for (VenteSnack vente : ventes) {
            Personnel personnel = null;
            for (Personnel p : adminService.getAllPersonnel()) {
                if (p.getId() == vente.getIdPersonnel()) {
                    personnel = p;
                    break;
                }
            }
            
            double totalVente = adminService.calculerTotalPourVenteSnack(vente);

            Object[] rowData = {
                vente.getIdVente(),
                vente.getDateVente().format(DATETIME_FORMATTER),
                personnel != null ? personnel.getPrenom() + " " + personnel.getNom() : "Personnel inconnu",
                CURRENCY_FORMATTER.format(totalVente)
            };
            ventesSnackTableModel.addRow(rowData);
        }
    }
}