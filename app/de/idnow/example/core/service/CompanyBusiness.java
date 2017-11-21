package de.idnow.example.core.service;

import de.idnow.example.core.entity.Company;
import de.idnow.example.core.repository.CRUDService;
import de.idnow.example.core.repository.CRUDServiceImpl;

public class CompanyBusiness {

	private CRUDService<Company> companyService;

	public CompanyBusiness() {
		companyService = new CRUDServiceImpl<Company>() {
			@Override
			protected Class<Company> forClass() {
				return Company.class;
			}
		};
	}

	public CompanyBusiness(CRUDService<Company> companyService) {
		this.companyService = companyService;
	}

	public Company create(Company company) {
		if (company == null)
			throw new IllegalArgumentException("Cannot call create service for null object");
		return this.companyService.insert(company);
	}

}
