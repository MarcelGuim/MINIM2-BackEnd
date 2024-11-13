package edu.upc.dsa.services;
import edu.upc.dsa.StoreManager;
import edu.upc.dsa.StoreManagerImpl;
import edu.upc.dsa.UserManager;
import edu.upc.dsa.UserManagerImpl;
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
    private StoreManager sm;
    public StoreService() {
        this.sm = StoreManagerImpl.getInstance();
    }
    @POST
    @ApiOperation(value = "User buys an Item", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Item.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/register/{NameUser}/{idItem}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserBuys(@PathParam("NameUser") String NameUser, @PathParam("idItem") String idItem) {
        if(NameUser == null || idItem == null) return Response.status(500).build();
        List<Item> items = sm.BuyItemUser(idItem,NameUser);
        GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
        return Response.status(201).entity(entity).build();
    }
}
