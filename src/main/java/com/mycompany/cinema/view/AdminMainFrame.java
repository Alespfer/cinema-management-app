package com.mycompany.cinema.view;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.service.AdminService;
import com.mycompany.cinema.view.admin.*; // Importation groupée pour la lisibilité

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
        
         // --- MISE EN PLACE DU PROTOCOLE DE RAFRAÎCHISSEMENT ---
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // On récupère le composant de l'onglet qui vient d'être sélectionné
                Component selectedComponent = tabbedPane.getSelectedComponent();
                
                // Si l'onglet sélectionné est notre panneau de gestion des séances...
                if (selectedComponent instanceof GestionSeancesPanel) {
                    // ...on lui ordonne de rafraîchir ses données.
                    ((GestionSeancesPanel) selectedComponent).rafraichirDonnees();
                }
                
                // On peut ajouter d'autres 'instanceof' ici si d'autres panneaux
                // ont besoin d'être rafraîchis de la même manière.
            }
        });

        // =====================================================================
        // === FIN DE LA MODIFICATION                                        ===
        // =====================================================================

        add(tabbedPane, BorderLayout.CENTER);
    }
}