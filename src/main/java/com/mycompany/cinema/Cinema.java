// Fichier : src/main/java/com/mycompany/cinema/Cinema.java
package com.mycompany.cinema;

import com.mycompany.cinema.util.DataInitializer;
import com.mycompany.cinema.view.MainFrame; // Importe ta nouvelle fenêtre
import java.io.File;
import javax.swing.SwingUtilities;

public class Cinema {

    public static void main(String[] args) {
        // Étape 1 : GESTION DE LA PERSISTANCE (inchangée)
        File dataDir = new File("data");
        if (!dataDir.exists() || dataDir.list() == null || dataDir.list().length == 0) {
            System.out.println("Base de données de fichiers non trouvée. Création du jeu de données...");
            DataInitializer.seed();
            System.out.println("Jeu de données créé.");
        } else {
            System.out.println("Base de données de fichiers détectée.");
        }

        // Étape 2 : LANCEMENT DE L'INTERFACE GRAPHIQUE
        // C'est la seule manière correcte et sûre de démarrer une application Swing.
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}