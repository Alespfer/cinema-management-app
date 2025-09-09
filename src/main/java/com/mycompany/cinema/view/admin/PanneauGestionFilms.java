/*
 * PanneauGestionFilms.java
 *  Panneau pour la gestion complète des films.
 * Affiche la liste des films et un formulaire détaillé pour éditer le film sélectionné.
 */
package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Genre;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PanneauGestionFilms extends javax.swing.JPanel {

    private final AdminService adminService;
    private Film filmSelectionne; // L'objet Film actuellement en cours d'édition.
    private DefaultListModel<Film> filmListModel;
    private DefaultListModel<Genre> genreListModel;
    private File fichierAfficheSelectionne; // Stocke temporairement le fichier image choisi par l'utilisateur.

    public PanneauGestionFilms(AdminService adminService) {
        this.adminService = adminService;
        initComponents();
        filmListModel = new DefaultListModel<>();
        filmJList.setModel(filmListModel);
        configureFilmListRenderer();
        chargerFilms();
        genreListModel = new DefaultListModel<>();
        genreJList.setModel(genreListModel);
        genreJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        configureGenreListRenderer();
        chargerTousLesGenres();

        supprimerButton.setEnabled(false);

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

    /**
     * Charge ou recharge la liste des films depuis le service.
     */
    private void chargerFilms() {
        filmListModel.clear();
        try {
            List<Film> films = adminService.trouverFilmsAffiche();
            for (Film film : films) {
                filmListModel.addElement(film);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des films : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Charge ou recharge la liste de tous les genres disponibles.
     */
    private void chargerTousLesGenres() {
        genreListModel.clear();
        try {
            List<Genre> genres = adminService.trouverTousLesGenres();
            for (Genre genre : genres) {
                genreListModel.addElement(genre);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des genres : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void rafraichirDonnees() {
        chargerFilms();
        chargerTousLesGenres();
    }

    /**
     * Affiche les détails d'un film sélectionné dans le formulaire.
     */
    private void afficherDetailsFilm(Film film) {
        if (film != null) {
            titreField.setText(film.getTitre());
            synopsisField.setText(film.getSynopsis());
            dureeField.setText(String.valueOf(film.getDureeMinutes()));
            classificationField.setText(film.getClassification());

            nomFichierAfficheLabel.setText(film.getUrlAffiche());

            notePresseField.setText(String.format("%.1f", film.getNotePresse()).replace(',', '.'));

            selectionnerGenresFilm(film);
        } else {
            reinitialiserFormulaire();
        }
    }

    /**
     * Sélectionne dans la JList les genres qui correspondent à ceux du film
     * passé en paramètre.
     *
     * @param film Le film à analyser.
     */
    private void selectionnerGenresFilm(Film film) {

        genreJList.clearSelection();

        List<Genre> genresDuFilm = film.getGenres();
        if (genresDuFilm == null || genresDuFilm.isEmpty()) {
            return;
        }

        // 1. On trouve les indices des genres du film dans le modèle de la JList.
        List<Integer> indicesASelectionner = new ArrayList<>();
        for (int i = 0; i < genreListModel.size(); i++) {
            Genre genreDeLaListe = genreListModel.getElementAt(i);
            for (Genre genreDuFilm : genresDuFilm) {
                if (genreDeLaListe.getId() == genreDuFilm.getId()) {
                    indicesASelectionner.add(i);
                    break;
                }
            }
        }

        // 2. On convertit la liste d'Integer en tableau de int[] pour la méthode de la JList.
        int[] indices = new int[indicesASelectionner.size()];
        for (int i = 0; i < indicesASelectionner.size(); i++) {
            indices[i] = indicesASelectionner.get(i);
        }

        // 3. On applique la sélection à la JList.
        genreJList.setSelectedIndices(indices);
    }

    /**
     * Vide tous les champs du formulaire et réinitialise l'état du panneau.
     */
    private void reinitialiserFormulaire() {
        filmJList.clearSelection();
        filmSelectionne = null;
        titreField.setText("");
        synopsisField.setText("");
        dureeField.setText("");
        classificationField.setText("");

        nomFichierAfficheLabel.setText("Aucun fichier sélectionné");
        fichierAfficheSelectionne = null;

        notePresseField.setText("");
        genreJList.clearSelection();

        supprimerButton.setEnabled(false);
    }

   /**
     * Gère la logique de sauvegarde (création ou modification d'un film).
     */
    private void actionEnregistrer() {
        try {
            // --- VALIDATIONS ---
            if (titreField.getText().trim().isEmpty() || dureeField.getText().trim().isEmpty() || notePresseField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le titre, la durée et la note de presse sont requis.", "Champs obligatoires", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nomFichierAffiche;
            
            // Si un nouveau fichier a été choisi, on le copie vers le dossier "images".
            if (fichierAfficheSelectionne != null) {
                nomFichierAffiche = copierImageEtRetournerNom(fichierAfficheSelectionne);
                if (nomFichierAffiche == null) {
                    return; 
                }
            } else if (filmSelectionne != null) {
                nomFichierAffiche = filmSelectionne.getUrlAffiche();
            } else {
                // Si on crée un nouveau film, l'affiche est obligatoire.
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une affiche pour le nouveau film.", "Affiche requise", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // --- Le reste des validations ---
            String noteTexte = notePresseField.getText().trim().replace(',', '.');
            double notePresse = Double.parseDouble(noteTexte);
            int duree = Integer.parseInt(dureeField.getText().trim());
            List<Genre> genresSelectionnes = genreJList.getSelectedValuesList();

            if (genresSelectionnes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un genre.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // --- Sauvegarde ---
            if (filmSelectionne == null) { // Mode Création
                int nouvelId = com.mycompany.cinema.util.IdManager.obtenirProchainIdFilm();
                Film nouveauFilm = new Film(nouvelId, titreField.getText().trim(), synopsisField.getText().trim(), duree, classificationField.getText().trim(), nomFichierAffiche, notePresse);
                nouveauFilm.setGenres(genresSelectionnes);
                adminService.ajouterFilm(nouveauFilm);
                JOptionPane.showMessageDialog(this, "Film ajouté avec succès !");
            } else { // Mode Mise à jour
                filmSelectionne.setTitre(titreField.getText().trim());
                filmSelectionne.setSynopsis(synopsisField.getText().trim());
                filmSelectionne.setDureeMinutes(duree);
                filmSelectionne.setClassification(classificationField.getText().trim());
                filmSelectionne.setUrlAffiche(nomFichierAffiche);
                filmSelectionne.setNotePresse(notePresse);
                filmSelectionne.setGenres(genresSelectionnes);
                adminService.mettreAJourFilm(filmSelectionne);
                JOptionPane.showMessageDialog(this, "Film mis à jour avec succès !");
            }

            rafraichirDonnees();
            reinitialiserFormulaire();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "La durée et la note doivent être des nombres valides.", "Erreur de format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionSupprimer() {
        if (filmSelectionne == null) {
            return;
        }

        int response = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer le film '" + filmSelectionne.getTitre() + "' ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerFilm(filmSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Film supprimé avec succès !");
                rafraichirDonnees();
                reinitialiserFormulaire();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur de suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void parcourirAfficheButtonMouseClicked(java.awt.event.MouseEvent evt) {

    }

    private String copierImageEtRetournerNom(File sourceFile) {
        if (sourceFile == null) {
            return null;
        }
        try {
            File repertoireImages = new File("images");
            if (!repertoireImages.exists()) {
                repertoireImages.mkdirs();
            }
            File destination = new File(repertoireImages, sourceFile.getName());
            Files.copy(sourceFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return sourceFile.getName();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la copie de l'image : " + e.getMessage(), "Erreur de Fichier", JOptionPane.ERROR_MESSAGE);
            return null;
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

        jScrollBar1 = new javax.swing.JScrollBar();
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
        jPanel1 = new javax.swing.JPanel();
        parcourirAfficheButton = new javax.swing.JButton();
        nomFichierAfficheLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 100));
        jScrollPane1.setSize(new java.awt.Dimension(300, 0));

        filmJList.setSize(new java.awt.Dimension(300, 0));
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
        supprimerButton.setEnabled(false);
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

        jLabel5.setText("Affiche :");
        detailsPanel.add(jLabel5);

        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        parcourirAfficheButton.setText("Parcourir...");
        parcourirAfficheButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parcourirAfficheButtonActionPerformed(evt);
            }
        });
        jPanel1.add(parcourirAfficheButton);

        nomFichierAfficheLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nomFichierAfficheLabel.setText("Aucun fichier sélectionné");
        jPanel1.add(nomFichierAfficheLabel);

        detailsPanel.add(jPanel1);

        add(detailsPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void nouveauButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauButtonActionPerformed
        reinitialiserFormulaire();
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
            afficherDetailsFilm(filmSelectionne);
        }// TODO add your handling code here:
    }//GEN-LAST:event_filmJListValueChanged

    private void genreJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_genreJListValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_genreJListValueChanged

    private void parcourirAfficheButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parcourirAfficheButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg", "png", "gif");
        chooser.setFileFilter(filter);

        int resultat = chooser.showOpenDialog(this);

        if (resultat == JFileChooser.APPROVE_OPTION) {
            this.fichierAfficheSelectionne = chooser.getSelectedFile();
            nomFichierAfficheLabel.setText(fichierAfficheSelectionne.getName());
        }// TODO add your handling code here:
    }//GEN-LAST:event_parcourirAfficheButtonActionPerformed


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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel nomFichierAfficheLabel;
    private javax.swing.JTextField notePresseField;
    private javax.swing.JButton nouveauButton;
    private javax.swing.JButton parcourirAfficheButton;
    private javax.swing.JButton supprimerButton;
    private javax.swing.JTextField synopsisField;
    private javax.swing.JTextField titreField;
    // End of variables declaration//GEN-END:variables

}
