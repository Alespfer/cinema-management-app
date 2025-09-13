// ========================================================================
// PersonnelDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.dao.PersonnelDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation concrète pour la gestion de la persistance des données des
 * employés. Interagit avec le fichier "personnel.dat".
 */
public class PersonnelDAOImpl extends GenericDAOImpl<Personnel> implements PersonnelDAO {

    public PersonnelDAOImpl() {
        super("personnel.dat");
    }

    /**
     * Ajoute un nouveau membre du personnel.
     * @param personnel Le nouvel employé à enregistrer.
     */
    @Override
    public void ajouterPersonnel(Personnel personnel) {
        this.data.add(personnel);
        sauvegarderDansFichier();
    }

    /**
     * Recherche un membre du personnel par son identifiant unique.
     * @param id L'identifiant de l'employé.
     * @return L'objet Personnel correspondant, ou `null` si non trouvé.
     */
    @Override
    public Personnel trouverPersonnelParId(int id) {
        for (Personnel membre : this.data) {
            if (membre.getId() == id) {
                return membre;
            }
        }
        return null;
    }
    
    
    
    /**
     * Retourne la liste de tous les membres du personnel.
     * @return Une copie de la liste des employés.
     */
    @Override
    public List<Personnel> trouverToutLePersonnel() {
        return new ArrayList<>(this.data);
    }
    

   /**
     * Met à jour les informations d'un membre du personnel.
     * @param personnelMisAJour L'objet Personnel avec les données mises à jour.
     */
    @Override
    public void mettreAJourPersonnel(Personnel personnelMisAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == personnelMisAJour.getId()) {
                this.data.set(i, personnelMisAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }

   /**
     * Supprime un membre du personnel de la source de données.
     * @param id L'identifiant de l'employé à supprimer.
     */
    @Override
    public void supprimerPersonnelParId(int id) {
        int indexASupprimer = -1;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                indexASupprimer = i;
                break;
            }
        }
        if (indexASupprimer != -1) {
            this.data.remove(indexASupprimer);
            sauvegarderDansFichier();
        }
    }
}
