// ========================================================================
// FICHIER : ClientDAO.java
// ========================================================================
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Client;
import java.util.List;

/**
 * Définit le contrat pour la gestion des comptes clients.
 * C'est l'interface de base pour toutes les interactions avec les données des utilisateurs.
 */
public interface ClientDAO {

    // Enregistre un nouveau client.
    void ajouterClient(Client client);

    // Recherche un client par son identifiant unique.
    Client trouverClientParId(int id);

    // Recherche un client par son adresse email.
    Client trouverClientParEmail(String email);

    // Retrouve la liste de tous les clients.
    List<Client> trouverTousLesClients();

    // Met à jour les informations d'un client.
    void mettreAJourClient(Client client);

    // Supprime un client à partir de son identifiant.
    void supprimerClientParId(int id);
    
    // Force le rechargement des données depuis la source.
    void rechargerDonnees();
}