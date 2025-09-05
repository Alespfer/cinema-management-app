// Fichier : ClientDAOImpl.java
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.dao.ClientDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour gérer la sauvegarde des comptes clients.
 * S'occupe du fichier "clients.dat".
 * 
 * Pour le développeur de l'interface graphique : c'est votre outil principal pour
 * interagir avec les données des utilisateurs. Chaque action dans `RegisterDialog`,
 * `LoginFrame`, et `InfosPersonnellesPanel` finira par appeler (via le service)
 * une des méthodes de cette classe.
 */
public class ClientDAOImpl extends GenericDAOImpl<Client> implements ClientDAO {

    public ClientDAOImpl() {
        super("clients.dat");
    }

    @Override
    public void addClient(Client client) {
        this.data.add(client);
        saveToFile();
    }

    @Override
    public Client getClientById(int id) {
        for (Client client : this.data) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(this.data);
    }

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