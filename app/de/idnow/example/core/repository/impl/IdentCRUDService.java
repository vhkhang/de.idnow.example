package de.idnow.example.core.repository.impl;

import de.idnow.example.core.entity.Identification;
import de.idnow.example.core.repository.CRUDServiceImpl;

public class IdentCRUDService extends CRUDServiceImpl<Identification> {

    @Override
    protected Class<Identification> forClass() {
        return Identification.class;
    }

}
