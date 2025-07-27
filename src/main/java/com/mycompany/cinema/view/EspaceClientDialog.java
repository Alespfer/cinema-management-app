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
        // Le constructeur JDialog prend la fenêtre "parente" et un booléen "modal"
        // 'true' signifie que cette fenêtre bloque l'interaction avec la fenêtre parente.
        super(owner, "Mon Espace Client", true);
        
        setSize(800, 600);
        setLocationRelativeTo(owner); // Centre la fenêtre par rapport à son parent.
        
        // Création du conteneur à onglets.
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Création des deux panneaux qui serviront d'onglets.
        JPanel infosPanel = new InfosPersonnellesPanel(clientService, clientConnecte, this);
        JPanel historiquePanel = new HistoriqueReservationsPanel(clientService, clientConnecte);
        
        // Ajout des onglets au conteneur.
        tabbedPane.addTab("Mes Informations", infosPanel);
        tabbedPane.addTab("Historique des Réservations", historiquePanel);
        
        // Ajout du conteneur à onglets à la fenêtre de dialogue.
        add(tabbedPane);
    }
}