package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Comporte;
import com.mycompany.cinema.dao.ComporteDAO;
import java.util.ArrayList;
import java.util.List;

public class ComporteDAOImpl extends GenericDAOImpl<Comporte> implements ComporteDAO {

    public ComporteDAOImpl() { super("lignes_ventes.dat"); }

    @Override
    public void addLigneVente(Comporte comporte) {
        this.data.add(comporte);
        saveToFile();
    }

    @Override
    public List<Comporte> getLignesByVenteId(int venteId) {
        List<Comporte> resultat = new ArrayList<>();
        for (Comporte ligne : this.data) {
            if (ligne.getIdVente() == venteId) {
                resultat.add(ligne);
            }
        }
        return resultat;
    }
}