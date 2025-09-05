// Fichier : CaisseDAOImpl.java
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Caisse;
import com.mycompany.cinema.dao.CaisseDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour gérer la sauvegarde des caisses enregistreuses.
 * S'occupe du fichier "caisses.dat".
 *
 * Pour le développeur de l'interface graphique : cette classe est utilisée par
 * le back-office. Le `PointDeVenteFrame` l'utilisera pour identifier la caisse,
 * et le `ReportingPanel` pour afficher les détails d'une vente. L'interface
 * client n'est pas concernée.
 */
public class CaisseDAOImpl extends GenericDAOImpl<Caisse> implements CaisseDAO {

    public CaisseDAOImpl() {
        super("caisses.dat");
    }

    @Override
    public void addCaisse(Caisse caisse) {
        this.data.add(caisse);
        saveToFile();
    }

    @Override
    public Caisse getCaisseById(int id) {
        for (Caisse c : this.data) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Caisse> getAllCaisses() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateCaisse(Caisse updatedCaisse) {
        // On parcourt la liste avec un index.
        for (int i = 0; i < this.data.size(); i++) {
            // Si on trouve l'objet à mettre à jour...
            if (this.data.get(i).getId() == updatedCaisse.getId()) {
                // ...on le remplace par la nouvelle version à la même position.
                this.data.set(i, updatedCaisse);
                saveToFile();
                return; // On sort de la méthode car la mise à jour est faite.
            }
        }
    }

    @Override
    public void deleteCaisse(int id) {
        boolean changed = false;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                this.data.remove(i);
                changed = true;
                break; // On sort car on ne supprime qu'un seul élément.
            }
        }
        if (changed) {
            saveToFile();
        }
    }
}
