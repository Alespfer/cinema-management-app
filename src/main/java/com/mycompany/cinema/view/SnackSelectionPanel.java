package com.mycompany.cinema.view;

import com.mycompany.cinema.ProduitSnack;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * C'est le dernier panneau du processus de commande.
 * Il affiche la liste des produits de snacking disponibles et permet à l'utilisateur
 * d'en ajouter à sa commande via des JSpinners (compteurs numériques).
 * Il propose de finaliser la commande avec ou sans snacks.
 */
public class SnackSelectionPanel extends JPanel {

    private final ClientService clientService;
    // Une Map qui lie chaque objet ProduitSnack à son JSpinner correspondant.
    private final Map<ProduitSnack, JSpinner> spinnerMap = new HashMap<>();
    private JLabel totalLabel;
    private static final DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,##0.00 €");

    // Interface pour notifier le parent que la sélection de snacks est terminée.
    public interface SnackSelectionListener {
        void onSnackSelectionCompleted(Map<ProduitSnack, Integer> cart);
    }
    private SnackSelectionListener listener;

    // Interface pour gérer la navigation (retour ou "ignorer").
    public interface NavigationListener {
        void onRetourToSieges();
        void onSkip();
    }
    private NavigationListener navigationListener;

    public SnackSelectionPanel(ClientService clientService) {
        this.clientService = clientService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Étape 3/3 : Ajouter une collation ?"));
        initComponents();
    }

    private void initComponents() {
        // Le panneau central contiendra une liste verticale de produits.
        JPanel productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(productListPanel);

        // On récupère tous les produits disponibles via le service.
        List<ProduitSnack> produits = clientService.getAllProduitsSnack();
        for (ProduitSnack produit : produits) {
            // On n'affiche que les produits qui sont en stock.
            if (produit.getStock() > 0) {
                // Pour chaque produit, on crée un petit panneau dédié et on l'ajoute à la liste.
                productListPanel.add(createProductPanel(produit));
                productListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Petit espace vertical.
            }
        }

        // --- Panneau du bas ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10,10));
        totalLabel = new JLabel("Total Snacks: " + CURRENCY_FORMATTER.format(0.0));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton retourButton = new JButton("<< Retour aux sièges");
        JButton skipButton = new JButton("Non, merci (Paiement)");
        JButton validerButton = new JButton("Valider avec Snacks (Paiement) >>");

        buttonPanel.add(retourButton);
        buttonPanel.add(skipButton);
        buttonPanel.add(validerButton);
        
        bottomPanel.add(totalLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        retourButton.addActionListener(e -> { if (navigationListener != null) navigationListener.onRetourToSieges(); });
        skipButton.addActionListener(e -> { if (navigationListener != null) navigationListener.onSkip(); });
        validerButton.addActionListener(e -> { if (listener != null) listener.onSnackSelectionCompleted(getCart()); });
    }

    /**
     * Crée un sous-panneau pour un seul produit, contenant son nom, son prix et un compteur (JSpinner).
     * @param produit Le produit à afficher.
     * @return Le JPanel configuré.
     */
    private JPanel createProductPanel(final ProduitSnack produit) {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        String labelText = "<html><b>" + produit.getNomProduit() + "</b><br>" 
                + "<i>" + produit.getDescription() + "</i><br>"
                + "<font color='blue'>Prix: " + CURRENCY_FORMATTER.format(produit.getPrixVente()) + "</font></html>";
        JLabel productLabel = new JLabel(labelText);
        
        // Le Spinner est configuré pour aller de 0 au stock maximum disponible.
        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, produit.getStock(), 1);
        final JSpinner spinner = new JSpinner(spinnerModel);
        
        // Chaque fois que la valeur du spinner change, on met à jour le total.
        spinner.addChangeListener(e -> updateTotal());
        spinnerMap.put(produit, spinner); // On stocke la référence au spinner.

        panel.add(productLabel, BorderLayout.CENTER);
        panel.add(spinner, BorderLayout.EAST);
        return panel;
    }

    /**
     * Calcule et met à jour le label du total en parcourant la map des spinners.
     */
    private void updateTotal() {
        double total = 0.0;
        for (Map.Entry<ProduitSnack, JSpinner> entry : spinnerMap.entrySet()) {
            ProduitSnack produit = entry.getKey();
            int quantite = (Integer) entry.getValue().getValue();
            total += produit.getPrixVente() * quantite;
        }
        totalLabel.setText("Total Snacks: " + CURRENCY_FORMATTER.format(total));
    }

    /**
     * Construit le "panier" (une Map) à partir des valeurs actuelles des spinners.
     * @return Une Map contenant uniquement les produits avec une quantité > 0.
     */
    private Map<ProduitSnack, Integer> getCart() {
        Map<ProduitSnack, Integer> cart = new HashMap<>();
        for (Map.Entry<ProduitSnack, JSpinner> entry : spinnerMap.entrySet()) {
            int quantite = (Integer) entry.getValue().getValue();
            if (quantite > 0) {
                cart.put(entry.getKey(), quantite);
            }
        }
        return cart;
    }

    /**
     * Réinitialise tous les spinners à 0. Appelé quand on arrive sur ce panneau.
     */
    public void resetPanel() {
        for (JSpinner spinner : spinnerMap.values()) {
            spinner.setValue(0);
        }
        updateTotal();
    }

    // Setters pour les listeners.
    public void setListener(SnackSelectionListener listener) { this.listener = listener; }
    public void setNavigationListener(NavigationListener navigationListener) { this.navigationListener = navigationListener; }
}