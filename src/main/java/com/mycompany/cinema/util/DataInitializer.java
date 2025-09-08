package com.mycompany.cinema.util;

// Importe toutes les classes du modèle (Film, Client, etc.) pour pouvoir créer des objets.
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
 * ATTENTION : Classe utilitaire pour créer le tout premier jeu de données.
 * Axelle : Tu n'auras jamais besoin de toucher ou d'appeler ce code. Il est
 * exécuté une seule fois pour générer les fichiers "data/nom_fichier.dat" que
 * le reste de l'application utilise comme une base de données. C'est la source
 * de toutes les informations que tu afficheras au démarrage.
 */
public final class DataInitializer {

    /**
     * Le constructeur est privé pour empêcher quiconque de créer une instance
     * de cette classe. On ne l'utilise qu'à travers sa méthode statique
     * 'seed()'.
     */
    private DataInitializer() {
    }

    /**
     * La méthode principale qui lance la création de toutes les données de
     * test. Pense à elle comme un grand script qui remplit la base de données
     * de zéro.
     */
    public static void seed() {
        System.out.println("Début de l'initialisation du jeu de données...");

        // --- PHASE 1: Création des données de base ---
        // On crée d'abord les objets simples qui ne dépendent de rien d'autre.
        // Par exemple, la liste des "Rôles" (Admin, Vendeur) pour les employés.
        List<Role> roles = createRoles();
        // La liste des "Tarifs" (Plein, Étudiant) que le client pourra choisir.
        List<Tarif> tarifs = createTarifs();
        // La liste des "Genres" (Action, Drame) qui seront affichés sur la fiche d'un film.
        List<Genre> genres = createGenres();
        // La liste des "Salles" du cinéma avec leur capacité.
        List<Salle> salles = createSalles();
        // La liste des "Produits" (Popcorn, Soda) que le client pourra acheter.
        List<ProduitSnack> produitsSnack = createProduitsSnack();
        // Les "Caisses" où les ventes de snacks sont enregistrées.
        List<Caisse> caisses = createCaisses();

        // --- PHASE 2: Création d'objets qui dépendent de la phase 1 ---
        // On crée les sièges pour chaque salle créée juste avant.
        List<Siege> sieges = createSieges(salles);
        // La liste des "Films" à l'affiche.
        List<Film> films = createFilms();
        // Quelques "Clients" pour les tests.
        List<Client> clients = createClients();
        // Quelques "Employés" avec leurs rôles.
        List<Personnel> personnel = createPersonnel(roles);

        // --- PHASE 3: On relie les objets entre eux ---
        // On associe des genres à chaque film (ex: "Dune" est "SF" et "Aventure").
        linkFilmsToGenres(films, genres);
        // On crée les "Séances" : on dit quel film joue, dans quelle salle, et à quelle heure.
        List<Seance> seances = createSeances(films, salles);
        // On affecte un employé à une séance.
        List<AffectationSeance> affectations = createAffectations(personnel, seances);
        // On crée un "Planning" de travail pour un employé.
        List<Planning> plannings = createPlannings(personnel);
        // On crée des "Évaluations" : un client donne une note et un avis sur un film.
        List<EvaluationClient> evaluations = createEvaluations(clients, films);

        // --- PHASE 4: Simulation de vraies actions utilisateur ---
        List<Reservation> reservations = new ArrayList<>();
        List<Billet> billets = new ArrayList<>();
        // On simule un client qui fait une réservation pour plusieurs sièges.
        createScenarioReservation(clients, seances, sieges, tarifs, reservations, billets);

        List<VenteSnack> ventesSnack = new ArrayList<>();
        List<Comporte> lignesVente = new ArrayList<>();
        // On simule une vente de snacks au comptoir.
        createScenarioVenteSnack(personnel, produitsSnack, caisses, clients, ventesSnack, lignesVente);

        // --- PHASE 5: Sauvegarde de tout dans des fichiers ---
        // Une fois tous les objets créés en mémoire, on les sauvegarde un par un dans des fichiers.
        // C'est ce qui assure que les données persistent même si on ferme l'application.
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

    // --- Ci-dessous, les méthodes de création de chaque type d'objet. ---
    // Mon binôme : Le détail n'est pas important pour toi, c'est juste le "remplissage"
    // de la base de données.
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
        items.add(new Genre(7, "Fantastique"));
        return items;
    }

    private static List<Caisse> createCaisses() {
        List<Caisse> items = new ArrayList<>();
        // AJOUT : Caisse virtuelle pour les ventes en ligne. ID 0.
        items.add(new Caisse(0, "Canal Web", "Vente en ligne"));
        items.add(new Caisse(1, "Comptoir Principal", "Hall d'entrée"));
        items.add(new Caisse(2, "Borne Automatique 1", "Hall d'entrée - Gauche"));
        return items;
    }

    private static List<Salle> createSalles() {
        List<Salle> items = new ArrayList<>();
        items.add(new Salle(1, "Salle 1", 100));
        items.add(new Salle(2, "Salle 2", 150));
        items.add(new Salle(3, "Salle IMAX 3D", 240));
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
        items.add(new Film(
                4, // ID suivant disponible
                "Le Seigneur des Anneaux : Le Retour du Roi",
                "Les armées de Sauron ont assiégé Minas Tirith, la capitale de Gondor...",
                201,
                "Tous publics",
                "seigneur_anneaux.jpg",
                5.0
        ));
        items.add(new Film(
                5, // ID suivant disponible
                "PISE : Le Film",
                "Le parcours initiatique d'un soldat du code face à une doctrine implacable.",
                240,
                "Interdit aux -18 ans", // Classification non apte à tous les publics
                "christophe.jpg",
                4.2
        ));
        return items;
    }

    private static void linkFilmsToGenres(List<Film> films, List<Genre> genres) {
        films.get(0).getGenres().add(genres.get(0));
        films.get(0).getGenres().add(genres.get(1));
        films.get(1).getGenres().add(genres.get(2));
        films.get(1).getGenres().add(genres.get(3));
        films.get(2).getGenres().add(genres.get(5));
        films.get(2).getGenres().add(genres.get(4));
        // --- LIAISON DES NOUVEAUX FILMS ---
        // Le Retour du Roi (index 3 dans la liste 'films')
        films.get(3).getGenres().add(genres.get(1)); // -> Aventure (index 1 dans 'genres')
        films.get(3).getGenres().add(genres.get(6)); // -> Fantastique (index 6 dans 'genres')

        // PISE : Le Film (index 4 dans la liste 'films')
        films.get(4).getGenres().add(genres.get(2)); // -> Drame (index 2 dans 'genres')
    }

    private static List<Client> createClients() {
        List<Client> items = new ArrayList<>();
        items.add(new Client(1, "Alice Martin", "alice.m@email.com", "pass123", LocalDate.of(2023, 5, 12)));
        items.add(new Client(2, "Bob Durand", "bob.d@email.com", "azerty", LocalDate.of(2024, 1, 20)));
        return items;
    }

    private static List<Personnel> createPersonnel(List<Role> roles) {
        List<Personnel> items = new ArrayList<>();
        // On remplace le nom d'utilisateur par un email
        items.add(new Personnel(0, "Système", "En Ligne", "system@cinema.local", "system_pass", roles.get(0).getId()));
        items.add(new Personnel(1, "Dupont", "Jean", "admin@pisecinema.com", "admin", roles.get(0).getId()));
        items.add(new Personnel(2, "Garcia", "Maria", "maria@pisecinema.com", "proj", roles.get(1).getId()));
        items.add(new Personnel(3, "Smith", "John", "vendeur@pisecinema.com", "vendeur", roles.get(2).getId()));
        return items;
    }

    // Dans le fichier DataInitializer.java
    private static List<Planning> createPlannings(List<Personnel> personnel) {
        List<Planning> items = new ArrayList<>();

        // On récupère la date et l'heure actuelles comme point de référence
        LocalDateTime maintenant = LocalDateTime.now();

        // --- Planning pour Jean Dupont (Admin, ID 1) ---
        // Il n'a pas de planning fixe car il est admin, on peut laisser vide ou ajouter
        // un créneau de supervision pour l'exemple.
        // Pas de planning ajouté pour lui pour l'instant.
        // --- Planning pour Maria Garcia (Projectionniste, ID 2) ---
        // On lui assigne des créneaux de projection pour les séances du soir
        items.add(new Planning(
                IdManager.getNextPlanningId(),
                maintenant.withHour(17).withMinute(0).withSecond(0), // Aujourd'hui à 17h00
                maintenant.withHour(23).withMinute(30).withSecond(0), // Aujourd'hui à 23h30
                "Projection Salle 1 & 2",
                personnel.get(1).getId() // ID de Maria
        ));
        items.add(new Planning(
                IdManager.getNextPlanningId(),
                maintenant.plusDays(1).withHour(17).withMinute(0).withSecond(0), // Demain à 17h00
                maintenant.plusDays(1).withHour(23).withMinute(30).withSecond(0), // Demain à 23h30
                "Projection Salle 3 (IMAX)",
                personnel.get(1).getId()
        ));

        // --- Planning pour John Smith (Vendeur, ID 3) ---
        // On lui assigne des créneaux de vente au comptoir
        items.add(new Planning(
                IdManager.getNextPlanningId(),
                maintenant.withHour(18).withMinute(0).withSecond(0), // Aujourd'hui à 18h00
                maintenant.withHour(22).withMinute(0).withSecond(0), // Aujourd'hui à 22h00
                "Vente Snacking",
                personnel.get(2).getId() // ID de John
        ));
        items.add(new Planning(
                IdManager.getNextPlanningId(),
                maintenant.plusDays(2).withHour(14).withMinute(0).withSecond(0), // Après-demain à 14h00
                maintenant.plusDays(2).withHour(19).withMinute(0).withSecond(0), // Après-demain à 19h00
                "Accueil & Billetterie",
                personnel.get(2).getId()
        ));

        return items;
    }

    private static List<Seance> createSeances(List<Film> films, List<Salle> salles) {
        List<Seance> items = new ArrayList<>();

        // Logique dynamique : on calcule les dates par rapport à AUJOURD'HUI.
        LocalDate aujourdhui = LocalDate.now();
        LocalDate demain = aujourdhui.plusDays(1);
        LocalDate apresDemain = aujourdhui.plusDays(2);

        int seanceIdCounter = 1;

        // --- Séances pour AUJOURD'HUI (uniquement si elles n'ont pas encore eu lieu) ---
        if (LocalTime.now().isBefore(LocalTime.of(14, 0))) {
            items.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(14, 0)), salles.get(1).getId(), films.get(1).getId())); // Oppenheimer
        }
        if (LocalTime.now().isBefore(LocalTime.of(17, 30))) {
            items.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(17, 30)), salles.get(2).getId(), films.get(2).getId())); // Spider-Man
        }
        if (LocalTime.now().isBefore(LocalTime.of(20, 15))) {
            items.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(20, 15)), salles.get(0).getId(), films.get(0).getId())); // Dune
        }
        if (LocalTime.now().isBefore(LocalTime.of(21, 0))) {
            items.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(21, 0)), salles.get(1).getId(), films.get(1).getId())); // Oppenheimer
        }

        // --- Séances pour DEMAIN ---
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(demain, LocalTime.of(14, 0)), salles.get(0).getId(), films.get(0).getId())); // Dune
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(demain, LocalTime.of(17, 0)), salles.get(1).getId(), films.get(2).getId())); // Spider-Man
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(demain, LocalTime.of(20, 30)), salles.get(2).getId(), films.get(1).getId())); // Oppenheimer

        // --- Séances pour APRÈS-DEMAIN ---
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(apresDemain, LocalTime.of(17, 30)), salles.get(0).getId(), films.get(2).getId())); // Spider-Man
        items.add(new Seance(seanceIdCounter++, LocalDateTime.of(apresDemain, LocalTime.of(21, 0)), salles.get(2).getId(), films.get(0).getId())); // Dune

        return items;
    }

    private static List<AffectationSeance> createAffectations(List<Personnel> personnel, List<Seance> seances) {
        List<AffectationSeance> items = new ArrayList<>();
        if (seances.size() >= 3) {
            items.add(new AffectationSeance(seances.get(2).getId(), personnel.get(1).getId()));
        }
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
        Reservation res = new Reservation(1, LocalDateTime.now().minusDays(1), clients.get(0).getId());
        reservations.add(res);

        Siege siege1 = sieges.get(57);
        Siege siege2 = sieges.get(58);
        billets.add(new Billet(1, res.getId(), tarifs.get(0).getId(), siege1.getId(), seances.get(0).getId()));
        billets.add(new Billet(2, res.getId(), tarifs.get(0).getId(), siege2.getId(), seances.get(0).getId()));
    }

    private static void createScenarioVenteSnack(List<Personnel> personnel, List<ProduitSnack> produits, List<Caisse> caisses, List<Client> clients, List<VenteSnack> ventes, List<Comporte> lignes) {
        VenteSnack vente = new VenteSnack(1, LocalDateTime.now().minusHours(2), personnel.get(2).getId(), caisses.get(0).getId(), null);
        ventes.add(vente);

        lignes.add(new Comporte(vente.getIdVente(), produits.get(0).getId(), 1, produits.get(0).getPrixVente()));
        lignes.add(new Comporte(vente.getIdVente(), produits.get(1).getId(), 1, produits.get(1).getPrixVente()));
    }

    /**
     * Méthode générique qui prend n'importe quelle liste d'objets et la
     * sauvegarde dans un fichier binaire. C'est le cœur de notre système de
     * persistance simple.
     *
     * @param filename Le nom du fichier (ex: "films.dat").
     * @param list La liste d'objets à sauvegarder.
     * @param <T> Le type des objets dans la liste.
     */
    private static <T> void saveList(String filename, List<T> list) {
        // Crée le dossier "data" s'il n'existe pas.
        new File("data").mkdirs();
        // Utilise un "try-with-resources" pour s'assurer que le fichier est bien fermé après l'écriture.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/" + filename))) {
            // La commande magique : écrit toute la liste d'un coup dans le fichier.
            oos.writeObject(list);
        } catch (IOException e) {
            // Si une erreur se produit (disque plein, pas les droits...), on affiche un message clair.
            System.err.println("ERREUR fatale lors de la sauvegarde du fichier " + filename);
            e.printStackTrace();
        }
    }
}
