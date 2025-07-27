package com.mycompany.cinema.view.admin;

import com.mycompany.cinema.Salle;
import com.mycompany.cinema.service.AdminService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panneau de gestion (CRUD) pour les Salles.
 * Ce panneau permet de créer, lire, mettre à jour et supprimer des salles de cinéma.
 * Il suit l'architecture et les contraintes de style imposées par le projet.
 */
public class GestionSallesPanel extends JPanel {

    private final AdminService adminService;

    // Composants de l'interface graphique
    private JList<Salle> listeSalles;
    private DefaultListModel<Salle> listModel;
    
    private JTextField idField;
    private JTextField numeroField;
    private JTextField capaciteField;

    private JButton nouveauBouton;
    private JButton enregistrerBouton;
    private JButton supprimerBouton;

    // Variable pour garder en mémoire la salle actuellement sélectionnée dans la liste
    private Salle salleSelectionnee;

    /**
     * Constructeur du panneau de gestion des salles.
     * @param adminService L'instance du service contenant la logique métier.
     */
    public GestionSallesPanel(AdminService adminService) {
        // Le service est indispensable au fonctionnement du panneau
        if (adminService == null) {
            throw new IllegalArgumentException("AdminService ne peut pas être null.");
        }
        this.adminService = adminService;
        this.salleSelectionnee = null;

        // Configuration du layout principal du panneau
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialisation des composants graphiques et de leurs écouteurs d'événements
        initComponents();
        initListeners();

        // Chargement des données initiales depuis la source de données via le service
        chargerListeSalles();
    }

    /**
     * Construit et organise les composants Swing du panneau.
     */
    private void initComponents() {
        // --- Panneau de gauche : Liste des salles ---
        JPanel panneauGauche = new JPanel(new BorderLayout());
        panneauGauche.setBorder(BorderFactory.createTitledBorder("Salles existantes"));

        listModel = new DefaultListModel<Salle>();
        listeSalles = new JList<Salle>(listModel);
        listeSalles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // On utilise un "renderer" pour personnaliser l'affichage des objets Salle dans la JList.
        // Au lieu d'afficher la sortie de toString(), on affiche le numéro et la capacité.
        listeSalles.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Salle) {
                    Salle salle = (Salle) value;
                    setText(salle.getNumero() + " (" + salle.getCapacite() + " places)");
                }
                return this;
            }
        });
        panneauGauche.add(new JScrollPane(listeSalles), BorderLayout.CENTER);

        // --- Panneau de droite : Formulaire de détails et boutons d'action ---
        JPanel panneauDroite = new JPanel(new BorderLayout());
        
        // Sous-panneau pour le formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Détails de la salle"));

        idField = new JTextField();
        idField.setEditable(false); // L'ID est géré par le système et non modifiable
        numeroField = new JTextField();
        capaciteField = new JTextField();

        formPanel.add(new JLabel("ID :"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Numéro/Nom :"));
        formPanel.add(numeroField);
        formPanel.add(new JLabel("Capacité :"));
        formPanel.add(capaciteField);

        panneauDroite.add(formPanel, BorderLayout.NORTH);

        // Sous-panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nouveauBouton = new JButton("Nouveau");
        enregistrerBouton = new JButton("Enregistrer");
        supprimerBouton = new JButton("Supprimer");
        supprimerBouton.setEnabled(false); // Désactivé tant qu'aucune salle n'est sélectionnée
        buttonPanel.add(nouveauBouton);
        buttonPanel.add(enregistrerBouton);
        buttonPanel.add(supprimerBouton);

        panneauDroite.add(buttonPanel, BorderLayout.SOUTH);

        // Ajout des deux panneaux principaux (gauche et droite) au panneau global
        add(panneauGauche, BorderLayout.CENTER);
        add(panneauDroite, BorderLayout.EAST);
    }

    /**
     * Attribue les écouteurs d'événements (listeners) aux composants interactifs.
     * Conforme à la directive de ne pas utiliser d'expressions lambda.
     */
    private void initListeners() {
        // Écouteur pour la sélection d'un élément dans la liste
        listeSalles.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    salleSelectionnee = listeSalles.getSelectedValue();
                    mettreAJourChamps(salleSelectionnee);
                }
            }
        });

        // Écouteur pour le bouton "Nouveau"
        nouveauBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionNouveau();
            }
        });

        // Écouteur pour le bouton "Enregistrer"
        enregistrerBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEnregistrer();
            }
        });
        
        // Écouteur pour le bouton "Supprimer"
        supprimerBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionSupprimer();
            }
        });
    }

    /**
     * Appelle le service pour récupérer toutes les salles et met à jour la JList.
     * Gère les erreurs de communication avec le service.
     */
    private void chargerListeSalles() {
        try {
            listModel.clear();
            List<Salle> salles = adminService.getAllSalles();
            // Boucle for-each comme exigé par les contraintes
            for (Salle salle : salles) {
                listModel.addElement(salle);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des salles : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Met à jour les champs du formulaire avec les détails de la salle fournie.
     * @param salle La salle à afficher, ou null pour vider le formulaire.
     */
    private void mettreAJourChamps(Salle salle) {
        if (salle != null) {
            idField.setText(String.valueOf(salle.getId()));
            numeroField.setText(salle.getNumero());
            capaciteField.setText(String.valueOf(salle.getCapacite()));
            supprimerBouton.setEnabled(true);
        } else {
            idField.setText("");
            numeroField.setText("");
            capaciteField.setText("");
            supprimerBouton.setEnabled(false);
        }
    }
    
    /**
     * Action déclenchée par le bouton "Nouveau". Réinitialise le formulaire.
     */
    private void actionNouveau() {
        salleSelectionnee = null;
        listeSalles.clearSelection();
        mettreAJourChamps(null);
    }

    /**
     * Action déclenchée par le bouton "Enregistrer".
     * Gère à la fois la création et la modification d'une salle.
     */
    private void actionEnregistrer() {
        // Validation simple des entrées utilisateur
        String numero = numeroField.getText();
        if (numero == null || numero.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro de la salle ne peut pas être vide.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int capacite;
        try {
            capacite = Integer.parseInt(capaciteField.getText());
            if (capacite <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La capacité doit être un nombre entier positif.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (salleSelectionnee == null) { // Mode Création
                Salle nouvelleSalle = new Salle();
                nouvelleSalle.setNumero(numero);
                nouvelleSalle.setCapacite(capacite);
                adminService.ajouterSalle(nouvelleSalle);
                JOptionPane.showMessageDialog(this, "Salle créée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else { // Mode Modification
                salleSelectionnee.setNumero(numero);
                salleSelectionnee.setCapacite(capacite);
                adminService.modifierSalle(salleSelectionnee);
                JOptionPane.showMessageDialog(this, "Salle modifiée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Recharger la liste pour afficher les changements
        chargerListeSalles();
        actionNouveau(); // Réinitialiser pour la prochaine action
    }
    
    /**
     * Action déclenchée par le bouton "Supprimer".
     * Demande confirmation avant d'appeler le service.
     */
    private void actionSupprimer() {
        if (salleSelectionnee == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle à supprimer.", "Action impossible", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmation = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir supprimer la salle '" + salleSelectionnee.getNumero() + "' ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                adminService.supprimerSalle(salleSelectionnee.getId());
                JOptionPane.showMessageDialog(this, "Salle supprimée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                actionNouveau(); 
                chargerListeSalles(); 
            } catch (Exception ex) {
                 JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}