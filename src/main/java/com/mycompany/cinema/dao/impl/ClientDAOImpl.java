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
}