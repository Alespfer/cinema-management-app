package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Salle;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class GestionSalles extends javax.swing.JPanel {

    private final AdminService adminService;
    private Salle salleSelectionnee;
    private DefaultListModel<Salle> listModel;

    public GestionSalles(AdminService adminService) {
        this.adminService = adminService;

        initComponents();
        initModelAndRenderers();

        chargerListeSalles();
    }

    private void initModelAndRenderers() {
        listModel = new DefaultListModel<>();
        listeSalles.setModel(listModel);

        listeSalles.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Salle) {
                    Salle salle = (Salle) value;
                    setText(salle.getNumero() + " (" + salle.getCapacite() + " places)");
                }
                return this;
            }
        });
    }

    private void chargerListeSalles() {
        try {
            listModel.clear();
            List<Salle> salles = adminService.getAllSalles();
            for (Salle salle : salles) {
                listModel.addElement(salle);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des salles : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mettreAJourChamps(Salle salle) {
        if (salle != null) {
            idField.setText(String.valueOf(salle.getId()));
            numeroField.setText(salle.getNumero());
            capaciteField.setText(String.valueOf(salle.getCapacite()));
            supprimerButton.setEnabled(true);
        } else {
            idField.setText("");
            numeroField.setText("");
            capaciteField.setText("");
            supprimerButton.setEnabled(false);
        }
    }

    private void actionNouveau() {
        salleSelectionnee = null;
        listeSalles.clearSelection();
        mettreAJourChamps(null);
    }

    /**
     * Gère la création ou la modification d'une salle. VERSION CORRIGÉE AVEC ID
     * MANAGER.
     */
    private void actionEnregistrer() {
        String numero = numeroField.getText();
        if (numero == null || numero.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro de la salle ne peut pas être vide.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int capacite;
        try {
            capacite = Integer.parseInt(capaciteField.getText());
            if (capacite <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La capacité doit être un nombre entier positif.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (salleSelectionnee == null) { // Mode Création
                // 1. OBTENIR UN NOUVEL ID UNIQUE
                int nouvelId = com.mycompany.cinema.util.IdManager.getNextSalleId();

                // 2. CRÉER L'OBJET AVEC L'ID
                Salle nouvelleSalle = new Salle(
                        nouvelId,
                        numero,
                        capacite
                );

                // 3. ENVOYER AU SERVICE
                adminService.ajouterSalle(nouvelleSalle);
                JOptionPane.showMessageDialog(this, "Salle créée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

            } else { // Mode Modification (inchangé)
                salleSelectionnee.setNumero(numero);
                salleSelectionnee.setCapacite(capacite);
                adminService.modifierSalle(salleSelectionnee);
                JOptionPane.showMessageDialog(this, "Salle modifiée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }

            // Rechargement et nettoyage
            chargerListeSalles();
            actionNouveau();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionSupprimer() {
        if (salleSelectionnee == null) {
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer la salle '" + salleSelectionnee.getNumero() + "' ?",
                "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerSalle(salleSelectionnee.getId());
                JOptionPane.showMessageDialog(this, "Salle supprimée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                actionNouveau();
                chargerListeSalles();
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

        panneauDroite = new javax.swing.JPanel();
        formPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        numeroField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        capaciteField = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        nouveauButton = new javax.swing.JButton();
        enregistrerButton = new javax.swing.JButton();
        supprimerButton = new javax.swing.JButton();
        panneauGauche = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listeSalles = new javax.swing.JList<>();

        setLayout(new java.awt.BorderLayout());

        panneauDroite.setLayout(new java.awt.BorderLayout());

        formPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Détails de la salle"));
        formPanel.setLayout(new java.awt.GridLayout(0, 2));

        jLabel1.setText("ID : ");
        formPanel.add(jLabel1);

        idField.setEditable(false);
        formPanel.add(idField);

        jLabel2.setText("Numéro/Nom : ");
        formPanel.add(jLabel2);
        formPanel.add(numeroField);

        jLabel3.setText("Capacité : ");
        formPanel.add(jLabel3);
        formPanel.add(capaciteField);

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
        supprimerButton.setEnabled(false);
        supprimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(supprimerButton);

        panneauDroite.add(buttonPanel, java.awt.BorderLayout.CENTER);

        add(panneauDroite, java.awt.BorderLayout.LINE_END);

        panneauGauche.setBorder(javax.swing.BorderFactory.createTitledBorder("Salles existantes"));
        panneauGauche.setLayout(new java.awt.BorderLayout());

        listeSalles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listeSallesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listeSalles);

        panneauGauche.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(panneauGauche, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void nouveauButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauButtonActionPerformed
        actionNouveau();
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

    private void listeSallesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listeSallesValueChanged
        if (!evt.getValueIsAdjusting()) {
            salleSelectionnee = listeSalles.getSelectedValue();
            mettreAJourChamps(salleSelectionnee);
        }// TODO add your handling code here:
    }//GEN-LAST:event_listeSallesValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JTextField capaciteField;
    private javax.swing.JButton enregistrerButton;
    private javax.swing.JPanel formPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<com.mycompany.cinema.Salle> listeSalles;
    private javax.swing.JButton nouveauButton;
    private javax.swing.JTextField numeroField;
    private javax.swing.JPanel panneauDroite;
    private javax.swing.JPanel panneauGauche;
    private javax.swing.JButton supprimerButton;
    // End of variables declaration//GEN-END:variables
}
