package edu.upc.dsa.services;
import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Character;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.naming.Name;
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
    private CharacterManager cm;
    public StoreService() {
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
                Item item2 = new Item("Truco2");
                Item item3 = new Item("PelaCables2000");
                Item item4 = new Item("Truco3");
                item1.setCost(5);
                item2.setCost(50);
                item3.setCost(500);
                item4.setCost(2000);
                item1.setItem_url("http://10.0.2.2:8080/itemsIcons/cizalla.png");
                item2.setItem_url("http://10.0.2.2:8080/itemsIcons/sierraelec.png");
                item3.setItem_url("http://10.0.2.2:8080/itemsIcons/pelacables.png");
                item4.setItem_url("http://10.0.2.2:8080/itemsIcons/sierra.png");
                this.im.addItem(item1);
                this.im.addItem(item2);
                this.im.addItem(item3);
                this.im.addItem(item4);
                this.sm.addAllItems(this.im.findAll());
                User u1 = new User("Blau", "Blau2002","emailBlau");
                User u2 = new User("Lluc", "Falco12","joan.lluc.fernandez@estudiantat.upc.edu");
                User u3 = new User("David", "1234","emailDavid");
                User u4 = new User("Marcel", "1234","marcel.guim@estudiantat.upc.edu");
                u1.setMoney(10);
                u2.setMoney(100);
                u3.setMoney(1000);
                u4.setMoney(5000);
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

    @POST
    @ApiOperation(value = "User buys an Item", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Item.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "Item Not Found"),
            @ApiResponse(code = 503, message = "Not enough Money")

    })
    @Path("/buyItem/{NameUser}/{idItem}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserBuys(@PathParam("NameUser") String NameUser, @PathParam("idItem") String idItem) {
        if(NameUser == null || idItem == null) return Response.status(500).build();
        try{
            List<Item> items = sm.BuyItemUser(idItem,NameUser);
            GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(ItemNotFoundException ex){
            return Response.status(502).build();
        }
        catch (NotEnoughMoneyException ex){
            return Response.status(503).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Items of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User has no Items"),
    })
    @Path("Items/{NameUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@PathParam("NameUser") String NameUser) {
        if(NameUser == null) return Response.status(500).build();

        try{
            List<Item> items = this.sm.getItemUser(NameUser);
            GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(501).build();
        }
        catch(UserHasNoItemsException ex){
            return Response.status(502).build();
        }
    }
    @POST
    @ApiOperation(value = "User buys an Character", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Character.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "Character Not Found"),
            @ApiResponse(code = 503, message = "Not enough Money")

    })
    @Path("/buyCharacters/{NameUser}/{CharacterName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserBuysCharcter(@PathParam("NameUser") String NameUser, @PathParam("CharacterName") String CharacterName) {
        if(NameUser == null || CharacterName == null) return Response.status(500).build();
        try{
            List<Character> characters = sm.BuyCharacter(NameUser,CharacterName);
            GenericEntity<List<Character>> entity = new GenericEntity<List<Character>>(characters) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(CharacterNotFoundException ex){
            return Response.status(502).build();
        }
        catch (NotEnoughMoneyException ex){
            return Response.status(503).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Characters of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Character.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User has no Characters"),
    })
    @Path("Characters/{NameUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCharacters(@PathParam("NameUser") String NameUser) {
        if(NameUser == null) return Response.status(500).build();

        try{
            List<Character> characters = this.sm.getCharacterUser(NameUser);
            GenericEntity<List<Character>> entity = new GenericEntity<List<Character>>(characters) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(501).build();
        }
        catch(UserHasNoCharacterException ex){
            return Response.status(502).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Characters a user can buy", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Character.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User has not enough Money"),
    })
    @Path("CharactersUserCanBuy/{NameUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCharactersUserCanBuy(@PathParam("NameUser") String NameUser) {
        if(NameUser == null) return Response.status(500).build();
        try{
            User u = this.um.getUserFromUsername(NameUser);
            List<Character> characters = this.sm.getCharacterUserCanBuy(u);
            GenericEntity<List<Character>> entity = new GenericEntity<List<Character>>(characters) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(501).build();
        }
        catch(NotEnoughMoneyException ex){
            return Response.status(502).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Items a user can buy", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User has not enough Money"),
    })
    @Path("ItemsUserCanBuy/{NameUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemssUserCanBuy(@PathParam("NameUser") String NameUser) {
        if(NameUser == null) return Response.status(500).build();
        try{
            User u = this.um.getUserFromUsername(NameUser);
            List<Item> items = this.sm.getItemsUserCanBuy(u);
            GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(501).build();
        }
        catch(NotEnoughMoneyException ex){
            return Response.status(502).build();
        }
    }
}
