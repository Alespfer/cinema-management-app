package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Comporte;
import com.mycompany.cinema.dao.ComporteDAO;
import java.util.ArrayList;
import java.util.List;

// DAO pour la gestion des lignes de vente (produits achetés par vente).
public class ComporteDAOImpl extends GenericDAOImpl<Comporte> implements ComporteDAO {

    // Initialise le fichier de données pour les lignes de ventes.
    public ComporteDAOImpl() {
        super("lignes_ventes.dat");
    }

    // Ajoute une nouvelle ligne de vente.
    @Override
    public void addLigneVente(Comporte comporte) {
        this.data.add(comporte);
        saveToFile();
    }

    // Retourne toutes les lignes de vente associées à une vente donnée.
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
