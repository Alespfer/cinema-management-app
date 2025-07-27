package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.service.ClientService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Panneau permettant à l'utilisateur de modifier ses informations personnelles
 * (nom, mot de passe) et de supprimer son compte.
 */
public class InfosPersonnellesPanel extends JPanel {

    private final ClientService clientService;
    private final Client clientConnecte;
    private final JDialog parentDialog;

    private JTextField nomField;
    private JPasswordField passField;
    private JPasswordField confirmPassField;

    public InfosPersonnellesPanel(ClientService clientService, Client clientConnecte, JDialog parentDialog) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;
        this.parentDialog = parentDialog;
        
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panneau de formulaire avec un GridBagLayout pour un alignement propre.
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        nomField = new JTextField(20);
        nomField.setText(clientConnecte.getNom()); // Pré-remplissage avec le nom actuel.
        passField = new JPasswordField(20);
        confirmPassField = new JPasswordField(20);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(nomField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; formPanel.add(new JLabel("Nouveau mot de passe :"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(passField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; formPanel.add(new JLabel("Confirmer le mot de passe :"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(confirmPassField, gbc);

        add(formPanel, BorderLayout.NORTH);
        
        // Panneau pour les boutons d'action.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton updateButton = new JButton("Mettre à jour");
        JButton deleteButton = new JButton("Supprimer mon compte");
        deleteButton.setBackground(Color.RED); // Mise en évidence du danger.
        deleteButton.setForeground(Color.WHITE);
        
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleUpdate();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleDelete();
            }
        });
    }

    private void handleUpdate() {
        // Validation : Le nom ne doit pas être vide.
        if (nomField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom ne peut pas être vide.", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mise à jour du nom.
        clientConnecte.setNom(nomField.getText().trim());

        // Logique de mise à jour du mot de passe (uniquement si les champs ne sont pas vides).
        char[] newPass = passField.getPassword();
        char[] confirmPass = confirmPassField.getPassword();
        
        if (newPass.length > 0) {
            if (!Arrays.equals(newPass, confirmPass)) {
                JOptionPane.showMessageDialog(this, "Les nouveaux mots de passe ne correspondent pas.", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
                return;
            }
            clientConnecte.setMotDePasse(new String(newPass));
        }

        try {
            clientService.modifierCompteClient(clientConnecte);
            JOptionPane.showMessageDialog(this, "Informations mises à jour avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            parentDialog.dispose(); // Ferme la fenêtre après succès.
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int response = JOptionPane.showConfirmDialog(this, 
                "ATTENTION : Cette action est irréversible et supprimera votre compte\net tout votre historique de réservations.\nÊtes-vous absolument sûr de vouloir continuer ?",
                "Confirmation de suppression de compte",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
        if (response == JOptionPane.YES_OPTION) {
            try {
                clientService.supprimerCompteClient(clientConnecte.getId());
                JOptionPane.showMessageDialog(null, "Votre compte a été supprimé. L'application va maintenant se fermer.", "Compte supprimé", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0); // Fermeture complète de l'application.
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}