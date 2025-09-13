// ========================================================================
// ClientDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Client;
import com.mycompany.cinema.dao.ClientDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion de la persistance des comptes clients.
 * S'occupe de la lecture et de l'écriture dans le fichier "clients.dat"
 */
public class ClientDAOImpl extends GenericDAOImpl<Client> implements ClientDAO {

    public ClientDAOImpl() {
        super("clients.dat");
    }

    /**
     * Enregistre un nouveau client dans la source de données.
     *
     * @param client L'objet Client à ajouter.
     */
    @Override
    public void ajouterClient(Client client) {
        this.data.add(client);
        sauvegarderDansFichier();
    }

    /**
     * Recherche un client par son identifiant unique.
     *
     * @param id L'identifiant du client à trouver.
     * @return L'objet Client correspondant, ou `null` si non trouvé.
     */
    @Override
    public Client trouverClientParId(int id) {
        for (Client client : this.data) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

    /**
     * Recherche un client par son adresse email. La recherche n'est pas
     * sensible à la casse.
     *
     * @param email L'email du client à rechercher.
     * @return L'objet Client correspondant, ou `null` si aucune correspondance
     * n'est trouvée.
     */
    @Override
    public Client trouverClientParEmail(String email) {
        for (Client client : this.data) {
            if (client.getEmail().equalsIgnoreCase(email)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Retourne la liste de tous les clients enregistrés.
     *
     * @return Une copie de la liste pour protéger les données.
     */
    @Override
    public List<Client> trouverTousLesClients() {
        return new ArrayList<>(this.data);
    }

    /**
     * Met à jour les informations d'un client.
     *
     * @param clientMisAJour L'objet Client avec les données mises à jour.
     */
    @Override
    public void mettreAJourClient(Client clientMisAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == clientMisAJour.getId()) {
                this.data.set(i, clientMisAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }

    /**
     * Supprime un client de la source de données à partir de son identifiant.
     *
     * @param id L'identifiant du client à supprimer.
     */
    @Override
    public void supprimerClientParId(int id) {
        int indexASupprimer = -1;

        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                indexASupprimer = i;
                break;
            }
        }

        if (indexASupprimer != -1) {
            this.data.remove(indexASupprimer);
            sauvegarderDansFichier();
        }
    }
}
