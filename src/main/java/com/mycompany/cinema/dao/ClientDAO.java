package com.mycompany.cinema.dao;
import com.mycompany.cinema.Client;
import java.util.List;
import java.util.Optional;

public interface ClientDAO {
    void addClient(Client client);
    Optional<Client> getClientById(int id);
    List<Client> getAllClients();
}