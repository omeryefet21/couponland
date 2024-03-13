package dao;

import beans.Coupon;
import exceptions.CLInputException;
import exceptions.CLLogicException;

import java.util.ArrayList;
import java.util.Map;

public interface CouponsDAO {

    /**
     * Adds a coupon to DB.
     * @param coupon Coupon instance to be added to DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with the coupon's companyID; if a coupon with the same title already exists with the coupon's companyID.
     */
    void addCoupon(Coupon coupon) throws CLLogicException;

    /**
     * Updates data values for an existing coupon on DB.
     * @param coupon A Coupon instance whose data values to overwrite existing ones on DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id; if the received companyID is different from the existing companyID;
     * if a coupon with the same title already exists with the coupon's companyID.
     */
    void updateCoupon(Coupon coupon) throws CLLogicException;

    /**
     * Subtracts 1 from the amount value of the designated coupon on DB.
     * @param couponID Id of the coupon to be processed.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id; if the designated coupon's amount value is already 0;
     */
    void updateCouponPurchase(Integer couponID) throws CLLogicException;

    /**
     * Deletes a coupon from DB.
     * @param couponID Id of the coupon to be deleted.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id.
     */
    void deleteCoupon(Integer couponID) throws CLLogicException;

    /**
     * Deletes coupons from DB whose end date has arrived.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained.
     */
    void deleteExpiredCoupons() throws CLLogicException;

    /**
     * Returns a list of all coupons found on DB.
     * @return an array list of coupons.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException  if the operation was interrupted; if a connection could not be obtained;
     * if no coupons were found on DB.
     */
    ArrayList<Coupon> getAllCoupons() throws CLInputException, CLLogicException;

    /**
     * Retrieves all coupons found on DB, issued by the designated company.
     * @param companyId Designated company's ID.
     * @return an array list of coupons, issued by the designated company.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * if no coupons were found on DB, issued by the designated company.
     */
    ArrayList<Coupon> getAllCompanyCoupons(Integer companyId) throws CLInputException, CLLogicException;

    /**
     * Returns a list of all coupons found on DB, purchased by the designated customer.
     * @param customerId Designated customer's ID.
     * @return an array list of coupons, purchased by the designated customer.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * if no coupons were found on DB, purchased by the designated customer.
     */
    ArrayList<Coupon> getAllCustomerCoupons(Integer customerId) throws CLInputException, CLLogicException;

    /**
     * Retrieves coupon data from DB.
     * @param couponID Designated coupon's ID.
     * @return an instance of Coupon with the designated coupon's data.
     * @throws CLInputException if an invalid input was used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id.
     */
    Coupon getOneCoupon(Integer couponID) throws CLInputException, CLLogicException;

    /**
     * Adds a purchase entry.
     * @param customerID Designated customer's ID.
     * @param couponID Designated coupon's ID.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if such purchase already exists on DB.
     */
    void addCouponPurchase(Integer customerID, Integer couponID) throws CLLogicException;

    /**
     * Deletes a purchase entry.
     * @param customerID Designated customer's ID.
     * @param couponID Designated coupon's ID.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no such purchase was found on DB.
     */
    void deleteCouponPurchase(Integer customerID, Integer couponID) throws CLLogicException;

    /**
     * Retrieves filtered purchase entries from DB.
     * @param filterParams A sequence of pairs of CVC filters and operators.
     * @param queryParams A map containing pairs of indexes and values to be used setting the query's values.
     * @return an array list containing a sequence of pairs of integers - representing filtered purchase entries.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * if no filtered purchase entries were found.
     */
    ArrayList<Integer> getFilteredPurchases(Object[] filterParams, Map<Integer, Object> queryParams) throws CLInputException, CLLogicException;

    /**
     * Retrieves filtered coupons from DB.
     * @param filterParams A sequence of pairs of coupons filters and operators.
     * @param queryParams A map containing pairs of indexes and values to be used setting the query's values.
     * @return an array list of filtered coupons.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no filtered coupons were found.
     */
    ArrayList<Coupon> getFilteredCoupons(Object[] filterParams, Map<Integer, Object> queryParams) throws CLInputException, CLLogicException;

    /**
     * Retrieves filtered purchased coupons from DB.
     * @param filterParams A sequence of pairs of coupons filters and operators.
     * @param queryParams A map containing pairs of indexes and values to be used setting the query's values.
     * @return an array list of filtered coupons.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupons were found on DB, purchased by the designated customer
     */
    ArrayList<Coupon> getFilteredPurchasedCoupons(Object[] filterParams, Map<Integer, Object> queryParams) throws CLInputException, CLLogicException;
}
