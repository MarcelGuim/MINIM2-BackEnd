package edu.upc.dsa.services;
import edu.upc.dsa.ItemManager;
import edu.upc.dsa.ItemManagerImpl;
import edu.upc.dsa.StoreManager;
import edu.upc.dsa.StoreManagerImpl;
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

@Api(value = "/items", description = "Endpoint to Users Service")
@Path("/items")
public class ItemService {
    private ItemManager im;
    private StoreManager sm;
    public ItemService() {
        this.im = ItemManagerImpl.getInstance();
        this.sm = StoreManagerImpl.getInstance();
        if (im.size()==0) {
            Item item1 = new Item("TrucoRumano");
            Item item2 = new Item("TrucoGitano");
            Item item3 = new Item("PelaCables2000");
            Item item4 = new Item("TrucoMurciano");
            this.im.addItem(item1);
            this.im.addItem(item2);
            this.im.addItem(item3);
            this.im.addItem(item4);
            this.sm.addItem(item1);
            this.sm.addItem(item2);
            this.sm.addItem(item3);
            this.sm.addItem(item4);
        }
    }
    @DELETE
    @ApiOperation(value = "delete an Item", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") String id) {
        Item i = this.im.getItem(id);
        if (i == null) return Response.status(404).build();
        else this.im.deleteItem(id);
        return Response.status(201).build();
    }

    @GET
    @ApiOperation(value = "get all Items", notes = "hahaha")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer="List"),
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems() {

        List<Item> items = this.im.findAll();

        GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
        return Response.status(201).entity(entity).build()  ;

    }

    @GET
    @ApiOperation(value = "get an Item", notes = "hahaha")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class),
            @ApiResponse(code = 404, message = "Track not found")
    })
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("id") String id) {
        Item i = this.im.getItem(id);
        if (i == null) return Response.status(404).build();
        else  return Response.status(201).entity(i).build();
    }

    @POST
    @ApiOperation(value = "Add a new Item", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Item.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response LoginUser(Item i) {

        if (i.getName()==null)  return Response.status(500).build();
        im.addItem(i);
        sm.addItem(i);
        return Response.status(201).entity(i).build();
    }

}
