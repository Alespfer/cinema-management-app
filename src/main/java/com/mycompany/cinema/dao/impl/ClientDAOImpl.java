package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.dao.ClientDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour gérer les opérations liées aux clients (ajout, recherche, modification, suppression).
public class ClientDAOImpl extends GenericDAOImpl<Client> implements ClientDAO {

    // Initialise le fichier de persistance lié aux clients.
    public ClientDAOImpl() {
        super("clients.dat");
    }

    // Ajoute un nouveau client à la base de données.
    @Override
    public void addClient(Client client) {
        this.data.add(client);
        saveToFile();
    }

    // Recherche un client en fonction de son identifiant.
    @Override
    public Optional<Client> getClientById(int id) {
        for (Client client : this.data) {
            if (client.getId() == id) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    // Retourne la liste complète des clients enregistrés.
    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(this.data);
    }

    // Met à jour les données d’un client existant.
    @Override
    public void updateClient(Client updatedClient) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedClient.getId()) {
                this.data.set(i, updatedClient);
                saveToFile();
                return;
            }
        }
    }

    // Supprime un client en fonction de son identifiant.
    @Override
    public void deleteClient(int id) {
        boolean changed = false;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                this.data.remove(i);
                changed = true;
                break;
            }
        }
        if (changed) {
            saveToFile();
        }
    }
}
