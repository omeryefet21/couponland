package local;

import beans.Category;
import db.*;
import exceptions.CLInputException;
import exceptions.CLLogicException;
import job.CouponExpirationDailyJob;
import validation.Constants;
import validation.InputValidation;
import validation.LogicValidation;

import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

public class CouponlandSystem {
    private static volatile CouponlandSystem instance = null;
    private static final Map<Integer, Installation> installations = new HashMap<>();
    private static final String FILE_PATH = "src/local/installations.bin";
    private static final File installationsFile = new File(FILE_PATH);
    private Map<Integer, Category> categoryMap;
    private ConnectionPool connectionPool = null;
    private volatile Installation loadedInstallation = null;
    private final String DATE_PATTERN = "dd/MM/yyyy";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private InputValidation inputValidation;
    private LogicValidation logicValidation;
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    private final Object lock3 = new Object();
    private static final Object lockS = new Object();
    private Thread job;
    private CouponExpirationDailyJob couponExpirationDailyJob = null;


    private static class Installation implements Serializable {
        private final DBManager dbManager;
        private final Constants constants;
        private final SQLCommands sqlCommands;

        public Installation() throws CLInputException {
            this.constants = new Constants();
            this.dbManager = new DBManager();
            this.sqlCommands = new SQLCommands(null, constants);

        }

        public Installation(String[] params) throws CLInputException {
            this.constants = new Constants();
            this.dbManager = new DBManager(params[0], params[1], params[2], params[3]);
            this.sqlCommands = new SQLCommands(params[3], constants);
        }

        public Installation(DBManager dbManager, SQLCommands sqlCommands, Constants constants) {
            this.constants = constants;
            this.dbManager = dbManager;
            this.sqlCommands = sqlCommands;

        }

        public DBManager getDbManager() {
            return dbManager;
        }

        public Constants getConstants() {
            return constants;
        }

        public SQLCommands getSqlCommands() {
            return sqlCommands;
        }


        @Override
        public String toString() {
            return "Installation{" +
                    "dbManager=" + dbManager +
                    "}\n";
        }
    }

    private CouponlandSystem() {
    }

    public Installation getLoadedInstallation() {
        return loadedInstallation;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public Map<Integer, Category> getCategoryMap() {
        return categoryMap;
    }

    public SQLCommands getLoadedSqlCommands() {
        return getLoadedInstallation().getSqlCommands();
    }

    public DBManager getLoadedDbManager() {
        return getLoadedInstallation().getDbManager();
    }

    public Constants getLoadedConstants() {
        return getLoadedInstallation().getConstants();
    }

    public InputValidation getInputValidation() {
        return inputValidation;
    }

    public LogicValidation getLogicValidation() {
        return logicValidation;
    }

    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    public static Map<Integer, Installation> getInstallations() {
        return installations;
    }

    public Object getLock1() {
        return lock1;
    }

    public Object getLock2() {
        return lock2;
    }

    public Object getLock3() {
        return lock3;
    }

    public static CouponlandSystem getInstance() {
        if (instance == null) {
            synchronized (CouponlandSystem.class) {
                if (instance == null) {
                    instance = new CouponlandSystem();
                }
            }
        }
        return instance;
    }


    /**
     * Initializes the system: assures an installations file exists and gets previously created installations - available to be loaded on the system.
     *
     * @throws CLLogicException if an I/O error occurs.
     */
    public static void initializeCouponlandSystem() throws CLLogicException {
        try {
            System.out.println(installationsFile.createNewFile() ? "File created" : "File exists");
            getInstallationsFromFile();
        } catch (IOException ioException) {
            throw new CLLogicException("I/O exception: " + ioException.getMessage(), 453);
        }
    }

    /**
     * shuts down the system: stops thread operation and closes all connections.
     *
     * @throws CLLogicException if a thread is interrupted.
     */
    public void shutDownCouponlandSystem() throws CLLogicException {
        couponExpirationDailyJob.stop();
        try {
            connectionPool.closeAllConnections();
        } catch (InterruptedException exception) {
            throw new CLLogicException("Operation interrupted - " + exception.getMessage(), 498);
        }
    }

    /**
     * Deserializes previously created installations to a static hash map 'installations'.
     *
     * @throws CLLogicException if a serialization or an I/O error occurs.
     */
    public static void getInstallationsFromFile() throws CLLogicException {
        try {
            if (installationsFile.length() != 0) {
                FileInputStream fileInputStream = new FileInputStream(installationsFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                int mapSize = (int) objectInputStream.readObject();
                for (int i = 0; i < mapSize; i++) {
                    installations.put((Integer) objectInputStream.readObject(), (Installation) objectInputStream.readObject());
                }
            } else {
                System.out.println("No installations on file");
            }
        } catch (IOException ioException) {
            throw new CLLogicException("I/O exception: " + ioException.getMessage(), 453);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new CLLogicException("Serialization error: " + classNotFoundException.getMessage(), 454);
        }
        System.out.println(installations);
    }

    /**
     * Serializes existing installations (contained in 'installations' hashmap).
     *
     * @throws CLLogicException if an I/O error occurs.
     */
    public static synchronized void saveInstallationsToFile() throws CLLogicException {
        try {
            if (installations.size() > 0) {
                FileOutputStream fileOutputStream = new FileOutputStream(installationsFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(installations.size());
                installations.forEach((key, value) -> {
                    try {
                        objectOutputStream.writeObject(key);
                        objectOutputStream.writeObject(value);
                    } catch (IOException ioException) {
                        try {
                            throw new CLLogicException("I/O exception: " + ioException.getMessage(), 453);
                        } catch (CLLogicException exception) {
                            System.out.println("Failed to save installations - please try again: " + exception.getMessage());
                        }
                    }
                });
                fileOutputStream.close();
                objectOutputStream.close();
            }
        } catch (IOException ioException) {
            throw new CLLogicException("I/O exception: " + ioException.getMessage(), 453);
        }
    }


    /**
     * Loads an Installation instance onto the current couponlandsystem instance, initiates a ConnectionPool instance,
     * maps the current categories from DB, initiates validation classes instances and the job's thread.
     * @param installation Installation instance to be loaded.
     * @throws CLInputException if an invalid input is used.
     * @throws CLLogicException if an installation is already loaded on current instance.
     */
    public void loadInstallationAndRun(Installation installation) throws CLInputException, CLLogicException {
        if (this.loadedInstallation == null) {
            synchronized (this) {
                if (loadedInstallation == null) {
                    synchronized (lockS) {
                        this.loadedInstallation = installation;
                    }
                    ConnectionPool.initializeConnectionPool(getLoadedInstallation().getDbManager());
                    this.connectionPool = ConnectionPool.getInstance();
                    this.categoryMap = new DBUtils(getConnectionPool()).mapCurrentCategories();

                    this.inputValidation = new InputValidation(installation.getConstants());
                    this.logicValidation = new LogicValidation();
                    couponExpirationDailyJob = new CouponExpirationDailyJob();
                    job = new Thread(couponExpirationDailyJob);
                    job.setDaemon(true);
                    job.start();
                }
            }
        } else {
            throw new CLLogicException("an installation is already loaded", 453);
        }
    }

    /**
     * Loads an installation by installation id.
     * @param id id of the installation instance to be loaded.
     * @throws CLInputException if an invalid input is used.
     * @throws CLLogicException if an installation is already loaded on current instance or the id entered does not exist in the key-set the installations map.
     */
    public void loadInstallationAndRun(Integer id) throws CLInputException, CLLogicException {
        if (installations.containsKey(id)) {
            loadInstallationAndRun(installations.get(id));
        } else {
            throw new CLLogicException("Installation " + id + " not found", 455);
        }
    }

    /**
     * Creates a new installation and loads it onto the current couponlandsystem instance.
     * @param params A String array containing:
     *               1.DBUrl.
     *               2.DBUsername.
     *               3.DBPassword.
     *               4.DBSchema name.
     * @throws CLInputException if an invalid input is used.
     * @throws CLLogicException if an installation is already loaded on current instance or the current installation creation attempt has failed.
     */
    public void loadInstallationAndRun(String[] params) throws CLInputException, CLLogicException {
        loadInstallationAndRun(createAndSaveInstallation(params));
    }


    /**
     * Creates a new installation, adds it to installations map, saves it to file, and creates a new schema on DB.
     * @param params  A String array containing:
     *                1.DBUrl.
     *                2.DBUsername.
     *                3.DBPassword.
     *                4.DBSchema name.
     * @return returns the id of the created installation.
     * @throws CLInputException if an invalid input is used.
     * @throws CLLogicException if the current installation creation attempt has failed or an I/O error occurs while saving to file.
     */
    public static Integer createAndSaveInstallation(String[] params) throws CLLogicException, CLInputException {
        Installation installation = new Installation(params);
        if (installOnDb(installation)) {
            int key = IntStream.range(1, installations.size() + 2).filter(num -> !installations.containsKey(num)).findFirst().orElse(0);
            installations.put(key, installation);
            saveInstallationsToFile();
            return key;
        } else {
            throw new CLLogicException("Installation creation failed", 452);
        }
    }

    /**
     * Deletes the selected installation/s from installations map and from file.
     * @param id id of installation to be deleted.
     * @param ids additional id values of installations to be deleted.
     * @throws CLLogicException if an I/O error occurs while saving to file.
     */
    public static void deleteInstallation(Integer id, Integer... ids) throws CLLogicException {
        synchronized (lockS) {
            installations.remove(id, installations.get(id));
            Arrays.stream(ids).forEach(id2 -> installations.remove(id2, installations.get(id2)));
        }
        saveInstallationsToFile();
    }

    /**
     * Updates DB connection parameters of an existing installation.
     * @param id id of installation to be updated.
     * @param params A String array containing:
     *               1.DBUrl.
     *               2.DBUsername.
     *               3.DBPassword.
     * @throws CLInputException if an invalid input is used.
     * @throws CLLogicException if a connection error occurs - BDUrl not found, or access is denied - DBUsername and/or DBPassword.
     */
    public static void updateInstallation(Integer id, String[] params) throws CLLogicException, CLInputException {
        if (installations.containsKey(id)) {
            Connection connection;
            try {
                synchronized (lockS) {
                    connection = DriverManager.getConnection(params[0], params[1], params[2]);
                    connection.close();
                    installations.get(id).getDbManager().setUrl(params[0]);
                    installations.get(id).getDbManager().setSqlUser(params[1]);
                    installations.get(id).getDbManager().setSqlPassword(params[2]);
                }
                saveInstallationsToFile();
            } catch (SQLException exception) {
                if (exception.getErrorCode() == 0 && exception.getMessage().contains("Communications link failure")) {
                    throw new CLLogicException("Server not responding - check db url", 456);
                } else if (exception.getErrorCode() == 1045 && exception.getMessage().contains("Access denied")) {
                    throw new CLLogicException("Access denied - invalid db username and/or password", 457);
                } else {
                    throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
                }
            }
        } else {
            throw new CLLogicException("Installation " + id + " not found", 455);
        }
    }

    /**
     * Creates a new schema on DB for selected installation.
     * @param installation installation for which to create a schema on DB.
     * @return true if schema creation was successful. false otherwise.
     * @throws CLLogicException if a connection error occurs - BDUrl not found, or access is denied - DBUsername and/or DBPassword.
     */
    private static Boolean installOnDb(Installation installation) throws CLLogicException {
        Connection connection;
        try {
            connection = DriverManager.getConnection(installation.dbManager.getUrl(), installation.dbManager.getSqlUser(), installation.getDbManager().getSqlPassword());
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 0 && exception.getMessage().contains("Communications link failure")) {
                throw new CLLogicException("Server not responding - check db url", 456);
            } else if (exception.getErrorCode() == 1045 && exception.getMessage().contains("Access denied")) {
                throw new CLLogicException("Access denied - invalid db username and/or password", 457);
            } else {
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
            }
        }
        try {
            ResultSet resultSet = connection.prepareStatement("select count(*) from information_schema.schemata where schema_name='" + installation.dbManager.getSchemaNAme() + "'").executeQuery();
            var ref = new Object() {
                Exception exception = null;
            };
            if (resultSet.next()) {
                if (resultSet.getInt(1) == 0) {
                    Boolean result = installation.getSqlCommands().getInstallationStack().stream().map(command -> {
                        try {
                            return connection.prepareStatement(command);
                        } catch (SQLException exception) {
                            ref.exception = exception;
                            return null;
                        }
                    }).filter(Objects::nonNull).map(statement -> {
                        try {
                            statement.execute();
                            return true;
                        } catch (SQLException | NullPointerException exception) {
                            ref.exception = exception;
                            return false;
                        }
                    }).filter(t -> !t).findFirst().orElse(true);
                    connection.close();
                    if (ref.exception != null) {
                        throw new CLLogicException("Schema creation failed: " + ref.exception.getMessage(), 499);
                    } else {
                        return result;
                    }
                } else {
                    throw new CLLogicException("Schema creation failed - schema already exists", 458);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
