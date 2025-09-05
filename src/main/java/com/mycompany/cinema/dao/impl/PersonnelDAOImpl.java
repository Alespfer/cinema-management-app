package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Personnel;
import com.mycompany.cinema.dao.PersonnelDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion des données des employés dans le
 * fichier "personnel.dat". Cette classe contient toute la logique de bas niveau
 * pour la persistance du personnel.
 *
 * Pour le développeur de l'interface graphique : vous ne toucherez jamais à
 * cette classe. Cependant, toutes les actions que vous implémenterez dans le
 * panneau `GestionPersonnelPanel` (créer un nouvel employé, mettre à jour son
 * rôle, le supprimer) feront appel au 'AdminService' qui, à son tour, utilisera
 * les méthodes de cette classe pour effectuer les modifications dans la base de
 * données de fichiers.
 */
public class PersonnelDAOImpl extends GenericDAOImpl<Personnel> implements PersonnelDAO {

    public PersonnelDAOImpl() {
        super("personnel.dat");
    }

    @Override
    public void addPersonnel(Personnel personnel) {
        this.data.add(personnel);
        saveToFile();
    }

    // Dans PersonnelDAOImpl.java
    @Override
    public Personnel getPersonnelById(int id) {
        for (Personnel p : this.data) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Personnel> getAllPersonnel() {
        // Retourne une copie de la liste pour éviter des modifications accidentelles de l'extérieur.
        return new ArrayList<>(this.data);
    }

    @Override
    public void updatePersonnel(Personnel updatedPersonnel) {
        // On cherche l'employé par son ID pour le remplacer par sa version mise à jour.
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedPersonnel.getId()) {
                this.data.set(i, updatedPersonnel);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void deletePersonnel(int id) {
        // On cherche l'employé par son ID pour le supprimer de la liste.
        boolean changed = false;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                this.data.remove(i);
                changed = true;
                break;
            }
        }
        if (changed) {
            saveToFile();
        }
    }
}
