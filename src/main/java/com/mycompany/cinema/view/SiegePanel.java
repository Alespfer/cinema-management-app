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

/**
 * Axelle, voici le panneau qui affiche le plan de la salle et permet à l'utilisateur
 * de sélectionner ses sièges. Il affiche les sièges disponibles, occupés et sélectionnés
 * avec des couleurs différentes. Il permet aussi de choisir un tarif et affiche
 * le prix total des billets en temps réel.
 */
public class SiegePanel extends JPanel {
    // Le service pour toute la logique métier.
    private final ClientService clientService;
    // La séance pour laquelle on affiche les sièges.
    private Seance currentSeance;
    // Le conteneur où les boutons des sièges seront ajoutés dynamiquement.
    private JPanel planSallePanel;
    private JButton reserveButton;
    // Une Map est essentielle ici pour lier chaque bouton (JToggleButton) à son objet Siege correspondant.
    private Map<JToggleButton, Siege> siegeButtonMap = new HashMap<>();
    private JComboBox<Tarif> tarifComboBox;
    private JLabel prixTotalLabel;
    
    // Constantes de couleur pour une maintenance facile et une bonne lisibilité.
    private static final Color COULEUR_DISPONIBLE = new Color(34, 177, 76); // Vert
    private static final Color COULEUR_OCCUPE = new Color(237, 28, 36);      // Rouge
    private static final Color COULEUR_SELECTIONNE = new Color(63, 72, 204); // Bleu

    // Interfaces pour communiquer avec la fenêtre parente (ClientMainFrame).
    public interface RetourListener { void onRetourClicked(); }
    private RetourListener retourListener;

    public interface ReservationListener {
        void onReservationDetailsSelected(List<Siege> sieges, Tarif tarif);
    }
    private ReservationListener reservationListener;

    public SiegePanel(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        initComponents();
        loadTarifs(); // Charge la liste des tarifs dans la JComboBox dès la création.
    }

    /**
     * Construit et organise tous les composants graphiques du panneau.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Étape 2/3 : Choisissez vos places et votre tarif"));
        planSallePanel = new JPanel();
        add(new JScrollPane(planSallePanel), BorderLayout.CENTER);
        
        // --- Panneau du bas avec les contrôles de navigation et de prix ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 5));
        JButton retourButton = new JButton("<< Retour aux détails du film");
        
        JPanel tarifPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tarifComboBox = new JComboBox<Tarif>();
        tarifPanel.add(new JLabel("Tarif:"));
        tarifPanel.add(tarifComboBox);
        
        prixTotalLabel = new JLabel("Prix total : 0.00 €");
        prixTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        reserveButton = new JButton("Continuer vers les Snacks >>");
        
        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        eastPanel.add(prixTotalLabel);
        eastPanel.add(reserveButton);
        
        bottomPanel.add(retourButton, BorderLayout.WEST);
        bottomPanel.add(tarifPanel, BorderLayout.CENTER);
        bottomPanel.add(eastPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // --- Listeners (Écouteurs d'événements) ---
        reserveButton.addActionListener(e -> handleContinueToSnacks());
        tarifComboBox.addActionListener(e -> updatePrixTotal()); // Le prix se met à jour si on change de tarif.
        retourButton.addActionListener(e -> { if (retourListener != null) retourListener.onRetourClicked(); });
    }

    /**
     * Charge tous les tarifs disponibles depuis le service et les ajoute à la JComboBox.
     */
    private void loadTarifs() {
        try {
            List<Tarif> tarifs = clientService.getAllTarifs();
            for (Tarif tarif : tarifs) {
                tarifComboBox.addItem(tarif);
            }
            // Personnalise l'affichage pour chaque tarif dans la liste déroulante pour être plus clair.
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
    
    /**
     * Met à jour le label du prix total en fonction du tarif et du nombre de sièges sélectionnés.
     * Cette méthode est appelée à chaque clic sur un siège ou changement de tarif.
     */
    private void updatePrixTotal() {
        Tarif tarifSelectionne = (Tarif) tarifComboBox.getSelectedItem();
        if (tarifSelectionne == null) return;
        
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

    /**
     * Gère le clic sur le bouton "Continuer".
     * Récupère les sièges sélectionnés et le tarif, puis notifie le parent pour passer à l'étape suivante.
     */
    private void handleContinueToSnacks() {
        List<Siege> siegesSelectionnes = new ArrayList<>();
        for (Map.Entry<JToggleButton, Siege> entry : siegeButtonMap.entrySet()) {
            if (entry.getKey().isSelected()) {
                siegesSelectionnes.add(entry.getValue());
            }
        }
        
        Tarif tarifSelectionne = (Tarif) tarifComboBox.getSelectedItem();

        if (siegesSelectionnes.isEmpty() || tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un siège et un tarif.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Notifie la fenêtre principale des détails de la sélection pour qu'elle puisse avancer.
        if (reservationListener != null) {
            reservationListener.onReservationDetailsSelected(siegesSelectionnes, tarifSelectionne);
        }
    }

    /**
     * Axelle : C'est la méthode la plus complexe de ce panneau.
     * Elle reconstruit dynamiquement le plan de la salle pour une séance donnée.
     * @param seance La séance pour laquelle afficher le plan.
     */
    public void displaySieges(Seance seance) {
        this.currentSeance = seance;
        planSallePanel.removeAll(); // On vide le plan précédent.
        siegeButtonMap.clear();

        if (seance == null) {
            planSallePanel.revalidate(); planSallePanel.repaint();
            updatePrixTotal(); return;
        }

        // On récupère tous les sièges de la salle et tous les billets déjà vendus pour cette séance.
        List<Siege> tousLesSieges = clientService.getSiegesPourSalle(seance.getIdSalle());
        List<Billet> billetsVendus = clientService.getBilletsPourSeance(seance.getId());
        List<Integer> idsSiegesOccupes = new ArrayList<>();
        for (Billet billet : billetsVendus) idsSiegesOccupes.add(billet.getIdSiege());

        // On détermine la taille de la grille (nombre de rangées et de sièges par rangée).
        int maxRangee = 0, maxSiegeNum = 0;
        for (Siege siege : tousLesSieges) {
            if (siege.getNumeroRangee() > maxRangee) maxRangee = siege.getNumeroRangee();
            if (siege.getNumeroSiege() > maxSiegeNum) maxSiegeNum = siege.getNumeroSiege();
        }

        // On crée la grille pour y placer les boutons.
        planSallePanel.setLayout(new GridLayout(maxRangee, maxSiegeNum, 5, 5));
        JToggleButton[][] siegeButtonsGrid = new JToggleButton[maxRangee][maxSiegeNum];

        // On parcourt chaque siège de la salle pour créer un bouton correspondant.
        for (Siege siege : tousLesSieges) {
            JToggleButton siegeButton = new JToggleButton(String.valueOf(siege.getNumeroSiege()));
            
            // =====================================================================
            // === DÉBUT DE LA CORRECTION : La ligne qui fixe le bug visuel      ===
            // =====================================================================

            // Axelle : C'est cette ligne qui manquait. Elle force le bouton
            // à peindre lui-même son arrière-plan. Sans elle, le système d'exploitation
            // (surtout macOS) peut ignorer la couleur que nous définissons, ce qui rendait les boutons blancs.
            siegeButton.setOpaque(true);

            // =====================================================================
            // === FIN DE LA CORRECTION                                          ===
            // =====================================================================
            
            siegeButton.setForeground(Color.WHITE);
            siegeButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            siegeButtonMap.put(siegeButton, siege); // On lie le bouton à l'objet Siège.

            // Si le siège a déjà été vendu...
            if (idsSiegesOccupes.contains(siege.getId())) {
                siegeButton.setEnabled(false); // ...on le désactive...
                siegeButton.setBackground(COULEUR_OCCUPE); // ...et on le met en rouge.
            } else {
                // Sinon, il est disponible.
                siegeButton.setBackground(COULEUR_DISPONIBLE);
                // On lui ajoute un écouteur qui change sa couleur au clic et met à jour le prix.
                siegeButton.addActionListener(e -> {
                    siegeButton.setBackground(siegeButton.isSelected() ? COULEUR_SELECTIONNE : COULEUR_DISPONIBLE);
                    updatePrixTotal();
                });
            }
            // On place le bouton dans notre grille temporaire à la bonne position.
            siegeButtonsGrid[siege.getNumeroRangee() - 1][siege.getNumeroSiege() - 1] = siegeButton;
        }
        
        // On remplit le panneau final à partir de notre grille.
        for (int i = 0; i < maxRangee; i++) {
            for (int j = 0; j < maxSiegeNum; j++) {
                // On ajoute un panneau vide pour les "trous" s'il y en a dans la numérotation.
                planSallePanel.add((siegeButtonsGrid[i][j] != null) ? siegeButtonsGrid[i][j] : new JPanel());
            }
        }
        
        // On force le rafraîchissement de l'affichage.
        planSallePanel.revalidate();
        planSallePanel.repaint();
        updatePrixTotal();
    }
    
    // Setters pour que la fenêtre parente puisse "brancher" ses listeners.
    public void setRetourListener(RetourListener listener) { this.retourListener = listener; }
    public void setReservationListener(ReservationListener listener) { this.reservationListener = listener; }
}