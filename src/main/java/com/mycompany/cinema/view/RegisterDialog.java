package com.mycompany.cinema.view;

import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {

    private final ClientService clientService;

    // Champs du formulaire
    private JTextField nomField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterDialog(Frame owner, ClientService clientService) {
        // Le constructeur JDialog prend la fenêtre "parente" (owner)
        // et un booléen "modal" (true signifie qu'elle bloque la fenêtre parente)
        super(owner, "Création de Compte Client", true);
        
        this.clientService = clientService;
        
        setSize(400, 250);
        setLocationRelativeTo(owner); // Centrer par rapport à la fenêtre de connexion
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nomField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        panel.add(new JLabel("Nom complet:"));
        panel.add(nomField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Mot de passe:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirmer le mot de passe:"));
        panel.add(confirmPasswordField);

        JButton registerButton = new JButton("S'inscrire");
        JButton cancelButton = new JButton("Annuler");
        
        panel.add(registerButton);
        panel.add(cancelButton);
        
        add(panel);

        // Action du bouton "Annuler" : ferme simplement la fenêtre
        cancelButton.addActionListener(e -> dispose());
        
        // Action du bouton "S'inscrire"
        registerButton.addActionListener(e -> handleRegister());
    }

    private void handleRegister() {
        String nom = nomField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // --- Vérifications de base dans la vue ---
        if (nom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // On appelle la méthode du service pour créer le compte
            clientService.creerCompteClient(nom, email, password);
            
            // Si ça réussit, on affiche un message de succès et on ferme la fenêtre
            JOptionPane.showMessageDialog(this, "Compte créé avec succès ! Vous pouvez maintenant vous connecter.", "Inscription réussie", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Ferme la boîte de dialogue
            
        } catch (Exception ex) {
            // Si le service lance une exception (ex: email déjà utilisé), on l'affiche
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}