package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.service.AdminService;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class GestionProduitsSnack extends javax.swing.JPanel {

    private final AdminService adminService;
    private ProduitSnack produitSelectionne;
    private DefaultListModel<ProduitSnack> listModel;

    public GestionProduitsSnack(AdminService adminService) {
        this.adminService = adminService;

        initComponents();
        initModelAndRenderers();

        chargerListeProduits();
    }

    private void initModelAndRenderers() {
        listModel = new DefaultListModel<>();
        listeProduits.setModel(listModel);

        listeProduits.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ProduitSnack) {
                    setText(((ProduitSnack) value).getNomProduit());
                }
                return this;
            }
        });
    }

    private void chargerListeProduits() {
        try {
            listModel.clear();
            List<ProduitSnack> produits = adminService.getAllProduitsSnack();
            for (ProduitSnack produit : produits) {
                listModel.addElement(produit);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement produits: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Dans la classe GestionProduitsSnack
    public void rafraichirDonnees() {
        chargerListeProduits(); // Suppose que 'chargerProduits()' est la méthode qui remplit ta JTable ou JList.
    }

    private void mettreAJourChamps(ProduitSnack p) {
        if (p != null) {
            idField.setText(String.valueOf(p.getId()));
            nomField.setText(p.getNomProduit());
            descriptionField.setText(p.getDescription());
            prixField.setText(String.format("%.2f", p.getPrixVente()).replace(',', '.'));
            stockField.setText(String.valueOf(p.getStock()));
            supprimerButton.setEnabled(true);
        } else {
            idField.setText("");
            nomField.setText("");
            descriptionField.setText("");
            prixField.setText("");
            stockField.setText("");
            supprimerButton.setEnabled(false);
        }
    }

    private void actionNouveau() {
        produitSelectionne = null;
        listeProduits.clearSelection();
        mettreAJourChamps(null);
    }

    /**
     * Gère la création ou la modification d'un produit. VERSION CORRIGÉE AVEC
     * ID MANAGER.
     */
    private void actionEnregistrer() {
        try {
            String nom = nomField.getText();
            if (nom == null || nom.trim().isEmpty()) {
                throw new Exception("Le nom est obligatoire.");
            }
            String description = descriptionField.getText();
            double prix = Double.parseDouble(prixField.getText().replace(',', '.'));
            int stock = Integer.parseInt(stockField.getText());

            if (produitSelectionne == null) { // Mode Création
                // 1. OBTENIR UN NOUVEL ID UNIQUE
                int nouvelId = com.mycompany.cinema.util.IdManager.getNextProduitSnackId();

                // 2. CRÉER L'OBJET AVEC L'ID
                ProduitSnack nouveau = new ProduitSnack(
                        nouvelId,
                        nom,
                        description,
                        prix,
                        stock
                );

                // 3. ENVOYER AU SERVICE
                adminService.ajouterProduitSnack(nouveau);
                JOptionPane.showMessageDialog(this, "Produit créé.", "Succès", JOptionPane.INFORMATION_MESSAGE);

            } else { // Mode Modification (inchangé)
                produitSelectionne.setNomProduit(nom);
                produitSelectionne.setDescription(description);
                produitSelectionne.setPrixVente(prix);
                produitSelectionne.setStock(stock);
                adminService.modifierProduitSnack(produitSelectionne);
                JOptionPane.showMessageDialog(this, "Produit modifié.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }

            // Rechargement et nettoyage
            chargerListeProduits();
            actionNouveau();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le prix et le stock doivent être des nombres valides.", "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionSupprimer() {
        if (produitSelectionne == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer '" + produitSelectionne.getNomProduit() + "' ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerProduitSnack(produitSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Produit supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerListeProduits();
                actionNouveau();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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

        panneauGauche = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listeProduits = new javax.swing.JList<>();
        panneauDroite = new javax.swing.JPanel();
        formPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nomField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        descriptionField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        prixField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        stockField = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        nouveauButton = new javax.swing.JButton();
        enregistrerButton = new javax.swing.JButton();
        supprimerButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        panneauGauche.setBorder(javax.swing.BorderFactory.createTitledBorder("Produits disponibles"));
        panneauGauche.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(250, 100));

        listeProduits.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listeProduitsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listeProduits);

        panneauGauche.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(panneauGauche, java.awt.BorderLayout.LINE_START);

        panneauDroite.setLayout(new java.awt.BorderLayout());

        formPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Détails du produit"));
        formPanel.setLayout(new java.awt.GridLayout(0, 2, 5, 5));

        jLabel1.setText("ID : ");
        formPanel.add(jLabel1);

        idField.setEditable(false);
        idField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                idFieldMouseClicked(evt);
            }
        });
        formPanel.add(idField);

        jLabel2.setText("Nom : ");
        formPanel.add(jLabel2);
        formPanel.add(nomField);

        jLabel3.setText("Description : ");
        formPanel.add(jLabel3);
        formPanel.add(descriptionField);

        jLabel4.setText("Prix (€) : ");
        formPanel.add(jLabel4);
        formPanel.add(prixField);

        jLabel5.setText("Stock : ");
        formPanel.add(jLabel5);
        formPanel.add(stockField);

        panneauDroite.add(formPanel, java.awt.BorderLayout.PAGE_START);

        nouveauButton.setText("Nouveau");
        nouveauButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(nouveauButton);

        enregistrerButton.setText("Enregistrer");
        enregistrerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(enregistrerButton);

        supprimerButton.setText("Supprimer");
        supprimerButton.setEnabled(false);
        supprimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(supprimerButton);

        panneauDroite.add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        add(panneauDroite, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void nouveauButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauButtonActionPerformed
        actionNouveau();// TODO add your handling code here:
    }//GEN-LAST:event_nouveauButtonActionPerformed

    private void enregistrerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enregistrerButtonActionPerformed
        actionEnregistrer();// TODO add your handling code here:
    }//GEN-LAST:event_enregistrerButtonActionPerformed

    private void supprimerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerButtonActionPerformed
        actionSupprimer();// TODO add your handling code here:
    }//GEN-LAST:event_supprimerButtonActionPerformed

    private void listeProduitsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listeProduitsValueChanged
        if (!evt.getValueIsAdjusting()) {
            produitSelectionne = listeProduits.getSelectedValue();
            mettreAJourChamps(produitSelectionne);
        }// TODO add your handling code here:
    }//GEN-LAST:event_listeProduitsValueChanged

    private void idFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_idFieldMouseClicked
        // On vérifie que le champ est bien désactivé pour l'édition avant d'afficher le message.
        if (!idField.isEditable()) {
            JOptionPane.showMessageDialog(
                    this, // Le composant parent (ce panneau)
                    "L'identifiant (ID) est généré automatiquement par le système lors de la création d'un nouvel élément.\n"
                    + "Il ne peut pas être modifié manuellement.", // Le message à afficher
                    "Information", // Le titre de la fenêtre pop-up
                    JOptionPane.INFORMATION_MESSAGE // L'icône "information"
            );
        }// TODO add your handling code here:
    }//GEN-LAST:event_idFieldMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JTextField descriptionField;
    private javax.swing.JButton enregistrerButton;
    private javax.swing.JPanel formPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<com.mycompany.cinema.ProduitSnack> listeProduits;
    private javax.swing.JTextField nomField;
    private javax.swing.JButton nouveauButton;
    private javax.swing.JPanel panneauDroite;
    private javax.swing.JPanel panneauGauche;
    private javax.swing.JTextField prixField;
    private javax.swing.JTextField stockField;
    private javax.swing.JButton supprimerButton;
    // End of variables declaration//GEN-END:variables

}
