package com.mycompany.cinema.util;

import com.mycompany.cinema.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire responsable de la création et de la persistance
 * du jeu de données initial pour l'application.
 * NE DOIT ÊTRE APPELÉE QU'UNE SEULE FOIS, AU PREMIER DÉMARRAGE.
 */
public final class DataInitializer {

    private DataInitializer() {}

    /**
     * Méthode maîtresse qui orchestre la création de toutes les données de test.
     */
    public static void seed() {
        System.out.println("Début de l'initialisation du jeu de données...");

        // --- PHASE 1: Création des entités de référence (sans dépendances) ---
        List<Role> roles = createRoles();
        List<Tarif> tarifs = createTarifs();
        List<Genre> genres = createGenres();
        List<Salle> salles = createSalles();
        List<ProduitSnack> produitsSnack = createProduitsSnack();
        List<Caisse> caisses = createCaisses();
        
        // --- PHASE 2: Création des entités avec dépendances de premier niveau ---
        List<Siege> sieges = createSieges(salles);
        List<Film> films = createFilms();
        List<Client> clients = createClients();
        List<Personnel> personnel = createPersonnel(roles);

        // --- PHASE 3: Création des liens et des entités complexes ---
        linkFilmsToGenres(films, genres);
        List<Seance> seances = createSeances(films, salles); // MÉTHODE CORRIGÉE POUR LA VISIBILITÉ
        List<AffectationSeance> affectations = createAffectations(personnel, seances);
        List<Planning> plannings = createPlannings(personnel);
        List<EvaluationClient> evaluations = createEvaluations(clients, films);

        // --- PHASE 4: Simulation de scénarios transactionnels ---
        List<Reservation> reservations = new ArrayList<>();
        List<Billet> billets = new ArrayList<>();
        createScenarioReservation(clients, seances, sieges, tarifs, reservations, billets);

        List<VenteSnack> ventesSnack = new ArrayList<>();
        List<Comporte> lignesVente = new ArrayList<>();
        createScenarioVenteSnack(personnel, produitsSnack, caisses, clients, ventesSnack, lignesVente);
        
        // --- PHASE 5: Persistance de toutes les données dans les fichiers .dat ---
        System.out.println("Sauvegarde des données dans les fichiers...");
        saveList("roles.dat", roles);
        saveList("tarifs.dat", tarifs);
        saveList("genres.dat", genres);
        saveList("salles.dat", salles);
        saveList("produits_snack.dat", produitsSnack);
        saveList("caisses.dat", caisses);
        saveList("sieges.dat", sieges);
        saveList("films.dat", films);
        saveList("clients.dat", clients);
        saveList("personnel.dat", personnel);
        saveList("seances.dat", seances);
        saveList("affectations_seance.dat", affectations);
        saveList("plannings.dat", plannings);
        saveList("reservations.dat", reservations);
        saveList("billets.dat", billets);
        saveList("ventes_snack.dat", ventesSnack);
        saveList("lignes_ventes.dat", lignesVente);
        saveList("evaluations_client.dat", evaluations);

        System.out.println("Initialisation du jeu de données terminée avec succès.");
    }

    // --- Méthodes de création détaillées ---

    private static List<Role> createRoles() {
        List<Role> items = new ArrayList<>();
        items.add(new Role(1, "Administrateur"));
        items.add(new Role(2, "Projectionniste"));
        items.add(new Role(3, "Vendeur"));
        return items;
    }

    private static List<Tarif> createTarifs() {
        List<Tarif> items = new ArrayList<>();
        items.add(new Tarif(1, "Plein Tarif", 9.20));
        items.add(new Tarif(2, "Tarif Étudiant", 7.60));
        items.add(new Tarif(3, "Tarif -14 ans", 5.90));
        return items;
    }

    private static List<Genre> createGenres() {
        List<Genre> items = new ArrayList<>();
        items.add(new Genre(1, "Science-Fiction"));
        items.add(new Genre(2, "Aventure"));
        items.add(new Genre(3, "Drame"));
        items.add(new Genre(4, "Historique"));
        items.add(new Genre(5, "Action"));
        items.add(new Genre(6, "Animation"));
        return items;
    }
    
    private static List<Caisse> createCaisses() {
        List<Caisse> items = new ArrayList<>();
        items.add(new Caisse(1, "Comptoir Principal", "Hall d'entrée"));
        items.add(new Caisse(2, "Borne Automatique 1", "Hall d'entrée - Gauche"));
        return items;
    }

    private static List<Salle> createSalles() {
        List<Salle> items = new ArrayList<>();
        items.add(new Salle(1, "Salle 1", 100)); // 10 rangées de 10
        items.add(new Salle(2, "Salle 2", 150)); // 10 rangées de 15
        items.add(new Salle(3, "Salle IMAX 3D", 240)); // 12 rangées de 20
        return items;
    }

    private static List<Siege> createSieges(List<Salle> salles) {
        List<Siege> items = new ArrayList<>();
        int siegeIdCounter = 1;
        for (Salle salle : salles) {
            int seatsPerRow = (salle.getId() == 3) ? 20 : (salle.getId() == 2 ? 15 : 10);
            int rows = salle.getCapacite() / seatsPerRow;
            for (int r = 1; r <= rows; r++) {
                for (int s = 1; s <= seatsPerRow; s++) {
                    items.add(new Siege(siegeIdCounter++, r, s, salle.getId()));
                }
            }
        }
        return items;
    }

    private static List<Film> createFilms() {
        List<Film> items = new ArrayList<>();
        items.add(new Film(1, "Dune: Part Two", "Paul Atreides s'unit à Chani et aux Fremen...", 166, "Tous publics", "dune.jpg", 4.8));
        items.add(new Film(2, "Oppenheimer", "Le portrait du physicien J. Robert Oppenheimer...", 180, "Tous publics avec avertissement", "oppenheimer.jpg", 4.5));
        items.add(new Film(3, "Spider-Man: Across the Spider-Verse", "Miles Morales est catapulté à travers le Multivers...", 140, "Tous publics", "spiderman.jpg", 4.9));
        return items;
    }

    private static void linkFilmsToGenres(List<Film> films, List<Genre> genres) {
        films.get(0).getGenres().add(genres.get(0));
        films.get(0).getGenres().add(genres.get(1));
        films.get(1).getGenres().add(genres.get(2));
        films.get(1).getGenres().add(genres.get(3));
        films.get(2).getGenres().add(genres.get(5));
        films.get(2).getGenres().add(genres.get(4));
    }

    private static List<Client> createClients() {
        List<Client> items = new ArrayList<>();
        items.add(new Client(1, "Alice Martin", "alice.m@email.com", "pass123", LocalDate.of(2023, 5, 12)));
        items.add(new Client(2, "Bob Durand", "bob.d@email.com", "azerty", LocalDate.of(2024, 1, 20)));
        return items;
    }

    private static List<Personnel> createPersonnel(List<Role> roles) {
        List<Personnel> items = new ArrayList<>();
        items.add(new Personnel(1, "Dupont", "Jean", "admin", roles.get(0).getId()));
        items.add(new Personnel(2, "Garcia", "Maria", "proj", roles.get(1).getId()));
        items.add(new Personnel(3, "Smith", "John", "vendeur", roles.get(2).getId()));
        return items;
    }

    /**
     * CORRIGÉ : Crée un jeu de séances de test cohérent et toujours visible pour l'utilisateur.
     * Génère des séances pour hier, aujourd'hui (à des heures futures), demain et après-demain.
     */
    private static List<Seance> createSeances(List<Film> films, List<Salle> salles) {
        List<Seance> items = new ArrayList<>();
        LocalDate aujourdhui = LocalDate.now();
        LocalDate hier = aujourdhui.minusDays(1);
        LocalDate demain = aujourdhui.plusDays(1);
        LocalDate apresDemain = aujourdhui.plusDays(2);

        int seanceIdCounter = 1;

        // --- Séance d'hier (pour l'historique des réservations) ---
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(hier, LocalTime.of(20, 0)), salles.get(0).getId(), films.get(0).getId()));

        // --- Séances d'aujourd'hui (visibles si l'heure actuelle est antérieure) ---
        if (LocalTime.now().isBefore(LocalTime.of(14, 0))) {
            items.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(14, 0)), salles.get(1).getId(), films.get(1).getId()));
        }
        if (LocalTime.now().isBefore(LocalTime.of(17, 30))) {
            items.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(17, 30)), salles.get(2).getId(), films.get(2).getId()));
        }
        if (LocalTime.now().isBefore(LocalTime.of(20, 15))) {
            items.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(20, 15)), salles.get(0).getId(), films.get(0).getId()));
        }
        if (LocalTime.now().isBefore(LocalTime.of(21, 0))) {
            items.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(21, 0)), salles.get(1).getId(), films.get(1).getId()));
        }

        // --- Séances de demain ---
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(demain, LocalTime.of(14, 0)), salles.get(0).getId(), films.get(0).getId()));
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(demain, LocalTime.of(17, 0)), salles.get(1).getId(), films.get(2).getId()));
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(demain, LocalTime.of(20, 30)), salles.get(2).getId(), films.get(1).getId()));

        // --- Séances d'après-demain ---
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(apresDemain, LocalTime.of(17, 30)), salles.get(0).getId(), films.get(2).getId()));
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(apresDemain, LocalTime.of(21, 0)), salles.get(2).getId(), films.get(0).getId()));

        return items;
    }

    private static List<AffectationSeance> createAffectations(List<Personnel> personnel, List<Seance> seances) {
        List<AffectationSeance> items = new ArrayList<>();
        // Affecte un projectionniste à la 3ème séance créée (qui est généralement celle d'aujourd'hui ou de demain)
        if (seances.size() >= 3) {
            items.add(new AffectationSeance(seances.get(2).getId(), personnel.get(1).getId()));
        }
        return items;
    }

    private static List<Planning> createPlannings(List<Personnel> personnel) {
        List<Planning> items = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        items.add(new Planning(1, now.withHour(18), now.withHour(23), "Vente Snacking", personnel.get(2).getId()));
        return items;
    }
    
    private static List<ProduitSnack> createProduitsSnack() {
        List<ProduitSnack> items = new ArrayList<>();
        items.add(new ProduitSnack(1, "Popcorn Salé Grand", "Maïs éclaté salé, 250g", 6.50, 100));
        items.add(new ProduitSnack(2, "Soda 50cl", "Boisson gazeuse sucrée", 3.50, 200));
        items.add(new ProduitSnack(3, "M&M's 200g", "Cacahuètes enrobées de chocolat", 4.00, 150));
        items.add(new ProduitSnack(4, "Popcorn Sucré Moyen", "Maïs éclaté sucré, 150g", 5.50, 80));
        items.add(new ProduitSnack(5, "Bouteille d'eau 50cl", "Eau de source plate", 2.00, 300));
        items.add(new ProduitSnack(6, "Nachos & Fromage", "Tortilla chips avec sauce fromage", 7.00, 50));
        return items;
    }
    
    private static List<EvaluationClient> createEvaluations(List<Client> clients, List<Film> films) {
        List<EvaluationClient> items = new ArrayList<>();
        items.add(new EvaluationClient(clients.get(0).getId(), films.get(0).getId(), 5, "Visuellement incroyable, une pure merveille !", LocalDateTime.now().minusDays(1)));
        items.add(new EvaluationClient(clients.get(1).getId(), films.get(0).getId(), 4, "Très bon film, un peu long par moments.", LocalDateTime.now().minusHours(5)));
        items.add(new EvaluationClient(clients.get(1).getId(), films.get(1).getId(), 5, "Un chef d'oeuvre. La performance de l'acteur est magistrale.", LocalDateTime.now().minusDays(3)));
        return items;
    }

    private static void createScenarioReservation(List<Client> clients, List<Seance> seances, List<Siege> sieges, List<Tarif> tarifs, List<Reservation> reservations, List<Billet> billets) {
        // Crée une réservation pour la première séance de la liste (qui est celle d'hier).
        Reservation res = new Reservation(1, LocalDateTime.now().minusDays(1), clients.get(0).getId());
        reservations.add(res);

        Siege siege1 = sieges.get(57); // Salle 1, rangée 6, siège 8
        Siege siege2 = sieges.get(58); // Salle 1, rangée 6, siège 9
        billets.add(new Billet(1, res.getId(), tarifs.get(0).getId(), siege1.getId(), seances.get(0).getId()));
        billets.add(new Billet(2, res.getId(), tarifs.get(0).getId(), siege2.getId(), seances.get(0).getId()));
    }

    private static void createScenarioVenteSnack(List<Personnel> personnel, List<ProduitSnack> produits, List<Caisse> caisses, List<Client> clients, List<VenteSnack> ventes, List<Comporte> lignes) {
        // Vente au comptoir par John Smith. idReservation et idClient sont null.
        VenteSnack vente = new VenteSnack(1, LocalDateTime.now().minusHours(2), personnel.get(2).getId(), caisses.get(0).getId(), null);
        ventes.add(vente);
        
        lignes.add(new Comporte(vente.getIdVente(), produits.get(0).getId(), 1, produits.get(0).getPrixVente()));
        lignes.add(new Comporte(vente.getIdVente(), produits.get(1).getId(), 1, produits.get(1).getPrixVente()));
    }

    private static <T> void saveList(String filename, List<T> list) {
        new File("data").mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/" + filename))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.err.println("ERREUR fatale lors de la sauvegarde du fichier " + filename);
            e.printStackTrace();
        }
    }
}