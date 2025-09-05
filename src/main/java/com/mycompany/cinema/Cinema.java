package com.mycompany.cinema;

import com.mycompany.cinema.util.DataInitializer;
import com.mycompany.cinema.view.Login;
import java.io.File;
import javax.swing.SwingUtilities;

/**
 * C'est le point de départ de toute l'application.
 * Quand le programme est lancé, c'est cette classe qui est exécutée en premier.
 * 
 * Pour le développeur de l'interface graphique, il n'y a rien à modifier ici.
 * Son rôle est simple :
 * 1. Préparer les données : Il vérifie si c'est la première fois que l'on lance
 *    le programme. Si c'est le cas, il crée un jeu de données de test (films, salles...).
 * 2. Lancer la première fenêtre : Il affiche la fenêtre de connexion (`LoginFrame`).
 * 
 * Votre travail sur l'interface commence donc dans le fichier `LoginFrame.java`.
 */
public class Cinema {

    /**
     * La méthode 'main' est le cœur du lancement.
     */
    public static void main(String[] args) {
        
        // On vérifie si le dossier "data" qui nous sert de base de données existe.
        File dataDir = new File("data");
        if (!dataDir.exists() || dataDir.list() == null || dataDir.list().length == 0) {
            // S'il n'existe pas, on le peuple avec des données de démonstration.
            System.out.println("Base de données de fichiers non trouvée. Création du jeu de données...");
            DataInitializer.seed();
            System.out.println("Jeu de données créé.");
        } else {
            System.out.println("Base de données de fichiers détectée.");
        }

            // Création et affichage de la fenêtre de connexion.
            Login loginFrame = new Login();
            loginFrame.setVisible(true);
    }
}