package de.idnow.example.core.repository;

import de.idnow.example.core.entity.Company;

public class CompanyCRUDService extends CRUDServiceImpl<Company>{

	@Override
	protected Class<Company> forClass() {
		return Company.class;
	}

}
