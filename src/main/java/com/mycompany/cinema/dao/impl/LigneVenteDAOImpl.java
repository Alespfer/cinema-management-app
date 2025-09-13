// ========================================================================
// LigneVenteDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.LigneVente;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.cinema.dao.LigneVenteDAO;

/**
 * Implémentation pour la gestion des lignes de vente de snacks.
 */
public class LigneVenteDAOImpl extends GenericDAOImpl<LigneVente> implements LigneVenteDAO {

    public LigneVenteDAOImpl() {
        super("lignes_ventes.dat");
    }

    /**
     * Enregistre une nouvelle ligne de vente.
     *
     * @param ligneDeVente L'objet LigneVente à ajouter.
     */
    @Override
    public void ajouterLigneVente(LigneVente ligneDeVente) {
        this.data.add(ligneDeVente);
        sauvegarderDansFichier();
    }

    /**
     * Récupère toutes les lignes de produits pour un ticket de caisse donné.
     * @param idVente L'identifiant de la vente de snack.
     * @return Une liste d'objets LigneVente.
     */
    @Override
    public List<LigneVente> trouverLignesParIdVente(int idVente) {
        List<LigneVente> lignesTrouvees = new ArrayList<>();
        for (LigneVente ligne : this.data) {
            if (ligne.getIdVente() == idVente) {
                lignesTrouvees.add(ligne);
            }
        }
        return lignesTrouvees;
    }

     /**
     * Retourne la totalité des lignes de vente enregistrées.
     * @return Une copie de la liste de toutes les lignes de vente.
     */
    @Override
    public List<LigneVente> trouverToutesLesLignesVente() {
        return new ArrayList<>(this.data);
    }
}