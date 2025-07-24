package com.mycompany.cinema.util;

import com.mycompany.cinema.*;
import com.mycompany.cinema.dao.*;
import com.mycompany.cinema.dao.impl.*;

import java.util.List; // Import nécessaire pour manipuler les listes

/**
 * Classe utilitaire pour la gestion des identifiants uniques.
 * 
 * RÔLE : Dans une architecture sans base de données, la génération de nouvelles
 * clés primaires (ID) n'est pas automatique. Cette classe a pour unique 
 * responsabilité de fournir un nouvel ID unique pour chaque type d'entité,
 * en se basant sur la valeur maximale existante dans les fichiers de données.
 *
 * CONCEPTION : Il s'agit d'une classe "final" avec un constructeur privé 
 * pour empêcher toute instanciation. Ses méthodes sont toutes "static"
 * pour être appelées directement (ex: IdManager.getNextFilmId()).
 */
public final class IdManager {

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private IdManager() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne doit pas être instanciée.");
    }

    // --- Gestionnaires d'ID pour chaque entité ---

    /**
     * Calcule et retourne le prochain identifiant unique disponible pour un Film.
     * Cette méthode illustre l'algorithme de recherche de la valeur maximale.
     * @return le prochain ID de film.
     */
    public static int getNextFilmId() {
        // Étape 1 : Récupérer la liste de tous les films existants.
        FilmDAO dao = new FilmDAOImpl();
        List<Film> tousLesFilms = dao.getAllFilms();

        // Étape 2 : Initialiser l'identifiant maximum trouvé à 0.
        // Si la liste est vide, cette valeur restera 0.
        int maxId = 0;

        // Étape 3 : Parcourir la liste pour trouver l'ID le plus grand.
        // C'est une boucle "for-each", très commune en Java.
        for (Film film : tousLesFilms) {
            // Pour chaque film, on compare son ID avec le maximum actuel.
            if (film.getId() > maxId) {
                // Si l'ID du film courant est plus grand, il devient le nouveau maximum.
                maxId = film.getId();
            }
        }

        // Étape 4 : Le nouvel ID sera le maximum trouvé + 1.
        // Si la liste était vide, maxId est 0, donc le premier ID sera 1.
        return maxId + 1;
    }

    /**
     * Calcule et retourne le prochain identifiant unique disponible pour un Client.
     * @return le prochain ID de client.
     */
    public static int getNextClientId() {
        ClientDAO dao = new ClientDAOImpl();
        List<Client> tousLesClients = dao.getAllClients();
        int maxId = 0;
        for (Client client : tousLesClients) {
            if (client.getId() > maxId) {
                maxId = client.getId();
            }
        }
        return maxId + 1;
    }
    
    /**
     * Calcule et retourne le prochain identifiant unique disponible pour un Billet.
     * @return le prochain ID de billet.
     */
    public static int getNextBilletId() {
        BilletDAO dao = new BilletDAOImpl();
        List<Billet> tousLesBillets = dao.getAllBillets();
        int maxId = 0;
        for (Billet billet : tousLesBillets) {
            if (billet.getId() > maxId) {
                maxId = billet.getId();
            }
        }
        return maxId + 1;
    }

    // Note : Les méthodes suivantes sont à décommenter lorsque vous aurez créé les DAO correspondants.
    // La logique reste rigoureusement la même.

    /*
    public static int getNextCaisseId() {
        CaisseDAO dao = new CaisseDAOImpl();
        List<Caisse> toutesLesCaisses = dao.getAllCaisses();
        int maxId = 0;
        for (Caisse caisse : toutesLesCaisses) {
            if (caisse.getId() > maxId) {
                maxId = caisse.getId();
            }
        }
        return maxId + 1;
    }

    public static int getNextGenreId() {
        GenreDAO dao = new GenreDAOImpl();
        List<Genre> tousLesGenres = dao.getAllGenres();
        int maxId = 0;
        for (Genre genre : tousLesGenres) {
            if (genre.getId() > maxId) {
                maxId = genre.getId();
            }
        }
        return maxId + 1;
    }
    */
    
    public static int getNextPersonnelId() {
        PersonnelDAO dao = new PersonnelDAOImpl();
        List<Personnel> toutLePersonnel = dao.getAllPersonnel();
        int maxId = 0;
        for (Personnel personnel : toutLePersonnel) {
            if (personnel.getId() > maxId) {
                maxId = personnel.getId();
            }
        }
        return maxId + 1;
    }

    /*
    public static int getNextPlanningId() {
        PlanningDAO dao = new PlanningDAOImpl();
        List<Planning> tousLesPlannings = dao.getAllPlannings();
        int maxId = 0;
        for (Planning planning : tousLesPlannings) {
            if (planning.getId() > maxId) {
                maxId = planning.getId();
            }
        }
        return maxId + 1;
    }
    */
    
    public static int getNextProduitSnackId() {
        ProduitSnackDAO dao = new ProduitSnackDAOImpl();
        List<ProduitSnack> tousLesProduits = dao.getAllProduits();
        int maxId = 0;
        for (ProduitSnack produit : tousLesProduits) {
            if (produit.getId() > maxId) {
                maxId = produit.getId();
            }
        }
        return maxId + 1;
    }
    
    public static int getNextReservationId() {
        ReservationDAO dao = new ReservationDAOImpl();
        List<Reservation> toutesLesReservations = dao.getAllReservations();
        int maxId = 0;
        for (Reservation reservation : toutesLesReservations) {
            if (reservation.getId() > maxId) {
                maxId = reservation.getId();
            }
        }
        return maxId + 1;
    }
    
    /*
    public static int getNextRoleId() {
        RoleDAO dao = new RoleDAOImpl();
        List<Role> tousLesRoles = dao.getAllRoles();
        int maxId = 0;
        for (Role role : tousLesRoles) {
            if (role.getId() > maxId) {
                maxId = role.getId();
            }
        }
        return maxId + 1;
    }
    */

    public static int getNextSalleId() {
        SalleDAO dao = new SalleDAOImpl();
        List<Salle> toutesLesSalles = dao.getAllSalles();
        int maxId = 0;
        for (Salle salle : toutesLesSalles) {
            if (salle.getId() > maxId) {
                maxId = salle.getId();
            }
        }
        return maxId + 1;
    }
    
    public static int getNextSeanceId() {
        SeanceDAO dao = new SeanceDAOImpl();
        List<Seance> toutesLesSeances = dao.getAllSeances();
        int maxId = 0;
        for (Seance seance : toutesLesSeances) {
            if (seance.getId() > maxId) {
                maxId = seance.getId();
            }
        }
        return maxId + 1;
    }
    
    public static int getNextSiegeId() {
        SiegeDAO dao = new SiegeDAOImpl();
        List<Siege> tousLesSieges = dao.getAllSieges();
        int maxId = 0;
        for (Siege siege : tousLesSieges) {
            if (siege.getId() > maxId) {
                maxId = siege.getId();
            }
        }
        return maxId + 1;
    }

    /*
    public static int getNextTarifId() {
        TarifDAO dao = new TarifDAOImpl();
        List<Tarif> tousLesTarifs = dao.getAllTarifs();
        int maxId = 0;
        for (Tarif tarif : tousLesTarifs) {
            if (tarif.getId() > maxId) {
                maxId = tarif.getId();
            }
        }
        return maxId + 1;
    }
    */
    
    public static int getNextVenteSnackId() {
        VenteSnackDAO dao = new VenteSnackDAOImpl();
        List<VenteSnack> toutesLesVentes = dao.getAllVentesSnack();
        int maxId = 0;
        for (VenteSnack vente : toutesLesVentes) {
            if (vente.getIdVente() > maxId) { // Adaptation pour le nom du getter spécifique
                maxId = vente.getIdVente();
            }
        }
        return maxId + 1;
    }
}