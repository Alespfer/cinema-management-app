package com.mycompany.cinema.dao;
import com.mycompany.cinema.Film;
import java.util.List;
import java.util.Optional;

public interface FilmDAO {
    void addFilm(Film film);
    Optional<Film> getFilmById(int id);
    List<Film> getAllFilms();
    void updateFilm(Film film);
    void deleteFilm(int id);
    List<Film> findFilmsByTitre(String keyword); 
}