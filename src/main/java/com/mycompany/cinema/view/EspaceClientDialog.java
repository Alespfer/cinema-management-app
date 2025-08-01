package com.mycompany.cinema.view;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.service.ClientService;
import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre de dialogue modale pour la gestion de l'espace client.
 * Utilise un JTabbedPane pour séparer la gestion des informations personnelles
 * de l'historique des réservations.
 */
public class EspaceClientDialog extends JDialog {

    public EspaceClientDialog(JFrame owner, ClientService clientService, Client clientConnecte) {
        super(owner, "Mon Espace Client", true);
        
        setSize(800, 600);
        setLocationRelativeTo(owner);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // On crée les deux panneaux qui serviront d'onglets.
        // On garde une référence au panneau de l'historique pour pouvoir le rafraîchir.
        JPanel infosPanel = new InfosPersonnellesPanel(clientService, clientConnecte, this);
        HistoriqueReservationsPanel historiquePanel = new HistoriqueReservationsPanel(clientService, clientConnecte);
        
        tabbedPane.addTab("Mes Informations", infosPanel);
        tabbedPane.addTab("Historique des Réservations", historiquePanel);
        
        // =====================================================================
        // === DÉBUT DE LA CORRECTION : Ajout de l'écouteur de rafraîchissement ===
        // =====================================================================
        
        tabbedPane.addChangeListener(e -> {
            // On vérifie si l'onglet qui vient d'être sélectionné est bien celui de l'historique.
            if (tabbedPane.getSelectedComponent() == historiquePanel) {
                // Si c'est le cas, on appelle sa méthode publique 'loadHistorique()'
                // pour forcer le rechargement des données depuis les fichiers.
                historiquePanel.loadHistorique();
            }
        });

        // =====================================================================
        // === FIN DE LA CORRECTION                                          ===
        // =====================================================================
        
        add(tabbedPane);
    }
}