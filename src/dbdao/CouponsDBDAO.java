package dbdao;

import beans.Coupon;
import dao.CouponsDAO;
import db.ConnectionPool;
import db.DBUtils;
import db.QueryReturnType;
import db.SQLCommands;
import exceptions.CLInputException;
import exceptions.CLLogicException;
import local.CouponlandSystem;
import utilities.GeneralUtils;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CouponsDBDAO implements CouponsDAO {

    private final ConnectionPool connectionPool;
    private final SQLCommands sqlCommands;
    private final DBUtils dbUtils;

    public CouponsDBDAO() {
        this.connectionPool = CouponlandSystem.getInstance().getConnectionPool();
        this.sqlCommands = CouponlandSystem.getInstance().getLoadedSqlCommands();
        this.dbUtils = new DBUtils(this.connectionPool);
    }

    /**
     * Adds a coupon to DB.
     * @param coupon Coupon instance to be added to DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with the coupon's companyID; if a coupon with the same title already exists with the coupon's companyID.
     */
    @Override
    public void addCoupon(Coupon coupon) throws CLLogicException {
        try {
            dbUtils.runQueryForUpdateCount(sqlCommands.ADD_COUPON,
                    GeneralUtils.getParamsMap(coupon.getCompanyID(), GeneralUtils.getCategoryId(coupon.getCategory()), coupon.getTitle(),
                            coupon.getDescription(), Date.valueOf(coupon.getStartDate()), Date.valueOf(coupon.getEndDate()), coupon.getAmount(),
                            coupon.getPrice(), coupon.getImage()));
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1452 && exception.getMessage().contains("fk_company_id")) {
                throw new CLLogicException("Company " + coupon.getCompanyID() + "was not found", 143);
            } else if (exception.getErrorCode() == 1062 && exception.getMessage().contains("unique coupon name per company")) {
                throw new CLLogicException("Coupon title: " + coupon.getTitle() + " already exists for company " + coupon.getCompanyID(), 151);
            } else {
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
            }
        }
    }

    /**
     * Updates data values for an existing coupon on DB.
     * @param coupon A Coupon instance whose data values to overwrite existing ones on DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id; if the received companyID is different from the existing companyID;
     * if a coupon with the same title already exists with the coupon's companyID.
     */
    @Override
    public void updateCoupon(Coupon coupon) throws CLLogicException {
        try {
            synchronized (CouponlandSystem.getInstance().getLock1()) {
                if (dbUtils.runQueryForUpdateCount(sqlCommands.UPDATE_COUPON,
                        GeneralUtils.getParamsMap(coupon.getCompanyID(), GeneralUtils.getCategoryId(coupon.getCategory()), coupon.getTitle(),
                                coupon.getDescription(), Date.valueOf(coupon.getStartDate()), Date.valueOf(coupon.getEndDate()), coupon.getAmount(),
                                coupon.getPrice(), coupon.getImage(), coupon.getId())) == 0) {
                    throw new CLLogicException("Coupon not found", 141);
                }
            }
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1644 && exception.getMessage().contains("CANNOT CHANGE COUPON`S COMPANY ID")) {
                throw new CLLogicException("Illegal operation - Can not update a coupon's company id", 271);
            } else if (exception.getErrorCode() == 1062 && exception.getMessage().contains("unique coupon name per company")) {
                throw new CLLogicException("Coupon title: " + coupon.getTitle() + " already exists for company " + coupon.getCompanyID(), 151);
            } else {
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
            }
        }
    }

    /**
     * Subtracts 1 from the amount value of the designated coupon on DB.
     * @param couponID Id of the coupon to be processed.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id; if the designated coupon's amount value is already 0;
     */
    @Override
    public void updateCouponPurchase(Integer couponID) throws CLLogicException {
        try {
            synchronized (CouponlandSystem.getInstance().getLock1()) {
                if (dbUtils.runQueryForUpdateCount(sqlCommands.UPDATE_COUPON_PURCHASE,
                        GeneralUtils.getParamsMap(couponID)) == 0) {
                    throw new CLLogicException("Coupon not found", 141);
                }
            }
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1690 && exception.getMessage().contains("value is out of range")) {
                throw new CLLogicException("Coupon " + couponID + " is out of stock", 352);
            } else
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }


    /**
     * Deletes a coupon from DB.
     * @param couponID Id of the coupon to be deleted.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id.
     */
    @Override
    public void deleteCoupon(Integer couponID) throws CLLogicException {
        try {
            synchronized (CouponlandSystem.getInstance().getLock1()) {
                if (dbUtils.runQueryForUpdateCount(sqlCommands.DELETE_COUPON, GeneralUtils.getParamsMap(couponID)) == 0) {
                    throw new CLLogicException("Coupon not found", 141);
                }
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Deletes coupons from DB whose end date has arrived.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained.
     */
    @Override
    public void deleteExpiredCoupons() throws CLLogicException {
        try {
            synchronized (CouponlandSystem.getInstance().getLock1()) {
                dbUtils.runQueryForUpdateCount(sqlCommands.DELETE_EXPIRED_COUPONS, null);
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Returns a list of all coupons found on DB.
     * @return an array list of coupons.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException  if the operation was interrupted; if a connection could not be obtained;
     * if no coupons were found on DB.
     */
    @Override
    public ArrayList<Coupon> getAllCoupons() throws CLInputException, CLLogicException {
        try {
            List<Object> objects = dbUtils.runQueryForResultList(QueryReturnType.COUPON, sqlCommands.GET_ALL_COUPONS);
            if (objects.size() == 0) {
                throw new CLLogicException("No coupons found", 145);
            }
            return (ArrayList<Coupon>) objects.stream().map(object -> (Coupon) (object)).collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Retrieves all coupons found on DB, issued by the designated company.
     * @param companyId Designated company's ID.
     * @return an array list of coupons, issued by the designated company.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * if no coupons were found on DB, issued by the designated company.
     */
    @Override
    public ArrayList<Coupon> getAllCompanyCoupons(Integer companyId) throws CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.COUPON, sqlCommands.GET_COUPONS_BY_COMPANY_ID, GeneralUtils.getParamsMap(companyId));
            if (result.size() == 0) {
                throw new CLLogicException("No coupons found for selected company", 142);
            } else
                return (ArrayList<Coupon>) result.stream().map(object -> (Coupon) (object)).collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Returns a list of all coupons found on DB, purchased by the designated customer.
     * @param customerId Designated customer's ID.
     * @return an array list of coupons, purchased by the designated customer.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * if no coupons were found on DB, purchased by the designated customer.
     */
    @Override
    public ArrayList<Coupon> getAllCustomerCoupons(Integer customerId) throws CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.COUPON, sqlCommands.GET_COUPONS_BY_CUSTOMER_ID, GeneralUtils.getParamsMap(customerId));
            if (result.size() == 0) {
                throw new CLLogicException("No coupons found for selected customer", 147);
            } else
                return (ArrayList<Coupon>) result.stream().map(object -> (Coupon) (object)).collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }


    /**
     * Retrieves coupon data from DB.
     * @param couponID Designated coupon's ID.
     * @return an instance of Coupon with the designated coupon's data.
     * @throws CLInputException if an invalid input was used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id.
     */
    @Override
    public Coupon getOneCoupon(Integer couponID) throws CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.COUPON, sqlCommands.GET_COUPON, GeneralUtils.getParamsMap(couponID));
            if (result.size() == 0) {
                throw new CLLogicException("Coupon not found", 141);
            } else return (Coupon) result.get(0);
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }

    }

    /**
     * Adds a purchase entry.
     * @param customerID Designated customer's ID.
     * @param couponID Designated coupon's ID.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if such purchase already exists on DB.
     */
    @Override
    public void addCouponPurchase(Integer customerID, Integer couponID) throws CLLogicException {
        try {
            dbUtils.runQueryForUpdateCount(sqlCommands.ADD_CVC_ENTRY, GeneralUtils.getParamsMap(customerID, couponID));
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1062 && exception.getMessage().contains("customers_vs_coupons.PRIMARY")) {
                throw new CLLogicException("Coupon " + couponID + " was already purchased by customer " + customerID, 351);
            } else {
                throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
            }
        }
    }

    /**
     * Deletes a purchase entry.
     * @param customerID Designated customer's ID.
     * @param couponID Designated coupon's ID.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no such purchase was found on DB.
     */
    @Override
    public void deleteCouponPurchase(Integer customerID, Integer couponID) throws CLLogicException {
        try {
            Integer result = dbUtils.runQueryForUpdateCount(sqlCommands.DELETE_CVC_ENTRY, GeneralUtils.getParamsMap(customerID, couponID));
            if (result == 0) {
                throw new CLLogicException("Purchase not found", 181);
            }
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }


    /**
     * Retrieves filtered purchase entries from DB.
     * @param filterParams A sequence of pairs of CVC filters and operators.
     * @param queryParams A map containing pairs of indexes and values to be used setting the query's values.
     * @return an array list containing a sequence of pairs of integers - representing filtered purchase entries.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * if no filtered purchase entries were found.
     */
    @Override
    public ArrayList<Integer> getFilteredPurchases(Object[] filterParams, Map<Integer, Object> queryParams) throws
            CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.C_VS_C, sqlCommands.getGetFilteredCommand(QueryReturnType.C_VS_C, filterParams), queryParams);
            if (result.size() == 0) {
                throw new CLLogicException("Purchase not found", 181);
            }
            return (ArrayList<Integer>) result.stream().map(object -> (Integer) (object)).collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

    /**
     * Retrieves filtered coupons from DB.
     * @param filterParams A sequence of pairs of coupons filters and operators.
     * @param queryParams A map containing pairs of indexes and values to be used setting the query's values.
     * @return an array list of filtered coupons.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no filtered coupons were found.
     */
    @Override
    public ArrayList<Coupon> getFilteredCoupons(Object[] filterParams, Map<Integer, Object> queryParams) throws
            CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.COUPON, sqlCommands.getGetFilteredCommand(QueryReturnType.COUPON, filterParams), queryParams);
            if (result.size() == 0) {
                throw new CLLogicException("No coupons found", 145);
            }
            return (ArrayList<Coupon>) result.stream().map(object -> (Coupon) (object)).collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }

    }

    /**
     * Retrieves filtered purchased coupons from DB.
     * @param filterParams A sequence of pairs of coupons filters and operators.
     * @param queryParams A map containing pairs of indexes and values to be used setting the query's values.
     * @return an array list of filtered coupons.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupons were found on DB, purchased by the designated customer
     */
    @Override
    public ArrayList<Coupon> getFilteredPurchasedCoupons(Object[] filterParams, Map<Integer, Object> queryParams) throws CLInputException, CLLogicException {
        try {
            List<Object> result = dbUtils.runQueryForResultList(QueryReturnType.COUPON, sqlCommands.getCVCCouponsFilteredCommand(filterParams), queryParams);
            if (result.size() == 0) {
                throw new CLLogicException("No coupons found", 145);
            }
            return (ArrayList<Coupon>) result.stream().map(object -> (Coupon) (object)).collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
    }

}
