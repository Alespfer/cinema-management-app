package com.mycompany.cinema.dao;

import com.mycompany.cinema.Caisse;
import java.util.List;
import java.util.Optional;

/**
 * Définit le contrat pour la gestion des caisses enregistreuses (points de vente).
 * 
 * L'interface graphique n'utilisera ce contrat que dans des contextes très spécifiques :
 * - Le `PointDeVenteFrame` (interface du vendeur) pour savoir sur quelle caisse la vente est effectuée.
 * - Le `ReportingPanel` (interface admin) pour afficher les détails d'une vente.
 * L'interface client n'est pas concernée.
 */
public interface CaisseDAO {

    /** Règle n°1 : Savoir ajouter une nouvelle caisse. */
    void addCaisse(Caisse caisse);
    
    /** 
     * Règle n°2 : Savoir chercher une caisse par son ID.
     * La méthode retourne un `Optional`, un objet qui peut contenir une Caisse ou être vide.
     * C'est une façon moderne et sûre de dire "je vais chercher, mais je ne garantis pas de trouver".
     */
    Optional<Caisse> getCaisseById(int id);

    /** Règle n°3 : Savoir lister toutes les caisses. */
    List<Caisse> getAllCaisses();
    
    /** Règle n°4 : Savoir mettre à jour les informations d'une caisse. */
    void updateCaisse(Caisse caisse);
    
    /** Règle n°5 : Savoir supprimer une caisse. */
    void deleteCaisse(int id);
    
    /** Règle n°6 : Savoir recharger les données depuis le disque. */
    void rechargerDonnees();
}