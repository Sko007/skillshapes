package com.devoteam.skillshapes.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.devoteam.skillshapes.TestUtil;
import com.devoteam.skillshapes.service.dto.SkillShapeDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

    import java.util.ArrayList;
import java.util.List;

@QuarkusTest
public class SkillShapeResourceTest {

    private static final TypeRef<SkillShapeDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<SkillShapeDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";



    String adminToken;

    SkillShapeDTO skillShapeDTO;

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
    public static SkillShapeDTO createEntity() {
        var skillShapeDTO = new SkillShapeDTO();
        skillShapeDTO.title = DEFAULT_TITLE;
        skillShapeDTO.category = DEFAULT_CATEGORY;
        return skillShapeDTO;
    }

    @BeforeEach
    public void initTest() {
        skillShapeDTO = createEntity();
    }

    @Test
    public void createSkillShape() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the SkillShape
        skillShapeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(skillShapeDTO)
            .when()
            .post("/api/skill-shapes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the SkillShape in the database
        var skillShapeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(skillShapeDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testSkillShapeDTO = skillShapeDTOList.stream().filter(it -> skillShapeDTO.id.equals(it.id)).findFirst().get();
        assertThat(testSkillShapeDTO.title).isEqualTo(DEFAULT_TITLE);
        assertThat(testSkillShapeDTO.category).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    public void createSkillShapeWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the SkillShape with an existing ID
        skillShapeDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(skillShapeDTO)
            .when()
            .post("/api/skill-shapes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the SkillShape in the database
        var skillShapeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(skillShapeDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        skillShapeDTO.title = null;

        // Create the SkillShape, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(skillShapeDTO)
            .when()
            .post("/api/skill-shapes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the SkillShape in the database
        var skillShapeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(skillShapeDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkCategoryIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        skillShapeDTO.category = null;

        // Create the SkillShape, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(skillShapeDTO)
            .when()
            .post("/api/skill-shapes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the SkillShape in the database
        var skillShapeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(skillShapeDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateSkillShape() {
        // Initialize the database
        skillShapeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(skillShapeDTO)
            .when()
            .post("/api/skill-shapes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the skillShape
        var updatedSkillShapeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes/{id}", skillShapeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the skillShape
        updatedSkillShapeDTO.title = UPDATED_TITLE;
        updatedSkillShapeDTO.category = UPDATED_CATEGORY;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedSkillShapeDTO)
            .when()
            .put("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the SkillShape in the database
        var skillShapeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(skillShapeDTOList).hasSize(databaseSizeBeforeUpdate);
        var testSkillShapeDTO = skillShapeDTOList.stream().filter(it -> updatedSkillShapeDTO.id.equals(it.id)).findFirst().get();
        assertThat(testSkillShapeDTO.title).isEqualTo(UPDATED_TITLE);
        assertThat(testSkillShapeDTO.category).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    public void updateNonExistingSkillShape() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
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
            .body(skillShapeDTO)
            .when()
            .put("/api/skill-shapes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the SkillShape in the database
        var skillShapeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(skillShapeDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSkillShape() {
        // Initialize the database
        skillShapeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(skillShapeDTO)
            .when()
            .post("/api/skill-shapes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the skillShape
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/skill-shapes/{id}", skillShapeDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var skillShapeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(skillShapeDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllSkillShapes() {
        // Initialize the database
        skillShapeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(skillShapeDTO)
            .when()
            .post("/api/skill-shapes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the skillShapeList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(skillShapeDTO.id.intValue()))
            .body("title", hasItem(DEFAULT_TITLE))            .body("category", hasItem(DEFAULT_CATEGORY));
    }

    @Test
    public void getSkillShape() {
        // Initialize the database
        skillShapeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(skillShapeDTO)
            .when()
            .post("/api/skill-shapes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the skillShape
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/skill-shapes/{id}", skillShapeDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the skillShape
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes/{id}", skillShapeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(skillShapeDTO.id.intValue()))
            
                .body("title", is(DEFAULT_TITLE))
                .body("category", is(DEFAULT_CATEGORY));
    }

    @Test
    public void getNonExistingSkillShape() {
        // Get the skillShape
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/skill-shapes/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
