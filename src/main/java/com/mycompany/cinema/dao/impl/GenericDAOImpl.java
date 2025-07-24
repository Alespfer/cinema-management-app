// Fichier : src/main/java/com/mycompany/cinema/dao/impl/GenericDAOImpl.java
package com.mycompany.cinema.dao.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de base abstraite pour toutes les implémentations de DAO.
 * Elle contient la logique générique de chargement et de sauvegarde par sérialisation.
 * Utilise les génériques (<T>) pour fonctionner avec n'importe quel type de POJO.
 */
public abstract class GenericDAOImpl<T> {

    private final String filePath;
    protected List<T> data;

    public GenericDAOImpl(String fileName) {
        this.filePath = "data/" + fileName;
        this.data = loadFromFile();
    }

    /**
     * Charge la liste d'objets depuis le fichier de persistance.
     * Si le fichier n'existe pas, retourne une liste vide.
     */
    @SuppressWarnings("unchecked")
    private List<T> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>(); // Si le fichier n'existe pas, la base de données est vide.
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur critique lors du chargement de " + filePath);
            e.printStackTrace();
            // Dans une application réelle, on lancerait une exception personnalisée ici.
            return new ArrayList<>();
        }
    }

    /**
     * Sauvegarde la liste actuelle d'objets dans le fichier de persistance.
     */
    protected void saveToFile() {
        new File("data").mkdirs(); // Assure que le dossier de données existe.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.filePath))) {
            oos.writeObject(this.data);
        } catch (IOException e) {
            System.err.println("Erreur critique lors de la sauvegarde de " + filePath);
            e.printStackTrace();
        }
    }
}