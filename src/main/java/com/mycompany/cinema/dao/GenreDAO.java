package com.mycompany.cinema.dao;

import com.mycompany.cinema.Genre;
import java.util.List;
import java.util.Optional;

public interface GenreDAO {
    void addGenre(Genre genre);
    Optional<Genre> getGenreById(int id);
    List<Genre> getAllGenres();
    
    
    void rechargerDonnees();

}