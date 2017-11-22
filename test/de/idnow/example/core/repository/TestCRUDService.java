package de.idnow.example.core.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.idnow.example.core.entity.Entity;

public class TestCRUDService<S extends Entity> implements CRUDService<S> {

    private Map<Integer, S> data = new HashMap<>();

    @Override
    public S insert(S obj) {
        data.put(obj.getId(), obj);
        return obj;
    }

    @Override
    public S update(S obj) {
        return data.put(obj.getId(), obj);
    }

    @Override
    public Optional<S> get(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<S> getAll() {
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public void delete(S obj) {
        throw new UnsupportedOperationException();
    }

}
