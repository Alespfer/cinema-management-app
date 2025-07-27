package com.mycompany.cinema.view;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.view.admin.GestionFilmsPanel;
import com.mycompany.cinema.view.admin.GestionPersonnelPanel;
import com.mycompany.cinema.view.admin.GestionSallesPanel;
import com.mycompany.cinema.view.admin.GestionSeancesPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale de l'interface d'administration.
 * Elle utilise un JTabbedPane pour organiser les différents panneaux de gestion.
 * Chaque onglet est responsable d'une partie spécifique de l'administration (films, séances, etc.).
 */
public class AdminMainFrame extends JFrame {

    // Le service qui contient toute la logique métier, injecté via le constructeur.
    private final AdminService adminService;
    
    // Le membre du personnel actuellement connecté, pour affichage.
    private final Personnel personnelConnecte;

    /**
     * Constructeur de la fenêtre principale d'administration.
     * @param adminService L'instance du service d'administration à utiliser.
     * @param personnelConnecte Le membre du personnel qui s'est connecté.
     */
    public AdminMainFrame(AdminService adminService, Personnel personnelConnecte) {
        this.adminService = adminService;
        this.personnelConnecte = personnelConnecte;

        // Configuration de base de la fenêtre (JFrame)
        setTitle("Panneau d'Administration - " + personnelConnecte.getPrenom() + " " + personnelConnecte.getNom());
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran
        
        // Initialisation et ajout des composants graphiques internes
        initComponents();
    }

    /**
     * Construit et agence les composants de l'interface graphique.
     * Le composant principal est un JTabbedPane (un conteneur à onglets).
     */
    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Onglet 1: Gestion des Films (implémentation de référence)
        GestionFilmsPanel gestionFilmsPanel = new GestionFilmsPanel(this.adminService);
        tabbedPane.addTab("Gestion Films", gestionFilmsPanel);

        // Onglet 2: Gestion des Séances (implémentation de référence)
        GestionSeancesPanel gestionSeancesPanel = new GestionSeancesPanel(this.adminService);
        tabbedPane.addTab("Gestion Séances", gestionSeancesPanel);
        
        // Onglet 3: Gestion des Salles (nouvellement implémenté)
        GestionSallesPanel gestionSallesPanel = new GestionSallesPanel(this.adminService);
        tabbedPane.addTab("Gestion Salles", gestionSallesPanel);

        // Onglet 4: Gestion du Personnel (nouvellement implémenté)
        GestionPersonnelPanel gestionPersonnelPanel = new GestionPersonnelPanel(this.adminService);
        tabbedPane.addTab("Gestion Personnel", gestionPersonnelPanel);

        // Onglet 5: Rapports de Ventes (coquille vide pour l'instant)
        JPanel reportingPanel = new JPanel();
        reportingPanel.add(new JLabel("Ici, on affichera les rapports de ventes."));
        tabbedPane.addTab("Rapports de Ventes", reportingPanel);

        // Ajoute le conteneur à onglets au centre de la fenêtre.
        add(tabbedPane, BorderLayout.CENTER);
    }
}