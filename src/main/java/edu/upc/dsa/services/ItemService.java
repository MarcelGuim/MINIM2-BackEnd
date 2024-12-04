package edu.upc.dsa.services;
import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.ItemNotFoundException;
import edu.upc.dsa.exceptions.ItemRepeatedException;
import edu.upc.dsa.exceptions.UserRepeatedException;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.mail.Session;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.eclipse.persistence.expressions.ExpressionOperator.Log;

@Api(value = "/items", description = "Endpoint to Users Service")
@Path("/items")
public class ItemService {
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    private CharacterManager cm;
    public ItemService() {
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
                Item item1 = new Item("Cizalla","http://10.0.2.2:8080/itemsIcons/cizalla.png");
                Item item2 = new Item("Sierra Electrica","http://10.0.2.2:8080/itemsIcons/sierraelec.png");
                Item item3 = new Item("PelaCables2000","http://10.0.2.2:8080/itemsIcons/pelacables.png");
                Item item4 = new Item("Sierra","http://10.0.2.2:8080/itemsIcons/sierra.png");
                item1.setCost(5);
                item2.setCost(50);
                item3.setCost(500);
                item4.setCost(2000);
                this.im.addItem(item1);
                this.im.addItem(item2);
                this.im.addItem(item3);
                this.im.addItem(item4);
                this.sm.addAllItems(this.im.findAll());
                User u1 = new User("Blau", "Blau2002","maria.blau.camarasa@estudiantat.upc.edu");
                User u2 = new User("Lluc", "Falco12","joan.lluc.fernandez@estudiantat.upc.edu");
                User u3 = new User("David", "1234","emailDavid");
                User u4 = new User("Marcel", "1234","marcel.guim@estudiantat.upc.edu");
                u1.setMoney(10);
                u2.setMoney(100);
                u3.setMoney(1000);
                u4.setMoney(5000);
                try{
                    this.um.addUser(u1);
                    this.um.addUser(u2);
                    this.um.addUser(u3);
                    this.um.addUser(u4);
                    this.sm.addAllUsers(this.um.findAll());
                    this.cm.addCharacter(1,1,1,"primer",10);
                    this.cm.addCharacter(1,1,1,"segon",60);
                    this.cm.addCharacter(1,1,1,"tercer",50);
                    this.sm.addAllCharacters(this.cm.findAll());
                }
                catch(UserRepeatedException ex){

                }
                catch(ItemRepeatedException ex){

                }
            }
        }
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
    public Response LoginUser(Item i) {

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
