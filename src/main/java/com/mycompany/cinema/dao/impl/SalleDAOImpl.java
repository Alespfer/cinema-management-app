package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Salle;
import com.mycompany.cinema.dao.SalleDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalleDAOImpl extends GenericDAOImpl<Salle> implements SalleDAO {

    public SalleDAOImpl() {
        super("salles.dat");
    }

    @Override
    public void addSalle(Salle salle) {
        this.data.add(salle);
        saveToFile();
    }

    @Override
    public Optional<Salle> getSalleById(int id) {
        for (Salle salle : this.data) {
            if (salle.getId() == id) {
                return Optional.of(salle);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Salle> getAllSalles() {
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateSalle(Salle updatedSalle) {
        // Pour mettre à jour, on parcourt la liste avec un index.
        for (int i = 0; i < this.data.size(); i++) {
            // Quand on trouve la salle avec le bon ID...
            if (this.data.get(i).getId() == updatedSalle.getId()) {
                // ...on la remplace par le nouvel objet à la même position.
                this.data.set(i, updatedSalle);
                saveToFile(); // On n'oublie pas de sauvegarder les changements sur le disque.
                return; // L'objet est trouvé et modifié, on peut sortir de la méthode.
            }
        }
    }

    @Override
    public void deleteSalle(int id) {
        // La méthode removeIf est pratique mais non vue en cours.
        // On utilise donc un Iterator pour la suppression.
        boolean changed = this.data.removeIf(salle -> salle.getId() == id);
        if(changed) {
            saveToFile();
        }
    }
}