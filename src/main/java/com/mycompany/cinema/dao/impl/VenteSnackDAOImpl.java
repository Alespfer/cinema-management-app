package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.VenteSnack;
import com.mycompany.cinema.dao.VenteSnackDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return this.data.stream().filter(v -> v.getIdVente() == id).findFirst();
    }

    @Override
    public List<VenteSnack> getAllVentesSnack() {
        return new ArrayList<>(this.data);
    }

    @Override
    public List<VenteSnack> getVentesByDate(LocalDate date) {
        return this.data.stream()
                .filter(v -> v.getDateVente().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }
}