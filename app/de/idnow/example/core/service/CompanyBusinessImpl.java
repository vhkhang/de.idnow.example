package de.idnow.example.core.service;

import com.google.inject.Inject;

import de.idnow.example.core.entity.Company;
import de.idnow.example.core.repository.CRUDService;

public class CompanyBusinessImpl implements CompanyBusiness {
	
	private CRUDService<Company> companyService;
	
	@Inject
	public CompanyBusinessImpl(CRUDService<Company> companyService) {
		this.companyService = companyService;
	}

	@Override
	public Company create(Company company) {
		if (company == null)
			throw new IllegalArgumentException("Cannot call create service for null object");
		return this.companyService.insert(company);
	}

}
