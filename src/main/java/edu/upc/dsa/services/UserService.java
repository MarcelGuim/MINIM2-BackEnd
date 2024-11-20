package edu.upc.dsa.services;
import edu.upc.dsa.*;
import edu.upc.dsa.models.Item;
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
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    public UserService() {
        this.im = ItemManagerImpl.getInstance();
        this.sm = StoreManagerImpl.getInstance();
        this.um = UserManagerImpl.getInstance();
        if (im.size() == 0) {
            Item item1 = new Item("Truco1");
            item1.setCost(10.0);
            Item item2 = new Item("Truco2");
            item2.setCost(12.0);
            Item item3 = new Item("PelaCables2000");
            item3.setCost(75.0);
            Item item4 = new Item("Truco3");
            item4.setCost(14.0);
            this.im.addItem(item1);
            this.im.addItem(item2);
            this.im.addItem(item3);
            this.im.addItem(item4);
            this.sm.addAllItems(this.im.findAll());
            User u1 = new User("Blau", "Blau2002");
            u1.setMoney(100.0);
            User u2 = new User("Lluc", "Falco12");
            u2.setMoney(100.0);
            User u3 = new User("David", "1234");
            u3.setMoney(100.0);
            User u4 = new User("Marcel", "1234");
            u4.setMoney(100.0);
            this.um.addUser(u1);
            this.um.addUser(u2);
            this.um.addUser(u3);
            this.um.addUser(u4);
            this.sm.addAllUsers(this.um.findAll());
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

        if (user.getName() == null || user.getPassword() == null) {
            return Response.status(500).entity(user).build();
        }

        User addedUser = this.um.addUser(user);
        if (addedUser == null) {
            return Response.status(409).entity("El usuario ya existe. Por favor, elige otro nombre.").build();
        }

        this.sm.addUser(user);
        return Response.status(201).entity(addedUser).build();
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

    @PUT
    @ApiOperation(value = "Modifica user", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @Path("/")
    public Response updateTrack(User user) {
        User u = this.um.updateUser(user);
        if (u == null) return Response.status(404).build();
        return Response.status(201).build();
    }
}
