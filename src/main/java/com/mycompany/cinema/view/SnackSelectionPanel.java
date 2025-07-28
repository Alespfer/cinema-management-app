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

public class SnackSelectionPanel extends JPanel {

    private final ClientService clientService;
    private final Map<ProduitSnack, JSpinner> spinnerMap = new HashMap<>();
    private JLabel totalLabel;
    private static final DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,##0.00 €");

    public interface SnackSelectionListener {
        void onSnackSelectionCompleted(Map<ProduitSnack, Integer> cart);
    }
    private SnackSelectionListener listener;

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
        JPanel productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(productListPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        List<ProduitSnack> produits = clientService.getAllProduitsSnack();
        for (ProduitSnack produit : produits) {
            if (produit.getStock() > 0) {
                productListPanel.add(createProductPanel(produit));
                productListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

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

        retourButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (navigationListener != null) navigationListener.onRetourToSieges();
            }
        });

        skipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (navigationListener != null) navigationListener.onSkip();
            }
        });

        validerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listener != null) listener.onSnackSelectionCompleted(getCart());
            }
        });
    }

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
        
        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, produit.getStock(), 1);
        final JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setPreferredSize(new Dimension(60, (int)spinner.getPreferredSize().getHeight()));
        
        // Listener conforme aux contraintes
        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateTotal();
            }
        });
        spinnerMap.put(produit, spinner);

        panel.add(productLabel, BorderLayout.CENTER);
        panel.add(spinner, BorderLayout.EAST);
        return panel;
    }

    private void updateTotal() {
        double total = 0.0;
        for (Map.Entry<ProduitSnack, JSpinner> entry : spinnerMap.entrySet()) {
            ProduitSnack produit = entry.getKey();
            int quantite = (Integer) entry.getValue().getValue();
            total += produit.getPrixVente() * quantite;
        }
        totalLabel.setText("Total Snacks: " + CURRENCY_FORMATTER.format(total));
    }

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

    public void resetPanel() {
        for (JSpinner spinner : spinnerMap.values()) {
            spinner.setValue(0);
        }
        updateTotal();
    }

    public void setListener(SnackSelectionListener listener) { this.listener = listener; }
    public void setNavigationListener(NavigationListener navigationListener) { this.navigationListener = navigationListener; }
}