package com.mycompany.cinema.view;

import com.mycompany.cinema.EvaluationClient;
import com.mycompany.cinema.Film;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Axelle : C'est un des panneaux les plus importants. Il affiche toutes
 * les informations détaillées d'un film sélectionné : son affiche, son synopsis,
 * les horaires disponibles pour un jour donné, et les avis des autres clients.
 */
public class FilmDetailPanel extends JPanel {

    private final ClientService clientService; // Pour interagir avec la logique métier.
    private int clientId; // ID du client connecté pour savoir s'il a déjà voté.
    private Film filmActuel; // Le film actuellement affiché.
    private LocalDate dateActuelle; // La date pour laquelle on affiche les séances.

    // Déclaration des composants graphiques pour pouvoir les mettre à jour.
    private JLabel titleLabel, posterLabel, infoLabel;
    private JTextArea synopsisArea;
    private JList<Seance> seanceJList; // La JList contient des objets Seance complets.
    private DefaultListModel<Seance> seanceListModel; // Le modèle qui gère le contenu de la JList.
    
    private JLabel notePresseLabel;
    private JLabel noteSpectateursLabel;
    private JButton noterButton;
    private JList<EvaluationClient> evaluationsJList;
    private DefaultListModel<EvaluationClient> evaluationsListModel;
    // Formatteur pour afficher les notes avec une décimale (ex: "4.5 / 5").
    private static final DecimalFormat NOTE_FORMATTER = new DecimalFormat("0.0 / 5");

    // Interfaces pour communiquer avec la fenêtre principale (ClientMainFrame).
    // Quand l'utilisateur choisit un horaire, on le notifie.
    public interface SeanceSelectionListener { void onSeanceSelected(Seance seance); }
    private SeanceSelectionListener seanceSelectionListener;
    // Quand l'utilisateur clique sur le bouton "Retour".
    public interface RetourListener { void onRetourClicked(); }
    private RetourListener retourListener;

    public FilmDetailPanel(ClientService clientService) {
        this.clientService = clientService;
        initComponents();
    }

    private void initComponents() {
        // ... (Le code d'initialisation est long mais standard : création des labels, boutons,
        // et leur placement sur le panneau avec des layouts) ...
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton retourButton = new JButton("<< Retour à la programmation");
        leftPanel.add(posterLabel, BorderLayout.CENTER);
        leftPanel.add(retourButton, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel northPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Titre du film", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        infoLabel = new JLabel("Durée | Classification", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        
        JPanel notesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        notePresseLabel = new JLabel("Presse: N/A");
        noteSpectateursLabel = new JLabel("Spectateurs: N/A");
        notePresseLabel.setFont(new Font("Arial", Font.BOLD, 16));
        noteSpectateursLabel.setFont(new Font("Arial", Font.BOLD, 16));
        notesPanel.add(notePresseLabel);
        notesPanel.add(noteSpectateursLabel);
        
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(infoLabel, BorderLayout.CENTER);
        northPanel.add(notesPanel, BorderLayout.SOUTH);

        synopsisArea = new JTextArea("Synopsis...");
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setLineWrap(true);
        synopsisArea.setEditable(false);
        synopsisArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane synopsisScrollPane = new JScrollPane(synopsisArea);
        synopsisScrollPane.setPreferredSize(new Dimension(100, 120));

        JPanel middleContentPanel = new JPanel(new BorderLayout(10, 10));
        seanceListModel = new DefaultListModel<Seance>();
        seanceJList = new JList<Seance>(seanceListModel);
        JScrollPane seanceScrollPane = new JScrollPane(seanceJList);
        seanceScrollPane.setBorder(BorderFactory.createTitledBorder("Choisissez un horaire pour ce film"));
        middleContentPanel.add(synopsisScrollPane, BorderLayout.NORTH);
        middleContentPanel.add(seanceScrollPane, BorderLayout.CENTER);
        
        JPanel evaluationsPanel = new JPanel(new BorderLayout(5, 5));
        evaluationsPanel.setBorder(BorderFactory.createTitledBorder("Avis des spectateurs"));
        evaluationsListModel = new DefaultListModel<EvaluationClient>();
        evaluationsJList = new JList<EvaluationClient>(evaluationsListModel);
        
        // --- Cell Renderer Personnalisé pour les Avis ---
        // Ce bloc permet d'afficher joliment les objets 'EvaluationClient' dans la liste.
        // Au lieu de l'affichage par défaut, on crée un HTML simple pour chaque ligne.
        evaluationsJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof EvaluationClient) {
                    EvaluationClient eval = (EvaluationClient) value;
                    String commentaire = eval.getCommentaire().isEmpty() ? "<i>(pas de commentaire)</i>" : eval.getCommentaire();
                    setText("<html><b>" + eval.getNote() + "/5 ★</b> - " + commentaire + "</html>");
                }
                return this;
            }
        });
        evaluationsPanel.add(new JScrollPane(evaluationsJList), BorderLayout.CENTER);
        
        noterButton = new JButton("Donner une note");
        evaluationsPanel.add(noterButton, BorderLayout.SOUTH);

        centerPanel.add(northPanel, BorderLayout.NORTH);
        centerPanel.add(middleContentPanel, BorderLayout.CENTER);
        centerPanel.add(evaluationsPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);

        // --- Listeners pour la navigation et les actions ---
        retourButton.addActionListener(e -> {
            if (retourListener != null) retourListener.onRetourClicked();
        });

        seanceJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && seanceJList.getSelectedValue() != null && seanceSelectionListener != null) {
                seanceSelectionListener.onSeanceSelected(seanceJList.getSelectedValue());
            }
        });
        
        // Personnalise l'affichage de chaque séance dans la JList pour n'afficher que l'heure et la salle.
        seanceJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Seance) {
                    Seance seance = (Seance) value;
                    setText("Horaire : " + seance.getDateHeureDebut().format(DateTimeFormatter.ofPattern("HH:mm")) + " - Salle " + seance.getIdSalle());
                }
                return this;
            }
        });
        
        // Ouvre la boîte de dialogue d'évaluation quand on clique sur le bouton "Noter".
        noterButton.addActionListener(e -> {
            if (filmActuel != null) {
                EvaluationDialog dialog = new EvaluationDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this), 
                    clientService, 
                    clientId, 
                    filmActuel.getId()
                );
                dialog.setVisible(true);
                // Après la fermeture du dialogue, on rafraîchit l'affichage au cas où une nouvelle note a été ajoutée.
                displayFilmAndSeances(filmActuel, dateActuelle, clientId);
            }
        });
    }

    /**
     * C'est la méthode publique principale de ce panneau.
     * La fenêtre parente l'appelle pour mettre à jour TOUT l'affichage avec les
     * informations d'un nouveau film.
     * @param film Le film à afficher.
     * @param date La date pour laquelle on doit lister les séances.
     * @param connectedClientId L'ID du client connecté.
     */
    public void displayFilmAndSeances(Film film, LocalDate date, int connectedClientId) {
        this.filmActuel = film;
        this.clientId = connectedClientId;
        this.dateActuelle = date;

        if (film != null) {
            // Mise à jour de tous les composants graphiques avec les données du film.
            titleLabel.setText(film.getTitre());
            infoLabel.setText("Durée: " + film.getDureeMinutes() + " min | Classification: " + film.getClassification());
            synopsisArea.setText(film.getSynopsis());
            // Chargement et redimensionnement de l'affiche.
            ImageIcon posterIcon = new ImageIcon("images/" + film.getUrlAffiche());
            Image image = posterIcon.getImage().getScaledInstance(300, 450, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(image));
            
            // Calcul et affichage des notes.
            notePresseLabel.setText("Presse: " + NOTE_FORMATTER.format(film.getNotePresse()));
            double moyenneSpectateurs = clientService.getNoteMoyenneSpectateurs(film.getId());
            noteSpectateursLabel.setText("Spectateurs: " + (moyenneSpectateurs > 0 ? NOTE_FORMATTER.format(moyenneSpectateurs) : "N/A"));

            // Chargement des avis des spectateurs.
            evaluationsListModel.clear();
            List<EvaluationClient> evaluations = clientService.getEvaluationsByFilmId(film.getId());
            Collections.reverse(evaluations); // Pour afficher les plus récents en premier.
            for (EvaluationClient eval : evaluations) {
                evaluationsListModel.addElement(eval);
            }

            // Vérifie si le client a déjà noté le film pour activer/désactiver le bouton "Noter".
            try {
                boolean aEvalue = clientService.aDejaEvalue(this.clientId, film.getId());
                noterButton.setEnabled(!aEvalue);
                noterButton.setToolTipText(aEvalue ? "Vous avez déjà noté ce film." : "Donnez votre avis sur ce film.");
            } catch (Exception e) {
                noterButton.setEnabled(false);
                e.printStackTrace();
            }
            
            // Chargement des séances pour le film et la date donnés.
            seanceListModel.clear();
            List<Seance> seances = clientService.getSeancesPourFilmEtDate(film.getId(), date);
            for(Seance seance : seances) {
                seanceListModel.addElement(seance);
            }

        } else {
            // Si aucun film n'est fourni, on vide le panneau.
            clearPanel();
        }
    }

    /**
     * Réinitialise le panneau à son état par défaut.
     */
    public void clearPanel() {
        titleLabel.setText("Aucun film sélectionné");
        infoLabel.setText("");
        synopsisArea.setText("");
        posterLabel.setIcon(null);
        seanceListModel.clear();
        notePresseLabel.setText("Presse: N/A");
        noteSpectateursLabel.setText("Spectateurs: N/A");
        evaluationsListModel.clear();
        noterButton.setEnabled(false);
    }
    
    // Méthodes pour que la fenêtre parente puisse "brancher" ses écouteurs sur ce panneau.
    public void setSeanceSelectionListener(SeanceSelectionListener listener) { this.seanceSelectionListener = listener; }
    public void setRetourListener(RetourListener listener) { this.retourListener = listener; }
}