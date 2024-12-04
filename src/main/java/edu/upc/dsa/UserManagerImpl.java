package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.SessionBD;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;


public class UserManagerImpl implements UserManager {
    private static UserManager instance;
    protected HashMap<String, User> users;
    protected HashMap<String, Double> multiplicadors;
    final static Logger logger = Logger.getLogger(UserManagerImpl.class);
    private UserManagerImpl() {
        this.users = new HashMap<>();
        this.multiplicadors = new HashMap<>();
    }

    public static UserManager getInstance() {
        if (instance==null) instance = new UserManagerImpl();
        return instance;
    }

    public int size() {
        int ret = this.users.size();
        logger.info("size " + ret);

        return ret;
    }

    public User addUser(User u) throws UserRepeatedException {
        logger.info("new User " + u);
        if(users.get(u.getName()) == null && !UserWithSameEmail(u.getCorreo()))
        {
            this.users.put(u.getName(),u);
            logger.info("new User added");
            return u;
        }
        else{
            logger.warn("User already exists with that name or email already in use");
            throw new UserRepeatedException();
        }
    }

    public boolean UserWithSameEmail(String correo) {
        for (User user : users.values()) {
            if (correo.equals(user.getCorreo())) { // Compara el correo buscado con el atributo
                return true;
            }
        }
        return false;
    }

    public User addUser(String user, String password,  String mail) throws UserRepeatedException{
        return this.addUser(user, password, mail);
    }

    public User getUserFromUsername(String _username) throws UserNotFoundException{
        logger.info("getUser("+_username+")");

        for (User u: this.users.values()) {
            if (u.getName().equals(_username)) {
                logger.info("getUser("+_username+"): "+u);

                return u;
            }
        }
        logger.warn("not found " + _username);
        throw new UserNotFoundException();
    }


    public List<User> findAll() {
        List<User> resupuesta = new ArrayList<>(this.users.values());
        return resupuesta;
    }

    @Override
    public void deleteUser(String userName) throws UserNotFoundException{

        User u = this.getUserFromUsername(userName);
        if (u==null) {
            logger.warn("not found " + u);
        }
        else {
            logger.info(u+" deleted ");
            this.users.remove(u.getName());
        }
    }
    public void updateCobre(double cobre, User user)throws UserNotFoundException{
        user.setCobre(cobre + user.getCobre());
    };
    public double updateMoney(User user) throws UserHasNoCobreException, UserHasNoMultiplicadorException{
        if (multiplicadors.containsKey(user.getName())) {
            if(user.getCobre() != 0){
                double resultat = user.getMoney() + user.getCobre()*multiplicadors.get(user.getName());
                user.setMoney(resultat);
                user.setCobre(0);
                return resultat;
            }
            else throw new UserHasNoCobreException();
        }
        else throw new UserHasNoMultiplicadorException();
    };
    public double damePrecioCobre(User user){
        double random = Math.random();
        double multiplicador = 1 + Math.log(1 + (9 * random));
        double arrodonit = Math.round(multiplicador*10.0)/10.0;
        multiplicadors.put(user.getName(), arrodonit);
        return arrodonit;
    };


    @Override
    public User updateUser(User u) throws UserNotFoundException{
        User t = this.getUserFromUsername(u.getName());
        if (t!=null) {
            logger.info(u+" rebut!!!! ");
            t.setName(u.getName());
            t.setPassword(u.getPassword());
            t.setMoney(u.getMoney());
            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+u);
        }
        return t;
    }
    public void clear() {
        this.users.clear();
    }

    public void changePassword(User user, String pswd){
        User u = users.get(user.getName());
        u.setPassword(pswd);
        logger.info("User " + u + " Changed password");
    };
    public void RecoverPassword(User user) throws Exception{
        String host = "smtp.gmail.com";
        final String fromEmail = "correuperdsa@gmail.com";
        final String password = "eqqq ymrx grbg htld";
        String toEmail = user.getCorreo();

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Crear el missatge
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Recuperació de contrasenya Per RobaCobres");

        // Cos del text del correu
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Hola " + user.getName() + ",\n\nLa teva contrasenya és: " + user.getPassword() + "\n\nIntenta no tornara a oblidar-ho, olvidonaaa!");

        // Adjuntar imatge
        MimeBodyPart imagePart = new MimeBodyPart();
        File imageFile = new File("public/Olvidonaa.jpg");
        imagePart.attachFile(imageFile);
        imagePart.setFileName("Olvidonaa.jpg"); // Nom de l'arxiu al correu

        // Crear el contingut multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart); // Afegir el text
        multipart.addBodyPart(imagePart); // Afegir la imatge

        // Assignar el contingut al missatge
        message.setContent(multipart);

        // Enviar el correu
        Transport.send(message);
    }

}