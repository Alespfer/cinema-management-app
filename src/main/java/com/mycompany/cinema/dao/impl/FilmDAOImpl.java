// ========================================================================
// FICHIER : FilmDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Film;
import com.mycompany.cinema.dao.FilmDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion du catalogue de films. Interagit avec le
 * fichier "films.dat".
 *
 * Elle fournit les données pour l'affichage de la programmation, les détails
 * des films, et permet les opérations de gestion (création, modification,
 * suppression) dans le panneau d'administration.
 */
public class FilmDAOImpl extends GenericDAOImpl<Film> implements FilmDAO {

    public FilmDAOImpl() {
        super("films.dat");
    }

    /**
     * Ajoute un nouveau film au catalogue.
     *
     * @param film L'objet Film à enregistrer.
     */
    @Override
    public void ajouterFilm(Film film) {
        this.data.add(film);
        sauvegarderDansFichier();
    }

    /**
     * Recherche un film par son identifiant unique.
     *
     * @param id L'identifiant du film.
     * @return L'objet Film correspondant ou `null` si non trouvé.
     */
    @Override
    public Film trouverFilmParId(int id) {
        for (Film film : this.data) {
            if (film.getId() == id) {
                return film;
            }
        }
        return null;
    }
    
    
    /**
     * Recherche des films dont le titre contient un mot-clé donné.
     * La recherche est insensible à la cass (on convertit le mot en minuscules
     * avant de comparer les titres)
     * @param motCle Le texte à rechercher dans les titres.
     * @return Une liste de films correspondant au critère.
     */
    @Override
    public List<Film> rechercherFilmsParTitre(String motCle) {
        List<Film> filmsCorrespondants = new ArrayList<>();
        String motCleMinuscule = motCle.toLowerCase();
        for (Film film : this.data) {
            if (film.getTitre().toLowerCase().contains(motCleMinuscule)) {
                filmsCorrespondants.add(film);
            }
        }
        return filmsCorrespondants;
    }

    /**
     * Retourne la liste complète de tous les films.
     *
     * @return Une copie de la liste pour protéger les données.
     */
    @Override
    public List<Film> trouverTousLesFilms() {
        return new ArrayList<>(this.data);
    }

    /**
     * Met à jour les informations d'un film existant.
     *
     * @param filmMisAJour L'objet Film avec les données mises à jour.
     */
    @Override
    public void mettreAJourFilm(Film filmMisAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == filmMisAJour.getId()) {
                this.data.set(i, filmMisAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }

     /**
     * Supprime un film du catalogue à partir de son ID.
     * @param id L'identifiant du film à supprimer.
     */
    @Override
    public void supprimerFilmParId(int id) {
        int indexASupprimer = -1;

        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == id) {
                indexASupprimer = i;
                break;
            }
        }

        if (indexASupprimer != -1) {
            this.data.remove(indexASupprimer);
            sauvegarderDansFichier();
        }
    }
}