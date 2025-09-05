package com.mycompany.cinema.view;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Genre;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;
import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Programmation extends javax.swing.JPanel {

    private final ClientService clientService;
    private SeanceSelectionListener selectionListener;

    public interface SeanceSelectionListener {

        void onSeanceSelected(Seance seance);
    }

    public Programmation(ClientService clientService) {
        this.clientService = clientService;

        initComponents();

        // --- LIAISON MANUELLE DE L'ÉVÉNEMENT TABLE ---
        seancesTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                seancesTableValueChanged(evt);
            }
        });

        loadGenres();
        rechercher();
    }

    private void loadGenres() {
        genreFilter.addItem(null);
        List<Genre> genres = clientService.getAllGenres();
        for (Genre g : genres) {
            genreFilter.addItem(g);
        }
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
    }

    public void rechercher() {
        LocalDate date = null;
        if (!dateFilter.getText().trim().isEmpty()) {
            try {
                date = LocalDate.parse(dateFilter.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez jj/MM/yyyy.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        String keyword = titreFilter.getText();
        Genre genre = (Genre) genreFilter.getSelectedItem();
        Integer genreId = (genre != null) ? genre.getId() : null;

        List<Seance> seances = clientService.rechercherSeances(date, genreId, keyword);

        SeanceTableModel newModel = new SeanceTableModel(seances, clientService);
        seancesTable.setModel(newModel);
    }

    // --- CLASSE DE MODÈLE DE TABLE (INDISPENSABLE) ---
    class SeanceTableModel extends DefaultTableModel {

        private final transient List<Seance> seances;
        private final transient ClientService clientService;
        private final String[] columnNames = {"Film", "Date", "Heure", "Salle", "Durée (min)"};
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

        public SeanceTableModel(List<Seance> seances, ClientService clientService) {
            this.seances = seances;
            this.clientService = clientService;
        }

        @Override
        public int getRowCount() {
            return (seances != null) ? seances.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Seance seance = seances.get(row);
            Film film = clientService.getFilmDetails(seance.getIdFilm());
            switch (col) {
                case 0:
                    return (film != null) ? film.getTitre() : "Film inconnu";
                case 1:
                    return seance.getDateHeureDebut().format(DATE_FORMATTER);
                case 2:
                    return seance.getDateHeureDebut().format(TIME_FORMATTER);
                case 3:
                    return "Salle " + seance.getIdSalle();
                case 4:
                    return (film != null) ? film.getDureeMinutes() : "N/A";
                default:
                    return null;
            }
        }

        public Seance getSeanceAt(int row) {
            return seances.get(row);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
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

        seancesTable.setModel(new javax.swing.table.DefaultTableModel(
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
        seancesTable.setFillsViewportHeight(true);
        seancesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(seancesTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void rechercheButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rechercheButtonActionPerformed
        rechercher();// TODO add your handling code here:
    }//GEN-LAST:event_rechercheButtonActionPerformed

    private void reinitialisationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reinitialisationButtonActionPerformed
        dateFilter.setText("");
        titreFilter.setText("");
        genreFilter.setSelectedIndex(0);
        rechercher();// TODO add your handling code here:
    }//GEN-LAST:event_reinitialisationButtonActionPerformed

    private void seancesTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting() && seancesTable.getSelectedRow() != -1) {
            if (selectionListener != null) {
                int modelRow = seancesTable.convertRowIndexToModel(seancesTable.getSelectedRow());
                // Important: On caste le modèle pour accéder à notre méthode personnalisée
                Seance selectedSeance = ((SeanceTableModel) seancesTable.getModel()).getSeanceAt(modelRow);
                selectionListener.onSeanceSelected(selectedSeance);
            }
        }
    }
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
