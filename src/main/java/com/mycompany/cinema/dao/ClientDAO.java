package com.mycompany.cinema.dao;
import com.mycompany.cinema.Client;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des comptes clients.
 * C'est la base de toutes les interactions avec les données des utilisateurs.
 * 
 * L'interface graphique utilisera ce contrat en permanence :
 * - `addClient` sera appelé par la fenêtre d'inscription (`RegisterDialog`).
 * - `getClientById` sera utilisé partout où vous avez besoin d'afficher le nom d'un client.
 * - `getAllClients` sera utilisé (implicitement via le service) par la fenêtre de connexion (`LoginFrame`) pour vérifier les identifiants.
 * - `updateClient` et `deleteClient` seront appelés depuis le panneau "Mes Informations" (`InfosPersonnellesPanel`).
 */
public interface ClientDAO {

    void addClient(Client client);
    Optional<Client> getClientById(int id);
    List<Client> getAllClients();
    void updateClient(Client client);
    void deleteClient(int id);
    
    void rechargerDonnees();
}