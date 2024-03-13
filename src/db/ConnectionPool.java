package db;

import exceptions.CLLogicException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {
    private static final Integer NUMBER_OF_CONNECTIONS = 10;
    private static volatile ConnectionPool instance = null;
    private final Stack<Connection> connections = new Stack<>();
    private Boolean isClosing=false;
    private final DBManager dbManager;

    private ConnectionPool(DBManager dbManager) throws CLLogicException {
        this.dbManager = dbManager;
            this.openAllConnections();
        }


    /**
     * Opens the maximum amount of connections and pushes them into 'connections' stack.
     * @throws CLLogicException if a connection error occurs - BDUrl not found, or access is denied - DBUsername and/or DBPassword.
     */
    private void openAllConnections() throws CLLogicException {
        isClosing = false;
        try {
            for (int counter = 0; counter < NUMBER_OF_CONNECTIONS; ++counter) {
                Connection connection;
                synchronized (DBManager.class) {
                    connection = DriverManager.getConnection(dbManager.getUrl(), dbManager.getSqlUser(), dbManager.getSqlPassword());
                }
                this.connections.push(connection);
            }
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 0 && exception.getMessage().contains("Communications link failure")) {
                throw new CLLogicException("Server not responding - check db url", 456);
            } else if (exception.getErrorCode() == 1045 && exception.getMessage().contains("Access denied")) {
                throw new CLLogicException("Access denied - invalid db username and/or password", 457);
            } else {
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
            }
        }

    }

    /**
     * Returns the initialized instance of ConnectionPool.
     * @return the initialized instance of ConnectionPool.
     * @throws CLLogicException if the connection pool is not initialized ('instance' is null).
     */
    public static ConnectionPool getInstance() throws CLLogicException {
        if (instance == null) {
            throw new CLLogicException("Connection pool not initialized",481);
        } else
            return instance;
    }

    /**
     * Closes every connection created by this instance of ConnectionPool.
     * @throws InterruptedException if a thread was interrupted.
     */
    public void closeAllConnections() throws InterruptedException {
        synchronized (this.connections) {
            isClosing = true;
            while (this.connections.size() < NUMBER_OF_CONNECTIONS) {
                this.connections.wait();
            }
            this.connections.removeAllElements();
        }
    }

    /**
     * Returns a connection - popped from 'connections' stack.
     * @return a connection.
     * @throws InterruptedException if a thread was interrupted.
     * @throws CLLogicException if this instance of ConnectionPoll has already started to close all it's connections.
     */
    public Connection getConnection() throws InterruptedException, CLLogicException {
        if (!this.isClosing) {
            synchronized (this.connections) {
                if (this.connections.isEmpty()) {
                    this.connections.wait();
                }
                return this.connections.pop();
            }
        } else {
            throw new CLLogicException("Connection pool is closing", 482);
        }
    }

    /**
     * Pushes the received connection into 'connections' stack.
     * @param connection a connection to be pushed into 'connections' stack.
     */
    public void returnConnection(Connection connection) {
        synchronized (this.connections) {
            this.connections.push(connection);
            this.connections.notify();
        }
    }

    /**
     * Creates a custom ConnectionPool instance.
     * @param dbManager a DBManager instance, containing the necessary parameters to construct the ConnectionPool instance.
     * @throws CLLogicException if a connection error occurs - BDUrl not found, or access is denied - DBUsername and/or DBPassword.
     */
    public static void initializeConnectionPool(DBManager dbManager) throws CLLogicException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool(dbManager);
                }
            }
        }
    }
}
