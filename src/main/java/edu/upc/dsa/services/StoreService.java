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

@Api(value = "/store", description = "Endpoint to Store Service")
@Path("/store")
public class StoreService {
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    public StoreService() {
        this.im = ItemManagerImpl.getInstance();
        this.sm = StoreManagerImpl.getInstance();
        this.um = UserManagerImpl.getInstance();
        if (im.size() == 0) {
            Item item1 = new Item("Truco1");
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
            User u4 = new User("Marcel", "1234");
            this.um.addUser(u1);
            this.um.addUser(u2);
            this.um.addUser(u3);
            this.um.addUser(u4);
            this.sm.addAllUsers(this.um.findAll());
        }
    }

    @POST
    @ApiOperation(value = "User buys an Item", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Item.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "You are poor")


    })
    @Path("/register/{NameUser}/{idItem}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserBuys(@PathParam("NameUser") String NameUser, @PathParam("idItem") String idItem) {
        if(NameUser == null || idItem == null) return Response.status(500).build();
        List<Item> items = sm.BuyItemUser(idItem,NameUser);
        if(items == null) return Response.status(501).build();
        GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
        return Response.status(201).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all Items of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer="List"),
    })
    @Path("/{NameUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@PathParam("NameUser") String NameUser) {
        List<Item> items = this.sm.getItemUser(NameUser);
        GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
        return Response.status(201).entity(entity).build()  ;
    }
}
