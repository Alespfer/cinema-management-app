package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.Salle;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.service.AdminService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GestionSeancesPanel extends JPanel {
    private final AdminService adminService;

    // Composants de l'interface
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
        loadSeances();
        loadFilmsAndSalles();
    }

    private void initComponents() {
        // --- Panneau de la liste des séances (à gauche) ---
        seanceListModel = new DefaultListModel<>();
        seanceJList = new JList<>(seanceListModel);
        seanceJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Formatter pour afficher les dates et heures de manière lisible
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        seanceJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Seance) {
                    Seance seance = (Seance) value;
                    // On rend l'affichage plus informatif
                    String filmTitre = adminService.getFilmDetails(seance.getIdFilm()).getTitre();
                    setText(filmTitre + " - " + seance.getDateHeureDebut().format(formatter));
                }
                return this;
            }
        });

        seanceJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seanceSelectionnee = seanceJList.getSelectedValue();
                displaySeanceDetails(seanceSelectionnee);
            }
        });
        add(new JScrollPane(seanceJList), BorderLayout.WEST);

        // --- Panneau des détails (au centre) ---
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Détails de la Séance"));
        dateHeureField = new JTextField();
        filmComboBox = new JComboBox<>();
        salleComboBox = new JComboBox<>();
        
        // Rendu pour afficher le titre du film dans le ComboBox
        filmComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Film) setText(((Film) value).getTitre());
                return this;
            }
        });
        
        // Rendu pour afficher le numéro de la salle dans le ComboBox
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

        // --- Panneau des boutons d'action (en bas) ---
        JPanel actionPanel = new JPanel();
        nouveauButton = new JButton("Nouveau");
        enregistrerButton = new JButton("Enregistrer");
        supprimerButton = new JButton("Supprimer");
        actionPanel.add(nouveauButton);
        actionPanel.add(enregistrerButton);
        actionPanel.add(supprimerButton);
        add(actionPanel, BorderLayout.SOUTH);

        // --- Logique des boutons ---
        nouveauButton.addActionListener(e -> clearForm());
        enregistrerButton.addActionListener(e -> saveSeance());
        supprimerButton.addActionListener(e -> deleteSeance());
    }
    
    // --- Méthodes de logique interne ---

    private void loadSeances() {
        seanceListModel.clear();
        List<Seance> seances = adminService.getAllSeances();
        seanceListModel.addAll(seances);
    }
    
    private void loadFilmsAndSalles() {
        // On peuple les listes déroulantes
        List<Film> films = adminService.getFilmsAffiche();
        for (Film film : films) filmComboBox.addItem(film);

        List<Salle> salles = adminService.getAllSalles();
        for (Salle salle : salles) salleComboBox.addItem(salle);
    }

    private void displaySeanceDetails(Seance seance) {
        if (seance != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            dateHeureField.setText(seance.getDateHeureDebut().format(formatter));
            
            // Sélectionner le bon film dans le ComboBox
            Film filmSeance = adminService.getFilmDetails(seance.getIdFilm());
            filmComboBox.setSelectedItem(filmSeance);
            
            // Sélectionner la bonne salle dans le ComboBox
            // Note: C'est lourd. Dans une vraie appli, on utiliserait une Map pour la performance.
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

            // Création d'une nouvelle séance
            if (this.seanceSelectionnee == null) {
                Seance nouvelleSeance = new Seance();
                nouvelleSeance.setDateHeureDebut(dateHeure);
                nouvelleSeance.setIdFilm(filmSelectionne.getId());
                nouvelleSeance.setIdSalle(salleSelectionnee.getId());
                adminService.ajouterSeance(nouvelleSeance);
                JOptionPane.showMessageDialog(this, "Séance ajoutée avec succès !");
            } else { // Mise à jour
                this.seanceSelectionnee.setDateHeureDebut(dateHeure);
                this.seanceSelectionnee.setIdFilm(filmSelectionne.getId());
                this.seanceSelectionnee.setIdSalle(salleSelectionnee.getId());
                adminService.modifierSeance(this.seanceSelectionnee);
                JOptionPane.showMessageDialog(this, "Séance mise à jour avec succès !");
            }
            loadSeances();
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
                loadSeances();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}