package com.mycompany.cinema.dao.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * C'est la "boîte à outils" magique pour tous les autres gestionnaires de données (DAO).
 * Son rôle est de fournir le code de base, commun à tous, pour lire et écrire
 * une liste d'objets dans un fichier.
 * 
 * Grâce au mécanisme de "génériques" (<T>), on peut lui dire de travailler avec n'importe
 * quel type d'objet : `GenericDAOImpl<Film>`, `GenericDAOImpl<Client>`, etc.
 * 
 * Pour le développeur de l'interface graphique : cette classe est le moteur invisible de la
 * persistance. Vous n'aurez jamais à l'utiliser directement, mais il est bon de savoir
 * qu'elle existe et qu'elle assure que toutes les données sont chargées et sauvegardées
 * de manière uniforme et sécurisée.
 */
public abstract class GenericDAOImpl<T> {

    // Le chemin vers le fichier de sauvegarde (ex: "data/films.dat").
    private final String filePath;
    // La liste des objets (films, clients, etc.) actuellement chargée en mémoire.
    protected List<T> data;

    /**
     * Le constructeur initialise le gestionnaire.
     * @param fileName Le nom du fichier où les données seront stockées.
     */
    public GenericDAOImpl(String fileName) {
        this.filePath = "data/" + fileName;
        // Dès sa création, le DAO charge immédiatement les données depuis le disque.
        this.data = loadFromFile();
    }
    
    /**
     * Force une relecture du fichier de données. Utile pour s'assurer que
     * l'application dispose des informations les plus récentes.
     */
    public void rechargerDonnees() {
        this.data = loadFromFile();
    }

    /**
     * Charge la liste d'objets depuis le fichier spécifié par 'filePath'.
     * C'est ici que la "désérialisation" a lieu.
     */
    @SuppressWarnings("unchecked")
    private List<T> loadFromFile() {
        File file = new File(filePath);
        // Si le fichier n'existe pas (premier lancement), on retourne une liste neuve et vide.
        if (!file.exists()) {
            return new ArrayList<>();
        }
        // Bloc 'try-with-resources' : gère automatiquement la fermeture du fichier.
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            // Lecture de la liste d'objets depuis le fichier.
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // En cas de problème de lecture, on affiche une erreur et on retourne une liste vide
            // pour éviter que l'application ne plante.
            System.err.println("Erreur critique lors du chargement de " + filePath);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Sauvegarde la liste 'data' (actuellement en mémoire) dans le fichier.
     * C'est ici que la "sérialisation" a lieu.
     */
    protected void saveToFile() {
        // S'assure que le dossier "data" existe avant d'essayer d'écrire dedans.
        new File("data").mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.filePath))) {
            // Écriture de la liste complète d'objets dans le fichier.
            oos.writeObject(this.data);
        } catch (IOException e) {
            System.err.println("Erreur critique lors de la sauvegarde de " + filePath);
            e.printStackTrace();
        }
    }
}