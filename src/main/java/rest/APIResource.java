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
    @Path("talk")
//    @RolesAllowed({"user"})
    public String getAllTalks() {
        Set<TalkDTO> talkDTOSet = FACADE.getAllTalks();
        return GSON.toJson(talkDTOSet);

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
    //    @RolesAllowed({"admin"})
    public String createSpeaker(String speakerJSON) {
        SpeakerDTO newSpeakerDTO = GSON.fromJson(speakerJSON, SpeakerDTO.class);
        SpeakerDTO createdSpeakerDTO = FACADE.createSpeaker(newSpeakerDTO);
        return GSON.toJson(createdSpeakerDTO);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("talk")
    //    @RolesAllowed({"admin"})
    public String createTalk(String talkJSON) {
        TalkDTO newTalkDTO = GSON.fromJson(talkJSON, TalkDTO.class);
        TalkDTO createdTalkDTO = FACADE.createTalk(newTalkDTO);
        return GSON.toJson(createdTalkDTO);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("conference")
    //    @RolesAllowed({"admin"})
    public String createConference(String conferenceJSON) {
        ConferenceDTO newConferenceDTO = GSON.fromJson(conferenceJSON, ConferenceDTO.class);
        ConferenceDTO createdConferenceDTO = FACADE.createConference(newConferenceDTO);
        return GSON.toJson(createdConferenceDTO);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("talk")
    //    @RolesAllowed({"admin"})
    public String editBoat(String talkJSON) {
        TalkDTO updateCandidateDTO = GSON.fromJson(talkJSON, TalkDTO.class);
//        TODO validate input
        TalkDTO editedTalkDTO = FACADE.editTalk(updateCandidateDTO);
        return GSON.toJson(editedTalkDTO);
    }

    @DELETE
    @Path("talk")
    //    @RolesAllowed({"admin"})
    public String deleteBoat(String talkJSON){
        TalkDTO deleteDTO = GSON.fromJson(talkJSON, TalkDTO.class);
        TalkDTO deletedTalk = FACADE.deleteTalk(deleteDTO);
        return GSON.toJson(deletedTalk);
    }

}

