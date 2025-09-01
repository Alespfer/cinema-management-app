package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 * Panneau de gestion (CRUD) pour les Films. Version corrigée et entièrement
 * compatible avec NetBeans et la Doctrine.
 */
public class GestionFilms extends javax.swing.JPanel {

    // --- VARIABLES D'INSTANCE (LOGIQUE MÉTIER) ---
    private final AdminService adminService;
    private Film filmSelectionne;
    // Le modèle est déclaré ici. Il sera initialisé DANS le code généré.
    // Il n'est PAS déclaré dans la section "Variables declaration" de NetBeans.
    private DefaultListModel<Film> filmListModel;

    /**
     * CONSTRUCTEUR CONFORME ET CORRIGÉ
     */
    public GestionFilms(AdminService adminService) {
        // CORRECTION 1 : Assignation de la variable avec la bonne casse. C'EST LA CORRECTION LA PLUS IMPORTANTE.
        this.adminService = adminService;

        // SÉQUENCE D'INITIALISATION STRATÉGIQUE :
        initComponents();
        filmListModel = new DefaultListModel<>();

        filmJList.setModel(filmListModel);

        configureListRenderer(); // 2. Configure leur apparence.
        loadFilms();             // 3. Charge les données dans les composants maintenant prêts et initialisés.
    }

    /**
     * Configure l'affichage des objets Film dans la JList.
     */
    private void configureListRenderer() {
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
    }

    /**
     * Initialise le modèle de données de la liste et configure son affichage.
     */
    private void initModelAndRenderer() {
        // CORRECTION CRITIQUE : Création de l'instance du modèle.
        filmListModel = new DefaultListModel<>();
        // Assignation de ce modèle à notre JList créée par l'éditeur.
        filmJList.setModel(filmListModel);

        // Configuration de l'affichage (Renderer)
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
    }

    /**
     * Charge les films depuis le service.
     */
    private void loadFilms() {
        // Cette ligne ne plantera plus car initComponents() a été appelée avant,
        // initialisant filmListModel.
        filmListModel.clear();
        try {
            List<Film> films = adminService.getFilmsAffiche();
            for (Film film : films) {
                filmListModel.addElement(film);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des films.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- LE RESTE DES MÉTHODES MÉTIER EST INCHANGÉ ---
    private void displayFilmDetails(Film film) {
        if (film != null) {
            titreField.setText(film.getTitre());
            synopsisField.setText(film.getSynopsis());
            dureeField.setText(String.valueOf(film.getDureeMinutes()));
            classificationField.setText(film.getClassification());
            urlAfficheField.setText(film.getUrlAffiche());
            supprimerButton.setEnabled(true);
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
        supprimerButton.setEnabled(false);
    }

    private void actionEnregistrer() {
        try {
            if (filmSelectionne == null) { // Mode Création
                // 1. OBTENIR UN NOUVEL ID UNIQUE
                int nouvelId = com.mycompany.cinema.util.IdManager.getNextFilmId();

                // 2. CRÉER UN NOUVEL OBJET FILM AVEC CET ID
                // Note : Nous utilisons le constructeur qui prend toutes les valeurs.
                Film nouveauFilm = new Film(
                        nouvelId,
                        titreField.getText(),
                        synopsisField.getText(),
                        Integer.parseInt(dureeField.getText()),
                        classificationField.getText(),
                        urlAfficheField.getText(),
                        0.0 // Note presse par défaut pour un nouveau film
                );

                // 3. ENVOYER AU SERVICE
                adminService.ajouterFilm(nouveauFilm);
                JOptionPane.showMessageDialog(this, "Film ajouté avec succès !");

            } else { // Mode Mise à jour (la logique reste inchangée)
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

    private void actionSupprimer() {
        if (filmSelectionne == null) {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        filmJList = new javax.swing.JList<>();
        actionPanel = new javax.swing.JPanel();
        nouveauButton = new javax.swing.JButton();
        enregistrerButton = new javax.swing.JButton();
        supprimerButton = new javax.swing.JButton();
        detailsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        titreField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        synopsisField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dureeField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        classificationField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        urlAfficheField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 100));

        filmJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                filmJListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(filmJList);

        add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        nouveauButton.setText("Nouveau");
        nouveauButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauButtonActionPerformed(evt);
            }
        });
        actionPanel.add(nouveauButton);

        enregistrerButton.setText("Enregistrer");
        enregistrerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerButtonActionPerformed(evt);
            }
        });
        actionPanel.add(enregistrerButton);

        supprimerButton.setText("Supprimer");
        supprimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerButtonActionPerformed(evt);
            }
        });
        actionPanel.add(supprimerButton);

        add(actionPanel, java.awt.BorderLayout.PAGE_END);

        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Détails du film"));
        detailsPanel.setLayout(new java.awt.GridLayout(0, 2, 5, 5));

        jLabel1.setText("Titre : ");
        detailsPanel.add(jLabel1);
        detailsPanel.add(titreField);

        jLabel2.setText("Synopsis : ");
        detailsPanel.add(jLabel2);
        detailsPanel.add(synopsisField);

        jLabel3.setText("Durée : ");
        detailsPanel.add(jLabel3);
        detailsPanel.add(dureeField);

        jLabel4.setText("Classification : ");
        detailsPanel.add(jLabel4);
        detailsPanel.add(classificationField);

        jLabel5.setText("Url affiche :");
        detailsPanel.add(jLabel5);
        detailsPanel.add(urlAfficheField);

        add(detailsPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void nouveauButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauButtonActionPerformed
        clearForm();
// TODO add your handling code here:
    }//GEN-LAST:event_nouveauButtonActionPerformed

    private void enregistrerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enregistrerButtonActionPerformed
        actionEnregistrer();
// TODO add your handling code here:
    }//GEN-LAST:event_enregistrerButtonActionPerformed

    private void supprimerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerButtonActionPerformed
        actionSupprimer();
// TODO add your handling code here:
    }//GEN-LAST:event_supprimerButtonActionPerformed

    private void filmJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_filmJListValueChanged
        if (!evt.getValueIsAdjusting()) {
            filmSelectionne = filmJList.getSelectedValue();
            displayFilmDetails(filmSelectionne);
        }// TODO add your handling code here:
    }//GEN-LAST:event_filmJListValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JTextField classificationField;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JTextField dureeField;
    private javax.swing.JButton enregistrerButton;
    private javax.swing.JList<com.mycompany.cinema.Film> filmJList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton nouveauButton;
    private javax.swing.JButton supprimerButton;
    private javax.swing.JTextField synopsisField;
    private javax.swing.JTextField titreField;
    private javax.swing.JTextField urlAfficheField;
    // End of variables declaration//GEN-END:variables
}
