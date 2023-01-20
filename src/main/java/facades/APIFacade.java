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
        Speaker newSpeaker = new Speaker(newSpeakerDTO);
        EntityManager em = emf.createEntityManager();
        if ((newSpeaker.getName().length() == 0) || (newSpeaker.getProfession().length() == 0)) {
            throw new WebApplicationException("Name and/or profession is missing", 400);
        }
//        TODO check for duplicates

        try {
            em.getTransaction().begin();
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
            em.persist(newConference);
            em.getTransaction().commit();
            return new ConferenceDTO(newConference);
        } finally {
            em.close();
        }
    }

    public TalkDTO editTalk(TalkDTO updateCandidateDTO) {
        EntityManager em = getEntityManager();
        Talk oldTalk;
        Conference newConference;
//        Set<SpeakerDTO> newSpeakerDTOs = updateCandidateDTO.getSpeakers();
        Set<Speaker> newSpeakers = new HashSet<>();

        try {
            em.getTransaction().begin();
            oldTalk = em.find(Talk.class, updateCandidateDTO.getId());

            oldTalk.setTopic(updateCandidateDTO.getTopic());
            oldTalk.setDuration(updateCandidateDTO.getDuration());
            oldTalk.setPropsList(updateCandidateDTO.getPropsList());

//         Frontend er implementeret så man i princippet (via dropdowns) kun kan vælge Conferencer som findes i db.
//            newConference = em.find(Conference.class, updateCandidateDTO.getConference().getId());
//            oldTalk.setConference(newConference);

//            Tilsvarende kan der fra frontend kun vælges speakers der er i db, og update kan bestå i at erstatte talks set af speakers
//
//            newSpeakerDTOs.forEach(speakerDTO -> newSpeakers.add(em.find(Speaker.class, speakerDTO.getId())));
//            oldTalk.getSpeakers().clear();
//            for (Speaker s : newSpeakers) {
//                oldTalk.addSpeaker(s);
//            }
            em.merge(oldTalk);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new TalkDTO(oldTalk);


    }

    public TalkDTO deleteTalk(TalkDTO deleteDTO) {
        EntityManager em = getEntityManager();
        int id = deleteDTO.getId();
        Talk talk = em.find(Talk.class, id);
        System.out.println(talk.toString());

        if (talk == null) {
            throw new WebApplicationException(String.format("Talk with id: (%d) is not in db", id), 404);
        }

        if (talk.getSpeakers().size() > 0) {
            talk.getSpeakers().forEach(speaker -> {
                speaker.getTalks().remove(talk);
                em.merge(speaker);
            });
        }

        if (talk.getConference() != null) {
            talk.getConference().removeTalk(talk);
        }
        try {
            em.getTransaction().begin();
            em.remove(talk);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        return new TalkDTO(talk);
    }
}






