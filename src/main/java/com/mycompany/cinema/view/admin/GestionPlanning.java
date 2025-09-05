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

    private void actionAjouterCreneau() {
        Personnel selection = (Personnel) personnelComboBox.getSelectedItem();
        if (selection == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dateDebutStr = JOptionPane.showInputDialog(this, "Date de début (jj/MM/aaaa HH:mm) :");
        if (dateDebutStr == null) {
            return;
        }
        String dateFinStr = JOptionPane.showInputDialog(this, "Date de fin (jj/MM/aaaa HH:mm) :");
        if (dateFinStr == null) {
            return;
        }
        String poste = JOptionPane.showInputDialog(this, "Poste occupé :");
        if (poste == null || poste.trim().isEmpty()) {
            return;
        }

        try {
            LocalDateTime debut = LocalDateTime.parse(dateDebutStr, FORMATTER);
            LocalDateTime fin = LocalDateTime.parse(dateFinStr, FORMATTER);

            adminService.creerPlanning(selection.getId(), debut, fin, poste);
            JOptionPane.showMessageDialog(this, "Créneau ajouté avec succès !");
            chargerPlanningPourPersonnelSelectionne();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
