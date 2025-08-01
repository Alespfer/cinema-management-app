package com.mycompany.cinema.util;

// Importe les classes du Modèle et les classes DAO d'implémentation.
import com.mycompany.cinema.*;
import com.mycompany.cinema.dao.impl.*;
import java.util.List;

/**
 * Axelle : Voici une classe utilitaire importante pour la logique métier.
 * Son unique rôle est de fournir un nouvel ID unique pour n'importe quel type d'objet
 * que l'on souhaite créer (un nouveau film, un nouveau client, une nouvelle réservation, etc.).
 *
 * COMMENT ÇA MARCHE ?
 * Quand la couche Service a besoin de créer un nouvel objet (suite à une action dans l'interface,
 * comme un admin qui ajoute un film), elle appellera la méthode correspondante ici
 * (ex: `getNextFilmId()`). Cette méthode va lire TOUS les films existants dans le fichier,
 * trouver l'ID le plus élevé, et renvoyer ce nombre + 1.
 *
 * Cela garantit qu'il n'y aura jamais deux objets avec le même ID.
 * Tu n'as pas besoin de l'appeler, la couche Service s'en occupe pour toi.
 */
public final class IdManager {

    /**
     * Le constructeur est privé et lance une erreur si on essaie de l'appeler.
     * C'est une sécurité pour forcer l'utilisation des méthodes statiques.
     */
    private IdManager() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne peut pas être instanciée.");
    }

    // --- Ci-dessous, une méthode pour chaque type d'objet de l'application ---

    /**
     * Calcule et retourne le prochain ID disponible pour un objet Film.
     * @return un int, qui sera le nouvel ID unique pour le film à créer.
     */
    public static int getNextFilmId() {
        // Appelle la logique générique en lui passant la liste de tous les films
        // et une petite fonction pour lui dire comment trouver l'ID dans un objet Film.
        return calculateNextId(new FilmDAOImpl().getAllFilms(), film -> film.getId());
    }

    /**
     * Calcule et retourne le prochain ID disponible pour un objet Client.
     * @return un int, le nouvel ID unique.
     */
    public static int getNextClientId() {
        return calculateNextId(new ClientDAOImpl().getAllClients(), client -> client.getId());
    }
    
    /**
     * Calcule et retourne le prochain ID disponible pour un objet Billet.
     * @return un int, le nouvel ID unique.
     */
    public static int getNextBilletId() {
        return calculateNextId(new BilletDAOImpl().getAllBillets(), billet -> billet.getId());
    }
    
    /**
     * Calcule et retourne le prochain ID disponible pour une Reservation.
     * @return un int, le nouvel ID unique.
     */
    public static int getNextReservationId() {
        return calculateNextId(new ReservationDAOImpl().getAllReservations(), reservation -> reservation.getId());
    }

    // ... (les autres méthodes suivent le même principe pour chaque entité) ...

    public static int getNextPersonnelId() {
        return calculateNextId(new PersonnelDAOImpl().getAllPersonnel(), personnel -> personnel.getId());
    }

    public static int getNextProduitSnackId() {
        return calculateNextId(new ProduitSnackDAOImpl().getAllProduits(), produit -> produit.getId());
    }

    public static int getNextSalleId() {
        return calculateNextId(new SalleDAOImpl().getAllSalles(), salle -> salle.getId());
    }

    public static int getNextSeanceId() {
        return calculateNextId(new SeanceDAOImpl().getAllSeances(), seance -> seance.getId());
    }

    public static int getNextSiegeId() {
        return calculateNextId(new SiegeDAOImpl().getAllSieges(), siege -> siege.getId());
    }

    public static int getNextVenteSnackId() {
        return calculateNextId(new VenteSnackDAOImpl().getAllVentesSnack(), vente -> vente.getIdVente());
    }

    public static int getNextCaisseId() {
        return calculateNextId(new CaisseDAOImpl().getAllCaisses(), caisse -> caisse.getId());
    }

    public static int getNextGenreId() {
        return calculateNextId(new GenreDAOImpl().getAllGenres(), genre -> genre.getId());
    }

    public static int getNextRoleId() {
        return calculateNextId(new RoleDAOImpl().getAllRoles(), role -> role.getId());
    }

    public static int getNextTarifId() {
        return calculateNextId(new TarifDAOImpl().getAllTarifs(), tarif -> tarif.getId());
    }

    public static int getNextPlanningId() {
        return calculateNextId(new PlanningDAOImpl().getAllPlannings(), planning -> planning.getId());
    }

    /**
     * Méthode générique privée qui contient la logique de calcul.
     * Elle parcourt une liste, trouve la valeur maximale de l'ID, et retourne max + 1.
     * @param <T> Le type générique de l'objet (Film, Client, etc.).
     * @param list La liste complète des objets à analyser.
     * @param extractor Le petit "outil" (l'interface) qui sait comment extraire l'ID de l'objet.
     * @return L'ID le plus élevé trouvé dans la liste, incrémenté de 1.
     */
    private static <T> int calculateNextId(List<T> list, IdExtractor<T> extractor) {
        int maxId = 0;
        // Parcours simple de toute la liste.
        for (T element : list) {
            int currentId = extractor.extractId(element);
            // Si l'ID de l'élément courant est plus grand que le max trouvé jusqu'ici...
            if (currentId > maxId) {
                // ... il devient le nouveau max.
                maxId = currentId;
            }
        }
        // Retourne le plus grand ID trouvé + 1. Si la liste est vide, retourne 0 + 1 = 1.
        return maxId + 1;
    }
}