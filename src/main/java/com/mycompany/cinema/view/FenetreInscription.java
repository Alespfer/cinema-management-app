/*
 * FenetreInscription.java
 * Fenêtre permettant à un nouvel utilisateur de créer un compte client.
 */

package com.mycompany.cinema.view;

import com.mycompany.cinema.service.ClientService;
import javax.swing.*;
import java.awt.*;

public class FenetreInscription extends javax.swing.JDialog {
    
    private final ClientService clientService;
    
    public FenetreInscription(java.awt.Frame parent, ClientService clientService, boolean modal) {
        super(parent, "Création de Compte Client", true);
        
        this.clientService = clientService;
        initComponents();
        setLocationRelativeTo(parent); 

    }
    
    
    /**
     * Gère la logique de création du compte lors du clic sur le bouton d'inscription.
     */
    private void gererInscription() {
        
        // 1. Récupération des données saisies
        String nom = nomField.getText();
        String email = emailField.getText();
        String password = new String(MotDePasseField.getPassword());
        String confirmPassword = new String(ConfirmationField.getPassword());
       
        
        // 2. Validation des données
        if (nom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 3. Appel au service pour créer le compte
        try {
            clientService.creerCompteClient(nom, email, password);
            JOptionPane.showMessageDialog(this, "Compte créé avec succès ! Vous pouvez maintenant vous connecter.", "Inscription réussie", JOptionPane.INFORMATION_MESSAGE);
            dispose(); 
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nomField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        MotDePasseField = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        ConfirmationField = new javax.swing.JPasswordField();
        inscriptionButton = new javax.swing.JButton();
        annulerButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel1.setLayout(new java.awt.GridLayout(0, 2, 5, 10));

        jLabel1.setText("Nom complet : ");
        jPanel1.add(jLabel1);
        jPanel1.add(nomField);

        jLabel2.setText("Email : ");
        jPanel1.add(jLabel2);
        jPanel1.add(emailField);

        jLabel3.setText("Nouveau mot de passe : ");
        jPanel1.add(jLabel3);
        jPanel1.add(MotDePasseField);

        jLabel4.setText("Confirmer le mot de passe : ");
        jPanel1.add(jLabel4);
        jPanel1.add(ConfirmationField);

        inscriptionButton.setText("S'inscrire");
        inscriptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inscriptionButtonActionPerformed(evt);
            }
        });
        jPanel1.add(inscriptionButton);

        annulerButton.setText("Annuler");
        annulerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerButtonActionPerformed(evt);
            }
        });
        jPanel1.add(annulerButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonInscriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInscriptionActionPerformed
    }//GEN-LAST:event_jButtonInscriptionActionPerformed

    private void jButtonAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnnulerActionPerformed
    }//GEN-LAST:event_jButtonAnnulerActionPerformed

    private void inscriptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inscriptionButtonActionPerformed
        gererInscription(); // TODO add your handling code here:
    }//GEN-LAST:event_inscriptionButtonActionPerformed

    private void annulerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerButtonActionPerformed
        dispose();
    }//GEN-LAST:event_annulerButtonActionPerformed
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField ConfirmationField;
    private javax.swing.JPasswordField MotDePasseField;
    private javax.swing.JButton annulerButton;
    private javax.swing.JTextField emailField;
    private javax.swing.JButton inscriptionButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nomField;
    // End of variables declaration//GEN-END:variables
}
