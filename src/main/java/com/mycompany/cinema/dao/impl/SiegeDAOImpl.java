package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Siege;
import com.mycompany.cinema.dao.SiegeDAO;
import java.util.ArrayList;
import java.util.List;

// DAO pour la gestion des sièges dans les salles.
public class SiegeDAOImpl extends GenericDAOImpl<Siege> implements SiegeDAO {

    // Initialise le DAO avec le fichier des sièges.
    public SiegeDAOImpl() {
        super("sieges.dat");
    }

    // Ajoute un nouveau siège dans la base de données.
    @Override
    public void addSiege(Siege siege) {
        this.data.add(siege);
        saveToFile();
    }

    // Retourne tous les sièges appartenant à une salle donnée.
    @Override
    public List<Siege> getSiegesBySalleId(int salleId) {
        List<Siege> resultat = new ArrayList<>();
        for (Siege siege : this.data) {
            if (siege.getIdSalle() == salleId) {
                resultat.add(siege);
            }
        }
        return resultat;
    }

    // Retourne l’ensemble des sièges disponibles.
    @Override
    public List<Siege> getAllSieges() {
        return new ArrayList<>(this.data);
    }
}
