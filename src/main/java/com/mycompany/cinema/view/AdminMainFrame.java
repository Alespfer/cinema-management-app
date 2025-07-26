package com.mycompany.cinema.view;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.view.admin.GestionFilmsPanel;
import com.mycompany.cinema.view.admin.GestionSeancesPanel; // <-- IMPORTER LE NOUVEAU PANNEAU


import javax.swing.*;
import java.awt.*;

public class AdminMainFrame extends JFrame {

    private final AdminService adminService;
    private final Personnel personnelConnecte;

    // Le constructeur reçoit le service ADMIN et le membre du personnel connecté
    public AdminMainFrame(AdminService adminService, Personnel personnelConnecte) {
        this.adminService = adminService;
        this.personnelConnecte = personnelConnecte;

        setTitle("Panneau d'Administration - " + personnelConnecte.getPrenom() + " " + personnelConnecte.getNom());
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Un JTabbedPane est idéal pour une interface d'administration
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pour chaque fonctionnalité, on crée un panneau qui sera placé dans un onglet.
        // Au début, ce sont de simples panneaux vides.
        // Par la suite, chacun deviendra sa propre classe complexe (ex: GestionFilmPanel.java)

        GestionFilmsPanel gestionFilmsPanel = new GestionFilmsPanel(this.adminService);
        tabbedPane.addTab("Gestion Films", gestionFilmsPanel);

        GestionSeancesPanel gestionSeancesPanel = new GestionSeancesPanel(this.adminService);
        tabbedPane.addTab("Gestion Séances", gestionSeancesPanel);
        
        JPanel gestionSallesPanel = new JPanel();
        gestionSallesPanel.add(new JLabel("Ici, on gérera les salles."));
        tabbedPane.addTab("Gestion Salles", gestionSallesPanel);

        JPanel gestionPersonnelPanel = new JPanel();
        gestionPersonnelPanel.add(new JLabel("Ici, on gérera le personnel et les plannings."));
        tabbedPane.addTab("Gestion Personnel", gestionPersonnelPanel);

        JPanel reportingPanel = new JPanel();
        reportingPanel.add(new JLabel("Ici, on affichera les rapports de ventes."));
        tabbedPane.addTab("Rapports de Ventes", reportingPanel);

        // On ajoute le système d'onglets à la fenêtre principale
        add(tabbedPane, BorderLayout.CENTER);
    }
}