package edu.upc.dsa.services;
import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.ChangePassword;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/users", description = "Endpoint to Users Service")
@Path("/users")
public class UserService {
    //test
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
                Item item2 = new Item("Truco2");
                Item item3 = new Item("PelaCables2000");
                Item item4 = new Item("Truco3");
                item1.setCost(5);
                item2.setCost(50);
                item3.setCost(500);
                item4.setCost(2000);
                item1.setItem_url("http://147.83.7.204/itemsIcons/cizalla.png");
                item2.setItem_url("http://147.83.7.204/itemsIcons/sierraelec.png");
                item3.setItem_url("http://147.83.7.204/itemsIcons/pelacables.png");
                item4.setItem_url("http://147.83.7.204/itemsIcons/sierra.png");
                this.im.addItem(item1);
                this.im.addItem(item2);
                this.im.addItem(item3);
                this.im.addItem(item4);
                this.sm.addAllItems(this.im.findAll());
                User u1 = new User("Blau", "Blau2002","maria.blau.camarasa@estudiantat.upc.edu");
                User u2 = new User("Lluc", "Falco12","joan.lluc.fernandez@estudiantat.upc.edu");
                User u3 = new User("David", "1234","david.arenas.romero@estudiantat.upc.edu");
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
            @ApiResponse(code = 501, message = "UNAUTHORIZED")
    })
    @Path("/sessionCheck")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSession(@CookieParam("authToken") String authToken) {
        if (SessionManager.getInstance().getSession(authToken)==null)
            return Response.status(501).build() ;
        else
            return Response.status(201).build();
    }

    @GET
    @ApiOperation(value = "LogOut", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK"),
            @ApiResponse(code = 501, message = "UNAUTHORIZED")
    })
    @Path("/sessionOut")
    @Produces(MediaType.APPLICATION_JSON)
    public Response quitSession(@CookieParam("authToken") String authToken) {
        if (SessionManager.getInstance().getSession(authToken)==null)
            return Response.status(501).build() ;
        else{
            SessionManager.getInstance().removeSession(authToken);
            return Response.status(201).build();
        }
    }

    @DELETE
    @ApiOperation(value = "delete a User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/deleteUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@CookieParam("authToken") String authToken) {
        try{
            User u =SessionManager.getInstance().getSession(authToken);
            String id = this.um.getUserFromUsername(u.getName()).getId();
            this.um.deleteUser(id);
            SessionManager.getInstance().removeSession(authToken);
            return Response.status(201).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
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

        if (user.getName()==null || user.getPassword()==null || user.getCorreo()==null)  return Response.status(500).entity(user).build();
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
    @ApiOperation(value = "Get stats of user", notes = "Retrieve user stats using authToken")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "User not found")
    })
    @Path("/stats")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response GetStatsUser(@CookieParam("authToken") String authToken) {
        if (authToken == null) {
            return Response.status(500).build();
        }
        try {
            // Validar el authToken y obtener el usuario asociado
            User sessionUser = SessionManager.getInstance().getSession(authToken);
            if (sessionUser == null) {
                return Response.status(501).build();
            }

            // Obtener detalles del usuario a través del UserManager
            User u = this.um.getUserFromUsername(sessionUser.getName());
            return Response.status(201).entity(u).build();
        } catch (UserNotFoundException ex) {
            return Response.status(501).build();
        }
    }


    @POST
    @ApiOperation(value = "User Gets Multiplicador", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= String.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found")
    })
    @Path("/GetMultiplicadorForCobre/{NameUser}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserGetsMultiplicador(@PathParam("NameUser") String NameUser) {
        if(NameUser == null) return Response.status(500).build();
        try{
            User u = this.um.getUserFromUsername(NameUser);
            double precio = this.um.damePrecioCobre(u);
            return Response.status(200).entity(String.valueOf(precio)).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
    }
    @POST
    @ApiOperation(value = "User Updates Cobre", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= double.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),

    })
    @Path("/updateCobre/{NameUser}/{Cobre}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserUpdatesCobre(@PathParam("NameUser") String NameUser, @PathParam("Cobre") double Cobre) {
        if(NameUser == null || Cobre == 0) return Response.status(500).build();
        try{
            User u = this.um.getUserFromUsername(NameUser);
            this.um.updateCobre(Cobre, u);
            return Response.status(201).entity(String.valueOf(u.getCobre())).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
    }

    @POST
    @ApiOperation(value = "User sells all Cobre", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= String.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User has no cobre"),
            @ApiResponse(code = 503, message = "User has no multiplicador")

    })
    @Path("/sellCobre/{NameUser}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserSellsCobre(@PathParam("NameUser") String NameUser) {
        if(NameUser == null) return Response.status(500).build();
        try{
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
    }

    @PUT
    @ApiOperation(value = "User wants to change the password", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "Actual password incorrect"),
    })
    @Path("/ChangePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserChangePassword(ChangePassword passwords, @CookieParam("authToken") String authToken) {
        try{
            User u =SessionManager.getInstance().getSession(authToken);
            String pass = this.um.getUserFromUsername(u.getName()).getPassword();
            if (pass.equals(passwords.getActualPassword())){
                this.um.changePassword(u,passwords.getNewPassword());
                return Response.status(201).build();
            }
            else {
                return Response.status(502).build();
            }
        }
        catch (Exception e)
        {
            return Response.status(500).build();
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
