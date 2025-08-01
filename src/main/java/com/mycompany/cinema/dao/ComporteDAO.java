// Fichier : ComporteDAO.java
package com.mycompany.cinema.dao;

import com.mycompany.cinema.Comporte;
import java.util.List;

/**
 * Définit le "contrat" pour gérer les lignes de détail d'une vente de snacks.
 * Une ligne de vente lie un ticket de caisse à un produit spécifique.
 * 
 * Pour l'interface graphique : ce contrat est utilisé en coulisses. Il garantit que
 * la logique métier peut toujours récupérer les informations nécessaires pour,
 * par exemple, afficher le détail d'un ticket de caisse dans le panneau de reporting.
 */
public interface ComporteDAO {
    
    /** Règle n°1 : Savoir enregistrer une nouvelle ligne de vente. */
    void addLigneVente(Comporte comporte);

    /** Règle n°2 : Savoir retrouver toutes les lignes de vente d'un ticket de caisse donné. */
    List<Comporte> getLignesByVenteId(int venteId);

    /**
     * Règle n°3 : Savoir lister TOUTES les lignes de vente jamais enregistrées.
     * C'est une méthode de support nécessaire pour des vérifications internes,
     * comme s'assurer qu'un produit n'est pas lié à une vente avant de le supprimer.
     */
    List<Comporte> getAllLignesVente();
    
    /** Règle n°4 : Savoir forcer le rechargement des données depuis le disque. */
    void rechargerDonnees();
}