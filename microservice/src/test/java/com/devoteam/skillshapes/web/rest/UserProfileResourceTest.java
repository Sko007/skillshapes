package com.devoteam.skillshapes.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.devoteam.skillshapes.TestUtil;
import com.devoteam.skillshapes.service.dto.UserProfileDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

    import java.util.List;

@QuarkusTest
public class UserProfileResourceTest {

    private static final TypeRef<UserProfileDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<UserProfileDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";



    String adminToken;

    UserProfileDTO userProfileDTO;

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
    public static UserProfileDTO createEntity() {
        var userProfileDTO = new UserProfileDTO();
        userProfileDTO.firstName = DEFAULT_FIRST_NAME;
        userProfileDTO.lastName = DEFAULT_LAST_NAME;
        userProfileDTO.email = DEFAULT_EMAIL;
        return userProfileDTO;
    }

    @BeforeEach
    public void initTest() {
        userProfileDTO = createEntity();
    }

    @Test
    public void createUserProfile() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the UserProfile
        userProfileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(userProfileDTO)
            .when()
            .post("/api/user-profiles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the UserProfile in the database
        var userProfileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(userProfileDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testUserProfileDTO = userProfileDTOList.stream().filter(it -> userProfileDTO.id.equals(it.id)).findFirst().get();
        assertThat(testUserProfileDTO.firstName).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserProfileDTO.lastName).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserProfileDTO.email).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    public void createUserProfileWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the UserProfile with an existing ID
        userProfileDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(userProfileDTO)
            .when()
            .post("/api/user-profiles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the UserProfile in the database
        var userProfileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(userProfileDTOList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void updateUserProfile() {
        // Initialize the database
        userProfileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(userProfileDTO)
            .when()
            .post("/api/user-profiles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the userProfile
        var updatedUserProfileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles/{id}", userProfileDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the userProfile
        updatedUserProfileDTO.firstName = UPDATED_FIRST_NAME;
        updatedUserProfileDTO.lastName = UPDATED_LAST_NAME;
        updatedUserProfileDTO.email = UPDATED_EMAIL;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedUserProfileDTO)
            .when()
            .put("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the UserProfile in the database
        var userProfileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(userProfileDTOList).hasSize(databaseSizeBeforeUpdate);
        var testUserProfileDTO = userProfileDTOList.stream().filter(it -> updatedUserProfileDTO.id.equals(it.id)).findFirst().get();
        assertThat(testUserProfileDTO.firstName).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserProfileDTO.lastName).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserProfileDTO.email).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    public void updateNonExistingUserProfile() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
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
            .body(userProfileDTO)
            .when()
            .put("/api/user-profiles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the UserProfile in the database
        var userProfileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(userProfileDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteUserProfile() {
        // Initialize the database
        userProfileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(userProfileDTO)
            .when()
            .post("/api/user-profiles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the userProfile
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/user-profiles/{id}", userProfileDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var userProfileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(userProfileDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllUserProfiles() {
        // Initialize the database
        userProfileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(userProfileDTO)
            .when()
            .post("/api/user-profiles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the userProfileList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(userProfileDTO.id.intValue()))
            .body("firstName", hasItem(DEFAULT_FIRST_NAME))            .body("lastName", hasItem(DEFAULT_LAST_NAME))            .body("email", hasItem(DEFAULT_EMAIL));
    }

    @Test
    public void getUserProfile() {
        // Initialize the database
        userProfileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(userProfileDTO)
            .when()
            .post("/api/user-profiles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the userProfile
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/user-profiles/{id}", userProfileDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the userProfile
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles/{id}", userProfileDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(userProfileDTO.id.intValue()))
            
                .body("firstName", is(DEFAULT_FIRST_NAME))
                .body("lastName", is(DEFAULT_LAST_NAME))
                .body("email", is(DEFAULT_EMAIL));
    }

    @Test
    public void getNonExistingUserProfile() {
        // Get the userProfile
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/user-profiles/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
