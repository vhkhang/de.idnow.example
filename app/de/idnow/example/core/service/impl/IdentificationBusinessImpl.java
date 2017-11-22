package de.idnow.example.core.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;

import de.idnow.example.core.entity.Company;
import de.idnow.example.core.entity.Identification;
import de.idnow.example.core.exception.ResourceNotFoundException;
import de.idnow.example.core.repository.CRUDService;
import de.idnow.example.core.service.IdentificationBusiness;

public class IdentificationBusinessImpl implements IdentificationBusiness {

    @Inject
    private CRUDService<Company> companyService;

    @Inject
    private CRUDService<Identification> identificationService;

    private static final Comparator<Identification> IDENT_COMPARATOR = new Comparator<Identification>() {

        @Override
        public int compare(Identification record1, Identification record2) {
            int c;
            c = Integer.compare(record2.getWaitingTime(), record1.getWaitingTime());
            if (c == 0)
                c = Float.compare(record1.getCompany().getCurrentSlaPercentage(),
                        record2.getCompany().getCurrentSlaPercentage());
            if (c == 0)
                c = Integer.compare(record1.getCompany().getSlaTime(), record2.getCompany().getSlaTime());

            return c;
        }
    };

    @Override
    public Identification start(Identification identification) throws ResourceNotFoundException {
        if (identification == null)
            throw new IllegalArgumentException("Cannot call start service for null object");

        Optional<Company> relatedCompany = this.companyService.get(identification.getCompanyId());
        if (!relatedCompany.isPresent()) {
            throw new ResourceNotFoundException(
                    (String.format("Company with id %d is undefined", identification.getCompanyId())));
        }
        identification.setCompany(relatedCompany.get());
        return identificationService.insert(identification);
    }

    @Override
    public List<Identification> getAll() {
        List<Identification> result = identificationService.getAll();
        Collections.sort(result, IDENT_COMPARATOR);
        return result;
    }
}
