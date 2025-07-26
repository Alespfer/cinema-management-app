// Fichier : src/main/java/com/mycompany/cinema/util/IdManager.java
package com.mycompany.cinema.util;

import com.mycompany.cinema.*;
import com.mycompany.cinema.dao.*;
import com.mycompany.cinema.dao.impl.*;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Classe utilitaire finale pour la gestion centralisée des identifiants uniques.
 * EMPÊCHE L'INSTANCIATION. UTILISATION UNIQUEMENT STATIQUE.
 * Calcule le prochain ID disponible en trouvant le maximum actuel et en ajoutant 1.
 * C'est la seule source de vérité pour les nouvelles clés primaires.
 */
public final class IdManager {

    private IdManager() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne peut pas être instanciée.");
    }

    // --- Gestionnaires d'ID pour chaque entité ---

    public static int getNextFilmId() {
        return calculateNextId(new FilmDAOImpl().getAllFilms(), Film::getId);
    }
    public static int getNextClientId() {
        return calculateNextId(new ClientDAOImpl().getAllClients(), Client::getId);
    }
    public static int getNextBilletId() {
        return calculateNextId(new BilletDAOImpl().getAllBillets(), Billet::getId);
    }
    public static int getNextReservationId() {
        return calculateNextId(new ReservationDAOImpl().getAllReservations(), Reservation::getId);
    }
    public static int getNextPersonnelId() {
        return calculateNextId(new PersonnelDAOImpl().getAllPersonnel(), Personnel::getId);
    }
    public static int getNextProduitSnackId() {
        return calculateNextId(new ProduitSnackDAOImpl().getAllProduits(), ProduitSnack::getId);
    }
    public static int getNextSalleId() {
        return calculateNextId(new SalleDAOImpl().getAllSalles(), Salle::getId);
    }
    public static int getNextSeanceId() {
        return calculateNextId(new SeanceDAOImpl().getAllSeances(), Seance::getId);
    }
    public static int getNextSiegeId() {
        return calculateNextId(new SiegeDAOImpl().getAllSieges(), Siege::getId);
    }
    public static int getNextVenteSnackId() {
        return calculateNextId(new VenteSnackDAOImpl().getAllVentesSnack(), VenteSnack::getIdVente);
    }
    public static int getNextCaisseId() {
        return calculateNextId(new CaisseDAOImpl().getAllCaisses(), Caisse::getId);
    }
    public static int getNextGenreId() {
        return calculateNextId(new GenreDAOImpl().getAllGenres(), Genre::getId);
    }
    public static int getNextRoleId() {
        return calculateNextId(new RoleDAOImpl().getAllRoles(), Role::getId);
    }
    public static int getNextTarifId() {
        return calculateNextId(new TarifDAOImpl().getAllTarifs(), Tarif::getId);
    }
    public static int getNextPlanningId() {
        return calculateNextId(new PlanningDAOImpl().getAllPlannings(), Planning::getId);
    }

    /**
     * Méthode générique qui calcule le prochain ID pour n'importe quelle liste d'objets.
     * @param list La liste des objets à inspecter.
     * @param mapper Une fonction qui extrait l'entier ID de l'objet.
     * @return Le prochain ID disponible.
     */
    private static <T> int calculateNextId(List<T> list, ToIntFunction<T> mapper) {
        int maxId = list.stream().mapToInt(mapper).max().orElse(0);
        return maxId + 1;
    }
}