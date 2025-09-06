package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Genre;
import com.mycompany.cinema.dao.GenreDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète pour gérer la sauvegarde des genres de films dans le
 * fichier "genres.dat".
 *
 * Pour le développeur de l'interface graphique : cette classe fournit les
 * données pour tous les menus déroulants de sélection de genre, que ce soit
 * dans le `ProgrammationPanel` pour le client ou dans les panneaux
 * d'administration.
 */
public class GenreDAOImpl extends GenericDAOImpl<Genre> implements GenreDAO {

    public GenreDAOImpl() {
        super("genres.dat");
    }

    @Override
    public void addGenre(Genre genre) {
        this.data.add(genre);
        saveToFile();
    }

    @Override
    public Genre getGenreById(int id) {
        for (Genre g : this.data) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        // Retourne une copie de la liste pour la sécurité.
        return new ArrayList<>(this.data);
    }

    @Override
    public void updateGenre(Genre genre) {
        for (int i = 0; i < this.data.size(); i++) {
            Genre g = this.data.get(i);
            if (g.getId() == genre.getId()) {
                this.data.set(i, genre);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void deleteGenre(int id) {
        for (int i = 0; i < this.data.size(); i++) {
            Genre g = this.data.get(i);
            if (g.getId() == id) {
                this.data.remove(i);
                saveToFile();
                return;
            }
        }
    }
}
