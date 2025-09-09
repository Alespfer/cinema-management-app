// ========================================================================
// FICHIER : GenreDAOImpl.java
// ========================================================================
package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Genre;
import com.mycompany.cinema.dao.GenreDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation pour la gestion de la persistance des objets Genre.
 * Interagit avec le fichier "genres.dat".
 *
 */
public class GenreDAOImpl extends GenericDAOImpl<Genre> implements GenreDAO {

    public GenreDAOImpl() {
        super("genres.dat");
    }

    /**
     * Ajoute un nouveau genre au système.
     *
     * @param genre L'objet Genre à enregistrer.
     */
    @Override
    public void ajouterGenre(Genre genre) {
        this.data.add(genre);
        sauvegarderDansFichier();
    }

    /**
     * Recherche un genre par son identifiant.
     *
     * @param id L'identifiant du genre.
     * @return L'objet Genre correspondant, ou `null` si non trouvé.
     */
    @Override
    public Genre trouverGenreParId(int id) {
        for (Genre genre : this.data) {
            if (genre.getId() == id) {
                return genre;
            }
        }
        return null;
    }

    /**
     * Retourne la liste complète de tous les genres.
     *
     * @return Une copie de la liste pour la sécurité des données.
     */
    @Override
    public List<Genre> trouverTousLesGenres() {
        return new ArrayList<>(this.data);
    }

    /**
     * Met à jour les informations d'un genre existant.
     *
     * @param genreMisAJour L'objet Genre contenant les nouvelles données.
     */
    @Override
    public void mettreAJourGenre(Genre genreMisAJour) {
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getId() == genreMisAJour.getId()) {
                this.data.set(i, genreMisAJour);
                sauvegarderDansFichier();
                return;
            }
        }
    }

    /**
     * Supprime un genre de la source de données à partir de son identifiant.
     *
     * @param id L'identifiant du genre à supprimer.
     */
    @Override
    public void supprimerGenreParId(int id) {
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
