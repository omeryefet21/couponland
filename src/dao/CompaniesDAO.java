package dao;

import beans.Company;
import exceptions.CLInputException;
import exceptions.CLLogicException;

import java.util.ArrayList;

public interface CompaniesDAO {

    /**
     * Checks whether a company exists on DB.
     * @param email Company email.
     * @param password Company password.
     * @return true, if a company exists on DB with the received email and password. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    Boolean isCompanyExists(String email, String password) throws CLInputException, CLLogicException;

    /**
     * Checks whether a company exists on DB.
     * @param id Company ID.
     * @return true, if a company exists on DB with the received ID. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    Boolean isCompanyExists(Integer id) throws CLInputException, CLLogicException;

    /**
     * Adds a new company to DB.
     * @param company Company instance to be added to DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if the designated company name already exists or the designated company email already exists.
     */
    void addCompany(Company company) throws CLLogicException;

    /**
     * Updates data values for an existing company on DB.
     * @param company A Company instance whose data values to overwrite existing ones on DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received id; if the received company name is different from the existing company name or the received company email already exists.
     */
    void updateCompany(Company company) throws CLLogicException;

    /**
     * Deletes a company from DB.
     * @param companyID Id of the company to be deleted.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received id.
     */
    void deleteCompany(Integer companyID) throws CLLogicException;

    /**
     * Retrieves company data for all companies found on DB.
     * @return an array list of companies.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained;
     * if no companies were found on DB.
     */
    ArrayList<Company> getAllCompanies() throws CLInputException, CLLogicException;

    /**
     * Retrieves company data from DB.
     * @param companyID Designated company's id on DB.
     * @return an instance of Company with the designated company's data.
     * @throws CLInputException if an invalid input is used creating the result object.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received id.
     */
    Company getOneCompany(Integer companyID) throws CLInputException, CLLogicException;

    /**
     * Retrieves company data from DB.
     * @param email Designated company's email on DB.
     * @param password Designated company's password on DB.
     * @return an instance of Company with the designated company's data.
     * @throws CLInputException if an invalid input is used creating the result object.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received email and password.
     */
    Company getOneCompany(String email, String password) throws CLInputException, CLLogicException;



}
