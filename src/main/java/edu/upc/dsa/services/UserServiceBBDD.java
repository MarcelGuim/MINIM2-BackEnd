package edu.upc.dsa.services;

import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.SessionBD;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/usersBD", description = "Endpoint to Users Service with Data Base")
@Path("/usersBD")
public class UserServiceBBDD {
    //test
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    private CharacterManager cm;
    private SessionManager sesm;
    final static Logger logger = Logger.getLogger(UserService.class);
    public UserServiceBBDD() {
        this.im = new ItemManagerImplBBDD();
        this.sm = StoreManagerImplBBDD.getInstance();
        this.um = UserManagerImplBBDD.getInstance();
        this.cm = CharacterManagerImplBBDD.getInstance();
        this.sesm = SessionManager.getInstance();
    }

    //PART AUTENT
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
        if (user.getName() == null || user.getPassword() == null) {
            return Response.status(500).build();
        }
        try {
            // Verificar si la contraseña es correcta
            if (user.getPassword().equals(this.um.getUserFromUsername(user.getName()).getPassword())) {

                // Crear una cookie con un identificador aleatorio o un token de sesión
                String cookieValue = generateRandomSessionId();
                NewCookie authCookie = new NewCookie(
                        "authToken",    // Nombre de la cookie
                        cookieValue,          // Valor de la cookie (puede ser un token generado o sesión)
                        "/",                  // Path donde la cookie es válida ("/" para toda la aplicación)
                        null,                 // Dominio de la cookie (null para el dominio actual)
                        "Autenticación",      // Comentario (opcional)
                        60 * 60 * 24,         // Expiración en segundos (aquí es 1 día)
                        false,                // Si debe ser solo para HTTPS (aquí false para desarrollo)
                        true                  // Hacer la cookie accesible solo en HTTP (no por JS)
                );
                SessionManager sessionManager = SessionManager.getInstance();
                sessionManager.createSession(cookieValue,user);


                // Devolver la respuesta con la cookie de autenticación
                return Response.status(201)
                        .entity(user)  // Enviar el objeto `user` en la respuesta
                        .cookie(authCookie)  // Añadir la cookie a la respuesta
                        .build();
            } else {
                return Response.status(501).build();  // Contraseña incorrecta
            }
        } catch (UserNotFoundException ex) {
            return Response.status(502).build();  // Usuario no encontrado
        }
    }

    @GET
    @ApiOperation(value = "Check Session Validity", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "AUTHORIZED"),
            @ApiResponse(code = 501, message = "UNAUTHORIZED"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/sessionCheck")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSession(@CookieParam("authToken") String authToken) {
        try{
            if (sesm.getSession(authToken)==null)
                return Response.status(501).build() ;
            else
                return Response.status(201).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "LogOut", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/sessionOut")
    @Produces(MediaType.APPLICATION_JSON)
    public Response quitSession(@CookieParam("authToken") String authToken) {
        try{
            sesm.getSession(authToken);
            SessionManager.getInstance().removeSession(authToken);
            return Response.status(201).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    //PART USERS MANAGER
    @DELETE
    @ApiOperation(value = "delete a User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),

    })
    @Path("/{userName}")
    public Response deleteUser(@PathParam("userName") String userName, @CookieParam("authToken") String authToken) {
        try{
            this.sesm.getSession(authToken);
            this.um.deleteUser(userName);
            return Response.status(201).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(404).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "create a new User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "User Exists"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
            })
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUser(User user, @CookieParam("authToken") String authToken) {

        if (user.getName()==null || user.getPassword()==null)  return Response.status(500).entity(user).build();
        try{
            this.sesm.getSession(authToken);
            this.um.addUser(user);
            this.sm.addUser(user);
            return Response.status(201).entity(user).build();
        }
        catch(UserRepeatedException ex){
            return Response.status(501).entity(user).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Users", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer="List"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@CookieParam("authToken") String authToken) {
        try{
            this.sesm.getSession(authToken);
            List<User> users = this.um.findAll();
            GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @PUT
    @ApiOperation(value = "Modifica user", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/")
    public Response updateUser(User user, @CookieParam("authToken") String authToken) {
        try{
            this.sesm.getSession(authToken);
            User u = this.um.updateUser(user);
            return Response.status(201).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(404).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "Get stats of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "Validation Error"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/stats/{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response GetStatsUser( @PathParam("userName") String userName, @CookieParam("authToken") String authToken) {
        if (userName == null)  return Response.status(500).build();
        try{
            this.sesm.getSession(authToken);
            User u = this.um.getUserFromUsername(userName);
            return Response.status(201).entity(u).build();
        }
        catch (UserNotFoundException ex){
            return Response.status(501).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "User Gets Multiplicador", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= String.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/GetMultiplicadorForCobre/{NameUser}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserGetsMultiplicador(@PathParam("NameUser") String NameUser, @CookieParam("authToken") String authToken) {
        if(NameUser == null) return Response.status(500).build();
        try{
            this.sesm.getSession(authToken);
            User u = this.um.getUserFromUsername(NameUser);
            double precio = this.um.damePrecioCobre(u);
            return Response.status(200).entity(String.valueOf(precio)).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }
    @POST
    @ApiOperation(value = "User Updates Cobre", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= double.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/updateCobre/{NameUser}/{Cobre}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserUpdatesCobre(@PathParam("NameUser") String NameUser, @PathParam("Cobre") double Cobre, @CookieParam("authToken") String authToken) {
        if(NameUser == null || Cobre == 0) return Response.status(500).build();
        try{
            this.sesm.getSession(authToken);
            User u = this.um.getUserFromUsername(NameUser);
            this.um.updateCobre(Cobre, u);
            return Response.status(201).entity(String.valueOf(u.getCobre())).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "User sells all Cobre", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= String.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User has no cobre"),
            @ApiResponse(code = 503, message = "User has no multiplicador"),
            @ApiResponse(code = 506, message = "User Not logged in yet")

    })
    @Path("/sellCobre/{NameUser}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserSellsCobre(@PathParam("NameUser") String NameUser, @CookieParam("authToken") String authToken) {
        if(NameUser == null) return Response.status(500).build();
        try{
            this.sesm.getSession(authToken);
            User u = this.um.getUserFromUsername(NameUser);
            this.um.updateMoney(u);
            return Response.status(201).entity(String.valueOf(u.getMoney())).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(UserHasNoCobreException ex){
            return Response.status(502).build();
        }
        catch(UserHasNoMultiplicadorException ex){
            return Response.status(503).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @PUT
    @ApiOperation(value = "User Wants to change password", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/ChangePassword/{UserName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserChangesPassword(String password, @PathParam("UserName") String UserName, @CookieParam("authToken") String authToken) {
        if(UserName == null|| password == null) return Response.status(500).build();
        try{
            this.sesm.getSession(authToken);
            User u = this.um.getUserFromUsername(UserName);
            this.um.changePassword(u,password);
            return Response.status(201).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "Recover Password", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "Error sending the e-mail")
    })
    @Path("/RecoverPassword/{UserName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response RecoverPassword( @PathParam("UserName") String UserName) {
        if(UserName == null)  return Response.status(500).build();
        try{
            User u = this.um.getUserFromUsername(UserName);
            this.um.RecoverPassword(u);
            return Response.status(201).build()  ;
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch (Exception ex)
        {
            return Response.status(502).build();
        }
    }

    private String generateRandomSessionId() {
        return java.util.UUID.randomUUID().toString();  // Genera un UUID aleatorio como token de sesión
    }
}
