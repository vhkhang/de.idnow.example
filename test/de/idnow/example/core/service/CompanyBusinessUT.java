package de.idnow.example.core.service;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

import de.idnow.example.core.entity.Company;
import de.idnow.example.core.repository.CRUDService;
import de.idnow.example.core.repository.TestCRUDService;

public class CompanyBusinessUT {
	private static final int ID = 1;
	CompanyBusinessImpl companyBusiness;
	
	private Injector injector;
	
	@Before
	public void setUp() throws Exception {
		injector = Guice.createInjector(new AbstractModule() {
			
			@Override
			protected void configure() {
				 bind(new TypeLiteral<CRUDService<Company>>(){})
			      .to(new TypeLiteral<TestCRUDService<Company>>(){});
			}
		});
		this.companyBusiness = injector.getInstance(CompanyBusinessImpl.class);
	}

	@After
	public void tearDown() throws Exception {
		injector = null;
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
