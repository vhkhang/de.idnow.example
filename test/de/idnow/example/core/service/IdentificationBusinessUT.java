package de.idnow.example.core.service;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

import de.idnow.example.core.entity.Company;
import de.idnow.example.core.entity.Identification;
import de.idnow.example.core.exception.ResourceNotFoundException;
import de.idnow.example.core.repository.CRUDService;
import de.idnow.example.core.repository.TestCRUDService;
import de.idnow.example.core.service.impl.IdentificationBusinessImpl;

public class IdentificationBusinessUT {
    private static final int ID = 1;
    private static final int COM_ID = 10;
    private static final int COM_01 = 1;
    private static final int IDENT_01 = 1;

    private static final int COM_02 = 2;
    private static final int IDENT_02 = 2;

    IdentificationBusinessImpl identificationBusiness;
    CRUDService<Company> mockedCompanyService;
    CRUDService<Identification> mockedIdentService;

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        mockedCompanyService = new TestCRUDService<>();
        mockedIdentService = new TestCRUDService<>();
        injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(new TypeLiteral<CRUDService<Company>>() {}).toInstance(mockedCompanyService);
                bind(new TypeLiteral<CRUDService<Identification>>() {}).toInstance(mockedIdentService);
            }
        });
        this.identificationBusiness = injector.getInstance(IdentificationBusinessImpl.class);
    }

    @After
    public void tearDown() throws Exception {
        injector = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void start_withNull_shouldThrowIllegalArgumentException() throws ResourceNotFoundException {
        this.identificationBusiness.start(null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void start_withIdentIsNotBelongToAnyCompany_shouldThownResourceNotFoundException()
            throws ResourceNotFoundException {
        Identification ident = new Identification(ID, "Test Ident", new Date(), 40, 100);
        this.identificationBusiness.start(ident);
    }

    @Test
    public void start_withIdentBelongsToACompany_shouldStartOkAndReturnObject() throws ResourceNotFoundException {
        Company company = new Company(COM_ID, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company);
        Identification ident = new Identification(ID, "Test Ident", new Date(), 40, COM_ID);

        Identification result = this.identificationBusiness.start(ident);
        assertNotNull(result);
        assertNotNull(result.getCompany());
        assertEquals(ident.getId(), result.getId());
        assertEquals(ident.getName(), result.getName());
        assertEquals(ident.getCompany().getId(), result.getCompany().getId());
        assertEquals(ident.getCompany().getName(), result.getCompany().getName());
    }

    @Test
    public void getAll_withNoRecord_shouldReturnEmpty() throws ResourceNotFoundException {
        List<Identification> result = this.mockedIdentService.getAll();
        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    public void getAll_withTwoIdent_hasOrderConditionRegardingToWaitingTime_shouldReturnCorrected()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 30, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 45, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_02, result.get(0).getId());
        assertEquals(IDENT_01, result.get(1).getId());
    }

    @Test
    public void getAll_withTwoIdent_hasOrderConditionRegardingToCurrentSLAPercentage_shouldReturnCorrected()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 30, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 60, 0.9f, 0.9f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 30, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_02, result.get(0).getId());
        assertEquals(IDENT_01, result.get(1).getId());
    }

    @Test
    public void getAll_withTwoIdent_hasOrderConditionRegardingToSLATime_shouldReturnCorrected()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 120, 0.9f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 30, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 30, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_02, result.get(0).getId());
        assertEquals(IDENT_01, result.get(1).getId());
    }

    @Test
    public void getAll_withTwoIdent_hasNoOrderConditionsForSLAPercent_shouldReturnCorrected_CaseBiggerThen()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 30, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 60, 0.8f, 0.95f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 30, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_01, result.get(0).getId());
        assertEquals(IDENT_02, result.get(1).getId());
    }

    @Test
    public void getAll_withTwoIdent_hasNoOrderConditionsForSLAPercent_shouldReturnCorrected_CaseLessThan()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 60, 0.8f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 30, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 30, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_01, result.get(0).getId());
        assertEquals(IDENT_02, result.get(1).getId());
    }

    @Test
    public void getAll_withTwoIdent_hasNoOrderConditionsForSLAPercent_shouldReturnCorrected_CaseEqualize()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 30, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 30, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_01, result.get(0).getId());
        assertEquals(IDENT_02, result.get(1).getId());
    }

    @Test
    public void getAll_withTwoIdent_hasOrderConditions_ByWaitingTimeAndCurrentSLA_shouldConsiderWaitingTimeOnly()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 45, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 60, 0.9f, 0.8f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 30, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_01, result.get(0).getId());
        assertEquals(IDENT_02, result.get(1).getId());
    }

    @Test
    public void getAll_withTwoIdent_hasOrderConditions_ByWaitingTimeAndSLATime_shouldConsiderWaitingTimeOnly()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 120, 0.9f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 45, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 30, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_01, result.get(0).getId());
        assertEquals(IDENT_02, result.get(1).getId());
    }

    @Test
    public void getAll_withTwoIdent_hasOrderConditions_ByWaitingTimeAndCurrentSLAAndSLATime_shouldConsiderWaitingTimeOnly()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 120, 0.9f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 45, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 60, 0.9f, 0.9f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 30, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_01, result.get(0).getId());
        assertEquals(IDENT_02, result.get(1).getId());
    }

    @Test
    public void getAll_withTwoIdent_hasOrderConditions_ByCurrentSLAAndSLATime_shouldConsiderCurrentSLAOnly()
            throws ResourceNotFoundException {
        Company company1 = new Company(COM_01, "companyName", 60, 0.9f, 0.95f);
        mockedCompanyService.insert(company1);
        Identification ident1 = new Identification(IDENT_01, "Test Ident", new Date(), 30, COM_01);
        this.identificationBusiness.start(ident1);

        Company company2 = new Company(COM_02, "companyName", 120, 0.9f, 0.9f);
        mockedCompanyService.insert(company2);
        Identification ident2 = new Identification(IDENT_02, "Test Ident", new Date(), 30, COM_02);
        this.identificationBusiness.start(ident2);

        List<Identification> result = this.identificationBusiness.getAll();
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(2, result.size());
        assertEquals(IDENT_02, result.get(0).getId());
        assertEquals(IDENT_01, result.get(1).getId());
    }

}
