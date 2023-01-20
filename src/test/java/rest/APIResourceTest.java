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

//                  add talks to speakers
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

    @Test
    void createSpeaker() {
        Speaker newSpeaker = new Speaker("Preben", "Cykelhandler", "m");
        SpeakerDTO newSpeakerDTO = new SpeakerDTO(newSpeaker);
        String requestBody = GSON.toJson(newSpeakerDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("info/speaker")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Preben"))
                .body("profession", equalTo("Cykelhandler"));
    }

    @Test
    void createTalk() {
        Talk newTalk = new Talk("Climbing", 30, "suits");

        TalkDTO newTalkDTO = new TalkDTO(newTalk);
        String requestBody = GSON.toJson(newTalkDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("info/talk")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("topic", equalTo("Climbing"))
                .body("duration", equalTo(30));
    }

    @Test
    void createConference() {
        Conference newConference = new Conference("Back to nature", "Østermarie", 30, "31-2-23", "02:00:00");
        ConferenceDTO newConferenceDTO = new ConferenceDTO(newConference);
        String requestBody = GSON.toJson(newConferenceDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("info/conference")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Back to nature"))
                .body("location", equalTo("Østermarie"))
                .body("capacity", equalTo(30));
    }

}
