package com.mycompany.cinema;

import com.mycompany.cinema.util.DataInitializer;
import com.mycompany.cinema.view.LoginFrame; // ON IMPORTE LoginFrame
import java.io.File;
import javax.swing.SwingUtilities;

public class Cinema {

    public static void main(String[] args) {
        // GESTION DE LA PERSISTANCE (inchangée)
        File dataDir = new File("data");
        if (!dataDir.exists() || dataDir.list() == null || dataDir.list().length == 0) {
            System.out.println("Base de données de fichiers non trouvée. Création du jeu de données...");
            DataInitializer.seed();
            System.out.println("Jeu de données créé.");
        } else {
            System.out.println("Base de données de fichiers détectée.");
        }

        // LANCEMENT DE LA FENÊTRE DE CONNEXION (au lieu de MainFrame)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}