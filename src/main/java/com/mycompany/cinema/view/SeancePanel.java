package com.mycompany.cinema.view;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panneau destiné à afficher la liste des séances pour un film préalablement sélectionné.
 * Il permet à l'utilisateur de choisir un horaire spécifique.
 * 
 * NOTE D'OBSOLESCENCE : Dans l'architecture actuelle du projet, ce panneau est
 * remplacé par le composant plus puissant 'ProgrammationPanel.java'.
 * Ce code est fourni corrigé et commenté à des fins de référence ou de réutilisation potentielle,
 * mais il n'est plus activement utilisé dans la navigation principale de l'interface client.
 */
public class SeancePanel extends JPanel {

    // --- CHAMPS DE LA CLASSE ---

    // Le service contenant la logique métier (accès aux données).
    private final ClientService clientService;
    
    // Le composant JList qui affichera visuellement la liste des objets Seance.
    private JList<Seance> seanceJList;
    
    // Le modèle de données sous-jacent à la JList. C'est cet objet que l'on manipule pour ajouter/supprimer des éléments.
    private DefaultListModel<Seance> seanceListModel;

    /**
     * Contrat (interface) pour notifier le composant parent (anciennement ClientMainFrame)
     * qu'une séance a été sélectionnée dans la liste.
     */
    public interface SeanceSelectionListener {
        void onSeanceSelected(Seance seance);
    }
    private SeanceSelectionListener selectionListener;

    /**
     * Constructeur du panneau.
     * @param clientService L'instance du service, fournie par le parent.
     */
    public SeancePanel(ClientService clientService) {
        this.clientService = clientService;
        initComponents();
    }

    /**
     * Construit et organise tous les composants graphiques du panneau.
     */
    private void initComponents() {
        // Le panneau utilise un BorderLayout pour que la liste prenne toute la place disponible.
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Séances du jour"));
        
        // Initialisation du modèle et de la JList qui l'utilisera.
        seanceListModel = new DefaultListModel<>();
        seanceJList = new JList<>(seanceListModel);
        
        // --- CORRECTION SYNTAXIQUE ---
        // Le listener de sélection est implémenté avec une classe anonyme pour respecter les contraintes du projet.
        seanceJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                // La condition 'getValueIsAdjusting' est cruciale. Elle est 'true' tant que l'utilisateur
                // déplace la souris en cliquant. L'événement n'est traité qu'une seule fois,
                // lorsque la souris est relâchée (la valeur est alors 'false').
                if (!e.getValueIsAdjusting() && selectionListener != null) {
                    Seance selectedSeance = seanceJList.getSelectedValue();
                    // On notifie le parent de la sélection.
                    selectionListener.onSeanceSelected(selectedSeance);
                }
            }
        });

        // Le "renderer" personnalise l'affichage des objets Seance dans la JList.
        // Sans lui, Swing afficherait le résultat de la méthode .toString() de l'objet.
        seanceJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // On récupère le rendu par défaut pour bénéficier des couleurs de sélection, etc.
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                // On vérifie que l'objet à afficher est bien une Seance.
                if (value instanceof Seance) {
                    Seance seance = (Seance) value;
                    
                    // Formatage de la date pour n'afficher que l'heure et les minutes.
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    
                    // On définit le texte à afficher. Amélioration possible : récupérer le nom de la salle.
                    setText(seance.getDateHeureDebut().format(formatter) + " - Salle " + seance.getIdSalle());
                }
                return c; // On retourne le composant modifié.
            }
        });
        
        // On ajoute la JList (placée dans un JScrollPane pour la barre de défilement) au panneau.
        add(new JScrollPane(seanceJList), BorderLayout.CENTER);
    }

    /**
     * Méthode publique appelée par le parent pour peupler la liste des séances
     * en fonction d'un film sélectionné.
     * @param film Le film pour lequel on veut afficher les séances.
     */
    public void loadSeances(Film film) {
        seanceListModel.clear(); // Vider la liste avant de la remplir.
        if (film != null) {
            // Dans cette version, la date est codée en dur (aujourd'hui).
            // Une version plus complète utiliserait un sélecteur de date (JDatePicker).
            List<Seance> seances = clientService.getSeancesPourFilmEtDate(film.getId(), LocalDate.now());
            // Ajoute tous les éléments de la liste 'seances' au modèle de la JList.
            seanceListModel.addAll(seances);
        }
    }
    
    /**
     * Permet au composant parent de s'enregistrer pour être notifié des sélections.
     * @param listener L'objet qui implémente l'interface SeanceSelectionListener.
     */
    public void setSeanceSelectionListener(SeanceSelectionListener listener) {
        this.selectionListener = listener;
    }
}