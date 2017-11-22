package de.idnow.example.core.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.idnow.example.core.entity.Company;
import de.idnow.example.core.repository.CRUDService;
import de.idnow.example.core.repository.TestCRUDService;
import de.idnow.example.core.service.CompanyBusinessImpl;

public class CompanyBusinessUT {
	private static final int ID = 1;
	CompanyBusinessImpl companyBusiness;

	@Before
	public void init() {
		CRUDService<Company> mockedCompanyService = new TestCRUDService<Company>();

		this.companyBusiness = new CompanyBusinessImpl(mockedCompanyService);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void create_withNull_shouldThrowIllegalArgumentException(){
		this.companyBusiness.create(null);
	}
	
	@Test
	public void create_withCompany_shouldReturnCompanyObject(){
		Company company = this.createMockCompany(ID);
		Company result = this.companyBusiness.create(company);
		assertEquals(company.getId(), result.getId());
		assertEquals(company.getName(), result.getName());
	}

	private Company createMockCompany(int id) {
		Company result = new Company();
		result.setId(id);
		result.setName("name");
		return result;
	}

}
