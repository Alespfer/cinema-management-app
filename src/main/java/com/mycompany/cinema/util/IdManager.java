package com.mycompany.cinema.util;

import com.mycompany.cinema.*;
import com.mycompany.cinema.dao.impl.*;
import java.util.List;

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
        return calculateNextId(new FilmDAOImpl().getAllFilms(), new IdExtractor<Film>() {
            public int extractId(Film film) {
                return film.getId();
            }
        });
    }

    public static int getNextClientId() {
        return calculateNextId(new ClientDAOImpl().getAllClients(), new IdExtractor<Client>() {
            public int extractId(Client client) {
                return client.getId();
            }
        });
    }

    public static int getNextBilletId() {
        return calculateNextId(new BilletDAOImpl().getAllBillets(), new IdExtractor<Billet>() {
            public int extractId(Billet billet) {
                return billet.getId();
            }
        });
    }

    public static int getNextReservationId() {
        return calculateNextId(new ReservationDAOImpl().getAllReservations(), new IdExtractor<Reservation>() {
            public int extractId(Reservation reservation) {
                return reservation.getId();
            }
        });
    }

    public static int getNextPersonnelId() {
        return calculateNextId(new PersonnelDAOImpl().getAllPersonnel(), new IdExtractor<Personnel>() {
            public int extractId(Personnel personnel) {
                return personnel.getId();
            }
        });
    }

    public static int getNextProduitSnackId() {
        return calculateNextId(new ProduitSnackDAOImpl().getAllProduits(), new IdExtractor<ProduitSnack>() {
            public int extractId(ProduitSnack produit) {
                return produit.getId();
            }
        });
    }

    public static int getNextSalleId() {
        return calculateNextId(new SalleDAOImpl().getAllSalles(), new IdExtractor<Salle>() {
            public int extractId(Salle salle) {
                return salle.getId();
            }
        });
    }

    public static int getNextSeanceId() {
        return calculateNextId(new SeanceDAOImpl().getAllSeances(), new IdExtractor<Seance>() {
            public int extractId(Seance seance) {
                return seance.getId();
            }
        });
    }

    public static int getNextSiegeId() {
        return calculateNextId(new SiegeDAOImpl().getAllSieges(), new IdExtractor<Siege>() {
            public int extractId(Siege siege) {
                return siege.getId();
            }
        });
    }

    public static int getNextVenteSnackId() {
        return calculateNextId(new VenteSnackDAOImpl().getAllVentesSnack(), new IdExtractor<VenteSnack>() {
            public int extractId(VenteSnack vente) {
                return vente.getIdVente();
            }
        });
    }

    public static int getNextCaisseId() {
        return calculateNextId(new CaisseDAOImpl().getAllCaisses(), new IdExtractor<Caisse>() {
            public int extractId(Caisse caisse) {
                return caisse.getId();
            }
        });
    }

    public static int getNextGenreId() {
        return calculateNextId(new GenreDAOImpl().getAllGenres(), new IdExtractor<Genre>() {
            public int extractId(Genre genre) {
                return genre.getId();
            }
        });
    }

    public static int getNextRoleId() {
        return calculateNextId(new RoleDAOImpl().getAllRoles(), new IdExtractor<Role>() {
            public int extractId(Role role) {
                return role.getId();
            }
        });
    }

    public static int getNextTarifId() {
        return calculateNextId(new TarifDAOImpl().getAllTarifs(), new IdExtractor<Tarif>() {
            public int extractId(Tarif tarif) {
                return tarif.getId();
            }
        });
    }

    public static int getNextPlanningId() {
        return calculateNextId(new PlanningDAOImpl().getAllPlannings(), new IdExtractor<Planning>() {
            public int extractId(Planning planning) {
                return planning.getId();
            }
        });
    }


    /**
     * Méthode générique pour déterminer le prochain ID à partir d'une liste d'objets et d'un extracteur d'ID.
     */
    private static <T> int calculateNextId(List<T> list, IdExtractor<T> extractor) {
        int maxId = 0;
        for (T element : list) {
            int currentId = extractor.extractId(element);
            if (currentId > maxId) {
                maxId = currentId;
            }
        }
        return maxId + 1;
    }
}
