package com.mycompany.cinema;

import com.mycompany.cinema.service.CinemaService;
import com.mycompany.cinema.service.impl.CinemaServiceImpl;
import com.mycompany.cinema.util.DataInitializer;
import java.io.File;

public class Cinema {
    public static void main(String[] args) {
        // ... (Code d'initialisation des données inchangé) ...

        System.out.println("--- Démarrage des services du cinéma ---");
        CinemaService service = new CinemaServiceImpl();
        
        // TEST : Appeler une méthode métier
        System.out.println("Films actuellement à l'affiche :");
        service.getFilmsAffiche().forEach(film -> System.out.println("- " + film.getTitre()));
        
        // La logique applicative (ex: IHM) commencera ici.
    }
}