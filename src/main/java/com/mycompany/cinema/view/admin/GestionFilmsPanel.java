package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.service.AdminService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionFilmsPanel extends JPanel {

    private final AdminService adminService;

    // Composants de l'interface
    private JList<Film> filmJList;
    private DefaultListModel<Film> filmListModel;
    private JTextField titreField, synopsisField, dureeField, classificationField, urlAfficheField;
    private JButton nouveauButton, enregistrerButton, supprimerButton;
    private Film filmSelectionne;

    public GestionFilmsPanel(AdminService adminService) {
        this.adminService = adminService;
        initComponents();        // 1. L'IDE construit les composants (et initialise filmListModel).
        configureListRenderer(); // 2. Nous configurons les composants qui viennent d'être créés.
        loadFilms();             // 3. Nous chargeons les données dans les composants prêts.
    }
    
        /**
     * Configure l'affichage des objets Film dans la JList.
     * Cette logique est extraite ici pour garder le code généré propre.
     */
    private void configureListRenderer() {
        filmJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // Cette logique est la nôtre, pas celle de l'IDE.
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Film) {
                    setText(((Film) value).getTitre());
                }
                return component;
            }
        });
    }

    private void initComponents() {
        // --- Panneau de la liste des films (à gauche) ---
        filmListModel = new DefaultListModel<>();
        filmJList = new JList<>(filmListModel);
        filmJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Rendu pour n'afficher que le titre
        filmJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // ON STOCKE LE COMPOSANT DANS UNE VARIABLE 'component'
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Film) {
                    // On modifie le texte du composant
                    setText(((Film) value).getTitre());
                }
                // ON RETOURNE LE COMPOSANT MODIFIÉ
                return component;
            }
        });

        // Quand on sélectionne un film dans la liste, on affiche ses détails
        filmJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                filmSelectionne = filmJList.getSelectedValue();
                displayFilmDetails(filmSelectionne);
            }
        });

        add(new JScrollPane(filmJList), BorderLayout.WEST);

        // --- Panneau des détails (au centre) ---
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Détails du Film"));

        titreField = new JTextField();
        synopsisField = new JTextField();
        dureeField = new JTextField();
        classificationField = new JTextField();
        urlAfficheField = new JTextField();

        detailsPanel.add(new JLabel("Titre:"));
        detailsPanel.add(titreField);
        detailsPanel.add(new JLabel("Synopsis:"));
        detailsPanel.add(synopsisField);
        detailsPanel.add(new JLabel("Durée (min):"));
        detailsPanel.add(dureeField);
        detailsPanel.add(new JLabel("Classification:"));
        detailsPanel.add(classificationField);
        detailsPanel.add(new JLabel("URL Affiche:"));
        detailsPanel.add(urlAfficheField);

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
        enregistrerButton.addActionListener(e -> saveFilm());
        supprimerButton.addActionListener(e -> deleteFilm());
    }

    private void loadFilms() {
        filmListModel.clear();
        try {
            List<Film> films = adminService.getFilmsAffiche();
            filmListModel.addAll(films);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des films.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayFilmDetails(Film film) {
        if (film != null) {
            titreField.setText(film.getTitre());
            synopsisField.setText(film.getSynopsis());
            dureeField.setText(String.valueOf(film.getDureeMinutes()));
            classificationField.setText(film.getClassification());
            urlAfficheField.setText(film.getUrlAffiche());
        } else {
            clearForm();
        }
    }

    private void clearForm() {
        filmJList.clearSelection();
        filmSelectionne = null;
        titreField.setText("");
        synopsisField.setText("");
        dureeField.setText("");
        classificationField.setText("");
        urlAfficheField.setText("");
    }

    private void saveFilm() {
        try {
            // Création d'un nouveau film
            if (filmSelectionne == null) {
                Film nouveauFilm = new Film();
                nouveauFilm.setTitre(titreField.getText());
                nouveauFilm.setSynopsis(synopsisField.getText());
                nouveauFilm.setDureeMinutes(Integer.parseInt(dureeField.getText()));
                nouveauFilm.setClassification(classificationField.getText());
                nouveauFilm.setUrlAffiche(urlAfficheField.getText());
                adminService.ajouterFilm(nouveauFilm);
                JOptionPane.showMessageDialog(this, "Film ajouté avec succès !");
            } else { // Mise à jour d'un film existant
                filmSelectionne.setTitre(titreField.getText());
                filmSelectionne.setSynopsis(synopsisField.getText());
                filmSelectionne.setDureeMinutes(Integer.parseInt(dureeField.getText()));
                filmSelectionne.setClassification(classificationField.getText());
                filmSelectionne.setUrlAffiche(urlAfficheField.getText());
                adminService.mettreAJourFilm(filmSelectionne);
                JOptionPane.showMessageDialog(this, "Film mis à jour avec succès !");
            }
            // Recharger la liste pour voir les changements
            loadFilms();
            clearForm();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "La durée doit être un nombre entier.", "Erreur de format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFilm() {
        if (filmSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un film à supprimer.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce film ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerFilm(filmSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Film supprimé avec succès !");
                loadFilms();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
