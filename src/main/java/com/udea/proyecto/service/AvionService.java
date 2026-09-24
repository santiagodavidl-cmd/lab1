package com.udea.proyecto.service;

import com.udea.proyecto.domain.Avion;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.udea.proyecto.domain.Avion}.
 */
public interface AvionService {
    /**
     * Save a avion.
     *
     * @param avion the entity to save.
     * @return the persisted entity.
     */
    Avion save(Avion avion);

    /**
     * Updates a avion.
     *
     * @param avion the entity to update.
     * @return the persisted entity.
     */
    Avion update(Avion avion);

    /**
     * Partially updates a avion.
     *
     * @param avion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Avion> partialUpdate(Avion avion);

    /**
     * Get all the avions.
     *
     * @return the list of entities.
     */
    List<Avion> findAll();

    /**
     * Get the "id" avion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Avion> findOne(Long id);

    /**
     * Delete the "id" avion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
