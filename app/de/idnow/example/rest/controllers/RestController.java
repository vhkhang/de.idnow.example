package de.idnow.example.rest.controllers;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import de.idnow.example.common.util.JsonMapper;
import de.idnow.example.core.entity.Company;
import de.idnow.example.core.entity.Identification;
import de.idnow.example.core.exception.ResourceNotFoundException;
import de.idnow.example.core.service.CompanyBusiness;
import de.idnow.example.core.service.IdentificationBusiness;
import play.mvc.Controller;
import play.mvc.Result;

public class RestController extends Controller {

	private CompanyBusiness companyBus;
	private IdentificationBusiness identBus;

	@Inject
	public RestController(CompanyBusiness companyBus, IdentificationBusiness identBus) {
		this.companyBus = companyBus;
		this.identBus = identBus;
	}

	public Result startIdentification() {
		// Get the parsed JSON data
		JsonNode json = request().body().asJson();
		Optional<Identification> identOpt = JsonMapper.toObj(json, Identification.class);
		if (!identOpt.isPresent()) {
			return badRequest("Bad request error");
		}
		Identification identification;
		try {
			identification = this.identBus.start(identOpt.get());
		} catch (ResourceNotFoundException e) {
			return badRequest(e.getMessage());
		}
		return ok(JsonMapper.toJson(identification));
	}

	public Result addCompany() {
		// Get the parsed JSON data
		JsonNode json = request().body().asJson();
		Optional<Company> companyOpt = JsonMapper.toObj(json, Company.class);
		if (!companyOpt.isPresent()) {
			return badRequest("Bad request error");
		}

		Company entity = this.companyBus.create(companyOpt.get());
		return ok(JsonMapper.toJson(entity));
	}

	public Result identifications() {
		return ok(JsonMapper.toJson(this.identBus.getAll()));
	}

}
