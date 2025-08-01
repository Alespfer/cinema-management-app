package com.mycompany.cinema.util;

/**
 * Ceci est une petite interface technique très abstraite.
 * Son seul but est de définir un "contrat" qui dit : "Pour n'importe quel objet,
 * je dois pouvoir en extraire un numéro d'identifiant (un int)".
 * Elle est utilisée par IdManager pour fonctionner avec n'importe quel type d'objet
 * (Film, Client, etc.) de manière générique. Tu n'interagiras jamais directement avec elle.
 *
 * @param <T> Le type de l'objet (par exemple, Film ou Client).
 */
public interface IdExtractor<T> {
    /**
     * La seule méthode à implémenter pour respecter le contrat.
     * @param object L'objet dont on veut l'ID.
     * @return L'identifiant sous forme d'entier.
     */
    int extractId(T object);
}