package facades;

import dtos.ConferenceDTO;
import dtos.SpeakerDTO;
import dtos.TalkDTO;
import entities.Conference;
import entities.Speaker;
import entities.Talk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class APIFacade {
    private static APIFacade instance;
    private static EntityManagerFactory emf;

    private APIFacade() {
    }


    public static APIFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new APIFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public long getConfereceCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long conferenceCount = (long) em.createQuery("SELECT COUNT(c) FROM Conference c").getSingleResult();
            return conferenceCount;
        } finally {
            em.close();
        }
    }

    public Set<ConferenceDTO> getAllConferences() {
        EntityManager em = getEntityManager();
        Set<ConferenceDTO> conferenceDTOSet = new HashSet<>();
        try {
            TypedQuery<Conference> query = em.createQuery("SELECT c FROM Conference c", Conference.class);
            List<Conference> conferences = query.getResultList();
            if (conferences.size() == 0) {
                throw new WebApplicationException("No conferences found", 404);
            }
            return ConferenceDTO.makeDTOSet(conferences);
        } finally {
            em.close();
        }
    }


    public Set<SpeakerDTO> getAllSpeakers() {
        EntityManager em = getEntityManager();
        Set<SpeakerDTO> speakerDTOSet = new HashSet<>();
        try {
            TypedQuery<Speaker> query = em.createQuery("SELECT s FROM Speaker s", Speaker.class);
            List<Speaker> speakers = query.getResultList();
            if (speakers.size() == 0) {
                throw new WebApplicationException("No speakers found", 404);
            }
            return SpeakerDTO.makeDTOSet(speakers);
        } finally {
            em.close();
        }
    }

    public Set<TalkDTO> getAllTalks() {
        EntityManager em = getEntityManager();
        Set<TalkDTO> talkDTOSet = new HashSet<>();
        try {
            TypedQuery<Talk> query = em.createQuery("SELECT t FROM Talk t", Talk.class);
            List<Talk> talks = query.getResultList();
            if (talks.size() == 0) {
                throw new WebApplicationException("No talks found", 404);
            }
            return TalkDTO.makeDTOSet(talks);
        } finally {
            em.close();
        }
    }

    public SpeakerDTO createSpeaker(SpeakerDTO newSpeakerDTO) {

//        System.out.println("fra facade create boat: " + newBoatDTO.getOwners());
        Speaker newSpeaker = new Speaker(newSpeakerDTO);

        EntityManager em = emf.createEntityManager();
        if ((newSpeaker.getName().length() == 0) || (newSpeaker.getProfession().length() == 0)) {
            throw new WebApplicationException("Name and/or profession is missing", 400);
        }


//        TODO check for duplicates

        try {
            em.getTransaction().begin();
//            harbour = em.find(Harbour.class, newBoat.getHarbour().getId());
//            harbour.addBoat(newBoat);

            em.persist(newSpeaker);
            em.getTransaction().commit();
            return new SpeakerDTO(newSpeaker);
        } finally {
            em.close();
        }


    }

    public TalkDTO createTalk(TalkDTO newTalkDTO) {
        Talk newTalk = new Talk(newTalkDTO);
        Conference conference;

        EntityManager em = emf.createEntityManager();
        if (newTalk.getConference() != null) {
            conference = em.find(Conference.class, newTalk.getConference().getId());
            conference.addTalk(newTalk);
        }

        if (newTalk.getTopic().length() == 0) {
            throw new WebApplicationException("Topic is missing", 400);
        }
//        TODO check for duplicates


        try {
            em.getTransaction().begin();
//            harbour = em.find(Harbour.class, newBoat.getHarbour().getId());
//            harbour.addBoat(newBoat);

            em.persist(newTalk);
            em.getTransaction().commit();
            return new TalkDTO(newTalk);
        } finally {
            em.close();
        }
    }

    public ConferenceDTO createConference(ConferenceDTO newConferenceDTO) {
        Conference newConference = new Conference(newConferenceDTO);
        EntityManager em = emf.createEntityManager();
        if (newConference.getName().length() == 0) {
            throw new WebApplicationException("Name is missing", 400);
        }

//        TODO check for duplicates

        try {
            em.getTransaction().begin();
//            harbour = em.find(Harbour.class, newBoat.getHarbour().getId());
//            harbour.addBoat(newBoat);

            em.persist(newConference);
            em.getTransaction().commit();
            return new ConferenceDTO(newConference);
        } finally {
            em.close();
        }
    }


    //    public Set<OwnerDTO> getAllOwners() {
//        EntityManager em = emf.createEntityManager();
//        try {
//            TypedQuery<Owner> query = em.createQuery("SELECT o FROM Owner o", Owner.class);
//            List<Owner> owners = query.getResultList();
//            Set<OwnerDTO> ownerDTOList = OwnerDTO.makeDTOSet(owners);
////        return RenameMeDTO.getDtos(rms);
//            return ownerDTOList;
//        } finally {
//            em.close();
//        }
//    }


//    @Override
//    public PersonsDTO getAllPersons() {
//        EntityManager em = getEntityManager();
//        try{
//            return new PersonsDTO(em.createNamedQuery("Person.getAllRows").getResultList());
//        }finally{
//            em.close();
//        }
//    }
//


//    public OwnerDTO getOwnerById(long id) { //throws RenameMeNotFoundException {
//        EntityManager em = emf.createEntityManager();
//        try {
//            Owner owner = em.find(Owner.class, id);
////        if (rm == null)
////            throw new RenameMeNotFoundException("The RenameMe entity with ID: "+id+" Was not found");
////        return new RenameMeDTO(rm);
//            return new OwnerDTO(owner);
//        } finally {
//            em.close();
//        }
//    }
//

//

//
//    public Set<HarbourDTO> getAllHarbours() {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            TypedQuery<Harbour> query = em.createQuery("SELECT h FROM Harbour h", Harbour.class);
//            List<Harbour> harbours = query.getResultList();
//            harbours.forEach(harbour -> harbour.getBoats().forEach(boat -> System.out.println("boat:" + boat.getId())));
////        List<HarbourDTO> harbourDTOList = HarbourDTO.makeDTOSet(harbours);
//            Set<HarbourDTO> harbourDTOList = HarbourDTO.makeDTOSet(harbours);
//            System.out.println("fra facade: ");
//            harbourDTOList.forEach(harbourDTO -> System.out.println(harbourDTO.toString()));
//            em.getTransaction().commit();
//            return harbourDTOList;
//        } finally {
//            em.close();
//        }
//    }
//
//    public Set<HarbourDTO> getAllHarbours2() {
//        EntityManager em = emf.createEntityManager();
//        try {
//            TypedQuery<Harbour> query = em.createQuery("Select h From Harbour h LEFT JOIN FETCH h.boats", Harbour.class);
////            ist<Author> authors = em.createQuery("SELECT a FROM Author a LEFT JOIN FETCH a.books", Author.class).getResultList();
//            List<Harbour> harbourList = query.getResultList();
//            harbourList.forEach(harbour -> harbour.getBoats().forEach(boat -> System.out.println("boat:" + boat.getId())));
//            return HarbourDTO.makeDTOSet(harbourList);
//        } finally {
//            em.close();
//        }
//    }
//
//
//    public Set<BoatDTO> getAllBoats() {
//        EntityManager em = emf.createEntityManager();
//        try {
//
//            TypedQuery<Boat> query = em.createQuery("SELECT b FROM Boat b", Boat.class);
//            List<Boat> boatList = query.getResultList();
//            Set<BoatDTO> boatDTOSet = BoatDTO.makeDTOSet(boatList);
//            return boatDTOSet;
//        } finally {
//            em.close();
//        }
//    }
//
//    public Owner create(Owner owner) {
//        Owner newOwner = owner;
//
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            em.persist(newOwner);
//            em.getTransaction().commit();
//            return owner;
//        } finally {
//            em.close();
//        }
////        return new RenameMeDTO(rme);
//    }
//
//    public BoatDTO createBoat(BoatDTO newBoatDTO) {
//        System.out.println("fra facade create boat: " + newBoatDTO.getOwners());
//        Boat newBoat = new Boat(newBoatDTO);
//        Harbour harbour;
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            harbour = em.find(Harbour.class, newBoat.getHarbour().getId());
//            harbour.addBoat(newBoat);
//
//            em.persist(newBoat);
//            em.getTransaction().commit();
//            return new BoatDTO(newBoat);
//        } finally {
//            em.close();
//            System.out.println("vi nåede til em close i create boat");
//        }
//
////        em.getTransaction().begin();
////        updateAddress(newPerson, em);
////        newPerson.getPhone().forEach(em::persist);
////        em.persist(newPerson);
////        em.getTransaction().commit();
////        return newPerson;
//
//    }
//
//    public BoatDTO editBoat(BoatDTO newBoatDTO) {
//        EntityManager em = getEntityManager();
//        Boat oldBoat;
//        Harbour newHarbour;
//        Set<OwnerDTO> newOwnerDTOs = newBoatDTO.getOwners();
//        Set<Owner> newOwners = new HashSet<>();
//
//        try {
//            em.getTransaction().begin();
//            oldBoat = em.find(Boat.class, newBoatDTO.getId());
//
//            oldBoat.setBrand(newBoatDTO.getBrand());
//            oldBoat.setName(newBoatDTO.getName());
//            oldBoat.setImage(newBoatDTO.getImage());
//            oldBoat.setModel(newBoatDTO.getModel());
//
////         Frontend er implementeret så man i princippet (via dropdowns) kun kan vælge havne som findes i backend.
//            newHarbour = em.find(Harbour.class, newBoatDTO.getHarbour().getId());
//            oldBoat.setHarbour(newHarbour);
//
////            Tilsvarende kan der fra frontend kun vælges owners der er i db, og update består i at erstatte bådens set af owners
////          der oprettes en managed set af owners
//            newOwnerDTOs.forEach(ownerDTO -> newOwners.add(em.find(Owner.class, ownerDTO.getId())));
//
////            em.detach(oldBoat);
//            oldBoat.getOwners().clear();
//            for (Owner o : newOwners) {
//                oldBoat.addOwner(o);
//            }
//            em.merge(oldBoat);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//        return new BoatDTO(oldBoat);
//    }
}





