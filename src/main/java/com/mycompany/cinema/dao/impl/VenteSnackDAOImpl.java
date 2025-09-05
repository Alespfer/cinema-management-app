package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.VenteSnack;
import com.mycompany.cinema.dao.VenteSnackDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion des tickets de caisse de snacks dans
 * le fichier "ventes_snack.dat".
 *
 * Pour le développeur de l'interface graphique : cette classe est
 * principalement utilisée par le `ReportingPanel` de l'administrateur. Elle
 * permet de récupérer la liste de toutes les ventes (`getAllVentesSnack`) et de
 * les filtrer par jour (`getVentesByDate`) pour calculer le chiffre d'affaires.
 */
public class VenteSnackDAOImpl extends GenericDAOImpl<VenteSnack> implements VenteSnackDAO {

    public VenteSnackDAOImpl() {
        super("ventes_snack.dat");
    }

    @Override
    public void addVenteSnack(VenteSnack vente) {
        this.data.add(vente);
        saveToFile();
    }

    // Dans VenteSnackDAOImpl.java
    @Override
    public VenteSnack getVenteSnackById(int id) {
        for (VenteSnack vente : this.data) {
            if (vente.getIdVente() == id) {
                return vente;
            }
        }
        return null;
    }

    @Override
    public List<VenteSnack> getAllVentesSnack() {
        return new ArrayList<>(this.data);
    }

    @Override
    public List<VenteSnack> getVentesByDate(LocalDate date) {
        List<VenteSnack> resultat = new ArrayList<>();
        // On parcourt toutes les ventes...
        for (VenteSnack vente : this.data) {
            // ...et on compare la date de la vente (en ignorant l'heure) avec la date demandée.
            if (vente.getDateVente().toLocalDate().isEqual(date)) {
                resultat.add(vente);
            }
        }
        return resultat;
    }
}
