package com.mycompany.cinema.dao;

import com.mycompany.cinema.Comporte;
import java.util.List;

/**
 * Contrat pour la gestion des lignes de vente (table Comporte).
 * Lie une VenteSnack à des ProduitsSnack.
 */
public interface ComporteDAO {
    void addLigneVente(Comporte comporte);
    List<Comporte> getLignesByVenteId(int venteId);
    void rechargerDonnees();

}