package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.service.ClientService;
import com.mycompany.cinema.service.impl.CinemaServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginFrame extends JFrame {
    
    // On garde des références aux deux interfaces de service.
    private final ClientService clientService;
    private final AdminService adminService;

    // Déclaration des composants graphiques
    private JTextField userField;
    private JPasswordField passField;
    private JRadioButton clientRadio, adminRadio;

    public LoginFrame() {
        // On instancie une seule fois l'implémentation
        CinemaServiceImpl serviceImpl = new CinemaServiceImpl();
        
        // On la "cast" dans les deux types d'interfaces qu'elle implémente.
        // Cela nous garantit que nous n'utiliserons que les méthodes de l'interface appropriée.
        this.clientService = serviceImpl;
        this.adminService = serviceImpl;

        setTitle("Connexion - Cinéma PISE");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

        private void initComponents() {
        // Panneau principal avec un BorderLayout pour une structure globale (Nord, Centre, Sud...)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // --- 1. Panneau pour le formulaire (au centre du mainPanel) ---
        // On utilise GridBagLayout pour un alignement précis des colonnes
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Marge entre les composants
        gbc.anchor = GridBagConstraints.WEST; // Aligner les composants à gauche

        // Ligne 0: Utilisateur/Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Utilisateur/Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Le champ de texte prend toute la largeur
        gbc.weightx = 1.0; // Donne du "poids" à cette colonne pour qu'elle s'étende
        userField = new JTextField(20); // 20 colonnes de large par défaut
        formPanel.add(userField, gbc);
        
        // Ligne 1: Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE; // Le label ne s'étire pas
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passField = new JPasswordField();
        formPanel.add(passField, gbc);
        
        // Ligne 2: Boutons Radio
        clientRadio = new JRadioButton("Client", true);
        adminRadio = new JRadioButton("Admin");
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(clientRadio);
        roleGroup.add(adminRadio);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.add(clientRadio);
        radioPanel.add(adminRadio);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(radioPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- 2. Panneau pour les boutons (au sud du mainPanel) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Aligner les boutons à droite
        JButton registerButton = new JButton("S'inscrire");
        JButton loginButton = new JButton("Connexion");
        
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // On ajoute le panneau principal à la fenêtre
        add(mainPanel);

        // On associe les actions aux boutons
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> {
            RegisterDialog registerDialog = new RegisterDialog(this, clientService);
            registerDialog.setVisible(true);
        });
        
        // Ajuste la taille de la fenêtre à son contenu préféré
        pack();
    }

    private void handleLogin() {
        String user = userField.getText();
        String password = new String(passField.getPassword());

        if (user.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (clientRadio.isSelected()) {
            // Tentative de connexion en tant que client
            Optional<Client> clientOpt = clientService.authentifierClient(user, password);
            
            if (clientOpt.isPresent()) {
                // Succès : on ferme la fenêtre de login...
                this.dispose(); 
                // ...et on lance la fenêtre principale du client.
                ClientMainFrame frame = new ClientMainFrame(clientService, clientOpt.get());
                frame.setVisible(true);
            } else {
                // Échec : on affiche une erreur
                JOptionPane.showMessageDialog(this, "Email ou mot de passe client incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            }
        } else { // Admin radio is selected
            // Tentative de connexion en tant qu'admin
            Optional<Personnel> personnelOpt = adminService.authentifierPersonnel(user, password);
            
            if (personnelOpt.isPresent()) {
                // Succès : on ferme la fenêtre de login...
                this.dispose();
                // ...et on lance la fenêtre principale de l'admin.
                AdminMainFrame frame = new AdminMainFrame(adminService, personnelOpt.get());
                frame.setVisible(true);
            } else {
                // Échec : on affiche une erreur
                JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe admin incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}