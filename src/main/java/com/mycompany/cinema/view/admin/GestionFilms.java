package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Genre;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

public class GestionFilms extends javax.swing.JPanel {

    private final AdminService adminService;
    private Film filmSelectionne;
    private DefaultListModel<Film> filmListModel;
    private DefaultListModel<Genre> genreListModel;

    public GestionFilms(AdminService adminService) {
        this.adminService = adminService;
        initComponents();
        filmListModel = new DefaultListModel<>();
        filmJList.setModel(filmListModel);
        configureFilmListRenderer();
        loadFilms();
        genreListModel = new DefaultListModel<>();
        genreJList.setModel(genreListModel);
        genreJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        configureGenreListRenderer();
        loadAllGenres();
    }

    private void configureFilmListRenderer() {
        filmJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Film) {
                    setText(((Film) value).getTitre());
                }
                return c;
            }
        });
    }

    private void configureGenreListRenderer() {
        genreJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Genre) {
                    setText(((Genre) value).getLibelle());
                }
                return c;
            }
        });
    }

    private void loadFilms() {
        filmListModel.clear();
        try {
            List<Film> films = adminService.getFilmsAffiche();
            for (Film film : films) {
                filmListModel.addElement(film);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des films : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllGenres() {
        genreListModel.clear();
        try {
            List<Genre> genres = adminService.getAllGenres();
            for (Genre genre : genres) {
                genreListModel.addElement(genre);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des genres : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void rafraichirDonnees() {
        loadFilms();
        loadAllGenres();
    }

    // MODIFICATION : Affichage de la note de presse
    private void displayFilmDetails(Film film) {
        if (film != null) {
            titreField.setText(film.getTitre());
            synopsisField.setText(film.getSynopsis());
            dureeField.setText(String.valueOf(film.getDureeMinutes()));
            classificationField.setText(film.getClassification());
            urlAfficheField.setText(film.getUrlAffiche());
            // AJOUT : Afficher la note de presse avec un formatage propre
            notePresseField.setText(String.format("%.1f", film.getNotePresse()).replace(',', '.'));
            supprimerButton.setEnabled(true);
            selectFilmGenres(film);
        } else {
            clearForm();
        }
    }

    private void selectFilmGenres(Film film) {
        genreJList.clearSelection();
        List<Genre> filmGenres = film.getGenres();
        if (filmGenres == null || filmGenres.isEmpty()) {
            return;
        }
        List<Integer> indicesToSelect = new ArrayList<>();
        for (int i = 0; i < genreListModel.size(); i++) {
            Genre genreFromList = genreListModel.getElementAt(i);
            for (Genre filmGenre : filmGenres) {
                if (genreFromList.getId() == filmGenre.getId()) {
                    indicesToSelect.add(i);
                    break;
                }
            }
        }
        int[] indices = indicesToSelect.stream().mapToInt(i -> i).toArray();
        genreJList.setSelectedIndices(indices);
    }

    // MODIFICATION : Effacer le champ de la note
    private void clearForm() {
        filmJList.clearSelection();
        filmSelectionne = null;
        titreField.setText("");
        synopsisField.setText("");
        dureeField.setText("");
        classificationField.setText("");
        urlAfficheField.setText("");
        notePresseField.setText(""); // AJOUT : Effacer le champ de la note
        genreJList.clearSelection();
        supprimerButton.setEnabled(false);
    }

    // MODIFICATION : Sauvegarde de la note de presse
    private void actionEnregistrer() {
        try {
            // Validation simple des champs numériques
            if (dureeField.getText().trim().isEmpty() || notePresseField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La durée et la note de presse sont requises.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Conversion de la note en double (gestion du point et de la virgule)
            String noteTexte = notePresseField.getText().trim().replace(',', '.');
            double notePresse = Double.parseDouble(noteTexte);

            List<Genre> genresSelectionnes = genreJList.getSelectedValuesList();

            if (filmSelectionne == null) { // Mode Création
                int nouvelId = com.mycompany.cinema.util.IdManager.getNextFilmId();
                Film nouveauFilm = new Film(
                        nouvelId,
                        titreField.getText(),
                        synopsisField.getText(),
                        Integer.parseInt(dureeField.getText()),
                        classificationField.getText(),
                        urlAfficheField.getText(),
                        notePresse // MODIFICATION : Utilisation de la note saisie
                );
                nouveauFilm.setGenres(genresSelectionnes);
                adminService.ajouterFilm(nouveauFilm);
                JOptionPane.showMessageDialog(this, "Film ajouté avec succès !");

            } else { // Mode Mise à jour
                filmSelectionne.setTitre(titreField.getText());
                filmSelectionne.setSynopsis(synopsisField.getText());
                filmSelectionne.setDureeMinutes(Integer.parseInt(dureeField.getText()));
                filmSelectionne.setClassification(classificationField.getText());
                filmSelectionne.setUrlAffiche(urlAfficheField.getText());
                filmSelectionne.setNotePresse(notePresse); // AJOUT : Mise à jour de la note
                filmSelectionne.setGenres(genresSelectionnes);

                adminService.mettreAJourFilm(filmSelectionne);
                JOptionPane.showMessageDialog(this, "Film mis à jour avec succès !");
            }
            loadFilms();
            clearForm();
        } catch (NumberFormatException nfe) {
            // Message d'erreur amélioré pour couvrir les deux champs
            JOptionPane.showMessageDialog(this, "La durée (entier) et la note de presse (nombre) doivent être des nombres valides.", "Erreur de format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionSupprimer() {
        if (filmSelectionne == null) {
            return;
        }
        int response = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce film ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerFilm(filmSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Film supprimé avec succès !");
                loadFilms();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
        filmJList = new javax.swing.JList<>();
        actionPanel = new javax.swing.JPanel();
        nouveauButton = new javax.swing.JButton();
        enregistrerButton = new javax.swing.JButton();
        supprimerButton = new javax.swing.JButton();
        detailsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        titreField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        synopsisField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dureeField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        classificationField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        genreJList = new javax.swing.JList<>();
        jLabel7 = new javax.swing.JLabel();
        notePresseField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        urlAfficheField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 100));

        filmJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                filmJListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(filmJList);

        add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        nouveauButton.setText("Nouveau");
        nouveauButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauButtonActionPerformed(evt);
            }
        });
        actionPanel.add(nouveauButton);

        enregistrerButton.setText("Enregistrer");
        enregistrerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerButtonActionPerformed(evt);
            }
        });
        actionPanel.add(enregistrerButton);

        supprimerButton.setText("Supprimer");
        supprimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerButtonActionPerformed(evt);
            }
        });
        actionPanel.add(supprimerButton);

        add(actionPanel, java.awt.BorderLayout.PAGE_END);

        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Détails du film"));
        detailsPanel.setLayout(new java.awt.GridLayout(0, 2, 5, 5));

        jLabel1.setText("Titre : ");
        detailsPanel.add(jLabel1);
        detailsPanel.add(titreField);

        jLabel2.setText("Synopsis : ");
        detailsPanel.add(jLabel2);
        detailsPanel.add(synopsisField);

        jLabel3.setText("Durée : ");
        detailsPanel.add(jLabel3);
        detailsPanel.add(dureeField);

        jLabel4.setText("Classification : ");
        detailsPanel.add(jLabel4);
        detailsPanel.add(classificationField);

        jLabel6.setText("Genre : ");
        detailsPanel.add(jLabel6);

        genreJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                genreJListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(genreJList);

        detailsPanel.add(jScrollPane2);

        jLabel7.setText("Note de la presse : ");
        detailsPanel.add(jLabel7);
        detailsPanel.add(notePresseField);

        jLabel5.setText("Url affiche :");
        detailsPanel.add(jLabel5);
        detailsPanel.add(urlAfficheField);

        add(detailsPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void nouveauButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauButtonActionPerformed
        clearForm();
// TODO add your handling code here:
    }//GEN-LAST:event_nouveauButtonActionPerformed

    private void enregistrerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enregistrerButtonActionPerformed
        actionEnregistrer();
// TODO add your handling code here:
    }//GEN-LAST:event_enregistrerButtonActionPerformed

    private void supprimerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerButtonActionPerformed
        actionSupprimer();
// TODO add your handling code here:
    }//GEN-LAST:event_supprimerButtonActionPerformed

    private void filmJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_filmJListValueChanged
        if (!evt.getValueIsAdjusting()) {
            filmSelectionne = filmJList.getSelectedValue();
            displayFilmDetails(filmSelectionne);
        }// TODO add your handling code here:
    }//GEN-LAST:event_filmJListValueChanged

    private void genreJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_genreJListValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_genreJListValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JTextField classificationField;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JTextField dureeField;
    private javax.swing.JButton enregistrerButton;
    private javax.swing.JList<com.mycompany.cinema.Film> filmJList;
    private javax.swing.JList<com.mycompany.cinema.Genre> genreJList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField notePresseField;
    private javax.swing.JButton nouveauButton;
    private javax.swing.JButton supprimerButton;
    private javax.swing.JTextField synopsisField;
    private javax.swing.JTextField titreField;
    private javax.swing.JTextField urlAfficheField;
    // End of variables declaration//GEN-END:variables

    // Dans la classe GestionFilms
}
