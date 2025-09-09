/*
 * PanneauGestionPersonnel.java
 * Panneau pour la gestion des membres du personnel.
 * Permet de créer, modifier et supprimer des employés, et de leur assigner un rôle.
 */
package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.Role;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class PanneauGestionPersonnel extends javax.swing.JPanel {

    private final AdminService adminService;
    private Personnel personnelSelectionne;
    private DefaultListModel<Personnel> personnelListModel;
    private DefaultComboBoxModel<Role> roleComboBoxModel;

    public PanneauGestionPersonnel(AdminService adminService) {
        this.adminService = adminService;
        initComponents();
        configurerModelesEtRendus();
        rafraichirDonnees();
    }

    /**
     * Méthode publique pour que la fenêtre principale puisse rafraîchir les
     * données lorsque cet onglet devient visible.
     */
    public void rafraichirDonnees() {
        chargerRoles();
        chargerListePersonnel();
        reinitialiserFormulaire();
    }

    /**
     * Initialise les modèles de données et les moteurs de rendu pour la JList
     * et la JComboBox.
     */
    private void configurerModelesEtRendus() {

        // Configuration de la liste du personnel
        personnelListModel = new DefaultListModel<>();
        listePersonnel.setModel(personnelListModel);
        listePersonnel.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Personnel) {
                    Personnel p = (Personnel) value;
                    setText(p.getPrenom() + " " + p.getNom());
                }
                return this;
            }
        });

        // Configuration de la liste déroulante des rôles
        roleComboBoxModel = new DefaultComboBoxModel<>();
        roleComboBox.setModel(roleComboBoxModel);
        roleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Role) {
                    setText(((Role) value).getLibelle());
                }
                return this;
            }
        });
    }

    /**
     * Charge ou recharge la liste des employés depuis le service.
     */
    private void chargerListePersonnel() {
        personnelListModel.clear();
        List<Personnel> personnelList = adminService.trouverToutLePersonnel();
        for (Personnel p : personnelList) {
            personnelListModel.addElement(p);
        }
    }

    /**
     * Charge ou recharge la liste des rôles disponibles pour la liste
     * déroulante.
     */
    private void chargerRoles() {
        roleComboBoxModel.removeAllElements();
        List<Role> roles = adminService.trouverTousLesRoles();
        for (Role role : roles) {
            roleComboBoxModel.addElement(role);
        }
    }

    /**
     * Affiche les détails d'un employé sélectionné dans le formulaire.
     */
    private void afficherDetailsPersonnel(Personnel p) {
        personnelSelectionne = p;
        if (p != null) {
            idField.setText(String.valueOf(p.getId()));
            nomField.setText(p.getNom());
            prenomField.setText(p.getPrenom());
            emailField.setText(p.getEmail());
            motDePasseField.setText("");
            supprimerButton.setEnabled(true);

            // Sélectionne le bon rôle dans la liste déroulante.
            Role roleAssigne = null;
            for (int i = 0; i < roleComboBoxModel.getSize(); i++) {
                if (roleComboBoxModel.getElementAt(i).getId() == p.getIdRole()) {
                    roleAssigne = roleComboBoxModel.getElementAt(i);
                    break;
                }
            }
            roleComboBox.setSelectedItem(roleAssigne);

        } else {
            reinitialiserFormulaire();
        }
    }

    /**
     * Vide le formulaire et le réinitialise pour une nouvelle saisie.
     */
    private void reinitialiserFormulaire() {
        listePersonnel.clearSelection();
        personnelSelectionne = null;
        idField.setText("");
        nomField.setText("");
        prenomField.setText("");
        emailField.setText("");
        motDePasseField.setText("");
        roleComboBox.setSelectedIndex(-1); // Aucune sélection
        supprimerButton.setEnabled(false);
    }

    /**
     * Gère la logique de sauvegarde (création ou modification d'un membre du
     * personnel).
     */
    private void actionEnregistrer() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String motDePasse = new String(motDePasseField.getPassword());
        Role roleSelectionne = (Role) roleComboBox.getSelectedItem();

        // --- DEBUT DES MODIFICATIONS DE VALIDATION ---
        // 1. Validation des champs obligatoires
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || roleSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Nom, prénom, email et rôle sont obligatoires.", "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validation du format de l'email
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une adresse email au format valide (ex: contact@cinema.com).", "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (personnelSelectionne == null) { // Mode Création
                if (motDePasse.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le mot de passe est obligatoire pour un nouvel utilisateur.", "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int nouvelId = com.mycompany.cinema.util.IdManager.obtenirProchainIdPersonnel();
                Personnel nouveau = new Personnel(nouvelId, nom, prenom, email, motDePasse, roleSelectionne.getId());
                adminService.ajouterPersonnel(nouveau);
                JOptionPane.showMessageDialog(this, "Membre du personnel créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

            } else { // Mode Modification
                personnelSelectionne.setNom(nom);
                personnelSelectionne.setPrenom(prenom);
                personnelSelectionne.setEmail(email);
                personnelSelectionne.setIdRole(roleSelectionne.getId());
                if (!motDePasse.isEmpty()) {
                    personnelSelectionne.setMotDePasse(motDePasse);
                }
                adminService.modifierPersonnel(personnelSelectionne);
                JOptionPane.showMessageDialog(this, "Membre du personnel modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
            rafraichirDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gère la suppression du membre du personnel sélectionné.
     */
    private void actionSupprimer() {
        if (personnelSelectionne == null) {
            return;
        }

        Object[] options = {"Oui", "Non"};
        int response = JOptionPane.showOptionDialog(
                this,
                "Êtes-vous sûr de vouloir supprimer '" + personnelSelectionne.getPrenom() + " " + personnelSelectionne.getNom() + "' ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (response == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerPersonnel(personnelSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Membre supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                rafraichirDonnees();
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
        jLabel6 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
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
        idField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                idFieldMouseClicked(evt);
            }
        });
        jPanel2.add(idField);

        jLabel2.setText("Nom : ");
        jPanel2.add(jLabel2);
        jPanel2.add(nomField);

        jLabel3.setText("Prénom : ");
        jPanel2.add(jLabel3);
        jPanel2.add(prenomField);

        jLabel6.setText("Email : ");
        jPanel2.add(jLabel6);
        jPanel2.add(emailField);

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
        reinitialiserFormulaire();
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
            afficherDetailsPersonnel(personnelSelectionne);
        }// TODO add your handling code here:
    }//GEN-LAST:event_listePersonnelValueChanged

    private void idFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_idFieldMouseClicked
        // On vérifie que le champ est bien désactivé pour l'édition
        if (!idField.isEditable()) {
            JOptionPane.showMessageDialog(
                    this, 
                    "L'identifiant (ID) est généré automatiquement par le système lors de la création d'un nouvel élément.\n"
                    + "Il ne peut pas être modifié manuellement.", // Le message à afficher
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE 
            );
        } // TODO add your handling code here:
    }//GEN-LAST:event_idFieldMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField emailField;
    private javax.swing.JButton enregistrerButton;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
