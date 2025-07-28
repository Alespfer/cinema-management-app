package com.mycompany.cinema.view;

import com.mycompany.cinema.EvaluationClient;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

/**
 * Fenêtre de dialogue permettant à un client de noter un film et de laisser un commentaire.
 * Appelée depuis le FilmDetailPanel.
 */
public class EvaluationDialog extends JDialog {

    private final ClientService clientService;
    private final int clientId;
    private final int filmId;

    private ButtonGroup ratingGroup;
    private JTextArea commentaireArea;

    /**
     * Constructeur de la fenêtre d'évaluation.
     * @param owner La fenêtre parente (pour le comportement modal).
     * @param clientService L'instance du service pour la logique métier.
     * @param clientId L'ID du client qui évalue.
     * @param filmId L'ID du film à évaluer.
     */
    public EvaluationDialog(Frame owner, ClientService clientService, int clientId, int filmId) {
        super(owner, "Donner votre avis", true);
        this.clientService = clientService;
        this.clientId = clientId;
        this.filmId = filmId;

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        initComponents();
    }

    private void initComponents() {
        // --- Panneau pour la note (étoiles) ---
        JPanel ratingPanel = new JPanel();
        ratingPanel.setBorder(BorderFactory.createTitledBorder("Votre note"));
        ratingGroup = new ButtonGroup();
        for (int i = 1; i <= 5; i++) {
            JRadioButton starButton = new JRadioButton(i + " ★");
            starButton.setActionCommand(String.valueOf(i)); // Stocke la note (1, 2, 3...)
            ratingGroup.add(starButton);
            ratingPanel.add(starButton);
        }

        // --- Panneau pour le commentaire ---
        JPanel commentPanel = new JPanel(new BorderLayout());
        commentPanel.setBorder(BorderFactory.createTitledBorder("Votre commentaire (optionnel)"));
        commentaireArea = new JTextArea();
        commentaireArea.setLineWrap(true);
        commentaireArea.setWrapStyleWord(true);
        commentPanel.add(new JScrollPane(commentaireArea), BorderLayout.CENTER);

        // --- Panneau des boutons d'action ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton validerButton = new JButton("Valider");
        JButton annulerButton = new JButton("Annuler");
        buttonPanel.add(validerButton);
        buttonPanel.add(annulerButton);

        // --- Assemblage ---
        add(ratingPanel, BorderLayout.NORTH);
        add(commentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Listeners (conformes aux contraintes) ---
        annulerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre.
            }
        });

        validerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleValidation();
            }
        });
    }

    private void handleValidation() {
        // Validation : une note doit être sélectionnée.
        if (ratingGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une note.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int note = Integer.parseInt(ratingGroup.getSelection().getActionCommand());
        String commentaire = commentaireArea.getText();

        EvaluationClient evaluation = new EvaluationClient(clientId, filmId, note, commentaire, LocalDateTime.now());

        try {
            clientService.ajouterEvaluation(evaluation);
            JOptionPane.showMessageDialog(this, "Merci pour votre avis !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Ferme la fenêtre après succès.
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}