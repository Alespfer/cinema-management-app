package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.LignePanier;
import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.view.Login;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class PointDeVente extends javax.swing.JFrame {

    private final AdminService adminService;
    private final Personnel vendeurConnecte;
    private DefaultListModel<ProduitSnack> listModel;
    private DefaultTableModel tableModel;
    private List<LignePanier> panier;

    public PointDeVente(AdminService adminService, Personnel vendeur) {
        this.adminService = adminService;
        this.vendeurConnecte = vendeur;
        initComponents();
        initModelsAndRenderers();
        this.panier = new ArrayList<>();
        chargerProduits();
        setTitle("Point de Vente - " + vendeur.getPrenom() + " " + vendeur.getNom());
        setLocationRelativeTo(null);
    }

    private void initModelsAndRenderers() {
        listModel = new DefaultListModel<>();
        listeProduits.setModel(listModel);

        String[] columnNames = {"Produit", "Quantité", "Prix Unitaire", "Sous-total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        panierTable.setModel(tableModel);

        listeProduits.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ProduitSnack) {
                    ProduitSnack p = (ProduitSnack) value;
                    setText(p.getNomProduit() + " (" + String.format("%.2f", p.getPrixVente()) + " €) - Stock: " + p.getStock());
                }
                return this;
            }
        });
    }

    private void chargerProduits() {
        try {
            listModel.clear();
            List<ProduitSnack> produits = adminService.getAllProduitsSnack();
            for (ProduitSnack p : produits) {
                listModel.addElement(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur au chargement des produits.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionAjouter() {
        ProduitSnack produitSelectionne = listeProduits.getSelectedValue();
        if (produitSelectionne == null) {
            return;
        }

        if (produitSelectionne.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "Produit en rupture de stock.", "Stock épuisé", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (LignePanier ligne : panier) {
            if (ligne.getProduit().getId() == produitSelectionne.getId()) {
                ligne.setQuantite(ligne.getQuantite() + 1);
                mettreAJourPanier();
                return;
            }
        }
        panier.add(new LignePanier(produitSelectionne, 1));
        mettreAJourPanier();
    }

    private void actionSupprimer() {
        int selectedRow = panierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne dans le panier.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        panier.remove(selectedRow);
        mettreAJourPanier();
    }

    private void actionAnnuler() {
        if (panier.isEmpty()) {
            return;
        }
        int response = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment annuler toute la vente ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            panier.clear();
            mettreAJourPanier();
        }
    }

    private void actionValider() {
        if (panier.isEmpty()) {
            return;
        }
        try {
            adminService.enregistrerVenteSnack(vendeurConnecte.getId(), 1, panier);
            JOptionPane.showMessageDialog(this, "Vente enregistrée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            actionAnnulerApresVente();
            chargerProduits();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur de Vente", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Version silencieuse d'actionAnnuler pour après une vente réussie
    private void actionAnnulerApresVente() {
        panier.clear();
        mettreAJourPanier();
    }

    private void mettreAJourPanier() {
        tableModel.setRowCount(0);
        double totalGeneral = 0.0;
        for (LignePanier ligne : panier) {
            double sousTotal = ligne.getProduit().getPrixVente() * ligne.getQuantite();
            tableModel.addRow(new Object[]{
                ligne.getProduit().getNomProduit(),
                ligne.getQuantite(),
                String.format("%.2f €", ligne.getProduit().getPrixVente()),
                String.format("%.2f €", sousTotal)
            });
            totalGeneral += sousTotal;
        }
        totalLabel.setText("TOTAL : " + String.format("%.2f €", totalGeneral));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        panneauGauche = new javax.swing.JPanel();
        ajouterButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listeProduits = new javax.swing.JList<>();
        panneauDroite = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panierTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        totalLabel = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        annulerButton = new javax.swing.JButton();
        validerButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panneauGauche.setBorder(javax.swing.BorderFactory.createTitledBorder("Produits disponibles"));
        panneauGauche.setPreferredSize(new java.awt.Dimension(300, 484));

        ajouterButton.setText("Ajouter au panier");
        ajouterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterButtonActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(listeProduits);

        javax.swing.GroupLayout panneauGaucheLayout = new javax.swing.GroupLayout(panneauGauche);
        panneauGauche.setLayout(panneauGaucheLayout);
        panneauGaucheLayout.setHorizontalGroup(
            panneauGaucheLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panneauGaucheLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panneauGaucheLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(ajouterButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panneauGaucheLayout.setVerticalGroup(
            panneauGaucheLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panneauGaucheLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ajouterButton)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(panneauGauche);

        panneauDroite.setBorder(javax.swing.BorderFactory.createTitledBorder("Vente en cours"));
        panneauDroite.setLayout(new java.awt.BorderLayout());

        panierTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(panierTable);

        panneauDroite.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        totalLabel.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        totalLabel.setText("TOTAL : 00,00 €");
        jPanel2.add(totalLabel, java.awt.BorderLayout.LINE_START);

        annulerButton.setText("Annuler la vente ");
        annulerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(annulerButton);

        validerButton.setText("Valider la vente");
        validerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validerButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(validerButton);

        jPanel2.add(buttonsPanel, java.awt.BorderLayout.LINE_END);

        panneauDroite.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jSplitPane1.setRightComponent(panneauDroite);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton1.setText("Déconnexion");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ajouterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterButtonActionPerformed
        actionAjouter();// TODO add your handling code here:
    }//GEN-LAST:event_ajouterButtonActionPerformed

    private void annulerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerButtonActionPerformed
        actionAnnuler();// TODO add your handling code here:
    }//GEN-LAST:event_annulerButtonActionPerformed

    private void validerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validerButtonActionPerformed
        actionValider();// TODO add your handling code here:
    }//GEN-LAST:event_validerButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // 1. Définir les options des boutons en français
        Object[] options = {"Oui", "Non"};

        // 2. Créer et afficher la boîte de dialogue personnalisée
        int reponse = JOptionPane.showOptionDialog(
                this, // Composant parent
                "Êtes-vous sûr de vouloir vous déconnecter ?", // Message
                "Confirmation de déconnexion", // Titre
                JOptionPane.YES_NO_OPTION, // Type d'option (pour l'icône)
                JOptionPane.QUESTION_MESSAGE, // Type de message (icône '?')
                null, // Icône personnalisée (aucune ici)
                options, // Le tableau avec nos textes de boutons
                options[0] // Le bouton par défaut 
        );

        // 3. Traiter la réponse
        // L'option "Oui" correspond à l'index 0, "Non" à l'index 1
        if (reponse == JOptionPane.YES_OPTION) { // ou (reponse == 0)
            this.dispose();
            new Login().setVisible(true);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ajouterButton;
    private javax.swing.JButton annulerButton;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JList<com.mycompany.cinema.ProduitSnack> listeProduits;
    private javax.swing.JTable panierTable;
    private javax.swing.JPanel panneauDroite;
    private javax.swing.JPanel panneauGauche;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JButton validerButton;
    // End of variables declaration//GEN-END:variables
}
