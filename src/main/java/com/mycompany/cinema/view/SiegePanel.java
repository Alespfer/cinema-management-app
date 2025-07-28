package com.mycompany.cinema.view;

import com.mycompany.cinema.Billet;
import com.mycompany.cinema.Client;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.Siege;
import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiegePanel extends JPanel {
    private final ClientService clientService;
    private Seance currentSeance;
    private JPanel planSallePanel;
    private JButton reserveButton;
    private Map<JToggleButton, Siege> siegeButtonMap = new HashMap<>();
    private JComboBox<Tarif> tarifComboBox;
    private JLabel prixTotalLabel;
    private static final Color COULEUR_DISPONIBLE = new Color(34, 177, 76);
    private static final Color COULEUR_OCCUPE = new Color(237, 28, 36);
    private static final Color COULEUR_SELECTIONNE = new Color(63, 72, 204);

    public interface RetourListener { void onRetourClicked(); }
    private RetourListener retourListener;

    // --- INTERFACE DE LISTENER MODIFIÉE ---
    public interface ReservationListener {
        void onReservationDetailsSelected(List<Siege> sieges, Tarif tarif);
    }
    private ReservationListener reservationListener;

    public SiegePanel(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        initComponents();
        loadTarifs();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Étape 2/3 : Choisissez vos places et votre tarif"));
        planSallePanel = new JPanel();
        add(new JScrollPane(planSallePanel), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 5));
        JButton retourButton = new JButton("<< Retour aux détails du film");
        JPanel tarifPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tarifComboBox = new JComboBox<Tarif>();
        tarifPanel.add(new JLabel("Tarif:"));
        tarifPanel.add(tarifComboBox);
        prixTotalLabel = new JLabel("Prix total : 0.00 €");
        prixTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        reserveButton = new JButton("Continuer vers les Snacks >>"); // Texte du bouton mis à jour
        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        eastPanel.add(prixTotalLabel);
        eastPanel.add(reserveButton);
        bottomPanel.add(retourButton, BorderLayout.WEST);
        bottomPanel.add(tarifPanel, BorderLayout.CENTER);
        bottomPanel.add(eastPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        
        reserveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleContinueToSnacks();
            }
        });
        tarifComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePrixTotal();
            }
        });
        retourButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (retourListener != null) {
                    retourListener.onRetourClicked();
                }
            }
        });
    }

    private void loadTarifs() {
        try {
            List<Tarif> tarifs = clientService.getAllTarifs();
            for (Tarif tarif : tarifs) {
                tarifComboBox.addItem(tarif);
            }
            tarifComboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Tarif) {
                        Tarif t = (Tarif) value;
                        setText(t.getLibelle() + " - " + String.format("%.2f", t.getPrix()) + " €");
                    }
                    return this;
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des tarifs.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updatePrixTotal() {
        Tarif tarifSelectionne = (Tarif) tarifComboBox.getSelectedItem();
        if (tarifSelectionne == null) { return; }
        int nombreDeSieges = 0;
        for (JToggleButton button : siegeButtonMap.keySet()) {
            if (button.isSelected()) {
                nombreDeSieges++;
            }
        }
        double prixTotal = tarifSelectionne.getPrix() * nombreDeSieges;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        prixTotalLabel.setText("Prix billets : " + df.format(prixTotal) + " €");
    }

    private void handleContinueToSnacks() {
        List<Siege> siegesSelectionnes = new ArrayList<>();
        for (Map.Entry<JToggleButton, Siege> entry : siegeButtonMap.entrySet()) {
            if (entry.getKey().isSelected()) {
                siegesSelectionnes.add(entry.getValue());
            }
        }
        
        Tarif tarifSelectionne = (Tarif) tarifComboBox.getSelectedItem();

        if (siegesSelectionnes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un siège.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un tarif.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reservationListener != null) {
            reservationListener.onReservationDetailsSelected(siegesSelectionnes, tarifSelectionne);
        }
    }

    public void displaySieges(Seance seance) {
        this.currentSeance = seance;
        planSallePanel.removeAll();
        siegeButtonMap.clear();

        if (seance == null) {
            planSallePanel.revalidate(); planSallePanel.repaint();
            updatePrixTotal(); return;
        }

        List<Siege> tousLesSieges = clientService.getSiegesPourSalle(seance.getIdSalle());
        List<Billet> billetsVendus = clientService.getBilletsPourSeance(seance.getId());
        List<Integer> idsSiegesOccupes = new ArrayList<>();
        for (Billet billet : billetsVendus) idsSiegesOccupes.add(billet.getIdSiege());

        int maxRangee = 0, maxSiegeNum = 0;
        for (Siege siege : tousLesSieges) {
            if (siege.getNumeroRangee() > maxRangee) maxRangee = siege.getNumeroRangee();
            if (siege.getNumeroSiege() > maxSiegeNum) maxSiegeNum = siege.getNumeroSiege();
        }

        planSallePanel.setLayout(new GridLayout(maxRangee, maxSiegeNum, 5, 5));
        JToggleButton[][] siegeButtonsGrid = new JToggleButton[maxRangee][maxSiegeNum];

        for (Siege siege : tousLesSieges) {
            final JToggleButton siegeButton = new JToggleButton(String.valueOf(siege.getNumeroSiege()));
            siegeButton.setOpaque(true); siegeButton.setForeground(Color.WHITE);
            siegeButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            siegeButtonMap.put(siegeButton, siege);

            if (idsSiegesOccupes.contains(siege.getId())) {
                siegeButton.setEnabled(false); siegeButton.setBackground(COULEUR_OCCUPE);
            } else {
                siegeButton.setBackground(COULEUR_DISPONIBLE);
                siegeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        siegeButton.setBackground(siegeButton.isSelected() ? COULEUR_SELECTIONNE : COULEUR_DISPONIBLE);
                        updatePrixTotal();
                    }
                });
            }
            siegeButtonsGrid[siege.getNumeroRangee() - 1][siege.getNumeroSiege() - 1] = siegeButton;
        }
        
        for (int i = 0; i < maxRangee; i++) {
            for (int j = 0; j < maxSiegeNum; j++) {
                planSallePanel.add((siegeButtonsGrid[i][j] != null) ? siegeButtonsGrid[i][j] : new JPanel());
            }
        }
        
        planSallePanel.revalidate(); planSallePanel.repaint();
        updatePrixTotal();
    }
    
    public Seance getCurrentSeance() { return this.currentSeance; }
    public void setRetourListener(RetourListener listener) { this.retourListener = listener; }
    public void setReservationListener(ReservationListener listener) { this.reservationListener = listener; }
}