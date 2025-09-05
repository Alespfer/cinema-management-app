// Dans Programmation.java
package com.mycompany.cinema.view;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Genre;
import com.mycompany.cinema.Salle;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;
import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
// Ajoute cet import pour l'événement
import javax.swing.event.ListSelectionEvent;

public class Programmation extends javax.swing.JPanel {

    private final ClientService clientService;
    private DefaultTableModel tableModel;
    private List<Seance> seancesAffichees;
    private DefaultComboBoxModel<Genre> genreComboBoxModel;
    private SeanceSelectionListener selectionListener;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public interface SeanceSelectionListener {
        void onSeanceSelected(Seance seance);
    }

    public Programmation(ClientService clientService) {
        this.clientService = clientService;
        initComponents();
        initCustomComponents();
        rechercher(); 
    }

    private void initCustomComponents() {
        String[] columnNames = {"Film", "Date", "Heure", "Salle", "Durée (min)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        seancesTable.setModel(tableModel);

        genreComboBoxModel = new DefaultComboBoxModel<>();
        genreFilter.setModel(genreComboBoxModel);
        genreFilter.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Genre) {
                    setText(((Genre) value).getLibelle());
                } else {
                    setText("Tous les genres");
                }
                return this;
            }
        });
        chargerGenres();
    }

    private void chargerGenres() {
        genreComboBoxModel.removeAllElements();
        genreComboBoxModel.addElement(null);
        List<Genre> genres = clientService.getAllGenres();
        for (Genre genre : genres) {
            genreComboBoxModel.addElement(genre);
        }
    }

    public void rechercher() {
        LocalDate date = null;
        try {
            if (!dateFilter.getText().trim().isEmpty()) {
                date = LocalDate.parse(dateFilter.getText().trim(), DATE_FORMATTER);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez jj/MM/aaaa.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String titreKeyword = titreFilter.getText().trim();
        Genre genreSelectionne = (Genre) genreFilter.getSelectedItem();
        Integer genreId = (genreSelectionne != null) ? genreSelectionne.getId() : null;
        seancesAffichees = clientService.rechercherSeances(date, genreId, titreKeyword);
        mettreAJourTable();
    }

    private void reinitialiserFiltres() {
        dateFilter.setText("");
        titreFilter.setText("");
        genreFilter.setSelectedIndex(0);
        rechercher();
    }

    private void mettreAJourTable() {
        tableModel.setRowCount(0);
        for (Seance seance : seancesAffichees) {
            Film film = clientService.getFilmDetails(seance.getIdFilm());
            Salle salle = null;
            for (Salle s : clientService.getAllSalles()) {
                if (s.getId() == seance.getIdSalle()) {
                    salle = s;
                    break;
                }
            }
            String titreFilm = (film != null) ? film.getTitre() : "Film Inconnu";
            String numeroSalle = (salle != null) ? salle.getNumero() : "Salle Inconnue";
            int dureeFilm = (film != null) ? film.getDureeMinutes() : 0;
            String dateSeance = seance.getDateHeureDebut().format(DATE_FORMATTER);
            String heureSeance = seance.getDateHeureDebut().format(TIME_FORMATTER);
            tableModel.addRow(new Object[]{ titreFilm, dateSeance, heureSeance, numeroSalle, dureeFilm });
        }
    }

    public void setSeanceSelectionListener(SeanceSelectionListener listener) {
        this.selectionListener = listener;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dateFilter = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        titreFilter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        genreFilter = new javax.swing.JComboBox<>();
        rechercheButton = new javax.swing.JButton();
        reinitialisationButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        seancesTable = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new java.awt.BorderLayout());

        filterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtres"));
        filterPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        jLabel1.setText("Date (jj/MM/aaaa) : ");
        filterPanel.add(jLabel1);

        dateFilter.setColumns(10);
        filterPanel.add(dateFilter);

        jLabel2.setText("Titre : ");
        filterPanel.add(jLabel2);

        titreFilter.setColumns(20);
        filterPanel.add(titreFilter);

        jLabel3.setText("Genre : ");
        filterPanel.add(jLabel3);

        filterPanel.add(genreFilter);

        rechercheButton.setText("Rechercher");
        rechercheButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rechercheButtonActionPerformed(evt);
            }
        });
        filterPanel.add(rechercheButton);

        reinitialisationButton.setText("Réinitialisation");
        reinitialisationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reinitialisationButtonActionPerformed(evt);
            }
        });
        filterPanel.add(reinitialisationButton);

        add(filterPanel, java.awt.BorderLayout.PAGE_START);

        seancesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seancesTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(seancesTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void rechercheButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rechercheButtonActionPerformed
        rechercher();// TODO add your handling code here:
    }//GEN-LAST:event_rechercheButtonActionPerformed

    private void reinitialisationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reinitialisationButtonActionPerformed
        reinitialiserFiltres();
// TODO add your handling code here:
    }//GEN-LAST:event_reinitialisationButtonActionPerformed

    private void seancesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_seancesTableMouseClicked
        if (selectionListener != null) {
            int selectedRow = seancesTable.getSelectedRow();
            if (selectedRow >= 0) {
                Seance selectedSeance = seancesAffichees.get(selectedRow);
                System.out.println("DAN REPORT (Clic détecté): Programmation Panel a détecté la sélection de la séance ID: " + selectedSeance.getId());
                selectionListener.onSeanceSelected(selectedSeance);
            }
        }// TODO add your handling code here:
    }//GEN-LAST:event_seancesTableMouseClicked

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField dateFilter;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JComboBox<com.mycompany.cinema.Genre> genreFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton rechercheButton;
    private javax.swing.JButton reinitialisationButton;
    private javax.swing.JTable seancesTable;
    private javax.swing.JTextField titreFilter;
    // End of variables declaration//GEN-END:variables
}
