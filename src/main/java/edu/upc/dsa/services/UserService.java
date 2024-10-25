package edu.upc.dsa.services;
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

    public UserService() {
        this.um = UserManagerImpl.getInstance();
        if (um.size()==0) {
            this.um.addUser("Blau", "Blau2002");
            this.um.addUser("Lluc", "Falco12");
            this.um.addUser("David", "1234");
            this.um.addUser("Marcel", "1234");
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

        if (user.getUser()==null || user.getPassword()==null)  return Response.status(500).entity(user).build();
        this.um.addUser(user);
        return Response.status(201).entity(user).build();
    }

    @POST
    @ApiOperation(value = "Login a new User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response LoginUser(User user) {

        if (user.getUser()==null || user.getPassword()==null)  return Response.status(500).build();
        if(user.getPassword().equals(um.getUserFromUsername(user.getUser()).getPassword()))
            return Response.status(201).build();
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
