package com.mycompany.cinema.view;

import com.mycompany.cinema.EvaluationClient;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

/**
 * Mon binôme : Fenêtre de dialogue modale qui permet au client de laisser un avis
 * sur un film (une note de 1 à 5 et un commentaire).
 * Elle est appelée depuis le panneau de détail d'un film.
 */
public class EvaluationDialog extends JDialog {

    private final ClientService clientService;
    private final int clientId;
    private final int filmId;

    // Composants de l'interface qu'on doit pouvoir accéder dans plusieurs méthodes.
    private ButtonGroup ratingGroup; // Pour gérer les boutons radio de la note.
    private JTextArea commentaireArea; // Pour récupérer le texte du commentaire.

    /**
     * Constructeur de la fenêtre.
     * @param owner Fenêtre parente.
     * @param clientService Le service pour envoyer l'évaluation.
     * @param clientId L'ID du client connecté.
     * @param filmId L'ID du film concerné.
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
        // --- Panneau pour la note (boutons radio en forme d'étoiles) ---
        JPanel ratingPanel = new JPanel();
        ratingPanel.setBorder(BorderFactory.createTitledBorder("Votre note"));
        ratingGroup = new ButtonGroup(); // Garantit qu'un seul bouton radio peut être sélectionné à la fois.
        for (int i = 1; i <= 5; i++) {
            JRadioButton starButton = new JRadioButton(i + " ★");
            // setActionCommand est une astuce pour stocker une valeur simple (la note) dans le bouton.
            starButton.setActionCommand(String.valueOf(i));
            ratingGroup.add(starButton);
            ratingPanel.add(starButton);
        }

        // --- Zone de texte pour le commentaire ---
        JPanel commentPanel = new JPanel(new BorderLayout());
        commentPanel.setBorder(BorderFactory.createTitledBorder("Votre commentaire (optionnel)"));
        commentaireArea = new JTextArea();
        commentaireArea.setLineWrap(true); // Le texte revient à la ligne automatiquement.
        commentaireArea.setWrapStyleWord(true); // Coupe les lignes aux espaces.
        commentPanel.add(new JScrollPane(commentaireArea), BorderLayout.CENTER);

        // --- Panneau pour les boutons "Valider" et "Annuler" ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton validerButton = new JButton("Valider");
        JButton annulerButton = new JButton("Annuler");
        buttonPanel.add(validerButton);
        buttonPanel.add(annulerButton);

        // --- Assemblage final des panneaux dans la fenêtre ---
        add(ratingPanel, BorderLayout.NORTH);
        add(commentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Ajout des écouteurs d'actions ---
        annulerButton.addActionListener(e -> dispose()); // Ferme la fenêtre sans rien faire.

        validerButton.addActionListener(e -> handleValidation()); // Appelle la méthode de validation.
    }

    /**
     * Gère la logique lorsque l'utilisateur clique sur "Valider".
     */
    private void handleValidation() {
        // On vérifie d'abord que l'utilisateur a bien sélectionné une note.
        if (ratingGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une note.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return; // On arrête l'exécution de la méthode.
        }

        // On récupère les données saisies par l'utilisateur.
        int note = Integer.parseInt(ratingGroup.getSelection().getActionCommand());
        String commentaire = commentaireArea.getText();

        // On crée un objet "modèle" EvaluationClient avec ces données.
        EvaluationClient evaluation = new EvaluationClient(clientId, filmId, note, commentaire, LocalDateTime.now());

        try {
            // On passe cet objet au service pour qu'il l'enregistre.
            clientService.ajouterEvaluation(evaluation);
            JOptionPane.showMessageDialog(this, "Merci pour votre avis !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // On ferme la fenêtre en cas de succès.
        } catch (Exception ex) {
            // Si le service renvoie une erreur, on l'affiche.
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}