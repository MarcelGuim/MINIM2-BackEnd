package edu.upc.dsa;

import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    final static Logger logger = Logger.getLogger(UserManagerImpl.class);

    // Singleton: única instancia de SessionManager
    private static SessionManager instance;

    // Mapa de sesiones activas, accesible de forma segura desde múltiples hilos
    private Map<String, User> activeSessions;

    // Constructor privado para impedir instanciación directa
    private SessionManager() {
        activeSessions = new HashMap<>();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void createSession(String sessionId, User user) {
        logger.info("Cookie "+sessionId+" asociada a "+user.getName());
        activeSessions.put(sessionId, user);
    }

    public User getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public void removeSession(String sessionId) {
        logger.info("Cookie de "+ this.getSession(sessionId).getName()+": "+sessionId+ " ,borrada");
        activeSessions.remove(sessionId);
    }
}

