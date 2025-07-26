// Fichier : src/main/java/com/mycompany/cinema/view/FilmListPanel.java
package com.mycompany.cinema.view;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.service.CinemaService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FilmListPanel extends JPanel {
    private final CinemaService cinemaService;
    private JList<Film> filmJList; // On stocke des objets Film, pas juste des String
    private DefaultListModel<Film> filmListModel;
    private List<Film> films; // Garder une copie locale

    // Contrat pour notifier le parent d'une sélection
    public interface FilmSelectionListener {
        void onFilmSelected(Film film);
    }
    private FilmSelectionListener selectionListener;

    public FilmListPanel(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
        initComponents();
        loadFilms();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Films à l'affiche"));
        
        filmListModel = new DefaultListModel<>();
        filmJList = new JList<>(filmListModel);
        
        // C'est ici que la magie opère : on écoute les changements de sélection
        filmJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && selectionListener != null) {
                Film selectedFilm = filmJList.getSelectedValue();
                selectionListener.onFilmSelected(selectedFilm);
            }
        });

        // Rendu personnalisé pour n'afficher que le titre du film dans la liste
        filmJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Film) {
                    setText(((Film) value).getTitre());
                }
                return c;
            }
        });
        
        add(new JScrollPane(filmJList), BorderLayout.CENTER);
    }

    private void loadFilms() {
        this.films = cinemaService.getFilmsAffiche();
        filmListModel.clear();
        filmListModel.addAll(this.films);
    }

    public void setFilmSelectionListener(FilmSelectionListener listener) {
        this.selectionListener = listener;
    }
}