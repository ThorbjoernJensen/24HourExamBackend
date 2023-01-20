package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.User;

import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import facades.UserFacade;
import utils.*;


@Path("user")
@DeclareRoles({"user", "admin"})
public class UserResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;
    @Context
    SecurityContext securityContext;
    private static final UserFacade FACADE = UserFacade.getFacadeInstance(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getWelcomeInfo() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {
        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed({"user"})
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed({"admin"})
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @POST
    @Path("signup")
    @Consumes("application/json")
    @Produces("application/json")
    public String createUser(String userJSON) { // input is the body of the request, generated in the frontend
        JsonObject json = JsonParser.parseString(userJSON).getAsJsonObject();
        String username = json.get("userName").getAsString();
        String password = json.get("userPass").getAsString();
        User user = new User(username, password);
//            if (!Objects.equals(newFullPersonDTO.getEmail(), null)
//                    && !Objects.equals(newFullPersonDTO.getFirstName(), null)
//                    && !Objects.equals(newFullPersonDTO.getLastName(), null)) {
//                Person newPerson = new Person(newFullPersonDTO);
        User createdUser = FACADE.createUser(user);

        return GSON.toJson(createdUser);
    }
}