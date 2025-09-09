package com.mycompany.cinema.util;

/**
 * Petite interface technique, utilisée par le `GestionnaireIds` pour qu'il puisse 
 * manipuler n'importe quelle liste d'objets (Films, Clients, etc.) sans connaître 
 * leurs détails.
 *
 * @param <T> Le type de l'objet (par exemple, Film ou Client).
 */
public interface ExtractId<T> {
    /**
     * @param object L'objet dont on veut l'ID.
     * @return L'identifiant sous forme d'entier.
     */
    int extraireId(T object);
}