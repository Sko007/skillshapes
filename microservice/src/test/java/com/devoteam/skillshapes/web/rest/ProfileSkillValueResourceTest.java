package com.devoteam.skillshapes.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.devoteam.skillshapes.TestUtil;
import com.devoteam.skillshapes.service.dto.ProfileSkillValueDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

    import java.util.List;

@QuarkusTest
public class ProfileSkillValueResourceTest {

    private static final TypeRef<ProfileSkillValueDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<ProfileSkillValueDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;



    String adminToken;

    ProfileSkillValueDTO profileSkillValueDTO;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config =
            RestAssured.config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfileSkillValueDTO createEntity() {
        var profileSkillValueDTO = new ProfileSkillValueDTO();
        profileSkillValueDTO.value = DEFAULT_VALUE;
        return profileSkillValueDTO;
    }

    @BeforeEach
    public void initTest() {
        profileSkillValueDTO = createEntity();
    }

    @Test
    public void createProfileSkillValue() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the ProfileSkillValue
        profileSkillValueDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(profileSkillValueDTO)
            .when()
            .post("/api/profile-skill-values")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the ProfileSkillValue in the database
        var profileSkillValueDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(profileSkillValueDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testProfileSkillValueDTO = profileSkillValueDTOList.stream().filter(it -> profileSkillValueDTO.id.equals(it.id)).findFirst().get();
        assertThat(testProfileSkillValueDTO.value).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    public void createProfileSkillValueWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the ProfileSkillValue with an existing ID
        profileSkillValueDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(profileSkillValueDTO)
            .when()
            .post("/api/profile-skill-values")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the ProfileSkillValue in the database
        var profileSkillValueDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(profileSkillValueDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkValueIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        profileSkillValueDTO.value = null;

        // Create the ProfileSkillValue, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(profileSkillValueDTO)
            .when()
            .post("/api/profile-skill-values")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the ProfileSkillValue in the database
        var profileSkillValueDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(profileSkillValueDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateProfileSkillValue() {
        // Initialize the database
        profileSkillValueDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(profileSkillValueDTO)
            .when()
            .post("/api/profile-skill-values")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the profileSkillValue
        var updatedProfileSkillValueDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values/{id}", profileSkillValueDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the profileSkillValue
        updatedProfileSkillValueDTO.value = UPDATED_VALUE;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedProfileSkillValueDTO)
            .when()
            .put("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the ProfileSkillValue in the database
        var profileSkillValueDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(profileSkillValueDTOList).hasSize(databaseSizeBeforeUpdate);
        var testProfileSkillValueDTO = profileSkillValueDTOList.stream().filter(it -> updatedProfileSkillValueDTO.id.equals(it.id)).findFirst().get();
        assertThat(testProfileSkillValueDTO.value).isEqualTo(UPDATED_VALUE);
    }

    @Test
    public void updateNonExistingProfileSkillValue() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(profileSkillValueDTO)
            .when()
            .put("/api/profile-skill-values")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the ProfileSkillValue in the database
        var profileSkillValueDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(profileSkillValueDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteProfileSkillValue() {
        // Initialize the database
        profileSkillValueDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(profileSkillValueDTO)
            .when()
            .post("/api/profile-skill-values")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the profileSkillValue
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/profile-skill-values/{id}", profileSkillValueDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var profileSkillValueDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(profileSkillValueDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllProfileSkillValues() {
        // Initialize the database
        profileSkillValueDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(profileSkillValueDTO)
            .when()
            .post("/api/profile-skill-values")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the profileSkillValueList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(profileSkillValueDTO.id.intValue()))
            .body("value", hasItem(DEFAULT_VALUE.intValue()));
    }

    @Test
    public void getProfileSkillValue() {
        // Initialize the database
        profileSkillValueDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(profileSkillValueDTO)
            .when()
            .post("/api/profile-skill-values")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the profileSkillValue
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/profile-skill-values/{id}", profileSkillValueDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the profileSkillValue
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values/{id}", profileSkillValueDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(profileSkillValueDTO.id.intValue()))
            
                .body("value", is(DEFAULT_VALUE.intValue()));
    }

    @Test
    public void getNonExistingProfileSkillValue() {
        // Get the profileSkillValue
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/profile-skill-values/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
