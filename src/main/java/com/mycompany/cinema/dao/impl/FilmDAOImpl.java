// Fichier : src/main/java/com/mycompany/cinema/dao/impl/FilmDAOImpl.java
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.dao.FilmDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilmDAOImpl extends GenericDAOImpl<Film> implements FilmDAO {

    public FilmDAOImpl() {
        super("films.dat"); // On spécifie le nom du fichier de données
    }

    @Override
    public void addFilm(Film film) {
        this.data.add(film);
        saveToFile();
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return this.data.stream().filter(film -> film.getId() == id).findFirst();
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(this.data); // Retourne une copie pour la sécurité
    }

    @Override
    public void updateFilm(Film updatedFilm) {
        getFilmById(updatedFilm.getId()).ifPresent(existingFilm -> {
            int index = this.data.indexOf(existingFilm);
            this.data.set(index, updatedFilm);
            saveToFile();
        });
    }

    @Override
    public void deleteFilm(int id) {
        if (this.data.removeIf(film -> film.getId() == id)) {
            saveToFile();
        }
    }
}