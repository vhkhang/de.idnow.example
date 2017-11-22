package de.idnow.example.core.repository.impl;

import de.idnow.example.core.entity.Company;
import de.idnow.example.core.repository.CRUDServiceImpl;

public class CompanyCRUDService extends CRUDServiceImpl<Company> {

    @Override
    protected Class<Company> forClass() {
        return Company.class;
    }

}
