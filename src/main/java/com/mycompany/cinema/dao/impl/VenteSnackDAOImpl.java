package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.VenteSnack;
import com.mycompany.cinema.dao.VenteSnackDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VenteSnackDAOImpl extends GenericDAOImpl<VenteSnack> implements VenteSnackDAO {

    public VenteSnackDAOImpl() {
        super("ventes_snack.dat");
    }

    @Override
    public void addVenteSnack(VenteSnack vente) {
        this.data.add(vente);
        saveToFile();
    }

    @Override
    public Optional<VenteSnack> getVenteSnackById(int id) {
        for (VenteSnack vente : this.data) {
            if (vente.getIdVente() == id) {
                return Optional.of(vente);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<VenteSnack> getAllVentesSnack() {
        return new ArrayList<>(this.data);
    }

    @Override
    public List<VenteSnack> getVentesByDate(LocalDate date) {
        List<VenteSnack> resultat = new ArrayList<>();
        for (VenteSnack vente : this.data) {
            if (vente.getDateVente().toLocalDate().isEqual(date)) {
                resultat.add(vente);
            }
        }
        return resultat;
    }
}