package facades;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;

public class APIFacadeTest {
    private static EntityManagerFactory emf;
    private static APIFacade facade;

//    private Owner o1;
//    private Owner o2;
//    private Owner o3;
//    private OwnerDTO o1DTO;
//    private OwnerDTO o2DTO;
//    private OwnerDTO o3DTO;
//    private Harbour h1;
//    private Harbour h2;
//    private Harbour h3;
//    private HarbourDTO h1DTO;
//    private HarbourDTO h2DTO;
//    private HarbourDTO h3DTO;
//    private Boat b1;
//    private Boat b2;
//    private Boat b3;
//    private BoatDTO b1DTO;
//    private BoatDTO b2DTO;
//    private BoatDTO b3DTO;

    public APIFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = APIFacade.getInstance(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

//    @BeforeEach
//    public void setUp() {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//
//            em.createQuery("delete from Boat").executeUpdate();
//            em.createQuery("delete from Harbour").executeUpdate();
//            em.createQuery("delete from Owner").executeUpdate();
//            em.createQuery("delete from User").executeUpdate();
//            em.createQuery("delete from Role").executeUpdate();
//
//
//            o1 = new Owner("Skipper Bænt", "Persillehaven 40", "38383838");
//            o2 = new Owner("Skipper Niels", "Persillehaven 42", "39393939");
//            o3 = new Owner("Skipper Bente", "Persillehaven 38", "40404040");
//
//            h1 = new Harbour("Melsted Havn", "Melsted byvej", 8);
//            h2 = new Harbour("Nexø Havn", "Hovedvejen", 14);
//            h3 = new Harbour("Aakirkeby Havn", "Melsted byvej", 32);
//
//            b1 = new Boat("Boatmaster", "speeder", "Martha", "https://img.fruugo.com/product/8/58/278398588_max.jpg");
//            b2 = new Boat("Das Boot", "submarine", "Aase", "https://cdn.shopify.com/s/files/1/0626/0562/3537/products/31S6ddXfLmL.jpg?v=1659358008");
//            b3 = new Boat("Hanger", "supersize", "King Lincoln", "https://upload.wikimedia.org/wikipedia/commons/2/2d/USS_Nimitz_%28CVN-68%29.jpg");
//
//            b1.addOwner(o1);
//            b2.addOwner(o1);
//            b2.addOwner(o2);
//            b3.addOwner(o3);
//            b3.addOwner(o3);
//
//            h1.addBoat(b1);
//            h3.addBoat(b2);
//            h3.addBoat(b3);
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
//        } finally {
//            em.close();
//        }
//        o1DTO = new OwnerDTO(o1);
//        o2DTO = new OwnerDTO(o2);
//        o3DTO = new OwnerDTO(o3);
//        h1DTO = new HarbourDTO(h1);
//        h2DTO = new HarbourDTO(h2);
//        h3DTO = new HarbourDTO(h3);
//        b1DTO = new BoatDTO(b1);
//        b2DTO = new BoatDTO(b2);
//        b3DTO = new BoatDTO(b3);
//
//    }
//
//    @AfterEach
//    public void tearDown() {
////        we do this before setup, so we can see the data in workbench
//
//    }
//
//
//    @Test
//    public void getOwnerCount() throws Exception {
//        assertEquals(3, facade.getOwnerCount(), "Expects 3 rows in the database");
//    }
//
//
//    @Test
//    void getOwnerById() {
//        OwnerDTO expected = o1DTO;
//        Long id = o1DTO.getId();
//        OwnerDTO actual = facade.getOwnerById(id);
//        assertEquals(expected, actual);
//
//    }
//
//    @Test
//    void getAllOwners() {
//        Set<OwnerDTO> owners = facade.getAllOwners();
//        int expexted = 3;
//        int actual = owners.size();
//        assertEquals(actual, expexted);
//        assertThat(owners, containsInAnyOrder(o1DTO, o2DTO, o3DTO));
//
//    }
//
//    @Test
//    void getAllHarbours() {
//        Set<HarbourDTO> harbours = facade.getAllHarbours();
//        int expectedLength = 3;
//        int actualLength = harbours.size();
//        assertEquals(actualLength, expectedLength);
//        assertThat(harbours, containsInAnyOrder(h1DTO, h2DTO, h3DTO));
//
//    }
//
//    @Test
//    void getAllBoats() {
//        Set<BoatDTO> boats = facade.getAllBoats();
//        int expectedLength = 3;
//        int actualLength = boats.size();
//        assertEquals(actualLength, expectedLength);
//        assertThat(boats, containsInAnyOrder(b1DTO, b2DTO, b3DTO));
//
//    }
//
//    @Test
//    void createBoat() {
//        Boat newBoat = new Boat("NyBåd", "speedbåd", "Myggen", "http://imageurl");
//        newBoat.setHarbour(h1);
//        BoatDTO newBoatDTO = new BoatDTO(newBoat);
//        BoatDTO actual = facade.createBoat(newBoatDTO);
//
//        assertEquals(4, facade.getAllBoats().size());
//
//
//    }
}