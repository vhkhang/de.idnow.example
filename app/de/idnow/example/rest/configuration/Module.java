package de.idnow.example.rest.configuration;

import com.google.inject.AbstractModule;

import de.idnow.example.core.service.CompanyBusiness;
import de.idnow.example.core.service.CompanyBusinessImpl;
import de.idnow.example.core.service.IdentificationBusiness;
import de.idnow.example.core.service.IdentificationBusinessImpl;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(CompanyBusiness.class).to(CompanyBusinessImpl.class);
		bind(IdentificationBusiness.class).to(IdentificationBusinessImpl.class);
	}

}
