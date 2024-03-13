package loginmanager;

import exceptions.CLInputException;
import exceptions.CLLogicException;
import facades.ClientFacade;
import local.CouponlandSystem;
import validation.InputValidation;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class LoginManager {
    private static volatile LoginManager instance = null;
    private final Set<ClientFacade> loggedInUsers = new HashSet<>();

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    public Set<ClientFacade> getLoggedInUsers() {
        return loggedInUsers;
    }

    /**
     * Verifies client's existence in DB and returns the relevant ClientFacade.
     * @param email client's email.
     * @param password client's password.
     * @param clientType client's ClientType.
     * @return a ClientFacade if the login was successful, null otherwise.
     * @throws CLInputException if an invalid input is used.
     */
    public ClientFacade login(String email, String password, ClientType clientType) throws CLInputException {
        CouponlandSystem.getInstance().getInputValidation().validateEmail(email);
        CouponlandSystem.getInstance().getInputValidation().validatePassword(password);
        ClientFacade clientFacade = clientType.getClientFacade();
        try {
            if (clientFacade.login(email, password)) {
                clientFacade.setClientId(email, password);
                if (!isLoggedIn(clientType,clientFacade.getClientId())) {
                    loggedInUsers.add(clientFacade);
                    return clientFacade;
                } else {
                    throw new CLLogicException(clientType+" "+clientFacade.getClientId()+" is already logged in", 121);
                }
            } else {
                throw new CLLogicException("Incorrect username and/or password", 101);
            }
        } catch (CLLogicException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    /**
     * Checks whether a client is already logged in.
     * @param type client's ClientType.
     * @param id client's ID.
     * @return true if the client's ClientFacade is found in 'loggedInUsers'. false otherwise.
     */
    public Boolean isLoggedIn(ClientType type, Integer id) {
        return loggedInUsers.stream().
                filter(loggedInUser -> loggedInUser.getClass().equals(type.getFacadeClass())).
                filter(loggedInUser -> loggedInUser.getClientId().equals(id)).
                map(t -> true).findFirst().orElse(false);
    }

}
