package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ConferenceDTO;
import dtos.SpeakerDTO;
import dtos.TalkDTO;
import entities.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;


public class APIResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    private static Conference c1, c2, c3;
    private static ConferenceDTO c1DTO, c2DTO, c3DTO;
    private static Talk t1, t2, t3;
    private static TalkDTO t1DTO, t2DTO, t3DTO;
    private static Speaker s1, s2, s3;
    private static SpeakerDTO s1DTO, s2DTO, s3DTO;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from Speaker ").executeUpdate();
            em.createQuery("delete from Talk").executeUpdate();
            em.createQuery("delete from Conference ").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            User user = new User("user", "As123456");
            User admin = new User("admin", "JK123456");
            User both = new User("user_admin", "DQ123456");

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);

            c1 = new Conference("conference 1", "Bornholm", 10, "20-1-23", "00:08:00");
            c2 = new Conference("conference 2", "Lyngby", 10, "20-1-23", "00:09:00");
            c3 = new Conference("conference 3", "Rom", 10, "20-1-23", "00:11:00");
            s1 = new Speaker("Boris", "retired", "m");
            s2 = new Speaker("Donald", "investor", "m");
            s3 = new Speaker("Beckham", "manager", "m");
            t1 = new Talk("climate change on Bornholm", 120, "projector, whiteboard, sunscrean");
            t2 = new Talk("Ideas for a new future", 90, "Gadgetset, waterbottles");
            t3 = new Talk("Generic Ted-Talk", 20, "whiteboard");

//                   add talks to conferences
            c1.addTalk(t1);
            c3.addTalk(t2);
            c3.addTalk(t3);

// add talks to speakers
            s2.addTalk(t1);
            s2.addTalk(t2);
            s3.addTalk(t1);
            s3.addTalk(t3);

            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.persist(t1);
            em.persist(t2);
            em.persist(t3);
            em.getTransaction().commit();


            c1DTO = new ConferenceDTO(c1);
            c2DTO = new ConferenceDTO(c2);
            c3DTO = new ConferenceDTO(c3);
            s1DTO = new SpeakerDTO(s1);
            s2DTO = new SpeakerDTO(s2);
            s3DTO = new SpeakerDTO(s3);
            t1DTO = new TalkDTO(t1);
            t2DTO = new TalkDTO(t2);
            t3DTO = new TalkDTO(t3);
        } finally {
            em.close();
        }
    }

    private static void login(String username, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);
        securityToken = given().contentType("application/json").body(json).when().post("/login").then().extract().path("token");
    }

    private void logOut() {
        securityToken = null;
    }

    private static String securityToken;


    @Test
    public void testAPIResourceIsResponding() {

        given().when().get("/info").then().statusCode(200);
    }

    @Test
    public void testUserResourceIsResponding() {

        given()
                .when()
                .get("/user")
                .then()
                .statusCode(200);
    }


    @Test
    void welcomeGreeting() {
        given()
                .contentType("application/json")
                .when().get("/info")
                .then().statusCode(200)
                .assertThat()
                .body("msg", equalTo("Hello world"));
    }

    @Test
    void getAllConferences() {
        List<ConferenceDTO> conferenceDTOList;
        conferenceDTOList =
                given()
                        .contentType("application/json")
                        .when()
                        .get("/info/conference")
                        .then().statusCode(200)
                        .assertThat()
                        .extract().body().jsonPath().getList("", ConferenceDTO.class);

//        conferenceDTOList.forEach(ownerDTO -> System.out.println(ownerDTO.getId() + ": " + ownerDTO.getName()));
        assertThat(conferenceDTOList, containsInAnyOrder(c1DTO, c2DTO, c3DTO));
    }

    @Test
    void getAllSpeakers() {
        List<SpeakerDTO> speakerDTOList;
        speakerDTOList =
                given()
                        .contentType("application/json")
                        .when()
                        .get("/info/speaker")
                        .then().statusCode(200)
                        .assertThat()
                        .extract().body().jsonPath().getList("", SpeakerDTO.class);

        assertThat(speakerDTOList, containsInAnyOrder(s1DTO, s2DTO, s3DTO));
    }


//
//    @Test
//    void getAllHarbours() {
//        List<HarbourDTO> harbourDTOList;
////       Set<HarbourDTOS> harbourDTOSet;
////       String jsonString =
//
//        harbourDTOList =
//                given()
//                        .contentType("application/json")
//                        .when()
//                        .get("/boat/harbour")
//                        .then()
//                        .assertThat()
//                        .statusCode(HttpStatus.OK_200.getStatusCode())
//                        .extract().body().jsonPath().getList("", HarbourDTO.class);
//
////                        .extract().body().asString();
////        System.out.println(jsonString);
//
//        assertThat(harbourDTOList, containsInAnyOrder(h1DTO, h2DTO, h3DTO));
//
//
////        Gson gson = new Gson();
//        //        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
////        Type collectionType = new TypeToken<Collection<Integer>>(){}.getType();
////        Collection<Integer> ints2 = gson.fromJson(json, collectionType);
//    }
//
//    @Test
//    void getAllBoats() {
//        List<BoatDTO> boatDTOList;
//        boatDTOList = given()
//                .contentType("application/json")
//                .when()
//                .get("/boat/boat")
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.OK_200.getStatusCode())
//                .extract().body().jsonPath().getList("", BoatDTO.class);
//        assertThat(boatDTOList, containsInAnyOrder(b1DTO, b2DTO, b3DTO));
//
////        String jsonString = given()
////                .contentType("application/json")
////                .when()
////                .get("/boat/boat")
////                .then()
////                .assertThat()
////                .statusCode(HttpStatus.OK_200.getStatusCode())
////                .extract().body().asString();
////        System.out.println(jsonString);
//    }
//
//
//    @Test
//    void createBoat() {
//        Boat newBoat = new Boat("Testbåd", "testmodel", "Dummy", "https://img.fruugo.com/product/8/58/278398588_max.jpg");
////        newBoat.setHarbour(h1);
//        h1.addBoat(newBoat);
//        BoatDTO newBoatDTO = new BoatDTO(newBoat);
//        String requestBody = GSON.toJson(newBoatDTO);
//
//        given()
//                .header("Content-type", ContentType.JSON)
//                .and()
//                .body(requestBody)
//                .when()
//                .post("boat/boat")
//                .then()
//                .assertThat()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("brand", equalTo("Testbåd"))
//                .body("model", equalTo("testmodel"));
//
//    }
//

//    @Test //fra Jons ca2
//    public void getAllPersons(){
//        List<PersonDTO> personsDTOs;
//
//        Response response = given()
//                .when().get("/person/all")
//                .then()
//                .contentType("application/json")
//                .body("all.firstName", hasItems("Jon","Jamie","Daenerys") )
//                .extract().response();
//        System.out.println(response.asString());
//
//    }


}
