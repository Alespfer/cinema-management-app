/*
 * PanneauGestionSalles.java
 * Panneau pour la gestion des salles de cinéma.
 * Contient une logique spécifique pour la création du plan des sièges lors de l'ajout d'une nouvelle salle.
 */
package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Salle;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class PanneauGestionSalles extends javax.swing.JPanel {

    private final AdminService adminService;
    private Salle salleSelectionnee;
    private DefaultListModel<Salle> listModel;

    public PanneauGestionSalles(AdminService adminService) {
        this.adminService = adminService;

        initComponents();
        configurerModeleEtRenderers();

        chargerListeSalles();
    }

    /**
     * Initialise le modèle de la JList et son rendu personnalisé.
     */
    private void configurerModeleEtRenderers() {
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

    /**
     * Charge ou recharge la liste des salles depuis le service.
     */
    private void chargerListeSalles() {
        try {
            listModel.clear();
            List<Salle> salles = adminService.trouverToutesLesSalles();
            for (Salle salle : salles) {
                listModel.addElement(salle);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des salles : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Méthode publique pour rafraîchir les données.
     */
    public void rafraichirDonnees() {
        chargerListeSalles();
    }

    /**
     * Met à jour les champs du formulaire avec les détails d'une salle.
     */
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

    /**
     * Réinitialise le formulaire.
     */
    private void actionNouveau() {
        salleSelectionnee = null;
        listeSalles.clearSelection();
        mettreAJourChamps(null);
    }

    /**
     * Gère la sauvegarde (création ou modification) d'une salle. La création
     * inclut une étape pour configurer le plan des sièges.
     */
    private void actionEnregistrer() {
        String numero = numeroField.getText().trim();
        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro de la salle ne peut pas être vide.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int capacite;
        try {
            capacite = Integer.parseInt(capaciteField.getText().trim());
            if (capacite <= 0) {
                JOptionPane.showMessageDialog(this, "La capacité doit être un nombre entier positif.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La capacité doit être un nombre entier valide.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (salleSelectionnee == null) { // Création

                int nbRangees = 0;
                int nbSiegesParRangee = 0;
                boolean configurationValide = false;

                while (!configurationValide) {
                    try {
                        String nbRangeesStr = JOptionPane.showInputDialog(this, "Combien de rangées pour la salle '" + numero + "' ?", "Configuration de la salle", JOptionPane.QUESTION_MESSAGE);
                        if (nbRangeesStr == null) {
                            return; 
                        }
                        nbRangees = Integer.parseInt(nbRangeesStr.trim());

                        String nbSiegesStr = JOptionPane.showInputDialog(this, "Combien de sièges par rangée ?", "Configuration de la salle", JOptionPane.QUESTION_MESSAGE);
                        if (nbSiegesStr == null) {
                            return; 
                        }
                        nbSiegesParRangee = Integer.parseInt(nbSiegesStr.trim());

                        if (nbRangees <= 0 || nbSiegesParRangee <= 0) {
                            JOptionPane.showMessageDialog(this, "Le nombre de rangées et de sièges doit être positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            // Si nombre invalide, configurationValide reste false, la boucle va recommencer.
                        } else if (nbRangees * nbSiegesParRangee != capacite) {
                            JOptionPane.showMessageDialog(this,
                                    "Incohérence : " + nbRangees + " rangées × " + nbSiegesParRangee + " sièges = " + (nbRangees * nbSiegesParRangee) + " places.\n"
                                    + "Cela ne correspond pas à la capacité totale de " + capacite + " que vous avez indiquée.",
                                    "Erreur de configuration", JOptionPane.ERROR_MESSAGE);
                            // Si nombre incohérent, configurationValide reste false, la boucle va recommencer.
                        } else {
                            // Toutes les validations sont passées, on peut sortir de la boucle.
                            configurationValide = true;
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Veuillez entrer des nombres entiers valides.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    }
                }

                int nouvelId = com.mycompany.cinema.util.IdManager.obtenirProchainIdSalle();
                Salle nouvelleSalle = new Salle(nouvelId, numero, capacite);

                adminService.ajouterSalleAvecPlan(nouvelleSalle, nbRangees, nbSiegesParRangee);

                JOptionPane.showMessageDialog(this, "Salle et son plan de " + (nbRangees * nbSiegesParRangee) + " sièges créés avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

            } else { // Modification
                salleSelectionnee.setNumero(numero);
                salleSelectionnee.setCapacite(capacite);
                adminService.modifierSalle(salleSelectionnee);
                JOptionPane.showMessageDialog(this, "Salle modifiée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }

            chargerListeSalles();
            actionNouveau();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    /**
     * Gère la suppression de la salle sélectionnée.
     */

    private void actionSupprimer() {
        if (salleSelectionnee == null) {
            return;
        }

        Object[] options = {"Oui", "Non"};
        int response = JOptionPane.showOptionDialog(
                this,
                "Êtes-vous sûr de vouloir supprimer la salle '" + salleSelectionnee.getNumero() + "' ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, 
                null, options, options[0]
        );

        if (response == JOptionPane.YES_OPTION) {
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
        idField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                idFieldMouseClicked(evt);
            }
        });
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
    }//GEN-LAST:event_nouveauButtonActionPerformed

    private void enregistrerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enregistrerButtonActionPerformed
        actionEnregistrer();
    }//GEN-LAST:event_enregistrerButtonActionPerformed

    private void supprimerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerButtonActionPerformed
        actionSupprimer();
    }//GEN-LAST:event_supprimerButtonActionPerformed

    private void listeSallesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listeSallesValueChanged
        if (!evt.getValueIsAdjusting()) {
            salleSelectionnee = listeSalles.getSelectedValue();
            mettreAJourChamps(salleSelectionnee);
        }
    }//GEN-LAST:event_listeSallesValueChanged

    private void idFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_idFieldMouseClicked
        if (!idField.isEditable()) {
            JOptionPane.showMessageDialog(
                    this, 
                    "L'identifiant (ID) est généré automatiquement par le système lors de la création d'un nouvel élément.\n"
                    + "Il ne peut pas être modifié manuellement.", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE 
            );
        }
    }//GEN-LAST:event_idFieldMouseClicked


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
