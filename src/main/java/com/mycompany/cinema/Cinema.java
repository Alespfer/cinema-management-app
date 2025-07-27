package com.mycompany.cinema;

import com.mycompany.cinema.util.DataInitializer;
import com.mycompany.cinema.view.LoginFrame;
import java.io.File;
import javax.swing.SwingUtilities;

/**
 * Classe principale de l'application.
 * C'est le point d'entrée du programme, là où tout commence.
 * Son rôle est :
 * 1. Vérifier si les données de test existent, et les créer si ce n'est pas le cas.
 * 2. Lancer l'interface graphique de connexion.
 *
 */
public class Cinema {

    /**
     * La méthode main, exécutée au lancement du programme.
     */
    public static void main(String[] args) {
        
        // --- GESTION DE LA PERSISTANCE ---
        // On vérifie si notre "base de données" (le dossier "data") existe.
        File dataDir = new File("data");
        if (!dataDir.exists() || dataDir.list() == null || dataDir.list().length == 0) {
            // Si le dossier n'existe pas ou est vide, c'est le premier lancement.
            System.out.println("Base de données de fichiers non trouvée. Création du jeu de données...");
            
            // On appelle notre classe utilitaire pour générer toutes les données de test.
            DataInitializer.seed();
            
            System.out.println("Jeu de données créé.");
        } else {
            // Si le dossier existe déjà, on ne fait rien pour ne pas écraser les données sauvegardées.
            System.out.println("Base de données de fichiers détectée.");
        }

        // --- LANCEMENT DE L'INTERFACE GRAPHIQUE ---
        // On lance l'interface dans le thread de gestion des événements Swing (EDT).
        // C'est la manière correcte et sûre de démarrer une application Swing, comme vu en cours.
        SwingUtilities.invokeLater(() -> {
            // On ne lance plus la fenêtre principale directement, mais la fenêtre de connexion.
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}