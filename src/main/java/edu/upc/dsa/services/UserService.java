package edu.upc.dsa.services;
import edu.upc.dsa.StoreManager;
import edu.upc.dsa.StoreManagerImpl;
import edu.upc.dsa.UserManager;
import edu.upc.dsa.UserManagerImpl;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/users", description = "Endpoint to Users Service")
@Path("/users")
public class UserService {
    private UserManager um;
    private StoreManager sm;
    public UserService() {
        this.um = UserManagerImpl.getInstance();
        this.sm = StoreManagerImpl.getInstance();
        if (um.size()==0) {
            User u1 = new User("Blau", "Blau2002");
            User u2 = new User("Lluc", "Falco12");
            User u3 = new User("David", "1234");
            User u4 = new User("Marcel", "1234");
            this.um.addUser(u1);
            this.um.addUser(u2);
            this.um.addUser(u3);
            this.um.addUser(u4);
            this.sm.addUser(u1);
            this.sm.addUser(u2);
            this.sm.addUser(u3);
            this.sm.addUser(u4);
        }
    }

    @DELETE
    @ApiOperation(value = "delete a User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Track not found")
    })
    @Path("/{id}")
    public Response deleteTrack(@PathParam("id") String id) {
        User u = this.um.getUser(id);
        if (u == null) return Response.status(404).build();
        else this.um.deleteUser(id);
        return Response.status(201).build();
    }

    @POST
    @ApiOperation(value = "create a new User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUser(User user) {

        if (user.getName()==null || user.getPassword()==null)  return Response.status(500).entity(user).build();
        //user.setRandomId();
        this.um.addUser(user);
        this.sm.addUser(user);
        return Response.status(201).entity(user).build();
    }

    @POST
    @ApiOperation(value = "Login a new User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response LoginUser(User user) {

        if (user.getName()==null || user.getPassword()==null)  return Response.status(500).build();
        if(user.getPassword().equals(um.getUserFromUsername(user.getName()).getPassword()))
            return Response.status(201).entity(user).build();
        else
            return Response.status(500).build();
    }

    @GET
    @ApiOperation(value = "get all Users", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer="List"),
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {

        List<User> users = this.um.findAll();

        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
        return Response.status(201).entity(entity).build()  ;

    }
}
