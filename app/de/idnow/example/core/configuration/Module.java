package de.idnow.example.core.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import de.idnow.example.core.entity.Company;
import de.idnow.example.core.entity.Identification;
import de.idnow.example.core.repository.CRUDService;
import de.idnow.example.core.repository.impl.CompanyCRUDService;
import de.idnow.example.core.repository.impl.IdentCRUDService;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<CRUDService<Identification>>() {}).to(IdentCRUDService.class);

        bind(new TypeLiteral<CRUDService<Company>>() {}).to(CompanyCRUDService.class);
    }

}