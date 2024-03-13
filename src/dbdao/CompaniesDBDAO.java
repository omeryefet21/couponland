package dbdao;

import beans.Company;
import dao.CompaniesDAO;
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

public class CompaniesDBDAO implements CompaniesDAO {
    private final ConnectionPool connectionPool;
    private final SQLCommands sqlCommands;
    private final DBUtils dbUtils;

    public CompaniesDBDAO() {
        this.connectionPool = CouponlandSystem.getInstance().getConnectionPool();
        this.sqlCommands = CouponlandSystem.getInstance().getLoadedSqlCommands();
        this.dbUtils = new DBUtils(this.connectionPool);
    }

    /**
     * Checks whether a company exists on DB.
     * @param email Company email.
     * @param password Company password.
     * @return true, if a company exists on DB with the received email and password. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    @Override
    public Boolean isCompanyExists(String email, String password) throws CLInputException, CLLogicException {
        try {
            return (Boolean) (dbUtils.runQueryForResultList(QueryReturnType.BOOLEAN, sqlCommands.COMPANY_EXISTS,
                    GeneralUtils.getParamsMap(email, password)).get(0));
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Checks whether a company exists on DB.
     * @param id Company ID.
     * @return true, if a company exists on DB with the received ID. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    @Override
    public Boolean isCompanyExists(Integer id) throws CLInputException, CLLogicException {
        try {
            return (Boolean) (dbUtils.runQueryForResultList(QueryReturnType.BOOLEAN, sqlCommands.COMPANY_EXISTS_ID,
                    GeneralUtils.getParamsMap(id)).get(0));
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Adds a new company to DB.
     * @param company Company instance to be added to DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if the designated company name already exists or the designated company email already exists.
     */
    @Override
    public void addCompany(Company company) throws CLLogicException {
        try {
            dbUtils.runQueryForUpdateCount(sqlCommands.ADD_COMPANY, GeneralUtils.getParamsMap(company.getName(), company.getEmail(), company.getPassword()));
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1062 && exception.getMessage().contains("email_UNIQUE")) {
                throw new CLLogicException("Company email: " + company.getEmail() + " already exists", 311);
            } else if (exception.getErrorCode() == 1062 && exception.getMessage().contains("name_UNIQUE")) {
                throw new CLLogicException("Company name: " + company.getName() + " already exists", 312);
            } else {
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
            }
        }
    }

    /**
     * Updates data values for an existing company on DB.
     * @param company A Company instance whose data values to overwrite existing ones on DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received id; if the received company name is different from the existing company name or the received company email already exists.
     */
    @Override
    public void updateCompany(Company company) throws CLLogicException {
        try {
            synchronized (CouponlandSystem.getInstance().getLock2()) {
                if (dbUtils.runQueryForUpdateCount(sqlCommands.UPDATE_COMPANY, GeneralUtils.getParamsMap(company.getName(), company.getEmail(), company.getPassword(), company.getId())) == 0) {
                    throw new CLLogicException("Company not found", 143);
                }
            }
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1062 && exception.getMessage().contains("email_UNIQUE")) {
                throw new CLLogicException("Company email: " + company.getEmail() + " already exists", 311);
            } else if (exception.getErrorCode() == 1644 && exception.getMessage().contains("COMPANY NAME")) {
                throw new CLLogicException("Illegal operation - Can not update company name", 251);
            } else {
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
            }
        }
    }

    /**
     * Deletes a company from DB.
     * @param companyID Id of the company to be deleted.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received id.
     */
    @Override
    public void deleteCompany(Integer companyID) throws CLLogicException {
        try {
            synchronized (CouponlandSystem.getInstance().getLock1()) {
                synchronized (CouponlandSystem.getInstance().getLock2()) {
                    if (dbUtils.runQueryForUpdateCount(sqlCommands.DELETE_COMPANY, GeneralUtils.getParamsMap(companyID)) == 0) {
                        throw new CLLogicException("Company not found", 143);
                    }
                }
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Retrieves company data for all companies found on DB.
     * @return an array list of companies.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained;
     * if no companies were found on DB.
     */
    @Override
    public ArrayList<Company> getAllCompanies() throws CLInputException, CLLogicException {
        try {
            List<Object> objects = dbUtils.runQueryForResultList(QueryReturnType.COMPANY, sqlCommands.GET_ALL_COMPANIES);
            if (objects.size() == 0) {
                throw new CLLogicException("No companies found", 144);
            } else {
                List<Company> result = objects.stream().map(object -> (Company) (object)).collect(Collectors.toList());
                return (ArrayList<Company>) result;
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Retrieves company data from DB.
     * @param companyID Designated company's id on DB.
     * @return an instance of Company with the designated company's data.
     * @throws CLInputException if an invalid input is used creating the result object.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received id.
     */
    @Override
    public Company getOneCompany(Integer companyID) throws CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.COMPANY, sqlCommands.GET_COMPANY,
                    GeneralUtils.getParamsMap(companyID));
            if (result.size() == 0) {
                throw new CLLogicException("Company not found", 143);
            } else {
                return (Company) result.get(0);
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Retrieves company data from DB.
     * @param email Designated company's email on DB.
     * @param password Designated company's password on DB.
     * @return an instance of Company with the designated company's data.
     * @throws CLInputException if an invalid input is used creating the result object.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with received email and password.
     */
    @Override
    public Company getOneCompany(String email, String password) throws CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.COMPANY, sqlCommands.GET_COMPANY_BY_LOGIN,
                    GeneralUtils.getParamsMap(email, password));
            if (result.size() == 0) {
                throw new CLLogicException("Company not found", 143);
            } else {
                return (Company) result.get(0);
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }
}
