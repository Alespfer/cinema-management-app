package com.mycompany.cinema.view;

import com.mycompany.cinema.Billet;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.Siege;
import com.mycompany.cinema.service.ClientService;
import com.mycompany.cinema.Client; // <-- Ajouter cet import


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiegePanel extends JPanel {
    private final ClientService clientService;
    private final Client clientConnecte; 
    private Seance currentSeance;
    private JPanel planSallePanel;
    private JButton reserveButton;
    private Map<JToggleButton, Siege> siegeButtonMap = new HashMap<>();

     public SiegePanel(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte; // <-- Initialiser le nouveau champ
        initComponents();
    }


    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Choisissez vos places"));

        planSallePanel = new JPanel();
        add(new JScrollPane(planSallePanel), BorderLayout.CENTER);

        reserveButton = new JButton("Réserver les places sélectionnées");
        add(reserveButton, BorderLayout.SOUTH);

        reserveButton.addActionListener(e -> {
            if (currentSeance == null) {
                JOptionPane.showMessageDialog(this, "Veuillez d'abord sélectionner une séance.", "Action impossible", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            List<Integer> selectedSiegeIds = new ArrayList<>();
            for (Map.Entry<JToggleButton, Siege> entry : siegeButtonMap.entrySet()) {
                if (entry.getKey().isSelected()) {
                    selectedSiegeIds.add(entry.getValue().getId());
                }
            }

            if (selectedSiegeIds.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un siège.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Dans une vraie application, l'ID client viendrait de l'objet Client connecté
            // et le tarif serait sélectionné dans une liste déroulante.
            int clientId = this.clientConnecte.getId();
            int tarifId = 1;  // Plein Tarif (en dur pour le test)

            try {
                clientService.effectuerReservation(clientId, currentSeance.getId(), selectedSiegeIds, tarifId);
                
                JOptionPane.showMessageDialog(this, "Réservation effectuée avec succès pour " + this.clientConnecte.getNom() + " !", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                
                displaySieges(currentSeance);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la réservation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void displaySieges(Seance seance) {
        this.currentSeance = seance;
        planSallePanel.removeAll();
        siegeButtonMap.clear();

        if (seance == null) {
            planSallePanel.revalidate();
            planSallePanel.repaint();
            return;
        }

        List<Siege> tousLesSieges = clientService.getSiegesPourSalle(seance.getIdSalle());
        List<Billet> billetsVendus = clientService.getBilletsPourSeance(seance.getId());
        List<Integer> idsSiegesOccupes = new ArrayList<>();
        for (Billet billet : billetsVendus) {
            idsSiegesOccupes.add(billet.getIdSiege());
        }

        int maxRangee = 0;
        int maxSiegeNum = 0;
        for (Siege siege : tousLesSieges) {
            if (siege.getNumeroRangee() > maxRangee) maxRangee = siege.getNumeroRangee();
            if (siege.getNumeroSiege() > maxSiegeNum) maxSiegeNum = siege.getNumeroSiege();
        }

        planSallePanel.setLayout(new GridLayout(maxRangee, maxSiegeNum, 5, 5));

        // Créer un tableau 2D de boutons pour un affichage correct
        JToggleButton[][] siegeButtonsGrid = new JToggleButton[maxRangee][maxSiegeNum];

        for (Siege siege : tousLesSieges) {
            JToggleButton siegeButton = new JToggleButton(String.valueOf(siege.getNumeroSiege()));
            siegeButtonMap.put(siegeButton, siege);
            
            if (idsSiegesOccupes.contains(siege.getId())) {
                siegeButton.setEnabled(false);
                siegeButton.setBackground(Color.RED);
            } else {
                siegeButton.setBackground(Color.GREEN);
            }
            // Placer le bouton au bon endroit dans la grille
            siegeButtonsGrid[siege.getNumeroRangee() - 1][siege.getNumeroSiege() - 1] = siegeButton;
        }
        
        // Ajouter les boutons de la grille au panneau
        for (int i = 0; i < maxRangee; i++) {
            for (int j = 0; j < maxSiegeNum; j++) {
                if (siegeButtonsGrid[i][j] != null) {
                    planSallePanel.add(siegeButtonsGrid[i][j]);
                } else {
                    planSallePanel.add(new JPanel()); // Espace vide pour les sièges non existants
                }
            }
        }
        
        planSallePanel.revalidate();
        planSallePanel.repaint();
    }
    
    public void clearPanel() {
        displaySieges(null);
    }
}