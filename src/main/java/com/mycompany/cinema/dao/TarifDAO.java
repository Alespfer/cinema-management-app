package com.mycompany.cinema.dao;

import com.mycompany.cinema.Tarif;
import java.util.List;
import java.util.Optional;

public interface TarifDAO {
    void addTarif(Tarif tarif);
    Optional<Tarif> getTarifById(int id);
    List<Tarif> getAllTarifs();
    void updateTarif(Tarif tarif);
    void deleteTarif(int id);
}