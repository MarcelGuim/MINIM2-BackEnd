package edu.upc.dsa.services;
import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.HashMissingException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.exceptions.UserRepeatedException;
import edu.upc.dsa.exceptions.WrongPasswordException;
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
    private CharacterManager cm;
    public UserService() {
        this.im = ItemManagerImpl.getInstance();
        this.sm = StoreManagerImpl.getInstance();
        this.um = UserManagerImpl.getInstance();
        this.cm = CharacterManagerImpl.getInstance();
        if (im.size()==0) {
            this.im = ItemManagerImpl.getInstance();
            this.sm = StoreManagerImpl.getInstance();
            this.um = UserManagerImpl.getInstance();
            this.cm = CharacterManagerImpl.getInstance();
            if (im.size() == 0) {
                Item item1 = new Item("Truco1");
                item1.setCost(10);
                Item item2 = new Item("Truco2");
                Item item3 = new Item("PelaCables2000");
                Item item4 = new Item("Truco3");
                this.im.addItem(item1);
                this.im.addItem(item2);
                this.im.addItem(item3);
                this.im.addItem(item4);
                this.sm.addAllItems(this.im.findAll());
                User u1 = new User("Blau", "Blau2002");
                User u2 = new User("Lluc", "Falco12");
                User u3 = new User("David", "1234");
                u3.setMoney(100);
                User u4 = new User("Marcel", "1234");
                u4.setMoney(50);
                this.cm.addCharacter(1,1,1,"primer",10);
                this.cm.addCharacter(1,1,1,"segon",60);
                this.cm.addCharacter(1,1,1,"tercer",50);
                this.sm.addAllCharacters(this.cm.findAll());
                try{
                    this.um.addUser(u1);
                    this.um.addUser(u2);
                    this.um.addUser(u3);
                    this.um.addUser(u4);
                    this.sm.addAllUsers(this.um.findAll());
                }
                catch(UserRepeatedException ex){

                }
            }
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
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "User Exists")

    })
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUser(User user) {

        if (user.getName()==null || user.getPassword()==null)  return Response.status(500).entity(user).build();
        //user.setRandomId();
        try{
            this.um.addUser(user);
            this.sm.addUser(user);
            return Response.status(201).entity(user).build();
        }
        catch(UserRepeatedException ex){
            return Response.status(501).entity(user).build();
        }
    }

    @POST
    @ApiOperation(value = "Login a new User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "Wrong Password"),
            @ApiResponse(code = 502, message = "User Not Found"),
    })
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response LoginUser(User user) {
        if (user.getName()==null || user.getPassword()==null)  return Response.status(500).build();
        try{
            if(user.getPassword().equals(this.um.getUserFromUsername(user.getName()).getPassword()))
                return Response.status(201).entity(user).build();
            else
                return Response.status(501).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(502).build();
        }
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

    @POST
    @ApiOperation(value = "Get stats of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "Validation Error")

    })
    @Path("/stats/{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response GetStatsUser( @PathParam("userName") String userName) {
        if (userName == null)  return Response.status(500).build();
        try{
            User u = this.um.getUserFromUsername(userName);
            return Response.status(201).entity(u).build();

        }
        catch (UserNotFoundException ex){
            return Response.status(501).build();
        }
    }
}
