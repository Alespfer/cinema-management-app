package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.dao.FilmDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        boolean changed = this.data.removeIf(film -> film.getId() == id);
        if(changed) {
            saveToFile();
        }
    }
    
    /**
     * Trouve des films en cherchant un mot-clé dans leur titre.
     * La recherche n'est pas sensible à la casse (majuscules/minuscules).
     * @param keyword Le texte à rechercher dans le titre.
     * @return Une liste de films dont le titre contient le mot-clé.
     */
    @Override
    public List<Film> findFilmsByTitre(String keyword) {
        List<Film> filmsTrouves = new ArrayList<>();
        // On met le mot-clé en minuscules une seule fois avant la boucle pour être efficace.
        String motCleMinuscule = keyword.toLowerCase();

        for (Film film : this.data) {
            // On compare le titre du film (aussi en minuscules) avec le mot-clé.
            if (film.getTitre().toLowerCase().contains(motCleMinuscule)) {
                filmsTrouves.add(film);
            }
        }
        return filmsTrouves;
    }
}