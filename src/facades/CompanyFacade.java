package facades;

import beans.Category;
import beans.Company;
import beans.Coupon;
import db.CouponFilter;
import exceptions.CLInputException;
import exceptions.CLLogicException;
import local.CouponlandSystem;
import utilities.GeneralUtils;
import java.util.ArrayList;


public class CompanyFacade extends ClientFacade {
    private Integer companyID = null;
    private Boolean lockID = false;

    public CompanyFacade() {
    }
    @Override
    public Integer getClientId() {
        return getCompanyID();
    }
    @Override
    public void setClientId(String email, String password) throws CLInputException, CLLogicException {
        setCompanyID(companiesDAO.getOneCompany(email, password).getId());
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) throws CLLogicException {
        if (!lockID) {
            this.companyID = companyID;
            lockID = true;
        } else {
            throw new CLLogicException("Client id is already set", 191);
        }
    }

    /**
     * Authenticates any company log in attempt.
     * @param email Company email.
     * @param password Company password.
     * @return true, if a company exists on DB with the received email and password. otherwise - false.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     */
    @Override
    public Boolean login(String email, String password) throws CLInputException, CLLogicException {
        return companiesDAO.isCompanyExists(email, password);
    }


    /**
     * Adds a coupon to DB.
     * @param coupon Coupon instance to be added to DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no company was found with the coupon's companyID; if a coupon with the same title already exists with the coupon's companyID;
     * if companyID value of the Coupon instance to be added to DB does not match companyID value of this CompanyFacade.
     */
    public void addCoupon(Coupon coupon) throws CLLogicException {
        if (CouponlandSystem.getInstance().getLogicValidation().OWNED_COUPON.test(companyID, coupon)) {
            couponsDAO.addCoupon(coupon);
            System.out.println("Coupon added");
        } else {
            throw new CLLogicException("Illegal operation - coupon not owned by company",221);
        }
    }

    /**
     * Updates data values for an existing coupon on DB.
     * @param coupon A Coupon instance whose data values to overwrite existing ones on DB.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id; if the received companyID is different from the existing companyID;
     * if a coupon with the same title already exists with the coupon's companyID;
     * if companyID value of the Coupon instance to be added to DB does not match companyID value of this CompanyFacade.
     */
    public void updateCoupon(Coupon coupon) throws CLLogicException {
        if (CouponlandSystem.getInstance().getLogicValidation().OWNED_COUPON.test(companyID, coupon)) {
            couponsDAO.updateCoupon(coupon);
            System.out.println("Coupon updated");
        } else {
            throw new CLLogicException("Illegal operation - coupon not owned by company",221);
        }
    }

    /**
     * Deletes a coupon from DB.
     * @param couponID Id of the coupon to be deleted.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no coupon was found with received id.
     * if companyID value of the Coupon instance to be added to DB does not match companyID value of this CompanyFacade.
     */
    public void deleteCoupon(Integer couponID) throws CLInputException, CLLogicException {
        if (CouponlandSystem.getInstance().getLogicValidation().OWNED_COUPON.test(companyID,
                couponsDAO.getOneCoupon(CouponlandSystem.getInstance().getInputValidation().validateId(couponID)))) {
            couponsDAO.deleteCoupon(couponID);
            System.out.println("Coupon deleted");
        } else {
            throw new CLLogicException("Illegal operation - coupon not owned by company",221);
        }
    }

    /**
     * Retrieves all coupons found on DB, issued by the logged in company.
     * @return an array list of coupons, issued by the logged in company.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * if no coupons were found on DB, issued by the logged in company.
     */
    public ArrayList<Coupon> getCompanyCoupons() throws CLInputException, CLLogicException {
        return couponsDAO.getAllCompanyCoupons(companyID);
    }

    /**
     * Retrieves all coupons found on DB, issued by the logged in company and filtered by category.
     * @param category Category to filter by.
     * @return an array list of coupons, issued by the logged in company and filtered by category.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no filtered coupons were found on DB, issued by the logged in company.
     */
    public ArrayList<Coupon> getCompanyCoupons(Category category) throws CLInputException, CLLogicException {
        return couponsDAO.getFilteredCoupons(new Object[]{CouponFilter.COMPANY_ID, "=?", CouponFilter.CATEGORY_ID, "=?"},
                GeneralUtils.getParamsMap(companyID, GeneralUtils.getCategoryId(CouponlandSystem.getInstance().getInputValidation().validateCategory(category))));
    }

    /**
     * Retrieves all coupons found on DB, issued by the logged in company and filtered by maximal price.
     * @param maxPrice Maximal price to filter by.
     * @return an array list of coupons, issued by the logged in company and filtered by maximal price.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     * if no filtered coupons were found on DB, issued by the logged in company.
     */
    public ArrayList<Coupon> getCompanyCoupons(Double maxPrice) throws CLInputException, CLLogicException {
        return couponsDAO.getFilteredCoupons(new Object[]{CouponFilter.COMPANY_ID, "=?", CouponFilter.PRICE, "<=?"},
                GeneralUtils.getParamsMap(companyID, CouponlandSystem.getInstance().getInputValidation().validatePrice(maxPrice)));
    }

    /**
     * Retrieves logged in company's data and issued coupons.
     * @return an instance of Company containing the logged in company's data and issued coupons.
     * @throws CLInputException if an invalid input was used.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values;
     */
    public Company getCompanyDetails() throws CLInputException, CLLogicException {
        Company result = companiesDAO.getOneCompany(companyID);
        try {
            result.setCoupons(couponsDAO.getAllCompanyCoupons(companyID));
        } catch (CLLogicException exception) {
            if (exception.getErrorCode() != 142) {
                throw exception;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "CompanyFacade{"+getClientId()+"}";
    }
}
