package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ConferenceDTO;
import dtos.SpeakerDTO;
import dtos.TalkDTO;
import facades.APIFacade;
import facades.Populator;
import utils.EMF_Creator;

import javax.annotation.security.DeclareRoles;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

@Path("info")
@DeclareRoles({"user", "admin"})
public class APIResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    private static final APIFacade FACADE = APIFacade.getInstance(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Populator populator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String welcomeGreeting() {
        return "{\"msg\":\"Hello world\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("populate")
//    @RolesAllowed({"admin"})
    public String populateDB() {
        populator.populate();
        return "{\"msg\":\"DB populated\"}";

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("conference")
//    @RolesAllowed({"user"})
    public String getAllConferences() {
        Set<ConferenceDTO> conferenceDTOSet = FACADE.getAllConferences();
        return GSON.toJson(conferenceDTOSet);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("speaker")
//    @RolesAllowed({"user"})
    public String getAllSpeakers() {
        Set<SpeakerDTO> speakerDTOSet = FACADE.getAllSpeakers();
        return GSON.toJson(speakerDTOSet);

    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("speaker")
    public String createSpeaker(String speakerJSON) {
        SpeakerDTO newSpeakerDTO = GSON.fromJson(speakerJSON, SpeakerDTO.class);
        SpeakerDTO createdSpeakerDTO = FACADE.createSpeaker(newSpeakerDTO);
        return GSON.toJson(createdSpeakerDTO);
    }
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("talk")
    public String createTalk(String talkJSON) {
        TalkDTO newTalkDTO = GSON.fromJson(talkJSON, TalkDTO.class);
        TalkDTO createdTalkDTO = FACADE.createTalk(newTalkDTO);
        return GSON.toJson(createdTalkDTO);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("conference")
    public String createConference(String conferenceJSON) {
        ConferenceDTO newConferenceDTO = GSON.fromJson(conferenceJSON, ConferenceDTO.class);
        ConferenceDTO createdConferenceDTO = FACADE.createConference(newConferenceDTO);
        return GSON.toJson(createdConferenceDTO);
    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("harbour")
////    @RolesAllowed({"user"})
//    public String getAllHarbours() {
//        Set<HarbourDTO> harbourDTOSet = FACADE.getAllHarbours();
//        System.out.println("vi var i harbour endpoint");
////        List<HarbourDTO> harbourDTOList = new ArrayList<>();
////        harbourDTOList.addAll(harbourDTOSet);
////        return GSON.toJson(harbourDTOList);
//        return GSON.toJson(harbourDTOSet);
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("boat")
////    @RolesAllowed({"user"})
//    public String getAllBoats() {
//        Set<BoatDTO> boatDTOSet = FACADE.getAllBoats();
//        List<BoatDTO> boatDTOList = new ArrayList<>();
//        boatDTOList.addAll(boatDTOSet);
//        return GSON.toJson(boatDTOList);
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("harbour2")
////    @RolesAllowed({"user"})
//    public String getAllHarbours2() {
//        Set<HarbourDTO> harbourDTOSet = FACADE.getAllHarbours2();
//
////        harbourDTOList.addAll(HarbourDTOSet);
//        return GSON.toJson(harbourDTOSet);
//    }
//
//    @POST
//    @Consumes("application/json")
//    @Produces("application/json")
//    @Path("boat")
//    public String createBoat(String boatJSON) {
//        System.out.println("her har vi boatJSON:" + boatJSON);
//        BoatDTO newBoatDTO = GSON.fromJson(boatJSON, BoatDTO.class);
//        System.out.printf("her er vi kommet til efter GSON");
////        Boat newBoat = new Boat(newBoatDTO);
//        BoatDTO createdBoatDTO = FACADE.createBoat(newBoatDTO);
//        return GSON.toJson(createdBoatDTO);
//
//    }
//
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("boat/{id}")
//    public String editBoat(@PathParam("id") String id, String boatJSON) {
//        BoatDTO newBoatDTO = GSON.fromJson(boatJSON, BoatDTO.class);
////        TODO validate input
//        BoatDTO editedBoatDTO = FACADE.editBoat(newBoatDTO);
//        return GSON.toJson(editedBoatDTO);
//    }
//


}

