package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Siege;
import com.mycompany.cinema.dao.SiegeDAO;
import java.util.ArrayList;
import java.util.List;

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