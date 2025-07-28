package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.service.AdminService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panneau de gestion (CRUD) pour les Produits de Snacking.
 */
public class GestionProduitsSnackPanel extends JPanel {

    private final AdminService adminService;

    private JList<ProduitSnack> listeProduits;
    private DefaultListModel<ProduitSnack> listModel;
    
    private JTextField idField, nomField, descriptionField, prixField, stockField;
    private JButton nouveauBouton, enregistrerBouton, supprimerBouton;

    private ProduitSnack produitSelectionne;

    public GestionProduitsSnackPanel(AdminService adminService) {
        this.adminService = adminService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        initListeners();
        chargerListeProduits();
    }

    private void initComponents() {
        // Panneau de gauche : Liste
        JPanel panneauGauche = new JPanel(new BorderLayout());
        panneauGauche.setBorder(BorderFactory.createTitledBorder("Produits disponibles"));
        listModel = new DefaultListModel<>();
        listeProduits = new JList<>(listModel);
        listeProduits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        panneauGauche.add(new JScrollPane(listeProduits), BorderLayout.CENTER);

        // Panneau de droite : Formulaire
        JPanel panneauDroite = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Détails du produit"));

        idField = new JTextField(); idField.setEditable(false);
        nomField = new JTextField();
        descriptionField = new JTextField();
        prixField = new JTextField();
        stockField = new JTextField();

        formPanel.add(new JLabel("ID :")); formPanel.add(idField);
        formPanel.add(new JLabel("Nom :")); formPanel.add(nomField);
        formPanel.add(new JLabel("Description :")); formPanel.add(descriptionField);
        formPanel.add(new JLabel("Prix (€) :")); formPanel.add(prixField);
        formPanel.add(new JLabel("Stock :")); formPanel.add(stockField);
        panneauDroite.add(formPanel, BorderLayout.CENTER);

        // Panneau des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nouveauBouton = new JButton("Nouveau");
        enregistrerBouton = new JButton("Enregistrer");
        supprimerBouton = new JButton("Supprimer");
        supprimerBouton.setEnabled(false);
        buttonPanel.add(nouveauBouton); buttonPanel.add(enregistrerBouton); buttonPanel.add(supprimerBouton);
        panneauDroite.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panneauGauche, BorderLayout.WEST);
        add(panneauDroite, BorderLayout.CENTER);
    }

    private void initListeners() {
        listeProduits.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    produitSelectionne = listeProduits.getSelectedValue();
                    mettreAJourChamps(produitSelectionne);
                }
            }
        });
        nouveauBouton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { actionNouveau(); } });
        enregistrerBouton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { actionEnregistrer(); } });
        supprimerBouton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { actionSupprimer(); } });
    }

    private void chargerListeProduits() {
        try {
            listModel.clear();
            listModel.addAll(adminService.getAllProduitsSnack());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement produits: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mettreAJourChamps(ProduitSnack p) {
        if (p != null) {
            idField.setText(String.valueOf(p.getId()));
            nomField.setText(p.getNomProduit());
            descriptionField.setText(p.getDescription());
            prixField.setText(String.format("%.2f", p.getPrixVente()).replace(',', '.'));
            stockField.setText(String.valueOf(p.getStock()));
            supprimerBouton.setEnabled(true);
        } else {
            idField.setText(""); nomField.setText(""); descriptionField.setText("");
            prixField.setText(""); stockField.setText("");
            supprimerBouton.setEnabled(false);
        }
    }
    
    private void actionNouveau() {
        produitSelectionne = null;
        listeProduits.clearSelection();
        mettreAJourChamps(null);
    }

    private void actionEnregistrer() {
        try {
            String nom = nomField.getText();
            if (nom == null || nom.trim().isEmpty()) throw new Exception("Le nom est obligatoire.");
            String description = descriptionField.getText();
            double prix = Double.parseDouble(prixField.getText().replace(',', '.'));
            int stock = Integer.parseInt(stockField.getText());

            if (produitSelectionne == null) { // Création
                ProduitSnack nouveau = new ProduitSnack(0, nom, description, prix, stock);
                adminService.ajouterProduitSnack(nouveau);
                JOptionPane.showMessageDialog(this, "Produit créé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else { // Modification
                produitSelectionne.setNomProduit(nom);
                produitSelectionne.setDescription(description);
                produitSelectionne.setPrixVente(prix);
                produitSelectionne.setStock(stock);
                adminService.modifierProduitSnack(produitSelectionne);
                JOptionPane.showMessageDialog(this, "Produit modifié.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
            chargerListeProduits();
            actionNouveau();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le prix et le stock doivent être des nombres valides.", "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actionSupprimer() {
        if (produitSelectionne == null) return;
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
}