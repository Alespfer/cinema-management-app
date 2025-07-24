package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Comporte;
import com.mycompany.cinema.dao.ComporteDAO;
import java.util.List;
import java.util.stream.Collectors;

public class ComporteDAOImpl extends GenericDAOImpl<Comporte> implements ComporteDAO {

    public ComporteDAOImpl() {
        super("lignes_ventes.dat");
    }

    @Override
    public void addLigneVente(Comporte comporte) {
        this.data.add(comporte);
        saveToFile();
    }

    @Override
    public List<Comporte> getLignesByVenteId(int venteId) {
        return this.data.stream()
                .filter(ligne -> ligne.getIdVente() == venteId)
                .collect(Collectors.toList());
    }
}