package com.mycompany.cinema.service;

import com.mycompany.cinema.Film;
import java.util.List;

// Interface de base pour les fonctionnalités communes
public interface CinemaService {
    List<Film> getFilmsAffiche();
    Film getFilmDetails(int filmId);
    List<Film> findFilmsByTitre(String keyword);
}