package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.Role; // Importation nécessaire pour la redirection par rôle.
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.service.ClientService;
import com.mycompany.cinema.service.impl.CinemaServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * Fenêtre de connexion initiale de l'application.
 * Elle sert de point d'entrée et redirige l'utilisateur vers l'interface appropriée
 * (Client, Administrateur, ou Vendeur) en fonction de son authentification.
 */
public class LoginFrame extends JFrame {
    
    // Références aux deux interfaces de service.
    private final ClientService clientService;
    private final AdminService adminService;

    // Composants graphiques du formulaire.
    private JTextField userField;
    private JPasswordField passField;
    private JRadioButton clientRadio, adminRadio;

    public LoginFrame() {
        // Instanciation unique de l'implémentation du service.
        CinemaServiceImpl serviceImpl = new CinemaServiceImpl();
        this.clientService = serviceImpl;
        this.adminService = serviceImpl;

        setTitle("Connexion - Alespfer Cinema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    /**
     * Construit et organise tous les composants graphiques de la fenêtre.
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // --- Formulaire de connexion ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Ligne 0: Utilisateur/Email
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Utilisateur/Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        userField = new JTextField(20);
        formPanel.add(userField, gbc);
        
        // Ligne 1: Mot de passe
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        passField = new JPasswordField();
        formPanel.add(passField, gbc);
        
        // Ligne 2: Boutons Radio pour sélectionner le type de compte
        clientRadio = new JRadioButton("Client", true);
        adminRadio = new JRadioButton("Personnel");
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(clientRadio);
        roleGroup.add(adminRadio);
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.add(clientRadio);
        radioPanel.add(adminRadio);

        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(radioPanel, gbc);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- Panneau des boutons d'action ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton registerButton = new JButton("S'inscrire");
        JButton loginButton = new JButton("Connexion");
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Association des actions aux boutons (sans lambdas) ---
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegisterDialog registerDialog = new RegisterDialog(LoginFrame.this, clientService);
                registerDialog.setVisible(true);
            }
        });
        
        pack(); // Ajuste la taille de la fenêtre au contenu.
    }

    /**
     * Gère la logique d'authentification et de redirection après un clic sur "Connexion".
     */
    private void handleLogin() {
        String user = userField.getText();
        String password = new String(passField.getPassword());

        if (user.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (clientRadio.isSelected()) {
            // --- Logique de connexion pour un CLIENT ---
            Optional<Client> clientOpt = clientService.authentifierClient(user, password);
            
            if (clientOpt.isPresent()) {
                this.dispose(); // Ferme la fenêtre de login.
                new ClientMainFrame(clientService, clientOpt.get()).setVisible(true); // Ouvre l'interface client.
            } else {
                JOptionPane.showMessageDialog(this, "Email ou mot de passe client incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            }
        } else { 
            // --- Logique de connexion pour un MEMBRE DU PERSONNEL ---
            Optional<Personnel> personnelOpt = adminService.authentifierPersonnel(user, password);
            
            if (personnelOpt.isPresent()) {
                Personnel personnel = personnelOpt.get();
                
                // --- DÉBUT DE LA LOGIQUE DE REDIRECTION PAR RÔLE ---
                Role role = null;
                // On parcourt la liste des rôles pour trouver celui qui correspond à l'ID du personnel connecté.
                for (Role r : adminService.getAllRoles()) {
                    if (r.getId() == personnel.getIdRole()) {
                        role = r;
                        break;
                    }
                }

                this.dispose(); // Ferme la fenêtre de login.
                
                // Redirection conditionnelle basée sur le libellé du rôle.
                if (role != null && role.getLibelle().equalsIgnoreCase("Vendeur")) {
                    // Si le rôle est "Vendeur", on lance le Point de Vente.
                    new PointDeVenteFrame(adminService, personnel).setVisible(true);
                } else {
                    // Pour tous les autres rôles (ex: Administrateur), on lance le panneau d'administration complet.
                    new AdminMainFrame(adminService, personnel).setVisible(true);
                }
                // --- FIN DE LA LOGIQUE DE REDIRECTION PAR RÔLE ---

            } else {
                JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe du personnel incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}