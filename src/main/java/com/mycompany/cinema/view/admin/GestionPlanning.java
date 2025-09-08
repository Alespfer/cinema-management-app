package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.Planning;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GestionPlanning extends javax.swing.JPanel {

    private final AdminService adminService;
    private DefaultComboBoxModel<Personnel> personnelComboBoxModel;
    private DefaultTableModel planningTableModel;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public GestionPlanning(AdminService adminService) {
        this.adminService = adminService;
        initComponents();
        initModelsAndRenderers();
        rafraichirDonnees();
    }

    public void rafraichirDonnees() {
        chargerPersonnel();
    }

    private void initModelsAndRenderers() {
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

        String[] columnNames = {"Début du service", "Fin du service", "Poste"};
        planningTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        planningTable.setModel(planningTableModel);
    }

    private void chargerPersonnel() {
        personnelComboBoxModel.removeAllElements();
        List<Personnel> personnelList = adminService.getAllPersonnel();
        for (Personnel p : personnelList) {
            personnelComboBoxModel.addElement(p);
        }
        if (!personnelList.isEmpty()) {
            personnelComboBox.setSelectedIndex(0);
        }
        // L'appel à chargerPlanning est déclenché par l'événement du ComboBox
    }

    private void chargerPlanningPourPersonnelSelectionne() {
        planningTableModel.setRowCount(0);
        Personnel selection = (Personnel) personnelComboBox.getSelectedItem();
        if (selection != null) {
            List<Planning> plannings = adminService.getPlanningPourPersonnel(selection.getId());
            for (Planning p : plannings) {
                planningTableModel.addRow(new Object[]{
                    p.getDateHeureDebutService().format(FORMATTER),
                    p.getDateHeureFinService().format(FORMATTER),
                    p.getPosteOccupe()
                });
            }
        }
    }

    // Dans la classe GestionPlanning
    // Dans la classe GestionPlanning
    private void actionAjouterCreneau() {
        Personnel selection = (Personnel) personnelComboBox.getSelectedItem();
        if (selection == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime debut = null;
        LocalDateTime fin = null;
        String poste = null;

        // --- BOUCLE DE VALIDATION POUR LA DATE DE DÉBUT ---
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

                // --- NOUVELLE VÉRIFICATION ---
                // On vérifie que la date de début n'est pas dans le passé.
                if (!estDansLePresentOuFutur(debut, LocalDateTime.now())) {
                    JOptionPane.showMessageDialog(this, "La date de début ne peut pas être dans le passé.", "Erreur de logique", JOptionPane.ERROR_MESSAGE);
                    continue; // La date est passée, on redemande la saisie.
                }
                // --- FIN DE LA NOUVELLE VÉRIFICATION ---

                break; // La date est valide et future, on sort de la boucle.
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez bien jj/MM/aaaa HH:mm.", "Erreur de format", JOptionPane.ERROR_MESSAGE);
            }
        }

        // --- BOUCLE DE VALIDATION POUR LA DATE DE FIN (avec isAfter, conforme à la doctrine sur ce point) ---
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
                // La méthode isAfter() de LocalDateTime est acceptable ici car elle fait partie de l'API standard java.time
                // que nous avons autorisée comme exception à la doctrine de base.
                if (!fin.isAfter(debut)) {
                    JOptionPane.showMessageDialog(this, "La date de fin doit être strictement postérieure à la date de début.", "Erreur de logique", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez bien jj/MM/aaaa HH:mm.", "Erreur de format", JOptionPane.ERROR_MESSAGE);
            }
        }

        // --- BOUCLE DE VALIDATION POUR LE POSTE ---
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

        // --- ENREGISTREMENT FINAL ---
        try {
            adminService.creerPlanning(selection.getId(), debut, fin, poste.trim());
            JOptionPane.showMessageDialog(this, "Créneau ajouté avec succès pour " + selection.getPrenom() + " !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerPlanningPourPersonnelSelectionne();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Compare deux instants temporels pour savoir si le premier est après OU au
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

        return true; // Les instants sont égaux à la minute près.
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
// TODO add your handling code here:
    }//GEN-LAST:event_ajouterButtonActionPerformed

    private void personnelComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_personnelComboBoxActionPerformed
        chargerPlanningPourPersonnelSelectionne();
// TODO add your handling code here:
    }//GEN-LAST:event_personnelComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ajouterButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JComboBox<com.mycompany.cinema.Personnel> personnelComboBox;
    private javax.swing.JTable planningTable;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
