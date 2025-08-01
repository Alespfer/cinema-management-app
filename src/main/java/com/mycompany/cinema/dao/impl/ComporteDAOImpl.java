package com.mycompany.cinema.dao.impl;

import com.mycompany.cinema.Comporte;
import com.mycompany.cinema.dao.ComporteDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation concrète pour gérer la sauvegarde des lignes de détail d'une vente de snacks.
 * S'occupe du fichier "lignes_ventes.dat".
 * 
 * Pour le développeur de l'interface graphique : cette classe est purement technique et
 * fonctionne en arrière-plan. Vous n'aurez jamais besoin de l'appeler. Elle est
 * utilisée par le service pour enregistrer le contenu d'un panier de snacks et
 * pour calculer le total d'une vente dans les rapports de l'administrateur.
 */
public class ComporteDAOImpl extends GenericDAOImpl<Comporte> implements ComporteDAO {

    /**
     * Constructeur qui spécifie le nom du fichier de sauvegarde.
     */
    public ComporteDAOImpl() {
        super("lignes_ventes.dat");
    }

    /**
     * Ajoute une nouvelle ligne de vente à la liste en mémoire et sauvegarde dans le fichier.
     */
    @Override
    public void addLigneVente(Comporte comporte) {
        this.data.add(comporte);
        saveToFile();
    }

    /**
     * Retourne toutes les lignes de vente associées à un ticket de caisse spécifique.
     */
    @Override
    public List<Comporte> getLignesByVenteId(int venteId) {
        List<Comporte> resultat = new ArrayList<>();
        // On parcourt toutes les lignes enregistrées...
        for (Comporte ligne : this.data) {
            // ...et on ne garde que celles qui appartiennent au ticket de caisse demandé.
            if (ligne.getIdVente() == venteId) {
                resultat.add(ligne);
            }
        }
        return resultat;
    }

    /**
     * Retourne la liste complète de toutes les lignes de vente enregistrées.
     * Cette méthode est nécessaire pour des vérifications d'intégrité globales.
     * Par exemple, pour vérifier si un produit a déjà été vendu.
     */
    @Override
    public List<Comporte> getAllLignesVente() {
        // On retourne une COPIE de la liste pour protéger les données internes,
        // conformément aux bonnes pratiques déjà établies dans le projet.
        return new ArrayList<>(this.data);
    }
}