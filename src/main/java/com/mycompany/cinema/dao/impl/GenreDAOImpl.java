package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Genre;
import com.mycompany.cinema.dao.GenreDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO pour gérer les genres de films.
public class GenreDAOImpl extends GenericDAOImpl<Genre> implements GenreDAO {

    // Initialise la DAO avec le fichier de genres.
    public GenreDAOImpl() {
        super("genres.dat");
    }

    // Ajoute un nouveau genre à la base de données.
    @Override
    public void addGenre(Genre genre) {
        this.data.add(genre);
        saveToFile();
    }

    // Recherche un genre à partir de son identifiant.
    @Override
    public Optional<Genre> getGenreById(int id) {
        for (Genre g : this.data) {
            if (g.getId() == id) {
                return Optional.of(g);
            }
        }
        return Optional.empty();
    }

    // Retourne la liste complète des genres enregistrés.
    @Override
    public List<Genre> getAllGenres() {
        return new ArrayList<>(this.data);
    }
}
