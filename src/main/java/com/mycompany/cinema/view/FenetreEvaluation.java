/*
 * FenetreEvaluation.java
 * Fenêtre de dialogue permettant à un client de donner ou de modifier son avis sur un film.
 * Elle gère deux modes : création et modification.
 */
package com.mycompany.cinema.view;

import com.mycompany.cinema.EvaluationClient;
import com.mycompany.cinema.service.ClientService;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

public class FenetreEvaluation extends javax.swing.JDialog {

    private final ClientService clientService;
    private int clientId;
    private int filmId;

    // Cette variable sert de "flag". Si elle est null, on passera au mode création.
    // Sinon, une évaluation précédente existe, donc on passe au mode modification.
    private EvaluationClient evaluationExistante;

    /**
     * Constructeur pour une nouvelle évaluation.
     */
    public FenetreEvaluation(java.awt.Frame parent, ClientService clientService, int clientId, int filmId) {
        super(parent, "Donner votre avis", true);
        this.clientService = clientService;
        this.clientId = clientId;
        this.filmId = filmId;
        this.evaluationExistante = null;
        initComponents();
        setLocationRelativeTo(parent);
    }

    /**
     * Constructeur pour modifier une évaluation existante.
     */
    public FenetreEvaluation(java.awt.Frame parent, ClientService clientService, EvaluationClient evaluation) {
        super(parent, "Modifier votre avis", true);
        this.clientService = clientService;
        this.evaluationExistante = evaluation;
        this.clientId = evaluation.getIdClient();
        this.filmId = evaluation.getIdFilm();
        initComponents();
        preRemplirFormulaire();
        setLocationRelativeTo(parent);
    }

    /**
     * Remplit les champs du formulaire si on est en mode modification.
     */
    private void preRemplirFormulaire() {
        if (evaluationExistante != null) {
            jTextAreaCommentaire.setText(evaluationExistante.getCommentaire());

            switch (evaluationExistante.getNote()) {
                case 1:
                    jRadioButton1.setSelected(true);
                    break;
                case 2:
                    jRadioButton2.setSelected(true);
                    break;
                case 3:
                    jRadioButton3.setSelected(true);
                    break;
                case 4:
                    jRadioButton4.setSelected(true);
                    break;
                case 5:
                    jRadioButton5.setSelected(true);
                    break;
            }
        }
    }

    /**
     * Gère la validation du formulaire pour la création ou la modification.
     */
    private void gererValidation() {
        if (buttonGroupNote.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une note.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int note = Integer.parseInt(buttonGroupNote.getSelection().getActionCommand());
        String commentaire = jTextAreaCommentaire.getText();

        try {
            // La séparation des modes création et modification évite qu'un client puisse voter plusieurs fois
            if (evaluationExistante == null) {
                // --- MODE CRÉATION ---
                EvaluationClient nouvelleEvaluation = new EvaluationClient(clientId, filmId, note, commentaire, LocalDateTime.now());
                clientService.ajouterEvaluation(nouvelleEvaluation);
                JOptionPane.showMessageDialog(this, "Merci pour votre avis !", "Succès", JOptionPane.INFORMATION_MESSAGE);

            } else { 
                // --- MODE MODIFICATION ---
                evaluationExistante.setNote(note);
                evaluationExistante.setCommentaire(commentaire);
                evaluationExistante.setDateEvaluation(LocalDateTime.now());
                
                clientService.modifierEvaluation(evaluationExistante);

                JOptionPane.showMessageDialog(this, "Votre avis a été mis à jour.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupNote = new javax.swing.ButtonGroup();
        jPanelNote = new javax.swing.JPanel();
        jLabelNote = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jPanelCommentaire = new javax.swing.JPanel();
        jLabelCommentaire = new javax.swing.JLabel();
        jScrollPaneCommentaire = new javax.swing.JScrollPane();
        jTextAreaCommentaire = new javax.swing.JTextArea();
        jButtonValider = new javax.swing.JButton();
        jButtonAnnuler = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelNote.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelNote.setText("Votre note :");

        buttonGroupNote.add(jRadioButton1);
        jRadioButton1.setText("1 ★ ");
        jRadioButton1.setActionCommand("1");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroupNote.add(jRadioButton2);
        jRadioButton2.setText("2 ★ ");
        jRadioButton2.setActionCommand("2");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroupNote.add(jRadioButton3);
        jRadioButton3.setText("3 ★ ");
        jRadioButton3.setActionCommand("3");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        buttonGroupNote.add(jRadioButton4);
        jRadioButton4.setText("4 ★ ");
        jRadioButton4.setActionCommand("4");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        buttonGroupNote.add(jRadioButton5);
        jRadioButton5.setText("5 ★ ");
        jRadioButton5.setActionCommand("5");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelNoteLayout = new javax.swing.GroupLayout(jPanelNote);
        jPanelNote.setLayout(jPanelNoteLayout);
        jPanelNoteLayout.setHorizontalGroup(
                jPanelNoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelNoteLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabelNote)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanelNoteLayout.createSequentialGroup()
                                .addContainerGap(55, Short.MAX_VALUE)
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton5)
                                .addGap(14, 14, 14))
        );
        jPanelNoteLayout.setVerticalGroup(
                jPanelNoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelNoteLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabelNote)
                                .addGap(4, 4, 4)
                                .addGroup(jPanelNoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jRadioButton1)
                                        .addComponent(jRadioButton2)
                                        .addComponent(jRadioButton3)
                                        .addComponent(jRadioButton5)
                                        .addComponent(jRadioButton4))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelCommentaire.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelCommentaire.setText("Votre commentaire (optionnel) :");

        jTextAreaCommentaire.setColumns(20);
        jTextAreaCommentaire.setRows(5);
        jScrollPaneCommentaire.setViewportView(jTextAreaCommentaire);

        javax.swing.GroupLayout jPanelCommentaireLayout = new javax.swing.GroupLayout(jPanelCommentaire);
        jPanelCommentaire.setLayout(jPanelCommentaireLayout);
        jPanelCommentaireLayout.setHorizontalGroup(
                jPanelCommentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelCommentaireLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPaneCommentaire)
                                .addContainerGap())
                        .addGroup(jPanelCommentaireLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabelCommentaire)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelCommentaireLayout.setVerticalGroup(
                jPanelCommentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelCommentaireLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabelCommentaire)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPaneCommentaire, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jButtonValider.setText("Valider");
        jButtonValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonValiderActionPerformed(evt);
            }
        });

        jButtonAnnuler.setText("Annuler");
        jButtonAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnnulerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanelCommentaire, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanelNote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonValider)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonAnnuler)
                                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanelNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelCommentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonValider)
                                        .addComponent(jButtonAnnuler))
                                .addContainerGap(8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jButtonValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonValiderActionPerformed
        gererValidation();
    }//GEN-LAST:event_jButtonValiderActionPerformed

    private void jButtonAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnnulerActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonAnnulerActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupNote;
    private javax.swing.JButton jButtonAnnuler;
    private javax.swing.JButton jButtonValider;
    private javax.swing.JLabel jLabelCommentaire;
    private javax.swing.JLabel jLabelNote;
    private javax.swing.JPanel jPanelCommentaire;
    private javax.swing.JPanel jPanelNote;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPaneCommentaire;
    private javax.swing.JTextArea jTextAreaCommentaire;
    // End of variables declaration//GEN-END:variables
}
