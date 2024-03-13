package dbdao;

import beans.Customer;
import dao.CustomersDAO;
import db.ConnectionPool;
import db.DBUtils;
import db.QueryReturnType;
import db.SQLCommands;
import exceptions.CLInputException;
import exceptions.CLLogicException;
import local.CouponlandSystem;
import utilities.GeneralUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomersDBDAO implements CustomersDAO {
    private final ConnectionPool connectionPool;
    private final SQLCommands sqlCommands;
    private final DBUtils dbUtils;

    public CustomersDBDAO() {
        this.connectionPool = CouponlandSystem.getInstance().getConnectionPool();
        this.sqlCommands = CouponlandSystem.getInstance().getLoadedSqlCommands();
        this.dbUtils = new DBUtils(this.connectionPool);
    }

    /**
     * Checks whether a customer exists on DB.
     * @param email Customer email.
     * @param password Customer password.
     * @return true, if a customer exists on DB with the received email and password. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    @Override
    public Boolean isCustomerExists(String email, String password) throws CLInputException, CLLogicException {
        try {
            return (Boolean) (dbUtils.runQueryForResultList(QueryReturnType.BOOLEAN, sqlCommands.CUSTOMER_EXISTS,
                    GeneralUtils.getParamsMap(email, password)).get(0));
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Checks whether a customer exists on DB.
     * @param id Customer ID.
     * @return true, if a customer exists on DB with the received ID. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    @Override
    public Boolean isCustomerExists(Integer id) throws CLInputException, CLLogicException {
        try {
            return (Boolean) (dbUtils.runQueryForResultList(QueryReturnType.BOOLEAN, sqlCommands.CUSTOMER_EXISTS_ID,
                    GeneralUtils.getParamsMap(id)).get(0));
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Adds a new customer to DB.
     * @param customer Customer instance to be added to DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if the designated customer email already exists.
     */
    @Override
    public void addCustomer(Customer customer) throws CLLogicException {
        try {
            dbUtils.runQueryForUpdateCount(sqlCommands.ADD_CUSTOMER, GeneralUtils.getParamsMap(customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPassword()));
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1062 && exception.getMessage().contains("email_UNIQUE")) {
                throw new CLLogicException("Customer email: " + customer.getEmail() + " already exists", 311);
            } else {
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
            }
        }
    }

    /**
     * Updates data values for an existing customer on DB.
     * @param customer A Customer instance whose data values to overwrite existing ones on DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received id; if the designated customer email already exists.
     */
    @Override
    public void updateCustomer(Customer customer) throws CLLogicException {
        try {
            synchronized (CouponlandSystem.getInstance().getLock3()) {
                if (dbUtils.runQueryForUpdateCount(sqlCommands.UPDATE_CUSTOMER,
                        GeneralUtils.getParamsMap(customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPassword(), customer.getId())) == 0) {
                    throw new CLLogicException("Customer " + customer.getId() + " not found", 146);
                }
            }
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1062 && exception.getMessage().contains("email_UNIQUE")) {
                throw new CLLogicException("Customer email: " + customer.getEmail() + " already exists", 311);
            } else {
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
            }
        }
    }

    /**
     * Deletes a customer from DB.
     * @param customerID Id of the customer to be deleted.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received id.
     */
    @Override
    public void deleteCustomer(Integer customerID) throws CLLogicException {
        try {
            synchronized (CouponlandSystem.getInstance().getLock3()) {
                if (dbUtils.runQueryForUpdateCount(sqlCommands.DELETE_CUSTOMER, GeneralUtils.getParamsMap(customerID)) == 0) {
                    throw new CLLogicException("Customer " + customerID + " not found", 146);
                }
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Returns a list of all customers found on DB.
     * @return an array list of customers.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException  if the operation was interrupted; if a connection could not be obtained;
     * if no customers were found on DB.
     */
    @Override
    public ArrayList<Customer> getAllCustomers() throws CLInputException, CLLogicException {
        try {
            List<Object> objects = dbUtils.runQueryForResultList(QueryReturnType.CUSTOMER, sqlCommands.GET_ALL_CUSTOMERS);
            if (objects.size() == 0) {
                throw new CLLogicException("No customers found", 148);
            } else {
                List<Customer> result = objects.stream().map(object -> (Customer) (object)).collect(Collectors.toList());
                return (ArrayList<Customer>) result;
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Retrieves customer data from DB.
     * @param customerID Designated customer's id on DB.
     * @return an instance of Customer with the designated customer's data.
     * @throws CLInputException if an invalid input is used creating the result object.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received id.
     */
    @Override
    public Customer getOneCustomer(Integer customerID) throws CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.CUSTOMER, sqlCommands.GET_CUSTOMER,
                    GeneralUtils.getParamsMap(customerID));
            if (result.size() == 0) {
                throw new CLLogicException("Customer " + customerID + " was not found", 146);
            } else {
                return (Customer) result.get(0);
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Retrieves customer data from DB.
     * @param email Designated customer's email on DB.
     * @param password Designated customer's password on DB.
     * @return an instance of Customer with the designated customer's data.
     * @throws CLInputException if an invalid input is used creating the result object.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received email and password.
     */
    @Override
    public Customer getOneCustomer(String email, String password) throws CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.CUSTOMER, sqlCommands.GET_CUSTOMER_BY_LOGIN,
                    GeneralUtils.getParamsMap(email, password));
            if (result.size() == 0) {
                throw new CLLogicException("Customer - email: " + email + " was not found", 146);
            } else {
                return (Customer) result.get(0);
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }
}























