package com.mycompany.cinema.view;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SeancePanel extends JPanel {
    private final ClientService clientService;
    private JList<Seance> seanceJList;
    private DefaultListModel<Seance> seanceListModel;

    // Contrat pour notifier le MainFrame qu'une séance a été choisie
    public interface SeanceSelectionListener {
        void onSeanceSelected(Seance seance);
    }
    private SeanceSelectionListener selectionListener;

    public SeancePanel(ClientService clientService) {
        this.clientService = clientService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Séances du jour"));
        
        seanceListModel = new DefaultListModel<>();
        seanceJList = new JList<>(seanceListModel);
        
        seanceJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && selectionListener != null) {
                Seance selectedSeance = seanceJList.getSelectedValue();
                selectionListener.onSeanceSelected(selectedSeance);
            }
        });

        // Rendu personnalisé pour afficher l'heure de la séance
        seanceJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Seance) {
                    Seance seance = (Seance) value;
                    // On pourrait enrichir en allant chercher le nom de la salle via le service
                    // mais pour l'instant on garde l'ID pour la simplicité.
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    setText(seance.getDateHeureDebut().format(formatter) + " - Salle " + seance.getIdSalle());
                }
                return c;
            }
        });
        
        add(new JScrollPane(seanceJList), BorderLayout.CENTER);
    }

    public void loadSeances(Film film) {
        seanceListModel.clear();
        if (film != null) {
            // Pour l'exemple, on prend la date d'aujourd'hui.
            // Dans une application complète, on aurait un sélecteur de date.
            List<Seance> seances = clientService.getSeancesPourFilmEtDate(film.getId(), LocalDate.now());
            seanceListModel.addAll(seances);
        }
    }
    
    public void setSeanceSelectionListener(SeanceSelectionListener listener) {
        this.selectionListener = listener;
    }
}