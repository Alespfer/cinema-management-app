package com.mycompany.cinema.view;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Genre;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Ce panneau est la vue principale de l'interface client. Il affiche un tableau
 * de toutes les séances disponibles et fournit des filtres (par date, titre, genre)
 * pour permettre à l'utilisateur d'affiner sa recherche.
 * Un clic sur une séance dans ce panneau déclenche l'affichage du 'FilmDetailPanel'.
 */
public class ProgrammationPanel extends JPanel {

    // --- CHAMPS DE LA CLASSE ---
    
    // Le service contenant la logique métier (accès aux données).
    private final ClientService clientService;

    // Le tableau qui affichera les séances.
    private JTable seancesTable;
    
    // Les composants graphiques pour les filtres.
    private JComboBox<Genre> genreFilter;
    private JTextField dateFilter;
    private JTextField titreFilter;
    
    /**
     * Contrat (interface) pour notifier le composant parent (ClientMainFrame)
     * qu'une séance a été sélectionnée dans le tableau.
     */
    public interface SeanceSelectionListener {
        void onSeanceSelected(Seance seance);
    }
    private SeanceSelectionListener selectionListener;

    /**
     * Constructeur du panneau.
     * @param clientService L'instance du service, fournie par le parent.
     */
    public ProgrammationPanel(ClientService clientService) {
        this.clientService = clientService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents(); // Construit l'interface.
        loadGenres();     // Peuple le filtre des genres.
        rechercher();     // Lance une recherche initiale pour tout afficher.
    }

    /**
     * Construit et organise tous les composants graphiques du panneau.
     */
    private void initComponents() {
        // --- Panneau de filtres (en haut, BorderLayout.NORTH) ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtres"));

        dateFilter = new JTextField(10);
        titreFilter = new JTextField(20);
        genreFilter = new JComboBox<Genre>();
        JButton searchButton = new JButton("Rechercher");
        JButton resetButton = new JButton("Réinitialiser");
        
        filterPanel.add(new JLabel("Date (jj/MM/aaaa):"));
        filterPanel.add(dateFilter);
        filterPanel.add(new JLabel("Titre:"));
        filterPanel.add(titreFilter);
        filterPanel.add(new JLabel("Genre:"));
        filterPanel.add(genreFilter);
        filterPanel.add(searchButton);
        filterPanel.add(resetButton);
        add(filterPanel, BorderLayout.NORTH);

        // --- Tableau des résultats (au centre, BorderLayout.CENTER) ---
        // On initialise la table avec un modèle vide, qui sera remplacé par notre modèle personnalisé.
        String[] columnNames = {"Film", "Date", "Heure", "Salle", "Durée"};
        DefaultTableModel initialModel = new DefaultTableModel(columnNames, 0);
        seancesTable = new JTable(initialModel);
        seancesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Une seule ligne sélectionnable à la fois.
        seancesTable.setFillsViewportHeight(true); // Le tableau occupe toute la hauteur du scroll pane.
        seancesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        seancesTable.setRowHeight(25);
        add(new JScrollPane(seancesTable), BorderLayout.CENTER);

        // --- Listeners (Actions) ---
        // Attache les actions aux boutons.
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rechercher();
            }
        });
        
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Réinitialise les champs de filtre et relance une recherche vide.
                dateFilter.setText("");
                titreFilter.setText("");
                genreFilter.setSelectedIndex(0);
                rechercher();
            }
        });
        
        // Listener pour la sélection d'une ligne dans le tableau.
        seancesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && seancesTable.getSelectedRow() != -1) {
                    if (selectionListener != null) {
                        // On récupère l'index de la ligne dans le modèle (important en cas de tri).
                        int modelRow = seancesTable.convertRowIndexToModel(seancesTable.getSelectedRow());
                        // On récupère l'objet Seance complet grâce à notre modèle de table personnalisé.
                        Seance selectedSeance = ((SeanceTableModel)seancesTable.getModel()).getSeanceAt(modelRow);
                        // On notifie le parent.
                        selectionListener.onSeanceSelected(selectedSeance);
                    }
                }
            }
        });
    }

    /**
     * Récupère la liste des genres via le service pour peupler la JComboBox de filtre.
     */
    private void loadGenres() {
        genreFilter.addItem(null); // L'élément 'null' représente l'option "Tous les genres".
        List<Genre> genres = clientService.getAllGenres();
        for (Genre g : genres) {
            genreFilter.addItem(g);
        }
        // Le "renderer" personnalise l'affichage pour l'option "null" et les objets Genre.
        genreFilter.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Genre) {
                    setText(((Genre) value).getLibelle());
                } else {
                    setText("Tous les genres");
                }
                return this;
            }
        });
    }

    /**
     * Méthode centrale qui lit les filtres, appelle le service,
     * et met à jour le tableau avec les résultats.
     */
    public void rechercher() {
        // --- Lecture des filtres ---
        LocalDate date = null;
        // Le parsing de la date est dans un bloc try-catch pour gérer les saisies incorrectes.
        if (!dateFilter.getText().trim().isEmpty()) {
            try {
                date = LocalDate.parse(dateFilter.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez jj/MM/yyyy.", "Erreur de Format", JOptionPane.ERROR_MESSAGE);
                return; // On arrête l'exécution de la méthode si la date est invalide.
            }
        }
        
        String keyword = titreFilter.getText();
        Genre genre = (Genre) genreFilter.getSelectedItem();
        Integer genreId = (genre != null) ? genre.getId() : null;

        // --- Appel au service ---
        List<Seance> seances = clientService.rechercherSeances(date, genreId, keyword);
        
        // --- Mise à jour de la vue ---
        // On crée un nouveau modèle de table personnalisé avec les résultats de la recherche.
        SeanceTableModel newModel = new SeanceTableModel(seances, clientService);
        // On applique ce nouveau modèle à notre JTable pour rafraîchir l'affichage.
        seancesTable.setModel(newModel);
    }
    
    /**
     * Permet au composant parent de s'enregistrer pour être notifié des sélections.
     * @param listener L'objet qui implémente l'interface SeanceSelectionListener.
     */
    public void setSeanceSelectionListener(SeanceSelectionListener listener) {
        this.selectionListener = listener;
    }
}

/**
 * Modèle de JTable personnalisé pour manipuler directement des objets Seance.
 * C'est une pratique bien plus robuste que d'utiliser un DefaultTableModel avec des String,
 * car elle nous permet de récupérer l'objet Seance complet à partir d'une ligne sélectionnée.
 */
class SeanceTableModel extends DefaultTableModel {
    private final transient List<Seance> seances; // 'transient' est une bonne pratique si l'objet devait être sérialisé.
    private final transient ClientService clientService;
    private final String[] columnNames = {"Film", "Date", "Heure", "Salle", "Durée (min)"};

    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    public SeanceTableModel(List<Seance> seances, ClientService clientService) {
        this.seances = seances;
        this.clientService = clientService;
    }

    @Override
    public int getRowCount() {
        return (seances != null) ? seances.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Seance seance = seances.get(row);
        // Pour afficher des informations complètes, on doit récupérer le film associé.
        Film film = clientService.getFilmDetails(seance.getIdFilm());
        
        switch (col) {
            case 0: return (film != null) ? film.getTitre() : "Film inconnu";
            case 1: return seance.getDateHeureDebut().format(DATE_FORMATTER);
            case 2: return seance.getDateHeureDebut().format(TIME_FORMATTER);
            case 3: return "Salle " + seance.getIdSalle(); // Amélioration possible: récupérer le nom/numéro de la salle via le service.
            case 4: return (film != null) ? film.getDureeMinutes() : "N/A";
            default: return null;
        }
    }
    
    /**
     * Méthode cruciale qui permet de récupérer l'objet Seance complet à partir de son index dans la table.
     * @param row L'index de la ligne.
     * @return L'objet Seance correspondant.
     */
    public Seance getSeanceAt(int row) {
        return seances.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // On interdit l'édition directe des cellules par l'utilisateur.
        return false;
    }
}