package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Tarif;
import com.mycompany.cinema.service.AdminService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Panneau de gestion (CRUD) pour les Tarifs.
 * Permet de créer, lire, METTRE À JOUR et SUPPRIMER les différents tarifs.
 */
public class GestionTarifsPanel extends JPanel {

    private final AdminService adminService;

    // Composants de l'interface graphique
    private JList<Tarif> listeTarifs;
    private DefaultListModel<Tarif> listModel;
    
    private JTextField idField;
    private JTextField libelleField;
    private JTextField prixField;

    private JButton nouveauBouton;
    private JButton enregistrerBouton;
    private JButton supprimerBouton;

    private Tarif tarifSelectionne;

    public GestionTarifsPanel(AdminService adminService) {
        if (adminService == null) {
            throw new IllegalArgumentException("AdminService ne peut pas être null.");
        }
        this.adminService = adminService;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        initListeners();
        chargerListeTarifs();
    }

    private void initComponents() {
        JPanel panneauGauche = new JPanel(new BorderLayout());
        panneauGauche.setBorder(BorderFactory.createTitledBorder("Tarifs existants"));

        listModel = new DefaultListModel<Tarif>();
        listeTarifs = new JList<Tarif>(listModel);
        listeTarifs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listeTarifs.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tarif) {
                    Tarif tarif = (Tarif) value;
                    setText(tarif.getLibelle() + " - " + String.format("%.2f", tarif.getPrix()) + " €");
                }
                return this;
            }
        });
        panneauGauche.add(new JScrollPane(listeTarifs), BorderLayout.CENTER);

        JPanel panneauDroite = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Détails du tarif"));

        idField = new JTextField();
        idField.setEditable(false);
        libelleField = new JTextField();
        prixField = new JTextField();

        formPanel.add(new JLabel("ID :"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Libellé :"));
        formPanel.add(libelleField);
        formPanel.add(new JLabel("Prix (€) :"));
        formPanel.add(prixField);

        panneauDroite.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nouveauBouton = new JButton("Nouveau");
        enregistrerBouton = new JButton("Enregistrer");
        supprimerBouton = new JButton("Supprimer");
        supprimerBouton.setEnabled(false);
        buttonPanel.add(nouveauBouton);
        buttonPanel.add(enregistrerBouton);
        buttonPanel.add(supprimerBouton);

        panneauDroite.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panneauGauche, BorderLayout.WEST);
        add(panneauDroite, BorderLayout.CENTER);
    }

    private void initListeners() {
        listeTarifs.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    tarifSelectionne = listeTarifs.getSelectedValue();
                    mettreAJourChamps(tarifSelectionne);
                }
            }
        });

        nouveauBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionNouveau();
            }
        });

        enregistrerBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEnregistrer();
            }
        });
        
        supprimerBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionSupprimer();
            }
        });
    }

    private void chargerListeTarifs() {
        try {
            listModel.clear();
            List<Tarif> tarifs = adminService.getAllTarifs();
            for (Tarif tarif : tarifs) {
                listModel.addElement(tarif);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des tarifs : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mettreAJourChamps(Tarif tarif) {
        if (tarif != null) {
            idField.setText(String.valueOf(tarif.getId()));
            libelleField.setText(tarif.getLibelle());
            prixField.setText(String.format("%.2f", tarif.getPrix()).replace(',', '.'));
            supprimerBouton.setEnabled(true);
        } else {
            idField.setText("");
            libelleField.setText("");
            prixField.setText("");
            supprimerBouton.setEnabled(false);
        }
    }
    
    private void actionNouveau() {
        tarifSelectionne = null;
        listeTarifs.clearSelection();
        mettreAJourChamps(null);
    }

    /**
     * Action déclenchée par le bouton "Enregistrer".
     * Gère à la fois la création (si aucun tarif n'est sélectionné)
     * et la MODIFICATION (si un tarif est sélectionné).
     */
    private void actionEnregistrer() {
        String libelle = libelleField.getText();
        if (libelle == null || libelle.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le libellé ne peut pas être vide.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(prixField.getText().replace(',', '.'));
            if (prix < 0) { throw new NumberFormatException(); }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le prix doit être un nombre positif (ex: 9.20).", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (tarifSelectionne == null) { // --- GESTION DE LA CRÉATION (CREATE) ---
                Tarif nouveauTarif = new Tarif();
                nouveauTarif.setLibelle(libelle);
                nouveauTarif.setPrix(prix);
                adminService.ajouterTarif(nouveauTarif);
                JOptionPane.showMessageDialog(this, "Tarif créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else { 
                // --- GESTION DE LA MODIFICATION (UPDATE) ---
                // Si un tarif est déjà sélectionné, nous sommes en mode "Mise à Jour".
                // On met à jour l'objet existant avec les nouvelles valeurs des champs.
                tarifSelectionne.setLibelle(libelle);
                tarifSelectionne.setPrix(prix);
                // On appelle la méthode du service dédiée à la modification.
                adminService.modifierTarif(tarifSelectionne);
                JOptionPane.showMessageDialog(this, "Tarif modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        chargerListeTarifs();
        actionNouveau();
    }
    
    /**
     * Action pour le bouton "Supprimer".
     * Gère la confirmation et l'appel au service pour la suppression.
     */
    private void actionSupprimer() {
        // --- LOGIQUE DE SUPPRESSION (DELETE) ---
        if (tarifSelectionne == null) {
            // Sécurité : ne rien faire si aucun tarif n'est sélectionné.
            return;
        }
        
        // Étape 1 : Demander une confirmation explicite à l'utilisateur.
        int confirmation = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir supprimer le tarif '" + tarifSelectionne.getLibelle() + "' ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
        // Étape 2 : Si l'utilisateur confirme...
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                // ...appeler la méthode du service dédiée à la suppression en passant l'ID.
                adminService.supprimerTarif(tarifSelectionne.getId());
                JOptionPane.showMessageDialog(this, "Tarif supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                
                // Réinitialiser la vue après la suppression.
                actionNouveau(); 
                chargerListeTarifs(); 
            } catch (Exception ex) {
                 JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}