package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Salle;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.AdminService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GestionSeancesPanel extends JPanel {
    private final AdminService adminService;

    private JList<Seance> seanceJList;
    private DefaultListModel<Seance> seanceListModel;
    private JTextField dateHeureField;
    private JComboBox<Film> filmComboBox;
    private JComboBox<Salle> salleComboBox;
    private JButton nouveauButton, enregistrerButton, supprimerButton;
    private Seance seanceSelectionnee;

    public GestionSeancesPanel(AdminService adminService) {
        this.adminService = adminService;
        setLayout(new BorderLayout(10, 10));
        initComponents();
        // APPEL UNIQUE ET OPTIMISÉ AU DÉMARRAGE
        rafraichirDonnees();
    }
    
    /**
     * NOUVELLE MÉTHODE PUBLIQUE.
     * Cette méthode est la clé de la correction. Elle peut être appelée de l'extérieur
     * (par AdminMainFrame) pour forcer le rechargement de toutes les données du panneau.
     */
    public void rafraichirDonnees() {
        loadFilmsAndSalles();
        loadSeances();
    }

    private void initComponents() {
        seanceListModel = new DefaultListModel<>();
        seanceJList = new JList<>(seanceListModel);
        seanceJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        seanceJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Seance) {
                    Seance seance = (Seance) value;
                    Film film = adminService.getFilmDetails(seance.getIdFilm());
                    String filmTitre = (film != null) ? film.getTitre() : "Film inconnu";
                    setText(filmTitre + " - " + seance.getDateHeureDebut().format(formatter));
                }
                return this;
            }
        });

        seanceJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seanceSelectionnee = seanceJList.getSelectedValue();
                    displaySeanceDetails(seanceSelectionnee);
                }
            }
        });
        add(new JScrollPane(seanceJList), BorderLayout.WEST);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Détails de la Séance"));
        dateHeureField = new JTextField();
        filmComboBox = new JComboBox<>();
        salleComboBox = new JComboBox<>();
        
        filmComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Film) setText(((Film) value).getTitre());
                return this;
            }
        });
        
        salleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Salle) setText(((Salle) value).getNumero());
                return this;
            }
        });

        detailsPanel.add(new JLabel("Date et Heure (dd/MM/yyyy HH:mm):"));
        detailsPanel.add(dateHeureField);
        detailsPanel.add(new JLabel("Film:"));
        detailsPanel.add(filmComboBox);
        detailsPanel.add(new JLabel("Salle:"));
        detailsPanel.add(salleComboBox);
        add(detailsPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        nouveauButton = new JButton("Nouveau");
        enregistrerButton = new JButton("Enregistrer");
        supprimerButton = new JButton("Supprimer");
        actionPanel.add(nouveauButton);
        actionPanel.add(enregistrerButton);
        actionPanel.add(supprimerButton);
        add(actionPanel, BorderLayout.SOUTH);

        nouveauButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        enregistrerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSeance();
            }
        });
        supprimerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSeance();
            }
        });
    }
    
    private void loadSeances() {
        seanceListModel.clear();
        List<Seance> seances = adminService.getAllSeances();
        seanceListModel.addAll(seances);
    }
    
    private void loadFilmsAndSalles() {
        Object selectedFilm = filmComboBox.getSelectedItem();
        Object selectedSalle = salleComboBox.getSelectedItem();

        filmComboBox.removeAllItems();
        salleComboBox.removeAllItems();
        
        List<Film> films = adminService.getFilmsAffiche();
        for (Film film : films) {
            filmComboBox.addItem(film);
        }

        List<Salle> salles = adminService.getAllSalles();
        for (Salle salle : salles) {
            salleComboBox.addItem(salle);
        }

        if(selectedFilm != null) filmComboBox.setSelectedItem(selectedFilm);
        if(selectedSalle != null) salleComboBox.setSelectedItem(selectedSalle);
    }

    private void displaySeanceDetails(Seance seance) {
        if (seance != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            dateHeureField.setText(seance.getDateHeureDebut().format(formatter));
            
            Film filmSeance = adminService.getFilmDetails(seance.getIdFilm());
            filmComboBox.setSelectedItem(filmSeance);
            
            for (int i = 0; i < salleComboBox.getItemCount(); i++) {
                if (salleComboBox.getItemAt(i).getId() == seance.getIdSalle()) {
                    salleComboBox.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            clearForm();
        }
    }

    private void clearForm() {
        seanceJList.clearSelection();
        seanceSelectionnee = null;
        dateHeureField.setText("");
        filmComboBox.setSelectedIndex(-1);
        salleComboBox.setSelectedIndex(-1);
    }

    private void saveSeance() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dateHeure = LocalDateTime.parse(dateHeureField.getText(), formatter);
            Film filmSelectionne = (Film) filmComboBox.getSelectedItem();
            Salle salleSelectionnee = (Salle) salleComboBox.getSelectedItem();

            if (filmSelectionne == null || salleSelectionnee == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un film et une salle.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (this.seanceSelectionnee == null) {
                Seance nouvelleSeance = new Seance(0, dateHeure, salleSelectionnee.getId(), filmSelectionne.getId());
                adminService.ajouterSeance(nouvelleSeance);
                JOptionPane.showMessageDialog(this, "Séance ajoutée avec succès !");
            } else {
                this.seanceSelectionnee.setDateHeureDebut(dateHeure);
                this.seanceSelectionnee.setIdFilm(filmSelectionne.getId());
                this.seanceSelectionnee.setIdSalle(salleSelectionnee.getId());
                adminService.modifierSeance(this.seanceSelectionnee);
                JOptionPane.showMessageDialog(this, "Séance mise à jour avec succès !");
            }
            rafraichirDonnees();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSeance() {
        if (seanceSelectionnee == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une séance à supprimer.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette séance ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerSeance(seanceSelectionnee.getId());
                JOptionPane.showMessageDialog(this, "Séance supprimée avec succès !");
                rafraichirDonnees();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}