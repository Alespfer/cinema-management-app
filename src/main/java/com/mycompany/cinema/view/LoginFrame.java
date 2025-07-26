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
        // Un panneau principal avec une bordure pour l'esthétique
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Champs de saisie
        mainPanel.add(new JLabel("Utilisateur/Email:"));
        userField = new JTextField();
        mainPanel.add(userField);

        mainPanel.add(new JLabel("Mot de passe:"));
        passField = new JPasswordField();
        mainPanel.add(passField);

        // Sélection du rôle avec des boutons radio
        clientRadio = new JRadioButton("Client", true);
        adminRadio = new JRadioButton("Admin");
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(clientRadio);
        roleGroup.add(adminRadio);
        mainPanel.add(clientRadio);
        mainPanel.add(adminRadio);
        
        // Bouton de connexion
        JButton loginButton = new JButton("Connexion");
        mainPanel.add(new JLabel()); // Espace vide pour l'alignement
        mainPanel.add(loginButton);

        // On ajoute le panneau principal à la fenêtre
        add(mainPanel);

        // On associe l'action du clic à notre méthode de gestion
        loginButton.addActionListener(e -> handleLogin());
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