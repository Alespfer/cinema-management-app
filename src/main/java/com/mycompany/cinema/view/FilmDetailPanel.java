package com.mycompany.cinema.view;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Ce panneau affiche les informations détaillées d'un film (synopsis, affiche)
 * ainsi que la liste des séances disponibles pour ce film à une date donnée.
 * Il sert d'étape intermédiaire entre la programmation générale et la sélection des sièges.
 */
public class FilmDetailPanel extends JPanel {

    private final ClientService clientService;

    // Composants pour les informations du film
    private JLabel titleLabel;
    private JLabel posterLabel;
    private JTextArea synopsisArea;
    private JLabel infoLabel;

    // Composants pour la liste des séances du film
    private JList<Seance> seanceJList;
    private DefaultListModel<Seance> seanceListModel;

    // Interfaces pour la communication avec le parent (ClientMainFrame)
    public interface SeanceSelectionListener {
        void onSeanceSelected(Seance seance);
    }
    private SeanceSelectionListener seanceSelectionListener;
    
    public interface RetourListener {
        void onRetourClicked();
    }
    private RetourListener retourListener;

    public FilmDetailPanel(ClientService clientService) {
        this.clientService = clientService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PARTIE GAUCHE : Affiche et bouton Retour ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton retourButton = new JButton("<< Retour à la programmation");
        leftPanel.add(posterLabel, BorderLayout.CENTER);
        leftPanel.add(retourButton, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);

        // --- PARTIE CENTRALE : Informations et liste des séances ---
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Panneau pour le titre et les métadonnées
        JPanel northPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Titre du film", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        infoLabel = new JLabel("Durée | Classification", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(infoLabel, BorderLayout.CENTER);

        // Panneau pour le synopsis
        synopsisArea = new JTextArea("Synopsis...");
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setLineWrap(true);
        synopsisArea.setEditable(false);
        synopsisArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane synopsisScrollPane = new JScrollPane(synopsisArea);
        synopsisScrollPane.setPreferredSize(new Dimension(100, 150)); // Hauteur préférée

        // Panneau pour la liste des séances
        seanceListModel = new DefaultListModel<>();
        seanceJList = new JList<>(seanceListModel);
        seanceJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane seanceScrollPane = new JScrollPane(seanceJList);
        seanceScrollPane.setBorder(BorderFactory.createTitledBorder("Choisissez un horaire pour ce film"));

        // Assemblage du panneau central
        centerPanel.add(northPanel, BorderLayout.NORTH);
        centerPanel.add(synopsisScrollPane, BorderLayout.CENTER);
        centerPanel.add(seanceScrollPane, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);

        // --- LISTENERS ---
        // Attacher l'action au bouton de retour
        retourButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (retourListener != null) {
                    retourListener.onRetourClicked();
                }
            }
        });

        // Attacher l'action à la sélection d'une séance dans la liste
        seanceJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && seanceJList.getSelectedValue() != null && seanceSelectionListener != null) {
                    seanceSelectionListener.onSeanceSelected(seanceJList.getSelectedValue());
                }
            }
        });
        
        // Personnalisation de l'affichage de la liste des séances
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
    }

    /**
     * Méthode publique pour mettre à jour tout le panneau avec les informations d'un film
     * et charger les séances correspondantes pour une date donnée.
     * @param film Le film à afficher.
     * @param date La date pour laquelle chercher les séances.
     */
    public void displayFilmAndSeances(Film film, LocalDate date) {
        if (film != null) {
            // Mise à jour des informations du film
            titleLabel.setText(film.getTitre());
            infoLabel.setText("Durée: " + film.getDureeMinutes() + " min | Classification: " + film.getClassification());
            synopsisArea.setText(film.getSynopsis());
            
            // Gestion de l'affiche (à adapter avec le chemin de vos images)
            ImageIcon posterIcon = new ImageIcon("images/" + film.getUrlAffiche());
            // Redimensionnement de l'image
            Image image = posterIcon.getImage().getScaledInstance(300, 450, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(image));

            // Chargement des séances pour ce film à cette date
            seanceListModel.clear();
            List<Seance> seances = clientService.getSeancesPourFilmEtDate(film.getId(), date);
            seanceListModel.addAll(seances);
        } else {
            // État par défaut si aucun film n'est fourni
            clearPanel();
        }
    }

    /**
     * Réinitialise le panneau à son état vide.
     */
    public void clearPanel() {
        titleLabel.setText("Aucun film sélectionné");
        infoLabel.setText("");
        synopsisArea.setText("");
        posterLabel.setIcon(null);
        seanceListModel.clear();
    }

    // Méthodes pour permettre au parent de s'enregistrer comme écouteur
    public void setSeanceSelectionListener(SeanceSelectionListener listener) {
        this.seanceSelectionListener = listener;
    }

    public void setRetourListener(RetourListener listener) {
        this.retourListener = listener;
    }
}