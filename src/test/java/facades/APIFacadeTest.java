package facades;

import dtos.ConferenceDTO;
import dtos.SpeakerDTO;
import dtos.TalkDTO;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APIFacadeTest {
    private static EntityManagerFactory emf;
    private static APIFacade facade;


    private Conference c1;
    private Conference c2;
    private Conference c3;
    private ConferenceDTO c1DTO;
    private ConferenceDTO c2DTO;
    private ConferenceDTO c3DTO;
    private Talk t1;
    private Talk t2;
    private Talk t3;
    private TalkDTO t1DTO;
    private TalkDTO t2DTO;
    private TalkDTO t3DTO;
    private Speaker s1;
    private Speaker s2;
    private Speaker s3;
    private SpeakerDTO s1DTO;
    private SpeakerDTO s2DTO;
    private SpeakerDTO s3DTO;


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

    @BeforeEach
    public void setUp() {

        EntityManager em = emf.createEntityManager();
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

        //       add talks to conferences
        c1.addTalk(t1);
        c3.addTalk(t2);
        c3.addTalk(t3);

//              add talks to speakers
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
        em.close();


        c1DTO = new ConferenceDTO(c1);
        c2DTO = new ConferenceDTO(c2);
        c3DTO = new ConferenceDTO(c3);
        s1DTO = new SpeakerDTO(s1);
        s2DTO = new SpeakerDTO(s2);
        s3DTO = new SpeakerDTO(s3);
        t1DTO = new TalkDTO(t1);
        t2DTO = new TalkDTO(t2);
        t3DTO = new TalkDTO(t3);
    }

    @AfterEach
    public void tearDown() {
//        we do this before setup, so we can see data in workbench between runs
    }


    @Test
    public void getOwnerCount() throws Exception {
        assertEquals(3, facade.getConfereceCount(), "Expects 3 rows in the database");
    }

    @Test
    void getAllConferences() {
        Set<ConferenceDTO> owners = facade.getAllConferences();
        int expexted = 3;
        int actual = owners.size();
        assertEquals(actual, expexted);
        assertThat(owners, containsInAnyOrder(c1DTO, c2DTO, c3DTO));
    }

    @Test
    void getAllSpeakers() {
        Set<SpeakerDTO> speakers = facade.getAllSpeakers();
        int expexted = 3;
        int actual = speakers.size();
        assertEquals(actual, expexted);
        assertThat(speakers, containsInAnyOrder(s1DTO, s2DTO, s3DTO));

    }

    @Test
    void createSpeaker() {
        Speaker newSpeaker = new Speaker("Preben", "Cykelhandler", "m");
        SpeakerDTO newSpeakerDTO = new SpeakerDTO(newSpeaker);
        SpeakerDTO createdDTO = facade.createSpeaker(newSpeakerDTO);
        assertEquals(4, facade.getAllSpeakers().size());
        Set<SpeakerDTO> speakers = facade.getAllSpeakers();
        assertThat(speakers, containsInAnyOrder(s1DTO, s2DTO, s3DTO, createdDTO));
    }

    @Test
    void createTalk() {
        Talk newTalk = new Talk("Climbing", 30, "suits");
        TalkDTO newTalkDTO = new TalkDTO(newTalk);
        TalkDTO createdDTO = facade.createTalk(newTalkDTO);
        assertEquals(4, facade.getAllTalks().size());
        Set<TalkDTO> talks = facade.getAllTalks();
        assertThat(talks, containsInAnyOrder(t1DTO, t2DTO, t3DTO, createdDTO));
    }

    @Test
    void createConference() {
        Conference newConference = new Conference("Back to nature", "Østermarie", 30, "31-2-23", "02:00:00");
        ConferenceDTO newConferenceDTO = new ConferenceDTO(newConference);
        ConferenceDTO createdDTO = facade.createConference(newConferenceDTO);

        assertEquals(4, facade.getAllConferences().size());
        Set<ConferenceDTO> talks = facade.getAllConferences();
        assertThat(talks, containsInAnyOrder(c1DTO, c2DTO, c3DTO, createdDTO));
    }

}

