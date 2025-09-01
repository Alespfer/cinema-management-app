package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fenêtre du Point de Vente pour les employés ayant le rôle "Vendeur".
 */
public class PointDeVenteFrame extends JFrame {

    private final AdminService adminService;
    private final Personnel vendeurConnecte;
    
    // Composants pour la liste des produits à gauche.

    private JList<ProduitSnack> listeProduits;
    private DefaultListModel<ProduitSnack> listModel;
    
    // Composants pour le panier à droite (le tableau).
    private JTable panierTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    
    
    // La structure de données qui contient le panier en mémoire (Produit -> Quantité).

    private Map<ProduitSnack, Integer> panier = new HashMap<>();
    private static final DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,##0.00 €");

    public PointDeVenteFrame(AdminService adminService, Personnel vendeur) {
        this.adminService = adminService;
        this.vendeurConnecte = vendeur;
        
        setTitle("Point de Vente - " + vendeur.getPrenom() + " " + vendeur.getNom());
        setSize(1024, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        chargerProduits();
    }

    private void initComponents() {
        // Un JSplitPane divise la fenêtre en deux parties redimensionnables.

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.4);

        // --- Panneau de gauche : Liste des produits ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Produits Disponibles"));
        listModel = new DefaultListModel<>();
        listeProduits = new JList<>(listModel);
        listeProduits.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ProduitSnack) {
                    ProduitSnack p = (ProduitSnack) value;
                    setText(p.getNomProduit() + " (" + CURRENCY_FORMATTER.format(p.getPrixVente()) + ") - Stock: " + p.getStock());
                }
                return this;
            }
        });
        leftPanel.add(new JScrollPane(listeProduits), BorderLayout.CENTER);
        JButton addButton = new JButton("Ajouter au Panier");
        leftPanel.add(addButton, BorderLayout.SOUTH);

        // --- Panneau de droite : Panier et validation ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Vente en Cours"));
        String[] columnNames = {"Produit", "Quantité", "Prix Unitaire", "Sous-total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        panierTable = new JTable(tableModel);
        rightPanel.add(new JScrollPane(panierTable), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("TOTAL : 0,00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton resetButton = new JButton("Annuler la Vente");
        JButton validateButton = new JButton("Valider la Vente");
        buttonsPanel.add(resetButton);
        buttonsPanel.add(validateButton);
        bottomPanel.add(buttonsPanel, BorderLayout.EAST);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        add(splitPane);
        
        // --- Listeners ---
        addButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { actionAjouter(); } });
        resetButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { actionAnnuler(); } });
        validateButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { actionValider(); } });
    }
    
     /**
     * Appelle le service pour récupérer la liste de tous les produits et les affiche.
     */

    private void chargerProduits() {
        try {
            listModel.clear();
            listModel.addAll(adminService.getAllProduitsSnack());
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur au chargement des produits.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
     /**
     * Action déclenchée par le bouton "Ajouter au Panier".
     * Ajoute le produit sélectionné au panier (la Map) et met à jour l'affichage.
     */

    private void actionAjouter() {
        ProduitSnack selected = listeProduits.getSelectedValue();
        if (selected == null) return;
        panier.put(selected, panier.getOrDefault(selected, 0) + 1);
        mettreAJourPanier();
    }
    
    
     /**
     * Action déclenchée par le bouton "Annuler la Vente".
     * Vide complètement le panier et met à jour l'affichage.
     */
    
    private void actionAnnuler() {
        panier.clear();
        mettreAJourPanier();
    }
    
    
    
     /**
     * Action déclenchée par le bouton "Valider la Vente".
     * Appelle le service pour enregistrer la vente en base de données.
     */
    private void actionValider() {
        if (panier.isEmpty()) return;
        try {
            // Pour l'exemple, on prend la caisse 1 en dur.
            adminService.enregistrerVenteSnack(vendeurConnecte.getId(), 1, panier);
            JOptionPane.showMessageDialog(this, "Vente enregistrée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            actionAnnuler(); // Vide le panier
            chargerProduits(); // Recharge la liste pour voir les stocks mis à jour
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur de Vente", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    /**
     * Rafraîchit le contenu du JTable du panier et le label du total
     * en se basant sur le contenu actuel de la Map 'panier'.
     */
    private void mettreAJourPanier() {
        tableModel.setRowCount(0);
        double totalGeneral = 0.0;
        for (Map.Entry<ProduitSnack, Integer> entry : panier.entrySet()) {
            ProduitSnack p = entry.getKey();
            Integer qte = entry.getValue();
            double sousTotal = p.getPrixVente() * qte;
            tableModel.addRow(new Object[]{
                p.getNomProduit(),
                qte,
                CURRENCY_FORMATTER.format(p.getPrixVente()),
                CURRENCY_FORMATTER.format(sousTotal)
            });
            totalGeneral += sousTotal;
        }
        totalLabel.setText("TOTAL : " + CURRENCY_FORMATTER.format(totalGeneral));
    }
}