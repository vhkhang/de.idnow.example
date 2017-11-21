package de.idnow.example.rest.controllers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import de.idnow.example.common.util.JsonMapper;
import de.idnow.example.core.entity.Company;
import de.idnow.example.core.entity.Identification;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

/**
 *
 * Simple (JUnit) tests that can call all parts of a play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 *
 */
public class RestControllerTest {

	JsonNode identifications;

	@Test
	public void addCompany_shouldReturnCorrectStatus() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {
				JsonNode company = Json.parse(
						"{\"id\": 1, \"name\": \"Test Bank\", \"sla_time\": 60, \"sla_percentage\": 0.9, \"current_sla_percentage\": 0.95}");
				WSResponse wsResponse = WS.url("http://localhost:3333/api/v1/addCompany").post(company).get(10000);
				assertEquals(OK, wsResponse.getStatus());
			}
		});
	}

	@Test
	public void addCompany_shouldReturnCorrectEntity() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {
				JsonNode company = Json.parse(
						"{\"id\": 1, \"name\": \"Test Bank\", \"sla_time\": 60, \"sla_percentage\": 0.9, \"current_sla_percentage\": 0.95}");
				WSResponse wsResponse = WS.url("http://localhost:3333/api/v1/addCompany").post(company).get(10000);
				Company result = JsonMapper.toObj(wsResponse.asJson(), Company.class).orElse(null);
				assertNotNull(result);
				assertEquals(company.textValue(), Json.toJson(result).textValue());
			}
		});
	}

	@Test
	public void postIdentification_shouldReturnOk() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {
				JsonNode identification = Json.parse(
						"{\"id\": 1, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 10, \"companyid\": 1}");
				assertEquals(OK, WS.url("http://localhost:3333/api/v1/startIdentification").post(identification)
						.get(10000).getStatus());
			}
		});

	}

	@Test
	public void postIdentification_shouldReturnCorrectEntity() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {

				JsonNode identification = Json.parse(
						"{\"id\": 1, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 10, \"companyid\": 1}");
				WSResponse wsResponse = WS.url("http://localhost:3333/api/v1/startIdentification").post(identification)
						.get(10000);
				Identification result = JsonMapper.toObj(wsResponse.asJson(), Identification.class).orElse(null);
				assertNotNull(result);
				assertNotNull(result.getCompany());
				assertEquals("Test Bank", result.getCompany().getName());
			}
		});

	}

	@Test
	public void getIdentifications_onWaitingTimeOfIdent_shouldReturnCorrectOrders() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {
				JsonNode company3 = Json.parse(
						"{\"id\": 3, \"name\": \"Test Bank\", \"sla_time\": 60, \"sla_percentage\": 0.9, \"current_sla_percentage\": 0.95}");
				assertEquals(WS.url("http://localhost:3333/api/v1/addCompany").post(company3).get(10000).getStatus(),
						OK);

				JsonNode i1 = Json.parse(
						"{\"id\": 10, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 30, \"companyid\": 3}");
				WS.url("http://localhost:3333/api/v1/startIdentification").post(i1).get(10000);

				JsonNode i2 = Json.parse(
						"{\"id\": 11, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 45, \"companyid\": 3}");
				WS.url("http://localhost:3333/api/v1/startIdentification").post(i2).get(10000);

				WSResponse response = callGetPendingIdentifications();
				List<Identification> idens = JsonMapper.toObjects(response.asJson(), Identification.class);
				assertTrue(!CollectionUtils.isEmpty(idens));

				List<Identification> result2 = idens.stream().filter(ident -> ident.getCompanyid() == 3)
						.collect(Collectors.toList());
				assertEquals(2, result2.size());
				assertEquals(11, result2.get(0).getId());
				assertEquals(10, result2.get(1).getId());

			}
		});
	}

	@Test
	public void getIdentifications_onCompanyCurrentSLA_shouldReturnCorrectOrders() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {
				JsonNode company1 = Json.parse(
						"{\"id\": 21, \"name\": \"Test Bank\", \"sla_time\": 60, \"sla_percentage\": 0.9, \"current_sla_percentage\": 0.95}");
				assertEquals(WS.url("http://localhost:3333/api/v1/addCompany").post(company1).get(10000).getStatus(),
						OK);

				JsonNode company2 = Json.parse(
						"{\"id\": 22, \"name\": \"Test Bank\", \"sla_time\": 60, \"sla_percentage\": 0.9, \"current_sla_percentage\": 0.9}");
				assertEquals(WS.url("http://localhost:3333/api/v1/addCompany").post(company2).get(10000).getStatus(),
						OK);

				JsonNode i1 = Json.parse(
						"{\"id\": 21, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 30, \"companyid\": 21}");
				WS.url("http://localhost:3333/api/v1/startIdentification").post(i1).get(10000);

				JsonNode i2 = Json.parse(
						"{\"id\": 22, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 30, \"companyid\": 22}");
				WS.url("http://localhost:3333/api/v1/startIdentification").post(i2).get(10000);

				WSResponse response = callGetPendingIdentifications();

				List<Identification> idens = JsonMapper.toObjects(response.asJson(), Identification.class);
				assertTrue(!CollectionUtils.isEmpty(idens));

				List<Identification> result = idens.stream()
						.filter(ident -> ident.getCompanyid() == 21 || ident.getCompanyid() == 22)
						.collect(Collectors.toList());
				assertEquals(2, result.size());
				assertEquals(22, result.get(0).getId());
				assertEquals(21, result.get(1).getId());

			}
		});
	}

	@Test
	public void getIdentifications_onCompanySLATime_shouldReturnCorrectOrders() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {
				JsonNode company1 = Json.parse(
						"{\"id\": 31, \"name\": \"Test Bank\", \"sla_time\": 60, \"sla_percentage\": 0.9, \"current_sla_percentage\": 0.95}");
				assertEquals(callAddCompany(company1).getStatus(), OK);

				JsonNode company2 = Json.parse(
						"{\"id\": 32, \"name\": \"Test Bank\", \"sla_time\": 120, \"sla_percentage\": 0.9, \"current_sla_percentage\": 0.95}");
				assertEquals(callAddCompany(company2).getStatus(), OK);

				JsonNode i1 = Json.parse(
						"{\"id\": 31, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 30, \"companyid\": 31}");
				callAddIdentification(i1);

				JsonNode i2 = Json.parse(
						"{\"id\": 32, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 30, \"companyid\": 32}");
				callAddIdentification(i2);

				WSResponse response = callGetPendingIdentifications();

				List<Identification> idens = JsonMapper.toObjects(response.asJson(), Identification.class);
				assertTrue(!CollectionUtils.isEmpty(idens));

				List<Identification> result = idens.stream()
						.filter(ident -> ident.getCompanyid() == 31 || ident.getCompanyid() == 32)
						.collect(Collectors.toList());

				assertEquals(2, result.size());
				assertEquals(31, result.get(0).getId());
				assertEquals(32, result.get(1).getId());

			}

			
		});
	}
	
	@Test
	public void getIdentifications_onMixConditions_shouldReturnCorrectOrders() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {
				JsonNode company1 = Json.parse(
						"{\"id\": 41, \"name\": \"Test Bank\", \"sla_time\": 60, \"sla_percentage\": 0.9, \"current_sla_percentage\": 0.95}");
				assertEquals(callAddCompany(company1).getStatus(), OK);

				JsonNode company2 = Json.parse(
						"{\"id\": 41, \"name\": \"Test Bank\", \"sla_time\": 120, \"sla_percentage\": 0.8, \"current_sla_percentage\": 0.8}");
				assertEquals(callAddCompany(company2).getStatus(), OK);

				JsonNode i1 = Json.parse(
						"{\"id\": 41, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 45, \"companyid\": 41}");
				callAddIdentification(i1);

				JsonNode i2 = Json.parse(
						"{\"id\": 42, \"name\": \"Peter Huber\", \"time\": 1435667215, \"waiting_time\": 30, \"companyid\": 41}");
				callAddIdentification(i2);

				WSResponse response = callGetPendingIdentifications();

				List<Identification> idens = JsonMapper.toObjects(response.asJson(), Identification.class);
				assertTrue(!CollectionUtils.isEmpty(idens));

				List<Identification> result = idens.stream()
						.filter(ident -> ident.getCompanyid() == 41 || ident.getCompanyid() == 41)
						.collect(Collectors.toList());

				assertEquals(2, result.size());
				assertEquals(41, result.get(0).getId());
				assertEquals(42, result.get(1).getId());

			}

			
		});
	}
	
	private WSResponse callAddIdentification(JsonNode i1) {
		return WS.url("http://localhost:3333/api/v1/startIdentification").post(i1).get(10000);
	}

	private WSResponse callAddCompany(JsonNode company1) {
		return WS.url("http://localhost:3333/api/v1/addCompany").post(company1).get(10000);
	}

	private WSResponse callGetPendingIdentifications() {
		return WS.url("http://localhost:3333/api/v1/identifications").get().get(10000);
	}

}
