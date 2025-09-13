package com.mycompany.cinema;

import com.mycompany.cinema.util.DataInitializer;
import com.mycompany.cinema.view.FenetreConnexion;
import java.io.File;
import javax.swing.SwingUtilities;

/**
 * Point de départ de toute l'application.
 * 
 * 1. Prépare les données : Il vérifie si c'est la première fois que l'on lance
 *    le programme. Si c'est le cas, il crée un jeu de données de test (films, salles...).
 * 2. Lance la première fenêtre : Il affiche la fenêtre de connexion.
 * 
 */
public class Cinema {

    public static void main(String[] args) {
        
        // On vérifie si le dossier "data" qui nous sert de base de données existe.
        File dataDir = new File("data");
        if (!dataDir.exists() || dataDir.list() == null || dataDir.list().length == 0) {
            // S'il n'existe pas, on le peuple avec des données de test.
            System.out.println("Base de données de fichiers non trouvée. Création du jeu de données...");
            DataInitializer.initialiser();
            System.out.println("Jeu de données créé.");
        } else {
            System.out.println("Base de données de fichiers détectée.");
        }

            // Création et affichage de la fenêtre de connexion.
            FenetreConnexion loginFrame = new FenetreConnexion();
            loginFrame.setVisible(true);
    }
}