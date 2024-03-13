package dao;

import beans.Customer;
import exceptions.CLInputException;
import exceptions.CLLogicException;

import java.util.ArrayList;

public interface CustomersDAO {

    /**
     * Checks whether a customer exists on DB.
     * @param email Customer email.
     * @param password Customer password.
     * @return true, if a customer exists on DB with the received email and password. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    Boolean isCustomerExists(String email, String password) throws CLInputException, CLLogicException;

    /**
     * Checks whether a customer exists on DB.
     * @param id Customer ID.
     * @return true, if a customer exists on DB with the received ID. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    Boolean isCustomerExists(Integer id) throws CLInputException, CLLogicException;

    /**
     * Adds a new customer to DB.
     * @param customer Customer instance to be added to DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if the designated customer email already exists.
     */
    void addCustomer(Customer customer) throws CLLogicException;

    /**
     * Updates data values for an existing customer on DB.
     * @param customer A Customer instance whose data values to overwrite existing ones on DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received id; if the designated customer email already exists.
     */
    void updateCustomer(Customer customer) throws CLLogicException;

    /**
     * Deletes a customer from DB.
     * @param customerID Id of the customer to be deleted.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received id.
     */
    void deleteCustomer(Integer customerID) throws CLLogicException;

    /**
     * Returns a list of all customers found on DB.
     * @return an array list of customers.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException  if the operation was interrupted; if a connection could not be obtained;
     * if no customers were found on DB.
     */
    ArrayList<Customer> getAllCustomers() throws CLInputException, CLLogicException;

    /**
     * Retrieves customer data from DB.
     * @param customerID Designated customer's id on DB.
     * @return an instance of Customer with the designated customer's data.
     * @throws CLInputException if an invalid input is used creating the result object.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received id.
     */
    Customer getOneCustomer(Integer customerID) throws CLInputException, CLLogicException;

    /**
     * Retrieves customer data from DB.
     * @param email Designated customer's email on DB.
     * @param password Designated customer's password on DB.
     * @return an instance of Customer with the designated customer's data.
     * @throws CLInputException if an invalid input is used creating the result object.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received email and password.
     */
    Customer getOneCustomer(String email, String password) throws CLInputException, CLLogicException;

}
