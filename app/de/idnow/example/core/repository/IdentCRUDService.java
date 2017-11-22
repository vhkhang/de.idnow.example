package de.idnow.example.core.repository;

import de.idnow.example.core.entity.Identification;

public class IdentCRUDService extends CRUDServiceImpl<Identification>{

	@Override
	protected Class<Identification> forClass() {
		return Identification.class;
	}

}
