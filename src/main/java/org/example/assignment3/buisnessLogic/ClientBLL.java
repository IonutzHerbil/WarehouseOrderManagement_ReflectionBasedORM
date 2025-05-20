package org.example.assignment3.buisnessLogic;

import org.example.assignment3.dataAccess.ClientDAO;
import org.example.assignment3.model.Client;

import java.util.List;

public class ClientBLL {
    private ClientDAO clientDAO;

    public ClientBLL() {
        clientDAO = new ClientDAO();
    }

    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    public int insertClient(Client client) {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Client name cannot be empty");
        }
        return clientDAO.insert(client);
    }

    public boolean updateClient(Client client) {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Client name cannot be empty");
        }
        return clientDAO.update(client);
    }

    public boolean deleteClient(int id) {
        return clientDAO.delete(id);
    }
}