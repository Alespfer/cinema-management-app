package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.dao.ClientDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAOImpl extends GenericDAOImpl<Client> implements ClientDAO {

    public ClientDAOImpl() {
        super("clients.dat");
    }

    @Override
    public void addClient(Client client) {
        this.data.add(client);
        saveToFile();
    }

    /**
     * Recherche un client par son identifiant.
     * C'est une opération de base : parcourir la liste jusqu'à trouver une correspondance.
     */
    @Override
    public Optional<Client> getClientById(int id) {
        for (Client client : this.data) {
            if (client.getId() == id) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(this.data);
    }
    
    /**
     * Met à jour un client existant dans la liste.
     * On cherche le client par son ID, puis on le remplace par la nouvelle version.
     */
    @Override
    public void updateClient(Client updatedClient) {
        // On a besoin de l'index pour utiliser la méthode .set(), donc une boucle 'for' classique est nécessaire.
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedClient.getId()) {
                this.data.set(i, updatedClient);
                saveToFile();
                return; // Une fois trouvé et modifié, on arrête la recherche.
            }
        }
    }

    /**
     * Supprime un client de la liste en se basant sur son ID.
     */
    @Override
    public void deleteClient(int id) {
        // La méthode .removeIf(...) est plus concise mais n'est pas dans le cours.
        // On utilise donc .remove() sur l'objet trouvé.
        boolean changed = this.data.removeIf(client -> client.getId() == id);
        if(changed) {
            saveToFile();
        }
    }
}