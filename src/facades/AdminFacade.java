package facades;

import beans.Company;
import beans.Customer;
import exceptions.CLInputException;
import exceptions.CLLogicException;
import local.CouponlandSystem;
import loginmanager.ClientType;
import loginmanager.LoginManager;
import java.util.ArrayList;
import java.util.Objects;


public class AdminFacade extends ClientFacade {

    private static final String ADMIN_EMAIL = "admin@admin.com";
    private static final String ADMIN_PASSWORD = "admin";

    public AdminFacade() {
    }
    @Override
    public Integer getClientId() {
        return -1;
    }

    @Override
    public void setClientId(String email, String password) {
    }

    /**
     * Authenticates any admin log in attempt.
     * @param email Admin email.
     * @param password Admin password.
     * @return true, if the received parameters match ADMIN_EMAIL and ADMIN_PASSWORD. otherwise - false.
     */
    @Override
    public Boolean login(String email, String password) {
        return Objects.equals(email, ADMIN_EMAIL) && Objects.equals(password, ADMIN_PASSWORD);
    }

    /**
     * Adds a new company to DB.
     * @param company Company instance to be added to DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if the designated company name already exists or the designated company email already exists.
     */
    public void addCompany(Company company) throws CLLogicException {
        companiesDAO.addCompany(company);
        System.out.println("Company added");
    }

    /**
     * Updates data values for an existing company on DB.
     * @param company A Company instance whose data values to overwrite existing ones on DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received id; if the received company name is different from the existing company name or the received company email already exists.
     */
    public void updateCompany(Company company) throws CLLogicException {
        companiesDAO.updateCompany(company);
        System.out.println("Company updated");
    }


    /**
     * Deletes a company from DB.
     * @param companyID Id of the company to be deleted.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received id; if the designated company is currently logged in.
     */
    public void deleteCompany(Integer companyID) throws CLInputException, CLLogicException {
        if (LoginManager.getInstance().isLoggedIn(ClientType.COMPANY, CouponlandSystem.getInstance().getInputValidation().validateId(companyID))) {
            throw new CLLogicException("Illegal operation - can not delete a logged-in company", 261);
        }
        companiesDAO.deleteCompany(companyID);
        System.out.println("Company deleted");
    }

    /**
     * Retrieves company data and issued coupons for all companies found on DB.
     * @return an array list of companies.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained;
     * if no companies were found on DB.
     */
    public ArrayList<Company> getAllCompanies() throws CLInputException, CLLogicException {
        ArrayList<Company> result = companiesDAO.getAllCompanies();
        for (Company company : result) {
            try {
                company.setCoupons(couponsDAO.getAllCompanyCoupons(company.getId()));
            } catch (CLLogicException exception) {
                if (exception.getErrorCode() != 142) {
                    throw exception;
                }
            }
        }
        return result;
    }

    /**
     * Retrieves company data and issued coupons.
     * @param companyID Designated company's id on DB.
     * @return an instance of Company containing the designated company's data and issued coupons.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received id.
     */
    public Company getOneCompany(Integer companyID) throws CLInputException, CLLogicException {
        Company result = companiesDAO.getOneCompany(CouponlandSystem.getInstance().getInputValidation().validateId(companyID));
        try {
            result.setCoupons(couponsDAO.getAllCompanyCoupons(companyID));
        } catch (CLLogicException exception) {
            if (exception.getErrorCode() != 142) {
                throw exception;
            }
        }
        return result;
    }

    /**
     * Adds a new customer to DB.
     * @param customer Customer instance to be added to DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if the designated customer email already exists.
     */
    public void addCustomer(Customer customer) throws CLLogicException {
        customersDAO.addCustomer(customer);
        System.out.println("Customer added");
    }

    /**
     * Updates data values for an existing customer on DB.
     * @param customer A Customer instance whose data values to overwrite existing ones on DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received id; if the designated customer email already exists.
     */
    public void updateCustomer(Customer customer) throws CLLogicException {
        customersDAO.updateCustomer(customer);
        System.out.println("Customer updated");
    }


    /**
     * Deletes a customer from DB.
     * @param customerID Id of the customer to be deleted.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received id; if the designated customer is currently logged in.
     */
    public void deleteCustomer(Integer customerID) throws CLInputException, CLLogicException {
        if (LoginManager.getInstance().isLoggedIn(ClientType.CUSTOMER, CouponlandSystem.getInstance().getInputValidation().validateId(customerID))) {
            throw new CLLogicException("Illegal operation - can not delete a logged-in customer", 262);
        }
        customersDAO.deleteCustomer(customerID);
        System.out.println("Customer deleted");
    }

    /**
     * Retrieves customer data and purchased coupons for all customers found on DB.
     * @return an array list of customers.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained;
     * if no customers were found on DB.
     */
    public ArrayList<Customer> getAllCustomers() throws CLInputException, CLLogicException {
        ArrayList<Customer> result = customersDAO.getAllCustomers();
        for (Customer customer : result) {
            try {
                customer.setCoupons(couponsDAO.getAllCustomerCoupons(customer.getId()));
            } catch (CLLogicException exception) {
                if (exception.getErrorCode() != 147) {
                    throw exception;
                }
            }
        }
        return result;
    }

    /**
     * Retrieves customer data and purchased coupons.
     * @param customerID Designated customer's id on DB.
     * @return an instance of Customer containing the designated customer's data and purchased coupons.
     * @throws CLInputException if an invalid input is used creating the result object.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no customer was found with received id.
     */
    public Customer getOneCustomer(Integer customerID) throws CLInputException, CLLogicException {
        Customer result = customersDAO.getOneCustomer(CouponlandSystem.getInstance().getInputValidation().validateId(customerID));
        try {
            result.setCoupons(couponsDAO.getAllCustomerCoupons(customerID));
        } catch (CLLogicException exception) {
            if (exception.getErrorCode() != 147) {
                throw exception;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "AdminFacade{ "+getClientId()+"}";
    }
}
