package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.Role;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultComboBoxModel; // Importation nécessaire
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class GestionPersonnel extends javax.swing.JPanel {

    private final AdminService adminService;
    private Personnel personnelSelectionne;
    // Les modèles sont gérés manuellement.
    private DefaultListModel<Personnel> listModel;
    private DefaultComboBoxModel<Role> comboBoxModel;

    public GestionPersonnel(AdminService adminService) {
        this.adminService = adminService;

        initComponents();
        initModelsAndRenderers();

        chargerRoles();
        chargerListePersonnel();
    }

    private void initModelsAndRenderers() {
        // CORRECTION : Création et assignation MANUELLE des modèles.
        listModel = new DefaultListModel<>();
        listePersonnel.setModel(listModel);

        comboBoxModel = new DefaultComboBoxModel<>();
        roleComboBox.setModel(comboBoxModel);

        // Configuration de l'affichage pour la JList.
        listePersonnel.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Personnel) {
                    setText(((Personnel) value).getPrenom() + " " + ((Personnel) value).getNom());
                }
                return this;
            }
        });

        // Configuration de l'affichage pour la JComboBox.
        roleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Role) {
                    setText(((Role) value).getLibelle());
                } else if (value == null) {
                    setText("Sélectionnez un rôle");
                }
                return this;
            }
        });
    }

    private void chargerListePersonnel() {
        try {
            listModel.clear();
            List<Personnel> personnelList = adminService.getAllPersonnel();
            for (Personnel p : personnelList) {
                listModel.addElement(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement du personnel: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Dans la classe GestionPersonnel
    public void rafraichirDonnees() {
        chargerListePersonnel(); // Suppose que 'chargerPersonnel()' est la méthode qui remplit ta JTable ou JList.
    }

    private void chargerRoles() {
        try {
            comboBoxModel.removeAllElements();
            List<Role> roles = adminService.getAllRoles();
            for (Role role : roles) {
                comboBoxModel.addElement(role);
            }
            roleComboBox.setSelectedIndex(-1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des rôles: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mettreAJourChamps(Personnel p) {
        if (p != null) {
            idField.setText(String.valueOf(p.getId()));
            nomField.setText(p.getNom());
            prenomField.setText(p.getPrenom());
            motDePasseField.setText(p.getMotDePasse());
            supprimerButton.setEnabled(true);

            Role roleAssigne = null;
            for (int i = 0; i < comboBoxModel.getSize(); i++) {
                if (comboBoxModel.getElementAt(i).getId() == p.getIdRole()) {
                    roleAssigne = comboBoxModel.getElementAt(i);
                    break;
                }
            }
            comboBoxModel.setSelectedItem(roleAssigne);

        } else {
            idField.setText("");
            nomField.setText("");
            prenomField.setText("");
            motDePasseField.setText("");
            roleComboBox.setSelectedIndex(-1);
            supprimerButton.setEnabled(false);
        }
    }

    // ... (Le reste des méthodes (actionNouveau, actionEnregistrer, actionSupprimer) reste INCHANGÉ)
    private void actionNouveau() {
        personnelSelectionne = null;
        listePersonnel.clearSelection();
        mettreAJourChamps(null);
    }

    /**
     * Gère la création ou la modification d'un membre du personnel. VERSION
     * CORRIGÉE AVEC ID MANAGER.
     */
    private void actionEnregistrer() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String motDePasse = new String(motDePasseField.getPassword());
        Role roleSelectionne = (Role) roleComboBox.getSelectedItem();

        // Validation des entrées (inchangée)
        if (nom == null || nom.trim().isEmpty() || prenom == null || prenom.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom et le prénom ne peuvent pas être vides.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (roleSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rôle.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (personnelSelectionne == null) { // Mode Création
                // 1. OBTENIR UN NOUVEL ID UNIQUE
                int nouvelId = com.mycompany.cinema.util.IdManager.getNextPersonnelId();

                // 2. CRÉER L'OBJET AVEC L'ID
                Personnel nouveau = new Personnel(
                        nouvelId,
                        nom,
                        prenom,
                        motDePasse,
                        roleSelectionne.getId()
                );

                // 3. ENVOYER AU SERVICE
                adminService.ajouterPersonnel(nouveau);
                JOptionPane.showMessageDialog(this, "Membre du personnel créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

            } else { // Mode Modification (inchangé)
                personnelSelectionne.setNom(nom);
                personnelSelectionne.setPrenom(prenom);
                personnelSelectionne.setMotDePasse(motDePasse);
                personnelSelectionne.setIdRole(roleSelectionne.getId());
                adminService.modifierPersonnel(personnelSelectionne);
                JOptionPane.showMessageDialog(this, "Membre du personnel modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }

            // Rechargement et nettoyage
            chargerListePersonnel();
            actionNouveau();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionSupprimer() {
        if (personnelSelectionne == null) {
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer '" + personnelSelectionne.getPrenom() + " " + personnelSelectionne.getNom() + "' ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerPersonnel(personnelSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Membre supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                actionNouveau();
                chargerListePersonnel();
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

        panneauGauche = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listePersonnel = new javax.swing.JList<>();
        panneauDroite = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nomField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        prenomField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        motDePasseField = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        roleComboBox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        nouveauButton = new javax.swing.JButton();
        enregistrerButton = new javax.swing.JButton();
        supprimerButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        panneauGauche.setBorder(javax.swing.BorderFactory.createTitledBorder("Membres du personnel"));
        panneauGauche.setLayout(new java.awt.BorderLayout());

        listePersonnel.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listePersonnelValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listePersonnel);

        panneauGauche.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(panneauGauche, java.awt.BorderLayout.CENTER);

        panneauDroite.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setLayout(new java.awt.GridLayout(0, 2));

        jLabel1.setText("ID : ");
        jPanel2.add(jLabel1);

        idField.setEditable(false);
        jPanel2.add(idField);

        jLabel2.setText("Nom : ");
        jPanel2.add(jLabel2);
        jPanel2.add(nomField);

        jLabel3.setText("Prénom : ");
        jPanel2.add(jLabel3);
        jPanel2.add(prenomField);

        jLabel4.setText("Mot de passe : ");
        jPanel2.add(jLabel4);
        jPanel2.add(motDePasseField);

        jLabel5.setText("Rôle : ");
        jPanel2.add(jLabel5);

        jPanel2.add(roleComboBox);

        panneauDroite.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        nouveauButton.setText("Nouveau");
        nouveauButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauButtonActionPerformed(evt);
            }
        });
        jPanel3.add(nouveauButton);

        enregistrerButton.setText("Enregistrer");
        enregistrerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerButtonActionPerformed(evt);
            }
        });
        jPanel3.add(enregistrerButton);

        supprimerButton.setText("Supprimer");
        supprimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerButtonActionPerformed(evt);
            }
        });
        jPanel3.add(supprimerButton);

        panneauDroite.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        add(panneauDroite, java.awt.BorderLayout.LINE_END);
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
        actionSupprimer();// TODO add your handling code here:
    }//GEN-LAST:event_supprimerButtonActionPerformed

    private void listePersonnelValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listePersonnelValueChanged
        if (!evt.getValueIsAdjusting()) {
            personnelSelectionne = listePersonnel.getSelectedValue();
            mettreAJourChamps(personnelSelectionne);
        }// TODO add your handling code here:
    }//GEN-LAST:event_listePersonnelValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton enregistrerButton;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<com.mycompany.cinema.Personnel> listePersonnel;
    private javax.swing.JPasswordField motDePasseField;
    private javax.swing.JTextField nomField;
    private javax.swing.JButton nouveauButton;
    private javax.swing.JPanel panneauDroite;
    private javax.swing.JPanel panneauGauche;
    private javax.swing.JTextField prenomField;
    private javax.swing.JComboBox<com.mycompany.cinema.Role> roleComboBox;
    private javax.swing.JButton supprimerButton;
    // End of variables declaration//GEN-END:variables

  
}
