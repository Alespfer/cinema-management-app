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
    
    /**
     * On implémente la logique de recherche. On parcourt tous les films et on ajoute à une liste de résultats ceux 
     * dont le titre (en minuscules, pour ne pas être sensible à la casse) contient le mot-clé (lui aussi en minuscules).
     * @param keyword
     * @return 
     */
    @Override
    public List<Film> findFilmsByTitre(String keyword) {
        List<Film> filmsTrouves = new ArrayList<>();
        String motCleMinuscule = keyword.toLowerCase(); // Pour une recherche insensible à la casse

        for (Film film : this.data) {
            // On vérifie si le titre du film en minuscule contient le mot-clé en minuscule
            if (film.getTitre().toLowerCase().contains(motCleMinuscule)) {
                filmsTrouves.add(film);
            }
        }
        return filmsTrouves;
    }
}