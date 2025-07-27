package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.VenteSnack;
import com.mycompany.cinema.dao.VenteSnackDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour gérer les ventes au snack.
public class VenteSnackDAOImpl extends GenericDAOImpl<VenteSnack> implements VenteSnackDAO {

    // Initialise le DAO avec le fichier des ventes snack.
    public VenteSnackDAOImpl() {
        super("ventes_snack.dat");
    }

    // Ajoute une nouvelle vente snack.
    @Override
    public void addVenteSnack(VenteSnack vente) {
        this.data.add(vente);
        saveToFile();
    }

    // Recherche une vente par son identifiant.
    @Override
    public Optional<VenteSnack> getVenteSnackById(int id) {
        for (VenteSnack vente : this.data) {
            if (vente.getIdVente() == id) {
                return Optional.of(vente);
            }
        }
        return Optional.empty();
    }

    // Retourne toutes les ventes enregistrées.
    @Override
    public List<VenteSnack> getAllVentesSnack() {
        return new ArrayList<>(this.data);
    }

    // Retourne les ventes qui ont eu lieu à une date précise.
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
