package com.mycompany.cinema.util;

import com.mycompany.cinema.*;
import com.mycompany.cinema.dao.impl.*;
import java.util.List;

/**
 * Cette classe fournit un nouvel ID, garanti unique, pour n'importe quel
 * objet à créer (un nouveau film, un client, une réservation, etc.).
 *
 * Quand une action de l'utilisateur demande de créer un nouvel objet (par exemple, un admin
 * qui ajoute un film), le code va appeler la méthode correspondante ici
 * (ex: `obtenirProchainIdFilm()`). Cette méthode devra lire tous les films existants,
 * trouver l'ID le plus grand, et renvoyer ce nombre + 1.
 *
 */
public final class IdManager {

    private IdManager() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne peut pas être instanciée.");
    }

    // --- Pour chaque type d'objet de l'application, on a une méthode dédiée ---
    // 1. Calcule et retourne le prochain ID disponible pour un objet donné
    // 2. Retourne un entier, nouvel ID unique de l'objet 

    /**
     * Calcule et retourne le prochain ID disponible pour un objet Film.
     * @return un entier, qui sera le nouvel ID unique pour le film à créer.
     */
    public static int obtenirProchainIdFilm() {
        return calculerProchainId(new FilmDAOImpl().trouverTousLesFilms(), film -> film.getId());
    }

   
    public static int obtenirProchainIdClient() {
        return calculerProchainId(new ClientDAOImpl().trouverTousLesClients(), client -> client.getId());
    }

  
    public static int obtenirProchainIdBillet() {
        return calculerProchainId(new BilletDAOImpl().trouverTousLesBillets(), billet -> billet.getId());
    }

    
    public static int obtenirProchainIdReservation() {
        return calculerProchainId(new ReservationDAOImpl().trouverToutesLesReservations(), reservation -> reservation.getId());
    }


    public static int obtenirProchainIdPersonnel() {
        return calculerProchainId(new PersonnelDAOImpl().trouverToutLePersonnel(), personnel -> personnel.getId());
    }

    public static int obtenirProchainIdProduitSnack() {
        return calculerProchainId(new ProduitSnackDAOImpl().trouverTousLesProduits(), produit -> produit.getId());
    }

    public static int obtenirProchainIdSalle() {
        return calculerProchainId(new SalleDAOImpl().trouverToutesLesSalles(), salle -> salle.getId());
    }

    public static int obtenirProchainIdSeance() {
        return calculerProchainId(new SeanceDAOImpl().trouverToutesLesSeances(), seance -> seance.getId());
    }

    public static int obtenirProchainIdSiege() {
        return calculerProchainId(new SiegeDAOImpl().trouverTousLesSieges(), siege -> siege.getId());
    }

    public static int obtenirProchainIdVenteSnack() {
        return calculerProchainId(new VenteSnackDAOImpl().trouverToutesLesVentesSnack(), vente -> vente.getIdVente());
    }

    public static int obtenirProchainIdCaisse() {
        return calculerProchainId(new CaisseDAOImpl().trouverToutesLesCaisses(), caisse -> caisse.getId());
    }

    public static int obtenirProchainIdGenre() {
        return calculerProchainId(new GenreDAOImpl().trouverTousLesGenres(), genre -> genre.getId());
    }

    public static int obtenirProchainIdRole() {
        return calculerProchainId(new RoleDAOImpl().trouverTousLesRoles(), role -> role.getId());
    }

    public static int obtenirProchainIdTarif() {
        return calculerProchainId(new TarifDAOImpl().trouverTousLesTarifs(), tarif -> tarif.getId());
    }

    public static int obtenirProchainIdPlanning() {
        return calculerProchainId(new PlanningDAOImpl().trouverTousLesPlannings(), planning -> planning.getId());
    }

    /**
     * @param <T> Le type d'objet (Film, Client, etc.).
     * @param liste La liste complète des objets à analyser.
     * @param extracteur L'outil qui sait comment récupérer l'ID de l'objet.
     * @return L'ID le plus élevé trouvé, incrémenté de 1.
     */
    private static <T> int calculerProchainId(List<T> liste, ExtractId<T> extracteur) {
        int idMax = 0;
        for (T element : liste) {
            int idActuel = extracteur.extraireId(element);
            if (idActuel > idMax) {
                idMax = idActuel;
            }
        }
        return idMax + 1;
    }
}