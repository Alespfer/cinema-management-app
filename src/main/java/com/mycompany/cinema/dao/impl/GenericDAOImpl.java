package com.mycompany.cinema.dao.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite servant de "boîte à outils" pour toutes les autresclasses
 * DAO. Elle centralise la logique commune de lecture et d'écritured'une liste d'objets dans un fichier.
 */
public abstract class GenericDAOImpl<T> {

    // Le chemin complet vers le fichier de sauvegarde (par ex : "data/films.dat").
    private final String cheminFichier;
    // La liste des objets (films, clients, etc.) actuellement chargée en mémoire.
    protected List<T> data;

    /**
     * Initialise le gestionnaire de données.
     *
     * @param nomFichier Le nom du fichier (ex: "films.dat") qui sera stocké
     * dans le dossier "data".
     */
    public GenericDAOImpl(String nomFichier) {
        this.cheminFichier = "data/" + nomFichier;
        this.data = chargerDepuisFichier();
    }

    /**
     * Force une relecture complète du fichier de données. Utile pour s'assurer
     * que l'application travaille avec les informations les plus à jour.
     */
    public void rechargerDonnees() {
        this.data = chargerDepuisFichier();
    }

    /**
     * Charge une liste d'objets depuis un fichier binaire.
     *
     * @return Une List<T> contenant les objets lus. Si le fichier n'existe pas
     * ou est corrompu, retourne une nouvelle liste vide.
     */
    private List<T> chargerDepuisFichier() {
        File fichier = new File(cheminFichier);
        if (!fichier.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream fluxEntreeObjet = new ObjectInputStream(new FileInputStream(fichier))) {
            // Lecture de l'intégralité de la liste d'objets depuis le fichier.
            return (List<T>) fluxEntreeObjet.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur critique lors du chargement du fichier : " + cheminFichier);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Sauvegarde la liste complète des objets actuellement en mémoire dans un
     * fichier binaire (sérialisation). 
     */
    protected void sauvegarderDansFichier() {
        new File("data").mkdirs();
        try (ObjectOutputStream fluxSortieObjet = new ObjectOutputStream(new FileOutputStream(this.cheminFichier))) {
            // Écrit l'intégralité de la liste 'data' dans le fichier, écrasant son contenu précédent.
            fluxSortieObjet.writeObject(this.data);
        } catch (IOException e) {
            System.err.println("Erreur critique lors de la sauvegarde dans le fichier : " + cheminFichier);
            e.printStackTrace();
        }
    }
}
