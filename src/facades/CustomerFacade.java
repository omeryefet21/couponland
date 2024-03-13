package facades;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import db.CVCFilter;
import db.CouponFilter;
import exceptions.CLInputException;
import exceptions.CLLogicException;
import local.CouponlandSystem;
import utilities.GeneralUtils;

import java.util.ArrayList;

public class CustomerFacade extends ClientFacade {
    private Integer customerID = null;
    private Boolean lockID = false;

    public CustomerFacade() {
    }

    @Override
    public void setClientId(String email, String password) throws CLInputException, CLLogicException {
        setCustomerID(customersDAO.getOneCustomer(email, password).getId());
    }

    public Integer getCustomerID() {
        return customerID;
    }

    @Override
    public Integer getClientId() {
        return getCustomerID();
    }

    public void setCustomerID(Integer customerID) {
        if (!lockID) {
            this.customerID = customerID;
            lockID = true;
        }
    }

    /**
     * Authenticates any customer log in attempt.
     * @param email Customer email.
     * @param password Customer password.
     * @return true, if a customer exists on DB with the received email and password. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    @Override
    public Boolean login(String email, String password) throws CLInputException, CLLogicException {
        return customersDAO.isCustomerExists(email, password);
    }

    /**
     * Performs a purchase for the Designated coupon by the logged in customer.
     * @param coupon Coupon instance to be purchased.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if such purchase already exists on DB; if no such coupon was found; if the designated coupon's amount value is 0;
     * if the designated coupon's end date has arrived; if the designated coupon's end date if before it's start date.
     */
    public void purchaseCoupon(Coupon coupon) throws CLInputException, CLLogicException {
        if (CouponlandSystem.getInstance().getLogicValidation().UNEXPIRED_COUPON.test(coupon)) {
            if (CouponlandSystem.getInstance().getLogicValidation().IN_STOCK_COUPON.test(coupon)) {
                if (CouponlandSystem.getInstance().getLogicValidation().ACTIVE_COUPON.test(coupon)) {
                    if (coupon.equals(couponsDAO.getOneCoupon(coupon.getId()))) {
                        try {
                            ArrayList<Integer> cvc = couponsDAO.getFilteredPurchases(new Object[]{CVCFilter.COUPON_ID, "=?", CVCFilter.CUSTOMER_ID, "=?"}, GeneralUtils.getParamsMap(coupon.getId(), customerID));
                        } catch (CLLogicException exception) {
                            if (exception.getErrorCode() != 181) {
                                throw exception;
                            } else {
                                couponsDAO.updateCouponPurchase(coupon.getId());
                                couponsDAO.addCouponPurchase(customerID, coupon.getId());
                                coupon.setAmount(coupon.getAmount() - 1);
                                System.out.println("Coupon purchased");
                                return;
                            }
                        }
                        throw new CLLogicException("Coupon " + coupon.getId() + " was already purchased by customer " + getCustomerID(), 351);

                    } else {
                        throw new CLLogicException("Coupon not found", 141);
                    }
                } else {
                    throw new CLLogicException("Coupon " + coupon.getId() + " is inactive", 353);
                }
            } else {
                throw new CLLogicException("Coupon " + coupon.getId() + " is out of stock", 352);
            }
        } else {
            throw new CLLogicException("Coupon " + coupon.getId() + " has expired", 354);
        }
    }


    /**
     * Retrieves all coupons found on DB, purchased by the logged in customer.
     * @return an array list of coupons, purchased by the logged in customer.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * if no coupons were found on DB, purchased by the logged in customer.
     */
    public ArrayList<Coupon> getCustomerCoupons() throws CLInputException, CLLogicException {
        return couponsDAO.getFilteredPurchasedCoupons(null, GeneralUtils.getParamsMap(customerID));
    }

    /**
     * Retrieves all coupons found on DB, purchased by the logged in customer and filtered by category.
     * @param category Category to filter by.
     * @return an array list of coupons, purchased by the logged in customer and filtered by category.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no filtered coupons were found DB, purchased by the logged in customer.
     */
    public ArrayList<Coupon> getCustomerCoupons(Category category) throws CLInputException, CLLogicException {
        return couponsDAO.getFilteredPurchasedCoupons(new Object[]{CouponFilter.CATEGORY_ID, "=?"}, GeneralUtils.getParamsMap(GeneralUtils.getCategoryId(CouponlandSystem.getInstance().getInputValidation().validateCategory(category)), customerID));
    }

    /**
     * Retrieves all coupons found on DB, purchased by the logged in customer and filtered by maximal price.
     * @param maxPrice Maximal price to filter by.
     * @return an array list of coupons, purchased by the logged in customer and filtered by maximal price.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no filtered coupons were found DB, purchased by the logged in customer.
     */
    public ArrayList<Coupon> getCustomerCoupons(Double maxPrice) throws CLInputException, CLLogicException {
        return couponsDAO.getFilteredPurchasedCoupons(new Object[]{CouponFilter.PRICE, "<=?"}, GeneralUtils.getParamsMap(CouponlandSystem.getInstance().getInputValidation().validatePrice(maxPrice), customerID));
    }

    /**
     * Retrieves logged in customer's data and purchased coupons.
     * @return an instance of Customer containing the logged in company's data and issued coupons.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     */
    public Customer getCustomerDetails() throws CLInputException, CLLogicException {
        Customer result = customersDAO.getOneCustomer(customerID);
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
        return "CustomerFacade{" + getClientId() + "}";
    }
}

