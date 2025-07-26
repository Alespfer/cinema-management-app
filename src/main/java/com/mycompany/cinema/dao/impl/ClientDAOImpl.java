// Fichier : src/main/java/com/mycompany/cinema/dao/impl/ClientDAOImpl.java
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

    @Override
    public Optional<Client> getClientById(int id) {
        return this.data.stream().filter(client -> client.getId() == id).findFirst();
    }

    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(this.data);
    }
    
    
    @Override
    public void updateClient(Client updatedClient) {
        // On utilise une boucle avec index pour pouvoir faire le remplacement
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedClient.getId()) {
                this.data.set(i, updatedClient);
                saveToFile();
                return; // On a trouvé et modifié le client, on peut quitter la méthode
            }
        }
    }

    // NOUVELLE MÉTHODE (syntaxe du cours)
    @Override
    public void deleteClient(int id) {
        Client clientASupprimer = null;
        for (Client client : this.data) {
            if (client.getId() == id) {
                clientASupprimer = client;
                break; // On a trouvé le client, on sort de la boucle
            }
        }
        
        if (clientASupprimer != null) {
            this.data.remove(clientASupprimer);
            saveToFile();
        }
    }
}