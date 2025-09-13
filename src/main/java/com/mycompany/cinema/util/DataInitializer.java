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
 * Classe utilitaire pour créer le tout premier jeu de données.
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
    public static void initialiser() {
        System.out.println("Début de l'initialisation du jeu de données...");

        // --- PHASE 1: Création des données de base ---
        // On crée d'abord les objets simples qui ne dépendent de rien d'autre.
        // La liste des "Rôles" (Admin, Vendeur) pour les employés.
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

        // --- PHASE 2: Création des objets qui utilisent les briques de la phase 1 ---
        // Les sièges pour chaque salle créée juste avant.
        List<Siege> sieges = createSieges(salles);
        // La liste des "Films" à l'affiche.
        List<Film> films = createFilms();
        // Quelques "Clients" pour les tests.
        List<Client> clients = createClients();
        // Quelques "Employés" avec leurs rôles.
        List<Personnel> personnel = createPersonnel(roles);

        // --- PHASE 3: On relie les objets entre eux ---
        // On associe des genres à chaque film (ex: "Dune" est "SF" et "Aventure").
        lierFilmsAuxGenres(films, genres);
        // On crée les "Séances" : on dit quel film joue, dans quelle salle, et à quelle heure.
        List<Seance> seances = createSeances(films, salles);
        // On affecte un employé à une séance.
        List<AffectationSeance> affectations = createAffectations(personnel, seances);
        // On crée un "Planning" de travail pour un employé.
        List<Planning> plannings = createPlannings(personnel);
        // "Évaluations" : un client donne une note et un avis sur un film.
        List<EvaluationClient> evaluations = createEvaluations(clients, films);

        // --- PHASE 4: Simulation de scénarios réels ---
        List<Reservation> reservations = new ArrayList<>();
        List<Billet> billets = new ArrayList<>();
        // On simule un client qui fait une réservation pour plusieurs sièges.
        createScenarioReservation(clients, seances, sieges, tarifs, reservations, billets);

        List<VenteSnack> ventesSnack = new ArrayList<>();
        List<LigneVente> lignesVente = new ArrayList<>();
        createScenarioVenteSnack(personnel, produitsSnack, caisses, clients, ventesSnack, lignesVente);

        // --- PHASE 5: Sauvegarde de tout dans des fichiers ---
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
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "Administrateur"));
        roles.add(new Role(2, "Projectionniste"));
        roles.add(new Role(3, "Vendeur"));
        return roles;
    }

    private static List<Tarif> createTarifs() {
        List<Tarif> tarifs = new ArrayList<>();
        tarifs.add(new Tarif(1, "Plein Tarif", 9.20));
        tarifs.add(new Tarif(2, "Tarif Étudiant", 7.60));
        tarifs.add(new Tarif(3, "Tarif -14 ans", 5.90));
        return tarifs;
    }

    private static List<Genre> createGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Science-Fiction"));
        genres.add(new Genre(2, "Aventure"));
        genres.add(new Genre(3, "Drame"));
        genres.add(new Genre(4, "Historique"));
        genres.add(new Genre(5, "Action"));
        genres.add(new Genre(6, "Animation"));
        genres.add(new Genre(7, "Fantastique"));
        return genres;
    }

    private static List<Caisse> createCaisses() {
        List<Caisse> caisses = new ArrayList<>();
        // Ajout d'une caisse "virtuelle" (ID 0) pour gérer les ventes en ligne.
        caisses.add(new Caisse(0, "Canal Web", "Vente en ligne"));
        caisses.add(new Caisse(1, "Comptoir Principal", "Hall d'entrée"));
        caisses.add(new Caisse(2, "Borne Automatique 1", "Hall d'entrée - Gauche"));
        return caisses;
    }

    private static List<Salle> createSalles() {
        List<Salle> salles = new ArrayList<>();
        salles.add(new Salle(1, "Salle 1", 100));
        salles.add(new Salle(2, "Salle 2", 150));
        salles.add(new Salle(3, "Salle IMAX 3D", 240));
        return salles;
    }

    private static List<Siege> createSieges(List<Salle> salles) {
        List<Siege> sieges = new ArrayList<>();
        int siegeIdCounter = 1;
        for (Salle salle : salles) {
            int seatsPerRow = (salle.getId() == 3) ? 20 : (salle.getId() == 2 ? 15 : 10);
            int rows = salle.getCapacite() / seatsPerRow;
            for (int r = 1; r <= rows; r++) {
                for (int s = 1; s <= seatsPerRow; s++) {
                    sieges.add(new Siege(siegeIdCounter++, r, s, salle.getId()));
                }
            }
        }
        return sieges;
    }

    private static List<Film> createFilms() {
        List<Film> films = new ArrayList<>();
        films.add(new Film(1, "Dune: Part Two", "Paul Atreides s'unit à Chani et aux Fremen...", 166, "Tous publics", "dune.jpg", 4.8));
        films.add(new Film(2, "Oppenheimer", "Le portrait du physicien J. Robert Oppenheimer...", 180, "Tous publics avec avertissement", "oppenheimer.jpg", 4.5));
        films.add(new Film(3, "Spider-Man: Across the Spider-Verse", "Miles Morales est catapulté à travers le Multivers...", 140, "Tous publics", "spiderman.jpg", 4.9));
        films.add(new Film(
                4,
                "Le Seigneur des Anneaux : Le Retour du Roi",
                "Les armées de Sauron ont assiégé Minas Tirith, la capitale de Gondor...",
                201,
                "Tous publics",
                "seigneur_anneaux.jpg",
                5.0
        ));
        films.add(new Film(
                5,
                "PISE : Le Film",
                "Le parcours initiatique d'un soldat du code face à une doctrine implacable.",
                240,
                "Interdit aux -18 ans",
                "christophe.jpg",
                4.2
        ));
        return films;
    }

    private static void lierFilmsAuxGenres(List<Film> films, List<Genre> genres) {
        films.get(0).getGenres().add(genres.get(0));
        films.get(0).getGenres().add(genres.get(1));
        films.get(1).getGenres().add(genres.get(2));
        films.get(1).getGenres().add(genres.get(3));
        films.get(2).getGenres().add(genres.get(5));
        films.get(2).getGenres().add(genres.get(4));
        films.get(3).getGenres().add(genres.get(1));
        films.get(3).getGenres().add(genres.get(6));
        films.get(4).getGenres().add(genres.get(2));
    }

    private static List<Client> createClients() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(1, "Alice Martin", "alice.m@email.com", "Alice123", LocalDate.of(2023, 5, 12)));
        clients.add(new Client(2, "Bob Durand", "bob.d@email.com", "Azerty123", LocalDate.of(2024, 1, 20)));
        return clients;
    }

    private static List<Personnel> createPersonnel(List<Role> roles) {
        List<Personnel> employes = new ArrayList<>();
        // On remplace le nom d'utilisateur par un email
        employes.add(new Personnel(0, "Système", "En Ligne", "system@cinema.local", "system_pass", roles.get(0).getId()));
        employes.add(new Personnel(1, "Dupont", "Jean", "admin@pisecinema.com", "Admin123", roles.get(0).getId()));
        employes.add(new Personnel(2, "Garcia", "Maria", "maria@pisecinema.com", "Project123", roles.get(1).getId()));
        employes.add(new Personnel(3, "Smith", "John", "vendeur@pisecinema.com", "Vendeur123", roles.get(2).getId()));
        return employes;
    }

    // Dans le fichier DataInitializer.java
    private static List<Planning> createPlannings(List<Personnel> personnel) {
        List<Planning> items = new ArrayList<>();

        // On récupère la date et l'heure actuelles comme point de référence
        LocalDateTime maintenant = LocalDateTime.now();

        // --- Planning pour Maria Garcia (Projectionniste, ID 2) ---
        items.add(new Planning(
                IdManager.obtenirProchainIdPlanning(),
                maintenant.withHour(17).withMinute(0).withSecond(0),
                maintenant.withHour(23).withMinute(30).withSecond(0),
                "Projection Salle 1 & 2",
                personnel.get(1).getId()
        ));
        items.add(new Planning(
                IdManager.obtenirProchainIdPlanning(),
                maintenant.plusDays(1).withHour(17).withMinute(0).withSecond(0),
                maintenant.plusDays(1).withHour(23).withMinute(30).withSecond(0), // Demain à 23h30
                "Projection Salle 3 (IMAX)",
                personnel.get(1).getId()
        ));

        // --- Planning pour John Smith (Vendeur, ID 3) ---
        items.add(new Planning(
                IdManager.obtenirProchainIdPlanning(),
                maintenant.withHour(18).withMinute(0).withSecond(0),
                maintenant.withHour(22).withMinute(0).withSecond(0),
                "Vente Snacking",
                personnel.get(2).getId() // ID de John
        ));
        items.add(new Planning(
                IdManager.obtenirProchainIdPlanning(),
                maintenant.plusDays(2).withHour(14).withMinute(0).withSecond(0),
                maintenant.plusDays(2).withHour(19).withMinute(0).withSecond(0),
                "Accueil & Billetterie",
                personnel.get(2).getId()
        ));

        return items;
    }

    private static List<Seance> createSeances(List<Film> films, List<Salle> salles) {
        List<Seance> seances = new ArrayList<>();

        // On calcule les dates par rapport à aujourd'hui.
        LocalDate aujourdhui = LocalDate.now();
        LocalDate demain = aujourdhui.plusDays(1);
        LocalDate apresDemain = aujourdhui.plusDays(2);

        int seanceIdCounter = 1;

        // --- Séances pour AUJOURD'HUI (uniquement si elles n'ont pas encore eu lieu) ---
        if (LocalTime.now().isBefore(LocalTime.of(14, 0))) {
            seances.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(14, 0)), salles.get(1).getId(), films.get(1).getId())); // Oppenheimer
        }
        if (LocalTime.now().isBefore(LocalTime.of(17, 30))) {
            seances.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(17, 30)), salles.get(2).getId(), films.get(2).getId())); // Spider-Man
        }
        if (LocalTime.now().isBefore(LocalTime.of(20, 15))) {
            seances.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(20, 15)), salles.get(0).getId(), films.get(0).getId())); // Dune
        }
        if (LocalTime.now().isBefore(LocalTime.of(21, 0))) {
            seances.add(new Seance(seanceIdCounter++, LocalDateTime.of(aujourdhui, LocalTime.of(21, 0)), salles.get(1).getId(), films.get(1).getId())); // Oppenheimer
        }

        // --- Séances pour DEMAIN ---
        seances.add(new Seance(seanceIdCounter++, LocalDateTime.of(demain, LocalTime.of(14, 0)), salles.get(0).getId(), films.get(0).getId())); // Dune
        seances.add(new Seance(seanceIdCounter++, LocalDateTime.of(demain, LocalTime.of(17, 0)), salles.get(1).getId(), films.get(2).getId())); // Spider-Man
        seances.add(new Seance(seanceIdCounter++, LocalDateTime.of(demain, LocalTime.of(20, 30)), salles.get(2).getId(), films.get(1).getId())); // Oppenheimer

        // --- Séances pour APRÈS-DEMAIN ---
        seances.add(new Seance(seanceIdCounter++, LocalDateTime.of(apresDemain, LocalTime.of(17, 30)), salles.get(0).getId(), films.get(2).getId())); // Spider-Man
        seances.add(new Seance(seanceIdCounter++, LocalDateTime.of(apresDemain, LocalTime.of(21, 0)), salles.get(2).getId(), films.get(0).getId())); // Dune

        return seances;
    }

    private static List<AffectationSeance> createAffectations(List<Personnel> personnel, List<Seance> seances) {
        List<AffectationSeance> items = new ArrayList<>();
        if (seances.size() >= 3) {
            items.add(new AffectationSeance(seances.get(2).getId(), personnel.get(1).getId()));
        }
        return items;
    }

    private static List<ProduitSnack> createProduitsSnack() {
        List<ProduitSnack> produits = new ArrayList<>();
        produits.add(new ProduitSnack(1, "Popcorn Salé Grand", "Maïs éclaté salé, 250g", 6.50, 100));
        produits.add(new ProduitSnack(2, "Soda 50cl", "Boisson gazeuse sucrée", 3.50, 200));
        produits.add(new ProduitSnack(3, "M&M's 200g", "Cacahuètes enrobées de chocolat", 4.00, 150));
        produits.add(new ProduitSnack(4, "Popcorn Sucré Moyen", "Maïs éclaté sucré, 150g", 5.50, 80));
        produits.add(new ProduitSnack(5, "Bouteille d'eau 50cl", "Eau de source plate", 2.00, 300));
        produits.add(new ProduitSnack(6, "Nachos & Fromage", "Tortilla chips avec sauce fromage", 7.00, 50));
        return produits;
    }

    private static List<EvaluationClient> createEvaluations(List<Client> clients, List<Film> films) {
        List<EvaluationClient> evaluations = new ArrayList<>();
        evaluations.add(new EvaluationClient(clients.get(0).getId(), films.get(0).getId(), 5, "Visuellement incroyable, une pure merveille !", LocalDateTime.now().minusDays(1)));
        evaluations.add(new EvaluationClient(clients.get(1).getId(), films.get(0).getId(), 4, "Très bon film, un peu long par moments.", LocalDateTime.now().minusHours(5)));
        evaluations.add(new EvaluationClient(clients.get(1).getId(), films.get(1).getId(), 5, "Un chef d'oeuvre. La performance de l'acteur est magistrale.", LocalDateTime.now().minusDays(3)));
        return evaluations;
    }

    private static void createScenarioReservation(List<Client> clients, List<Seance> seances, List<Siege> sieges, List<Tarif> tarifs, List<Reservation> reservations, List<Billet> billets) {

        // --- On rend le scénario de test fiable et déterministe ---
        // 1. On cherche une séance spécifique qui existera TOUJOURS : 
        //    celle de Dune (film ID 1), qui est programmée pour demain.
        Seance seancePourReservation = null;
        for (Seance s : seances) {
            // On cible un film et une heure précise pour éviter toute ambiguïté.
            if (s.getIdFilm() == 1 && s.getDateHeureDebut().getHour() == 14) {
                seancePourReservation = s;
                break; // On a trouvé la bonne séance, on arrête la recherche.
            }
        }

        // 2. Sécurité : si pour une raison quelconque la séance n'est pas trouvée, on ne crée pas de réservation.
        if (seancePourReservation == null) {
            System.err.println("AVERTISSEMENT: La séance de test ('Dune' demain à 14h) n'a pas été trouvée. Le scénario de réservation est annulé.");
            return;
        }

        // 3. On crée la réservation pour le premier client (Alice).
        Reservation res = new Reservation(1, LocalDateTime.now().minusHours(1), clients.get(0).getId());
        reservations.add(res);

        // 4. On choisit deux sièges spécifiques dans la salle correspondante (Salle 1, qui a 100 places).
        //    Les sièges d'ID 58 et 59 correspondent à la Rangée 6, sièges 8 et 9.
        Siege siege1 = sieges.get(57);
        Siege siege2 = sieges.get(58);

        // 5. On crée les billets en utilisant l'ID de la séance qu'on a spécifiquement ciblée.
        billets.add(new Billet(1, res.getId(), tarifs.get(0).getId(), siege1.getId(), seancePourReservation.getId()));
        billets.add(new Billet(2, res.getId(), tarifs.get(0).getId(), siege2.getId(), seancePourReservation.getId()));

        System.out.println("INFO: Scénario de réservation créé pour Alice pour la séance de 'Dune' (ID: " + seancePourReservation.getId() + ") avec les sièges 58 et 59.");
    }

    private static void createScenarioVenteSnack(List<Personnel> personnel, List<ProduitSnack> produits, List<Caisse> caisses, List<Client> clients, List<VenteSnack> ventes, List<LigneVente> lignes) {
        VenteSnack vente = new VenteSnack(1, LocalDateTime.now().minusHours(2), personnel.get(0).getId(), caisses.get(0).getId(), 1);
        ventes.add(vente);

        lignes.add(new LigneVente(vente.getIdVente(), produits.get(0).getId(), 1, produits.get(0).getPrixVente()));
        lignes.add(new LigneVente(vente.getIdVente(), produits.get(1).getId(), 1, produits.get(1).getPrixVente()));
    }

    /**
     * Méthode générique qui prend n'importe quelle liste d'objets et la
     * sauvegarde dans un fichier binaire.
     *
     * @param filename Le nom du fichier (ex: "films.dat").
     * @param list La liste d'objets à sauvegarder.
     * @param <T> Le type des objets dans la liste.
     */
    private static <T> void saveList(String filename, List<T> list) {
        // On s'assure que le dossier "data" existe. Si non, on le crée.
        new File("data").mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/" + filename))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.err.println("ERREUR fatale lors de la sauvegarde du fichier " + filename);
            e.printStackTrace();
        }
    }
}
