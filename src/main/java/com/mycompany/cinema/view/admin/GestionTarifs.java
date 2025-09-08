package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class GestionTarifs extends javax.swing.JPanel {

    private final AdminService adminService;
    private Tarif tarifSelectionne;
    // Le modèle de la liste est géré manuellement.
    private DefaultListModel<Tarif> listModel;

    public GestionTarifs(AdminService adminService) {
        this.adminService = adminService;

        initComponents();
        initModelAndRenderers(); // Initialise notre modèle et configure l'affichage.

        chargerListeTarifs(); // Charge les données initiales.
    }

    private void initModelAndRenderers() {
        listModel = new DefaultListModel<>();
        listeTarifs.setModel(listModel);

        listeTarifs.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tarif) {
                    Tarif tarif = (Tarif) value;
                    setText(tarif.getLibelle() + " - " + String.format("%.2f", tarif.getPrix()) + " €");
                }
                return this;
            }
        });
    }

    private void chargerListeTarifs() {
        try {
            listModel.clear();
            List<Tarif> tarifs = adminService.getAllTarifs();
            for (Tarif tarif : tarifs) {
                listModel.addElement(tarif);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des tarifs : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Dans la classe GestionTarifs
    public void rafraichirDonnees() {
        chargerListeTarifs(); // Suppose que 'chargerTarifs()' est la méthode qui remplit ta JTable ou JList.
    }

    private void mettreAJourChamps(Tarif tarif) {
        if (tarif != null) {
            idField.setText(String.valueOf(tarif.getId()));
            libelleField.setText(tarif.getLibelle());
            prixField.setText(String.format("%.2f", tarif.getPrix()).replace(',', '.'));
            supprimerButton.setEnabled(true);
        } else {
            idField.setText("");
            libelleField.setText("");
            prixField.setText("");
            supprimerButton.setEnabled(false);
        }
    }

    private void actionNouveau() {
        tarifSelectionne = null;
        listeTarifs.clearSelection();
        mettreAJourChamps(null);
    }

    /**
     * Gère la création ou la modification d'un tarif. VERSION CORRIGÉE AVEC ID
     * MANAGER.
     */
    private void actionEnregistrer() {
        String libelle = libelleField.getText();
        if (libelle == null || libelle.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le libellé ne peut pas être vide.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double prix;
        try {
            prix = Double.parseDouble(prixField.getText().replace(',', '.'));
            if (prix < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le prix doit être un nombre positif (ex: 9.20).", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (tarifSelectionne == null) { // Mode Création
                // 1. OBTENIR UN NOUVEL ID UNIQUE
                int nouvelId = com.mycompany.cinema.util.IdManager.getNextTarifId();

                // 2. CRÉER L'OBJET AVEC L'ID
                Tarif nouveauTarif = new Tarif(
                        nouvelId,
                        libelle,
                        prix
                );

                // 3. ENVOYER AU SERVICE
                adminService.ajouterTarif(nouveauTarif);
                JOptionPane.showMessageDialog(this, "Tarif créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

            } else { // Mode Modification (inchangé)
                tarifSelectionne.setLibelle(libelle);
                tarifSelectionne.setPrix(prix);
                adminService.modifierTarif(tarifSelectionne);
                JOptionPane.showMessageDialog(this, "Tarif modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }

            // Rechargement et nettoyage
            chargerListeTarifs();
            actionNouveau();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionSupprimer() {
        if (tarifSelectionne == null) {
            return;
        }

        Object[] options = {"Oui", "Non"};
        int response = JOptionPane.showOptionDialog(
                this,
                "Êtes-vous sûr de vouloir supprimer le tarif '" + tarifSelectionne.getLibelle() + "' ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, options, options[0]
        );

        if (response == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerTarif(tarifSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Tarif supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                actionNouveau();
                chargerListeTarifs();
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
        listeTarifs = new javax.swing.JList<>();
        panneauDroite = new javax.swing.JPanel();
        formPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        libelleField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        prixField = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        nouveauButton = new javax.swing.JButton();
        enregistrerButton = new javax.swing.JButton();
        supprimerButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        listeTarifs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listeTarifsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listeTarifs);

        add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        panneauDroite.setLayout(new java.awt.BorderLayout());

        formPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Détails du tarif"));
        formPanel.setLayout(new java.awt.GridLayout(0, 2, 5, 5));

        jLabel1.setText("ID : ");
        formPanel.add(jLabel1);

        idField.setEditable(false);
        idField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                idFieldMouseClicked(evt);
            }
        });
        formPanel.add(idField);

        jLabel2.setText("Libellé : ");
        formPanel.add(jLabel2);
        formPanel.add(libelleField);

        jLabel3.setText("Prix : ");
        formPanel.add(jLabel3);
        formPanel.add(prixField);

        panneauDroite.add(formPanel, java.awt.BorderLayout.PAGE_START);

        nouveauButton.setText("Nouveau");
        nouveauButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(nouveauButton);

        enregistrerButton.setText("Enregistrer");
        enregistrerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(enregistrerButton);

        supprimerButton.setText("Supprimer");
        supprimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(supprimerButton);

        panneauDroite.add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        add(panneauDroite, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void nouveauButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauButtonActionPerformed
        actionNouveau();// TODO add your handling code here:
    }//GEN-LAST:event_nouveauButtonActionPerformed

    private void enregistrerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enregistrerButtonActionPerformed
        actionEnregistrer();// TODO add your handling code here:
    }//GEN-LAST:event_enregistrerButtonActionPerformed

    private void supprimerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerButtonActionPerformed
        actionSupprimer();
// TODO add your handling code here:
    }//GEN-LAST:event_supprimerButtonActionPerformed

    private void listeTarifsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listeTarifsValueChanged
        if (!evt.getValueIsAdjusting()) {
            tarifSelectionne = listeTarifs.getSelectedValue();
            mettreAJourChamps(tarifSelectionne);
        }// TODO add your handling code here:
    }//GEN-LAST:event_listeTarifsValueChanged

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
        }        // TODO add your handling code here:
    }//GEN-LAST:event_idFieldMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton enregistrerButton;
    private javax.swing.JPanel formPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField libelleField;
    private javax.swing.JList<com.mycompany.cinema.Tarif> listeTarifs;
    private javax.swing.JButton nouveauButton;
    private javax.swing.JPanel panneauDroite;
    private javax.swing.JTextField prixField;
    private javax.swing.JButton supprimerButton;
    // End of variables declaration//GEN-END:variables

}
