/*
 * PanneauGestionGenres.java
 * Panneau pour la gestion simple des genres de films.
 */
package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Genre;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class PanneauGestionGenres extends javax.swing.JPanel {

    private final AdminService adminService;
    private Genre genreSelectionne;
    private DefaultListModel<Genre> genreListModel;

    public PanneauGestionGenres(AdminService adminService) {
        this.adminService = adminService;

        initComponents();
        genreListModel = new DefaultListModel<>();
        genreJList.setModel(genreListModel);
        configureListRenderer();
        chargerGenres();
    }

    /**
     * Personnalise l'affichage des objets Genre dans la JList. Au lieu
     * d'afficher le résultat de `toString()`, on affiche uniquement le libellé
     * du genre.
     */
    private void configureListRenderer() {
        genreJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Genre) {
                    setText(((Genre) value).getLibelle());
                }
                return c;
            }
        });
    }

    /**
     * Charge la liste complète des genres depuis le service et met à jour le
     * modèle de la JList.
     */
    private void chargerGenres() {
        genreListModel.clear();
        try {
            List<Genre> genres = adminService.trouverTousLesGenres();
            for (Genre g : genres) {
                genreListModel.addElement(g);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des genres.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Méthode appelée par la fenêtre principale pour s'assurer que les
     * données sont à jour lorsque l'onglet devient visible.
     */
    public void rafraichirDonnees() {
        chargerGenres();
    }

    /**
     * Affiche les détails d'un genre dans le formulaire de droite.
     */
    private void afficherDetailsGenre(Genre genre) {
        if (genre != null) {
            idField.setText(String.valueOf(genre.getId()));
            libelleField.setText(genre.getLibelle());
            supprimerButton.setEnabled(true);
        } else {
            reinitialiserFormulaire();
        }
    }

    /**
     * Vide le formulaire. Appelé pour le mode "Nouveau" ou après une
     * suppression.
     */
    private void reinitialiserFormulaire() {
        genreJList.clearSelection();
        genreSelectionne = null;
        idField.setText("");
        libelleField.setText("");
        supprimerButton.setEnabled(false);
    }

    /**
     * Gère la logique de sauvegarde (création ou modification d'un genre).
     */
    private void actionEnregistrer() {
        try {
            if (libelleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le libellé est requis.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (genreSelectionne == null) { // Création
                int nouvelId = com.mycompany.cinema.util.IdManager.obtenirProchainIdGenre();
                Genre nouveauGenre = new Genre(nouvelId, libelleField.getText().trim());
                adminService.ajouterGenre(nouveauGenre);
                JOptionPane.showMessageDialog(this, "Genre ajouté avec succès !");
            } else { // Modification
                genreSelectionne.setLibelle(libelleField.getText().trim());
                adminService.modifierGenre(genreSelectionne); 
                JOptionPane.showMessageDialog(this, "Genre mis à jour avec succès !");
            }

            chargerGenres();
            reinitialiserFormulaire();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gère la suppression du genre actuellement sélectionné, avec une demande
     * de confirmation.
     */
    private void actionSupprimer() {
        if (genreSelectionne == null) {
            return; // Ne rien faire si aucun genre n'est sélectionné.
        }

        Object[] options = {"Oui", "Non"};
        int response = JOptionPane.showOptionDialog(
                this,
                "Êtes-vous sûr de vouloir supprimer le genre '" + genreSelectionne.getLibelle() + "' ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        if (response == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerGenre(genreSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Genre supprimé avec succès !");
                chargerGenres();
                reinitialiserFormulaire();
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
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        genreJList = new javax.swing.JList<>();
        buttonsPanel = new javax.swing.JPanel();
        nouveauButton = new javax.swing.JButton();
        enregistrerButton = new javax.swing.JButton();
        supprimerButton = new javax.swing.JButton();
        modificationPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        libelleField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        genreJList.setPreferredSize(new java.awt.Dimension(300, 0));
        genreJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                genreJListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(genreJList);

        add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        nouveauButton.setText("Nouveau");
        nouveauButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(nouveauButton);

        enregistrerButton.setText("Enregistrer");
        enregistrerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(enregistrerButton);

        supprimerButton.setText("Supprimer");
        supprimerButton.setEnabled(false);
        supprimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(supprimerButton);

        add(buttonsPanel, java.awt.BorderLayout.PAGE_END);

        modificationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Détails "));
        java.awt.GridBagLayout modificationPanelLayout = new java.awt.GridBagLayout();
        modificationPanelLayout.columnWidths = new int[] {0, 5, 0};
        modificationPanelLayout.rowHeights = new int[] {0, 5, 0};
        modificationPanel.setLayout(modificationPanelLayout);

        jLabel1.setText("ID : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        modificationPanel.add(jLabel1, gridBagConstraints);

        idField.setEditable(false);
        idField.setPreferredSize(new java.awt.Dimension(250, 23));
        idField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                idFieldMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        modificationPanel.add(idField, gridBagConstraints);

        jLabel2.setText("Libellé : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        modificationPanel.add(jLabel2, gridBagConstraints);

        libelleField.setPreferredSize(new java.awt.Dimension(250, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        modificationPanel.add(libelleField, gridBagConstraints);

        add(modificationPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void nouveauButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauButtonActionPerformed
        reinitialiserFormulaire();
    }//GEN-LAST:event_nouveauButtonActionPerformed

    private void enregistrerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enregistrerButtonActionPerformed
        actionEnregistrer();
    }//GEN-LAST:event_enregistrerButtonActionPerformed

    private void supprimerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerButtonActionPerformed
        actionSupprimer();
    }//GEN-LAST:event_supprimerButtonActionPerformed

    private void genreJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_genreJListValueChanged
        if (!evt.getValueIsAdjusting()) {
            genreSelectionne = genreJList.getSelectedValue();
            afficherDetailsGenre(genreSelectionne);
        }
    }//GEN-LAST:event_genreJListValueChanged

    private void idFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_idFieldMouseClicked
        // On vérifie que le champ est bien désactivé pour l'édition
        if (!idField.isEditable()) {
            JOptionPane.showMessageDialog(
                    this, // Le panneau parent
                    "L'identifiant (ID) est généré automatiquement par le système lors de la création d'un nouvel élément.\n"
                    + "Il ne peut pas être modifié manuellement.", // Le message à afficher
                    "Information", // Le titre de la fenêtre pop-up
                    JOptionPane.INFORMATION_MESSAGE // Le type de message (icône "i")
            );
        }        
    }//GEN-LAST:event_idFieldMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton enregistrerButton;
    private javax.swing.JList<com.mycompany.cinema.Genre> genreJList;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField libelleField;
    private javax.swing.JPanel modificationPanel;
    private javax.swing.JButton nouveauButton;
    private javax.swing.JButton supprimerButton;
    // End of variables declaration//GEN-END:variables
}
