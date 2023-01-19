package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class APIResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


//    private static Owner o1, o2, o3;
//    private static OwnerDTO o1DTO, o2DTO, o3DTO;
//    private static Harbour h1, h2, h3;
//    private static HarbourDTO h1DTO, h2DTO, h3DTO;
//    private static Boat b1, b2, b3;
//    private static BoatDTO b1DTO, b2DTO, b3DTO;

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

//    @BeforeEach
//    public void setUp() {
//        EntityManager em = emf.createEntityManager();
//
//        o1 = new Owner("Skipper Bænt", "Persillehaven 40", "38383838");
//        o2 = new Owner("Skipper Niels", "Persillehaven 42", "39393939");
//        o3 = new Owner("Skipper Bente", "Persillehaven 38", "40404040");
//
//        h1 = new Harbour("Melsted Havn", "Melsted byvej", 8);
//        h2 = new Harbour("Nexø Havn", "Hovedvejen", 14);
//        h3 = new Harbour("Aakirkeby Havn", "Melsted byvej", 32);
//
//        b1 = new Boat("Boatmaster", "speeder", "Martha", "https://img.fruugo.com/product/8/58/278398588_max.jpg");
//        b2 = new Boat("Das Boot", "submarine", "Aase", "https://cdn.shopify.com/s/files/1/0626/0562/3537/products/31S6ddXfLmL.jpg?v=1659358008");
//        b3 = new Boat("Hanger", "supersize", "King Lincoln", "https://upload.wikimedia.org/wikipedia/commons/2/2d/USS_Nimitz_%28CVN-68%29.jpg");
//
//
//        b1.addOwner(o1);
//        b2.addOwner(o1);
//        b2.addOwner(o2);
//        b3.addOwner(o3);
//        b3.addOwner(o3);
//
//        h1.addBoat(b1);
//        h3.addBoat(b2);
//        h3.addBoat(b3);
//
//        Role userRole = new Role("user");
//        Role adminRole = new Role("admin");
//        User user = new User("user", "test1");
//        user.addRole(userRole);
//        User admin = new User("admin", "test2");
//        admin.addRole(adminRole);
//        User both = new User("user_admin", "test3");
//
//        try {
//            em.getTransaction().begin();
//
//            em.createQuery("delete from Boat").executeUpdate();
//            em.createQuery("delete from Harbour").executeUpdate();
//            em.createQuery("delete from Owner").executeUpdate();
//            em.createQuery("delete from User").executeUpdate();
//            em.createQuery("delete from Role").executeUpdate();
//
//            both.addRole(userRole);
//            both.addRole(adminRole);
//            em.persist(userRole);
//            em.persist(adminRole);
//            em.persist(user);
//            em.persist(admin);
//            em.persist(both);
//
//            em.persist(o1);
//            em.persist(o2);
//            em.persist(o3);
//            em.persist(b1);
//            em.persist(b2);
//            em.persist(b3);
//            em.persist(h1);
//            em.persist(h2);
//            em.persist(h3);
//            em.getTransaction().commit();
//
//
//            o1DTO = new OwnerDTO(o1);
//            o2DTO = new OwnerDTO(o2);
//            o3DTO = new OwnerDTO(o3);
//            h1DTO = new HarbourDTO(h1);
//            h2DTO = new HarbourDTO(h2);
//            h3DTO = new HarbourDTO(h3);
//            b1DTO = new BoatDTO(b1);
//            b2DTO = new BoatDTO(b2);
//            b3DTO = new BoatDTO(b3);
//        } finally {
//            em.close();
//        }
//
//    }

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

//    @Test
//    void getAllOwners() {
//        List<OwnerDTO> ownerDTOList;
//        ownerDTOList =
//                given()
//                        .contentType("application/json")
//                        .when()
//                        .get("/boat/owner")
//                        .then().statusCode(200)
//                        .assertThat()
//                        .extract().body().jsonPath().getList("", OwnerDTO.class);
//        System.out.println("fetched data:");
//        ownerDTOList.forEach(ownerDTO -> System.out.println(ownerDTO.getId() + ": " + ownerDTO.getName()));
//
//        System.out.println("lokal data");
//        System.out.println(o1DTO.getId() + ": " + o1DTO.getName());
//        System.out.println(o2DTO.getId() + ": " + o2DTO.getName());
//        System.out.println(o3DTO.getId() + ": " + o3DTO.getName());
//        assertThat(ownerDTOList, containsInAnyOrder(o1DTO, o2DTO, o3DTO));
//
//    }
//
//
//    @Test
//    void getAllOwners2() {
//        List<OwnerDTO> ownerDTOList;
////       Set<HarbourDTOS> harbourDTOSet;
////       String jsonString =
//
//        ownerDTOList =
//                given()
//                        .contentType("application/json")
//                        .when()
//                        .get("/boat/owner")
//                        .then()
//                        .assertThat()
//                        .statusCode(HttpStatus.OK_200.getStatusCode())
//                        .extract().body().jsonPath().getList("", OwnerDTO.class);
//
////                        .extract().body().asString();
////        System.out.println(jsonString);
//
//        assertThat(ownerDTOList, containsInAnyOrder(o1DTO, o2DTO, o3DTO));
//    }
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
