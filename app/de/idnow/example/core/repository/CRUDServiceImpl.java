package de.idnow.example.core.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.idnow.example.core.entity.Entity;
import de.idnow.example.core.resource.ResourceManager;

public abstract class CRUDServiceImpl<T extends Entity> implements CRUDService<T> {

    private Class<T> persistentClass;

    public CRUDServiceImpl() {
        this.persistentClass = this.resolveClass();
    }

    @SuppressWarnings("unchecked")
    private Class<T> resolveClass() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public T insert(T obj) {
        this.getResource().put(obj.getId(), obj);
        return obj;
    }

    @Override
    public T update(T obj) {
        this.getResource().put(obj.getId(), obj);
        return obj;
    }

    @Override
    public Optional<T> get(int id) {
        return Optional.ofNullable(this.getResource().get(id));
    }

    @Override
    public void delete(T obj) {
        throw new UnsupportedOperationException("Delete method is not supported");
    }

    @Override
    public List<T> getAll() {
        return (List<T>) this.getResource().values().stream().collect(Collectors.toList());
    }

    private Map<Integer, T> getResource() {
        return ResourceManager.get(this.persistentClass).getResource();
    }

}
