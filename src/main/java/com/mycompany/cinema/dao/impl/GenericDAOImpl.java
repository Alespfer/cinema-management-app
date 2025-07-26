package com.mycompany.cinema.dao.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de base pour tous les DAO.
 * Elle contient le code générique pour charger une liste d'objets depuis un fichier
 * et la sauvegarder. Grâce aux génériques (<T>), elle fonctionne pour n'importe quel
 * type d'objet (Film, Client, etc.).
 */
public abstract class GenericDAOImpl<T> {

    private final String filePath;
    protected List<T> data;

    /**
     * Le constructeur initialise le DAO en indiquant le nom du fichier à utiliser.
     * Il appelle tout de suite la méthode pour charger les données depuis le disque.
     * @param fileName Le nom du fichier de données (ex: "films.dat").
     */
    public GenericDAOImpl(String fileName) {
        this.filePath = "data/" + fileName;
        this.data = loadFromFile();
    }

    /**
     * Charge la liste d'objets depuis le fichier.
     * Si le fichier n'existe pas, on considère que c'est le premier lancement,
     * donc on retourne une liste vide.
     */
    @SuppressWarnings("unchecked")
    private List<T> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur critique lors du chargement de " + filePath);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Sauvegarde la liste d'objets (this.data) dans le fichier.
     * Cette méthode est appelée par les autres méthodes (add, update, delete)
     * à chaque fois qu'une modification est faite.
     */
    protected void saveToFile() {
        new File("data").mkdirs(); // S'assure que le dossier "data" existe
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.filePath))) {
            oos.writeObject(this.data);
        } catch (IOException e) {
            System.err.println("Erreur critique lors de la sauvegarde de " + filePath);
            e.printStackTrace();
        }
    }
}