package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Siege;
import com.mycompany.cinema.dao.SiegeDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation concrète pour la gestion des sièges dans le fichier "sieges.dat".
 * 
 * Pour le développeur de l'interface graphique : cette classe est la source de données
 * pour le `SiegePanel`. Sa méthode la plus importante, `getSiegesBySalleId`, est
 * celle que vous appellerez (via le service) pour obtenir la liste de tous les sièges
 * à dessiner sur le plan de la salle.
 */
public class SiegeDAOImpl extends GenericDAOImpl<Siege> implements SiegeDAO {

    public SiegeDAOImpl() {
        super("sieges.dat");
    }

    @Override
    public void addSiege(Siege siege) {
        this.data.add(siege);
        saveToFile();
    }

    @Override
    public List<Siege> getSiegesBySalleId(int salleId) {
        List<Siege> resultat = new ArrayList<>();
        // On parcourt tous les sièges du cinéma pour ne retourner que ceux
        // qui appartiennent à la salle demandée.
        for (Siege siege : this.data) {
            if (siege.getIdSalle() == salleId) {
                resultat.add(siege);
            }
        }
        return resultat;
    }

    @Override
    public List<Siege> getAllSieges() {
        return new ArrayList<>(this.data);
    }
}