package de.idnow.example.core.service;

import java.util.List;

import de.idnow.example.core.entity.Identification;
import de.idnow.example.core.exception.ResourceNotFoundException;

public interface IdentificationBusiness {
    Identification start(Identification identification) throws ResourceNotFoundException;

    List<Identification> getAll();
}
