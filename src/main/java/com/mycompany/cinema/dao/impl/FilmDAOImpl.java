package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.dao.FilmDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour la gestion du catalogue de films dans le fichier "films.dat".
 * C'est l'une des classes de gestion les plus importantes.
 * 
 * Pour le développeur de l'interface graphique : cette classe fournit toutes les données
 * nécessaires pour les panneaux liés aux films.
 * - `getAllFilms` et `findFilmsByTitre` alimentent le tableau du `ProgrammationPanel`.
 * - `getFilmById` est crucial pour afficher toutes les informations dans le `FilmDetailPanel`.
 * - Les autres méthodes (`add`, `update`, `delete`) sont les actions effectuées par
 *   les boutons du `GestionFilmsPanel` de l'administrateur.
 */
public class FilmDAOImpl extends GenericDAOImpl<Film> implements FilmDAO {

    public FilmDAOImpl() {
        super("films.dat");
    }

    @Override
    public void addFilm(Film film) {
        this.data.add(film);
        saveToFile();
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        for (Film film : this.data) {
            if (film.getId() == id) {
                return Optional.of(film);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(this.data);
    }

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

    @Override
    public List<Film> findFilmsByTitre(String keyword) {
        List<Film> filmsTrouves = new ArrayList<>();
        // On convertit le mot-clé en minuscules une seule fois pour l'efficacité.
        String motCleMinuscule = keyword.toLowerCase();
        for (Film film : this.data) {
            // On compare les titres en minuscules pour que la recherche ne soit pas sensible à la casse.
            if (film.getTitre().toLowerCase().contains(motCleMinuscule)) {
                filmsTrouves.add(film);
            }
        }
        return filmsTrouves;
    }
}