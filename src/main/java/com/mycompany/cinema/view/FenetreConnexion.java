/*
 * FenetreConnexion.java
 * Point d'entrée de l'application. Gère l'authentification des clients et du personnel.
 */
package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.Role;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.service.ClientService;
import com.mycompany.cinema.service.impl.CinemaServiceImpl;
import com.mycompany.cinema.view.admin.FenetreAdmin;
import com.mycompany.cinema.view.admin.PointDeVente;
import javax.swing.JOptionPane;

public class FenetreConnexion extends javax.swing.JFrame {

    private final ClientService clientService;
    private final AdminService adminService;

    public FenetreConnexion() {
        CinemaServiceImpl serviceImpl = new CinemaServiceImpl();
        this.clientService = serviceImpl;
        this.adminService = serviceImpl;
        initComponents();
        setTitle("Cinema PISE 2025 - Connexion");
        // On centre la fenêtre à l'écran
        setLocationRelativeTo(null); 
    }

   
    /**
     * Gère la logique d'authentification lorsque l'utilisateur clique sur
     * "Connexion". Cette méthode vérifie les identifiants pour un client ou un
     * membre du personnel et redirige vers l'interface appropriée.
     */
    private void actionConnexion() {
        // 1. Récupération des informations saisies par l'utilisateur.
        String identifiant = utilisateurField.getText().trim();
        String motDePasse = new String(passwordField.getPassword());

        // 2. Validation de base : on s'assure que les champs ne sont pas vides.
        if (identifiant.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return; // On arrête la méthode ici.
        }

        // 3. On détermine si la tentative de connexion concerne un client ou un membre du personnel.
        if (clientRadio.isSelected()) {
            // --- LOGIQUE POUR UN CLIENT ---

            // On vérifie le format de l'email.
            if (!emailValide(identifiant)) {
                JOptionPane.showMessageDialog(this, "L'identifiant client doit être une adresse e-mail valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // On demande au service d'authentifier le client.
            Client client = clientService.authentifierClient(identifiant, motDePasse);
            if (client != null) {
                this.dispose();
                new FenetrePrincipaleClient(clientService, client).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Email ou mot de passe client incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            
            // --- LOGIQUE POUR UN MEMBRE DU PERSONNEL ---

            Personnel personnel = adminService.authentifierPersonnel(identifiant, motDePasse);
            if (personnel != null) {
                Role role = adminService.trouverRoleParId(personnel.getIdRole());
                this.dispose(); // On ferme la fenêtre de connexion dans tous les cas.

                // On vérifie si l'employé a le rôle "Administrateur".
                if (role != null && role.getLibelle().equalsIgnoreCase("Administrateur")) {
                    // Cas 1 : L'employé est administrateur -> accès au back-office.
                    new FenetreAdmin(adminService, personnel).setVisible(true);
                } else {
                    // Cas 2 : Pour tous les autres rôles on redirige vers l'interface la plus restrictive, le point de vente.
                    new PointDeVente(adminService, personnel).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Identifiant ou mot de passe du personnel incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Ouvre la fenêtre d'inscription.
     */
    private void actionInscription() {
        this.setVisible(false);
        FenetreInscription inscriptionDialog = new FenetreInscription(this, clientService, true);
        inscriptionDialog.setVisible(true);
        this.setVisible(true);
    }

    /**
     * Gère la procédure de réinitialisation du mot de passe.
     */
    private void actionMotDePasseOublie() {
        String email = JOptionPane.showInputDialog(
                this,
                "Veuillez saisir l'adresse e-mail de votre compte :",
                "Récupération de mot de passe",
                JOptionPane.QUESTION_MESSAGE
        );

        if (email == null || email.trim().isEmpty()) {
            return; 
        }

        email = email.trim();
        if (!emailValide(email)) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une adresse email valide.", "Format invalide", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Client client = clientService.trouverClientParEmail(email);
        Personnel personnel = adminService.trouverPersonnelParEmail(email);

        if (client == null && personnel == null) {
            JOptionPane.showMessageDialog(this, "Aucun compte n'est associé à cette adresse e-mail.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ReinitialiserMotDePasse dialog = new ReinitialiserMotDePasse(this, true);
        String nouveauMotDePasse = dialog.afficherDialogue();

        if (nouveauMotDePasse != null) { 
            try {
                if (client != null) {
                    clientService.changerMotDePasseClient(client.getId(), nouveauMotDePasse);
                } else { 
                    adminService.changerMotDePassePersonnel(personnel.getId(), nouveauMotDePasse);
                }
                JOptionPane.showMessageDialog(this, "Votre mot de passe a été mis à jour avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue lors de la mise à jour : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Fonction utilitaire pour valider le format d'un email
     */
    private boolean emailValide(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        panneauPrincipal = new javax.swing.JPanel();
        panneauBoutons = new javax.swing.JPanel();
        motOublieButton = new javax.swing.JButton();
        inscriptionButton = new javax.swing.JButton();
        connexionButton = new javax.swing.JButton();
        panneauFormulaire = new javax.swing.JPanel();
        labelsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fieldsPanel = new javax.swing.JPanel();
        utilisateurField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        radioPanel = new javax.swing.JPanel();
        clientRadio = new javax.swing.JRadioButton();
        adminRadio = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panneauPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panneauPrincipal.setLayout(new java.awt.BorderLayout());

        motOublieButton.setText("Mot de passe oublié ?");
        motOublieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motOublieButtonActionPerformed(evt);
            }
        });
        panneauBoutons.add(motOublieButton);

        inscriptionButton.setText("S'inscrire");
        inscriptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inscriptionButtonActionPerformed(evt);
            }
        });
        panneauBoutons.add(inscriptionButton);

        connexionButton.setText("Connexion");
        connexionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connexionButtonActionPerformed(evt);
            }
        });
        panneauBoutons.add(connexionButton);

        panneauPrincipal.add(panneauBoutons, java.awt.BorderLayout.PAGE_END);

        panneauFormulaire.setLayout(new java.awt.BorderLayout());

        labelsPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 12));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Email : ");
        labelsPanel.add(jLabel1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Mot de passe : ");
        labelsPanel.add(jLabel2);

        panneauFormulaire.add(labelsPanel, java.awt.BorderLayout.LINE_START);

        fieldsPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 5));
        fieldsPanel.add(utilisateurField);
        fieldsPanel.add(passwordField);

        radioPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        buttonGroup1.add(clientRadio);
        clientRadio.setSelected(true);
        clientRadio.setText("Client");
        radioPanel.add(clientRadio);

        buttonGroup1.add(adminRadio);
        adminRadio.setText("Personnel");
        radioPanel.add(adminRadio);

        fieldsPanel.add(radioPanel);

        panneauFormulaire.add(fieldsPanel, java.awt.BorderLayout.CENTER);

        panneauPrincipal.add(panneauFormulaire, java.awt.BorderLayout.CENTER);

        getContentPane().add(panneauPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inscriptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inscriptionButtonActionPerformed
        actionInscription();// TODO add your handling code here:
    }//GEN-LAST:event_inscriptionButtonActionPerformed

    private void connexionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connexionButtonActionPerformed
        actionConnexion();// TODO add your handling code here:
    }//GEN-LAST:event_connexionButtonActionPerformed

    private void motOublieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_motOublieButtonActionPerformed
        // 1. Demander à l'utilisateur quel type de compte il veut réinitialiser.
        actionMotDePasseOublie();
        // Si l'utilisateur ferme la boîte, il ne se passe rien.// TODO add your handling code here:
    }//GEN-LAST:event_motOublieButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton adminRadio;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JRadioButton clientRadio;
    private javax.swing.JButton connexionButton;
    private javax.swing.JPanel fieldsPanel;
    private javax.swing.JButton inscriptionButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel labelsPanel;
    private javax.swing.JButton motOublieButton;
    private javax.swing.JPanel panneauBoutons;
    private javax.swing.JPanel panneauFormulaire;
    private javax.swing.JPanel panneauPrincipal;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JPanel radioPanel;
    private javax.swing.JTextField utilisateurField;
    // End of variables declaration//GEN-END:variables
}
