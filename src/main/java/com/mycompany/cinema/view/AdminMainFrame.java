package com.mycompany.cinema.view;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.view.admin.*; // Importation groupée pour la lisibilité

import javax.swing.*;
import java.awt.*;

public class AdminMainFrame extends JFrame {

    private final AdminService adminService;
    private final Personnel personnelConnecte;

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
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Gestion Films", new GestionFilmsPanel(this.adminService));
        tabbedPane.addTab("Gestion Séances", new GestionSeancesPanel(this.adminService));
        tabbedPane.addTab("Gestion Salles", new GestionSallesPanel(this.adminService));
        tabbedPane.addTab("Gestion Personnel", new GestionPersonnelPanel(this.adminService));
        tabbedPane.addTab("Gestion Tarifs", new GestionTarifsPanel(this.adminService));
        
        
        tabbedPane.addTab("Gestion Snacking", new GestionProduitsSnackPanel(adminService));


        // =====================================================================
        // === DÉBUT DE LA MODIFICATION : Remplacement de la coquille vide   ===
        // =====================================================================
        
        // On instancie notre nouveau panneau de rapport complet.
        ReportingPanel reportingPanel = new ReportingPanel(this.adminService);
        tabbedPane.addTab("Rapports de Ventes", reportingPanel);

        // =====================================================================
        // === FIN DE LA MODIFICATION                                        ===
        // =====================================================================

        add(tabbedPane, BorderLayout.CENTER);
    }
}