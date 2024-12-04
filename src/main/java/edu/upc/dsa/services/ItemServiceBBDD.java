package edu.upc.dsa.services;
import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.ItemNotFoundException;
import edu.upc.dsa.exceptions.UserRepeatedException;
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

@Api(value = "/itemsBBDD", description = "Endpoint to Items Service with Data Base")
@Path("/itemsBBDD")
public class ItemServiceBBDD {
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    private CharacterManager cm;
    public ItemServiceBBDD() {
        this.im = new ItemManagerImplBBDD();
        this.sm = StoreManagerImplBBDD.getInstance();
        this.um = UserManagerImplBBDD.getInstance();
        this.cm = CharacterManagerImplBBDD.getInstance();

    }
    @DELETE
    @ApiOperation(value = "delete an Item", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @Path("/{ItemName}")
    public Response deleteItem(@PathParam("ItemName") String ItemName) {
        try{
            this.im.deleteItem(ItemName);
            return Response.status(201).build();
        }
        catch(ItemNotFoundException ex){
            return Response.status(404).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Items", notes = "hahaha")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer="List"),
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems(@CookieParam("authToken") String authToken) {

        List<Item> items = this.im.findAll();
        //TREUREHO ES UN TEST
        User u=SessionManager.getInstance().getSession(authToken);
        System.out.println(u.getName());
        GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
        return Response.status(201).entity(entity).build()  ;

    }

    @GET
    @ApiOperation(value = "get an Item", notes = "hahaha")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class),
            @ApiResponse(code = 404, message = "Track not found")
    })
    @Path("/{ItemName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("ItemName") String ItemName) {
        try{
            Item i = this.im.getItem(ItemName);
            return Response.status(201).entity(i).build();
        }
        catch(ItemNotFoundException ex){
            return Response.status(404).build();
        }
    }

    @POST
    @ApiOperation(value = "Add a new Item", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Item.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AddNewItem(Item i) {
        if (i.getName()==null)  return Response.status(500).build();
        im.addItem(i);
        sm.addItem(i);
        return Response.status(201).entity(i).build();
    }

    @PUT
    @ApiOperation(value = "Aplica descuento/modifica Item", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @Path("/")
    public Response updateTrack(Item item) {
        try{
            Item i = this.im.updateItem(item);
            return Response.status(201).build();
        }
        catch(ItemNotFoundException ex){
            return Response.status(404).build();
        }
    }
}
