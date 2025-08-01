package com.mycompany.cinema.view;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.view.admin.*; // Importation de tous les panneaux du package admin.

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Voici la fenêtre principale du panneau d'administration.
 * C'est un conteneur 'JFrame' qui utilise un système d'onglets 'JTabbedPane'
 * pour afficher les différents écrans de gestion (films, séances, etc.).
 * Elle ne contient aucune logique, elle ne fait qu'assembler les panneaux que tu as créés.
 */
public class AdminMainFrame extends JFrame {

    // Le service qui contient toute la logique métier de l'administration.
    // C'est le seul lien entre cette fenêtre (la Vue) et le reste de l'application.
    private final AdminService adminService;
    // L'objet de l'employé qui est actuellement connecté.
    // On l'utilise pour afficher son nom dans le titre de la fenêtre.
    private final Personnel personnelConnecte;

    /**
     * Constructeur de la fenêtre principale de l'administration.
     * @param adminService L'instance du service admin, fournie au démarrage.
     * @param personnelConnecte L'objet Personnel de l'utilisateur connecté.
     */
    public AdminMainFrame(AdminService adminService, Personnel personnelConnecte) {
        this.adminService = adminService;
        this.personnelConnecte = personnelConnecte;

        // Configuration de base de la fenêtre (titre, taille, etc.).
        setTitle("Panneau d'Administration - " + personnelConnecte.getPrenom() + " " + personnelConnecte.getNom());
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Le programme se ferme quand on clique sur la croix.
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran.
        
        // Méthode qui construit et ajoute tous les composants graphiques.
        initComponents();
    }

    /**
     * Initialise et assemble tous les composants de l'interface.
     */
    private void initComponents() {
        // Le conteneur principal qui va gérer les onglets.
        JTabbedPane tabbedPane = new JTabbedPane();

        // --- Création et ajout de chaque onglet ---
        // Chaque onglet est un 'JPanel' spécialisé que tu as créé.
        // On passe l'instance du 'adminService' à chaque panneau pour qu'il puisse
        // communiquer avec la logique métier.
        tabbedPane.addTab("Gestion Films", new GestionFilmsPanel(this.adminService));
        tabbedPane.addTab("Gestion Séances", new GestionSeancesPanel(this.adminService));
        tabbedPane.addTab("Gestion Salles", new GestionSallesPanel(this.adminService));
        tabbedPane.addTab("Gestion Personnel", new GestionPersonnelPanel(this.adminService));
        tabbedPane.addTab("Gestion Tarifs", new GestionTarifsPanel(this.adminService));
        tabbedPane.addTab("Gestion Snacking", new GestionProduitsSnackPanel(adminService));
        tabbedPane.addTab("Rapports de Ventes", new ReportingPanel(this.adminService));
        
        // --- Protocole de Rafraîchissement Automatique ---
        // Mon binôme : Ce bloc est important. Il met en place un "écouteur" qui
        // détecte chaque fois que l'administrateur change d'onglet.
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // On récupère le panneau de l'onglet qui vient d'être affiché.
                Component selectedComponent = tabbedPane.getSelectedComponent();
                
                // Si l'onglet sélectionné est celui de la gestion des séances...
                if (selectedComponent instanceof GestionSeancesPanel) {
                    // ...on appelle sa méthode 'rafraichirDonnees()'.
                    // CELA GARANTIT QUE LA LISTE DES SÉANCES EST TOUJOURS À JOUR
                    // chaque fois que l'admin clique sur cet onglet. C'est plus fiable
                    // qu'un bouton de rafraîchissement manuel.
                    ((GestionSeancesPanel) selectedComponent).rafraichirDonnees();
                }
                
                // On pourrait ajouter d'autres vérifications ici si d'autres panneaux
                // nécessitent ce même comportement.
            }
        });

        // On ajoute le système d'onglets au centre de la fenêtre principale.
        add(tabbedPane, BorderLayout.CENTER);
    }
}