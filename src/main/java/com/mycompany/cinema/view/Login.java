package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.Role;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.service.ClientService;
import com.mycompany.cinema.service.impl.CinemaServiceImpl;
import com.mycompany.cinema.view.admin.AdminMain;
import com.mycompany.cinema.view.admin.PointDeVente;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    private final ClientService clientService;
    private final AdminService adminService;

    public Login() {
        CinemaServiceImpl serviceImpl = new CinemaServiceImpl();
        this.clientService = serviceImpl;
        this.adminService = serviceImpl;

        initComponents();

        setTitle("Connexion - Alespfer Cinema");
        setLocationRelativeTo(null);

    }

    private void actionConnexion() {
        String user = utilisateurField.getText();
        String password = new String(passwordField.getPassword());

        if (user.isEmpty() || password.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur de saisie", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (clientRadio.isSelected()) {
            // CORRECTION : La méthode authentifierClient retourne maintenant un Client ou null.
            Client client = clientService.authentifierClient(user, password);

            // CORRECTION : La vérification se fait avec '!= null'.
            if (client != null) {
                this.dispose();
                // CORRECTION : On passe l'objet 'client' directement. L'appel '.get()' était une erreur et a été supprimé.
                new ClientMain(clientService, client).setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Email ou mot de passe client incorrect.", "Erreur d'authentification", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // CORRECTION : La méthode authentifierPersonnel retourne maintenant un Personnel ou null.
            Personnel personnel = adminService.authentifierPersonnel(user, password);

            // CORRECTION : La vérification se fait avec '!= null'.
            if (personnel != null) {
                Role role = null;
                for (Role r : adminService.getAllRoles()) {
                    if (r.getId() == personnel.getIdRole()) {
                        role = r;
                        break;
                    }
                }
                this.dispose();
                if (role != null && role.getLibelle().equalsIgnoreCase("Vendeur")) {
                    new PointDeVente(adminService, personnel).setVisible(true);
                } else {
                    new AdminMain(adminService, personnel).setVisible(true);
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe du personnel incorrect.", "Erreur d'authentification", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actionInscription() {
        // ORDRE N°1 : Cacher le quartier-général (la fenêtre de login).
        this.setVisible(false);

        // ORDRE N°2 : Déployer l'unité d'inscription (votre code existant).
        // Le programme attendra ici que la fenêtre se ferme.
        Inscription inscriptionDialog = new Inscription(this, clientService, true);
        inscriptionDialog.setVisible(true);

        // ORDRE N°3 : Une fois la mission d'inscription terminée (succès ou annulation),
        // redéployer le quartier-général.
        this.setVisible(true);
    }

    // Copiez cette méthode (de la réponse précédente) dans votre classe Login.java
    private void handleForgotPassword() {
        String email = JOptionPane.showInputDialog(
                this,
                "Veuillez saisir votre adresse e-mail :",
                "Récupération de mot de passe",
                JOptionPane.QUESTION_MESSAGE
        );

        if (email == null || email.trim().isEmpty()) {
            return;
        }

        // Assurez-vous d'avoir une variable 'clientService' dans votre classe Login
        Client client = clientService.getClientByEmail(email.trim());

        if (client != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Un e-mail de réinitialisation a été simulé.\nVotre mot de passe est : " + client.getMotDePasse(),
                    "Mot de passe récupéré",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Aucun compte n'est associé à cette adresse e-mail.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        panneauPrincipal = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        forgotPassWordButton = new javax.swing.JButton();
        inscriptionButton = new javax.swing.JButton();
        connexionButton = new javax.swing.JButton();
        formPanel = new javax.swing.JPanel();
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

        forgotPassWordButton.setText("Mot de passe oublié ?");
        forgotPassWordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgotPassWordButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(forgotPassWordButton);

        inscriptionButton.setText("S'inscrire");
        inscriptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inscriptionButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(inscriptionButton);

        connexionButton.setText("Connexion");
        connexionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connexionButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(connexionButton);

        panneauPrincipal.add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        formPanel.setLayout(new java.awt.BorderLayout());

        labelsPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 12));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Utilisateur/Email : ");
        labelsPanel.add(jLabel1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Mot de passe : ");
        labelsPanel.add(jLabel2);

        formPanel.add(labelsPanel, java.awt.BorderLayout.LINE_START);

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

        formPanel.add(fieldsPanel, java.awt.BorderLayout.CENTER);

        panneauPrincipal.add(formPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(panneauPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inscriptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inscriptionButtonActionPerformed
        actionInscription();// TODO add your handling code here:
    }//GEN-LAST:event_inscriptionButtonActionPerformed

    private void connexionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connexionButtonActionPerformed
        actionConnexion();// TODO add your handling code here:
    }//GEN-LAST:event_connexionButtonActionPerformed

    private void forgotPassWordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgotPassWordButtonActionPerformed
        handleForgotPassword();// TODO add your handling code here:
    }//GEN-LAST:event_forgotPassWordButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton adminRadio;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JRadioButton clientRadio;
    private javax.swing.JButton connexionButton;
    private javax.swing.JPanel fieldsPanel;
    private javax.swing.JButton forgotPassWordButton;
    private javax.swing.JPanel formPanel;
    private javax.swing.JButton inscriptionButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel labelsPanel;
    private javax.swing.JPanel panneauPrincipal;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JPanel radioPanel;
    private javax.swing.JTextField utilisateurField;
    // End of variables declaration//GEN-END:variables
}
