package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.dao.FilmDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour la gestion des films (ajout, recherche, modification, suppression).
public class FilmDAOImpl extends GenericDAOImpl<Film> implements FilmDAO {

    // Initialise le DAO avec le fichier associé aux films.
    public FilmDAOImpl() {
        super("films.dat");
    }

    // Ajoute un nouveau film à la base de données.
    @Override
    public void addFilm(Film film) {
        this.data.add(film);
        saveToFile();
    }

    // Recherche un film par son identifiant.
    @Override
    public Optional<Film> getFilmById(int id) {
        for (Film film : this.data) {
            if (film.getId() == id) {
                return Optional.of(film);
            }
        }
        return Optional.empty();
    }

    // Retourne l'ensemble des films enregistrés.
    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(this.data);
    }

    // Met à jour les informations d'un film existant.
    @Override
    public void updateFilm(Film updatedFilm) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == updatedFilm.getId()) {
                this.data.set(i, updatedFilm);
                saveToFile();
                return;
            }
        }
    }

    // Supprime un film selon son identifiant.
    @Override
    public void deleteFilm(int id) {
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

    // Recherche les films dont le titre contient un mot-clé donné (sans tenir compte de la casse).
    @Override
    public List<Film> findFilmsByTitre(String keyword) {
        List<Film> filmsTrouves = new ArrayList<>();
        String motCleMinuscule = keyword.toLowerCase();
        for (Film film : this.data) {
            if (film.getTitre().toLowerCase().contains(motCleMinuscule)) {
                filmsTrouves.add(film);
            }
        }
        return filmsTrouves;
    }
}
