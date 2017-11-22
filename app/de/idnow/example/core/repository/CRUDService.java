package de.idnow.example.core.repository;

import java.util.List;
import java.util.Optional;

import com.google.inject.ImplementedBy;

import de.idnow.example.core.entity.Entity;

public interface CRUDService<T extends Entity> {
    /**
     * Provide a service to insert entity into datasource then return flushed
     * item
     * 
     * @param obj
     * @return
     */
    T insert(T obj);

    /**
     * Provide a service to update entity then return flushed item, the item
     * itself should be signed before hand
     * 
     * @param obj
     * @return
     */
    T update(T obj);

    /**
     * Provide a service to get entity by id. If there is no entity, result will
     * be empty
     * 
     * @param id
     * @return Nullable {@link Optional}
     */
    Optional<T> get(int id);

    /**
     * Provide a service to getAll entities without condition
     * 
     * @return List of item or empty if there is no result
     */
    List<T> getAll();

    /**
     * Provide a service to delete a entity from datasource
     * 
     * @param obj
     */
    void delete(T obj);
}
