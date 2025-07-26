// Fichier : src/main/java/com/mycompany/cinema/dao/CaisseDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Caisse;
import java.util.List;
import java.util.Optional;

/**
 * Contrat pour la gestion de la persistance des Caisses (Points de Vente).
 */
public interface CaisseDAO {

    void addCaisse(Caisse caisse);
    
    Optional<Caisse> getCaisseById(int id);

    List<Caisse> getAllCaisses();
    
    void updateCaisse(Caisse caisse);
    
    void deleteCaisse(int id);
}