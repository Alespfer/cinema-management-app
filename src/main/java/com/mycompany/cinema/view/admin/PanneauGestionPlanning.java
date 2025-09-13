
/**
 * Méthode publique pour rafraîchir les données, appelée par la fenêtre principale.
 */
package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.Planning;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class PanneauGestionPlanning extends javax.swing.JPanel {

    private final AdminService adminService;
    private DefaultComboBoxModel<Personnel> personnelComboBoxModel;
    private DefaultTableModel planningTableModel;
    private List<Planning> planningsAffiches;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PanneauGestionPlanning(AdminService adminService) {
        this.adminService = adminService;
        this.planningsAffiches = new ArrayList<>();
        initComponents();
        configurerModelesEtRenderers();
        planningTable.getSelectionModel().addListSelectionListener(e -> {
            // On vérifie si une ligne est bien sélectionnée.
            boolean estSelectionne = !e.getValueIsAdjusting() && planningTable.getSelectedRow() != -1;
            // On active ou désactive les boutons en conséquence.
            modifierButton.setEnabled(estSelectionne);
            supprimerButton.setEnabled(estSelectionne);
        });
        rafraichirDonnees();

    }

    /**
     * Initialise les modèles de données et les moteurs de rendu pour les
     * composants.
     */
    public void rafraichirDonnees() {
        chargerPersonnel();
    }

    /**
     * Initialise les modèles de données et les moteurs de rendu pour les
     * composants.
     */
    private void configurerModelesEtRenderers() {
        // Configuration de la liste déroulante des employés
        personnelComboBoxModel = new DefaultComboBoxModel<>();
        personnelComboBox.setModel(personnelComboBoxModel);
        personnelComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Personnel) {
                    Personnel p = (Personnel) value;
                    setText(p.getPrenom() + " " + p.getNom());
                }
                return this;
            }
        });

        // Configuration du tableau du planning
        String[] columnNames = {"Début du service", "Fin du service", "Poste"};
        planningTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        planningTable.setModel(planningTableModel);
    }

    /**
     * Charge la liste complète du personnel dans la liste déroulante.
     */
    private void chargerPersonnel() {
        personnelComboBoxModel.removeAllElements();
        List<Personnel> personnelList = adminService.trouverToutLePersonnel(false);
        for (Personnel p : personnelList) {
            // On n'affiche pas les informations de l'utilisateur Système (ID = 0)
            if (p.getId() != 0) {
                personnelComboBoxModel.addElement(p);
            }
        }
        if (!personnelList.isEmpty()) {
            personnelComboBox.setSelectedIndex(0);
        }
    }

    /**
     * Charge et affiche dans le tableau le planning de l'employé actuellement
     * sélectionné.
     */
    private void chargerPlanningPourPersonnelSelectionne() {
        planningTableModel.setRowCount(0);
        this.planningsAffiches.clear();
        Personnel selection = (Personnel) personnelComboBox.getSelectedItem();
        if (selection != null) {
            List<Planning> planningsDuService = adminService.trouverPlanningPourPersonnel(selection.getId());

            // --- DÉBUT DE LA CORRECTION ---
            // On crée une NOUVELLE liste qui est une copie de celle du service.
            // C'est cette copie que nous allons stocker et manipuler.
            this.planningsAffiches = new ArrayList<>(planningsDuService);
            // --- FIN DE LA CORRECTION ---

            // Le reste du code ne change pas, mais il travaille maintenant sur la copie.
            for (Planning p : this.planningsAffiches) {
                planningTableModel.addRow(new Object[]{
                    p.getDateHeureDebutService().format(FORMATTER),
                    p.getDateHeureFinService().format(FORMATTER),
                    p.getPosteOccupe()
                });
            }
        }
    }

    /**
     * Gère la logique d'ajout d'un nouveau créneau horaire via une série de
     * boîtes de dialogue.
     */
    private void actionAjouterCreneau() {
        Personnel selection = (Personnel) personnelComboBox.getSelectedItem();
        if (selection == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime debut = null;
        LocalDateTime fin = null;
        String poste = null;

        while (true) {
            String dateDebutStr = JOptionPane.showInputDialog(
                    this,
                    "Entrez la date de début du service (ex: " + LocalDateTime.now().format(FORMATTER) + ")",
                    "Ajout - Début du créneau",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (dateDebutStr == null) {
                return;
            }

            try {
                debut = LocalDateTime.parse(dateDebutStr.trim(), FORMATTER);

                // La date de début ne doit pas être dans le passé.
                if (!estDansLePresentOuFutur(debut, LocalDateTime.now())) {
                    JOptionPane.showMessageDialog(this, "La date de début ne peut pas être dans le passé.", "Erreur de logique", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                break;
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez bien jj/MM/aaaa HH:mm.", "Erreur de format", JOptionPane.ERROR_MESSAGE);
            }
        }

        while (true) {
            String dateFinStr = JOptionPane.showInputDialog(
                    this,
                    "Entrez la date de fin du service (ex: " + debut.plusHours(5).format(FORMATTER) + ")",
                    "Ajout - Fin du créneau",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (dateFinStr == null) {
                return;
            }

            try {
                fin = LocalDateTime.parse(dateFinStr.trim(), FORMATTER);
                if (!fin.isAfter(debut)) {
                    JOptionPane.showMessageDialog(this, "La date de fin doit être strictement postérieure à la date de début.", "Erreur de logique", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez bien jj/MM/aaaa HH:mm.", "Erreur de format", JOptionPane.ERROR_MESSAGE);
            }
        }

        // --- Boucle de validation pour le poste ---
        while (true) {
            poste = JOptionPane.showInputDialog(
                    this,
                    "Entrez le nom du poste occupé (ex: Vente, Projection...)",
                    "Ajout - Poste",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (poste == null) {
                return;
            }

            if (poste.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom du poste ne peut pas être vide.", "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
            } else {
                break;
            }
        }

        // Enregistrement final
        try {
            adminService.creerPlanning(selection.getId(), debut, fin, poste.trim());
            JOptionPane.showMessageDialog(this, "Créneau ajouté avec succès pour " + selection.getPrenom() + " !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerPlanningPourPersonnelSelectionne();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Compare deux instants temporels pour savoir si le premier est après ou au
     * même moment que le second.
     *
     * @param dateTimeATester L'instant à tester.
     * @param maintenant L'instant de référence.
     * @return true si dateTimeATester est dans le futur ou exactement
     * maintenant, sinon false.
     */
    private boolean estDansLePresentOuFutur(LocalDateTime dateTimeATester, LocalDateTime maintenant) {
        if (dateTimeATester.getYear() > maintenant.getYear()) {
            return true;
        }
        if (dateTimeATester.getYear() < maintenant.getYear()) {
            return false;
        }

        if (dateTimeATester.getMonthValue() > maintenant.getMonthValue()) {
            return true;
        }
        if (dateTimeATester.getMonthValue() < maintenant.getMonthValue()) {
            return false;
        }

        if (dateTimeATester.getDayOfMonth() > maintenant.getDayOfMonth()) {
            return true;
        }
        if (dateTimeATester.getDayOfMonth() < maintenant.getDayOfMonth()) {
            return false;
        }

        if (dateTimeATester.getHour() > maintenant.getHour()) {
            return true;
        }
        if (dateTimeATester.getHour() < maintenant.getHour()) {
            return false;
        }

        if (dateTimeATester.getMinute() > maintenant.getMinute()) {
            return true;
        }
        if (dateTimeATester.getMinute() < maintenant.getMinute()) {
            return false;
        }

        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        personnelComboBox = new javax.swing.JComboBox<>();
        ajouterButton = new javax.swing.JButton();
        modifierButton = new javax.swing.JButton();
        supprimerButton = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        planningTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        topPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Employé : ");
        topPanel.add(jLabel1);

        personnelComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                personnelComboBoxActionPerformed(evt);
            }
        });
        topPanel.add(personnelComboBox);

        ajouterButton.setText("Ajouter un créneau");
        ajouterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterButtonActionPerformed(evt);
            }
        });
        topPanel.add(ajouterButton);

        modifierButton.setText("Modifier un créneau");
        modifierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifierButtonActionPerformed(evt);
            }
        });
        topPanel.add(modifierButton);

        supprimerButton.setText("Supprimer un créneau");
        supprimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerButtonActionPerformed(evt);
            }
        });
        topPanel.add(supprimerButton);

        add(topPanel, java.awt.BorderLayout.PAGE_START);

        planningTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane.setViewportView(planningTable);

        add(jScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void ajouterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterButtonActionPerformed
        actionAjouterCreneau();
    }//GEN-LAST:event_ajouterButtonActionPerformed

    private void personnelComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_personnelComboBoxActionPerformed
        chargerPlanningPourPersonnelSelectionne();
    }//GEN-LAST:event_personnelComboBoxActionPerformed

    private void modifierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifierButtonActionPerformed

        int selectedRow = planningTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        // Récupération du planning à la ligne sélectionnée.
        Planning planningAModifier = planningsAffiches.get(selectedRow);

        try {
            // Saisie de nouvelles valeurs par l'utilisateur
            String nouvelleDateDebutStr = JOptionPane.showInputDialog(this, "Modifier la date de début :", planningAModifier.getDateHeureDebutService().format(FORMATTER));
            if (nouvelleDateDebutStr == null) {
                return;
            }
            LocalDateTime nouvelleDateDebut = LocalDateTime.parse(nouvelleDateDebutStr.trim(), FORMATTER);

            String nouvelleDateFinStr = JOptionPane.showInputDialog(this, "Modifier la date de fin :", planningAModifier.getDateHeureFinService().format(FORMATTER));
            if (nouvelleDateFinStr == null) {
                return; 
            }
            LocalDateTime nouvelleDateFin = LocalDateTime.parse(nouvelleDateFinStr.trim(), FORMATTER);

            String nouveauPoste = JOptionPane.showInputDialog(this, "Modifier le poste :", planningAModifier.getPosteOccupe());
            if (nouveauPoste == null) {
                return; 
            }

            // Mise à jour de l'objet en mémoire 
            planningAModifier.setDateHeureDebutService(nouvelleDateDebut);
            planningAModifier.setDateHeureFinService(nouvelleDateFin);
            planningAModifier.setPosteOccupe(nouveauPoste.trim());

            // On envoie l'objet mis à jour au service pour persistance 
            adminService.modifierPlanning(planningAModifier);

            // On met à jour la ligne spécifique dans le tableau.
            planningTableModel.setValueAt(nouvelleDateDebut.format(FORMATTER), selectedRow, 0);
            planningTableModel.setValueAt(nouvelleDateFin.format(FORMATTER), selectedRow, 1);
            planningTableModel.setValueAt(nouveauPoste.trim(), selectedRow, 2);

            JOptionPane.showMessageDialog(this, "Créneau mis à jour.");

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. La modification est annulée.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur de modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            chargerPlanningPourPersonnelSelectionne();
        }
    }//GEN-LAST:event_modifierButtonActionPerformed

    private void supprimerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerButtonActionPerformed
        int selectedRow = planningTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        Planning planningSelectionne = planningsAffiches.get(selectedRow);

        int reponse = JOptionPane.showConfirmDialog(this, "Supprimer ce créneau ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (reponse == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerPlanning(planningSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Créneau supprimé.");
                chargerPlanningPourPersonnelSelectionne();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_supprimerButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ajouterButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JButton modifierButton;
    private javax.swing.JComboBox<com.mycompany.cinema.Personnel> personnelComboBox;
    private javax.swing.JTable planningTable;
    private javax.swing.JButton supprimerButton;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
