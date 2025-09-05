package com.mycompany.cinema.view;

import com.mycompany.cinema.service.ClientService;
import javax.swing.*;
import java.awt.*;

public class Inscription extends javax.swing.JDialog {
    
    private final ClientService clientService;
    
    public Inscription(java.awt.Frame parent, ClientService clientService, boolean modal) {
        super(parent, "Création de Compte Client", true);
        
        this.clientService = clientService;
        initComponents();
    }
    
    private void gererInscription() {
        String nom = jTextFieldNom.getText();
        String email = jTextFieldEmail.getText();
        String password = new String(jPasswordField1.getPassword());
        String confirmPassword = new String(jPasswordField2.getPassword());
       
        if (nom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

        jPanelInscription = new javax.swing.JPanel();
        jLabelNom = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelMdp = new javax.swing.JLabel();
        jLabelConfirmationMdp = new javax.swing.JLabel();
        jTextFieldNom = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jButtonInscription = new javax.swing.JButton();
        jButtonAnnuler = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPasswordField2 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelNom.setText("Nom complet :");

        jLabelEmail.setText("Email :");

        jLabelMdp.setText("Mot de passe :");

        jLabelConfirmationMdp.setText("Confimer le mot de passe :");

        jButtonInscription.setText("S'inscrire");
        jButtonInscription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInscriptionActionPerformed(evt);
            }
        });

        jButtonAnnuler.setText("Annuler");
        jButtonAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnnulerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelInscriptionLayout = new javax.swing.GroupLayout(jPanelInscription);
        jPanelInscription.setLayout(jPanelInscriptionLayout);
        jPanelInscriptionLayout.setHorizontalGroup(
            jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInscriptionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonInscription, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
            .addGroup(jPanelInscriptionLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNom)
                    .addComponent(jLabelEmail)
                    .addGroup(jPanelInscriptionLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabelMdp))
                    .addComponent(jLabelConfirmationMdp))
                .addGap(59, 59, 59)
                .addGroup(jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPasswordField2, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addGroup(jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextFieldNom)
                        .addComponent(jTextFieldEmail)
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelInscriptionLayout.setVerticalGroup(
            jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInscriptionLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNom)
                    .addComponent(jTextFieldNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail))
                .addGap(15, 15, 15)
                .addGroup(jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMdp)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelConfirmationMdp)
                    .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jPanelInscriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAnnuler)
                    .addComponent(jButtonInscription))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelInscription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelInscription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonInscriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInscriptionActionPerformed
        gererInscription();
    }//GEN-LAST:event_jButtonInscriptionActionPerformed

    private void jButtonAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnnulerActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonAnnulerActionPerformed
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnnuler;
    private javax.swing.JButton jButtonInscription;
    private javax.swing.JLabel jLabelConfirmationMdp;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelMdp;
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JPanel jPanelInscription;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldNom;
    // End of variables declaration//GEN-END:variables
}
