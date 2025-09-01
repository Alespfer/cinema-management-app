/*
 * Fichier : Siege.java (Panneau de sélection des sièges)
 * Version finale adaptée pour NetBeans.
 */
package com.mycompany.cinema.view;

// Imports nécessaires
import com.mycompany.cinema.Billet;
import com.mycompany.cinema.Client;
import com.mycompany.cinema.Seance;
import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.service.ClientService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * Ce panneau est responsable de l'affichage du plan d'une salle de cinéma.
 *
 * @author albertoesperon
 */
public class AffichageSieges extends javax.swing.JPanel {

    // --- PARTIE MANUELLE ---
    private final ClientService clientService;
    private Seance seanceActuelle;
    private List<JToggleButton> listeBoutonsSiege = new ArrayList<>();
    private List<com.mycompany.cinema.Siege> listeObjetsSiege = new ArrayList<>();

    private static final Color COULEUR_DISPONIBLE = new Color(34, 177, 76);
    private static final Color COULEUR_OCCUPE = new Color(237, 28, 36);
    private static final Color COULEUR_SELECTIONNE = new Color(63, 72, 204);

    public interface EcouteurRetour {

        void auClicSurRetour();
    }
    private EcouteurRetour ecouteurRetour;

    public interface EcouteurReservation {

        void detailsReservationSelectionnes(List<com.mycompany.cinema.Siege> sieges, Tarif tarif);
    }
    private EcouteurReservation ecouteurReservation;

    public AffichageSieges(ClientService clientService, Client clientConnecte) {
        this.clientService = clientService;
        initComponents();
        setupCustomComponents();
        chargerTarifs();
    }

    private void setupCustomComponents() {
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
    }

    private void chargerTarifs() {
        try {
            List<Tarif> tarifs = clientService.getAllTarifs();
            tarifComboBox.removeAllItems();
            for (Tarif tarif : tarifs) {
                tarifComboBox.addItem(tarif);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des tarifs.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mettreAJourPrixTotal() {
        Tarif tarifSelectionne = (Tarif) tarifComboBox.getSelectedItem();
        if (tarifSelectionne == null) {
            return;
        }
        int nombreDeSieges = 0;
        for (JToggleButton bouton : listeBoutonsSiege) {
            if (bouton.isSelected()) {
                nombreDeSieges++;
            }
        }
        double prixTotal = tarifSelectionne.getPrix() * nombreDeSieges;
        prixTotalLabel.setText("Prix billets : " + String.format("%.2f", prixTotal) + " €");
    }

    private void continuerVersSnacks() {
        List<com.mycompany.cinema.Siege> siegesSelectionnes = new ArrayList<>();
        for (int i = 0; i < listeBoutonsSiege.size(); i++) {
            if (listeBoutonsSiege.get(i).isSelected()) {
                siegesSelectionnes.add(listeObjetsSiege.get(i));
            }
        }
        Tarif tarifSelectionne = (Tarif) tarifComboBox.getSelectedItem();
        if (siegesSelectionnes.isEmpty() || tarifSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un siège et un tarif.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ecouteurReservation != null) {
            ecouteurReservation.detailsReservationSelectionnes(siegesSelectionnes, tarifSelectionne);
        }
    }

    public void afficherSieges(Seance seance) {
        this.seanceActuelle = seance;
        JPanel panelContenu = new JPanel();
        listeBoutonsSiege.clear();
        listeObjetsSiege.clear();

        if (seance != null) {
            List<com.mycompany.cinema.Siege> tousLesSieges = clientService.getSiegesPourSalle(seance.getIdSalle());
            List<Billet> billetsVendus = clientService.getBilletsPourSeance(seance.getId());
            List<Integer> idsSiegesOccupes = new ArrayList<>();
            for (Billet billet : billetsVendus) {
                idsSiegesOccupes.add(billet.getIdSiege());
            }

            int maxRangee = 0, maxSiegeNum = 0;
            for (com.mycompany.cinema.Siege siege : tousLesSieges) {
                if (siege.getNumeroRangee() > maxRangee) {
                    maxRangee = siege.getNumeroRangee();
                }
                if (siege.getNumeroSiege() > maxSiegeNum) {
                    maxSiegeNum = siege.getNumeroSiege();
                }
            }

            panelContenu.setLayout(new GridLayout(maxRangee > 0 ? maxRangee : 1, maxSiegeNum > 0 ? maxSiegeNum : 1, 5, 5));
            com.mycompany.cinema.Siege[][] grilleSieges = new com.mycompany.cinema.Siege[maxRangee][maxSiegeNum];
            for (com.mycompany.cinema.Siege siege : tousLesSieges) {
                grilleSieges[siege.getNumeroRangee() - 1][siege.getNumeroSiege() - 1] = siege;
            }

            for (int i = 0; i < maxRangee; i++) {
                for (int j = 0; j < maxSiegeNum; j++) {
                    final com.mycompany.cinema.Siege siege = grilleSieges[i][j];
                    if (siege == null) {
                        panelContenu.add(new JPanel());
                        continue;
                    }
                    final JToggleButton boutonSiege = new JToggleButton(String.valueOf(siege.getNumeroSiege()));
                    boutonSiege.setOpaque(true);
                    boutonSiege.setForeground(Color.WHITE);
                    boutonSiege.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                    listeBoutonsSiege.add(boutonSiege);
                    listeObjetsSiege.add(siege);
                    if (idsSiegesOccupes.contains(siege.getId())) {
                        boutonSiege.setEnabled(false);
                        boutonSiege.setBackground(COULEUR_OCCUPE);
                    } else {
                        boutonSiege.setEnabled(true);
                        boutonSiege.setBackground(COULEUR_DISPONIBLE);
                        boutonSiege.addActionListener(e -> {
                            boutonSiege.setBackground(boutonSiege.isSelected() ? COULEUR_SELECTIONNE : COULEUR_DISPONIBLE);
                            mettreAJourPrixTotal();
                        });
                    }
                    panelContenu.add(boutonSiege);
                }
            }
        }

        planSallePanel.setViewportView(panelContenu);
        planSallePanel.revalidate();
        planSallePanel.repaint();
        mettreAJourPrixTotal();
    }

    public void setEcouteurRetour(EcouteurRetour listener) {
        this.ecouteurRetour = listener;
    }

    public void setEcouteurReservation(EcouteurReservation listener) {
        this.ecouteurReservation = listener;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        bottomPanel = new javax.swing.JPanel();
        retourButton = new javax.swing.JButton();
        eastPanel = new javax.swing.JPanel();
        prixTotalLabel = new javax.swing.JLabel();
        reserveButton = new javax.swing.JButton();
        tarifPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tarifComboBox = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        ecranLabel = new javax.swing.JLabel();
        planSallePanel = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Étape 2/3 : Choisissez vos places et votre tarif"));
        setLayout(new java.awt.BorderLayout(10, 10));

        bottomPanel.setLayout(new java.awt.BorderLayout(10, 5));

        retourButton.setText("<< Retour aux détails du film");
        retourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retourButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(retourButton, java.awt.BorderLayout.LINE_START);

        eastPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        prixTotalLabel.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        prixTotalLabel.setText("Prix total : 0.00 €");
        eastPanel.add(prixTotalLabel);

        reserveButton.setText("Continuer vers les Snacks >>");
        reserveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserveButtonActionPerformed(evt);
            }
        });
        eastPanel.add(reserveButton);

        bottomPanel.add(eastPanel, java.awt.BorderLayout.LINE_END);

        tarifPanel.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Tarif :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        tarifPanel.add(jLabel1, gridBagConstraints);

        tarifComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tarifComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 10, 0);
        tarifPanel.add(tarifComboBox, gridBagConstraints);

        bottomPanel.add(tarifPanel, java.awt.BorderLayout.CENTER);

        add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        jPanel1.setLayout(new java.awt.BorderLayout());

        ecranLabel.setBackground(new java.awt.Color(153, 153, 153));
        ecranLabel.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        ecranLabel.setForeground(new java.awt.Color(255, 255, 255));
        ecranLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ecranLabel.setText("ÉCRAN");
        ecranLabel.setOpaque(true);
        jPanel1.add(ecranLabel, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 670, Short.MAX_VALUE)
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 217, Short.MAX_VALUE)
        );

        planSallePanel.setViewportView(contentPanel);

        jPanel1.add(planSallePanel, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void reserveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserveButtonActionPerformed
        continuerVersSnacks();
    }//GEN-LAST:event_reserveButtonActionPerformed

    private void retourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retourButtonActionPerformed
        ecouteurRetour.auClicSurRetour();
    }//GEN-LAST:event_retourButtonActionPerformed

    private void tarifComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tarifComboBoxActionPerformed
        mettreAJourPrixTotal();
    }//GEN-LAST:event_tarifComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel eastPanel;
    private javax.swing.JLabel ecranLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane planSallePanel;
    private javax.swing.JLabel prixTotalLabel;
    private javax.swing.JButton reserveButton;
    private javax.swing.JButton retourButton;
    private javax.swing.JComboBox<com.mycompany.cinema.Tarif> tarifComboBox;
    private javax.swing.JPanel tarifPanel;
    // End of variables declaration//GEN-END:variables
}
