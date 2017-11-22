package de.idnow.example.core.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;

import de.idnow.example.core.entity.Company;
import de.idnow.example.core.entity.Identification;
import de.idnow.example.core.exception.ResourceNotFoundException;
import de.idnow.example.core.repository.CRUDService;
import de.idnow.example.core.repository.CRUDServiceImpl;

public class IdentificationBusinessImpl implements IdentificationBusiness{

	private static final Comparator<Identification> idenComparator = new Comparator<Identification>() {

		@Override
		public int compare(Identification record1, Identification record2) {
			int c;
			c = Integer.compare(record2.getWaiting_time(), record1.getWaiting_time());
			if (c == 0)
				c = Float.compare(record1.getCompany().getCurrent_sla_percentage(),
						record2.getCompany().getCurrent_sla_percentage());
			if (c == 0)
				c = Integer.compare(record1.getCompany().getSla_time(), record2.getCompany().getSla_time());
			return c;
		}
	};

	private CRUDService<Company> companyService;
	private CRUDService<Identification> identificationService;

	/*public IdentificationBusinessImpl() {
		companyService = new CRUDServiceImpl<Company>() {
			@Override
			protected Class<Company> forClass() {
				return Company.class;
			}
		};
		identificationService = new CRUDServiceImpl<Identification>() {
			@Override
			protected Class<Identification> forClass() {
				return Identification.class;
			}
		};
	}*/

	@Inject
	public IdentificationBusinessImpl(CRUDService<Company> companyService,
			CRUDService<Identification> identificationService) {
		this.companyService = companyService;
		this.identificationService = identificationService;
	}

	@Override
	public Identification start(Identification identification) throws ResourceNotFoundException {
		if(identification == null) 
			throw new IllegalArgumentException("Cannot call start service for null object");
		
		Optional<Company> relatedCompany = this.companyService.get(identification.getCompanyid());
		if (!relatedCompany.isPresent()) {
			throw new ResourceNotFoundException(
					(String.format("Company with id %d is undefined", identification.getCompanyid())));
		}
		identification.setCompany(relatedCompany.get());
		return identificationService.insert(identification);
	}

	@Override
	public List<Identification> getAll() {
		List<Identification> result = identificationService.getAll();
		Collections.sort(result, idenComparator);
		return result;
	}
}
