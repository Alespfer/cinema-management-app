package com.mycompany.cinema.view;

import com.mycompany.cinema.Billet;
import com.mycompany.cinema.Client;
import com.mycompany.cinema.Reservation; // Importation nécessaire
import com.mycompany.cinema.Salle; // Importation nécessaire
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.Siege;
import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter; // Importation nécessaire
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional; // Importation nécessaire

public class SiegePanel extends JPanel {
    // ... (Tout le début du fichier reste identique)
    private final ClientService clientService;
    private final Client clientConnecte;
    private Seance currentSeance;
    private JPanel planSallePanel;
    private JButton reserveButton;
    private Map<JToggleButton, Siege> siegeButtonMap = new HashMap<>();
    private JComboBox<Tarif> tarifComboBox;
    private JLabel prixTotalLabel;
    private static final Color COULEUR_DISPONIBLE = new Color(34, 177, 76);
    private static final Color COULEUR_OCCUPE = new Color(237, 28, 36);
    private static final Color COULEUR_SELECTIONNE = new Color(63, 72, 204);

    public interface RetourListener {
        void onRetourClicked();
    }
    private RetourListener retourListener;

    public SiegePanel(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        this.clientConnecte = clientConnecte;
        initComponents();
        loadTarifs();
    }

    private void initComponents() {
        // ... (cette méthode reste identique)
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Choisissez vos places et votre tarif"));
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
        reserveButton = new JButton("Valider la réservation");
        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        eastPanel.add(prixTotalLabel);
        eastPanel.add(reserveButton);
        bottomPanel.add(retourButton, BorderLayout.WEST);
        bottomPanel.add(tarifPanel, BorderLayout.CENTER);
        bottomPanel.add(eastPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        initListeners(retourButton);
    }
    
    private void initListeners(JButton retourButton) {
        // ... (cette méthode reste identique)
        reserveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleReservation();
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
        // ... (cette méthode reste identique)
        try {
            List<Tarif> tarifs = clientService.getAllTarifs();
            tarifComboBox.removeAllItems();
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
        // ... (cette méthode reste identique)
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
        prixTotalLabel.setText("Prix total : " + df.format(prixTotal) + " €");
    }

    /**
     * Logique exécutée lors du clic sur "Valider la réservation".
     * Modifiée pour inclure la génération du billet après la réservation.
     */
    private void handleReservation() {
        if (currentSeance == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une séance.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Siege> siegesSelectionnes = new ArrayList<>();
        List<Integer> selectedSiegeIds = new ArrayList<>();
        for (Map.Entry<JToggleButton, Siege> entry : siegeButtonMap.entrySet()) {
            if (entry.getKey().isSelected()) {
                selectedSiegeIds.add(entry.getValue().getId());
                siegesSelectionnes.add(entry.getValue()); // On garde l'objet Siège complet.
            }
        }
        
        Tarif tarifSelectionne = (Tarif) tarifComboBox.getSelectedItem();

        if (selectedSiegeIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un siège.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un tarif.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // ÉTAPE 1 : Exécuter la réservation et récupérer l'objet Reservation créé.
            Reservation reservation = clientService.effectuerReservation(clientConnecte.getId(), currentSeance.getId(), selectedSiegeIds, tarifSelectionne.getId());
            
            // =====================================================================
            // === DÉBUT DE L'AJOUT : Préparation et affichage du billet       ===
            // =====================================================================
            
            // ÉTAPE 2 : Rassembler toutes les informations pour le billet.
            BilletInfo infosBillet = new BilletInfo();
            infosBillet.clientNom = clientConnecte.getNom();
            infosBillet.reservationId = reservation.getId();
            infosBillet.filmTitre = clientService.getFilmDetails(currentSeance.getIdFilm()).getTitre();
            // On cherche le nom de la salle (amélioration par rapport à l'ID seul).
            // Remarque : cette recherche pourrait être optimisée dans un vrai projet (ex: Map en cache).
            Optional<Salle> salleOpt = clientService.getAllSalles().stream().filter(s -> s.getId() == currentSeance.getIdSalle()).findFirst();
            infosBillet.salleNumero = salleOpt.isPresent() ? salleOpt.get().getNumero() : "Salle " + currentSeance.getIdSalle();

            infosBillet.seanceDateHeure = currentSeance.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
            infosBillet.sieges = siegesSelectionnes;
            infosBillet.tarifLibelle = tarifSelectionne.getLibelle();
            infosBillet.prixTotal = prixTotalLabel.getText();
            
            // ÉTAPE 3 : Créer et afficher le dialogue du billet.
            BilletDialog billetDialog = new BilletDialog((Frame) SwingUtilities.getWindowAncestor(this), infosBillet);
            billetDialog.setVisible(true);

            // =====================================================================
            // === FIN DE L'AJOUT                                                ===
            // =====================================================================

            // ÉTAPE 4 : Mettre à jour la vue des sièges pour refléter les nouvelles places prises.
            displaySieges(currentSeance);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la réservation : \n" + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Utile pour le débogage.
        }
    }

    public void displaySieges(Seance seance) {
        // ... (cette méthode reste identique)
        this.currentSeance = seance;
        planSallePanel.removeAll();
        siegeButtonMap.clear();

        if (seance == null) {
            planSallePanel.revalidate();
            planSallePanel.repaint();
            updatePrixTotal();
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
        JToggleButton[][] siegeButtonsGrid = new JToggleButton[maxRangee][maxSiegeNum];

        for (Siege siege : tousLesSieges) {
            final JToggleButton siegeButton = new JToggleButton(String.valueOf(siege.getNumeroSiege()));
            
            siegeButton.setOpaque(true); 
            siegeButton.setForeground(Color.WHITE);
            siegeButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            siegeButtonMap.put(siegeButton, siege);

            if (idsSiegesOccupes.contains(siege.getId())) {
                siegeButton.setEnabled(false);
                siegeButton.setBackground(COULEUR_OCCUPE);
            } else {
                siegeButton.setBackground(COULEUR_DISPONIBLE);
                
                siegeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (siegeButton.isSelected()) {
                            siegeButton.setBackground(COULEUR_SELECTIONNE);
                        } else {
                            siegeButton.setBackground(COULEUR_DISPONIBLE);
                        }
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
        
        planSallePanel.revalidate();
        planSallePanel.repaint();
        updatePrixTotal();
    }
    
    public void clearPanel() {
        // ... (cette méthode reste identique)
        displaySieges(null);
    }

    public void setRetourListener(RetourListener listener) {
        // ... (cette méthode reste identique)
        this.retourListener = listener;
    }
}