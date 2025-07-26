package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Genre;
import com.mycompany.cinema.dao.GenreDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreDAOImpl extends GenericDAOImpl<Genre> implements GenreDAO {

    public GenreDAOImpl() { super("genres.dat"); }

    @Override
    public void addGenre(Genre genre) {
        this.data.add(genre);
        saveToFile();
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        for (Genre g : this.data) {
            if (g.getId() == id) {
                return Optional.of(g);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Genre> getAllGenres() {
        return new ArrayList<>(this.data);
    }
}